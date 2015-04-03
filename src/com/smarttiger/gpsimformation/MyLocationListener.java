package com.smarttiger.gpsimformation;

import lib.DialogUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

public class MyLocationListener {
	private LocationClient mLocationClient;
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private MyLocationListenner myListener ;
	private LocationSuccessListener locationSuccessListener = null;
	private Context context;
	
	public MyLocationListener(Context context) {
		// TODO Auto-generated method stub
		this.context = context;
		
		
		myListener = new MyLocationListenner();
		mLocationClient = new LocationClient(context);
		mLocationClient.registerLocationListener( myListener );
		InitLocation();
	}
	
	public void startGetLocation()
	{
		mLocationClient.start();	

	}
	public void stopGetLocation()
	{
		mLocationClient.stop();
	}
	public Boolean isStarted()
	{
		return mLocationClient.isStarted();
	}
	public void setLocationSuccessListener(LocationSuccessListener locationSuccessListener)
	{
		this.locationSuccessListener = locationSuccessListener;
	}
	
	public interface LocationSuccessListener 
	{
		public void getLocation(BDLocation location, String s);
	}

	private void InitLocation(){
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);//设置定位模式
		option.setCoorType("bd09ll"); // 返回的定位结果是百度经纬度，默认值gcj02（国标），因为用百度地图显示，所以还是用百度坐标好。
		option.setScanSpan(1000);//设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);//返回的定位结果包含地址信息
//		option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向，手机上部正朝向北的方向为0°方向
		option.setOpenGps( true );//设置是否打开gps，使用gps前提是用户硬件打开gps。默认是不打开gps的。
		mLocationClient.setLocOption(option);
	}
	
	
	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;		
			
			StringBuffer sb = new StringBuffer(256);
//			sb.append("时间 : ");
//			sb.append(location.getTime());
			
			sb.append("Lat(");
			sb.append(location.getLatitude());
			sb.append(")\n");
			
			sb.append("Lon(");
			sb.append(location.getLongitude());
			sb.append(")\n");
			
//			sb.append("\n精度(m) : ");
//			sb.append(location.getRadius());
//			sb.append("\n方向(北=0) : ");
//			sb.append(location.getDirection());
			if (location.getLocType() == BDLocation.TypeGpsLocation){
//				sb.append("\nspeed : ");
//				sb.append(location.getSpeed());
//				sb.append("\nsatellite : ");
//				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
//				sb.append("\n省：");
//				sb.append(location.getProvince());
//				sb.append("\n市：");
//				sb.append(location.getCity());
//				sb.append("\n区/县：");
//				sb.append(location.getDistrict());
				sb.append("地址 :");
				sb.append(location.getAddrStr());
			}
//			sb.append("\nerror code : ");
//			sb.append(location.getLocType());
//			sb.append("\nsdk version : ");
//			sb.append(mLocationClient.getVersion());
//			sb.append("\nisCellChangeFlag : ");
//			sb.append(location.isCellChangeFlag());
			int code = location.getLocType();
			DialogUtils.debugShow(context, "定位code", "" + code);
			if(code==62)
				sb.append("定位失败，无网络");
			else if(code==68)
				sb.append("离线定位数据");
			
			String s = sb.toString();
			
			if(locationSuccessListener != null)
				locationSuccessListener.getLocation(location, s);
		}

	}
	
	
}
