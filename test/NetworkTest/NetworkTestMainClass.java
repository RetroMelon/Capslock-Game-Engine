package test.NetworkTest;

import com.Capslock.Engine.Debug.Debug;
import com.Capslock.Engine.Net.*;
import com.Capslock.Engine.Utility.EngineInstance;

import java.util.*;

public class NetworkTestMainClass {
	
	
	public static Server s;
	
	public static boolean quit = false;
	
	public static ArrayList<Connection> connections = new ArrayList<Connection>();
	
	public static void main(String[] args){
		Debug.println("NETWORKTESTMAINCLASS", "STARTING SERVER...");
		s = new Server(2);
		
		new Thread(new ServerHandler()).start();
		
		EngineInstance e = new EngineInstance("Press Q to Quit", 200, 200);
		
		QuitGame q = new QuitGame(e);
		
		Debug.println("NETWORKTESTMAINCLASS", "STARTING QUITGAME");
		
		q.run();
		
		s.stopServer();
		
		for(int i = 0; i<connections.size(); i++){
			connections.get(i).disconnect();
		}
		
		Debug.println("NETWORKTESTMAINCLASS", "SUCCESSFULLY STOPPED ALL CONNECTIONS. QUITTING...");
	}
	
	public static void getNewConnections(){
		Connection C = s.getNextConnection();
		if(C!=null){
			connections.add(C);
		}
	}
	


}
