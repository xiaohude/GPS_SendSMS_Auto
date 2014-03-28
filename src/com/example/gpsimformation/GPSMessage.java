package com.example.gpsimformation;


import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

public class GPSMessage {
    private LocationManager lm;  
    private Criteria criteria;  
    private Location location; 
    public Context context;
    public double longitude; //经度  
    public double latitude; // 纬度    
    public double speed ; // 速度  
    public double altitude ;   // 海拔  
    public double bearing ; // 方位  
    
    public Thread thread;
    
    public GPSMessage(final Context context)
    {
    	this.context=context;
    	
    	longitude=0;
    	latitude=0;
    	
    	
    	if(isOPen(context))
    	{
    		getGPS();
    		gettingGPS();
    	}
    	else
    	{
    		openGPS(context);
//    		刚打开GPS时调用gettingGPS会程序停止工作，想办法拖延几秒钟再调用gettingGPS就好。在线程里调用gettingGPS会导致无法关闭。
    		
    		
    		thread=new Thread(new Runnable() {		
     			@Override
     			public void run() {
     				// TODO Auto-generated method stub	  
     				Looper.prepare();	//加上这句话线程就不报错了（Can't create handler inside thread that has not called Looper.prepare() 异常问题）		
 	    	
    				   	try {
    						Thread.sleep(2000);
    					} catch (InterruptedException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
    				   	getGPS();
    		 		   	gettingGPS();				
     			}			
     		});           
            thread.start();
		
    	}

    }
    
    
    /**
     * 强制帮用户打开GPS   调用一次就打开，再次调用就关闭了。
     * @param context
     */ 
    public final void openGPS(Context context) { 
        Intent GPSIntent = new Intent(); 
        GPSIntent.setClassName("com.android.settings", 
                "com.android.settings.widget.SettingsAppWidgetProvider"); 
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE"); 
        GPSIntent.setData(Uri.parse("custom:3")); 
        try { 
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send(); 
        } catch (CanceledException e) { 
            e.printStackTrace(); 
        }     
    }
    
    
    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     * @param context
     * @return true 表示开启
     */ 
    public static final boolean isOPen(final Context context) { 
        LocationManager locationManager  
                                 = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE); 
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快） 
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER); 
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位） 
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER); 
        if (gps || network) { 
            return true; 
        } 
         return false; 
    }


    
    
    /**
     *  用来获取当前一次的GPS信息，想实时获取，请调用GettingGPS()函数
     * */
    public Boolean getGPS()
    {
    	// TODO 用来获取当前一次的GPS信息，想实时获取，请调用GettingGPS()函数
    	lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);  
        
        if (!lm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER))  
        {  
            Toast.makeText(context, "GPS已关闭,请手动开启GPS后再试！", Toast.LENGTH_SHORT).show();  
            return false;
            //return;  
        }  
        else  
        {  
            Toast.makeText(context, "GPS定位中...", Toast.LENGTH_SHORT).show();  
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
        
        return true;
    }
    
    
    /**
     * 前提是getGPS函数调用成功，返回值为真，这个函数才能调用，否则会出现停止工作。
     * */
    public void gettingGPS()
    {

    	System.out.println("gettingGPS   ~~~~~~~~");
    	// TODO 用来实时动态获取GPS信息，每1秒一次。
    	String provider = lm.getBestProvider(criteria, true);
    	
    	 // 监听1秒一次 忽略位置变化  
        lm.requestLocationUpdates(provider, 1*1000, 0, new locationListener());  
        
        
    
    }
    
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
            longitude = location.getLongitude(); // 经度        
            latitude = location.getLatitude(); // 纬度 
            speed = location.getSpeed(); // 速度  
            altitude = location.getAltitude();   // 海拔  
            bearing = location.getBearing(); // 方位   
//            Toast.makeText(context, "GPS获取成功！", Toast.LENGTH_SHORT).show();
        }  
        else  
        {  
            // 未获取地理信息位置  
        	Toast.makeText(context, "GPS信号不好，请到室外或窗户旁边...", Toast.LENGTH_SHORT).show();
//            tv.setText("地理信息位置未知或正在获取地理信息位置中...");  
        }  
    }  
      
	
	
	
}
