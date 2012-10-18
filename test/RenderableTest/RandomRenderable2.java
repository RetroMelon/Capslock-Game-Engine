package test.RenderableTest;

import java.awt.Color;
import java.util.Random;

import com.Capslock.Engine.Math.CTrig;
import com.Capslock.Engine.Math.CVector2D;
import com.Capslock.Engine.Render.RenderTarget;
import com.Capslock.Engine.Render.Renderable;

public class RandomRenderable2 extends Renderable{
	
	Color Colour;
	
	int size = 20;
	
	public RandomRenderable2(CVector2D v, Color c){
		this.Colour = c;
		this.Position = new CVector2D(v);

		new Thread(new Orbiter()).start();
	}
	
	protected void setupRenderQueue(){
		
	}

	@Override
	protected void draw(RenderTarget Target, CVector2D DesiredDrawingPosition) {
		Target.getGraphics().setColor(Colour);
		Target.getGraphics().fillOval((int)DesiredDrawingPosition.getX(), (int)DesiredDrawingPosition.getY(), size, size);
		
		Target.getGraphics().setColor(Color.white);
		Target.getGraphics().drawOval((int)DesiredDrawingPosition.getX(), (int)DesiredDrawingPosition.getY(), size, size);
	}
	
	private class Orbiter implements Runnable{

		@Override
		public void run() {
			double speed = (Math.abs(new Random().nextDouble())%1)+1;
			while(true){
				Position.setBearing(Position.getBearing()+CTrig.toRadians(360)/200*speed);
				try{Thread.sleep(10);}catch(Exception e){}
			}
			
		}

	}

}
