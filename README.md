# Group Data
![Group Data](/../screenshoots/screenshoots/activitydiagram.png?raw=true "Group Data")

Implementation of Distributed Data Structure (Stack and Set) by using JGroup

## Introduction
1. [What is JGroups?] (http://www.jgroups.org/overview.html)
2. [Getting started with JGroup] (http://www.jgroups.org/api.html)

JGroups is a reliable group communication toolkit written entirely in Java. It is based on IP multicast (TCP also supported), however there are special features such as reliability and group membership. In this project, I implemented a Distributed (replicated) Stack and Set by using JGroups as a messaging tool. These replicated data structures will use state transfer to handle new member join case and use messaging feature to broadcast operation that was recently performed.

## Architecture
The following diagram describes activities that are performed during our replicated datastructures lifecycle.

![Group Data](/../screenshoots/screenshoots/activitydiagram.png?raw=true "Group Data")

**Explanation:**

1. First, the datastructure that just have been created connect to its designated channel. 
2. It then request to get current state from current coordinator.
3. Then it wait for coordinator to send its state (`stream` of collection).
4. if coordinator sent its current state, copy the data inside of it to self's collection. But, if coordinator didn't send anything in some particular time, `timeOut()` is called.
5. `fork()` called, from now on, there are two threads that handle this stack.

*Thread 1 (handle messages that were received):*

6. stand by until `receiveMessage()` is called.
7. Check the received message's command. If it's an add operation, add received data to collection. If it's a remove operation, remove top (stack) / received data (set) from collection.
8. If `finish()` is called, terminate connection to group then end.

*Thread 2 (handle operations performed by method call):*

6. stand by until `add()` or `remove()` is called.
7. Do the called operation (add / remove).
8. Broadcast operation that just have been performed to group
9. If `finish()` is called, terminate connection to group then end.

## Requirement
In order to install and run this project, you will need:

1. Windows/Linux operating system.
2. JRE 7+
3. Netbeans IDE 8+ *(only for compiling)*

This project is made by using Netbeans IDE. I recommended you to use Netbeans IDE 8+ to compile this project.

## Usage
To compile this project, you can open this project directly from Netbeans IDE and Build it.

I also included precompiled binary in `dist` folder. To run, issue the following command:
```
java -jar GroupData.jar
```

## Test and Execution Results
There are some tests that I've performed to this project. Followings are the results:

### Standard Messaging Stack Test
![Test Case 1](/../screenshoots/screenshoots/tc1.JPG?raw=true "Test Case 1")

**Scenario:**

1. First client join the group, so did the second client
2. First client push "Andre" and "Susanto" to stack
3. Second client push "13512028" to stack
4. Second client empty the stack
5. The results are: "13512028", "Susanto", and "Andre"
6. First client empty the stack
7. The result is: Empty stack

### Transfer State Stack Test
![Test Case 2](/../screenshoots/screenshoots/tc2.JPG?raw=true "Test Case 2")

**Scenario:**

1. First client push "Berjaya!", "ITB", "HMIF"
2. Second client push "Buat", "Kita", "Ayo"
3. Third client join the channel
4. Third client empty the stack
5. The result are "Ayo", "Kita", "Buat", "HMIF", "ITB", "Berjaya!"

### Standard Messaging Set Test
![Test Case 3](/../screenshoots/screenshoots/tc3.JPG?raw=true "Test Case 3")

**Scenario:**

1. First client join the group, so did the second client
2. First client add "Andre" and "Susanto" to set
3. Second client add "Keren" to set
4. First client print all the set members
5. The results are: "Keren", "Susanto", and "Andre"
6. First client remove "Susan", the program returned "Not a member"
7. First client remove "Susanto"
8. Second client print all the set members
9. The results are: "Keren", "Andre"

### Transfer State Set Test
![Test Case 4](/../screenshoots/screenshoots/tc4.JPG?raw=true "Test Case 4")

**Scenario:**

1. Third client join the group
2. Third client print all the set members
3. The results are: "Keren", "Andre"
