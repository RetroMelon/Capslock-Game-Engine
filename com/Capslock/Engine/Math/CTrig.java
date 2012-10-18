package com.Capslock.Engine.Math;

/*
 * The CTrig class is used as a way from isolating all trigonometric functions from the user.
 * The methods currently simply resolve to the standard java Math methods for trigonometric maths, however, a lookup table of trig values could be created
 * in order to speed up the process of resolving trig values.
 * 
 * After some research, namely on this article:
 * 
 *  			http://en.wikipedia.org/wiki/Lookup_table
 *  
 *  under the heading "lookup tables and image processing" it can be concluded that a lookup table in jhava is not an efficient implementation for trig.
 *  further tests should be carried out, however, for now we will stick to resolving all trig queries with standard java Math.
 */

public final class CTrig {
	
	public static double sin(double i){
		return Math.sin(i);
	}
	
	public static double cos(double i){
		return Math.cos(i);
	}
	
	public static double tan(double i){
		return Math.tan(i);
	}
	
	/*
	 * These methods are the inverse functions. they are named differently to the ones found in standard java Math because for some reason the inverse functions are hard to 
	 * locate in standard java math.
	 */
	
	public static double isin(double i){
		return Math.asin(i);
	}
	
	public static double icos(double i){
		return Math.acos(i);
	}
	
	public static double itan(double i){
		return Math.atan(i);
	}
	
	/*
	 * This method is unlikely to require a lookup table but it is always a possibility.
	 */
	
	public static double toDegrees(double i){
		return Math.toDegrees(i);
	}
	
	public static double toRadians(double i){
		return Math.toRadians(i);
	}

}
