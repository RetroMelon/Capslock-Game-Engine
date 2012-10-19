package test.nct;

import com.Capslock.Engine.Net.NetworkPacket;

public class Packet extends NetworkPacket{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3733630100976869921L;
	private String s = "";
	
	public Packet(String s){
		this.s = s;
	}
	
	public String getData(){
		return s;
	}

}
