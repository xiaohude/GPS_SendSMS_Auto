package lib;


import java.util.ArrayList;
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
		this.context = context;
		paIntent = PendingIntent.getBroadcast(context, 0, new Intent(), 0);
		smsManager = SmsManager.getDefault();
		
		/*
		 * 需要在manifest中加入发送短信的权限，如下
		 * <uses-permission android:name="android.permission.SEND_SMS"></uses-permission>
		 * */
	}
	
	/**
	 * senSMS的第一个参数为目的号码，第二个参数为短信内容。 
	 * 记得短信内容需要精简，否则发送不成功。
	 * 经查一条短信最多发送160个字符或70个汉字，只要信息里有汉字，所有的数字或字母都按汉字算数了。
	 * */
	public void sendSMS(String num,String message)
	{
		try {
			//由于短信可能较长，故将短信拆分。但是这样导致长内容发送多条短信，费钱。所以还是精简发送内容。
			ArrayList<String> texts = smsManager.divideMessage(message);
			for(String text : texts){
				smsManager.sendTextMessage(num, null, text, paIntent, null);//分别发送每一条短信
			}		
			//如果短信内容较长，直接调用底下的代码会导致发送不出去。
			//smsManager.sendTextMessage(num, null, message, paIntent,null);
			//sendTextMessage方法中
			//第一个参数表示短信的目的电话号码，
			//第二个参数表示短信服务中心号码，如果为null则使用默认的短信服务中心号码。
			//第三个参数表示短信内容，
			//第四个参数表示发送短信结果内容，
			//第五个参数表示发送短信到目的地址后的回复信息。
			
			Toast.makeText(context, "已发送一条短信", 1).show(); 
//			Toast.makeText(context, "发送"+message+"短信到："+num, 1).show(); 
			DialogUtils.debugShow(context, "", "发送:\n"+message+"\n到："+num);
		
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(context, "手机号有误", 1).show();
		}
	}
			
}
