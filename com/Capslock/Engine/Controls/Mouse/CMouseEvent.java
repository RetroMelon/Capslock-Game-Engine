package com.Capslock.Engine.Controls.Mouse;

import java.awt.event.MouseEvent;

import com.Capslock.Engine.Math.CVector2D;

/*
 * The CMouseEvent class is used as a means of representing mouse click events.
 * 
 * The MouseInput class makes instances of this object to store in a queue of mouse click events.
 * 
 * This class contains the following data:
 * 
 * 		Type of event (ie. Pressed/Released)
 * 		Mouse button code
 * 		Coordinates at which the event occurred
 * 
 * the type of event is stored as a byte. the mouse button is also stored as a byte.
 * although the MouseListener implementation stores the button as an integer, this is unnecessary as there are only 3 buttons.
 * as a result, the MouseInput class changes this to a byte 
 */

public class CMouseEvent {
	
	//static values including the names that each of the mousebuttons resolve to.
	
	public static final byte MOUSEEVENT_UNSPECIFIED = -1;
	public static final byte MOUSEEVENT_PRESSED = 0;
	public static final byte MOUSEEVENT_RELEASED = 1;
	
	public static final int MOUSEBUTTON_UNSPECIFIED = MouseEvent.NOBUTTON;
	public static final int MOUSEBUTTON_LEFT = MouseEvent.BUTTON1;
	public static final int MOUSEBUTTON_MIDDLE = MouseEvent.BUTTON2;
	public static final int MOUSEBUTTON_RIGHT = MouseEvent.BUTTON3;
	
	
	//the data stored in each MouseEvent object.
	protected int MouseButton = MOUSEBUTTON_UNSPECIFIED;
	protected byte EventType = MOUSEEVENT_UNSPECIFIED;
	protected CVector2D MousePosition = new CVector2D();
	
	/*
	 * The constructor takes a normal integer which is passed to it by the MouseInput class. Normal implies that the MouseInput class does
	 * not use any of the public static final values such as MOUSEBUTTON_UNSPECIFIED to resolve the button number. This makes the MouseInput class more efficient because
	 * it can simply pass in whatever integer value it gets in the MouseEvent.
	 */
	public CMouseEvent(int MouseButton, byte EventType, CVector2D MousePosition){
		this.MouseButton = MouseButton;
		this.EventType = EventType;
		this. MousePosition = MousePosition;
	}
	
	public CMouseEvent(int MouseButton, byte EventType, int PositionX, int PositionY){
		this.MouseButton = MouseButton;
		this.EventType = EventType;
		this. MousePosition = new CVector2D(PositionX, PositionY);
	}
	
	/*
	 * All get methods
	 */
	public CVector2D getPosition(){
		return MousePosition;
	}
	
	public int getPositionX(){
		return (int) MousePosition.getX();
	}
	
	public int getPositionY(){
		return (int) MousePosition.getY();
	}
	
	public int getButton(){
		return MouseButton;
	}
	
	public byte getEventType(){
		return EventType;
	}
	
	/*
	 * this method is used to resolve button names for the user to view in the game (or in the debug log). if the integer passed does not correspond to a button name the "MOUSEBUTTON_UNSPECIFIED"
	 * name is returned.
	 */
	public String getButtonName(){
		return CButtonNames.getButtonName(MouseButton);
	}

}
