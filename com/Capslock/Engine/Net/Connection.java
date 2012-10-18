package com.Capslock.Engine.Net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;

import com.Capslock.Engine.Debug.Debug;

/*
 * The connection class handles a socket after it has been connected.
 * This class must be provided with a socket which has already opened it's connection in order to work properly.
 * 
 * there are two classes that control the connection of clients and servers before passing the socket to this class:
 * 
 * 		the Server class handles incoming connections from clients, and stores connection objects in a queue/list after passing the sockets to the respective connection objects.
 * 		The Client class handles requesting a connection to a server, after making a successful connection the client simply stores this connection in a variables.
 * 
 * The connection class is a "wrapper" for the BaseConnection class.
 * 
 * This class handles the higher level input and output of data after it being initially processed by the BaseConnection class.
 * This high level management could involve the construction of packets, and the pre-validation of data before it is put in to the input/output streams.
 * 
 */

public class Connection {
	
	/*
	 * The following are some default values used by the connection such as the default port number and IP address. this is useful as if the port number
	 * used by the engine should be changed, it only needs to be changed in one place.
	 */
	public static final int DEFAULT_PORT_NUMBER = 9999;

	
	protected BaseConnection MainConnection;
	
	protected LinkedList<NetworkPacket> Buffer = new LinkedList<NetworkPacket>();
	
	/*
	 * The buffer size is the number of NetworkPackets that will be read in to the "Buffer" variable at any one time.
	 * it is there purely so as to avoid the Buffer list becoming unmanageably big. essentially, it passes most of the buffer control on
	 * to the internal workings of the socket, so that if the buffered data in the socket becomes too large it has the option simply to stop accepting data.
	 */
	
	protected int BufferSize = 500;
	
	/*
	 * The ConnectionTestPeriod variable is used by the "ConnectionTester" class. it is the time between each test packet that is sent along the socket.
	 * the time is in milliseconds.
	 */
	
	protected int ConnectionTestPeriod = 3000;

	
	public Connection(Socket socket){
		MainConnection = new BaseConnection(socket);
		
		/*
		 * Here we are starting a new Reader thread for reading the data in to our BaseConnection in to our Buffer.
		 * We also start a ConnectionTester thread which periodically tests our connection by writing a test data packet to it with a period specified by "ConnectionTestPeriod".
		 */
		new Thread(new Reader()).start();
		new Thread(new ConnectionTester()).start();
	}
	
	/*
	 * Wrapper methods for all of the public functionality in BaseConnection
	 */
	
	/*
	 * Since the BaseConnection throws an exception when no data is present and this class certainly shouldn't, iut simply returns null
	 */
	public NetworkPacket readPacket(){
			return Buffer.poll();
	}
	
	public boolean writePacket(NetworkPacket PacketofData){
		boolean result = MainConnection.writeObject(PacketofData);
		MainConnection.flushOutput();
		
		return result;
	}
	
	public void disconnect(){
		MainConnection.disconnect();
	}
	
	public boolean isConnected(){
		return MainConnection.isConnected();
	}
	
	public boolean testConnection(){
		return MainConnection.testConnection();
	}
	
	public int getLocalPort(){
		return MainConnection.getLocalPort();
	}
	
	public int getRemotePort(){
		return MainConnection.getRemotePort();
	}
	
	public InetAddress getRemoteIP(){
		return MainConnection.getRemoteIP();
	}
	
	/*
	 * This method is used by the reader class to internally load the data from the readObject method in the BaseConnection in to
	 * the LinkedList where all of the data is stored.
	 */
	
	private void readData(){
		Buffer.add((NetworkPacket) MainConnection.readObject());
	}
	
	
	private class BaseConnection{
		
		/*
		 * this protected inner class manages all low level functionality of the socket.
		 * The class is only really inner so as to allow it to protected; if it were a separate class it would be accessible
		 * to all other classes as it would require the public modifier.
		 * 
		 * This class handles the following functions.
		 * 
		 * Input from the socket.
		 * Output to the socket.
		 * Testing the connection to the other socket.
		 * Handling disconnects from the socket.
		 * 
		 * This class must be constructed with a valid open socket.
		 * The valid open socket is passed to this class from the parent Connection class which is, in turn, passed the socket from the server or client classes.
		 */
		
		
		/*
		 * The socket is passed down to this class from the connection class. if the socket disconnects it cannot be re-opened, and a new connection class must be made in order to reopen it.
		 */
		protected Socket MainSocket;
		
