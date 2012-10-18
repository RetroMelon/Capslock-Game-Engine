package com.Capslock.Engine.Math;


/*
 * The CVector2D class is a simple vector class which consists of a pair of doubles representing the X and Y components of a vector.
 * 
 * The CVector2D class has support for different operations regarding the vector:
 * 
 * 		find "Magnitude"
 * 		find "Bearing" / change to polar coordinate
 * 		multiply by scalar
 * 		translate through addition of another vector
 * 		find "Scalar Product"
 * 
 * if a negative magnitude is set using the set magnitude method, the magnitude is set to positive and 180degrees is added on to the bearing.
 * 
 */

public class CVector2D{
	
	/*
	 * The x and y components of the vector
	 */

	protected double X = 0;
	protected double Y = 0;
	
	/*
	 * Some other information about the vector that is vital when working with physics and geometry.
	 * The bearing is measured in radians.
	 */
	
	protected double Bearing = 0;
	protected double Magnitude = 0;
	
	/*
	 * if this boolean is true the vector will recalculate it's magnitude and bearing before returning it.
	 * if this boolean is false it will not recalculate the magnitude or bearing.
	 * This boolean is set to true whenever the x or y values of the vector are changed and set to false whenever the magnitude and bearing are updated.
	 */
	
	protected boolean PolarRecalculateRequired;

	public CVector2D(){
		PolarRecalculateRequired = true;
	}
	
	public CVector2D(CVector2D Vector){
		this.X = Vector.getX();
		this.Y = Vector.getY();
		
		PolarRecalculateRequired = true;
	}

	public CVector2D(double X, double Y){
		this.X = X;
		this.Y = Y;
		
		PolarRecalculateRequired = true;
	}
	
	/*
	 * We want to include functionality to create a vector given a bearing and a magnitude. Unfortunately the double, double constructor has already been taken up
	 * by the x and y values.
	 * since this is a seldomly required feature anyways, we use a factory method in order to get a new vector with the given magnitude and bearing.
	 */
	
	
	
	/*
	 * The set methods in the Vector are the only methods which should edit the values of the vector.
	 * All other vector arithmetic methods should not edit this vector, instead, simply returning the result of the calculation
	 */
	
	public double getX(){return X;}
	public double getY(){return Y;}

	public void setX(double newX) {
		this.X = newX;
		PolarRecalculateRequired = true;
	}

	public void setY(double newY) {
		this.Y = newY;
		PolarRecalculateRequired = true;
	}
	
	public void setRectangularCoordinates(double newX, double newY){
		this.X = newX;
		this.Y = newY;
		PolarRecalculateRequired = true;
	}
	
	public void setMagnitude(double newMagnitude){
		if(newMagnitude<0){
			this.Bearing += Math.PI;
			this.Magnitude = Math.abs(newMagnitude);
		}else{
			this.Magnitude = newMagnitude;
		}
		
		calculateRectangularProperties();
	}
	
	public void setBearing(double newBearingRadians){
		newBearingRadians = newBearingRadians % ((double)2*(double)Math.PI);
		
		this.Bearing = newBearingRadians;
		
		calculateRectangularProperties();
	}
	
	public void setPolarCoordinates(double newMagnitude, double newBearingRadians){
		newBearingRadians = newBearingRadians % ((double)2*(double)Math.PI);
		this.Bearing = newBearingRadians;
		
		if(newMagnitude<0){
			this.Bearing += Math.PI;
			this.Magnitude = Math.abs(newMagnitude);
		}else{
			this.Magnitude = newMagnitude;
		}

		
		calculateRectangularProperties();
	}
	
	
	/*
	 * This method is used to keep the information about the vector like the x and y values up to date.
	 * whenever a method is called which changes the magnitude or bearing the X and Y values must be recalculated.
	 */
	
	private void calculateRectangularProperties(){
		
		/*
		 * In this method we get the dot product of the vector with another vector which points straight vertically and has a magnitude of 1.
		 * since we know that the x value of the north-pointing vector will be 0, the scalar product must equal the y value of this vector.
		 */
		
		this.Bearing = this.Bearing % (2*Math.PI);
		
		double DotProduct = this.Magnitude * CTrig.cos(this.Bearing);
		
		double XValue = Math.sqrt( Math.pow(Magnitude, 2) - Math.pow(DotProduct, 2));
		
		
		if(this.Bearing>Math.PI){
			XValue = XValue * -1;
		}
		
		
		this.X = XValue;
		this.Y = DotProduct;
		
		//Debug.println("VECTOR2D", "CALCULATERECTANGULARPROPERTIES CALLED WITH MAGNITUDE "+Magnitude+" AND BEARING "+CTrig.toDegrees(Bearing)+"\n:: CALCULATED X "+X+" AND Y"+Y);
	}
	
