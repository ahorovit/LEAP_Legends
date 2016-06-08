package com.JAAAM.Hex_v07;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.authorwjf.gamedevtut05.R;




//#########################################################################
//								Main Activity:    
//							  -----------------
//#########################################################################
public class Main extends Activity{

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        setupClickButton();
        
	}
	
	private void setupClickButton(){
		Button clickButton = (Button) findViewById(R.id.start_button);
		clickButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
		        MediaPlayer btnSound = MediaPlayer.create(Main.this, R.raw.sword_strike);//
				btnSound.start();
				startActivity(new Intent(Main.this, MainGame.class));

			}
		});
		
		
		Button clickTutorial = (Button) findViewById(R.id.tutorial_button);
		clickTutorial.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
		        MediaPlayer btnSound = MediaPlayer.create(Main.this, R.raw.sword_strike);//
				btnSound.start();
				startActivity(new Intent(Main.this, Tutorial.class));

			}
		});
		
		Button clickStory = (Button) findViewById(R.id.story_button);
		clickStory.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
		        MediaPlayer btnSound = MediaPlayer.create(Main.this, R.raw.sword_strike);//
				btnSound.start();
				startActivity(new Intent(Main.this, story.class));

			}
		});
	}
	
	
	
}