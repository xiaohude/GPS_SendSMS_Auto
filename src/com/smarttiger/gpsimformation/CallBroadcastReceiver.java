package com.smarttiger.gpsimformation;




import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lib.DialogUtils;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class CallBroadcastReceiver extends BroadcastReceiver {
	
	
	private SaveSet saveSet;
	private Thread thread;
	String phoneSet;
	private boolean bool;
	private int triggerCondition;
	
	Context context;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		
		 System.out.println("Broadcast的onReceive函数被调用！");
		 
		 this.context = context;
		 
		saveSet = new SaveSet(context);
		
		phoneSet = saveSet.getNum();
		bool = saveSet.getBool();
		triggerCondition = saveSet.getTriggerCondition();
			  
		 

	  TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
	  
	 
	  
	  switch (tm.getCallState()) {

	  case TelephonyManager.CALL_STATE_RINGING:// 来电响铃
		  
		  
		  //////在响铃或者通话过程中不能发送短息！！！所以在通话结束后再发送短息
		  ////但是获取来电号码，还只能在这来电响铃处获取。所以这里还需要保存一下是否为设定的这个号码
		  String phoneNumber = intent.getStringExtra("incoming_number");	   
		  if (isPhoneSet(phoneNumber)) {
			  bool = true;
			  if(triggerCondition == 3)
				  bool = false;
			  saveSet.setBool(bool);
			  saveSet.saveReceiverNum(phoneNumber);
		  }


	   break;// 响铃

	  case TelephonyManager.CALL_STATE_OFFHOOK: // 来电接通 去电拨出
		  
		  System.out.println("来电接通。去点拨出。");
		  if(triggerCondition == 1)
		  {
			  bool = false;
			  saveSet.setBool(bool);
		  }
			  
 
		  
	   break;// 摘机

	  case TelephonyManager.CALL_STATE_IDLE: // 来去电电话挂断
     
		  DialogUtils.debugShow(context, "", "挂断通话："); 
		  
		  if (bool) {
		      Intent intent2 = new Intent(context, MyService.class);
		      context.stopService(intent2);
		      context.startService(intent2);	
		      
		      DialogUtils.debugShow(context, "", "发送完短信！"); 
		      saveSet.setBool(false);			   
		  }
			   	   
		  System.out.println("startService(intent)!!!!");
		

	   break;// 挂机

	  }

	 
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
