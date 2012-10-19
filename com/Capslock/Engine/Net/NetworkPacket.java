package com.Capslock.Engine.Net;

import java.io.Serializable;

public class NetworkPacket implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1057674181465102390L;
	
	/*
	 * This class describes all of the packets of data which are sent between connections over the network.
	 * 
	 * This class is used for a number of reasons:
	 * 
	 * 		It ensures that all classes which are sent over the network are implementing the Serializable interface so as not to cause "non-serializable" errors at runtime.
	 * 		It simply acts as a means of maintaining a neat hierarchy of inheritance which is always desirable in object oriented programming.
	 * 
	 * The typical usage for this class is as follows:
	 * 
	 * 		The user will define a new class and extend the NetworkData class (this class).
	 * 		The user will add their own customised fields to the class.
	 * 		The user can send the data over the network.
	 * 		When the other connection receives the data it can determine what type of data it is using the instanceof statement along with a switch statement
	 * 		After determining the type of data the receiver then has all of the information necessary about the object to start processing the data held within it.
	 * 
	 * A notable down-side to this method of data exchange is that both client and server applications, which are likely to be developed independently, must both share the same
	 * data classes which extend the NetworkData. ie, if one were to develop a multi-player game that requires the position of players to be transferred, both server and client applications
	 * would be required to have access to the "PositionPacket" class.
	 * 
	 * To circumvent this difficulty, a generic "DataPacket" class could be created. this class could store an array of bytes which could be manipulated by means of streams.
	 * This way, the type of packet could be determined by the first byte in the stream, after which the data could be interpreted in a way specified by the type of the packet.
	 */

}