	/*
	 * This method is used to keep information about a vector such as it's magnitude and bearing
	 * whenever a method is called which changes the vector such as "set" method all of these values are recalculated.
	 * 
	 * This method does not have to be called by arithmetical functions such as add or scalar-multiply because these methods should never directly edit this particular vector.
	 * 
	 * This method will only recalculate the properties if the PolarRecalculateRequired boolean is true. this saves a lot of processing power as it means that the trigonometric calculations
	 * only need to be redone when a vector is changed as these calculations can be lengthy.
	 * 
	 * This simple boolean makes this class approximately 8 times faster during standard use.
	 */
	
	private void calculatePolarProperties(){
		if(!PolarRecalculateRequired)return;
		calculateMagnitude();
		calculateBearing();
		PolarRecalculateRequired = false;
	}
	
	private void calculateMagnitude(){
		this.Magnitude = Math.sqrt( Math.pow(this.getX(), 2)  +  Math.pow(this.getY(), 2)  );
	}
	
	
	private void calculateBearing(){
		
		//we put this safety check in place to prevent a case in which we could divide by 0 later.
		if(this.Magnitude == 0){
			Bearing = 0;
			return;
		}
		
		/*
		 * To calculate the bearing we first create a vector of magnitude 1 that points straight upwards.
		 * we cannot actually technically create a vector, because that vector would then call calculate bearing which would result in a stack overflow.
		 * instead we simply use the coordinates x=0 and y=0 in the dotproduct method.
		 * we then get the scalar product of it and our current vector and use that scalar product along with our magnitudes to find a bearing.
		 * However, this bearing will always be less than 180degrees, so if the vector is pointing left (ie, if the x component is -ve) then we must
		 * subtract the resulting bearing from 360 in order to get our final result.
		 */
		
		//the scalar product
		double DotProduct = this.dotProduct(0, 1);
		
		/*
		 * here we divide the dotproduct by the magnitudes of both vectors. since the north vector's magnitude is 1 we don't need to divide it.
		 * we then perform an inverse cos on the resulting number to get the bearing in radians.
		 */
		
		double Angle = CTrig.icos(DotProduct / this.Magnitude);
		
		/*
		 * Now we check whether we need to subtract the resulting bearing from 360degrees or not.
		 */
		if(this.getX()<0){
			Angle = 2*Math.PI - Angle;
		}
		
		//finally we set the bearing equal to the angle.
		
		this.Bearing = Angle;		
	}
	
	/*
	 * All Arithmetical functions relating to vector arithmetic.
	 * All of these functions return a CVector2D with the updated properties of the vector.
	 * These functions do not change the current vector in any way, instead, just returning a new vector which gives the user the option to replace the current one.
	 */
	
	public CVector2D add(CVector2D Vector){
		CVector2D NewVector = new CVector2D();
		
		NewVector.setX(this.getX() + Vector.getX());
		NewVector.setY(this.getY() + Vector.getY());
		
		return NewVector;
	}
	
	/*
	 * This method subtracts the vector passed as a parameter from the current vector.
	 * if this vector were V1 and the vector passed in were V2 then this method would perform the function:  V1 - V2
	 */
	
	public CVector2D subtract(CVector2D Vector){
		CVector2D NewVector = new CVector2D();
		
		NewVector.setX(this.getX() - Vector.getX());
		NewVector.setY(this.getY() - Vector.getY());
		
		return NewVector;
	}
	
	/*
	 * this method performs the functionality of multiplying the vector by a scalar value.
	 * as expected every component within the vector is multiplied by this scalar value.
	 */
	
	public CVector2D scalarMultiply(double Scalar){
		CVector2D NewVector = new CVector2D();
		
		NewVector.setX(this.getX() * Scalar);
		NewVector.setY(this.getY() * Scalar);
		
		return NewVector;
	}
	
	public double dotProduct(CVector2D Vector){
		return this.getX()*Vector.getX() + this.getY()*Vector.getY();
	}
	
	public double dotProduct(int x, int y){
		return this.getX()*x + this.getY()*y;
	}
	
	/*
	 * These method relate to other pieces of information you might want to know about a vector including the bearing at which it is pointing and it's length
	 */
	
	// Get methods
	public double getBearing(){
		calculatePolarProperties();
		return Bearing;
	}
	
	public double getMagnitude(){
		calculatePolarProperties();
		return Magnitude;
	}
	
	/*
	 * Returns a string representation of the vector as a convenience for debugging purposes
	 */
	public String getString(){
		return ("Vector: Rec( "+X+"  ,  "+Y+" )   Pol( "+CTrig.toDegrees(Bearing)+"Deg  ,  "+Magnitude+" )");
	}
	
	/*
	 * this method returns a unit vector for this vector. this method does the calculation to find the unit vector every time it is called as opposed to having it update
	 * when the set methods are called. This implementation was chosen due to how infrequently this method will be required.
	 */
	
	public CVector2D getUnitVector(){
		return this.scalarMultiply((1/this.getMagnitude()));
	}
	
	
	
	

}
