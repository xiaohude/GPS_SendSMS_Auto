package com.smarttiger.gpsimformation;

import lib.DialogUtils;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 点击登录界面的小i图标启动的activity，显示软件帮助和软件基本信息的
 * */
public class about extends Activity {
	/** Called when the activity is first created. */
	private TextView titleText;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		WebView webview = (WebView) findViewById(R.id.webView1);
		webview.loadUrl("file:///android_asset/about.txt");
		
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setOnLongClickListener(new OnLongClickListener() {		
			@Override
			public boolean onLongClick(View arg0) {
				// TODO Auto-generated method stub
				if(DialogUtils.debugType == 0)
					DialogUtils.debugType = 1;
				else if(DialogUtils.debugType == 1)
					DialogUtils.debugType = 2;
				else if(DialogUtils.debugType == 2)
				{
					DialogUtils.debugType = 0;
					Toast.makeText(about.this, "关闭调试", 1).show();
				}
				DialogUtils.debugShow(about.this, "设置", "开启调试");
				new SaveSet(about.this).saveDebugType(DialogUtils.debugType);
				return false;
			}
		});
		
	}

	public void onBackToMain(View view) {
		this.finish();
	}
}