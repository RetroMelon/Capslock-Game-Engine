package test.RenderableTest;

import java.awt.Color;

import com.Capslock.Engine.Math.CVector2D;
import com.Capslock.Engine.Render.Camera;
import com.Capslock.Engine.Render.RenderTarget;
import com.Capslock.Engine.Render.Renderable;

public class AxesRenderable extends Renderable{
	
	private Camera camera;
	
	public AxesRenderable(Camera c){
		this.camera = c;
	}

	@Override
	protected void draw(RenderTarget Target, CVector2D DesiredRenderPosition) {
		Target.getGraphics().setColor(Color.red);
		
		//horizontal
		Target.getGraphics().drawLine((int)DesiredRenderPosition.getX(), 0, (int)DesiredRenderPosition.getX(), (int)camera.getSizeY());
		
		//vertical
		Target.getGraphics().drawLine(0, (int)DesiredRenderPosition.getY(), (int)camera.getSizeX(), (int)DesiredRenderPosition.getY());
		
		Target.getGraphics().drawString("(0, 0)", (int)DesiredRenderPosition.getX()+5, (int)DesiredRenderPosition.getY()-5);
	}

}
