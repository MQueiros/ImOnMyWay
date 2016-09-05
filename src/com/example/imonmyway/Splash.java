package com.example.imonmyway;

import com.example.imonmyway.R;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

public class Splash extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		Thread timer = new Thread(){
			public void run(){
				try{
					sleep(1500);
				}
				catch(InterruptedException e){
					e.printStackTrace();
				}
				finally{
					Intent openIntroActivity = new Intent("com.examples.imonmyway.INTROACTIVITY");
					startActivity(openIntroActivity);
				}
			}
		};
		timer.start();
	}
	
}