		/*
		 * this class contains a BufferedReader for handling received data and a PrintStream for handling outgoing data.
		 * The BufferedReader and PrintStream are kept constantly in sync with the socket. This means that should the socket be disconnected,
		 * reader and stream will be set to null variables.
		 */
		protected ObjectInputStream Reader;
		protected ObjectOutputStream Writer;
		
		/*
		 * This class is required to keep a number of pieces of data relating to the socket.
		 * 
		 * The most important is the "connected" boolean which represents whether the socket is open or closed.
		 * When the Connection class is consulted regarding the state of the socket, if the connected boolean is false it simply returns false.
		 * if the connected boolean is set to true it first tests the connection before returning true.
		 * 
		 * The connected boolean is initially set to true so that when we initially test the connection in the constructor, when isConnected is called we
		 * have the opportunity to actually test the connection. see the comments at the isConnected and testConnection methods for more details.
		 * 
		 * There are other pieces of data regarding the connection. One of which is the port number. this piece of data is not strictly necessary
		 * in most cases as the socket will be connected prior to reaching this class. However, the port number is resolved simply for the purposes of
		 * having it available should it be required. The port number is instantiated on creation because if we fail to connect we will not have the opportunity to instantiate it.
		 * as a result we set it before starting so that if it is ever requested if the socket failed to open we still have a value to return.
		 */
		protected int LocalPortNumber = -1;
		
		protected int RemotePortNumber = -1;
		protected InetAddress RemoteIP = null;
		
		protected boolean Connected = true;
		
		public BaseConnection(Socket MainSocket){
			this.MainSocket = MainSocket;
			
			
			/*
			 * Here we must use a try and catch statement to ensure we do not throw any unhandled exceptions.
			 * If there are any exceptions it is clear that the socket was not connected properly or that we have another internal problem.
			 * as a result we simply disconnect.
			 */
			try{
				Writer = new ObjectOutputStream(MainSocket.getOutputStream());				
				Reader = new ObjectInputStream(MainSocket.getInputStream());
				
			}catch(Exception e){
				disconnect(false);
				Debug.println("BASECONNECTION", "FAILED TO INSTANTIATE INPUT/OUTPUT CHANNELS ON SOCKET. DISCONNECTED.");
				Debug.printst(e);
				return;
			}
			
			/*
			 * if the connection is not actually valid and isConnection returns calse we do not actually have to do anything.
			 * this is because on finding out that we are not connected, a function will be called to set the connection variable to false,
			 * and ensure all of the streams and socket are closed.
			 */
			if(!testConnection()){return;}
			
			//Simply updating the port and InetAddress variables associated with the connection.
			updateInfo();
			
		}
		
		private void updateInfo(){
			LocalPortNumber = MainSocket.getLocalPort();
			RemotePortNumber = MainSocket.getPort();
			
			RemoteIP = MainSocket.getInetAddress();
		}
		
		/*
		 * The following methods relate to input/output of data to the socket.
		 * 
		 * 
		 * after consulting the java docs it can be concluding that the reader and writer for the socket operates in the following manner:
		 * 
		 * Reader:
		 * 
		 * 	*if disconnected an IOexception is thrown on reading.
		 * 	*if there is no more data in the stream a "-1" character for the Read() method is returned or an empty string for the readLine() method.
		 * 			
		 * 			*(FALSE)if there is no data an IOexception is thrown on reading, however, 	*we can use the available() method in the socket to detect whether there is any more data.
		 * 																						*we can use the ready() method in the BufferedReader to detect whether there is any more data.
		 * 
		 * Writer:
		 * 
		 * 	*if disconnected an IOexception is thrown on writing.
		 * 
		 * 
		 * as a result the read and write methods will behave in the following way:
		 * 
		 * Read:
		 * 
		 * 	*if no more data exists, an empty string is returned.
		 * 	*if the the socket is disconnected an IOexception is thrown.
		 * 
		 * 
		 * Write:
		 * 	
		 * 	*the write method will return a boolean. if the boolean returned is true then the data was written to the socket.
		 * 	 If it was not written successfully the boolean will be false.
		 * 
		 */
		
