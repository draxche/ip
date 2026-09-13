package drax;

/**
 * Stores a current snapshot of the TaskList
 */
public class TaskMemento {
    private final TaskList taskList;

    /**
     * Creates a new TaskMemento
     * @param taskList the TaskList to be copied as a snapshot
     */
    public TaskMemento(TaskList taskList) {
        this.taskList = new TaskList(taskList.asList());
    }

    /**
     * Extracts the saved TaskList content from the memento
     * @return TaskList a new TaskList containing the same field as the stored snapshot
     */
    public TaskList getSavedContent() {
        return new TaskList(this.taskList.asList());
    }
}
