*********
Students :

1. BhagvathiDhass - 
2. Varun Pandey - 5108436

********

Steps to follow :

1. Once you are in this folder, run script.sh
>>sh script.sh
This will :
	Remove any old log files (server log files and client log file)
	Compile the java files to produce the class files

2. For testing on the same machine

	a. Open 4 tabs, 3 for servers and 1 for client
	b. >>java Server Config.txt <server_id>   -- This is for the 3 servers, where server_id = (1,2,3)
	c. >>java Client Config.txt		  -- This is for the client
	d. The port configurations are present in the file Config.txt

3. For testing on 4 different machines

	a. ssh to the 4 different machines (3 for servers and one for client)
	b. Copy the folder (remember that step 1 has to be performed before this) to the 4 different machines.
	c. Use the config file Config1.txt and run the same commands as for the same machine
	d. The 4 machines used must match the ones mentioned in the config files. We tested this using the cse machines and maybe you could use the same

3. Each server process will have a server log file named as  :  serverLogFile_<server_id>.txt

4. Client has a log file named as clientLogFile.txt

5. Also note that the final balances in the 10 accounts are printed on the console. They add up to 10 * 1000 = 10,000 which the total amount to begin with

6. Also the average response time is printed onto the console

7. Kill the server processes.
