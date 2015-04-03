package com.smarttiger.gpsimformation;

import java.util.StringTokenizer;

import lib.DialogUtils;
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
	String phoneSet;
	String message;
	Context context;

	@Override
	public void onReceive(final Context context, Intent intent) {
		// TODO Auto-generated method stub
		// 取出监听到的短信的电话号码
		
		DialogUtils.debugShow(context, "", "进入Broadcastreciever的onreceive函数！"); 
        
		this.context = context;
		saveSet=new SaveSet(context);
//		GPSm=new GPSMessage(context);
		
		
		
		System.out.println("进入Broadcastreciever的onreceive函数！");
        String phone = this.getInf(intent);
        DialogUtils.debugShow(context, "", phone); 
        phoneSet = saveSet.getNum();
        
        if(message.contains("SmartTigerZXH"))
        {
      	  this.abortBroadcast();//中断广播,孩子手机上就不会显示这条短信了。
      	  Intent intent2 = new Intent(context, MyService.class);
      	  intent2.putExtra("num", phone);
		  context.startService(intent2);
        }
        else
        //如果收到的短信的电话号码为13693490547,则中断广播，让手机收不到该电话号码的短信
        //这里用contains比较好，因为有些手机号默认加+86了,并且注意谁包括谁
        if (isPhoneSet(phone)) {
                  //this.abortBroadcast();//中断广播
        	DialogUtils.debugShow(context, "", "判断出来自保存号码"); 
        	saveSet.saveReceiverNum(phone);
                  
                  if(message.startsWith("Lat("))
                  {
                	  if(!saveSet.getIsSaveSMS())
                		  this.abortBroadcast();//中断广播,家长手机上就不会显示这条短信了。
                	  DialogUtils.debugShow(context, "", "识别为地址信息。"); 
                	  Intent intent0 = context.getPackageManager()
                    		  .getLaunchIntentForPackage("com.smarttiger.gpsimformation"); 
                      intent0.putExtra("SMSmessage", message); 
                      intent0.putExtra("phone", phone); 
                      context.startActivity(intent0);
                      
                      //或
//                      Intent intent1 = new Intent(context, MainActivity.class);
//      				intent1.putExtra("SMSmessage", message);             
//                      context.startActivity(intent1);
                  }
                  else if(message.contains("zxhSmartTiger"))
                  {
                	  this.abortBroadcast();//中断广播,孩子手机上就不会显示这条短信了。
                	  Intent intent2 = new Intent(context, MyService.class);
        		      context.stopService(intent2);
        		      context.startService(intent2);
                  }
       
                  
/*
                  //开始调用获取GPS的函数，并向这个号码返回信息。           
                  thread=new Thread(new Runnable() {        			
                  	boolean bool=true;
          			@Override
          			public void run() {
          				// TODO Auto-generated method stub
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
                
*/
                
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
	                               message = currentMessage.getDisplayMessageBody();
	                       }
	                 }
	                
	              	DialogUtils.debugShow(context, "", message);
	                return phone;
	           }
	          return null;
	    }
	
	
	//判断是否为保存的手机号。
	private Boolean isPhoneSet(String num)
	{
    	String s;
		//按","将sResult解析出字符串
		StringTokenizer st = new StringTokenizer(phoneSet, ",");
		int strLeng = st.countTokens();
		for (int i=0; i<strLeng; i++) {
			s =  st.nextToken();
			if(num.contains(s))
				return true;
		}
        return false;
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
