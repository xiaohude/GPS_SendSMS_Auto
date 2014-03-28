package com.example.gpsimformation;  
  
import android.app.Activity;  
import android.app.ActivityManager;  
import android.app.AlertDialog;  
import android.app.AlertDialog.Builder;  
import android.content.Context;  
import android.content.DialogInterface;  
import android.content.DialogInterface.OnClickListener;  
import android.location.Criteria;  
import android.location.Location;  
import android.location.LocationListener;  
import android.location.LocationManager;  
import android.os.Bundle;  
import android.view.KeyEvent;  
import android.view.Menu;  
import android.view.MenuItem;  
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SlidingDrawer;
import android.widget.TextView;  
import android.widget.Toast;  
  
public class MainActivity extends Activity {  
    private TextView tv;  
    private EditText editText;
    private Button button;
    
    
    
    private LocationManager lm;  
    private Criteria criteria;  
    private Location location;  
    private final static int MENU_ABOUT = Menu.FIRST;    
    private final static int MENU_EXIT = Menu.FIRST+1;  
    
    
    private GPSMessage GPSm;
    private SaveSet saveSet;
    private Thread thread;
    private SendSMS sendSMS;
    
    /** Called when the activity is first created. */  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);  
        
        saveSet=new SaveSet(this);
        sendSMS=new SendSMS(this);
          
        tv = (TextView)findViewById(R.id.tv);  
        editText=(EditText)findViewById(R.id.editText);
        editText.setText(saveSet.getNum());
        button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("��ȡ���༭�����ϢΪ��"+editText.getText());
				saveSet.saveNum(editText.getText().toString());
				
//				sendSMS.sendSMS(editText.getText().toString(), "�Զ����͵Ķ��ţ�����");
			}
		});
        
        GPSm=new GPSMessage(this);
