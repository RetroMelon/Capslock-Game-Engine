package test.RenderableTest;

import com.Capslock.Engine.Utility.EngineInstance;

public class RenderableTestMain {
	
	public static void main(String[] args){
		EngineInstance e = new EngineInstance("RENDERING TEST", 1080, 640);
		
		RenderableTestGame r = new RenderableTestGame(e);
		r.run();
	}

}
