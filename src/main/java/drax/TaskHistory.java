package drax;

import java.util.ArrayList;
import java.util.List;

public class TaskHistory {
    private List<TaskMemento> mementos;

    public TaskHistory() {
        this.mementos = new ArrayList<>();
    }

    public void addMemento(TaskMemento memento) {
        this.mementos.add(memento);
    }

    public TaskMemento getMemento(int index) {
        return this.mementos.get(index);
    }
}
