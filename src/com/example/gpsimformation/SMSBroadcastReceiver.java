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
		// ȡ���������Ķ��ŵĵ绰����
		
		Toast.makeText(context, "����Broadcastreciever��onreceive������", Toast.LENGTH_SHORT).show(); 
        
		
		saveSet=new SaveSet(context);
		GPSm=new GPSMessage(context);
		sendSMS=new SendSMS(context);
		
		
		
		System.out.println("����Broadcastreciever��onreceive������");
        String phone = this.getInf(intent);
        
        Toast.makeText(context, phone, Toast.LENGTH_SHORT).show(); 
        
        phoneSet = saveSet.getNum();
        //����յ��Ķ��ŵĵ绰����Ϊ13693490547,���жϹ㲥�����ֻ��ղ����õ绰����Ķ���
        if (phoneSet.equals(phone)) {
                  //this.abortBroadcast();//�жϹ㲥
                  Toast.makeText(context, "���ε���Ѽ�Ӷ��ţ�����", Toast.LENGTH_SHORT).show(); 
                  
                  //��ʼ���û�ȡGPS�ĺ���������������뷵����Ϣ��
              
                  thread=new Thread(new Runnable() {
          			
                  	boolean bool=true;
          			@Override
          			public void run() {
          				// TODO Auto-generated method stub
//       			   	Toast.makeText(context, "GPS�źŲ��ã��뵽���⣬���ߴ����Աߣ�", Toast.LENGTH_SHORT).show(); 
          			//��toast��ֵ���Ҳ�����˳���ֹͣ���У�
          				while(bool)
          				{
          					
          					if(GPSm.latitude!=0)
          					{
          						
          						message="����"+GPSm.longitude+'\n'+  
          				                "γ��"+GPSm.latitude+'\n'+  
          				                "�ٶ�"+GPSm.speed+"m/s"+'\n'+  
          				                "����"+GPSm.altitude+"m"+'\n'+  
          				                "��λ"+GPSm.bearing+'\n';
          					    	//������߳��������ַ�����ֵ��䵼���˳���ֹͣ���У�
          						//Toast.makeText(context, message, Toast.LENGTH_SHORT).show(); 
                                
          						
          						sendSMS.sendSMS(phoneSet, message);
                                 
          						bool=false;
          					}
          					else
          					{
          						//bool=false;
//          						tv.setText("GPS�źŲ��ã��뵽���⣬���ߴ����Աߣ�");
//          					//��ѭ�������ƺ����ܸ���ʾ���ı���ֵ��
          						
          					}
          					
//          					try {
//          						Thread.sleep(10000);
//          					} catch (InterruptedException e) {
//          						// TODO Auto-generated catch block
//          						e.printStackTrace();
//          					}
          					
          					//���߳�˯����֮�������ġ�
          					
          				}
          				
          			}
          			
          		});
                  
                  thread.start();
                
                  
        }

	}
	
	
	// ȡ���������Ķ��ŵĵ绰����
	 private String getInf(Intent intent) {
	         if (intent.getAction() .equals("android.provider.Telephony.SMS_RECEIVED")) { 
	        	   /* �����ַ�������sb */
	               /** ������Intent���������� */
	        	 System.out.println("����getinf����������");
	               String phone = null;
	               Bundle bundle = intent.getExtras();
	               /** �ж�Intent�������� */
	              if (bundle != null) {
	                     /**pdusΪ android���ö��Ų��� identifier ͨ��bundle.get("")����һ����pdus����*/
	                     Object[] myOBJpdus = (Object[]) bundle.get("pdus");
	                     /* �������Ŷ���array,�������յ��Ķ��󳤶�������array�Ĵ�С */
	                     SmsMessage[] messages = new SmsMessage[myOBJpdus.length];
	                      for (int i = 0; i < messages.length; i++) {
	                               messages[i] = SmsMessage.createFromPdu((byte[]) myOBJpdus[i]);
	                      }
	                     /* �Ѵ����Ķ��źϲ�������stringbuffer�� */
	                      for (SmsMessage currentMessage : messages) {
	                                /* �����˵绰���� */
	                               phone = currentMessage.getDisplayOriginatingAddress();
	                       }
	                 }
	                 /* ��(Toase)��ʽչʾ */
	                return phone;
	           }
	          return null;
	    }
	
}

/*
 * �����ľͲ���Ҫ����ʲô�ˣ�Ҳ����Ҫ��������ֻ࣬����
 * Manifest�м��룺���ǵð�����д�ԣ���
 * <!--�Ѹý����������ȼ�����Ϊ1000������ϵͳ�Դ��������Ź㲥�Ĺ㲥������-->
        <receiver android:name="com.example.gpsimformation.SMSBroadcastReceiver"
            android:priority="1000"
            android:permission="android.permission.READ_SMS">
   			
  			 <intent-filter > 
   			 <!--���ŵ�����Ϊ:android.provider.Telephony.SMS_RECEIVED-->
   			 	<action android:name="android.provider.Telephony.SMS_RECEIVED" />
  			 </intent-filter>
  		</receiver> 
  		
  		 
  		 
  		 <!--�����������ص�Ȩ��-->
 	<uses-permission android:name="android.permission.RECEIVE_SMS"></uses-permission>
 * */
