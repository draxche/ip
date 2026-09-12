package drax;

public class TaskMemento {
    private final TaskList taskList;

    public TaskMemento(TaskList taskList) {
        this.taskList = taskList;
    }

    public TaskList getSavedContent() {
        return this.taskList;
    }
}
