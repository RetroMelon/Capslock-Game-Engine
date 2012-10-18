package test.RenderableTest;

import com.Capslock.Engine.Math.CVector2D;
import com.Capslock.Engine.Physics.Physical;
import com.Capslock.Engine.Render.RenderTarget;

public class BasicEntity extends Physical {
	
	public BasicEntity(){
		this.Position = new CVector2D(0, 0);
		
		this.Friction = new CVector2D(1000, 1000);
	}

	@Override
	protected void draw(RenderTarget Target, CVector2D DesiredRenderPosition) {
		// TODO Auto-generated method stub
	}

}
