package test.EverythingTestClient;

import com.Capslock.Engine.game;
import com.Capslock.Engine.Net.Connection;
import com.Capslock.Engine.Utility.EngineInstance;

public class EverythingTestClientMain {
	
	public static Connection MainConnection = null;
	
	public static void main(String[] args){
		EngineInstance e = new EngineInstance("Everything Test Game", 640, 480);
		
		game connectiongame = new EverythingTestClientConnecter(e);
		connectiongame.run();
	}

}
