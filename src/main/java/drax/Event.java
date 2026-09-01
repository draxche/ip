package drax;

import java.time.LocalDateTime;

/** A task that takes place between a typed start and end date and time. */
public class Event extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;

    /**
     * Creates an event task with a start and end time.
     *
     * @param task description of the event
     * @param from date and time when the event starts
     * @param to date and time when the event ends
     */
    public Event(String task, LocalDateTime from, LocalDateTime to) {
        super(task);
        this.from = from;
        this.to = to;
    }

    public LocalDateTime getFrom() {
        return this.from;
    }

    public LocalDateTime getTo() {
        return this.to;
    }

    @Override
    public String toString() {
        return String.format("[E][%s] %s (from: %s to: %s)", this.getStatusIcon(), this.task,
                ScheduleDateTime.format(this.from), ScheduleDateTime.format(this.to));
    }
}
