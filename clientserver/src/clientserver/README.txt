to Compile cd into this folder
>> javac *.java

To run clientA
>>java Server <portNo>
>>java clientA <hostname> <portno>

ServerLogFile.txt will be created in the same directory as Server file

Stop the server(This is required because logger has stopped in server)
Start the server again.

To run clientB
>>java Server <portNo>
>>java clientA <hostname> <portno> <transferthreadcount> <iterationcount>

ServerLogFile.txt and clientLogFile.txt will be created in the same directory

Server Requires
---------------
Account.java
BankOperations.java
FileWriter.java
Parameter.java
Request.java
Server.java
ServerLogger.java

Client Requires
---------------
Account.java
ClientA.java
ClientB.java
ClientLogger.java
FileWriter.java
Parameter.java
Request.java
TransferClient.java
