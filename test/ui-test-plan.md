# UI test plan

The runner executes these cases in order and compares combined standard output and standard error exactly. Each case is independent: the command must compile or start the program as needed.

### Test case 1: Start and exit

*Aim*
Confirm that drax.Drax displays its greeting and exits cleanly when the user says `bye`.

*Command*
```bash
source "$HOME/.sdkman/bin/sdkman-init.sh" && sdk use java 25.0.3.fx-zulu >/dev/null && rm -rf /tmp/drax-ui-test /tmp/drax-ui-test-work && mkdir -p /tmp/drax-ui-test /tmp/drax-ui-test-work && javac -d /tmp/drax-ui-test src/main/java/drax/*.java && (cd /tmp/drax-ui-test-work && printf 'bye\n' | java -cp /tmp/drax-ui-test drax.Drax)
```

*Input*
```text
bye
```

*Expected output*
```text
██████╗ ██████╗  █████╗ ██╗  ██╗
██╔══██╗██╔══██╗██╔══██╗╚██╗██╔╝
██║  ██║██████╔╝███████║ ╚███╔╝
██║  ██║██╔══██╗██╔══██║ ██╔██╗
██████╔╝██║  ██║██║  ██║██╔╝ ██╗
╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝

Infinite Salutations! I'm Drax!
What's on your mind today?
Goodbye. Hope to see you again soon!
```

### Test case 2: Add and list a task

*Aim*
Confirm that drax.Drax accepts a todo command, reports the new task, and lists it.

*Command*
```bash
source "$HOME/.sdkman/bin/sdkman-init.sh" && sdk use java 25.0.3.fx-zulu >/dev/null && rm -rf /tmp/drax-ui-test-work && mkdir -p /tmp/drax-ui-test-work && (cd /tmp/drax-ui-test-work && printf 'todo read book\nlist\nbye\n' | java -cp /tmp/drax-ui-test drax.Drax)
```

*Input*
```text
todo read book
list
bye
```

*Expected output*
```text
██████╗ ██████╗  █████╗ ██╗  ██╗
██╔══██╗██╔══██╗██╔══██╗╚██╗██╔╝
██║  ██║██████╔╝███████║ ╚███╔╝
██║  ██║██╔══██╗██╔══██║ ██╔██╗
██████╔╝██║  ██║██║  ██║██╔╝ ██╗
╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝

Infinite Salutations! I'm Drax!
What's on your mind today?
I've added this task
[T][ ] read book
Now you have 1 task!
Here are the tasks in your list!
1.[T][ ] read book
Goodbye. Hope to see you again soon!
```

### Test case 3: Mark and unmark a task

*Aim*
Confirm that drax.Drax can mark a task as done, unmark it, and display the updated status.

*Command*
```bash
source "$HOME/.sdkman/bin/sdkman-init.sh" && sdk use java 25.0.3.fx-zulu >/dev/null && rm -rf /tmp/drax-ui-test-work && mkdir -p /tmp/drax-ui-test-work && (cd /tmp/drax-ui-test-work && printf 'todo read book\nmark 1\nunmark 1\nlist\nbye\n' | java -cp /tmp/drax-ui-test drax.Drax)
```

*Input*
```text
todo read book
mark 1
unmark 1
list
bye
```

*Expected output*
```text
██████╗ ██████╗  █████╗ ██╗  ██╗
██╔══██╗██╔══██╗██╔══██╗╚██╗██╔╝
██║  ██║██████╔╝███████║ ╚███╔╝
██║  ██║██╔══██╗██╔══██║ ██╔██╗
██████╔╝██║  ██║██║  ██║██╔╝ ██╗
╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝

Infinite Salutations! I'm Drax!
What's on your mind today?
I've added this task
[T][ ] read book
Now you have 1 task!
I've marked this task as done:
[T][X] read book
I've marked this task as not done:
[T][ ] read book
Here are the tasks in your list!
1.[T][ ] read book
Goodbye. Hope to see you again soon!
```

### Test case 4: Add all task types

*Aim*
Confirm that drax.Drax parses typed deadline and event dates, then displays them in a readable format.

*Command*
```bash
source "$HOME/.sdkman/bin/sdkman-init.sh" && sdk use java 25.0.3.fx-zulu >/dev/null && rm -rf /tmp/drax-ui-test-work && mkdir -p /tmp/drax-ui-test-work && (cd /tmp/drax-ui-test-work && printf 'todo read book\ndeadline submit report /by 2/12/2019 1800\nevent meeting /from 2019-12-03T09:00 /to 2019-12-03T10:30\nlist\nbye\n' | java -cp /tmp/drax-ui-test drax.Drax)
```

