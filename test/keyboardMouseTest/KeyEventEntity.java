package test.keyboardMouseTest;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import com.Capslock.Engine.entity;
import com.Capslock.Engine.screen;
import com.Capslock.Engine.game;

public class KeyEventEntity extends entity{
	
	static int duration = 3000;
	
	game maingame;
	int size = 15;
	
	String button;
	String event;
	
	long starttime;

	public KeyEventEntity(screen mainscreen, double x, double y, game maingame, String button, String event) {
		super(mainscreen, x, y);
		
		this.maingame = maingame;
		this.button = button;
		this.event = event;
		
		this.starttime = System.currentTimeMillis();
	}

	@Override
	public void setup() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void draw(){
		if(System.currentTimeMillis()>=starttime + duration){
			maingame.destroyentity(this);
			return;
		}
		
		mainscreen.screengraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1-(float)(System.currentTimeMillis()-starttime)/(float)duration));
		
		mainscreen.screengraphics.setColor(Color.yellow);
		
		mainscreen.screengraphics.drawLine((int)(this.x-20), (int)this.y, (int)(this.x+20), (int)this.y);
		mainscreen.screengraphics.drawLine((int)(this.x), (int)this.y-20, (int)(this.x), (int)this.y+20);
		
		mainscreen.screengraphics.setFont(new Font(Font.DIALOG_INPUT, Font.PLAIN, 11));
		
		mainscreen.screengraphics.drawString(button, (int)x+7, (int)y-30);
		mainscreen.screengraphics.drawString(event, (int)x+7, (int)y-20);
	}

}
