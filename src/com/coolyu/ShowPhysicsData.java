package com.coolyu;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.*;
import android.view.View;

/* Show Physics Data */
public class ShowPhysicsData extends Activity implements View.OnClickListener {
	private SensorManager mySM;
	private SensorListener mySL;
	private TextView accView;
	private TextView phyView;
	private PointFloat p;    // The x, y coordinates of the ball's top-left
	private Physicist Newton;
	private Button backButton;	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.physics);
        
        accView = (TextView) findViewById(R.id.accView);
        phyView = (TextView) findViewById(R.id.phyView);
        backButton = (Button) findViewById(R.id.phyBack);
        backButton.setOnClickListener(this);
        // Create a physicist to help us with the calculation of motion
        Newton = new Physicist();
        p = new PointFloat();
        
        
        mySM = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mySL = new SensorListener() {
			// Define a class here
			public void onSensorChanged(int sensor, float[] values) {
				/* Everything we need to do when the sensor data change */
				
				/* [1] Read in the normal vector (x,y,z) */
				final float sensorX = values[SensorManager.DATA_X];
				final float sensorY = values[SensorManager.DATA_Y];
				final float sensorZ = values[SensorManager.DATA_Z];
				accView.setText("sensorX = " + sensorX + "\nsensorY = " + (-sensorY) + "\nsensorZ = " + sensorZ);
		
				/* [2] Ask Newton to calculate and show the physical quantities we want */
				Newton.calculateAndShow(sensorX, sensorY, sensorZ, p, phyView);
			}
			
			public void onAccuracyChanged(int sensor, int values) {
				// Empty, but we need this method.
			}			
		};
		
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
    	// Only one button
    	if (v == backButton) {
    		finish();
    		System.gc();
    	}    	
    }
}