*Input*
```text
todo read book
deadline submit report /by 2/12/2019 1800
event meeting /from 2019-12-03T09:00 /to 2019-12-03T10:30
list
bye
```

*Expected output*
```text
██████╗ ██████╗  █████╗ ██╗  ██╗
██╔══██╗██╔══██╗██╔══██╗╚██╗██╔╝
██║  ██║██████╔╝███████║ ╚███╔╝
██║  ██║██╔══██╗██╔══██║ ██╔██╗
██████╔╝██║  ██║██║  ██║██╔╝ ██╗
╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝

Infinite Salutations! I'm Drax!
What's on your mind today?
I've added this task
[T][ ] read book
Now you have 1 task!
I've added this task
[D][ ] submit report (by: Dec 02 2019 6:00 PM)
Now you have 2 tasks!
I've added this task
[E][ ] meeting (from: Dec 03 2019 9:00 AM to: Dec 03 2019 10:30 AM)
Now you have 3 tasks!
Here are the tasks in your list!
1.[T][ ] read book
2.[D][ ] submit report (by: Dec 02 2019 6:00 PM)
3.[E][ ] meeting (from: Dec 03 2019 9:00 AM to: Dec 03 2019 10:30 AM)
Goodbye. Hope to see you again soon!
```

### Test case 5: Handle invalid input

*Aim*
Confirm that drax.Drax reports invalid task descriptions, missing scheduling information, invalid task numbers, and non-numeric task numbers without exiting unexpectedly.

*Command*
```bash
source "$HOME/.sdkman/bin/sdkman-init.sh" && sdk use java 25.0.3.fx-zulu >/dev/null && rm -rf /tmp/drax-ui-test-work && mkdir -p /tmp/drax-ui-test-work && (cd /tmp/drax-ui-test-work && printf 'todo \ndeadline submit report\nevent meeting\ndeadline submit report /by next Friday\nmark 1\nmark abc\nbye\n' | java -cp /tmp/drax-ui-test drax.Drax)
```

*Input*
```text
todo 
deadline submit report
event meeting
deadline submit report /by next Friday
mark 1
mark abc
bye
```

*Expected output*
```text
██████╗ ██████╗  █████╗ ██╗  ██╗
██╔══██╗██╔══██╗██╔══██╗╚██╗██╔╝
██║  ██║██████╔╝███████║ ╚███╔╝
██║  ██║██╔══██╗██╔══██║ ██╔██╗
██████╔╝██║  ██║██║  ██║██╔╝ ██╗
╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝

Infinite Salutations! I'm Drax!
What's on your mind today?
You didn't provide a task!?
You didn't provide a end date! Use /by [deadline]
You didn't provide when this event is happening! Use /from [date] /to [date]
Please use a valid date and time: yyyy-MM-dd, yyyy-MM-ddTHH:mm, or d/M/yyyy HHmm.
This task doesn't exist. You don't have that many tasks!
Please enter a valid number!
Goodbye. Hope to see you again soon!
```

### Test case 6: Save changed tasks

*Aim*
Confirm that adding, completing, and deleting tasks writes the current list to `data/drax.txt`.

*Command*
```bash
source "$HOME/.sdkman/bin/sdkman-init.sh" && sdk use java 25.0.3.fx-zulu >/dev/null && rm -rf /tmp/drax-ui-test /tmp/drax-ui-test-work && mkdir -p /tmp/drax-ui-test /tmp/drax-ui-test-work && javac -d /tmp/drax-ui-test src/main/java/drax/*.java && (cd /tmp/drax-ui-test-work && printf 'todo read book\ndeadline submit report /by 2019-12-02\nmark 1\ndelete 2\nbye\n' | java -cp /tmp/drax-ui-test drax.Drax >/dev/null && cat data/drax.txt)
```

*Input*
```text
ignored
```

*Expected output*
```text
T | 1 | read book
```

### Test case 7: Load saved tasks on startup

*Aim*
Confirm that drax.Drax reconstructs all task types and their completion status when it starts again.

*Command*
```bash
source "$HOME/.sdkman/bin/sdkman-init.sh" && sdk use java 25.0.3.fx-zulu >/dev/null && rm -rf /tmp/drax-ui-test /tmp/drax-ui-test-work && mkdir -p /tmp/drax-ui-test /tmp/drax-ui-test-work && javac -d /tmp/drax-ui-test src/main/java/drax/*.java && (cd /tmp/drax-ui-test-work && printf 'todo read book\nmark 1\ndeadline submit report /by 2019-12-02\nevent project meeting /from 2/12/2019 0900 /to 2/12/2019 1000\nbye\n' | java -cp /tmp/drax-ui-test drax.Drax >/dev/null && printf 'list\nbye\n' | java -cp /tmp/drax-ui-test drax.Drax)
```

