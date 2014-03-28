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
				System.out.println("获取到编辑框的信息为："+editText.getText());
				saveSet.saveNum(editText.getText().toString());
				
//				sendSMS.sendSMS(editText.getText().toString(), "自动发送的短信！！！");
			}
		});
        
        GPSm=new GPSMessage(this);
//        GPSm.gettingGPS();
        
        tv.setText("经度"+GPSm.longitude+'\n'+  
                "纬度"+GPSm.latitude+'\n'+  
                "速度"+GPSm.speed+"m/s"+'\n'+  
                "海拔"+GPSm.altitude+"m"+'\n'+  
                "方位"+GPSm.bearing+'\n'); 
        
   /*     
        
        thread=new Thread(new Runnable() {
			
        	boolean bool=true;
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				tv.setText("GPS信号不好，请到户外，或者窗户旁边！");
				while(bool)
				{
					
					if(GPSm.latitude!=0)
					{
						tv.setText("经度"+GPSm.longitude+'\n'+  
				                "纬度"+GPSm.latitude+'\n'+  
				                "速度"+GPSm.speed+"m/s"+'\n'+  
				                "海拔"+GPSm.altitude+"m"+'\n'+  
				                "方位"+GPSm.bearing+'\n');  
						bool=false;
					}
					else
					{
						bool=false;
//						tv.setText("GPS信号不好，请到户外，或者窗户旁边！");
//					//死循环里面似乎不能给显示的文本框赋值。
						
					}
					
//					try {
//						Thread.sleep(10000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					
					//是线程睡觉完之后出问题的。
					
				}
				
			}
			
		});
        
        thread.start();
        
        
   */     
        
     /*
        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);  
          
        if (!lm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER))  
        {  
            Toast.makeText(this, "GPS已关闭,请手动开启GPS后再试！", Toast.LENGTH_SHORT).show();  
            return;  
        }  
        else  
        {  
            Toast.makeText(this, "GPS定位中...", Toast.LENGTH_SHORT).show();  
        }  
          
        criteria = new Criteria();  
        criteria.setAccuracy(Criteria.ACCURACY_FINE);   // 设置精确度  
        criteria.setAltitudeRequired(true);             // 设置请求海拔  
        criteria.setBearingRequired(true);              // 设置请求方位  
        criteria.setCostAllowed(true);                  // 设置允许运营商收费  
        criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗  
          
        String provider = lm.getBestProvider(criteria, true);  
        location = lm.getLastKnownLocation(provider);  
        newLocalGPS(location);  
        // 监听1秒一次 忽略位置变化  
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
            double longitude = location.getLongitude(); // 经度        
            double latitude = location.getLatitude(); // 纬度 
            double speed = location.getSpeed(); // 速度  
            double altitude = location.getAltitude();   // 海拔  
            double bearing = location.getBearing(); // 方位  
            tv.setText("经度"+longitude+'\n'+  
                           "纬度"+latitude+'\n'+  
                           "速度"+speed+"m/s"+'\n'+  
                           "海拔"+altitude+"m"+'\n'+  
                           "方位"+bearing+'\n');  
        }  
        else  
        {  
            // 未获取地理信息位置  
            tv.setText("地理信息位置未知或正在获取地理信息位置中...");  
        }  
    }  
      
    
    */
    
    
    @Override  
    public boolean onCreateOptionsMenu(Menu menu) {  
        // TODO Auto-generated method stub  
        menu.add(0, MENU_ABOUT, 1, "关于");    
        menu.add(0, MENU_EXIT, 2, "退出");  
        return super.onCreateOptionsMenu(menu);  
    }  
  
    @Override  
    public boolean onOptionsItemSelected(MenuItem item) {  
        // TODO Auto-generated method stub  
        switch (item.getItemId())  
        {  
        case MENU_ABOUT:  
             AlertDialog.Builder bd = new Builder(MainActivity.this);    
                bd.setMessage("GPS.apk\n版本:1.0\n作者:小虎\n请在授权管理中授予此软件为信任软件，允许自启动，否则可能会出现不自动发送短信的问题。");    
                bd.setTitle("关于");    
                bd.setPositiveButton("确认", new OnClickListener(){    
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
         builder.setMessage("确认退出吗？");    
         builder.setTitle("提示");    
         builder.setPositiveButton("确认", new OnClickListener(){    
             @Override    
             public void onClick(DialogInterface arg0, int arg1) {    
                 // TODO Auto-generated method stub    
                 arg0.dismiss();  
                 ActivityManager actMgr = (ActivityManager)getSystemService(ACTIVITY_SERVICE);    
                 actMgr.restartPackage(getPackageName());  
                 
//                 GPSm.thread.stop();
                 GPSm.openGPS(MainActivity.this);//调用两次就关闭了。这里目的是关闭。
                 finish();
             }    
         });    
         builder.setNegativeButton("取消", new OnClickListener(){    
  
             @Override    
             public void onClick(DialogInterface dialog, int which) {    
                 // TODO Auto-generated method stub    
                 dialog.dismiss();    
             }    
         });    
         builder.create().show();  
           
    }  
} 