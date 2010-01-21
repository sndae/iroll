package com.coolyu;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import android.app.Activity;
import android.content.*;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.AbsoluteLayout;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.view.View;

/* Map symbols:
 * 0: Space
 * 1: Obstacles
 * 2, 3, 4, ...: Door1, Door2, Door3, ... Have two for now
 * 12, 13, 14, ...: Key1, Key2, Key3, ... Have two for now
 * 20: Goal
 * Should eventually be enum or final
 */

/* Remember, since we're not using Android 2D graphics,
 * screen display and map information are complete independent.
 * They should be handled separately.
 */

public class PlayGame extends Activity 
	implements View.OnClickListener, PixelInformation {
	
	private SensorManager mySM;
	private SensorListener mySL;
	
	private ImageView ballView;
	private ImageView key1View;
	private ImageView key2View;
	private ImageView goalView;
	private ImageView mapView;
	private Button backButton;
	private Button resetButton;
	
	private PointFloat p;
	private Physicist Newton;
	private BallRolling ballBoy;
	private DataReader reader;
	private GameData data;
	
	BufferedReader finMap;
	BufferedReader finBall;

	/* Initial (x,y) value, not considering the BUTTON_HEIGHT */
	/** These values should eventually be read from mapXXX.txt **/
	private final float initX = 50.f;
	private final float initY = 76.f;
	private final float key1X = 46.f;
	private final float key1Y = 457.f;
	private final float key2X = 112.f;
	private final float key2Y = 261.f;
	private final float goalX = 385.f;
	private final float goalY = 452.f;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        ballView = (ImageView) findViewById(R.id.gameBall);
        moveImageTo(ballView, (int)initX, (int) (initY + BUTTON_HEIGHT));
        mapView = (ImageView) findViewById(R.id.map);
        backButton = (Button) findViewById(R.id.gameBack);
        backButton.setOnClickListener(this);
        resetButton = (Button) findViewById(R.id.gameReset);
        resetButton.setOnClickListener(this);
        
        /* This is where we really create Point, Physicist, BallRolling objects */
        p = new PointFloat(initX, initY + BUTTON_HEIGHT);
        Newton = new Physicist();
        ballBoy = new BallRolling(p, Newton, ballView);
        
        /* File read of map and ball data */
        data = new GameData();
        reader = new DataReader();
        reader.readMapData(getResources().openRawResource(R.raw.map_r0_480x542), data);
        /** Also memorizes the positions of doors in ArrarLists **/
        reader.readBallData(getResources().openRawResource(R.raw.balldata), data);
        
        /* Create the keys and goal */
       	key1View = (ImageView) findViewById(R.id.key1);
        moveImageTo(key1View, (int)key1X, (int) (key1Y + BUTTON_HEIGHT));
       	key2View = (ImageView) findViewById(R.id.key2);
        moveImageTo(key2View, (int)key2X, (int) (key2Y + BUTTON_HEIGHT));
        goalView = (ImageView) findViewById(R.id.goal);
        moveImageTo(goalView, (int)goalX, (int) (goalY + BUTTON_HEIGHT));
        
        /* Fill the keys' and goal's reachable area according to data */
        /** Also memorizes the keys' positions in ArrayLists **/
        fillKeysAndGoal();
        
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
				
				/* [3] Detect any map objects within the ball */
				/* For any pixel "reachable" to the Point p(x,y) */
				for (int y = data.ballYStart, i = 0; y <= data.ballYEnd; y++, i++) {
					for (int x = data.ballX[i].start; x <= data.ballX[i].end; x++) {
						char pixel = data.map[(int)p.y - (int)BUTTON_HEIGHT + y][(int)p.x + x];
						
						switch (pixel) {
							/* Obstacles */
							case 1:
							case 2:
							case 3:
								resetButton.setText("You Lost!!");
								reset();
								break;
								
							/* Key 1 */
							case 12:
								resetButton.setText("Door 1 Opened");								
								/* Changes on ImageViews */
								key1View.setVisibility(View.INVISIBLE);
								mapView.setImageResource(R.drawable.map_r1_480x542);					
								/* Clear the key1 and door1 areas on data.map */
								clearKeyAndDoor(1);
								break;
							
							/* Key 2 */
							case 13:
								resetButton.setText("Door 2 Opened");
								/* Changes on ImageViews */
								key2View.setVisibility(View.INVISIBLE);
								mapView.setImageResource(R.drawable.map_r2_480x542);					
								/* Clear the key2 and door2 areas on data.map */
								clearKeyAndDoor(2);					
								break;
								
							/* Reach the goal!! */
							case 20:
								resetButton.setText("You Won!!");
								reset();
								break;
						}  // End of switch (pixel)
					}
				}
				// To be completed ...

				
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
    		((Button) v).setText("reset");
    		reset();
    	}
    }
     
    /* Reset ball's position to (initX, initY), and stop all the motion quantities */
    public void reset() {
    	moveImageTo(ballView, (int) initX, (int) (initY + BUTTON_HEIGHT));
		p.setValue(initX, initY + BUTTON_HEIGHT);
		Newton.reset();
		/* Reset the ImageViews */
		mapView.setImageResource(R.drawable.map_r0_480x542);
		key1View.setVisibility(View.VISIBLE);
		key2View.setVisibility(View.VISIBLE);
		/* Fill the keys' and doors' reachable area according to ball data */
		fillKeysAndDoors();
    }
    
    /* Move the ImageView to a specified (int x,int y) position */
    public void moveImageTo(ImageView v, int x, int y) {
		v.setLayoutParams(new AbsoluteLayout.LayoutParams
			(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, x, y));		
    }
    
    /* Called initially:
     * Fill the keys' and goal's reachable area according to data.
     * Also memorizes the positions of doors in ArrayLists.
     */
    public void fillKeysAndGoal() {
    	data.key1 = new ArrayList<PointInt>();
    	data.key2 = new ArrayList<PointInt>();
    	for (int y = data.ballYStart, i = 0; y <= data.ballYEnd; y++, i++) {
    		for (int x = data.ballX[i].start; x <= data.ballX[i].end; x++) {
    			int temp1X = (int) key1X + x, temp1Y = (int) key1Y + y;
    			int temp2X = (int) key2X + x, temp2Y = (int) key2Y + y;
    			data.map[temp1Y][temp1X] = (char)12;
    			data.map[temp2Y][temp2X] = (char)13;
    			data.map[(int) goalY + y][(int) goalX + x] = (char)20;
    			
    			data.key1.add(new PointInt(temp1X, temp1Y));
    			data.key2.add(new PointInt(temp2X, temp2Y));
    		}
    	}
    }
    
    /* Utility function: 
     * Clear all the PointInts memorized in the ArrayList.
     * Passed by the Iterator<PointInt>
     */
    public void clearPoints(Iterator<PointInt> it) {
		while (it.hasNext()) {
			PointInt pt = it.next();
			data.map[pt.y][pt.x] = (char) 0;
		}    	
    }
    
    /* Called when a key is reached:
     * Fill the key's and the door's reachable area according to ball data
     * key: 1 or 2
     */
    public void clearKeyAndDoor(int key) {
    	if (key == 1) {
    		clearPoints(data.key1.listIterator());
    		clearPoints(data.door1.listIterator());
    	}
    	else if (key == 2) {
    		clearPoints(data.key2.listIterator());
    		clearPoints(data.door2.listIterator());
    	}
    }

    /* Utility function:
     * Fill all the PointInts memorized in the ArrayList with a specified char value.
     * Passed by the Iterator<PointInt>
     */
    public void fillPoints(Iterator<PointInt> it, char value) {
		while (it.hasNext()) {
			PointInt pt = it.next();
			data.map[pt.y][pt.x] = value;
		}    	
    }
    
    /* Called when reset:
     * Fill the keys' and doors' reachable area according to ball data
     */
    public void fillKeysAndDoors() {
    	fillPoints(data.key1.listIterator(), (char) 12);
    	fillPoints(data.key2.listIterator(), (char) 13);
    	fillPoints(data.door1.listIterator(), (char) 2);
    	fillPoints(data.door2.listIterator(), (char) 3);    
    }
}


/* An ordered pair (start, end) */
class Pair {
	public int start;
	public int end;
}

class GameData {
	/* Game variables */
	public char map[][];  // The 2D map array
	public int row;  // [Useless]
	public int col;  // [Useless]
	public int doorNum;
	/* Relative to the top-left corner(x,y), record all the pixels considered "reachable" of the ball. */
	// ballYStart ~ ballYEnd rows below (x,y) are considered as reachable.
	public int ballYStart;
	public int ballYEnd;
	// In each of the above rows, columns between Pairs [a,b] are considered as reachable.
	// e.g., ballX = { [8,23], [6,25], [5, 26], ... };
	public Pair [] ballX;
	/* Positions of door1, door2, key1, key2.
	 * These are the PointInts to be clear when reaching key1 and key2 */
	public ArrayList<PointInt> door1;
	public ArrayList<PointInt> door2;
	public ArrayList<PointInt> key1;
	public ArrayList<PointInt> key2;
}