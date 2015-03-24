##############################################
Assignment 2
PartB
Team Members:
        Name : BhagavathiDhass Thirunavukarasu
        Student ID : 5078702

        Name : Varun Pandey
        Student ID : 5108436
###############################################

Please follow the following steps to set up and test Part B:

1. Open one terminal, use this terminal to start up the server

2. Compile all java files using:
javac *.java

3. Start up the rmiregistry at port 2015 (say)
rmiregistry 2015&

4. Start the server
java -Djava.security.policy=mySecurityPolicyfile AccountServerImpl 2015
You will see a server up message on the terminal

5. Open another terminal, use this to test the client and see the output

6. Run ClientA using the command:
java -Djava.security.policy=mySecurityPolicyfile ClientA localhost 2015

7. Run ClientB using the command:
java -Djava.security.policy=mySecurityPolicyfile ClientB localhost 1099 5 6

8. Shut down the server

9. Undo rmiregistry by brining the rmirgistry process to the foreground
% fg

10. Kill the rmiregistry process
