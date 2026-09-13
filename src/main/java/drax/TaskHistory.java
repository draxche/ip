package drax;

import java.util.Stack;

/**
 * Stores two stacks of mementos, and undoneMementos
 */
public class TaskHistory {
    private Stack<TaskMemento> mementos;
    private Stack<TaskMemento> undoneMementos;

    /**
     * Creates a TaskHistory object and initializes it with two new stacks
     */
    public TaskHistory() {
        this.mementos = new Stack<>();
        this.undoneMementos = new Stack<>();
    }

    /**
     * Adds a TaskMemento into the history
     * @param memento
     */
    public void addMemento(TaskMemento memento) {
        this.mementos.push(memento);
    }

    /**
     * Clears the redo stack history
     */
    public void clearUndoneMementos() {
        this.undoneMementos.clear();
    }

    /**
     * Retrieves the previous saved snapshot TaskMemento
     * @return a snapshot of the previous TaskMemento state
     */
    public TaskMemento getPreviousMemento() {
        TaskMemento previousMemento = this.mementos.pop();
        this.undoneMementos.push(previousMemento);
        return previousMemento;

    }
}
