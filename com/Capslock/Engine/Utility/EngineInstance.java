package com.Capslock.Engine.Utility;

import com.Capslock.Engine.screen;
import com.Capslock.Engine.Controls.Keyboard.KeyboardInput;
import com.Capslock.Engine.Controls.Mouse.MouseInput;

/*
 * The engine instance class is used as a means of grouping all of the components required in the engine.
 * 
 * It creates a screen, frametimer, mouse input and keyboard input on being passed the required informationa bout how the user would like the screen to be set up.
 * 
 * It also allows the keyboard and mouse input classes to be kept outside of the screen class, and only passing them to the screen class in order to hae them associated
 * with the canvas as listeners.
 * 
 * an instance of this class is passed in to every game on creation of the game object. This is for convenience so the game may have complete access to the engine features such
 * as the screen and input without requiring multiple parameters. On recieveal of this object the game "unpacks" it, and assigns the keyboard, screen, mouse, etc, their own internal handle
 * within the game class, so the user does not have to repeatedly say:  "EngineInstance.getScreen().getScreenGraphics().<do something>();" or similar.
 */

public class EngineInstance {
	
	//variables relating to the screen. (some of these variables are also used in order components such as the MouseInput class)
	String WindowName = "";
	int SizeX = -1;
	int SizeY = -1;
	
	//the objects that will be available to the "user" when the GameINstance object is passed in to the game class
	KeyboardInput Keyboard;
	MouseInput Mouse;
	screen MainScreen;
	CTime FrameTimer;
	
	
	public EngineInstance(String WindowName, int SizeX, int SizeY){
		this.WindowName = WindowName;
		this.SizeX = SizeX;
		this.SizeY = SizeY;
		
		setupKeyboard();
		setupMouse();
		setupFrameTimer();
		setupScreen();
		
	}
	
	/*
	 * The following methods set up each of the components in the required order.
	 * 
	 * The setupKeyboard method is simple. it simply sets up a new KeyboardInput object.
	 * 
	 * The setupMouse method is slightly more complex, it is required to provide the window size as parameters to the constructor of the 
	 * MouseInput class so as to enable it to clip any mouse input to within the bounds of the canvas.
	 * 
	 * The setupFrameTimer method sets up a frametimer as standard for now. It may be made more feature-ful in the fuure with the ability to change the desired framerate.
	 * 
	 * The setupScreen method sets up a screen using the keyboardInput and MouseInput objects as parameters in the constructor.
	 * This is because they are required to be added to the canvas as input listeners, and this cannot be done unless they are passed to the screen.
	 */
	
	private void setupKeyboard(){
		Keyboard = new KeyboardInput();
	}
	
	private void setupMouse(){
		Mouse = new MouseInput(SizeX, SizeY);
	}
	
	private void setupFrameTimer(){
		FrameTimer = new CTime(100);
	}
	
	private void setupScreen(){
		MainScreen = new screen(WindowName, SizeX, SizeY, Keyboard, Mouse);
	}
	
	/*
	 * The following methods are ones which allow the user of the Engine Instance class to access all of the components within it.
	 */
	
	public KeyboardInput getKeyboard(){
		return Keyboard;
	}
	
	public MouseInput getMouse(){
		return Mouse;
	}
	
	public screen getScreen(){
		return MainScreen;
	}
	
	public CTime getFrameTimer(){
		return FrameTimer;
	}

}
