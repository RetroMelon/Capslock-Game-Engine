package test.RenderableTest;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import com.Capslock.Engine.Math.CTrig;
import com.Capslock.Engine.Math.CVector2D;
import com.Capslock.Engine.Physics.Collidable;
import com.Capslock.Engine.Physics.CollisionBox;
import com.Capslock.Engine.Render.RenderTarget;
import com.Capslock.Engine.Render.Renderable;

public class RandomRenderable extends Collidable{
	
	private ArrayList<Renderable> children = new ArrayList<Renderable>();
	
	Random r = new Random(System.currentTimeMillis());
	
	public RandomRenderable(CVector2D position) {
		this.Position = position;
		
		this.CollidableArea = new CollisionBox(0, 0, 50, 50, this);
	}

	public RandomRenderable() {
		this.CollidableArea = new CollisionBox(0, 0, 50, 50, this);
		
	}
	
	public void removechild(){
		if(!(children.size()>0))return;
		children.remove(children.size()-1);
	}
	
	public void addchild(){
		if(!(children.size()<Integer.MAX_VALUE))return;
		
			int magnitude = r.nextInt()%70;
			magnitude = 25;
			double angle = CTrig.toRadians(Math.abs(r.nextInt()));

			CVector2D pos = new CVector2D();
			pos.setPolarCoordinates(magnitude, angle);
			pos = pos.add(new CVector2D(50, 50));
			children.add(new RandomRenderable2(pos, new Color(Math.abs(r.nextInt())%256, Math.abs(r.nextInt())%256, Math.abs(r.nextInt())%256)));

	}

	protected void setupRenderQueue(){
		RenderQueue.add(this.getCollidableArea());
		
		for(int i = 0; i<children.size(); i++){
			this.RenderQueue.add(children.get(i));
		}
	}
	
	@Override
	protected void draw(RenderTarget Target, CVector2D DesiredRenderPosition) {
		Target.setTransparency(0.2f);
		
		Target.getGraphics().setColor(Color.YELLOW);
		Target.getGraphics().fillRect((int)DesiredRenderPosition.getX(), (int)DesiredRenderPosition.getY(), 50, 50);
		
		Target.getGraphics().setColor(Color.GREEN);
		Target.getGraphics().drawRect((int)DesiredRenderPosition.getX(), (int)DesiredRenderPosition.getY(), 50, 50);
		
		Target.setTransparency(1f);
		
		Target.getGraphics().setColor(Color.RED);
		Target.getGraphics().drawString(""+this.getPosition().getX()+",  "+this.getPosition().getY(), (int)DesiredRenderPosition.getX()+5, (int)DesiredRenderPosition.getY()+10);

		Target.getGraphics().setColor(Color.RED);
		Target.getGraphics().drawString(""+children.size()+" Children", (int)DesiredRenderPosition.getX()+5, (int)DesiredRenderPosition.getY()+10+15);

	}

}