//        GPSm.gettingGPS();
        
        tv.setText("����"+GPSm.longitude+'\n'+  
                "γ��"+GPSm.latitude+'\n'+  
                "�ٶ�"+GPSm.speed+"m/s"+'\n'+  
                "����"+GPSm.altitude+"m"+'\n'+  
                "��λ"+GPSm.bearing+'\n'); 
        
   /*     
        
        thread=new Thread(new Runnable() {
			
        	boolean bool=true;
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				tv.setText("GPS�źŲ��ã��뵽���⣬���ߴ����Աߣ�");
				while(bool)
				{
					
					if(GPSm.latitude!=0)
					{
						tv.setText("����"+GPSm.longitude+'\n'+  
				                "γ��"+GPSm.latitude+'\n'+  
				                "�ٶ�"+GPSm.speed+"m/s"+'\n'+  
				                "����"+GPSm.altitude+"m"+'\n'+  
				                "��λ"+GPSm.bearing+'\n');  
						bool=false;
					}
					else
					{
						bool=false;
//						tv.setText("GPS�źŲ��ã��뵽���⣬���ߴ����Աߣ�");
//					//��ѭ�������ƺ����ܸ���ʾ���ı���ֵ��
						
					}
					
//					try {
//						Thread.sleep(10000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					
					//���߳�˯����֮�������ġ�
					
				}
				
			}
			
		});
        
        thread.start();
        
        
   */     
        
     /*
        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);  
          
        if (!lm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER))  
        {  
            Toast.makeText(this, "GPS�ѹر�,���ֶ�����GPS�����ԣ�", Toast.LENGTH_SHORT).show();  
            return;  
        }  
        else  
        {  
            Toast.makeText(this, "GPS��λ��...", Toast.LENGTH_SHORT).show();  
        }  
          
        criteria = new Criteria();  
        criteria.setAccuracy(Criteria.ACCURACY_FINE);   // ���þ�ȷ��  
        criteria.setAltitudeRequired(true);             // �������󺣰�  
        criteria.setBearingRequired(true);              // ��������λ  
        criteria.setCostAllowed(true);                  // ����������Ӫ���շ�  
        criteria.setPowerRequirement(Criteria.POWER_LOW); // �͹���  
          
        String provider = lm.getBestProvider(criteria, true);  
        location = lm.getLastKnownLocation(provider);  
        newLocalGPS(location);  
        // ����1��һ�� ����λ�ñ仯  
        lm.requestLocationUpdates(provider, 1*1000, 0, new locationListener());  
        */
        
        
        
    }  
    
    
    
    
     /*
    class locationListener implements LocationListener  
    {  
  
        @Override  
        public void onLocationChanged(Location location) {  
            // TODO Auto-generated method stub  
            newLocalGPS(location);  
        }  
  
        @Override  
        public void onProviderDisabled(String provider) {  
            // TODO Auto-generated method stub  
            newLocalGPS(null);  
        }  
  
        @Override  
        public void onProviderEnabled(String provider) {  
            // TODO Auto-generated method stub  
              
        }  
  
        @Override  
        public void onStatusChanged(String provider, int status, Bundle extras) {  
            // TODO Auto-generated method stub  
              
        }  
          
    }  
      
    private void newLocalGPS(Location location)  
    {  
        if (location!=null)  
        {  
            double longitude = location.getLongitude(); // ����        
            double latitude = location.getLatitude(); // γ�� 
            double speed = location.getSpeed(); // �ٶ�  
            double altitude = location.getAltitude();   // ����  
            double bearing = location.getBearing(); // ��λ  
            tv.setText("����"+longitude+'\n'+  
                           "γ��"+latitude+'\n'+  
                           "�ٶ�"+speed+"m/s"+'\n'+  
                           "����"+altitude+"m"+'\n'+  
                           "��λ"+bearing+'\n');  
        }  
        else  
        {  
            // δ��ȡ������Ϣλ��  
            tv.setText("������Ϣλ��δ֪�����ڻ�ȡ������Ϣλ����...");  
        }  
    }  
      
    
    */
    
    
    @Override  
    public boolean onCreateOptionsMenu(Menu menu) {  
        // TODO Auto-generated method stub  
        menu.add(0, MENU_ABOUT, 1, "����");    
        menu.add(0, MENU_EXIT, 2, "�˳�");  
        return super.onCreateOptionsMenu(menu);  
    }  
  
    @Override  
    public boolean onOptionsItemSelected(MenuItem item) {  
        // TODO Auto-generated method stub  
        switch (item.getItemId())  
        {  
        case MENU_ABOUT:  
             AlertDialog.Builder bd = new Builder(MainActivity.this);    
                bd.setMessage("GPS.apk\n�汾:1.0\n����:С��\n������Ȩ��������������Ϊ���������������������������ܻ���ֲ��Զ����Ͷ��ŵ����⡣");    
                bd.setTitle("����");    
                bd.setPositiveButton("ȷ��", new OnClickListener(){    
                    @Override    
                    public void onClick(DialogInterface arg0, int arg1) {    
                        // TODO Auto-generated method stub    
                        arg0.dismiss();    
                    }    
                });    
                bd.create().show();   
            break;  
        case MENU_EXIT:  
            exit();  
            break;  
        }  
        return super.onOptionsItemSelected(item);  
    }  
  
    @Override    
    public boolean onKeyDown(int keyCode, KeyEvent event) {    
        // TODO Auto-generated method stub    
        if (keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0)    
        {    
            exit();    
            return true;    
        }    
        return super.onKeyDown(keyCode, event);    
    }   
      
    private void exit()    
    {    
    	//thread.stop();
    	
         AlertDialog.Builder builder = new Builder(MainActivity.this);    
         builder.setMessage("ȷ���˳���");    
         builder.setTitle("��ʾ");    
         builder.setPositiveButton("ȷ��", new OnClickListener(){    
             @Override    
             public void onClick(DialogInterface arg0, int arg1) {    
                 // TODO Auto-generated method stub    
                 arg0.dismiss();  
                 ActivityManager actMgr = (ActivityManager)getSystemService(ACTIVITY_SERVICE);    
                 actMgr.restartPackage(getPackageName());  
                 
//                 GPSm.thread.stop();
                 GPSm.openGPS(MainActivity.this);//�������ξ͹ر��ˡ�����Ŀ���ǹرա�
                 finish();
             }    
         });    
         builder.setNegativeButton("ȡ��", new OnClickListener(){    
  
             @Override    
             public void onClick(DialogInterface dialog, int which) {    
                 // TODO Auto-generated method stub    
                 dialog.dismiss();    
             }    
         });    
         builder.create().show();  
           
    }  
} 