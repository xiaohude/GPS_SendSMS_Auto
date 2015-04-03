package com.smarttiger.gpsimformation;  
  
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lib.DialogUtils;
import lib.SendSMS;
import lib.TimeUtils;

import cn.waps.AppConnect;

import com.smarttiger.gpsimformation.MyLocationListener.LocationSuccessListener;
import com.baidu.location.BDLocation;
import lib.SlidingMenu;
import com.smarttiger.gpsimformation.R;

import android.app.Activity;  
import android.app.AlertDialog;  
import android.app.AlertDialog.Builder;  
import android.content.Context;  
import android.content.DialogInterface;  
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;  
import android.graphics.Color;
import android.location.Criteria;  
import android.location.Location;  
import android.location.LocationManager;  
import android.os.Bundle;  
import android.view.KeyEvent;  
import android.view.LayoutInflater;
import android.view.Menu;  
import android.view.MenuItem;  
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;  
import android.widget.Toast;  
  
public class MainActivity extends Activity {
	private final static String APP_PID = "91";  
    private static TextView tv;  
    private EditText editText;
    private Button button;
    private Button topBtn;
    private TextView phoneNumText ;
    private LinearLayout childLayout;
    private LinearLayout parentLayout;
    private TextView titleText;
    private RadioButton missedcallRB;
    private RadioButton callRB;
    private ListView locationListView;
    private TextView childMessage;
    
    private Boolean isChild = true;
    
    
    private LocationManager lm;  
    private Criteria criteria;  
    private Location location;  
    private final static int MENU_ABOUT = Menu.FIRST;    
    private final static int MENU_EXIT = Menu.FIRST+1;  
    
    
    private GPSMessage GPSm;
    private SaveSet saveSet;
    private SendSMS sendSMS;
    
    private String phone;
    private String SMSmessage;
    private ListAdapter listAdapter;
    private LocationList locationList;
    private List<Map<String, Object>> mList;
    
    
    private MyLocationListener myLocationListener;
    
    private SlidingMenu mMenu;
    private View menuLayout;
    
    
    private int roleType = 1;
    private String titleStr0;
    private String titleStr1;
    private String topBtnStr0;
    private String topBtnStr1;
    private String phoneNumTextStr0;
    private String phoneNumTextStr1;
    
    
    /** Called when the activity is first created. */  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);  
        
        saveSet=new SaveSet(this);
        sendSMS=new SendSMS(this);
    	
        DialogUtils.debugType = saveSet.getDebugType();
        roleType = saveSet.getRoleType();

		mMenu = (SlidingMenu) findViewById(R.id.id_menu);
          
        titleText = (TextView) findViewById(R.id.titleText);
        tv = (TextView)findViewById(R.id.tv);  
        editText=(EditText)findViewById(R.id.editText);
        childLayout = (LinearLayout) findViewById(R.id.childLayout);
        parentLayout = (LinearLayout) findViewById(R.id.parentLayout);
        phoneNumText = (TextView) findViewById(R.id.phoneNumText);      
        topBtn = (Button) findViewById(R.id.topBtn);
        button = (Button)findViewById(R.id.button);
        callRB = (RadioButton) findViewById(R.id.callRB);
        missedcallRB = (RadioButton) findViewById(R.id.missedcallRB);
        locationListView = (ListView) findViewById(R.id.locationList);
        childMessage = (TextView) findViewById(R.id.childMessage);
        
        Intent intent = getIntent();
        SMSmessage = intent.getStringExtra("SMSmessage");  
        phone = intent.getStringExtra("phone");  
        //测试代码：
