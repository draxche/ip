package drax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Owns drax.Drax's collection of tasks and exposes only the operations the command loop needs.
 */
public class TaskList implements Iterable<Task> {
    private final ArrayList<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list from tasks loaded by storage.
     *
     * @param tasks tasks to copy into this list
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes the task at the specified zero-based index.
     *
     * @param index index of the task to remove
     */
    public void remove(int index) {
        tasks.remove(index);
    }

    /**
     * Returns the task at the specified zero-based index.
     *
     * @param index index of the task to retrieve
     * @return task at the specified index
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns the number of tasks in this list.
     *
     * @return number of tasks
     */
    public int getSize() {
        return tasks.size();
    }

    /**
     * Checks whether this list contains no tasks.
     *
     * @return true if the list is empty
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Returns an unmodifiable view of the tasks in their current order.
     *
     * @return unmodifiable task list view
     */
    public List<Task> asList() {
        return Collections.unmodifiableList(tasks);
    }

    /**
     * Provides read-only iteration over the current task order.
     *
     * @return an iterator that does not support removing tasks
     */
    @Override
    public Iterator<Task> iterator() {
        return asList().iterator();
    }
}
