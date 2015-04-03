package com.smarttiger.gpsimformation;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipData.Item;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.smarttiger.gpsimformation.R;


/**
 * 此demo用来展示如何结合定位SDK实现定位，并使用MyLocationOverlay绘制定位位置 同时展示如何使用自定义图标绘制并点击时弹出泡泡
 * 
 */
public class MyBaiduMap extends Activity implements OnGetGeoCoderResultListener {

	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;

	MapView mMapView;
	BaiduMap mBaiduMap;

	// UI相关
	OnCheckedChangeListener radioButtonListener;
	Button requestLocButton;
	boolean isFirstLoc = true;// 是否首次定位
	

	//返回值所用
	private double myLatitude ;
	private double myLontitude ;
	private String myAddress = null;
	
	private double latitude = 0;
	private double lontitude = 0;
	private String address = null;
	private Marker marker;
	
	private GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	private TextView locationText;
	private Button coordinateBtn;
	private Button myLocationBtn;

	private Boolean isShowLocation = false;
	
	private TextView titleText ;
	private EditText latitudeEdit ;
	private EditText lontitudeEdit ;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.mybaidumap);
		
		Intent intent = getIntent();
		isShowLocation = intent.getBooleanExtra("isShowLocation", false);
		latitude = intent.getDoubleExtra("latitude", 0);
		lontitude = intent.getDoubleExtra("lontitude", 0);
		
		
		locationText = (TextView) findViewById(R.id.locationText);
		coordinateBtn = (Button) findViewById(R.id.btnCoordinate);
		myLocationBtn = (Button) findViewById(R.id.btnMyLocation);
		
		
		latitudeEdit = (EditText) findViewById(R.id.latitudeEdit);
		lontitudeEdit = (EditText) findViewById(R.id.lontitudeEdit);
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setOnLongClickListener(new OnLongClickListener() {		
			@Override
			public boolean onLongClick(View arg0) {
				// TODO Auto-generated method stub
				initMySearchLayout();
				return true;
			}
		});
		
		
//		requestLocButton = (Button) findViewById(R.id.button1);
//		mCurrentMode = LocationMode.NORMAL;
//		requestLocButton.setText("普通");
//		OnClickListener btnClickListener = new OnClickListener() {
//			public void onClick(View v) {
//				switch (mCurrentMode) {
//				case NORMAL:
//					requestLocButton.setText("跟随");
//					mCurrentMode = LocationMode.FOLLOWING;
//					mBaiduMap
//							.setMyLocationConfigeration(new MyLocationConfiguration(
//									mCurrentMode, true, mCurrentMarker));
//					break;
//				case COMPASS:
//					requestLocButton.setText("普通");
//					mCurrentMode = LocationMode.NORMAL;
//					mBaiduMap
//							.setMyLocationConfigeration(new MyLocationConfiguration(
//									mCurrentMode, true, mCurrentMarker));
//					break;
//				case FOLLOWING:
//					requestLocButton.setText("罗盘");
//					mCurrentMode = LocationMode.COMPASS;
//					mBaiduMap
//							.setMyLocationConfigeration(new MyLocationConfiguration(
//									mCurrentMode, true, mCurrentMarker));
//					break;
//				}
//			}
//		};
//		requestLocButton.setOnClickListener(btnClickListener);
//
//		RadioGroup group = (RadioGroup) this.findViewById(R.id.radioGroup);
//		radioButtonListener = new OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(RadioGroup group, int checkedId) {
//				if (checkedId == R.id.defaulticon) {
//					// 传入null则，恢复默认图标
//					mCurrentMarker = null;
//					mBaiduMap
//							.setMyLocationConfigeration(new MyLocationConfiguration(
//									mCurrentMode, true, null));
//				}
//				if (checkedId == R.id.customicon) {
//					// 修改为自定义marker
//					mCurrentMarker = BitmapDescriptorFactory
//							.fromResource(R.drawable.page_indicator_focused);
//					mBaiduMap
//							.setMyLocationConfigeration(new MyLocationConfiguration(
//									mCurrentMode, true, mCurrentMarker));
//				}
//			}
//		};
//		group.setOnCheckedChangeListener(radioButtonListener);

		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		
		//添加单击事件
		if(!isShowLocation)
			mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
				public void onMapClick(LatLng point) {
					latitude = point.latitude;
					lontitude = point.longitude;
					
					mBaiduMap.clear();
					marker = (Marker) mBaiduMap.addOverlay(new MarkerOptions().position(point)
										.icon(BitmapDescriptorFactory
											.fromResource(R.drawable.icon_map_mark)));
					locationText.setText("......");
					
					getAddress(latitude, lontitude);
				}
	
				public boolean onMapPoiClick(MapPoi poi) {
					return false;
				}
			});
		
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {		
			@Override
			public boolean onMarkerClick(Marker arg0) {
				// TODO Auto-generated method stub
				if (arg0 == marker)
				{
					locationText.setText(address);
				}
				return true;
			}
		});
				
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		option.setIsNeedAddress(true);
		mLocClient.setLocOption(option);
		
		
		// 初始化搜索模块，注册事件监听
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		
		if(isShowLocation)
		{
//			sendBtn.setVisibility(View.GONE);
//			myLocationBtn.setVisibility(View.GONE);
			getAddress(latitude, lontitude);
		}
		else
			mLocClient.start();
	}

	private void getAddress(double latitude ,double lontitude )
	{
		setEditText(latitude, lontitude);
		LatLng ptCenter = new LatLng(latitude, lontitude);
		// 反Geo搜索
		mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));
	}
	
	//按地址搜索结果。
	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(MyBaiduMap.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
		mBaiduMap.clear();
		mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.icon_map_mark)));
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
				.getLocation()));
		String strInfo = String.format("纬度：%f 经度：%f",
				result.getLocation().latitude, result.getLocation().longitude);
		Toast.makeText(MyBaiduMap.this, strInfo, Toast.LENGTH_LONG).show();
	}
	
	//按经纬度搜索结果
	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(MyBaiduMap.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
		if(mBaiduMap == null)
			return;
		mBaiduMap.clear();
		marker = (Marker) mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
							.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.icon_map_mark)));
		//调整地图显示中心
		if(isShowLocation)
			mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result.getLocation()));
		
		address = result.getAddress();	
