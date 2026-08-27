package drax;

public class Todo extends Task {
    public Todo(String task) {
        super(task);
    }

    public String toString() {
        return String.format("[T][%s] %s", this.getStatusIcon(), this.getTask());
    }
}
