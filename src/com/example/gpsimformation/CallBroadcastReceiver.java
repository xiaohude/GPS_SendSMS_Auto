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

		
		 System.out.println("Broadcast��onReceive���������ã�");
		 
		 this.context=context;
		 
		saveSet=new SaveSet(context);
		GPSm=new GPSMessage(context);
		sendSMS=new SendSMS(context);
		
		phoneSet = saveSet.getNum();
		bool=saveSet.getBool();
		message="boradcast�еĳ�ʼ��";
			  
		 

	  TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
	  
	 
	  
	  switch (tm.getCallState()) {

	  case TelephonyManager.CALL_STATE_RINGING:// ��������
		  

		  
		  //////���������ͨ�������в��ܷ��Ͷ�Ϣ������������ͨ���������ٷ��Ͷ�Ϣ
		  ////���ǻ�ȡ������룬��ֻ�������������崦��ȡ���������ﻹ��Ҫ����һ���������
		  String phoneNumber = intent.getStringExtra("incoming_number");	   
		  if (phoneSet.equals(phoneNumber)) {
			  saveSet.setBool(true);
		  }


	   break;// ����

	  case TelephonyManager.CALL_STATE_OFFHOOK: // �����ͨ ȥ�粦��
		  
		  System.out.println("�����ͨ��ȥ�㲦����");
		  
		  saveSet.setBool(false);

		  
		  
	   break;// ժ��

	  case TelephonyManager.CALL_STATE_IDLE: // ��ȥ��绰�Ҷ�
		  
//		  sendSMS.sendSMS(phoneSet, message);
		  
		  
		  try {
				
			//    String phoneNumber = intent.getStringExtra("incoming_number");	   
				 //  int number= Integer.parseInt(phoneNumber);//�绰����ת����int���ݴ���ȥ�������У�int�����ʮ������
				  
				   
				   Toast.makeText(context, "�Ҷ�ͨ����", Toast.LENGTH_SHORT).show(); 
				  
				   if (bool) {
					   
					  
					   
   						
   						message="����"+GPSm.longitude+'\n'+  
   				                "γ��"+GPSm.latitude;
   						
   						//���ŷ�����ȥ����һ������Ҫ��ԭ���ǣ�double����̫���ˡ������ַ����ܳ���
   						
   					    	//������߳��������ַ�����ֵ��䵼���˳���ֹͣ���У�
   						//Toast.makeText(context, message, Toast.LENGTH_SHORT).show(); 
                         
   						System.out.println("message==========="+message);
   						sendSMS.sendSMS(phoneSet, message);
                          
   						Toast.makeText(context, "��������ţ�", Toast.LENGTH_SHORT).show(); 
   						
						GPSm.openGPS(context);//�������ξ͹ر��ˡ�����Ŀ���ǹرա�
					   
   						saveSet.setBool(false);
					   
					   
				   }
				   
				   
				   		   
				   System.out.println("startService(intent)!!!!");
				   	
			   } catch (Exception e) {
			    e.printStackTrace();
			   }

	   break;// �һ�

	  }

	 

	}

}
