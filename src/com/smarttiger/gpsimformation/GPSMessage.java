package com.smarttiger.gpsimformation;


import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;


import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
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
    public String myLocation;
    
    public GPSMessage (Context context)
    {
    	this.context=context;
    	
//    	openGPSSettings(context);
    	
    	longitude=0;
    	latitude=0;
    	speed = 0;
    	altitude = 0;
    	bearing = 0;
    	try {
        	getGPS();
		} catch (Exception e) {
			// TODO: handle exception
		}
    	
    	getLocation(context);
    }
    
    private void getLocation(Context context)
    {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 返回所有已知的位置提供者的名称列表，包括未获准访问或调用活动目前已停用的。
        List<String> lp = lm.getAllProviders();
        for (String item:lp)
        {
            System.out.println("可用位置服务："+item); 
        }
        Criteria criteria = new Criteria();  
        criteria.setCostAllowed(false); 
        //设置位置服务免费 
        criteria.setAccuracy(Criteria.ACCURACY_COARSE); //设置水平位置精度
         //getBestProvider 只有允许访问调用活动的位置供应商将被返回
        String  providerName =         lm.getBestProvider(criteria, true);
        //下面这条代码解决了一个大问题，就是lm.getLastKnownLocation(providerName);返回值为空，无法获取当前坐标。
        lm.requestLocationUpdates(providerName, 0, 0,new LocationListener(){
        	@Override
        	    public void onLocationChanged(Location location) {           
        		System.out.println("纬度:"+location.getLatitude()+"经度："+ location.getLongitude());
//       		//获取维度信息
//	            double latitude = location.getLatitude();
//	            //获取经度信息
//	            double longitude = location.getLongitude();
//	            new GetGPSName().execute(Double.toString(longitude),Double.toString(latitude));
	            }
        	    @Override
        	    public void onProviderDisabled(String provider) {
        	    }
        	    @Override
        	    public void onProviderEnabled(String provider) {
        	    }
        	    @Override
        	    public void onStatusChanged(String provider, int status, Bundle extras) {
        	    }
        	});
        System.out.println("------位置服务："+providerName);
        if (providerName != null)
        {        
        	System.out.println("providerName != null00000000000000----" + lm);
            Location location = lm.getLastKnownLocation(providerName);
            System.out.println("providerName != null111111111111111---" + location);  
            if(location == null)
            {
            	System.out.println("无法获取您手机当前地址，请打开GPS或直接输入地址。" );
            	Toast.makeText(context, "无法获取您手机当前坐标，请打开移动网络或GPS!",  Toast.LENGTH_SHORT).show();
            }
            else
            {
	             //获取维度信息
	            double latitude = location.getLatitude();
	            //获取经度信息
	            double longitude = location.getLongitude();
	            this.latitude = latitude;
	            this.longitude = longitude;
	            System.out.println("定位方式： "+providerName+"  维度："+latitude+"  经度："+longitude);	            
	            new GetGPSName().execute(Double.toString(longitude),Double.toString(latitude));
            }           
        }
        else
        {
        	System.out.println("1.请检查网络连接 \n2.请打开我的位置" );
        }
    }
    
    
    public String getMyLocation()
    {
    	return myLocation;
    }
    
///////////////////////////////////以下为GPS坐标转换为城市道路名所用/////////////////////////////////////////////////////////

    
    /**
	 * 简单的根据第一个参数所给的url发送请求，返回服务器返回的值。
	 * 第一个参数为完整的url，包括具体的id。第二个参数为Cookie所需的参数。
	 * */
	public String putRequest(String url)
	{
		String result = "";
		try {
			// 1.得到HttpClient对象
			HttpClient httpClient = new DefaultHttpClient();
			// 2.实例化一个HttpPut对象
			HttpPut httpPut = new HttpPut(url);			
			// 4.执行httpPost提交，得到返回响应。  
			HttpResponse response=httpClient.execute(httpPut);
			// 5.判断是否请求成功。
			if(response.getStatusLine().getStatusCode()==200)
			{
                // 6.取出应答字符串-----------这里的应答字符串result就是最终获取的信息，服务器发送来的。可以是一个网页，也可以是一个OK。
                HttpEntity entity=response.getEntity();
                result=EntityUtils.toString(entity, HTTP.UTF_8);
			}			
			System.out.println("putRequest()--result=-=" + result );		
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return result;
	}
	
	/**
	 * 根据url获取返回值，并解析出地点名。
	 * */
	public String getGPSName(String url)
	{
		String result = putRequest(url);
		String address = null;
		try {
			JSONObject jsonobjectFather= new JSONObject(result);
			JSONObject jsonobjectSon = jsonobjectFather.getJSONObject("result");
			address = jsonobjectSon.getString("formatted_address");
			System.out.println("address======"  + address);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return address;
	}


	/**
	 * 异步访问百度地图服务api，根据GPS信息获取城市街道名称。
	 * 第一个参数为：经度（大的那个），第二个参数为：纬度（0-90）
	 * */
	private class GetGPSName extends AsyncTask<String, Void, String> {
		// 任务启动，可以在这里显示一个对话框，这里简单处理
		@Override
		protected String doInBackground(String... params) {
			// 异步刷新列表  自动在新建线程中调用此方法
			String longitude = params[0];//经度
			String latitude = params[1];//纬度
			return getGPSName("http://api.map.baidu.com/geocoder?output=json&location="+ latitude + "," + longitude);
		}
		@Override
		protected void onPostExecute(String result) {
		// doInBackground返回时触发，换句话说，就是doInBackground执行完后触发
		// 这里的result就是上面doInBackground执行后的返回值，所以这里是"执行完毕"
			System.out.println("地点信息为：=====" + result);
			
			 myLocation = result;
			 
			 MainActivity.setText(result);
			
			super.onPostExecute(result);
		}
	}
    
///////////////////////////////////以上为GPS坐标转换为城市道路名所用/////////////////////////////////////////////////////////
	
    
    
    //这种自动打开gps的方法不可行，因为权限只是提供给系统app的，无法获取权限。
//    private void openGPSSettings(Context context) {       
//        //获取GPS现在的状态（打开或是关闭状态）
//      boolean gpsEnabled = Settings.Secure.isLocationProviderEnabled( context.getContentResolver(), LocationManager.GPS_PROVIDER );
//   
//      if(gpsEnabled)
//      { //关闭GPS
//       Settings.Secure.setLocationProviderEnabled( context.getContentResolver(), LocationManager.GPS_PROVIDER, false );
//      }
//      else
//      { //打开GPS 
//       Settings.Secure.setLocationProviderEnabled( context.getContentResolver(), LocationManager.GPS_PROVIDER, true );
//      }
//    }
    
    
    /*
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
    
    
    
    public void gettingGPS()//前提是getGPS函数调用成功，返回值为真，这个函数才能调用，否则会出现停止工作。
    {
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
            Toast.makeText(context, "GPS获取成功！", Toast.LENGTH_SHORT).show();
        }  
        else  
        {  
            // 未获取地理信息位置  
        	Toast.makeText(context, "GPS信号不好，请到室外或窗户旁边...", Toast.LENGTH_SHORT).show();
//            tv.setText("地理信息位置未知或正在获取地理信息位置中...");  
        }  
    }  
      
	
	
	
}
