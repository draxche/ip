package drax;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/** Handles loading, serializing, and saving drax.Drax tasks. */
public class Storage {
    private static final Path SAVE_FILE = Path.of("data", "drax.txt");
    private static final Path TEMP_FILE = Path.of("data", "drax.txt.tmp");

    /**
     * Loads saved tasks and reports malformed records as warnings.
     *
     * @return successfully loaded tasks together with warnings for skipped records
     * @throws IOException if the save file cannot be read
     */
    public LoadResult load() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();
        ArrayList<String> warnings = new ArrayList<>();
        if (!Files.exists(SAVE_FILE)) {
            return new LoadResult(tasks, warnings);
        }
        List<String> lines = Files.readAllLines(SAVE_FILE);
        for (int lineNumber = 0; lineNumber < lines.size(); lineNumber++) {
            String line = lines.get(lineNumber);
            if (line.isBlank()) {
                continue;
            }
            try {
                tasks.add(deserialize(line));
            } catch (IllegalArgumentException e) {
                warnings.add("Saved task on line " + (lineNumber + 1)
                        + " was ignored: " + e.getMessage());
            }
        }
        return new LoadResult(tasks, warnings);
    }

    /**
     * Saves tasks atomically using drax.Drax's established file format.
     *
     * @param tasks tasks to persist in their current order
     * @throws IOException if the temporary or save file cannot be written
     */
    public void save(TaskList tasks) throws IOException {
        Files.createDirectories(SAVE_FILE.getParent());
        try (BufferedWriter writer = Files.newBufferedWriter(TEMP_FILE)) {
            for (Task task : tasks) {
                writer.write(serialize(task));
                writer.newLine();
            }
        }
        replaceSaveFile();
    }

    /**
     * Converts one task to an escaped, pipe-delimited save-file record.
     *
     * @param task task to store
     * @return one save-file line representing the task
     * @throws IllegalArgumentException if the task subtype cannot be stored
     */
    private String serialize(Task task) {
        String done = task.isDone() ? "1" : "0";
        return switch (task) {
            case Deadline deadline -> String.format("D | %s | %s | %s", done,
                    escape(deadline.getTask()), escape(deadline.getDeadline().toString()));
            case Event event -> String.format("E | %s | %s | %s | %s", done,
                    escape(event.getTask()), escape(event.getFrom().toString()), escape(event.getTo().toString()));
            case Todo todo -> String.format("T | %s | %s", done, escape(task.getTask()));
            default -> throw new IllegalArgumentException("Unsupported task type");
        };
    }

    /**
     * Reconstructs a task from one save-file record and restores its completion state.
     *
     * @param line one escaped, pipe-delimited save-file record
     * @return the reconstructed task
     * @throws IllegalArgumentException if the record is malformed or unsupported
     */
    private Task deserialize(String line) {
        List<String> parts = splitFields(line);
        String type = requireValue(parts, 0, "task type");
        String status = requireValue(parts, 1, "completion status");
        if (!status.equals("0") && !status.equals("1")) {
            throw new IllegalArgumentException("completion status must be 0 or 1");
        }
        Task task = switch (type) {
        case "T" -> {
            requireFieldCount(parts, 3);
            yield new Todo(requireValue(parts, 2, "task description"));
        }
        case "D" -> {
            requireFieldCount(parts, 4);
            yield new Deadline(requireValue(parts, 2, "task description"),
                    ScheduleDateTime.parse(requireValue(parts, 3, "deadline")));
        }
        case "E" -> {
            requireFieldCount(parts, 5);
            yield new Event(requireValue(parts, 2, "task description"),
                    ScheduleDateTime.parse(requireValue(parts, 3, "start time")),
                    ScheduleDateTime.parse(requireValue(parts, 4, "end time")));
        }
        default -> throw new IllegalArgumentException("unknown task type " + type);
        };
        if (status.equals("1")) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Escapes field separators and escape characters before a value is written to disk.
     *
     * @param value unescaped field value
     * @return value safe to include in a pipe-delimited record
     */
    private String escape(String value) {
        return value.replace("\\", "\\\\").replace("|", "\\|");
    }

    /**
     * Splits a save-file record into fields while preserving escaped separators.
     *
     * @param line escaped, pipe-delimited save-file record
     * @return the unescaped fields in record order
     */
    private List<String> splitFields(String line) {
        ArrayList<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean escaped = false;
        for (int index = 0; index < line.length(); index++) {
            char character = line.charAt(index);
            if (escaped) {
                if (character == '|' || character == '\\') {
                    field.append(character);
                } else {
                    field.append('\\').append(character);
                }
                escaped = false;
            } else if (character == '\\') {
                escaped = true;
            } else if (character == '|') {
                fields.add(field.toString().trim());
                field.setLength(0);
            } else {
                field.append(character);
            }
        }
        if (escaped) {
            field.append('\\');
        }
        fields.add(field.toString().trim());
        return fields;
    }

    /**
     * Validates that a record contains the field count required for its task type.
     *
     * @param fields fields extracted from a record
     * @param expectedCount required number of fields
     * @throws IllegalArgumentException if the count differs
     */
    private void requireFieldCount(List<String> fields, int expectedCount) {
        if (fields.size() != expectedCount) {
            throw new IllegalArgumentException("expected " + expectedCount + " fields");
        }
    }

    /**
     * Retrieves a required non-blank field from a parsed record.
     *
     * @param fields fields extracted from a record
     * @param index zero-based index of the required field
     * @param fieldName name used in a validation error
     * @return the requested non-blank field value
     * @throws IllegalArgumentException if the field is absent or blank
     */
    private String requireValue(List<String> fields, int index, String fieldName) {
        if (index >= fields.size() || fields.get(index).isBlank()) {
            throw new IllegalArgumentException(fieldName + " is missing");
        }
        return fields.get(index);
    }

    /**
     * Result of loading tasks together with warnings for skipped records.
     *
     * @param tasks successfully reconstructed tasks
     * @param warnings explanations for records that could not be loaded
     */
    public record LoadResult(ArrayList<Task> tasks, ArrayList<String> warnings) {
    }

    /**
     * Replaces the save file with the completed temporary file, preferring an atomic move.
     *
     * @throws IOException if the completed temporary file cannot replace the save file
     */
    private void replaceSaveFile() throws IOException {
        try {
            Files.move(TEMP_FILE, SAVE_FILE, StandardCopyOption.ATOMIC_MOVE,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(TEMP_FILE, SAVE_FILE, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
