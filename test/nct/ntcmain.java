package test.nct;

import java.util.Scanner;

import com.Capslock.Engine.Net.Client;
import com.Capslock.Engine.Net.Connection;

public class ntcmain {
	
	public static void main(String args[]){
		
		System.out.println("PROGRAM STARTED... CREATING CONNECTION");
		Client cl = new Client("localhost", 9999, 2000);
		
		cl.connect();
		
		while(cl.getConnectionStatus() == Client.NOT_YET_CONNECTED){
			try{Thread.sleep(20);}catch(Exception e){}
		}
		
		if(cl.getConnectionStatus() == Client.UNSUCCESSFULLY_CONNECTED){
			System.out.println("CLIENT FAILED TO CONNECT. QUITTING.");
			return;
		}
		
		Connection c = cl.getConnection();
		
		System.out.println("SLEEPING FOR 500MS...");
		try{Thread.sleep(500);}catch(Exception e){}
		
		System.out.println("TYPE MESSAGES TO WRITE TO THE SOCKET OR 'QUIT' TO TERMINATE.");
		Scanner scan = new Scanner(System.in);
		String message = scan.nextLine();
		
		while(!message.equalsIgnoreCase("QUIT")){
			c.writePacket(new Packet(message));
			
			System.out.println("");
			
			message = scan.nextLine();
		}
		
		System.out.println("PACKET HAS BEEN WRITTEN... PRESS ANY KEY TO DISCONNECT...");
		(new Scanner(System.in)).nextLine();
		
		System.out.println("DISCONNECTING...");
		c.disconnect();
		
		System.out.println("QUITTING...");		
	}

}
