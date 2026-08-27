import java.time.LocalDateTime;

/** A task that takes place between a typed start and end date and time. */
public class Event extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;

    public Event(String task, LocalDateTime from, LocalDateTime to) {
        super(task);
        this.from = from;
        this.to = to;
    }

    /** Returns the typed event start time. */
    public LocalDateTime getFrom() {
        return this.from;
    }

    /** Returns the typed event end time. */
    public LocalDateTime getTo() {
        return this.to;
    }

    @Override
    public String toString() {
        return String.format("[E][%s] %s (from: %s to: %s)", this.getStatusIcon(), this.task,
                ScheduleDateTime.format(this.from), ScheduleDateTime.format(this.to));
    }
}
