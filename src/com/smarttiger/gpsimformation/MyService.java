package com.smarttiger.gpsimformation;

import lib.DialogUtils;
import lib.SendSMS;

import com.baidu.location.BDLocation;
import com.smarttiger.gpsimformation.MyLocationListener.LocationSuccessListener;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.DialerFilter;
import android.widget.Toast;

public class MyService extends Service{

	private Context context;
	
	private SaveSet saveSet;
	private SendSMS sendSMS;
	private String receiverNum;
	private MyLocationListener myLocationListener ;
	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		System.out.println("MyService----------onCreate()");
		context = this;
			
		saveSet = new SaveSet(context);
		sendSMS = new SendSMS(context);
		receiverNum = saveSet.getReceiverNum();
		
		
		DialogUtils.debugShow(context, "", "MyService--onCreat()");
		
//		myLocationListener = new MyLocationListener(context);
//        myLocationListener.setLocationSuccessListener(new LocationSuccessListener() {		
//			@Override
//			public void getLocation(BDLocation location, String s) {
//				// TODO Auto-generated method stub			
//				sendSMS.sendSMS(phoneSet, s);			
//				myLocationListener.stopGetLocation();
//				MyService.this.stopSelf();
//			}
//		});
//        myLocationListener.startGetLocation();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub		
		System.out.println("onStartCommand------flags=="+flags+",startId=="+startId);		
		String phone = intent.getStringExtra("num");		
		
		if(phone != null)
			receiverNum = phone;
		
		DialogUtils.debugShow(context, "onStartCommand", receiverNum);
		
		myLocationListener = new MyLocationListener(context);
        myLocationListener.setLocationSuccessListener(new LocationSuccessListener() {		
			@Override
			public void getLocation(BDLocation location, String s) {
				// TODO Auto-generated method stub	
				//这里如果没运行成，需要新开一个Activity或这service。
				sendSMS.sendSMS(receiverNum, s);
				myLocationListener.stopGetLocation();
				MyService.this.stopSelf();
			}
		});
        myLocationListener.startGetLocation();
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		System.out.println("this---is----MyService------onBind()");
		return null;
	}
	
	

}
