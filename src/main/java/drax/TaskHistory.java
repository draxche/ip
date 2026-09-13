package drax;

import java.util.Stack;

/**
 * Stores two stacks of mementos, and undoneMementos
 */
public class TaskHistory {
    private final Stack<TaskMemento> mementos;
    private final Stack<TaskMemento> undoneMementos;

    /**
     * Creates a TaskHistory object and initializes it with two new stacks
     */
    public TaskHistory() {
        this.mementos = new Stack<>();
        this.undoneMementos = new Stack<>();
    }

    /**
     * Adds a TaskMemento into the history and clears the redo stack
     * @param memento the current snapshot to be added
     */
    public void addMemento(TaskMemento memento) {
        clearUndoneMementos();
        this.mementos.push(memento);
    }

    /**
     * Clears the redo stack history
     */
    private void clearUndoneMementos() {
        this.undoneMementos.clear();
    }

    /**
     * Retrieves the previous saved snapshot TaskMemento
     * @return a snapshot of the previous TaskMemento state
     */
    public TaskMemento getPreviousMemento() {
        if (this.mementos.size() <= 1) {
            return null;
        }
        TaskMemento currentMemento = this.mementos.pop();
        TaskMemento previousMemento = this.mementos.peek();
        this.undoneMementos.push(currentMemento);
        return previousMemento;
    }

    /**
     * Retrieves the latest snapshot from the redo stack
     * @return a snapshot of the previous state before undoing
     */
    public TaskMemento getNextMemento() {
        if (this.undoneMementos.isEmpty()) {
            return null;
        }
        TaskMemento nextMemento = this.undoneMementos.pop();
        this.mementos.push(nextMemento);
        return nextMemento;
    }
}
