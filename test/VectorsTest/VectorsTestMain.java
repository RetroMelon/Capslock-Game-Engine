package test.VectorsTest;

import com.Capslock.Engine.Math.CTrig;
import com.Capslock.Engine.Math.CVector2D;

public class VectorsTestMain {
	
	public static void main(String[] args){
		
		for(int i = 0; i<10; i++){
			test();
		}
	}
	
	public static void test(){
		long currenttime = System.currentTimeMillis();
		
		for(int i = 0; i<100000; i++){
			
			CVector2D v1 = new CVector2D(1.21345324, 4);
			CVector2D v2 = new CVector2D(3.21345324, 2.21345324);
			CVector2D v3 = new CVector2D(0, -2.21345324);
			CVector2D v4 = new CVector2D(-2, 0);
			
			v1.setX(3.21345324);
			v2.setX(3.21345324);
			v3.setX(3.21345324);
			v1.setX(3.21345324);
			v2.setX(3.21345324);
			v3.setX(3.21345324);
			v1.setX(3.21345324);
			v2.setX(3.21345324);
			v3.setX(3.21345324);
			v1.setX(3.21345324);
			v2.setX(3.21345324);
			v3.setX(3.21345324);
			v1.setX(3.21345324);
			v2.setX(3.21345324);
			v3.setX(3.21345324);
			v1.setX(3.21345324);
			v2.setX(3.21345324);
			v3.setX(3.21345324);
			v1.setX(3.21345324);
			v2.setX(3.21345324);
			v3.setX(3.21345324);
			
			
			v3 = v1.add(v2);
			
			v3 = v3.scalarMultiply(3.445);
			
			v4.setBearing(CTrig.toRadians(90));
			
			v2.getMagnitude();
			v1.getMagnitude();
			v2.getMagnitude();
			v1.getMagnitude();
			v2.getMagnitude();
			v1.getMagnitude();
			v2.getMagnitude();
			v1.getMagnitude();
			v2.getMagnitude();
			v1.getMagnitude();
			v2.getMagnitude();
			v1.getMagnitude();
			v2.getMagnitude();
			v1.getMagnitude();
			v2.getMagnitude();
			v1.getMagnitude();
			v2.getMagnitude();
			v1.getMagnitude();
			
			v3.getBearing();
		
		}
		
		System.out.println("Time taken with Vector2D:  "+(System.currentTimeMillis()-currenttime));

	}
	
}
