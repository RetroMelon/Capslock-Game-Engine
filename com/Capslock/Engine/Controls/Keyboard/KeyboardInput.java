package com.Capslock.Engine.Controls.Keyboard;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

/*
 * The KeyboardInput class is responsible for handling all of the keyboard input on the part of the player.
 * 
 * It has two ways of representing the keyboard input.
 * 
 * One is a QUEUE OF KEYEVENTS which is populated with KeyEvents as they occur.
 * This queue of keyevents can be accessed by calling the NextKeyEvent() method.
 * 
 * The class contains private methods for clearing the queue or manually injecting key events in to the queue.
 * This can be useful if the screen loses focus, and one must assume that all keys have been released.
 * 
 * The other is an ARRAY OF BOOLEANS, with the index to the array the "Key code" of the KeyEvent.
 * The booleans can be accessed by calling the KeyPressed(Index) method where the Index variables is both the location in the array and the "Key code" of the key.
 * 
 * A possible troublesome feature to bear in mind is the fact that even if the boolean array is accessed in order to gain knowledge of the state of a key,
 * the key's press and release events remain in the queue until accessed. For this reason, one should use one method (booleans), or the other (queue), exclusively
 * in order to avoid key presses being accounted for repeatedly.
 * 
 * Another feature is that the queue may sit for a length of time with no polls. On eventually being polled the events in the queue may be drastically out of date. as a result if
 * ever a new element of the game requires input (eg, onscreen keyboard, etc) it is recommended that the queue be cleared to prevent unwanted 'fake' keypresses.
 * 
 * 
 */

public class KeyboardInput extends KeyAdapter{
	
	
	//The number of keys that ought to be tracked by the array of booleans. The number 256 is used as it is the number of possible keys on a keyboard, and maps nicely to the "key codes" in the "KeyEvent" class.
	public final static short KEY_COUNT = 256; 
	//The boolean array that stores the "pressed" state of each key. true equates to pressed while false equates to "unpressed"
	protected boolean[] KeysArray = new boolean[KEY_COUNT];
	
	
	//The queue of data that stores the KevEvent instances
	protected LinkedList<CKeyEvent> KeysQueue = new LinkedList<CKeyEvent>();
	
	/* These are the methods called by the KeyAdapter interface every time a key is pressed.
	 * 
	 * In these methods we pass all of the functionality off to the ProcessKeyChange method.
	 * 
	 * In this method we first create a CKeyEvent out of the KeyEvent before adding the CKeyEvent to the keyevent queue.
	 * We then check that the even is within the bounds of the KeysArray before changing the boolean to match the "pressed" parameter.
	 * 
	 * Before adding a "key pressed" event to the queue we ensure that the key is currently in an unpressed state. this is to prevent multiple instances of a
	 * "key pressed" event being spawned while we hold down a key. 
	 */
	@Override
	public void keyPressed(KeyEvent e){
		processKeyChange(e, true);		
	}
	
	@Override
	public void keyReleased(KeyEvent e){
		processKeyChange(e, false);
	}
	
	protected void processKeyChange(KeyEvent e, boolean pressed){
		byte KeyEventType;
		
		if(pressed){
			KeyEventType = CKeyEvent.KEYEVENT_PRESSED;
		}else{
			KeyEventType = CKeyEvent.KEYEVENT_RELEASED;
		}
		
		//making sure we are not posting multiple PRESSED events
		if(KeyEventType == CKeyEvent.KEYEVENT_RELEASED || !keyPressed(e.getKeyCode())){
			CKeyEvent k = new CKeyEvent(e.getKeyCode(), KeyEventType);
			KeysQueue.add(k);
		}
		
		if(e.getKeyCode()>=0 && e.getKeyCode()<=255){
			KeysArray[e.getKeyCode()] = pressed;
		}
	}
	
	
	/*
	 * The NextKeyEvent method is used to get the next event from the  queue and return it in the form of a CKeyEvent.
	 * If there is no CKeyEvent objects to return because the queue is empty it returns null. This is useful because the user can create a loop
	 * that quits when a null object is returned, meaning they know when there is no more player input to process.
	 */
	public CKeyEvent nextKeyEvent(){
		return KeysQueue.poll();
	}
	
	/*
	 * The keyPressed method returns a boolean based on whether a given key is pressed or unpressed.
	 * the integer passed in to this method is the keycode of the key and is used as the index of the array.
	 * if the key is pressed the boolean will be true. if the key is unpressed it will be false.
	 * If the keycode passed in is invalid the method will always return false.
	 */
	public boolean keyPressed(int KeyCode){
		
		if(KeyCode>=0 && KeyCode<=255){
			return KeysArray[KeyCode];
		}
		
		return false;
	}
	
	/*
	 * These methods allow a used to clear any part of the system if they deem it to be out of date.
	 */
	public void clearBooleans(){
		for(int i = 0; i<KEY_COUNT; i++){
			KeysArray[i] = false;
		}
	}
	
	public void clearQueue(){
		KeysQueue.clear();
	}
	
	public void clear(){
		clearBooleans();
		clearQueue();
	}
	
	/*
	 * This method allows us to send a key release event for all keys that are currently pressed. This is used if the window loses focus so that we
	 * don't get a situation in which we could have a case in which <pressed>, <lose focus>, <pressed>, <released> case could occur.
	 * 
	 * It then sets all booleans to false to match the queue.
	 * 
	 * there is only a slight difference between the releaseAll method and the clear one. The release all method posts key release events to the queue for
	 * all currently pressed keys. whereas the clear method simply wipes the buffer, even if some keys may have been mid press-release at the time.
	 */
	
	public void releaseAll(){
		releaseAllInQueue();
		clearBooleans();
	}
	
	protected void releaseAllInQueue(){
		for(int i = 0; i<KEY_COUNT; i++){
			if(KeysArray[i]){
				CKeyEvent k = new CKeyEvent(i, CKeyEvent.KEYEVENT_RELEASED);
				KeysQueue.add(k);
			}
		}
	}
	
	
}
