# Thread communication - Introduction

This application consists of two modules - Thread communication and Process communication.
This Java application is written and compiled using JDK version 8 and tested on JDK 12. Prior versions may not be
supported.
Maven is required to build the project

Detailed documentation for each class/ method / field can be found in-line with the code.

Dependency - Junit 5 (test scope)

## Module.1 Thread communication

This module demonstrates communication between two player instances running in separate threads in the same process.

In this scenario, a strict 'back-and forth' exchange must be enforced. ie, a player should only send a reply after a
message has been received(except for the very first message).
To achieve this, a data structure is needed which has a holding capacity of 1 (or enables direct handoff between
threads) and makes the threads wait for it's turn to read/write, while also being thread safe.
I am using BlockingQueue (which blocks 'read' operation if queue is empty, and blocks 'put' operation if queue is full)
to enforce this behaviour.
Using BlockingQueue helps simplify the code by eliminating the need for synchronized blocks, thread.wait(),
thread.notify etc.

**Implementation of BlockingQueue**

ArrayBlockingQueue of size 1 can be used, but I am using SynchronousQueue because a 'direct handoff' makes sense in this
scenario.
SynchronousQueues have no holding capacity, instead it makes the producer thread wait until a consumer is ready to
receive.
Similarly, a consumer trying to take an item from the queue must wait until producer thread puts the item into the
queue.

I am using two queues- Queue1 will be the 'inbox' of Player1 and also the 'outbox' of Player2, whereas Queue2 will be
the 'outbox' of Player1 and also the 'inbox' of Player2.
Using two separate queues for each stream of communication makes the code much simpler and cleaner.

## Module.2 Process communication (Additional challenge)

This module demonstrates communication between two player instances running inside separate process (instances of JVM).

For process-to-process messaging, I am using WebSockets - because it is the simplest solution using pure Java.
Websockets have an InputStream and an OutputStream which can be used as inbox and outbox.
Sockets implicitly make sure that a 'read' operation has to wait until a 'write' is performed on the other side, and the
I/O streams make sure that the message is 'consumed' as soon as its read (only one read per message).
However, The code has to ensure that a player does not send the next messages without waiting for reply from the other
player.

**Junit with multiple processes**

To allow the test cases to run in separate processes (required in the case of ProcessCommunication) during maven build
operations, we must use a JVM fork in maven-surefire-plugin using the following configuration in POM

```
                <configuration>
                    <forkCount>2C</forkCount>
                    <reuseForks>false</reuseForks>
                </configuration>
```

where 'C' is a placeholder for the number of available CPU cores in the system.

## How to build

Use maven to build the JAR file. This will create [/target/ThreadCommunication-1.0.jar]

## How to run ThreadCommunication after build

Use 'Run_ThreadCommunication.sh' with arguments as given below, or execute the following command in the commandline from
the project root directory.

> java -cp .\target\ThreadCommunication-1.0.jar thread.ThreadCommunication 10 true Hello

     *   args[0] -> stopCondition (int, number of messages that each player should send as well as receive)
     *   args[1] -> verboseLogging (boolean, whether the players should log their actions to the console)
     *   args[2] -> initialMessage (String, the very first message that Initiator will send)
	 *   If args are not supplied, defaults will be used.

## How to run ProcessCommunication

**Important Note -1. Server must run before Client to avoid 'Connection refused' exception. Client must be run using a
second command line instance while Server waits.**

**Important Note -2. the port number supplied to the server and client must be the same for communication to work.**

**Important Note -3. the stopCondition supplied to the server and client must be the same for both to stop gracefully.**

Use 'Run_ProcessCommunicationServer.sh' followed by 'Run_ProcessCommunicationClient.sh' with arguments as given below,
or execute the following commands in the commandline from the project root directory.

> java -cp .\target\ThreadCommunication-1.0.jar process.ProcessCommunicationServer 10 true 88 Hello
>

	 *   args[0] -> stopCondition (int, number of messages that each player should send as well as receive)
     *   args[1] -> verboseLogging (boolean, whether the players should log their actions to the console)
     *   args[2] -> port (int, port number to use)
     *   args[3] -> initialMessage (String, the very first message that the Server will send)
	 *   If args are not supplied, defaults will be used. 

> java -cp .\target\ThreadCommunication-1.0.jar process.ProcessCommunicationClient 10 true 88
>

     *   args[0] -> stopCondition (int, number of messages that each player should send as well as receive)
     *   args[1] -> verboseLogging (boolean, whether the players should log their actions to the console)
     *   args[2] -> port (int, port number to use)
	 *   If args are not supplied, defaults will be used.

PS: The .bat versions of these scripts are also there for Windows OS but '.bat' files are often not allowed in code
packages so they as renamed to '.notbat'
