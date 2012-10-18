package com.Capslock.Engine.Physics;

import com.Capslock.Engine.Render.Renderable;

/*
 * The collidable class gives an entity a collision box which can be compared with other entities' collision boxes to check if they collide
 * a collision box consists of two vectors. one vector specifies the position that the collision box starts relative to the entity's position
 * the second vector specifies the size of the collision box relative to the start of the collision box.
 * 
 * the collidable object could look like this:
 * 
 * 		Entity's Position	+-----------------------------------------------------+
 * 							|	Collision box Position						  	  |
 * 							|	+-------------------------+						  |
 * 							|	|						  |						  |
 * 							|	|						  |						  |
 * 							|	|						  |						  |
 * 							|	|						  |						  |
 * 							|	|						  |						  |
 * 							|	|						  |						  |
 * 							|	|						  |						  |
 * 							|	+-------------------------+ Collision box end	  |
 * 							|													  |
 * 							+-----------------------------------------------------+
 */

public abstract class Collidable extends Renderable{
	
	protected CollisionBox CollidableArea;
	
	public Collidable(){
	}
	
	public CollisionBox getCollidableArea(){
		return CollidableArea;
	}
	
	public void setCollidableArea(CollisionBox newCollidable){
		CollidableArea = newCollidable;
	}
	
}
