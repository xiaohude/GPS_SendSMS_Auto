package com.example.gpsimformation;



import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class CallBroadcastReceiver extends BroadcastReceiver {
	
	
	private SaveSet saveSet;
	private GPSMessage GPSm;
	private Thread thread;
	private SendSMS sendSMS;
	String phoneSet;
	String message;
	boolean bool;
	
	Context context;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		
		 System.out.println("Broadcast的onReceive函数被调用！");
		 
		 this.context=context;
		 
		saveSet=new SaveSet(context);
		GPSm=new GPSMessage(context);
		sendSMS=new SendSMS(context);
		
		phoneSet = saveSet.getNum();
		bool=saveSet.getBool();
		message="boradcast中的初始化";
			  
		 

	  TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
	  
	 
	  
	  switch (tm.getCallState()) {

	  case TelephonyManager.CALL_STATE_RINGING:// 来电响铃
		  

		  
		  //////在响铃或者通话过程中不能发送短息！！！所以在通话结束后再发送短息
		  ////但是获取来电号码，还只能在这来电响铃处获取。所以这里还需要保存一下这个号码
		  String phoneNumber = intent.getStringExtra("incoming_number");	   
		  if (phoneSet.equals(phoneNumber)) {
			  saveSet.setBool(true);
		  }


	   break;// 响铃

	  case TelephonyManager.CALL_STATE_OFFHOOK: // 来电接通 去电拨出
		  
		  System.out.println("来电接通。去点拨出。");
		  
		  saveSet.setBool(false);

		  
		  
	   break;// 摘机

	  case TelephonyManager.CALL_STATE_IDLE: // 来去电电话挂断
		  
//		  sendSMS.sendSMS(phoneSet, message);
		  
		  
		  try {
				
			//    String phoneNumber = intent.getStringExtra("incoming_number");	   
				 //  int number= Integer.parseInt(phoneNumber);//电话号码转换成int数据传出去。。不行，int类型最长十个数字
				  
				   
				   Toast.makeText(context, "挂断通话：", Toast.LENGTH_SHORT).show(); 
				  
				   if (bool) {
					   
					  
					   
   						
   						message="经度"+GPSm.longitude+'\n'+  
   				                "纬度"+GPSm.latitude;
   						
   						//短信发不过去还有一个很重要的原因是，double类型太长了。导致字符串很长！
   						
   					    	//在这个线程里的这个字符串赋值语句导致了程序停止运行！
   						//Toast.makeText(context, message, Toast.LENGTH_SHORT).show(); 
                         
   						System.out.println("message==========="+message);
   						sendSMS.sendSMS(phoneSet, message);
                          
   						Toast.makeText(context, "发送完短信！", Toast.LENGTH_SHORT).show(); 
   						
						GPSm.openGPS(context);//调用两次就关闭了。这里目的是关闭。
					   
   						saveSet.setBool(false);
					   
					   
				   }
				   
				   
				   		   
				   System.out.println("startService(intent)!!!!");
				   	
			   } catch (Exception e) {
			    e.printStackTrace();
			   }

	   break;// 挂机

	  }

	 

	}

}
