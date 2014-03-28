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
		 * ��Ҫ��manifest�м��뷢�Ͷ��ŵ�Ȩ�ޣ�����
		 * <uses-permission android:name="android.permission.SEND_SMS"></uses-permission>
		 * */
	}
	
	/**
	 *senSMS�ĵ�һ������ΪĿ�ĺ��룬�ڶ�������Ϊ�������ݡ� 
	 * */
	public void sendSMS(String num,String message)
	{
		smsManager.sendTextMessage(num, null, message, paIntent,null);
		Toast.makeText(context, "����"+message+"���ŵ���"+num, Toast.LENGTH_SHORT).show(); 
//		sendTextMessage������
//		��һ��������ʾ���ŵ�Ŀ�ĵ绰���룬
//		�ڶ���������ʾ���ŷ������ĺ��룬���Ϊnull��ʹ��Ĭ�ϵĶ��ŷ������ĺ��롣
//		������������ʾ�������ݣ�
//		���ĸ�������ʾ���Ͷ��Ž�����ݣ�
//		�����������ʾ���Ͷ��ŵ�Ŀ�ĵ�ַ��Ļظ���Ϣ��
	}
			
}
