package com.Capslock.Engine.Render;

import java.awt.Color;

import com.Capslock.Engine.Math.CVector2D;
import com.Capslock.Engine.Physics.Collidable;

/*
 * The camera class is responsible for acting as a means of rendering the 
 * Cameras do not need to be the size of the screen and therefore a boundary is specified as a vector named the CameraBounds vector.
 * 
 * The camera also extends the collidable class. it does this so as to allow it to check collisions with other objects.
 * collision checking with other objects is useful as it allows the camera to detect what objects will be visible on screen.
 * 
 * The camera does not do this collision checking automatically as if a renderable which WAS NOT within the bounds of the camera had a renderable
 * within it's render queue which WAS within the bounds of the camera, renderable2 would not be rendered even though it should have been.
 * 
 * Therefore the collision checking with the camera must be done by the user within the code of the game.
 * 
 * 
 * The camera is drawn to a redertarget just like any other object. since the camera will create it's own rendertarget before drawing anything to the screen,
 * we must override  the drawRenderQueue. the changes we wil make to the default renderable class are as follows:
 * 		
 * 		override the drawRenderQueue method so that renderables draw to the camera's rendertarget.
 * 		Change the draw method to draw the camera's rendertarget to the rendertarget provided at the position indicated.
 * 
 */

public class Camera extends Collidable{
	
	/*
	 * This vector describes the size of the bounds of the camera in pixels.
	 * once this value is set it cannot be changed.
	 * this is so as not to cause errors within the class, however, it is also due to the fact that the rendertarget class also does not support
	 * resizing of itself.
	 */
	
	protected CVector2D CameraBounds;
	
	/*
	 * This vector stores the position at which the camera is in the virtual space of the "world".
	 * unlike any other renderables, the camera's position vector will determine where it should sit on the screen.
	 * the ViewPosition vector, on the other hand, determines at what point in space the camera's upper left hand corner is looking at.
	 */
	
	protected CVector2D ViewPosition;
	
	/*
	 * the renderable area is the actual bufferedImage on to which the "view" for the camera is drawn on to.
	 * This buffered image is then drawn to the graphics object of the rendertarget that is passed to the camera when the user wants the camera to be drawn.
	 */
	
	protected RenderTarget RenderableArea;
	
	public Camera(CVector2D Size){
		CameraBounds = Size;
		setupRenderTarget();
	}

	public Camera(int SizeX, int SizeY){
		CameraBounds = new CVector2D(SizeX, SizeY);
		setupRenderTarget();
	}
	
	private void setupRenderTarget(){
		this.RenderableArea = new RenderTarget(CameraBounds);
	}
	
	public void setViewPosition(CVector2D newPosition){
		this.ViewPosition = newPosition;
	}
	
	public void setViewPositionX(double newX){
		this.ViewPosition.setX(newX);
	}
	
	public void setViewPositionY(double newY){
		this.ViewPosition.setY(newY);
	}
	
	public CVector2D getViewPosition(){
		return ViewPosition;
	}
	
	public int getSizeX(){
		return (int) CameraBounds.getX();
	}
	
	public int getSizeY(){
		return (int) CameraBounds.getY();
	}
	
	public CVector2D getSize(){
		return CameraBounds;
	}
	
	public void addtoRenderQueue(Renderable renderable){
		this.RenderQueue.add(renderable);
	}

	/*
	 * This method must be overridden as it is an abstract method in renderable. this method is not used by camera.
	 * 
	 */
	protected void setupRenderQueue() {}
	
	/*
	 * Since the camera draws first to a render target of it's own and then to the render target it is provided with, it must override the drawRenderQueue method to ensure that the render-ables 
	 * are draw to it's render target instead of the one provided as a parameter.
	 * 
	 * Once the camera has successfully drawn all of the renderables to it's own rendertarget it then draws it's render target on to the render target provided as a parameter in the render method. 
	 */
	
	protected void drawRenderQueue(RenderTarget Target, CVector2D ParentPosition){
		RenderableArea.clear();
		
		while(!RenderQueue.isEmpty()){
			RenderQueue.poll().render(RenderableArea, this.ViewPosition.scalarMultiply(-1));
		}
		
	}

	protected void draw(RenderTarget Target, CVector2D DesiredRenderPosition) {
		Target.getGraphics().setColor(Color.BLACK);
		Target.getGraphics().drawImage(RenderableArea.getImage(), null, (int)Math.floor(DesiredRenderPosition.getX()), (int)Math.floor(DesiredRenderPosition.getY()));
	}

}
