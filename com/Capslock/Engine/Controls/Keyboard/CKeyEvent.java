package com.Capslock.Engine.Controls.Keyboard;


/*
 * This class is used as a means of storing keyboard events.
 * 
 * The KeyboardInput class makes instances of this object to store in a queue of keyboard events.
 * It also returns instances of this class in the KextKeyEvent method.
 * 
 * This class contains the following data:
 * 
 * 		Key Code
 * 		Type of Event
 * 
 * This method also contains Static Final values concerning the type of keyevent. This is useful when creating these objects so as not to make errors
 * and when attempting to distinguish the nature of the key event when programming gameplay.
 * 
 * It also contains a method which allows the user to get the character (in string form)
 * which relates to the key on the keyboard.
 */

public class CKeyEvent {
	
	public static final byte KEYEVENT_UNSPECIFIED = -1;
	public static final byte KEYEVENT_PRESSED = 0;
	public static final byte KEYEVENT_RELEASED = 1;
	
	protected int KeyCode = 0;
	protected byte KeyEventType = KEYEVENT_UNSPECIFIED;
	
	public CKeyEvent(int KeyCode, byte KeyEventType){
		this.KeyCode = KeyCode;
		this.KeyEventType = KeyEventType;
	}
	
	public int getKeyCode(){
		return KeyCode;
	}
	
	public byte getEventType(){
		return KeyEventType;
	}
	
	public byte getKeyType(){
		return CKeyInfo.getKeyType(KeyCode);
	}
	
	public String getKeyString(){
		return CKeyInfo.getKeyName(KeyCode);
	}

}
