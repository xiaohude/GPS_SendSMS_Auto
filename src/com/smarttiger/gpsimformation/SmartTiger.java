package com.smarttiger.gpsimformation;


import cn.waps.AppConnect;

import com.baidu.mapapi.SDKInitializer;

import android.app.Application;

public class SmartTiger extends Application{

	@Override
	public void onCreate() {
		super.onCreate();
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		SDKInitializer.initialize(this);
	}

}
