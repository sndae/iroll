package com.coolyu;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.*;


public class TestRolling extends Activity implements View.OnClickListener, Direction, PixelInformation {
	SensorManager mySM;
	SensorListener mySL;
	ImageView ball;
	PointFloat p;
	Physicist Newton;
	BallRolling ballBoy;
	private Button backButton;
	private Button resetButton;
	/* Initial (x,y) value */
	private final float initX = (AVAILABLE_WIDTH - BALL_SIZE) / 2.f;
	private final float initY = BUTTON_HEIGHT + (AVAILABLE_HEIGHT - BALL_SIZE) / 2.f;
	/* The four boundaries */
	private final float leftBound = 0.f;
	private final float topBound = BUTTON_HEIGHT;
	private final float rightBound = AVAILABLE_WIDTH - BALL_SIZE;
	private final float bottomBound = BUTTON_HEIGHT + AVAILABLE_HEIGHT - BALL_SIZE;
	/* Small constant */
	private final float EPSILON = 0.1f;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rolling);
        
        ball = (ImageView) findViewById(R.id.rollBall);
        backButton = (Button) findViewById(R.id.rollBack);
        backButton.setOnClickListener(this);
        resetButton = (Button) findViewById(R.id.rollReset);
        resetButton.setOnClickListener(this);        
        
        /* This is where we really create Point, Physicist, BallRolling objects */
        p = new PointFloat(initX, initY);
        Newton = new Physicist();
        ballBoy = new BallRolling(p, Newton, ball);
        mySM = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        
		mySL = new SensorListener() {
			// Define a class here
			public void onSensorChanged(int sensor, float[] values) {
				/* Everything we need to do when the sensor data change */
				
				/* [1] Read in the normal vector (x,y,z) */
				final float sensorX = values[SensorManager.DATA_X];
				final float sensorY = values[SensorManager.DATA_Y];
				final float sensorZ = values[SensorManager.DATA_Z];
				
				/* [2] Ask the BallRolling object to do with ball rolling and display */
				ballBoy.roll(sensorX, sensorY, sensorZ);
				
				/* [3] Detect wall hitting */
				// Left
				if (p.x <= leftBound) {
					// Adjust the position to be within boundaries
					p.x = leftBound + EPSILON;
					Newton.hitWall(LEFT);					
				}
				// TOP
				if (p.y <= topBound) {
					p.y = topBound + EPSILON;
					Newton.hitWall(TOP);
				}
				// RIGHT
				if (p.x >= rightBound) {
					p.x = rightBound - EPSILON;
					Newton.hitWall(RIGHT);
				}
				// BOTTOM
				if (p.y >= bottomBound) {
					p.y = bottomBound - EPSILON;
					Newton.hitWall(BOTTOM);
				}
				
			} // End of onSensorChanged()
			
			public void onAccuracyChanged(int sensor, int values) {
				// Empty, but we need this method.
			}
		}; // End of class SensorListener definition 
		
        // The SensorManager registers a SensorListener
        mySM.registerListener(mySL, SensorManager.SENSOR_ACCELEROMETER, SensorManager.SENSOR_DELAY_GAME);        
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	// Return resources
    	mySM.unregisterListener(mySL);
    	mySL = null;
    }
    
    public void onClick(View v) {
    	if (v == backButton) {
    		finish();
    		System.gc();
    	}
    	else if (v == resetButton) {
    		reset();
    	}
    }
    
    /* Reset ball's position to (initX, initY), and stop all the motion quantities */
    public void reset() {
		ball.setLayoutParams(new AbsoluteLayout.LayoutParams
				(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, (int)initX, (int)initY));
		p.setValue(initX, initY);
		Newton.reset();
    }
}
