package com.Capslock.Engine.Controls.Mouse;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.Capslock.Engine.Math.CVector2D;

/*
 * The MouseInput class is responsible for handling all of the mouse input on the part of the player.
 * 
 * It has two ways of representing button presses and one way of representing mouse position.
 * 
 * One method of representing button presses is a QUEUE OF BUTTON EVENTS. This queue holds CMouseEvent objects.
 * These objects contain three things:
 * 
 * 			The button that the event occurred on (button one, button two, etc).
 * 			The type of button event (pressed/released).
 * 			The X/Y coordinated at which this event occurred.
 * 			
 * The other method of tracking button presses is simply an ARRAY OF BOOLEANS which tracks which buttons are pressed.
 * The Button number is used as an index to the array where the true value means a button is pressed, and the false value means a button is not.
 * 
 * 
 */

public class MouseInput implements MouseListener, MouseMotionListener{

	//the position of the mouse, kept up to date by the "mouseMove" method.
	protected CVector2D MousePosition = new CVector2D(0, 0);
	
	//the queue that stores the CMouseEvent instances
	protected LinkedList<CMouseEvent> MouseQueue = new LinkedList<CMouseEvent>();
	
	/*
	 * the Map which stores all of the booleans about whether a mouse button is currently pressed.
	 * a map is used so as to allow for future inclusion of other input events like extra buttons. these buttons would not necessarily
	 * be sequential in terms of "Button Codes" to the three basic buttons.
	 * One should note that if they should choose to upgrade this class with more keys, they should also adjust the "estimated number of elements"
	 * parameter in the hashmap's constructor.
	*/
	protected Map<Integer, Boolean> ButtonStatesMap = new HashMap<Integer, Boolean>(4);
	
	
	/*
	 * These variables are used to clip the mouse location to within the screen, since the mousemotionlistener is able to detect it's location outside of the canvas,
	 * and the mouselistener is able to detect clicks and releases when unfocussed, we are required to detect if a click is out of range and clip it to within the screen's bounds. 
	 * as standard these values are set to -1, which means that we do not have any information on the screen's bounds.
	 */
	protected int ScreenSizeX = -1;
	protected int ScreenSizeY = -1;
	
	//unused methods that belong to the interfaces implemented, and therefore cannot be removed entirely.
	@Override
	public void mouseClicked(MouseEvent e){}
	
	
	public MouseInput(int ScreenSizeX, int ScreenSizeY){
		this.ScreenSizeX = ScreenSizeX;
		this.ScreenSizeY = ScreenSizeY;		
	}
	

	/*
	 * The following methods are used to track the mouse's movement within the game window.
	 * 
	 * The mouseEntered and mouseExited methods are used so as to avoid the problems that may occur when a mouse's button changes
	 * state outwith the bounds of a window and as a result, is not registered.
	 * 
	 */
	
	@Override
	public void mouseMoved(MouseEvent e) {
		setMouseX(e.getX());
		setMouseY(e.getY());
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		setMouseX(e.getX());
		setMouseY(e.getY());
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {		
	}
	
	/*
	 * The following methods are called by the MouseListener interface every time a mouse click occurs.
	 * 
	 * when a button is pressed the method does a number of things. first it creates a CMouseEvent event object with the following data:
	 * 
	 * 		mousebutton
	 * 		eventtype
	 * 		location of click
	 * 
	 * the method then sets the boolean corresponding to the mouse button in the ButtonStatesMap HashMap.
	 * 		
	 */

	@Override
	public void mousePressed(MouseEvent e) {
		MouseQueue.add( new CMouseEvent(e.getButton(), CMouseEvent.MOUSEEVENT_PRESSED, MousePosition) );
		ButtonStatesMap.put(e.getButton(), true);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		MouseQueue.add( new CMouseEvent(e.getButton(), CMouseEvent.MOUSEEVENT_RELEASED, MousePosition) );
		ButtonStatesMap.put(e.getButton(), false);
	}
	
	/*
	 * all methods that are exposed to the user of this class.
	*/
	
	/*
	 * the nextMouseEvent method is used to get the next event from the queue and return it in the form of a CMouseEvent.
	 * If there is no CMouseEvent objects to return because the queue is empty it returns null. This is useful because the user can create a loop
	 * that quits when a null object is returned, meaning they know when there is no more player input to process.
	 */
	public CMouseEvent nextMouseEvent(){
		return MouseQueue.poll();
		
	}
	
	public boolean buttonPressed(int ButtonCode){
		if(ButtonStatesMap.containsKey(ButtonCode)){
			return ButtonStatesMap.get(ButtonCode); 
		}
		
		return false;
	}
	
	public int getMouseX(){
		return (int) MousePosition.getX();
	}
	
	public int getMouseY(){
		return (int) MousePosition.getY();
	}
	
	/*
	 * The clearing methods are not really necessary within the MouseInput class as unlike the KeyboardInput class, the system
	 * manages mouse events well when the window has lost focus. These methods are purely here so as to be here if ever necessary
	 */
	public void clearBooleans(){
		while(ButtonStatesMap.entrySet().iterator().hasNext()){
			ButtonStatesMap.entrySet().iterator().next().setValue(false);
		}
	}
	
	public void clearQueue(){
		MouseQueue.clear();
	}
	
	public void clear(){
		clearBooleans();
		clearQueue();
	}
	
	/*
	 * As with the keyboard input class there is only a slight difference between clear and releaseAll. release all sends MouseEvents of the
	 * release variety to the mouse event queue for all currently pressed mouse buttons, whereas clear simply clears the queue.
	 */
	
	public void releaseAll(){
		releaseAllInQueue();
		clearBooleans();
	}
	
	protected void releaseAllInQueue(){
		while(ButtonStatesMap.entrySet().iterator().hasNext()){
			int Button = ButtonStatesMap.entrySet().iterator().next().getKey();
			MouseQueue.add(new CMouseEvent(Button, CMouseEvent.MOUSEEVENT_RELEASED, 0, 0));
		}
	}
	
	
	/*
	 * the set mouse methods are used internally to set the mouse position in the MousePosition vector. They are used so as to abstract the functionality of the
	 * listener from the data storage, but also so tha we can perform clipping on any coordinates that are outwith the bounds of the canvas
	 */
	
	protected void setMouseX(int newX){
		if(newX<0) newX = 0;
		else if(newX>=ScreenSizeX) newX = ScreenSizeX-1;
		
		MousePosition.setX(newX);
	}

	protected void setMouseY(int newY){
		if(newY<0) newY = 0;
		else if(newY>=ScreenSizeY) newY = ScreenSizeY-1;
		
		MousePosition.setY(newY);
	}
	
	
	
	
}