		/*
		 * Since the "read" must differentiate between valuable data and connection-testing data it must be written as a loop or recursive function. since we should only have to check the connection once
		 * we are going to deligate the actual reading and checking of the data to a separate private method
		 * This method will block until there is data available to be read. for this reason the connection class runs a thread which reads data from
		 * this method in to a queue. 
		 */
		
		public Object readObject(){
			if(!isConnected())return null;
			
			try{
				return readObject_Loop();
			}catch(Exception e){
				disconnect(false);
				Debug.println("BASECONNECTION", "FAILED TO READLINE. DISCONNED AND RETURNED IOEXCEPTION.");
				Debug.printst(e);
			}
			
			return null;
		}
		
		private Object readObject_Loop() throws Exception{
			Object o;
			
			/*
			 * Here we start a loop which iterates over the data coming in from the network. if it finds packets of test data it simply discards them and continues to wait
			 * for more data. once it finds a piece of valid data it returns it. If an IOException )or exception of any kind) occurs, we simply disconnect and print the debug output.
			 * 
			 * if the reader finds a disconnect packet then we disconnect and return null.
			 */
			
			while(true){
				o = Reader.readObject();
				
				if(o instanceof TestPacket){
					continue;
				}
				else if(o instanceof DisconnectPacket){
					disconnect(false);
					return null;
				}
				
				return o;
			}
		}
		
		/*
		 * The write method does not throw exceptions. instead it handles them, and then simply returns false indicating that there was an
		 * error while sending the data and disconnects the socket.
		 */
		
		public boolean writeObject(Object Data){
			if(!isConnected())return false;
			
			try{
				Writer.writeObject(Data);
				flushOutput();
			}catch(Exception e){
				disconnect(false);
				Debug.println("BASECONNECTION", "FAILED TO WRITE TO SOCKET. DISCONNECTED AND RETURNED FALSE.");
				Debug.printst(e);
				return false;
			}
			
			return true;
		}
		
		/*
		 * The isConnected method simply returns the current Connected status of the socket. Since this is not guaranteed to be up to date
		 * if the user wants a current status they ought to call the testConnection() method which checks the connection before returning the answer.
		 */
		
		public boolean isConnected(){
			return Connected;
		}
		
		/*
		 * The testConnection method tests the connection by sending the "connection test data" over the socket. If the socket is not connected
		 * an IOException will be thrown. If it is thrown the socket is disconnected and the false boolean is returned.
		 * If the data sends successfully then the true boolean is returned.
		 */
		
		/*
		 * When this class tests the connection a piece of data is sent across the socket. this data is used when testing the connection so that when the test data reaches the recipient, they know
		 * simply to discard it. it is kept private as no other classes should require to know it because they should never know of the internal workings of the
		 * connection testing functionality.
		 */
		
		public boolean testConnection(){
			if(Connected==false)return false;
			
			try{
				Writer.writeObject(new TestPacket());
				flushOutput();

			}catch(Exception e){
				disconnect(false);
				Debug.println("BASECONNECTION", "FAILED TO WRITE TEST DATA TO SOCKET. DISCONNED AND RETURNED FALSE.");
				Debug.printst(e);
				return false;
			}
			
			return true;
		}
		
		/*
		 * The flush output method is required to ensure that all of the data has been sent through the socket instead of sitting in the buffer.
		 */
		
		public boolean flushOutput(){
			try {
				Writer.flush();
			} catch (IOException e) {
				disconnect(false);
				Debug.println("BASECONNECTION", "FAILED TO FLUSH DATA FROM OBJECTOUTPUTSTREAM. DISCONNECTING.");
				Debug.printst(e);
				return false;
			}
			
			return true;
		}
		
