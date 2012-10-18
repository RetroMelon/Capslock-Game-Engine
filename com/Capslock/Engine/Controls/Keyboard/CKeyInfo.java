package com.Capslock.Engine.Controls.Keyboard;

import java.awt.event.KeyEvent;

/*
 * This class stores the text representations for keys that the KeyboardInput class handles.
 * This class also stores the type that a key is for most keys. the types can include:
 * 
 * 		TYPE_CHARACTER = 0			-	a, b, c, d... z, 1, 2, 3, 4, 5, etc
 * 		TYPE_SYMBOL = 1			-		!"£$%^&*()_+{}:@~<>? etc
 * 		TYPE_INPUT = 2		-			Esc, Tab, shift, Space, Enter, Backspace, capslock.   (any keys that you might want to monitor while inputting text to an inputbox)
 * 		TYPE_SPECIAL = 3		-		F1, F2, F3, F4, F5, F6, F7, F8, F9, PRTSCN, SCRLK, PAUSE, Ctrl, Alt, Windows, Arrows, Insert, PgDn, etc (any keys you might want to observe for controlling a character or other game input)
 * 		TYPE_UNSPECIFIED = -1	-		anything else.
 * 
 * This class is referred to when the CKeyEvent class translates itself in to a text representation.
 * The CKeyEvent class first consults this class. this class will always return a text representation.
 * By default all representations are set up as the default found in "KeyEvent" class.
 */

public abstract class CKeyInfo  {
	
	private static String[] KeyNames = new String[KeyboardInput.KEY_COUNT];
	
	public static final String UNSPECIFIED_KEY_NAME = "KEY_UNSPECIFIED";
	
	private static byte[] KeyTypes = new byte[KeyboardInput.KEY_COUNT];
	
	public static final byte KEY_TYPE_UNSPECIFIED = -1;
	public static final byte KEY_TYPE_CHARACTER = 0;
	public static final byte KEY_TYPE_SYMBOL = 1;
	public static final byte KEY_TYPE_INPUT = 2;
	public static final byte KEY_TYPE_SPECIAL = 3;
	
	/*
	 * This area of code is used to set up different key names and key types
	 */
	
	static{
		
			//setting up the names of all of the keys.
			
			for(int i = 0; i<KeyNames.length; i++){
				KeyNames[i] = KeyEvent.getKeyText(i);
			}
			
			//setting up a blank set of KEY_TYPES which will then be overwritten 
			
			for(int i = 0; i<KeyTypes.length; i++){
				KeyTypes[i] = KEY_TYPE_UNSPECIFIED;
			}
			
			//setting up letters and numbers
			for(int i = 65; i<91; i++){ //letters
				KeyTypes[i] = KEY_TYPE_CHARACTER;
			}
			for(int i = 48; i<58; i++){ //numbers
				KeyTypes[i] = KEY_TYPE_CHARACTER;
			}
			
			//setting up "input" keys
			KeyTypes[8]  = KEY_TYPE_INPUT; //backspace
			KeyTypes[9]	 = KEY_TYPE_INPUT; //tab
			KeyTypes[10] = KEY_TYPE_INPUT; //enter
			KeyTypes[16] = KEY_TYPE_INPUT; //shift
			KeyTypes[27] = KEY_TYPE_INPUT; //escape
			KeyTypes[32] = KEY_TYPE_INPUT; //space
			KeyTypes[20] = KEY_TYPE_INPUT; //capslock
			
			//setting up symbol keys
			KeyTypes[192] = KEY_TYPE_SYMBOL; //`
			KeyTypes[45] = KEY_TYPE_SYMBOL; //-
			KeyTypes[61] = KEY_TYPE_SYMBOL; //+
			KeyTypes[91] = KEY_TYPE_SYMBOL; //[
			KeyTypes[93] = KEY_TYPE_SYMBOL; //]
			KeyTypes[59] = KEY_TYPE_SYMBOL; //;
			KeyTypes[222] = KEY_TYPE_SYMBOL; //'
			KeyTypes[44] = KEY_TYPE_SYMBOL; //~
			KeyTypes[92] = KEY_TYPE_SYMBOL; //\
			KeyTypes[44] = KEY_TYPE_SYMBOL; //<
			KeyTypes[46] = KEY_TYPE_SYMBOL; //>
			KeyTypes[47] = KEY_TYPE_SYMBOL; //?
			
			//setting up special keys
			//F1, F2, etc
			for(int i = 112; i<123; i++){
				KeyTypes[i] = KEY_TYPE_SPECIAL;
			}
			
			KeyTypes[17] = KEY_TYPE_SPECIAL; //Ctrl
			KeyTypes[18] = KEY_TYPE_SPECIAL; //Alt
			KeyTypes[157] = KEY_TYPE_SPECIAL; //Win or Command
			KeyTypes[156] = KEY_TYPE_SPECIAL; //Insert or Help
			KeyTypes[36] = KEY_TYPE_SPECIAL; //Home
			KeyTypes[35] = KEY_TYPE_SPECIAL; //End
			KeyTypes[33] = KEY_TYPE_SPECIAL; //PgUp
			KeyTypes[34] = KEY_TYPE_SPECIAL; //PgDown
			KeyTypes[127] = KEY_TYPE_SPECIAL; //Delete
			KeyTypes[38] = KEY_TYPE_SPECIAL; //Up Arrow
			KeyTypes[40] = KEY_TYPE_SPECIAL; //Down Arrow
			KeyTypes[37] = KEY_TYPE_SPECIAL; //Left Arrow
			KeyTypes[39] = KEY_TYPE_SPECIAL; //Right Arrow
			
			
	}
	
	public static byte getKeyType(int KeyCode){
		if(KeyCode>=0 && KeyCode<=KeyboardInput.KEY_COUNT){
			return KeyTypes[KeyCode];
		}
		
		return KEY_TYPE_UNSPECIFIED;
	}
	
	public static String getKeyName(int KeyCode){
		if(KeyCode>=0 && KeyCode<=KeyboardInput.KEY_COUNT){
			return KeyNames[KeyCode];
		}
		return UNSPECIFIED_KEY_NAME;
	}

}
