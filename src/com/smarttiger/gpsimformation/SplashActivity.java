package com.smarttiger.gpsimformation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;


/**
 * app最开始显示的图片
 * */
public class SplashActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		ImageView splashImg = (ImageView) findViewById(R.id.splash_image);  
		splashImg.postDelayed(new Runnable() {//这里利用了View的postDelayed  
			public void run() {  
				Intent intent = new Intent();  
				intent.setClass(SplashActivity.this, MainActivity.class);  
				startActivity(intent);  
				finish();  
			}  
		}, 2000);  
		
	}

}