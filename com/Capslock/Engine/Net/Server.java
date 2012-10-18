package com.Capslock.Engine.Net;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

import com.Capslock.Engine.Debug.Debug;

/*
 * The server class acts as a means of allowing clients to connect via the "Connection" class.
 * 
 * On creation the user specifies the maximum number of simultaneous connections to the server.
 * this number is then used to set up an array of connections.
 * 
 * the server has a loop which accepts connections and stores them in an array while there is still space for open connections remaining.
 * when a client attempts a connection the following events occur:
 * 
 * 		The server compares the OpenConnections variable to the maximum connections variable to determine if we have space.
 * 		
 * if we do not have space the following events occur:
 * 	
 * 		The server periodically checks to see if we have any connections in the array which are actually disconnected.
 * 		If we do, the server removes them from the array, removes them from the queue should the exist, and decrements the OpenConnections variable by 1.
 * 
 * if the connection is accepted following events occur:
 * 
 * 		The connection is added to the array (at the next available space).
 * 		The connection is added to the connection queue.
 * 		The OpenConnections variable is incremented by 1.
 * 
 * 
 * All connections are opened by the ServerListener class and all disconnections are performed with a method in the Server class.
 * However, the vast majority of disconnections are executed in the ConnectionTest class
 * 
 * The server class requires the following fields:
 * 
 * 		
 * 		Boolean running			-		tells the server whether to stop listening. once it is stopped it cannot be restarted.
 * 		Int maximumconnections	-		specified by the user to determine the maximum number of connection that can be open at once.
 * 		Int OpenConnections		-		keeps track of how many currently "open" connections we have.
 * 										This number may be more than the number of actually open connections as the server class may not have detected that the connections
 * 										are actually disconnected yet.
 * 
 * 		Connection[] Connections-		This is the array of connections that the server stores all open connections in.
 * 										If a disconnect occurs, on detection of it the connection is replaced by a null and the OpenConnections variable is changed
 * 		
 * All methods relating to the store of connection objects are in the Server class. The methods include:
 * 
 * 		Disconnect:
 * 
 * 			Connection is replaced by null.
 * 			Connection is removed from queue.
 * 			OpenConnections Decremented (by 1).
 * 
 * 		Connect:
 * 
 * 			Connection is stored in next available slot in array.
 * 			Connection is added to queue.
 * 			OpenConnections Incremented (by 1).
 * 
 * these methods will have helper methods for adding and removing connections to the array and queue.
 */

public class Server {
	
	/*
	 * This is the server socket that the ServerListener thread will have control over in order to listen for connections.
	 * since there exception handling is required when opening the socket, the ServerLitstener thread will initialise the variable
	 * as opposed to the constructor of this class.
	 */
	protected ServerSocket MainSocket;
	
	/*
	 * The port number of the socket can be specified by the user in the constructor. if it is not specified, the portnumber of 9999 is used.
	 * This portnumber (9999) will be the standard port used by the engine if a different port is not specified.
	 */
	protected int PortNumber = Connection.DEFAULT_PORT_NUMBER;
	
	/*
	 * The maximum number of clients that can be connected at once, and hence the size of the array that stores them
	 */
	protected int MaxConnections;
	
	/*
	 * the array of connections. the size of which is governed by the maxConnections.
	 */
	protected Connection[] Connections;
	
	/*
	 * The queue in which all open connections are stored.
	 */
	protected LinkedList<Connection> ConnectionsQueue;
	
	
	/*
	 * The number of currently open connections.
	 */
	protected int OpenConnections;
	
	/*
	 * This variable notifies the threads used in this class of when it is appropriate to run.
	 * once set to false it cannot be set to true again, and is only accessible through the "Stop" methods which sets it to false.
	 */
	protected boolean Running;
	
	/*
	 * These are the objects which will be the runners of the threads, one of which listens for connections and one of which will check for broken connections.
	 */
	protected ServerListener Listener;
	protected ConnectionTester Tester;
	
	/*
	 * The constructor starts the Server Listener thread along with initialising all of the variables necessary for 
	 */
	public Server(int MaxConnections){
		startServer(MaxConnections);
	}
	
