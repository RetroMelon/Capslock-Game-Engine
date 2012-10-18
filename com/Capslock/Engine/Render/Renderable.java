package com.Capslock.Engine.Render;

import java.util.LinkedList;

import com.Capslock.Engine.Math.CVector2D;
import com.Capslock.Engine.Physics.Movable;

/*
 * This class is a subclass of the movable class which is a subclass of entity.
 * 
 * The Renderable class has a queue of other Renderable objects. this allows a complex entity such as a player with a name bubble or health bar to decide what components
 * of itself it wants to be displayed on the screen. When a Renderable object is rendered it first renders all of the Renderables in it's render queue before then calling the 
 * draw method which allows it to draw itself in any way it sees fit.
 * 
 * The renderable class has a position as it implements the movable class. this position will signify the renderable's position relative to it's parent renderable.
 * it the renderable has no parent renderables, this position will represent it's position relative to the point (0, 0) on the RenderTarget.
 * When the render method is called, the parent renderable passes in 
 * 
 * This class allows an entity to be drawn to a rendertarget. a render target is essentially an image on to which renderable objects can be drawn.
 * A Renderable object has 5 main methods:
 * 
 * 		render	-			The render method is the one called by anything which wishes to render the renderable on to a rendertarget
 * 							The render method recieves a render target on which to render and a position which is the starting point of the parent class (in other words, where it ought to be rendered)
 * 
 * 		setupRenderQueue - 	The renderqueue is the queue of renderable objects which ought to be rendered before this one.
 * 							the render queue is useful if a renderable object has more than one component that it wishes to render such as a particle effect or
 * 							a player with a health bar. this method almost always needs to be overridden for a complex object. however, since simple objects do not necessarily
 * 							require this method it is not abstract because it would only get in the way.
 * 
 * 		drawRenderQueue	-	This method calls the render method on all renderables in the render queue.
 * 
 * 		draw			-	This method is the one which actually draws the renderable to the rendertarget. since all renderables have the ability to be drawn differently this method is abstract as the
 * 							user must choose how they wish to render the object.
 * 							This method will ultimately be called on all renderables as this is the only method that is able to actually draw anything.
 * 							the parameters for this method are the rendertarget on which to draw and a desired drawing position.
 * 							this desired position is a combination of the parent renderable's position and this renderable's position.
 */

public abstract class Renderable extends Movable{

	protected LinkedList<Renderable> RenderQueue = new LinkedList<Renderable>();
	
	public Renderable(){}
	
	public Renderable(CVector2D Position){
		super(Position);
	}
	
	/*
	 * In the render method the Parent Position is the position of the parent object.
	 * 
	 * Note that for a renderable to work successfully in the render queue it's position must be RELATIVE to the parent object. If it's position is relative to the world then
	 * 
	 */
	
	public void render(RenderTarget Target, CVector2D ParentPosition){
		setupRenderQueue();
		drawRenderQueue(Target, ParentPosition.add(Position));
		draw(Target, ParentPosition.add(Position));
	}
	
	protected void setupRenderQueue(){}
	
	protected abstract void draw(RenderTarget Target, CVector2D DesiredRenderPosition);
	
	protected void drawRenderQueue(RenderTarget Target, CVector2D ParentPosition){
		int RenderQueueSize = RenderQueue.size();
		for(int i = 0; i<RenderQueueSize; i++){
			Target.drawRenderable(RenderQueue.poll(), ParentPosition);
		}
	}

}
