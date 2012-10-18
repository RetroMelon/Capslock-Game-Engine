package com.Capslock.Engine.Physics;

import com.Capslock.Engine.Game.Entity;
import com.Capslock.Engine.Math.CVector2D;

/*
 * This class is the first subclass of the entity class.
 * This class simply provides the facility for the entity to have a location.
 * 
 * The location/position of the entity is described by a vector which can be retrieved or updated through use of the get and set methods.
 */

public abstract class Movable extends Entity{
	
	protected CVector2D Position = new CVector2D(0, 0);
	
	public Movable(){
		
	}
	
	public Movable(CVector2D Position){
		this.Position = new CVector2D(Position);
	}
	
	public void setPosition(CVector2D newPosition){
		this.Position = newPosition;
	}
	
	public CVector2D getPosition(){
		return Position;
	}
	
}
