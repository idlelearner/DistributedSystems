README

********* 
Students : 
1. BhagavathiDhass Thirunavukarasu - 5078702 
2. Varun Pandey - 5108436
********

Steps to follow : 
1. Once you are in this folder, run script.sh 
>>sh script.sh 

This will :     
Remove any old log files (server log files and client log file)
Compile the java files to produce the class files 

2. For testing on the same machine     
a. Start the  rmiregistry
>> rmiregistry 1099 &
b. Open 4 tabs     
c. >>java NodeStart node00  --node00 here means node-0,
>>java NodeStart node01     -- This join node01 to the chord
>>java NodeStart node02     -- This join node02 to the chord
>>java NodeStart node03     -- This join node03 to the chord
Here node00,node01,node02,node03 are different names where our processes will bind. 
It has to be different for different process. IMPORTANT : Main node should always be node00

d.  In new terminal now run the client 
>>java Client sample-data.txt <hostname of node-0> 
Note : you can pass any file similar to the format of sample-data.txt
<hostname of node-0> is the hostname of the node-0 to which the client can connect.
Client will insert the words from the mentioned file and ask for user input to enter the word to be searched 
and will give the meaning.

3. For testing on 4 different machines     
a. ssh to the 4 different machines     
b. Copy the folder (remember that step 1 has to be performed before this) to the 4 different machines.     
c. Use the same commands as in step 2, 
machine1>>java NodeStart node00  --node00 here means node-0,
machine2>>java NodeStart node01  -- This join node01 to the chord
machine3>>java NodeStart node02  -- This join node02 to the chord
machine4>>java NodeStart node03  -- This join node03 to the chord
     
4. To get the details of the chord ring, use the below command.
>>java GetRingDetailsClient <hostname of node-0>
This will give details of the finger table, successor and word arrangements. This could be run in between joining the new node to Chord(before java NodeStart)

5. To run the Testcases, run the below command.
>>java TestCasesClient <hostname of node-0>
This has following three testcases, 
1.positive testcase to insert and search for a word.
2.Negative testcase, to search  for the word which is not in dictionary.
3.Testcase to check the total number of items in the system before and after adding items.