*Input*
```text
ignored
```

*Expected output*
```text
██████╗ ██████╗  █████╗ ██╗  ██╗
██╔══██╗██╔══██╗██╔══██╗╚██╗██╔╝
██║  ██║██████╔╝███████║ ╚███╔╝
██║  ██║██╔══██╗██╔══██║ ██╔██╗
██████╔╝██║  ██║██║  ██║██╔╝ ██╗
╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝

Infinite Salutations! I'm Drax!
What's on your mind today?
Here are the tasks in your list!
1.[T][X] read book
2.[D][ ] submit report (by: Dec 02 2019)
3.[E][ ] project meeting (from: Dec 02 2019 9:00 AM to: Dec 02 2019 10:00 AM)
Goodbye. Hope to see you again soon!
```

### Test case 8: Skip malformed saved records

*Aim*
Confirm that blank and malformed records do not stop valid saved tasks from loading.

*Command*
```bash
source "$HOME/.sdkman/bin/sdkman-init.sh" && sdk use java 25.0.3.fx-zulu >/dev/null && rm -rf /tmp/drax-ui-test /tmp/drax-ui-test-work && mkdir -p /tmp/drax-ui-test /tmp/drax-ui-test-work/data && javac -d /tmp/drax-ui-test src/main/java/drax/*.java && (cd /tmp/drax-ui-test-work && printf '%s\n' 'T | 1 | valid task' 'Q | 0 | bad task' 'D | 2 | invalid status | Friday' 'E | 0 | missing end | Monday' '' > data/drax.txt && printf 'list\nbye\n' | java -cp /tmp/drax-ui-test drax.Drax)
```

*Input*
```text
ignored
```

*Expected output*
```text
██████╗ ██████╗  █████╗ ██╗  ██╗
██╔══██╗██╔══██╗██╔══██╗╚██╗██╔╝
██║  ██║██████╔╝███████║ ╚███╔╝
██║  ██║██╔══██╗██╔══██║ ██╔██╗
██████╔╝██║  ██║██║  ██║██╔╝ ██╗
╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝

Infinite Salutations! I'm Drax!
What's on your mind today?
Saved task on line 2 was ignored: unknown task type Q
Saved task on line 3 was ignored: completion status must be 0 or 1
Saved task on line 4 was ignored: expected 5 fields
Here are the tasks in your list!
1.[T][X] valid task
Goodbye. Hope to see you again soon!
```

### Test case 9: Preserve delimiters in saved task text

*Aim*
Confirm that pipes and backslashes in task text survive a save-and-restart cycle.

*Command*
```bash
source "$HOME/.sdkman/bin/sdkman-init.sh" && sdk use java 25.0.3.fx-zulu >/dev/null && rm -rf /tmp/drax-ui-test /tmp/drax-ui-test-work && mkdir -p /tmp/drax-ui-test /tmp/drax-ui-test-work && javac -d /tmp/drax-ui-test src/main/java/drax/*.java && (cd /tmp/drax-ui-test-work && printf '%s\n' 'todo revise A | B\C' 'bye' | java -cp /tmp/drax-ui-test drax.Drax >/dev/null && printf 'list\nbye\n' | java -cp /tmp/drax-ui-test drax.Drax)
```

*Input*
```text
ignored
```

*Expected output*
```text
██████╗ ██████╗  █████╗ ██╗  ██╗
██╔══██╗██╔══██╗██╔══██╗╚██╗██╔╝
██║  ██║██████╔╝███████║ ╚███╔╝
██║  ██║██╔══██╗██╔══██║ ██╔██╗
██████╔╝██║  ██║██║  ██║██╔╝ ██╗
╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝

Infinite Salutations! I'm Drax!
What's on your mind today?
Here are the tasks in your list!
1.[T][ ] revise A | B\C
Goodbye. Hope to see you again soon!
```

### Test case 10: Continue after a save failure

*Aim*
Confirm that drax.Drax reports a write failure without crashing when the data directory cannot be created.

*Command*
```bash
source "$HOME/.sdkman/bin/sdkman-init.sh" && sdk use java 25.0.3.fx-zulu >/dev/null && rm -rf /tmp/drax-ui-test /tmp/drax-ui-test-work && mkdir -p /tmp/drax-ui-test /tmp/drax-ui-test-work && javac -d /tmp/drax-ui-test src/main/java/drax/*.java && (cd /tmp/drax-ui-test-work && printf 'not a directory' > data && printf 'todo read book\nlist\nbye\n' | java -cp /tmp/drax-ui-test drax.Drax)
```

