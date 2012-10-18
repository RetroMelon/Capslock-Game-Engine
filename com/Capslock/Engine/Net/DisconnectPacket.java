package com.Capslock.Engine.Net;

import java.io.Serializable;

public final class DisconnectPacket implements Serializable {
	
	/*
	 * This class exists so as to allow the Connection class to notify the other connection when a disconnect is about to occur.
	 * 
	 * This class contains no data as it is distinguished from the other classes by means of an instanceof statement.
	 * 
	 * If the disconnect method is called in the connection
	 * on recieving this packet the baseconnection will disconnect instantly and will not send a disconnect packet.
	 * 
	 * This class should be kept purely within the baseconnection class. However, since the serialization of a private inner class is disallowed, we make this a public class but we
	 * do not extend the networkpacket class. by doing this we prevent the user from sending disconnect or test packets, as they should never require to send them.
	 */
}