	public Server(int MaxConnections, int PortNumber){
		startServer(MaxConnections);
		this.PortNumber = PortNumber;
	}
	
	/*
	 * this method exists so as to allow for the expansion in functionality of this class later, where the functionality would be to specify a time when you
	 * wanted the server to start as opposed to having it automatically start on creation of the class. as of yet this functionality is not required.
	 */
	private void startServer(int MaxConnections){
		this.MaxConnections = MaxConnections;
		this.OpenConnections = 0;
		this.Running = true;
		this.Connections = new Connection[MaxConnections];
		this.ConnectionsQueue = new LinkedList<Connection>();
		
		this.Listener = new ServerListener();
		this.Tester = new ConnectionTester();
		
		new Thread(Listener).start();
	}
	
	/*
	 * The following methods relate to the user of this class retrieving the Connections from it.
	 * 
	 * The "getConnection(int)" method allows the user to get a connection from the array.
	 * If the connection exists it is returned.
	 * If the connection does not exist a null is returned.
	 * if the arraylocation being accessed is out of bounds a null is returned.
	 * 
	 * the "getNextConnection()" retrieves the next connection from the queue.
	 * if there is no more connections in the queue a null value is returned.
	 */
	
	public Connection getConnection(int ArrayLocation){
		if(ArrayLocation>=0 && ArrayLocation<MaxConnections){
			return Connections[ArrayLocation];
		}
		
		return null;
	}
	
	public Connection getNextConnection(){
		return ConnectionsQueue.poll();
	}
	
	/*
	 * This method is useful for the ServerListener thread which is required to know if there are any currently available spaces to accept connections in to.
	 * Because this method des not do any realtime checking of connections the serverListener is then required to call upon the ConnectionTester thread to actually check the
	 * status of connections.
	 */
	public boolean spaceRemaining(){
		return OpenConnections<MaxConnections;
	}
	
	/*
	 * this method stops the server by closing the server MainSocket and changing the running variable to false (which prevents the threads from running)
	 * It leaves the current connection in the array and queue should anyone want to use them, however, it cannot be guaranteed that these connections are still open.
	 */
	public void stopServer(){
		Running = false;
		
		try{
			MainSocket.close();
		}catch(Exception e){}		
	}
	
	/*
	 * The "isRunning" method simply returns the running status of the server so as to indicate whether the server is able to recieve new connections.
	 */
	
	public boolean isRunning(){
		return Running;
	}
	
	/*
	 * This method starts the ConnectionTester thread which iterates over the connections to check that they are still open.
	 * Details on the operations involved in testing the Connections can be found in the ConnectionTester class.
	 * 
	 * the ConnectionTester class has a variable names TesterRunning. this allows this method to detect whether an instance of that class
	 * is already running and to avoid starting a new instance if it is.
	 */
	
	public void testConnections(){
		if(!Tester.isRunning()){
			new Thread(Tester).start();
		}else{
			Debug.println("SERVER", "A CONNECTIONTESTER THREAD IS ALREADY RUNNING. TESTCONNECTION DID NOT RUN A NEW THREAD.");
		}
	}
	
	/*
	 * Sometimes the user requires to know the maximum number of possible connections to the server. this get method provides that functionality.
	 */
	
	public int getMaxConnections(){
		return MaxConnections;
	}
	
	/*
	 * All private methods relating to the adding or removal of connections to the array and queue.
	 */
	
	/*
	 * The newConnection method creates a connection and adds it to the required arrays.
	 * 
	 * it does the following things:
	 * 		
	 * 		Creates a new Connection object
	 * 		Increments the OpenConnections variable by 1
	 * 		adds the Connection to the array and queue
	 * 		
	 */
	private void newConnection(Socket newSocket){
		OpenConnections++;
		
		Connection C = new Connection(newSocket);
		
		Connections[getNextArrayLocation()] = C;
		ConnectionsQueue.add(C);
		
		Debug.println("SERVER", "SERVER HAS ACCEPTED CONNECTION FROM ADDRESS "+C.getRemoteIP().getHostAddress());
	}
	
	private int getNextArrayLocation(){
		for(int i = 0; i<MaxConnections; i++){
			if(Connections[i] == null){
				return i;
			}
		}
		
		return 0;
	}
	
