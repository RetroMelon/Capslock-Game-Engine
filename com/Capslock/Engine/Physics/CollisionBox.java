package com.Capslock.Engine.Physics;

import java.awt.Color;

import com.Capslock.Engine.Debug.Debug;
import com.Capslock.Engine.Math.CVector2D;
import com.Capslock.Engine.Render.RenderTarget;
import com.Capslock.Engine.Render.Renderable;

	/*
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
	* 
	* Usually the collision box is part of a collidable entity. in this case, the collision box needs to know the position of this parent entity. as a result the collision box
	* has a constructor in to which can be passed any movable entity. whenever a collision box is tasked to detect a collision it will update it's own Position vector with the parent entity's position.
	* it will then carry out the collision detection, adding it's current "StartPosition" on to the parent entity's position. if the collision box is tasked to check collisions with any other
	* collision box, it will also call a method to update that collision box's position before carrying out the calculations.
	* 
	* all of the collisionBox's method include a parameter for a position vector of a movable object. this is the vector which will be used to locate the boundingbox in the world.
	* if the bounding box is not associated with 
	*/

public class CollisionBox extends Renderable{
	
	Movable ParentMovable;
	
	CVector2D StartPosition;
	CVector2D Size;
	
	/*
	 * The first two points specify where the vector starts RELATIVE to the entity it is the child of. The second two points are the 
	 */
	
	public CollisionBox(double StartPositionX, double StartPositionY, double SizeX, double SizeY, Movable ParentMovable){
		
		this.StartPosition = new CVector2D(StartPositionX, StartPositionY);
		this.Size = new CVector2D(SizeX, SizeY);
		
		this.ParentMovable = ParentMovable;
		
		Debug.println("COLLISIONBOX", "COLLISION BOX CREATED WITH POSITION: "+StartPosition.getString()+ " and size: "+Size.getString());
	}
	
	public CollisionBox(CVector2D StartPosition, CVector2D Size, Movable ParentMovable){
		this.StartPosition = StartPosition;
		this.Size = Size;
		
		this.ParentMovable = ParentMovable;
		
		Debug.println("COLLISIONBOX", "COLLISION BOX CREATED WITH POSITION: "+StartPosition.getString()+ " and size: "+Size.getString());
	}
	
	
	public boolean collides(CollisionBox OtherBox){
		CVector2D ThisStart = this.getCompensatedStartPosition();
		CVector2D ThisEnd = this.getEndPosition();
		
		CVector2D OtherStart = OtherBox.getCompensatedStartPosition();
		CVector2D OtherEnd = OtherBox.getEndPosition();
		
		if(ThisStart.getX()	>	OtherEnd.getX())return false;
		if(ThisEnd.getX()	<	OtherStart.getX())return false;
		if(ThisStart.getY()	>	OtherEnd.getY())return false;
		if(ThisEnd.getY()	<	OtherStart.getY())return false;
		
		return true;
	}
	
	public boolean collides(CVector2D PointPosition){
		return collides(PointPosition.getX(), PointPosition.getY());
	}
	
	public boolean collides(double PointX, double PointY){
		CVector2D ThisStart = this.getCompensatedStartPosition();
		CVector2D ThisEnd = this.getEndPosition();
		
		if(PointX	>	ThisEnd.getX())return false;
		if(PointX	<	ThisStart.getX())return false;
		if(PointY	>	ThisEnd.getY())return false;
		if(PointY	<	ThisStart.getY())return false;
		
		return true;
	}
	
	/*
	 * get and set methods
	 */
	
	public Movable getParentMovable(){
		return ParentMovable;
	}
	
	/*
	 * When getting the start position we always add on the parent movable's location. this is because when a collision box wants to check it's collisions with another collision box it is easier for it
	 * to ask for the "compensated" start position with the parent movable's position already taken in to consideration.
	 * 
	 * Some of the methods require a knowlege of the parent movable's location. these methods will first call the updatePosition method which will set this collision box's position equal to the parent movable's (if one should exist)
	 */
	
	public CVector2D getCompensatedStartPosition(){
		updatePosition();
		return Position.add(StartPosition);
	}
	
	public CVector2D getUncompensatedStartPosition(){
		return StartPosition;
	}
	
	public CVector2D getSize(){
		return Size;
	}
	
	public CVector2D getEndPosition(){
		updatePosition();
		return Position.add(StartPosition).add(Size);
	}
	
	/*
	 * The updatePosition method is used to update this collision box's position to be the same as that of the parent movable's position.
	 * if this collision box has no parent movable, updateposition will do nothing.
	 */
	
	public void updatePosition(){
		if(ParentMovable == null)return;
		this.Position = ParentMovable.getPosition();
		
		//Debug.println("COLLISIONBOX", "UPDATED COLLISION BOX'S POSITION WITH THE PARENT'S POSITION OF: "+Position.getString());
	}
	

	/*
	 * The collision box class extends the renderable class. This is so that during debugging we can draw the collision box
	 * as a rectangle so that we can visualise the collisions better.
	 * 
	 * as a result, the collision box also has a colour. this can be set by the setColor method.
	 * this colour is useful when we want to change the colour of the collision box to signify that it is currently colliding
	 * 
	 * Since the position of the collision box is already augmented by the parentmovableposition variable, we do not use the parentposition.
	 * this may cause problems if rendering inside a camera which is inside another camer, however, this drawing functionality is only intended for debugging purposes.
	 * 
	 */
	protected void draw(RenderTarget Target, CVector2D ParentPosition) {
		CVector2D RelativeStartPosition = ParentPosition.add(StartPosition);
		
		Target.getGraphics().setColor(Color.YELLOW);
		
		Target.getGraphics().drawRect((int)RelativeStartPosition.getX(), (int)RelativeStartPosition.getY(), (int)Size.getX(), (int)Size.getY());
	}

}
