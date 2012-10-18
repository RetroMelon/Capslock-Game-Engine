package com.Capslock.Engine.Controls.Mouse;

import java.util.HashMap;
import java.util.Map;

/*
 * this class stores the text names of all supported mouse buttons.
 * This class is referred to by CMouseEvent when it attempts to resolve a name for the mouse button being pressed.
 * If this class does not have a stored name it will return the name present in UNSPECIFIED_BUTTON_NAME appended by the mouse button code
 */

public class CButtonNames {
	
	private static Map<Integer, String> MouseButtonNames = new HashMap<Integer, String>(4);
	
	private static final String UNSPECIFIED_BUTTON_NAME = "MOUSEBUTTON_UNSPECIFIED";
	
	static{
		MouseButtonNames.put(CMouseEvent.MOUSEBUTTON_UNSPECIFIED, "MOUSEBUTTON_UNSPECIFIED");
		MouseButtonNames.put(CMouseEvent.MOUSEBUTTON_LEFT, "MOUSEBUTTON_LEFT");
		MouseButtonNames.put(CMouseEvent.MOUSEBUTTON_MIDDLE, "MOUSEBUTTON_MIDDLE");
		MouseButtonNames.put(CMouseEvent.MOUSEBUTTON_RIGHT, "MOUSEBUTTON_RIGHT");
	}
	
	public static String getButtonName(int MouseButtonCode){
		String Name = MouseButtonNames.get(MouseButtonCode);
		
		if(Name == null){
			return UNSPECIFIED_BUTTON_NAME+"_"+MouseButtonCode;
		}
		
		return Name;
	}

}
