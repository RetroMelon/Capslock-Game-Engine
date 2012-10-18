package com.Capslock.Engine.Render;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.Capslock.Engine.Math.CVector2D;

/*
 * 
 */

public class RenderTarget {
	
	private static final int IMAGE_TYPE = BufferedImage.TYPE_INT_ARGB;
	
	protected CVector2D Size;
	
	protected BufferedImage Image;
	protected Graphics2D Graphics;
	
	/*
	 * This float is used to keep track of the level of transparency at which renderables will be drawn. changing this value does not actually directly change the transparency.
	 * Rather, this value is merely to keep track of the transparency level so it can be returned if requested by getTransparency()
	 */
	
	protected float TransparencyLevel = 1f;

	
	public RenderTarget(CVector2D Size){
		this.Size = Size;
		
		Image = new BufferedImage((int)Size.getX(), (int)Size.getY(),IMAGE_TYPE);
		Graphics = (Graphics2D) Image.getGraphics();
	}
	
	public RenderTarget(int SizeX, int SizeY){
		this.Size = new CVector2D(SizeX, SizeY);
		
		Image = new BufferedImage((int)Size.getX(), (int)Size.getY(), IMAGE_TYPE);
		Graphics = (Graphics2D) Image.getGraphics();
	}
	
	public RenderTarget(BufferedImage Image){
		this.Image = Image;
		
		Size = new CVector2D(Image.getWidth(), Image.getHeight());
		Graphics = (Graphics2D) Image.getGraphics();
	}
	
	/*
	 * The render target provides a simple utility to change the transparency level of the object being drawn. this transparency level is reset whenever a new renderable is drawn.
	 * The resetting is performed by the drawRenderable method.
	 */
	
	public void setTransparency(float TransparencyLevel){
		this.TransparencyLevel = TransparencyLevel;
		Graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, TransparencyLevel));
	}
	
	public float getTransparency(){
		return TransparencyLevel;
	}
	
	/*
	 * The following methods are the get and set methods for the objects such as the bufferedImage and, most importantly, the graphics 2d object.
	 */
	
	public BufferedImage getImage(){
		return Image;
	}
	
	public Graphics2D getGraphics(){
		return Graphics;
	}
	
	/*
	 * This method is simply a convenience method. sometimes in object oriented programming it's nice to call a method on the thing which is actually being manipulated.
	 * this method also resets any transparency changes that the renderable may have made to the graphics object as we do not want all of our objects to accidentally change transparent
	 */

	public void drawRenderable(Renderable Renderable, CVector2D Position){
		Renderable.render(this, Position);
		setTransparency(1f);
	}
	
	/*
	 * This method allows the user to clear the buffered image back to it's fully transparent state. this is useful as it is more efficient that deallocating the old buffered image and replacing it with a new one.
	 */
	
	public void clear(){
		Composite OldComposite = Graphics.getComposite();
		
		Graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
		Graphics.setColor(new Color(0, 0, 0, 0));
		Graphics.fillRect(0, 0, Image.getWidth(), Image.getHeight());
		
		Graphics.setComposite(OldComposite);
	}

}