//		Toast.makeText(MyBaiduMap.this, result.getAddress(),Toast.LENGTH_LONG).show();
		locationText.setText(address);
		

	}
	
	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			
			myLatitude = location.getLatitude();
			myLontitude = location.getLongitude();
			myAddress = location.getAddrStr();
			
			locationText.setText(myAddress);
			
			
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(myLatitude)
					.longitude(myLontitude).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(myLatitude, myLontitude);
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
			
			//如果已经获取成功了，就停止获取，防止占内存。
			mLocClient.stop();
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}

	
	public void OnBack(View view) {
		finish();
	}

	public void OnSend(View view) {
//		Intent intent1 = new Intent();
//		if(latitude == 0)
//		{
//			intent1.putExtra("latitude", "" + myLatitude);
//			intent1.putExtra("lontitude", "" + myLontitude);
//			intent1.putExtra("address", myAddress);
//		}
//		else
//		{
//			intent1.putExtra("latitude", "" + latitude);
//			intent1.putExtra("lontitude", "" + lontitude);
//			intent1.putExtra("address", address);
//		}
//		setResult(0, intent1);
//		mLocClient.stop();
//		finish();
		
		initMySearchLayout();
	}
	
	public void OnMyLocation(View view) {
		if(isShowLocation)
			mLocClient.start();
		else
			getAddress(myLatitude, myLontitude);
	}

	
	private void initMySearchLayout()
	{
		LinearLayout searchLayout = (LinearLayout) findViewById(R.id.searchLayout);
		Button searchBtn = (Button) findViewById(R.id.btnSearch);
		
		if(searchLayout.getVisibility() == 0)
			searchLayout.setVisibility(View.GONE);
		else
			searchLayout.setVisibility(View.VISIBLE);
		searchBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				latitude = Double.valueOf(latitudeEdit.getText().toString());
				lontitude = Double.valueOf(lontitudeEdit.getText().toString());
				getAddress(latitude, lontitude);
			}
		});
	}
	private void setEditText(Double latitude, Double lontitude)
	{
		latitudeEdit.setText(latitude.toString());
		lontitudeEdit.setText(lontitude.toString());
	}
	
	public void onCoordinate(View view)
	{
//		initMySearchLayout();
		initSMSLayout();
	}
	
	private void initSMSLayout()
	{
		final LinearLayout smsLayout = (LinearLayout) findViewById(R.id.smsLayout);
		Button pasteBtn = (Button) smsLayout.findViewById(R.id.btnPaste);
		Button searchBtn = (Button) smsLayout.findViewById(R.id.btnSearch);
		final EditText smsEdit = (EditText) smsLayout.findViewById(R.id.smsEdit);
		
		if(smsLayout.getVisibility() == 0)
			smsLayout.setVisibility(View.GONE);
		else
			smsLayout.setVisibility(View.VISIBLE);
		
		pasteBtn.setOnClickListener(new OnClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ClipboardManager clipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);  
		        Item item = null;
		        //无数据时直接返回  
		        if(!clipboard.hasPrimaryClip()){  
		            Toast.makeText(getApplicationContext(), "剪贴板中无数据", Toast.LENGTH_SHORT).show();  
		            return ;  
		        }
		        //如果是文本信息  
		        if (clipboard.getPrimaryClipDescription().hasMimeType(  
		                ClipDescription.MIMETYPE_TEXT_PLAIN)) {  
		            ClipData cdText = clipboard.getPrimaryClip();  
		            item = cdText.getItemAt(0);  
		            //此处是TEXT文本信息  
		            if(item.getText() == null){  
		                Toast.makeText(getApplicationContext(), "剪贴板中无内容", Toast.LENGTH_SHORT).show();  
		                return ;  
		            }else{  
		                smsEdit.setText(item.getText());
		            }  
		        }
		        else
		        {
		        	Toast.makeText(getApplicationContext(), "剪贴板中无短信数据", Toast.LENGTH_SHORT).show();  
		            return ; 
		        }
			}
		});
		
		searchBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String location = smsEdit.getText().toString();
				try {
					int latID0 = location.indexOf("(")+1;
		    		int latID1 = location.indexOf(")");
		    		int lonID0 = location.indexOf("(", latID1)+1;
		    		int lonID1 = location.indexOf(")", lonID0);
		    		String lat = location.substring(latID0, latID1);
		    		String lon = location.substring(lonID0, lonID1);
		    		if(lat.contains("4.9E-324"))
		    		{
		    			Toast.makeText(MyBaiduMap.this, "孩子手机未连接网络或GPS，定位失败。", 1).show();
		    			return;
		    		}
					latitude = Double.valueOf(lat);
					lontitude = Double.valueOf(lon);
					getAddress(latitude, lontitude);
					smsLayout.setVisibility(View.GONE);
				} catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(MyBaiduMap.this, "非位置短信格式！", 1).show();
				}
			}
		});
	}
}