	/*
	 * The destroy connection method clears a broken/closed connection from array/queue.
	 * 
	 * it does the following things:
	 * 
	 * 		Decrements the OpenConnections variable by 1
	 * 		Removes the connection from the queue should it exist there
	 * 		Removes the connection from the array
	 */
	
	private void destroyConnection(int ArrayLocation){
		OpenConnections--;
		
		ConnectionsQueue.remove(Connections[ArrayLocation]);
		Connections[ArrayLocation] = null;
	}
	
	
	
	
	private class ServerListener implements Runnable{

		
		/*
		 * This class acts as a new thread with a loop which accepts connections if we have available space.
		 * 
		 * first this class initialises the MainSocket in the server class. If the socket cannot be initialised correctly, this class will simply stop the server from running
		 * and it will not be able to be restarted.
		 * 
		 * if there is not available space the server starts a new thread which iterates over the connections testing them all.
		 * the comments on the Server class give an overview on what events occur "if we do not have space". 
		 * This class calls a method called testConnection in the Se3rver class which in turn starts the thread named ConnectionTester.
		 * Details on the events can be found in the ConnectionTest class.
		 * 
		 * if there is an error we stop the server and exit the loop.
		 * 
		 */
		
		/*
		 * If the maximum number of connections have been reached the server will check all of the connections to ensure that they are still connected.
		 * When this happens there is a small wait time before the next accept/check is made. This integer specifies the number of milliseconds of wait time before the
		 * server listener continues.
		 */
		
		private static final int WAIT_TIME = 500;
		

		@Override
		public void run() {
			if(!Running)return;
			
			try{
				MainSocket = new ServerSocket(PortNumber);
			}catch(Exception e){
				Debug.println("SERVER", "SERVERLISTENER FAILED TO INITIALISE SERVERSOCKET. SERVER HAS BEEN STOPPED.");
				Debug.printst(e);
				stopServer();
				return;
			}
			
			/*
			 * the main loop which accepts all of the connections should we have space to.
			 */
			
			while(Running){
				
				//if the maximum number of connections had not been reached
				if(spaceRemaining()){
					try{
						//accepting a new connection. the accept method simply waits until a new client tries to connect
						newConnection(MainSocket.accept());
					}catch(Exception e){
						//there was an error in the server socket, so we stop the server and exit the loop
						Debug.println("SERVER", "SERVERLISTENER FAILED TO CONNECT CLIENT IN MAIN LOOP. SERVER HAS BEEN STOPPED.");
						stopServer();
						return;
					}
				}
				//there is not enough room in the array because the max connections has been reached.
				else{
					testConnections();
					try{
						Thread.sleep(WAIT_TIME);
					}catch(Exception e){}
				}
				
			}
			
		}
	}
	
	private class ConnectionTester implements Runnable{

		/*
		 * This class iterates over the Connections array in the Server class and test each connection/
		 * 
		 * If the Connection is disconnected/broken this class calls the "destroyConnection" method which does the following:
		 * 
		 * 		Remove the Connection form the queue (should it exist)
		 * 		Remove the Connection from the array and replace it will null
		 * 		Decrement the OpenConnections variable by 1
		 * 
		 * so as not to create multiple instances of this class as a thread, it has a "testerRunning" variable. this allows the testConnection method
		 * in the server class to detect whether this is running and prevent it from running multiple times, as this would be unnecessary
		 * 
		 * This class contains a for loop that iterates over all of the members of the Connections array. For each member it calls the testConnection method.
		 * 
		 * If the connection is open the following occurs:
		 * 
		 * 		Nothing
		 * 
		 * if the connection is closed the following occurs:
		 * 
		 * 		the "destroyConnection" method is run on the connection.
		 */
		
		private boolean TesterRunning = false;
		
		public void run() {
			if(!Running)return;
			
			TesterRunning = true;
			
			for(int i = 0; i<MaxConnections; i++){
				if(Connections[i]==null)continue;
				
				//if the connection is not open
				if(!Connections[i].isConnected()){
					destroyConnection(i);
				}
			}
			
			TesterRunning = false;				
		}
		
		public boolean isRunning(){
			return TesterRunning;
		}
		
	}

}
