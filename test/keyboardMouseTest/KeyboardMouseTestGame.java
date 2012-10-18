package test.keyboardMouseTest;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;

import com.Capslock.Engine.entity;
import com.Capslock.Engine.game;
import com.Capslock.Engine.Controls.Keyboard.CKeyEvent;
import com.Capslock.Engine.Controls.Mouse.CMouseEvent;
import com.Capslock.Engine.Debug.Debug;
import com.Capslock.Engine.Math.CVector2D;
import com.Capslock.Engine.Utility.EngineInstance;

public class KeyboardMouseTestGame extends game{

	public KeyboardMouseTestGame(EngineInstance e) {
		super(e);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void input() {
		
		//processing keyboard events
		CKeyEvent k = mainscreen.keyboard.nextKeyEvent();
		while(k!=null){
			String name = k.getKeyString();
			String action = (k.getEventType()==0 ? "Pressed" : "Released");
			
			Debug.println("KEYBOARDMOUSETESTGAME", "'"+name+"' key performed action '"+action + "' (KeyEventType: "+k.getEventType()+")");
			
			//creating a visual representation of the click on the screen
			entities.add(new KeyEventEntity(mainscreen, mainscreen.mouse.getMouseX(), mainscreen.mouse.getMouseY(), this, "KEY: "+name+"  (Code: "+k.getKeyCode()+")", action+ " (KeyEventType: "+k.getEventType()+")"));
			
			k = mainscreen.keyboard.nextKeyEvent();
		}
		
		
		//processing mouse events
		CMouseEvent m = mainscreen.mouse.nextMouseEvent();
		while(m!=null){
			String name = m.getButtonName();
			String action = (m.getEventType()==CMouseEvent.MOUSEEVENT_PRESSED? "Pressed" : "Released") + "(MouseEventType:  "+m.getEventType()+")";
			String location = m.getPosition().getString();
			
			Debug.println("KEYBOARDMOUSETESTGAME", "'"+name+"' performed action '"+action+"' at location: "+location);
			
			//creating a visual representation of the click on the screen
			entities.add(new MouseClickEntity(mainscreen, mainscreen.mouse.getMouseX(), mainscreen.mouse.getMouseY(), this, name, action));
			
			m = mainscreen.mouse.nextMouseEvent();
		}
		
		
	}

	@Override
	public void update() {
	}

	@Override
	public void draw() {
		//drawing the mouse click entities
		for(entity e : entities){
			e.draw();
		}
		
		//drawing the current mouse location (vector) on to the screen
		mainscreen.screengraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		mainscreen.screengraphics.setColor(Color.green);
		mainscreen.screengraphics.setFont(new Font(Font.DIALOG_INPUT, Font.PLAIN, 11));
		mainscreen.screengraphics.drawString((new CVector2D(mainscreen.mouse.getMouseX(), mainscreen.mouse.getMouseY())).getString(), mainscreen.mouse.getMouseX()+7, mainscreen.mouse.getMouseY()-10);
		
	}

	@Override
	public int quit() {
		// TODO Auto-generated method stub
		return 0;
	}

	
}
