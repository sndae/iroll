package com.coolyu;

import android.widget.TextView;

public class Physicist implements Direction {
	/* Define mathematical constants */
	public final float PI = (float) Math.PI; // Avoid using "double" type
	public float SIN[];  // sin table
	public float COS[];  // cos table
	public final float DEG_TO_RAD = PI / 180.f;  // Multiplier for DEG->RAD
	public final float RAD_TO_DEG = 180.f / PI;  // Multiplier for RAD->DEG
	
	/* Define physics parameters 
	 * Normalize such that mass = 1 */	
	private final float g = (float) 500;  // gravity
	private final float deltaT = (float) 1e-2;  // Time interval between two sensor changes
    // The multiplier of speed after collision (coefficient of restitution)
	private final float collision = 0.75f;
	
	/* Define internal physics variables */
	private int goAngle = 0;  // The directional angle in DEGREES at which the ball will go.
	private float goAngleFloat = 0.f;  // The temporary float value for goAngle
	private int rampAngle = 0;  // Ramp angle in degrees [for table lookup]
	private float rampAngleFloat = 0.f;  // The temporary float value rampAngle

	private float a = 0.f;    // Acceleration magnitide
	private float a_x = 0.f;  // Acceleration in x
	private float a_y = 0.f;  // Acceleration in y
	private float v_x = 0.f;  // Velocity in x
	private float v_y = 0.f;  // Velocity in y
	
	public Physicist() {
        /* Fill in the sine and cosine tables */
        SIN = new float[ 360 ];
        COS = new float[ 360 ];
        for (int i = 0; i < 360; i++) {
        	SIN[ i ] = (float) Math.sin((float) i * DEG_TO_RAD);
        	COS[ i ] = (float) Math.cos((float) i * DEG_TO_RAD);
        }		
	}
	
	public void calculate(float sensorX, float sensorY, float sensorZ, PointFloat p) {
		/* [1] Compute the goAngle (keep it in float) */
		goAngleFloat = (float) Math.atan(sensorY / sensorX) * 180.f / PI;
		// Modify for II, III quadrants
		if (sensorX < 0) {
			goAngleFloat += 180;
		}
		// Modify for IV quadrant
		else if (sensorY < 0) {
			goAngleFloat += 360;
		}
		// Cut the fraction
		goAngle = (int) goAngleFloat;
		//assert goAngle >= 0 && goAngle < 360;      		
		
		/* [2] Compute rampAngle
		 * Actually, cos(rampAngle) equals the "directional cosine" between
		 * the normal vector and z-axis.
		 * cos(rampAngle) = z / ||v||
		 */
		rampAngleFloat = (float) Math.acos(sensorZ / 
			(float) Math.sqrt(sensorX * sensorX + sensorY * sensorY + sensorZ * sensorZ));
		rampAngle = (int) (rampAngleFloat * RAD_TO_DEG);
		
		/* [3] Compute accelerations, velocities, and positions */
		// Acceleration = g * (sin) 
		a = g * (SIN[rampAngle]);
		a_x = a * COS[goAngle];
		a_y = a * SIN[goAngle];

		v_x += a_x * deltaT;
		v_y += a_y * deltaT;
		p.x += v_x * deltaT;
		p.y += v_y * deltaT;		
	}
	
	public void calculateAndShow(float sensorX, float sensorY, float sensorZ, PointFloat p, TextView v) {
		// call calculate()
		calculate(sensorX, sensorY, sensorZ, p);
		v.setText(
    		"goAngle[deg] = " + goAngle + 
    		"\nRamp Angle[deg] = " + rampAngle +
    		"\na = " + a + "\na_x = " + a_x + ",  a_y = " + a_y +
    		"\nv_x = " + v_x + ",  v_y = " + v_y +
    		"\nx = " + p.x + ",  y = " + p.y
    	);		
	}
	
	/* Reset all the motion quantities to zero */
	public void reset() {
		a = 0.f;   
		a_x = 0.f; 
		a_y = 0.f; 
		v_x = 0.f; 
		v_y = 0.f; 
	}
	
	/* Collision: Hitting the wall */
	public void hitWall(int direction) {
		switch (direction) {
			case LEFT:
			case RIGHT:
				v_x = -v_x * collision;
				break;
			case TOP:
			case BOTTOM:
				v_y = -v_y * collision;
				break;
		}
	}
}

/* 2-D coordinate point: May normally be <float> or <int> */
class PointFloat {
	public float x;    // Position in x
	public float y;    // Position in y
	PointFloat() {
		this.x = 0.f;
		this.y = 0.f;
	}
	PointFloat(float x, float y) {
		this.x = x;
		this.y = y;
	}
	public void setValue(float x, float y) {
		this.x = x;
		this.y = y;
	}
}

/* int equivalent. Writing as Java generics causes problems in Physicist's methods */
class PointInt {
	public int x;
	public int y;
	PointInt() {
		this.x = 0;
		this.y = 0;
	}
	PointInt(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
