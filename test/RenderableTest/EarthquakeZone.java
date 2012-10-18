package test.RenderableTest;

import java.awt.Color;

import com.Capslock.Engine.Math.CVector2D;
import com.Capslock.Engine.Physics.Collidable;
import com.Capslock.Engine.Physics.CollisionBox;
import com.Capslock.Engine.Render.RenderTarget;

public class EarthquakeZone extends Collidable{
	
	int sizex = 320;
	int sizey = 200;

	public EarthquakeZone(){
		this.Position = new CVector2D(200, 200);
		this.CollidableArea = new CollisionBox(0, 0, sizex, sizey, this);
		
	}

	protected void draw(RenderTarget Target, CVector2D DesiredRenderPosition) {
		Target.getGraphics().setColor(Color.RED);
		Target.getGraphics().drawRect((int)DesiredRenderPosition.getX(), (int)DesiredRenderPosition.getY(), sizex, sizey);
		Target.getGraphics().drawString("Earthquake Zone", (int)DesiredRenderPosition.getX()+3, (int)DesiredRenderPosition.getY()+13);
		
		Target.drawRenderable(this.getCollidableArea(), DesiredRenderPosition.subtract(this.getCollidableArea().getPosition()));
	}

}
