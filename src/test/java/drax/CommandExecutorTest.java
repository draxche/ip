package drax;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommandExecutorTest {
    private TaskList tasks;
    private RecordingStorage storage;
    private CommandExecutor executor;

    @BeforeEach
    public void setUp() {
        tasks = new TaskList();
        storage = new RecordingStorage();
        executor = new CommandExecutor(tasks, storage);
    }

    @Test
    public void executeUndo_withoutEarlierState_returnsBoundaryMessage() {
        CommandExecutor.ExecutionResult result = executeUndo();

        assertEquals("There's nothin' to undo!", result.response());
        assertEquals(CommandExecutor.Outcome.CONTINUE, result.outcome());
        assertTrue(tasks.isEmpty());
        assertEquals(0, storage.getSaveCount());
    }

    @Test
    public void executeRedo_withoutUndoneState_returnsBoundaryMessage() {
        CommandExecutor.ExecutionResult result = executeRedo();

        assertEquals("There's nothin' to redo!", result.response());
        assertEquals(CommandExecutor.Outcome.CONTINUE, result.outcome());
        assertTrue(tasks.isEmpty());
        assertEquals(0, storage.getSaveCount());
    }

    @Test
    public void executeUndoAndRedo_fiveTodos_restoresEachListState() {
        for (int taskNumber = 1; taskNumber <= 5; taskNumber++) {
            executeTodo("task " + taskNumber);
        }

        for (int expectedSize = 4; expectedSize >= 0; expectedSize--) {
            executeUndo();

            assertEquals(expectedSize, tasks.getSize(),
                    "Undo should restore a list with " + expectedSize + " tasks");
            if (expectedSize > 0) {
                assertEquals("task " + expectedSize, tasks.get(expectedSize - 1).getTask(),
                        "Undo should restore the correct previous task");
            }
        }

        for (int expectedSize = 1; expectedSize <= 5; expectedSize++) {
            executeRedo();

            assertEquals(expectedSize, tasks.getSize(),
                    "Redo should restore a list with " + expectedSize + " tasks");
            assertEquals("task " + expectedSize, tasks.get(expectedSize - 1).getTask(),
                    "Redo should restore the correct next task");
        }
    }

    @Test
    public void executeUndoAndRedo_deadline_restoresDeadlineDetails() {
        executeDeadline("B", "2019-12-02T18:00");

        executeUndo();
        assertTrue(tasks.isEmpty(), "Undo should remove the deadline");

        executeRedo();
        assertInstanceOf(Deadline.class, tasks.get(0));
        assertTaskDisplays("[D][ ] B (by: Dec 02 2019 6:00 PM)");
    }

    @Test
    public void executeUndoAndRedo_event_restoresEventDetails() {
        executeEvent("C", "2019-12-03T09:00", "2019-12-03T10:30");

        executeUndo();
        assertTrue(tasks.isEmpty(), "Undo should remove the event");

        executeRedo();
        assertInstanceOf(Event.class, tasks.get(0));
        assertTaskDisplays("[E][ ] C (from: Dec 03 2019 9:00 AM to: Dec 03 2019 10:30 AM)");
    }

    @Test
    public void executeUndoAndRedo_mark_restoresCompletionState() {
        executeTodo("A");
        executeMark("1");

        executeUndo();
        assertFalse(tasks.get(0).isDone(), "Undoing mark should restore the incomplete state");

        executeRedo();
        assertTrue(tasks.get(0).isDone(), "Redoing mark should restore the completed state");
    }

    @Test
    public void executeUndoAndRedo_unmark_restoresCompletionState() {
        executeTodo("A");
        executeMark("1");
        executeUnmark("1");

        executeUndo();
        assertTrue(tasks.get(0).isDone(), "Undoing unmark should restore the completed state");

        executeRedo();
        assertFalse(tasks.get(0).isDone(), "Redoing unmark should restore the incomplete state");
    }

    @Test
    public void executeUndoAndRedo_delete_restoresDeletedTaskAndOrder() {
        executeTodo("A");
        executeTodo("B");
        executeTodo("C");
        executeDelete("2");

        assertTaskDisplays("[T][ ] A", "[T][ ] C");

        executeUndo();
        assertTaskDisplays("[T][ ] A", "[T][ ] B", "[T][ ] C");

        executeRedo();
        assertTaskDisplays("[T][ ] A", "[T][ ] C");
    }

    @Test
    public void executeUndoAndRedo_successfulTransition_savesRestoredState() {
        executeTodo("A");
        int savesBeforeUndo = storage.getSaveCount();

        executeUndo();
        assertEquals(savesBeforeUndo + 1, storage.getSaveCount());
        assertTrue(storage.getLastSavedTasks().isEmpty());

        executeRedo();
        assertEquals(savesBeforeUndo + 2, storage.getSaveCount());
        assertEquals(getTaskDisplays(tasks), getTaskDisplays(storage.getLastSavedTasks()));
    }

    @Test
    public void executeNewCommand_afterUndo_discardsRedoHistory() {
        executeTodo("original");
        executeUndo();

        executeTodo("replacement");
        CommandExecutor.ExecutionResult redoResult = executeRedo();

        assertEquals("There's nothin' to redo!", redoResult.response(),
                "Executing a new command should clear the redo stack");
        assertTaskDisplays("[T][ ] replacement");
    }

    @Test
    public void executeInvalidMutation_afterSuccessfulMutation_doesNotAddHistoryState() {
        executeTodo("A");
        CommandExecutor.ExecutionResult invalidResult = executeMark("2");

        CommandExecutor.ExecutionResult undoResult = executeUndo();

        assertEquals("This task doesn't exist. You don't have that many tasks!", invalidResult.response(),
                "Executing mark 2 shouldn't be allowed as there is only one task in the list");
        assertEquals("Your last command was undone!\n"
                + "Oops! You currently have no tasks.\n"
                + "Here are the tasks in your list!", undoResult.response(),
                "Executing undo should remove todo A");
        assertTrue(tasks.isEmpty(), "TaskList should be empty by the end of execution");
    }

    @Test
    public void executeUndoAndRedo_whenSaveFails_restoresStateAndReportsFailure() {
        storage.setShouldFail(true);
        executeTodo("A");

        CommandExecutor.ExecutionResult undoResult = executeUndo();
        CommandExecutor.ExecutionResult redoResult = executeRedo();

        String warning = "Sorry! I couldn't save your tasks :(. They are available till you exit the program!";
        assertTrue(undoResult.response().startsWith(warning + "\nYour last command was undone!"));
        assertTrue(redoResult.response().startsWith(warning + "\nYour last command was redone!"));
        assertTaskDisplays("[T][ ] A");
    }

    private CommandExecutor.ExecutionResult executeTodo(String description) {
        return execute(new Parser.Command(Parser.Type.TODO, "", description, "", ""));
    }

    private CommandExecutor.ExecutionResult executeDeadline(String description, String deadline) {
        return execute(new Parser.Command(Parser.Type.DEADLINE, "", description, "", deadline));
    }

    private CommandExecutor.ExecutionResult executeEvent(String description, String start, String end) {
        return execute(new Parser.Command(Parser.Type.EVENT, "", description, start, end));
    }

    private CommandExecutor.ExecutionResult executeMark(String taskNumber) {
        return execute(new Parser.Command(Parser.Type.MARK, taskNumber, "", "", ""));
    }

    private CommandExecutor.ExecutionResult executeUnmark(String taskNumber) {
        return execute(new Parser.Command(Parser.Type.UNMARK, taskNumber, "", "", ""));
    }

    private CommandExecutor.ExecutionResult executeDelete(String taskNumber) {
        return execute(new Parser.Command(Parser.Type.DELETE, taskNumber, "", "", ""));
    }

    private CommandExecutor.ExecutionResult executeUndo() {
        return execute(new Parser.Command(Parser.Type.UNDO, "", "", "", ""));
    }

    private CommandExecutor.ExecutionResult executeRedo() {
        return execute(new Parser.Command(Parser.Type.REDO, "", "", "", ""));
    }

    private CommandExecutor.ExecutionResult execute(Parser.Command command) {
        return executor.execute(command);
    }

    private void assertTaskDisplays(String... expectedDisplays) {
        assertEquals(List.of(expectedDisplays), getTaskDisplays(tasks));
    }

    private static List<String> getTaskDisplays(TaskList taskList) {
        return taskList.asList().stream()
                .map(Task::toString)
                .toList();
    }

    /** Records saved state in memory so unit tests never modify the user's save file. */
    private static class RecordingStorage extends Storage {
        private TaskList lastSavedTasks;
        private int saveCount;
        private boolean shouldFail;

        @Override
        public void save(TaskList tasks) throws IOException {
            saveCount++;
            if (shouldFail) {
                throw new IOException("Simulated write failure");
            }
            this.lastSavedTasks = new TaskList(tasks.asList());
        }

        public TaskList getLastSavedTasks() {
            return this.lastSavedTasks;
        }

        public int getSaveCount() {
            return this.saveCount;
        }

        public void setShouldFail(boolean shouldFail) {
            this.shouldFail = shouldFail;
        }
    }
}
