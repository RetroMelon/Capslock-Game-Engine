package test.keyboardMouseTest;

import com.Capslock.Engine.Controls.Keyboard.CKeyInfo;
import com.Capslock.Engine.Utility.EngineInstance;

public class KeyboardMouseTestMainClass {
	
	public static void main(String[] args){
		EngineInstance e = new EngineInstance("Keyboard and Mouse test game for CL Engine v2.0 - Joe Frew", 600, 600);
		
		for(int i = 0; i<256; i++){
			System.out.println(i+"  -  "+CKeyInfo.getKeyName(i));
		}
		
		KeyboardMouseTestGame bgame = new KeyboardMouseTestGame(e);
		bgame.run();
		
	}

}
