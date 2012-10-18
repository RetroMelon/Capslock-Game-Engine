package test.NetworkTest;

import java.awt.event.KeyEvent;

import test.nct.Packet;

import com.Capslock.Engine.game;
import com.Capslock.Engine.Controls.Keyboard.CKeyEvent;
import com.Capslock.Engine.Net.Connection;
import com.Capslock.Engine.Utility.EngineInstance;

public class QuitGame extends game{

	public QuitGame(EngineInstance e) {
		super(e);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void input() {
		CKeyEvent k = this.Engine.getKeyboard().nextKeyEvent();
		
		while(k!=null){
			if(k.getKeyCode()==KeyEvent.VK_Q)quit=true;
			else if(k.getKeyCode() == KeyEvent.VK_SPACE && k.getEventType()==CKeyEvent.KEYEVENT_PRESSED)printallconnected();
			
			k = this.Engine.getKeyboard().nextKeyEvent();
		}
		
	}
	
	private void printallconnected(){
		System.out.println("-----------");
		for(int i = 0; i<NetworkTestMainClass.s.getMaxConnections(); i++){
			Connection c = NetworkTestMainClass.s.getConnection(i);
			if(c == null)continue;
			
			System.out.println(NetworkTestMainClass.s.getConnection(i).getRemoteIP() + "   :   "+ NetworkTestMainClass.s.getConnection(i).testConnection());
			
			Packet d = (Packet) NetworkTestMainClass.s.getConnection(i).readPacket();
			
			if(d == null)continue;
			
			System.out.println("Next Message from Connection:  "+((Packet)d).getData());
		}
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int quit() {
		// TODO Auto-generated method stub
		return 0;
	}

}
