# UI test session

## Test case 1: Start and exit

**Aim:** Confirm that Drax displays its greeting and exits cleanly when the user says `bye`.

**Command**
```bash
source "$HOME/.sdkman/bin/sdkman-init.sh" && sdk use java 25.0.3.fx-zulu >/dev/null && rm -rf /tmp/drax-ui-test && mkdir -p /tmp/drax-ui-test && javac -d /tmp/drax-ui-test src/main/java/*.java && printf 'bye\n' | java -cp /tmp/drax-ui-test Drax
```

**Console input**
```text
bye
```

**Console output**
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

**Result:** PASS (exit status 0)

## Test case 2: Add and list a task

**Aim:** Confirm that Drax accepts a todo command, reports the new task, and lists it.

**Command**
```bash
source "$HOME/.sdkman/bin/sdkman-init.sh" && sdk use java 25.0.3.fx-zulu >/dev/null && printf 'todo read book\nlist\nbye\n' | java -cp /tmp/drax-ui-test Drax
```

**Console input**
```text
todo read book
list
bye
```

**Console output**
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

**Result:** PASS (exit status 0)

## Test case 3: Mark and unmark a task

**Aim:** Confirm that Drax can mark a task as done, unmark it, and display the updated status.

**Command**
```bash
source "$HOME/.sdkman/bin/sdkman-init.sh" && sdk use java 25.0.3.fx-zulu >/dev/null && printf 'todo read book\nmark 1\nunmark 1\nlist\nbye\n' | java -cp /tmp/drax-ui-test Drax
```

**Console input**
```text
todo read book
mark 1
unmark 1
list
bye
```

**Console output**
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

**Result:** PASS (exit status 0)

## Test case 4: Add all task types

**Aim:** Confirm that Drax accepts todo, deadline, and event tasks and displays each task type correctly.

**Command**
```bash
source "$HOME/.sdkman/bin/sdkman-init.sh" && sdk use java 25.0.3.fx-zulu >/dev/null && printf 'todo read book\ndeadline submit report /by Friday\nevent meeting /from Monday /to Tuesday\nlist\nbye\n' | java -cp /tmp/drax-ui-test Drax
```

**Console input**
```text
todo read book
deadline submit report /by Friday
event meeting /from Monday /to Tuesday
list
bye
```

**Console output**
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
[D][ ] submit report (by: Friday)
Now you have 2 tasks!
I've added this task
[E][ ] meeting (from: Monday to: Tuesday)
Now you have 3 tasks!
Here are the tasks in your list!
1.[T][ ] read book
2.[D][ ] submit report (by: Friday)
3.[E][ ] meeting (from: Monday to: Tuesday)
Goodbye. Hope to see you again soon!

```

**Result:** PASS (exit status 0)

## Test case 5: Handle invalid input

**Aim:** Confirm that Drax reports invalid task descriptions, missing scheduling information, invalid task numbers, and non-numeric task numbers without exiting unexpectedly.

**Command**
```bash
source "$HOME/.sdkman/bin/sdkman-init.sh" && sdk use java 25.0.3.fx-zulu >/dev/null && printf 'todo \ndeadline submit report\nevent meeting\nmark 1\nmark abc\nbye\n' | java -cp /tmp/drax-ui-test Drax
```

**Console input**
```text
todo 
deadline submit report
event meeting
mark 1
mark abc
bye
```

**Console output**
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
This task doesn't exist. You don't have that many tasks!
Please enter a valid number!
Goodbye. Hope to see you again soon!

```

**Result:** PASS (exit status 0)
