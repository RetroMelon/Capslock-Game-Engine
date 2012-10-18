package test.EverythingTestClient;

import java.awt.Color;
import java.awt.Font;

import com.Capslock.Engine.Debug.Debug;
import com.Capslock.Engine.Math.CVector2D;
import com.Capslock.Engine.Physics.Collidable;
import com.Capslock.Engine.Physics.CollisionBox;
import com.Capslock.Engine.Render.RenderTarget;

public class Inputbox extends Collidable {

	private int textsize = 12;
	private String message = "Test Message";
	private Font mainfont = new Font(Font.DIALOG_INPUT, Font.PLAIN, textsize);
	
	private Color underlinecolor = Color.CYAN;
	
	public Inputbox(){
		updatecollisionbox();
	}
	
	protected void draw(RenderTarget Target, CVector2D DesiredRenderPosition) {
		
		Target.getGraphics().setFont(mainfont);
		Target.getGraphics().setColor(Color.white);
		
		Target.getGraphics().drawString(message, (int)DesiredRenderPosition.getX(), (int)DesiredRenderPosition.getY()+textsize);
		
		int messagelength = (int)Target.getGraphics().getFontMetrics().stringWidth(message);
		
		Target.getGraphics().setColor(underlinecolor);
		
		Target.getGraphics().drawLine((int)DesiredRenderPosition.getX(), (int)DesiredRenderPosition.getY()+textsize+2, (int)DesiredRenderPosition.getX()+(messagelength < 50 ? 50 : messagelength), (int)DesiredRenderPosition.getY()+textsize+2);
		
		//Target.drawRenderable(this.CollidableArea, this.getPosition());
	}
	
	public String getMessage(){return message;}
	
	public void setMessage(String newmessage){
		Debug.println("INPUTBOX", "UPDATED MESSAGE TO: "+message);
		this.message = newmessage;
		updatecollisionbox();
	}
	
	private void updatecollisionbox(){
		this.CollidableArea = new CollisionBox(0, -5, (message.length()*9 > 50 ? message.length()*9 : 50)+5, 5+5+textsize, this);
	}

	public void deselect() {
		underlinecolor = Color.cyan;
	}
	
	public void select(){
		underlinecolor = Color.orange;
	}

}
