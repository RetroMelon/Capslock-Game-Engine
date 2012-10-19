package com.Capslock.Engine.Net;

import java.io.Serializable;

public final class TestPacket implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2837272519292293446L;
	
	/*
	 * This class exists so as to allow the Connection class to exchange test packets as a means of ensuring that their sockets are still open.
	 * 
	 * This class contains no data as it is distinguished from the other classes by means of an instanceof statement.
	 * 
	 * The BaseConnection class also discards any TestData objects before them reaching the buffer. This means that this class should never be required to be referenced/used
	 * in any way by the user of the networking utility as they should never have to come in to contact with it.
	 * 
	 * This class should be kept purely within the baseconnection class. However, since the serialization of a private inner class is disallowed, we make this a public class but we
	 * do not extend the networkpacket class. by doing this we prevent the user from sending disconnect or test packets, as they should never be required to send them.
	 */
}
