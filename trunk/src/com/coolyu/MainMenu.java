package com.coolyu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends Activity implements View.OnClickListener {
	/* Buttons in menu */
	private Button phyButton;
	private Button rollButton;
	private Button playButton;
	private Button exitButton;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        
        phyButton = (Button) findViewById(R.id.phyButton);
        phyButton.setOnClickListener(this);
        rollButton = (Button) findViewById(R.id.rollButton);
        rollButton.setOnClickListener(this);
        playButton = (Button) findViewById(R.id.playButton);
        playButton.setOnClickListener(this);
        exitButton = (Button) findViewById(R.id.exitButton);
        exitButton.setOnClickListener(this);

    }
     
	public void onClick(View v) {
		if (v == phyButton) {
			Intent intent = new Intent();
			intent.setClass(MainMenu.this, ShowPhysicsData.class);
			startActivity(intent);
		}
		else if (v == rollButton) {
			Intent intent = new Intent();
			intent.setClass(MainMenu.this, TestRolling.class);
			startActivity(intent);			
		}
		else if (v == playButton) {
			Intent intent = new Intent();
			intent.setClass(MainMenu.this, PlayGame.class);
			startActivity(intent);
		}
		else if (v == exitButton) {
			finish();
		}
	}
}

/* Define directional constants */
interface Direction {
	public final int LEFT = 0;
	public final int TOP = 1;
	public final int RIGHT = 2;
	public final int BOTTOM = 3;
}

/* Pixel value information */
interface PixelInformation {
	public final float BALL_SIZE = 32.f;
	/* Height of Android system menu bar in pixels */
	public final float ANDROID_MENU_HEIGHT = 48.f;
	/* Height of backButton and resetButton in pixels */
	public final float BUTTON_HEIGHT = 50.f;
	/* Openmoko spec, not the final available width and height */
	public final float SCREEN_WIDTH = 480.f;
	public final float SCREEN_HEIGHT = 640.f;
	/* Available width and height (with the buttons) */
	public final float AVAILABLE_WIDTH = SCREEN_WIDTH;
	public final float AVAILABLE_HEIGHT = 
		SCREEN_HEIGHT - ANDROID_MENU_HEIGHT - BUTTON_HEIGHT;
}