import java.time.LocalDateTime;

/** A task that must be completed by a specific date and time. */
public class Deadline extends Task {
    protected LocalDateTime deadline;

    public Deadline(String task, LocalDateTime deadline) {
        super(task);
        this.deadline = deadline;
    }

    /** Returns the typed deadline so callers can compare it chronologically. */
    public LocalDateTime getDeadline() {
        return this.deadline;
    }

    @Override
    public String toString() {
        return String.format("[D][%s] %s (by: %s)", this.getStatusIcon(), this.task,
                ScheduleDateTime.format(this.deadline));
    }
}
