package test.EverythingTestClient;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.Capslock.Engine.game;
import com.Capslock.Engine.Controls.Keyboard.CKeyEvent;
import com.Capslock.Engine.Controls.Keyboard.CKeyInfo;
import com.Capslock.Engine.Controls.Mouse.CMouseEvent;
import com.Capslock.Engine.Math.CVector2D;
import com.Capslock.Engine.Render.RenderTarget;
import com.Capslock.Engine.Utility.EngineInstance;

public class EverythingTestClientConnecter extends game {
	
	private ArrayList<Inputbox> boxlist = new ArrayList<Inputbox>();
	Inputbox i = new Inputbox();

	public EverythingTestClientConnecter(EngineInstance e) {
		super(e);
	}

	@Override
	public void init() {
		
		for(int j = 0; j<2; j++){
			boxlist.add(new Inputbox());
			boxlist.get(j).setPosition(new CVector2D(100, 120*j+30));
		}
		
		selecti(boxlist.get(0));
	}

	private void selecti(Inputbox j){
		i = j;
		for(int x = 0; x<boxlist.size(); x++){
			boxlist.get(x).deselect();
		}
		i.select();
	}
	
	@Override
	public void input() {
		CKeyEvent k = this.Engine.getKeyboard().nextKeyEvent();
		while(k != null){
			
			if(k.getEventType() == CKeyEvent.KEYEVENT_PRESSED){
				
				if(k.getKeyType() == CKeyInfo.KEY_TYPE_CHARACTER || k.getKeyType() == CKeyInfo.KEY_TYPE_SYMBOL){
					processcharacter(k);
				}else if(k.getKeyType() == CKeyInfo.KEY_TYPE_INPUT){
					processinputkey(k);
				}
				
			}
			
			k = this.Engine.getKeyboard().nextKeyEvent();
		}
		
		CMouseEvent m = this.Engine.getMouse().nextMouseEvent();
		while(m != null){
			
			if(m.getEventType() == CMouseEvent.MOUSEEVENT_PRESSED){
				checkmousecollisions(m);
			}
			
			m = Engine.getMouse().nextMouseEvent();
		}
	}
	
	private void checkmousecollisions(CMouseEvent m){
		for(int j = 0; j<boxlist.size(); j++){
			if(boxlist.get(j).getCollidableArea().collides(m.getPosition())){
				selecti(boxlist.get(j));
			}
		}
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw() {
		RenderTarget r = new RenderTarget(this.Engine.getScreen().getsizex(), this.Engine.getScreen().getsizey());
		
		for(int j = 0; j<boxlist.size(); j++){
			boxlist.get(j).render(r, new CVector2D(0, 0));
		}
		
		this.Engine.getScreen().drawRenderTarget(r);
		
		r.clear();
	}

	@Override
	public int quit() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private void processcharacter(CKeyEvent k){
		i.setMessage(i.getMessage()+k.getKeyString());
	}
	
	private void processinputkey(CKeyEvent k){
		if(k.getKeyCode() == KeyEvent.VK_SPACE){
			i.setMessage(i.getMessage()+" ");
		}else if(k.getKeyCode() == KeyEvent.VK_BACK_SPACE && k.getEventType()==CKeyEvent.KEYEVENT_PRESSED){
			setupdeleterthread();
		}
	}
	
	private void deletelastchar(){
		if( i.getMessage().length()>=1){
			i.setMessage(i.getMessage().substring(0, i.getMessage().length()-1));
		}
	}
	
	private void setupdeleterthread(){
			new Thread(new DeleterThread()).start();
	}
	
	private class DeleterThread implements Runnable{

		//time between each deletion in milliseconds.
		private static final int timedelay = 200;

		
		public void run() {
			while(Engine.getKeyboard().keyPressed(KeyEvent.VK_BACK_SPACE)){
				deletelastchar();
				try{Thread.sleep(timedelay);}catch(Exception e){}
			}	
		}

	}

}
