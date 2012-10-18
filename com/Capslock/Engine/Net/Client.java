package com.Capslock.Engine.Net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;

import com.Capslock.Engine.Debug.Debug;

/*
 * The Client class is used to set up a connection which connects to a remote server.
 * 
 * Some information is required in order to initialise the server. this information is as follows:
 * 
 * 		PortNumber
 * 		IP Address
 * 		Timeout (when connecting)
 * 		Connection status
 * 
 * The connection status is the status of the connection just after it has attempted to connect. This variable will not be updated along with the connection
 * and as a result, will be out of date as soon as the result is first returned.
 * 
 * The attempt to connect is ran in a separate thread so as to allow the game to continue running. as a result, should the game want to wait for the result of a connection,
 * it must constantly monitor the state of the socket. as a result the best way to implement a waiting functionality within the user's application is to have a while loop
 * which sleeps until the connection state has been determined:
 * 
 * 		while(c.getConnectionStatus() == Client.NOT_YET_CONNECTED){
 * 			<sleep for a short time>
 * 		}
 * 
 * 		<the client's socket's state has been determined. therefore take action depending on whether it connected successfully or not>
 * 
 * in order for a user of the client class to get information as to whether a successful connection has been made, the "getConnectionStatus" method must be used.
 * This method will return the following in the situations specified:
 * 
 * 		NOT_YET_CONNECTED - when the socket is still trying to make the connection and the connection status cannot yet be determined.
 * 		SUCCESSFULLY_CONNECTED - when the socket has successfully made a connection.
 * 		UNSUCCESSFULLY_CONNECTED - when the socket has failed to make a connection.
 * 
 * The client attempts a connection as soon as the constructor is called
 */

public class Client {
	
	protected Connection MainConnection;
	
	protected int PortNumber = Connection.DEFAULT_PORT_NUMBER;
	
	protected String IPAddress;
	
	protected int Timeout = 5000;
	
	/*
	 * Enums for the states of the connections. the connection is always "not_yet_connected" until the connecting thread has finished,
	 * after which it will be set to successfully connected, or unsuccessfully connected.
	 */
	
	public static final byte NOT_YET_CONNECTED = 0;
	public static final byte SUCCESSFULLY_CONNECTED = 1;
	public static final byte UNSUCCESSFULLY_CONNECTED = 2;
	
	protected byte ConnectionStatus = NOT_YET_CONNECTED;
	
	/*
	 * since the timeout and port are not necessarily required and the client can make assumptions about portnumbers and timeouts
	 * on the part of the user, we have multiple constructors.
	 */
	
	public Client(String IPAddress, int PortNumber, int Timeout){
		setup(IPAddress, PortNumber, Timeout);
	}
	
	public Client(String IPAddress, int PortNumber){
		setup(IPAddress, PortNumber, this.Timeout);
	}
	
	public Client(String IPAddress){
		setup(IPAddress, this.PortNumber, this.Timeout);
	}
	
	private void setup(String IP, int Port, int Timeout){
		this.IPAddress = IP;
		this.PortNumber = Port;
		this.Timeout = Timeout;
	}
	
	/*
	 * this method is the method called by the user when they wish for the client to attempt to connect. this method will only run if the client has never attempted a connection before.
	 */
	public void connect(){
		if(ConnectionStatus!=NOT_YET_CONNECTED)return;
		
		new Thread(new Connecter()).start();
	}
	
	/*
	 * This method returns the actual connection which the client class created.
	 * if the connection does not exist because the socket did not connect successfully a null is returned.
	 */
	
	public Connection getConnection(){
		if(ConnectionStatus == SUCCESSFULLY_CONNECTED){
			return MainConnection;
		}
		
		return null;
	}
	
	/*
	 * This method returns the status of the connection.
	 * the connection states are defined by some public static final bytes along with the rest of the field declarations.
	 */
	
	public byte getConnectionStatus(){
		return ConnectionStatus;	
	}
	
	/*
	 * this is the thread which attempts to connect to the remote server
	 */
	private class Connecter implements Runnable{
		
		/*
		 * This thread does a number of tasks in the following order:
		 * 
		 * 		Creates a new socket
		 * 		(if successfully connected) 	creates a new connection to store in MainConnection using the successfully connected socket.
		 * 										sets the ConnectionStatus to successful.
		 * 
		 * 		(if unsuccessfully connected)	sets the ConnectionStatus to unsuccessful
		 */
		
		Socket s;
		
		public void run(){
			SocketAddress SocketAddr = new InetSocketAddress(IPAddress, PortNumber);
			s = new Socket();
			
			Debug.println("CLIENT", "CONNECTER THREAD IS ATTEMPTING TO CONNECT TO  " + SocketAddr.toString());
			
			try{
				s.connect(SocketAddr, Timeout);
			}
			catch(SocketTimeoutException se){
				Debug.println("CLIENT", "SOCKET TIMED OUT WHILE CONNECTION IN CONNECTOR THREAD. CLIENT WAS NOT SUCCESSFULLY CONNECTED.");
				fail();
				return;
			}
			catch(IOException ie){
				Debug.println("CLIENT", "SOCKET FAILED TO CONNECT IN CONNECTOR THREAD. CLIENT WAS NOT SUCCESSFULLY CONNECTED.");
				fail();
				return;
			}
			catch(Exception e){
				Debug.println("CLIENT", "SOCKET FAILED TO CONNECT FOR UNKNOWN REASON IN CONNECTOR THREAD. CLIENT WAS NOT SUCCESSFULLY CONNECTED.");
				fail();
				return;
			}
			
			MainConnection = new Connection(s);
			
			ConnectionStatus = SUCCESSFULLY_CONNECTED;
			
			Debug.println("CLIENT", "CONNECTER THREAD SUCCESSFULLY CONNECTED TO  " + SocketAddr.toString());
		}
		
		public void fail(){
			try{
				s.close();
			}catch(Exception e){}
			ConnectionStatus = UNSUCCESSFULLY_CONNECTED;
		}
		
	}

}
