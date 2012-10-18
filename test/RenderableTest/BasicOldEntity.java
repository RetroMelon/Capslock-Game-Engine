package test.RenderableTest;

import com.Capslock.Engine.entity;
import com.Capslock.Engine.screen;

public class BasicOldEntity extends entity{

	public BasicOldEntity(screen mainscreen) {
		super(mainscreen);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setup() {
		x = 0; y = 0;
		
		this.maxdirx = 400;
		this.maxdiry = 400;
		
		this.frictionx = 800;
		this.frictiony = 800;
	}

}
