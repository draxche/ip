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

    public void add(Task task) {
        tasks.add(task);
    }

    public void remove(int index) {
        tasks.remove(index);
    }

    public Task get(int index) {
        return tasks.get(index);
    }

    public int size() {
        return tasks.size();
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

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
