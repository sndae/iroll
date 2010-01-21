package com.coolyu;

import android.widget.*;
import android.widget.AbsoluteLayout.LayoutParams;

/* This class only controls the (x,y) of the "ball" ImageView at
 * any time given the sensor values.
 * Should be instantiated by other Activity classes
 */
public class BallRolling implements Direction {
	/* We don't really "new" these objects. We only set references. */
	private PointFloat p;    // The x, y coordinates of the ball's top-left
	private Physicist Newton;
	private ImageView ball;

    public BallRolling(PointFloat p, Physicist Newton, ImageView ball) {
        this.p = p;
        this.Newton = Newton;        
        this.ball = ball;
    }
    
    /* Method to roll the ball. Will call Physicist.calculate() */
    public void roll(float sensorX, float sensorY, float sensorZ) {
		/* [1] Ask the Physicist to calculate the desired p(x,y)
		 * Pass -sensorY because the "+y" direciton of normal vector is
		 * the "-y" direction of the screen coordinates
		 */
		Newton.calculate(sensorX, -sensorY, sensorZ, p);
		
		/* [2] Adjust the ball's position according to (x,y) */
		ball.setLayoutParams(new AbsoluteLayout.LayoutParams
			(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, (int)p.x, (int)p.y));	
    }
    
}
