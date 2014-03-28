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
    public double longitude; //����  
    public double latitude; // γ��    
    public double speed ; // �ٶ�  
    public double altitude ;   // ����  
    public double bearing ; // ��λ  
    
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
//    		�մ�GPSʱ����gettingGPS�����ֹͣ��������취���Ӽ������ٵ���gettingGPS�ͺá����߳������gettingGPS�ᵼ���޷��رա�
    		
    		
    		thread=new Thread(new Runnable() {		
     			@Override
     			public void run() {
     				// TODO Auto-generated method stub	  
     				Looper.prepare();	//������仰�߳̾Ͳ������ˣ�Can't create handler inside thread that has not called Looper.prepare() �쳣���⣩		
 	    	
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
     * ǿ�ư��û���GPS   ����һ�ξʹ򿪣��ٴε��þ͹ر��ˡ�
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
     * �ж�GPS�Ƿ�����GPS����AGPS����һ������Ϊ�ǿ�����
     * @param context
     * @return true ��ʾ����
     */ 
    public static final boolean isOPen(final Context context) { 
        LocationManager locationManager  
                                 = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE); 
        // ͨ��GPS���Ƕ�λ����λ������Ծ�ȷ���֣�ͨ��24�����Ƕ�λ��������Ϳտ��ĵط���λ׼ȷ���ٶȿ죩 
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER); 
        // ͨ��WLAN���ƶ�����(3G/2G)ȷ����λ�ã�Ҳ����AGPS������GPS��λ����Ҫ���������ڻ��ڸ������Ⱥ��ï�ܵ����ֵȣ��ܼ��ĵط���λ�� 
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER); 
        if (gps || network) { 
            return true; 
        } 
         return false; 
    }


    
    
    /**
     *  ������ȡ��ǰһ�ε�GPS��Ϣ����ʵʱ��ȡ�������GettingGPS()����
     * */
    public Boolean getGPS()
    {
    	// TODO ������ȡ��ǰһ�ε�GPS��Ϣ����ʵʱ��ȡ�������GettingGPS()����
    	lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);  
        
        if (!lm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER))  
        {  
            Toast.makeText(context, "GPS�ѹر�,���ֶ�����GPS�����ԣ�", Toast.LENGTH_SHORT).show();  
            return false;
            //return;  
        }  
        else  
        {  
            Toast.makeText(context, "GPS��λ��...", Toast.LENGTH_SHORT).show();  
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
        
        return true;
    }
    
    
    /**
     * ǰ����getGPS�������óɹ�������ֵΪ�棬����������ܵ��ã���������ֹͣ������
     * */
    public void gettingGPS()
    {

    	System.out.println("gettingGPS   ~~~~~~~~");
    	// TODO ����ʵʱ��̬��ȡGPS��Ϣ��ÿ1��һ�Ρ�
    	String provider = lm.getBestProvider(criteria, true);
    	
    	 // ����1��һ�� ����λ�ñ仯  
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
            longitude = location.getLongitude(); // ����        
            latitude = location.getLatitude(); // γ�� 
            speed = location.getSpeed(); // �ٶ�  
            altitude = location.getAltitude();   // ����  
            bearing = location.getBearing(); // ��λ   
//            Toast.makeText(context, "GPS��ȡ�ɹ���", Toast.LENGTH_SHORT).show();
        }  
        else  
        {  
            // δ��ȡ������Ϣλ��  
        	Toast.makeText(context, "GPS�źŲ��ã��뵽����򴰻��Ա�...", Toast.LENGTH_SHORT).show();
//            tv.setText("������Ϣλ��δ֪�����ڻ�ȡ������Ϣλ����...");  
        }  
    }  
      
	
	
	
}
