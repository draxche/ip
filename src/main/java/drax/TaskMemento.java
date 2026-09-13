package drax;

/**
 * Stores an immutable current snapshot of the TaskList
 */
public class TaskMemento {
    private final TaskList taskList;

    public TaskMemento(TaskList taskList) {
        this.taskList = taskList;
    }

    /**
     * Extracts the saved TaskList content from the memento
     * @return TaskList the saved TaskList content
     */
    public TaskList getSavedContent() {
        return this.taskList;
    }
}