//        SMSmessage = "Lat(40.061013)\nLon(116.339465)\n地址：北京市海淀区建材城西路";
//        SMSmessage = "Lat(39.9132)\nLon(116.12346)\n地址：北京市海淀区";
//        SMSmessage = "Lat(39.91615)\nLon(116.403757)\n地址：北京市东城区中华路";
//        phone = "18888888888";
        
        
        locationList = new LocationList(MainActivity.this);
        mList = locationList.getList();
        if(SMSmessage != null)
        {
        	mList = locationList.addLocation(SMSmessage, TimeUtils.getTime(0), phone);
        }
        
        
        listAdapter = new ListAdapter(this);
        locationListView.setAdapter(listAdapter);
        
        
        childMessage.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setTitle("提示");
				builder.setIcon(android.R.drawable.ic_dialog_info);
				builder.setMessage("确认清空宝贝位置记录吗？");
				builder.setPositiveButton("确认", new OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						locationList.clearList();
						mList.clear();
						listAdapter.notifyDataSetChanged();
						Toast.makeText(MainActivity.this, "已清空记录", 1).show();
					}
				});
				builder.setNegativeButton("取消", null);
				builder.create().show();
				return true;
			}
		});
        
        editText.setText(saveSet.getNum());
        isChild = saveSet.getIsChild();
        
        
        missedcallRB.setChecked(saveSet.getTriggerCondition() == 1);
        callRB.setChecked(saveSet.getTriggerCondition() == 2);
        missedcallRB.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1)
					saveSet.setTriggerCondition(1);
			}
		});
        callRB.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1)
					saveSet.setTriggerCondition(2);
			}
		});
        
        topBtn.setOnClickListener(new Button.OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				isChild = !isChild;
				saveSet.setIsChild(isChild);
				initLayout();
			}
		});
        button.setOnClickListener(new Button.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("获取到编辑框的信息为："+editText.getText());
				String num = editText.getText().toString();
				if(isPhoneNum(num))
					saveSet.saveNum(num);
				else
					Toast.makeText(MainActivity.this, "请输入正确手机号!", 1).show();
				
				
//				sendSMS.sendSMS(editText.getText().toString(), tv.getText().toString());	
								
//				Intent intent2 = new Intent(MainActivity.this, MyService.class);
//				MainActivity.this.stopService(intent2);
//				MainActivity.this.startService(intent2);
				
//				Intent intent = new Intent(MainActivity.this, MyBaiduMap.class);
//				startActivity(intent);
			}
		});
        
        
//        GPSm=new GPSMessage(this);
////        GPSm.gettingGPS();        
//        tv.setText("经度"+GPSm.longitude+'\n'+  
//                "纬度"+GPSm.latitude+'\n'+  
//                "速度"+GPSm.speed+"m/s"+'\n'+  
//                "海拔"+GPSm.altitude+"m"+'\n'+  
//                "方位"+GPSm.bearing+'\n'+  
//                "具体地点"+GPSm.myLocation+'\n'); 
        
        
        myLocationListener = new MyLocationListener(this);
        myLocationListener.setLocationSuccessListener(new LocationSuccessListener() {		
			@Override
			public void getLocation(BDLocation location, String s) {
				// TODO Auto-generated method stub
				tv.setText(s);	
				myLocationListener.stopGetLocation();
			}
		});
        myLocationListener.startGetLocation();
        
        