*Input*
```text
ignored
```

*Expected output*
```text
██████╗ ██████╗  █████╗ ██╗  ██╗
██╔══██╗██╔══██╗██╔══██╗╚██╗██╔╝
██║  ██║██████╔╝███████║ ╚███╔╝
██║  ██║██╔══██╗██╔══██║ ██╔██╗
██████╔╝██║  ██║██║  ██║██╔╝ ██╗
╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝

Infinite Salutations! I'm Drax!
What's on your mind today?
Sorry! I could not save your tasks. They are available until you exit the program.
I've added this task
[T][ ] read book
Now you have 1 task!
Here are the tasks in your list!
1.[T][ ] read book
Goodbye. Hope to see you again soon!
```

### Test case 11: Find matching tasks

*Aim*
Confirm that drax.Drax lists only tasks whose descriptions contain the supplied keyword.

*Command*
```bash
source "$HOME/.sdkman/bin/sdkman-init.sh" && sdk use java 25.0.3.fx-zulu >/dev/null && rm -rf /tmp/drax-ui-test /tmp/drax-ui-test-work && mkdir -p /tmp/drax-ui-test /tmp/drax-ui-test-work && javac -d /tmp/drax-ui-test src/main/java/drax/*.java && (cd /tmp/drax-ui-test-work && printf 'todo read book\ntodo return book\ntodo buy groceries\nfind book\nbye\n' | java -cp /tmp/drax-ui-test drax.Drax)
```

*Input*
```text
ignored
```

*Expected output*
```text
██████╗ ██████╗  █████╗ ██╗  ██╗
██╔══██╗██╔══██╗██╔══██╗╚██╗██╔╝
██║  ██║██████╔╝███████║ ╚███╔╝
██║  ██║██╔══██╗██╔══██║ ██╔██╗
██████╔╝██║  ██║██║  ██║██╔╝ ██╗
╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝

Infinite Salutations! I'm Drax!
What's on your mind today?
I've added this task
[T][ ] read book
Now you have 1 task!
I've added this task
[T][ ] return book
Now you have 2 tasks!
I've added this task
[T][ ] buy groceries
Now you have 3 tasks!
Here are the matching tasks in your list:
1.[T][ ] read book
2.[T][ ] return book
Goodbye. Hope to see you again soon!
```

### Test case 12: Package the FXML-based JavaFX interface

*Aim*
Confirm that the runnable JAR contains the JavaFX entry points, controllers, FXML views, and avatar resources.

*Command*
```bash
source "$HOME/.sdkman/bin/sdkman-init.sh" && sdk use java 25.0.3.fx-zulu >/dev/null && ./gradlew shadowJar >/dev/null && jar tf build/libs/duke.jar | grep -E '^(drax/(Launcher|Main|MainWindow|DialogBox)\.class|images/(DaUser|DaDrax)\.png|view/(MainWindow|DialogBox)\.fxml)$' | sort
```

*Input*
```text

```

*Expected output*
```text
drax/DialogBox.class
drax/Launcher.class
drax/Main.class
drax/MainWindow.class
images/DaDrax.png
images/DaUser.png
view/DialogBox.fxml
view/MainWindow.fxml
```

### Test case 13: Return responses for the graphical interface

*Aim*
Confirm that one Drax instance returns command responses and preserves its task state without printing a console banner.

*Command*
```bash
source "$HOME/.sdkman/bin/sdkman-init.sh" && sdk use java 25.0.3.fx-zulu >/dev/null && rm -rf /tmp/drax-gui-test /tmp/drax-gui-test-work && mkdir -p /tmp/drax-gui-test /tmp/drax-gui-test-work && javac -d /tmp/drax-gui-test src/main/java/drax/*.java && printf '%s\n' 'import drax.Drax;' 'public class GuiResponseProbe {' '    public static void main(String[] args) {' '        Drax drax = new Drax();' '        System.out.println(drax.greet());' '        System.out.println(drax.getResponse("todo read book"));' '        System.out.println(drax.getResponse("list"));' '    }' '}' > /tmp/drax-gui-test/GuiResponseProbe.java && javac -cp /tmp/drax-gui-test -d /tmp/drax-gui-test /tmp/drax-gui-test/GuiResponseProbe.java && (cd /tmp/drax-gui-test-work && java -cp /tmp/drax-gui-test GuiResponseProbe)
```

*Input*
```text

```

*Expected output*
```text
Infinite Salutations! I'm Drax!
What's on your mind today?
I've added this task
[T][ ] read book
Now you have 1 task!
Here are the tasks in your list!
1.[T][ ] read book
```
