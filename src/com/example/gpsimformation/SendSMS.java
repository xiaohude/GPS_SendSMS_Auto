package com.example.gpsimformation;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

public class SendSMS {
	PendingIntent paIntent;

	SmsManager smsManager;
	Context context;
	
	public SendSMS(Context context)
	{
		this.context=context;
		paIntent = PendingIntent.getBroadcast(context, 0, new Intent(), 0);
		smsManager = SmsManager.getDefault();


		
		/*
		 * 需要在manifest中加入发送短信的权限，如下
		 * <uses-permission android:name="android.permission.SEND_SMS"></uses-permission>
		 * */
	}
	
	/**
	 *senSMS的第一个参数为目的号码，第二个参数为短信内容。 
	 * */
	public void sendSMS(String num,String message)
	{
		smsManager.sendTextMessage(num, null, message, paIntent,null);
		Toast.makeText(context, "发送"+message+"短信到："+num, Toast.LENGTH_SHORT).show(); 
//		sendTextMessage方法中
//		第一个参数表示短信的目的电话号码，
//		第二个参数表示短信服务中心号码，如果为null则使用默认的短信服务中心号码。
//		第三个参数表示短信内容，
//		第四个参数表示发送短信结果内容，
//		第五个参数表示发送短信到目的地址后的回复信息。
	}
			
}