//        try {
//        	//获取系统最高权限！
//            Runtime.getRuntime().exec("su");
//	    } catch (IOException e) {
//	            // TODO Auto-generated catch block
//	            e.printStackTrace();
//	    }
        
        refreshLayout();
        
        initAd();
        
        initItem();
    }  
    
    
    @Override
    protected void onRestart() {
    	// TODO Auto-generated method stub

    	missedcallRB.setChecked(saveSet.getTriggerCondition() == 1);
        callRB.setChecked(saveSet.getTriggerCondition() == 2);
    	
        mList = locationList.getList();
		listAdapter.notifyDataSetChanged();
    	super.onRestart();
    }
    
    private void initLayout()
    {
    	if(isChild)
		{
    		titleText.setText(titleStr0);
			topBtn.setText(topBtnStr0);
			phoneNumText.setText(phoneNumTextStr0);
			childLayout.setVisibility(View.VISIBLE);
			parentLayout.setVisibility(View.GONE);
			
			if(!myLocationListener.isStarted())
				myLocationListener.startGetLocation();
		}
		else
		{
			titleText.setText(titleStr1);
			topBtn.setText(topBtnStr1);
			phoneNumText.setText(phoneNumTextStr1);
			childLayout.setVisibility(View.GONE);
			parentLayout.setVisibility(View.VISIBLE);
		}
    }
    
    //广告模块初始化
    private void initAd()
    {
    	AppConnect.getInstance("80b27567fde0a74a99c149fead053490",APP_PID,this);
//    	AppConnect.getInstance(this); 

    	//设置迷你广告背景颜色 
    	AppConnect.getInstance(this).setAdBackColor(Color.argb(50, 120, 240, 120)); 
    	//设置迷你广告广告语颜色 
    	AppConnect.getInstance(this).setAdForeColor(Color.BLUE); 
    	//若未设置以上两个颜色，则默认为黑底白字 
    	LinearLayout miniLayout =(LinearLayout)findViewById(R.id.miniAdLinearLayout); 
    	AppConnect.getInstance(this).showMiniAd(this, miniLayout, 10); //默认 10 秒切换一次广告 
    }
    
    public static void setText(String s)
    {
    	tv.setText(
                "具体地点"+s+'\n'); 
    }
    
    
    
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
        	if(!mMenu.keyDown())
        		exit();    
            return true;    
        }    
        return super.onKeyDown(keyCode, event);    
    }   
      
    private void exit()    
    {    
    	 finish();
    	 
//         AlertDialog.Builder builder = new Builder(MainActivity.this);    
//         builder.setMessage("确认退出吗？");    
//         builder.setTitle("提示");    
//         builder.setPositiveButton("确认", new OnClickListener(){    
//             @Override    
//             public void onClick(DialogInterface arg0, int arg1) {    
//                 // TODO Auto-generated method stub    
//                 arg0.dismiss();  
//                 ActivityManager actMgr = (ActivityManager)getSystemService(ACTIVITY_SERVICE);    
//                 actMgr.restartPackage(getPackageName());  
//                 finish();
//             }    
//         });    
//         builder.setNegativeButton("取消", new OnClickListener(){    
//  
//             @Override    
//             public void onClick(DialogInterface dialog, int which) {    
//                 // TODO Auto-generated method stub    
//                 dialog.dismiss();    
//             }    
//         });    
//         builder.create().show();  
           
    }  
    
    
    public void showOnMap(View view)
    {
    	showOnMap(tv.getText().toString());
    } 
    public void showOnMap(String location)
    {
    	try {
    		System.out.println("location====="+location);
    		int latID0 = location.indexOf("(")+1;
    		int latID1 = location.indexOf(")");
    		int lonID0 = location.indexOf("(", latID1)+1;
    		int lonID1 = location.indexOf(")", lonID0);
    		
    		System.out.println("-------纬度："+latID0+"+"+latID1);
    		System.out.println("-------经度："+lonID0+"+"+lonID1);
    		
    		String lat = location.substring(latID0, latID1);
    		String lon = location.substring(lonID0, lonID1);
    		
    		if(lat.contains("4.9E-324"))
    		{
    			Toast.makeText(this, "孩子手机未连接网络或GPS，定位失败。", 1).show();
    			return;
    		}
    		
    		Intent intent = new Intent(MainActivity.this, MyBaiduMap.class);
    		intent.putExtra("isShowLocation", true);
    		intent.putExtra("latitude", Double.valueOf(lat));
    		intent.putExtra("lontitude", Double.valueOf(lon));
    		startActivity(intent);
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(this, "无宝贝信息", 1).show();
			Intent intent = new Intent(MainActivity.this, MyBaiduMap.class);
    		startActivity(intent);
		}
    }
    
    public void getChildLocation(View view)
    {
    	String phoneSet = saveSet.getNum();
    	String s;
		//按","将sResult解析出字符串
		StringTokenizer st = new StringTokenizer(phoneSet, ",");
		int strLeng = st.countTokens();
		if(strLeng == 0)
		{
			Toast.makeText(this, "无对方手机号", 1).show();
			return;
		}
		//如果只有一个手机号，则直接发送，不需弹出选择对话框。
		else if(strLeng == 1)
		{
			s =  st.nextToken();
			sendSMS.sendSMS(s, "zxhSmartTiger");
			return ;
		}
		final String[] mItems = new String[strLeng]; 
		for (int i=0; i<strLeng; i++) {
			s =  st.nextToken();
			mItems[i] = s;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);   
        builder.setTitle("选择宝贝手机号");  
        builder.setItems(mItems, new DialogInterface.OnClickListener() {  
            public void onClick(DialogInterface dialog, int which) {  
                //点击后弹出窗口选择了第几项  
                DialogUtils.debugShow(MainActivity.this, "", "你选择的手机号为：" + mItems[which]);
                sendSMS.sendSMS(mItems[which], "zxhSmartTiger");
            }  
        }); 
        builder.create().show();  
    }
    
    //用正则判断是否为手机号。
    private Boolean isPhoneNum(String num)
    {
    	ArrayList<String> sList = new ArrayList<String>();
    	String s;
    	Pattern p = Pattern.compile("1[3,4,5,7,8]\\d{9}");
		//按","将sResult解析出字符串
		StringTokenizer st = new StringTokenizer(num, ",");
		int strLeng = st.countTokens();
		for (int i=0; i<strLeng; i++) {
			s =  st.nextToken();
			Matcher m = p.matcher(s);
			if(!m.matches())
				return false;
			sList.add(s);
		}
        return true;
    }
    
    
    private class ListAdapter extends BaseAdapter {

    	private LayoutInflater inflater;
    	
    	public ListAdapter(Context context) {
			this.inflater = LayoutInflater.from(context);
		}
    	
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			if(convertView == null)
                convertView = inflater.inflate(R.layout.locationitem, null);
            
			TextView locationText = (TextView) convertView.findViewById(R.id.locationText);
			TextView timeText = (TextView) convertView.findViewById(R.id.timeText);
			TextView fromNameText = (TextView) convertView.findViewById(R.id.fromNameText);
			
			//倒序显示出来。
			position = mList.size() - position - 1;
			final String location = (String)mList.get(position).get("location");
			String time = (String)mList.get(position).get("time");
			String phone = (String)mList.get(position).get("phone");
			
			locationText.setText(location);
			timeText.setText(time);
			fromNameText.setText(phone);
			
			convertView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					showOnMap(location);
				}
			});
			
			convertView.setOnTouchListener(new OnTouchListener() {
	        	float x,mx;
	    		float y,my;///添加y的判断是为了防止用户在上下滑动邮件时不小心跳页.
	    		Boolean hasDeal = false;////记录是否处理此次滑动。如果处理，就返回true不让父控件的ontouch收到这事件了。
				@Override
				public boolean onTouch(View arg0, MotionEvent event) {
					// TODO Auto-generated method stub
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						System.out.println("ListItem----按下事件------DOWN");
						x = event.getX(); 
						y = event.getY();
						hasDeal = false;
						break;
					case MotionEvent.ACTION_MOVE:
						System.out.println("ListItem----移动事件");
						mx = event.getX();
						my = event.getY();
						if(Math.abs(my-y) > 5 && !hasDeal)
							if(Math.abs(my-y) > Math.abs(mx-x) )
							{
								System.out.println("ListItem----return true");
	//							return true;
								hasDeal = true;
							}
							else
								hasDeal = false;
							
						break;				
					case MotionEvent.ACTION_UP:
						System.out.println("ListItem----抬起事件------UP");
						break;				
					default:
						System.out.println("ListItem----其他事件------DEFAULT");
						break;
					}
					return hasDeal;
				}
			});
			
			return convertView;
		}
    }
    
    
    //菜单按钮的触发事件
    public void toggleMenu(View view)
	{
		mMenu.toggle();
	}
    //初始化菜单界面。
    private void initItem()
    {
    	menuLayout = findViewById(R.id.menuLayout);
    	LinearLayout item1 = (LinearLayout) menuLayout.findViewById(R.id.item1);
    	LinearLayout item3 = (LinearLayout) menuLayout.findViewById(R.id.item3);
    	
    	item1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent =  new Intent(MainActivity.this, about.class);
			    startActivity(intent);
			}
		});
    	item3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent =  new Intent(MainActivity.this, SetView.class);
			    startActivity(intent);
			}
		});
    	
    	RadioGroup radioGroup = (RadioGroup) menuLayout.findViewById(R.id.roleRadio);
    	if(roleType == 1)
    		radioGroup.check(R.id.roleRadio1);
    	else if(roleType == 2)
	    	radioGroup.check(R.id.roleRadio2);
    	else
    		radioGroup.check(R.id.roleRadio3);
    	
    	radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				switch (arg1) {
				case R.id.roleRadio1:
//					DialogUtils.debugShow(MainActivity.this, "", "家长孩子");
					roleType = 1;
					refreshLayout();
					saveSet.saveRoleType(roleType);
					break;
				case R.id.roleRadio2:
//					DialogUtils.debugShow(MainActivity.this, "", "情侣爱人");
					roleType = 2;
					refreshLayout();
					saveSet.saveRoleType(roleType);
					break;
				case R.id.roleRadio3:
//					DialogUtils.debugShow(MainActivity.this, "", "闺蜜基友");
					roleType = 3;
					refreshLayout();
					saveSet.saveRoleType(roleType);
					break;
				default:
					break;
				}
			}
		});
    }
    
    //重新被设置角色类型后刷新界面
    private void refreshLayout()
    {
    	if(roleType == 1)
    	{
    		titleStr0 = "宝贝界面";
    		titleStr1 = "家长界面";
    		topBtnStr0 = "我是家长";
    		topBtnStr1 = "我是孩子";
    		phoneNumTextStr0 = "家长手机号：";
    		phoneNumTextStr1 = "宝贝手机号：";
    	}
    	else if(roleType == 2)
    	{
    		titleStr0 = "我的位置信息";
    		titleStr1 = "爱人的位置信息";
    		topBtnStr0 = "爱人信息";
    		topBtnStr1 = "我的信息";
    		phoneNumTextStr0 = "对方的手机号：";
    		phoneNumTextStr1 = "对方的手机号：";
    	}
    	else
    	{
    		titleStr0 = "我的位置信息";
    		titleStr1 = "Ta的位置信息";
    		topBtnStr0 = "Ta的信息";
    		topBtnStr1 = "我的信息";
    		phoneNumTextStr0 = "对方的手机号：";
    		phoneNumTextStr1 = "对方的手机号：";
    	}
    	
    	
    	initLayout();
    }
} 