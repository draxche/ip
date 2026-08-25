public class Event extends Task {
    protected String from;
    protected String to;

    public Event(String task, String from, String to) {
        super(task);
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return this.from;
    }

    public String getTo() {
        return this.to;
    }

    @Override
    public String toString() {
        return String.format("[E][%s] %s (from: %s to: %s)", this.getStatusIcon(), this.task, this.from, this.to);
    }
}