		/*
		 * The disconnect method has a boolean that determines whether a disconnect message should be sent to the other connection.
		 * The instances in which a disconnect should not be sent aree as follows:
		 * 
		 * 		a broken connection has been detected
		 * 		the other connection is the one who initiated the disconnect (if we have recieved a disconnect message)
		 * 
		 * for this reason the user of this class should have no reason to call the disconnect(false) method.
		 * as a result, the option to specify whether a disconnect message is sent is limited to within this class and the only option exposed to the user is to SEND
		 * the disconnect message.
		 */
		
		public void disconnect(){
			disconnect(true);
		}
		
		/*
		 * The disconnect method first sets the connected variable to false. then, if the boolean is true, writes a DisconnectPacket to the socket.
		 * it also attempts to close the socket and all streams associated with it.
		 */
		
		private void disconnect(boolean writeDisconnect){
			Debug.println("BASECONNECTION", "DISCONNECT METHOD CALLED. DISCONNECTED.");
			Connected = false;
			
			if(writeDisconnect){
				writeObject(new DisconnectPacket());
			}
			
			try{Writer.close();}catch(Exception e){}
			try{Reader.close();}catch(Exception e){}
			try{MainSocket.close();}catch(Exception e){}
		}
		
		/*
		 * Assorted get and set methods.
		 */
		
		/*
		 * The get port number method returns the port number of the socket.
		 * If the socket was not properly initialise on being passed to this class the port number will be -1.
		 */
		public int getLocalPort(){return LocalPortNumber;}
		public int getRemotePort(){return RemotePortNumber;}
		public InetAddress getRemoteIP(){return RemoteIP;}
				
	}
	
	
	private class Reader implements Runnable{
		
		/*
		 * Since we are using an objectstream as opposed to a test/bufferedinputstream or similar, we only have the "available" method to
		 * check if there is available data on the socket. unfortunately this method does not actually work as it always returns 0 bytes of available data regardless
		 * of how much there is in the socket. This coupled with the timeout functionality was causing the connection to be dropped as soon as it started
		 * because we were trying to read packets when there were none and throwing an IOException (due to the timeout of the socket.) Due to this
		 * the only option we have is to make the readObject method a blocking method, which waits for input as long as is required until it has something to return.
		 * Obviously this is of no use to the user as they do not want their whole game to wait for a new packet of data to arrive.
		 * 
		 * As a result this class exists to wait for the blocking method to return a piece of data. it then adds it to the "Data" linked list which is a queue in the Connection
		 * class. This lined list is then in turn read from by the user of the Connection class via the "getPacket" method as if the readObject method of the baseConnection class
		 * were a non-blocking method.
		 * 
		 * Since we do not want to read loads of data in to an insanely big LinkedList, we cap the amount of data that can be read in to it by causing this method not to read the data
		 * if we already have the specified amount in the Data LinkedList. This buffer size is specified by the "BufferSize" variable in the Connection class.
		 */

		public void run() {

			while(isConnected()){
				
				if(Buffer.size()<BufferSize){
					readData();
				}else{
					try{Thread.sleep(1);}catch(Exception e){}
				}
				
			}
			
		}
		
	}
	
	
	private class ConnectionTester implements Runnable{
		
		/*
		 * This class acts as a means of periodically ensuring that the connection to the other socket is open.
		 * It means firstly that the class utilising the Connection class does not have to implement it's own "heartbeat" functionality
		 * in order to ensure the connection remains open. it also means that when the server class has no free space, and it wishes to check to make sure
		 * clients are still connected, it will not have to send an excessive amount of traffic over the network.
		 * instead, this class will occasionally check to make sure it is still connected. if it is not, it will update it's connection status, meaning that the server
		 * class can merely check the connection status of this class, without having to run the "testConnection" method. 
		 */
		
		public void run() {
			
			/*
			 * This method simply sends a packet of test data every specified test period (usually 3 seconds) while the server is still running.
			 * if the server stops running this thread will end.
			 */
			
			while(isConnected()){
					testConnection();
					
					try{
						Thread.sleep(ConnectionTestPeriod);
					}catch(Exception e){
						break;
					}
			}
			
		}
		
	}
	

}
