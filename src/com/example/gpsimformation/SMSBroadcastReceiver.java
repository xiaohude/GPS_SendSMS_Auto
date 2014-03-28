package com.example.gpsimformation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SMSBroadcastReceiver extends BroadcastReceiver {
	
	private SaveSet saveSet;
	private GPSMessage GPSm;
	private Thread thread;
	private SendSMS sendSMS;
	String phoneSet;
	String message;

	@Override
	public void onReceive(final Context context, Intent intent) {
		// TODO Auto-generated method stub
		// 取出监听到的短信的电话号码
		
		Toast.makeText(context, "进入Broadcastreciever的onreceive函数！", Toast.LENGTH_SHORT).show(); 
        
		
		saveSet=new SaveSet(context);
		GPSm=new GPSMessage(context);
		sendSMS=new SendSMS(context);
		
		
		
		System.out.println("进入Broadcastreciever的onreceive函数！");
        String phone = this.getInf(intent);
        
        Toast.makeText(context, phone, Toast.LENGTH_SHORT).show(); 
        
        phoneSet = saveSet.getNum();
        //如果收到的短信的电话号码为13693490547,则中断广播，让手机收不到该电话号码的短信
        if (phoneSet.equals(phone)) {
                  //this.abortBroadcast();//中断广播
                  Toast.makeText(context, "屏蔽掉了鸭子短信！！！", Toast.LENGTH_SHORT).show(); 
                  
                  //开始调用获取GPS的函数，并向这个号码返回信息。
              
                  thread=new Thread(new Runnable() {
          			
                  	boolean bool=true;
          			@Override
          			public void run() {
          				// TODO Auto-generated method stub
//       			   	Toast.makeText(context, "GPS信号不好，请到户外，或者窗户旁边！", Toast.LENGTH_SHORT).show(); 
          			//在toast赋值语句也导致了程序停止运行！
          				while(bool)
          				{
          					
          					if(GPSm.latitude!=0)
          					{
          						
          						message="经度"+GPSm.longitude+'\n'+  
          				                "纬度"+GPSm.latitude+'\n'+  
          				                "速度"+GPSm.speed+"m/s"+'\n'+  
          				                "海拔"+GPSm.altitude+"m"+'\n'+  
          				                "方位"+GPSm.bearing+'\n';
          					    	//在这个线程里的这个字符串赋值语句导致了程序停止运行！
          						//Toast.makeText(context, message, Toast.LENGTH_SHORT).show(); 
                                
          						
          						sendSMS.sendSMS(phoneSet, message);
                                 
          						bool=false;
          					}
          					else
          					{
          						//bool=false;
//          						tv.setText("GPS信号不好，请到户外，或者窗户旁边！");
//          					//死循环里面似乎不能给显示的文本框赋值。
          						
          					}
          					
//          					try {
//          						Thread.sleep(10000);
//          					} catch (InterruptedException e) {
//          						// TODO Auto-generated catch block
//          						e.printStackTrace();
//          					}
          					
          					//是线程睡觉完之后出问题的。
          					
          				}
          				
          			}
          			
          		});
                  
                  thread.start();
                
                  
        }

	}
	
	
	// 取出监听到的短信的电话号码
	 private String getInf(Intent intent) {
	         if (intent.getAction() .equals("android.provider.Telephony.SMS_RECEIVED")) { 
	        	   /* 创建字符串变量sb */
	               /** 接收由Intent传来的数据 */
	        	 System.out.println("进入getinf函数！！！");
	               String phone = null;
	               Bundle bundle = intent.getExtras();
	               /** 判断Intent有无数据 */
	              if (bundle != null) {
	                     /**pdus为 android内置短信参数 identifier 通过bundle.get("")返回一包含pdus对象*/
	                     Object[] myOBJpdus = (Object[]) bundle.get("pdus");
	                     /* 构建短信对象array,并根据收到的对象长度来定义array的大小 */
	                     SmsMessage[] messages = new SmsMessage[myOBJpdus.length];
	                      for (int i = 0; i < messages.length; i++) {
	                               messages[i] = SmsMessage.createFromPdu((byte[]) myOBJpdus[i]);
	                      }
	                     /* 把传来的短信合并定义在stringbuffer中 */
	                      for (SmsMessage currentMessage : messages) {
	                                /* 发送人电话号码 */
	                               phone = currentMessage.getDisplayOriginatingAddress();
	                       }
	                 }
	                 /* 以(Toase)形式展示 */
	                return phone;
	           }
	          return null;
	    }
	
}

/*
 * 其他的就不需要设置什么了，也不需要调用这个类，只需在
 * Manifest中加入：（记得把名字写对！）
 * <!--把该接收器的优先级设置为1000，大于系统自带监听短信广播的广播接收器-->
        <receiver android:name="com.example.gpsimformation.SMSBroadcastReceiver"
            android:priority="1000"
            android:permission="android.permission.READ_SMS">
   			
  			 <intent-filter > 
   			 <!--短信的类型为:android.provider.Telephony.SMS_RECEIVED-->
   			 	<action android:name="android.provider.Telephony.SMS_RECEIVED" />
  			 </intent-filter>
  		</receiver> 
  		
  		 
  		 
  		 <!--配置与短信相关的权限-->
 	<uses-permission android:name="android.permission.RECEIVE_SMS"></uses-permission>
 * */
