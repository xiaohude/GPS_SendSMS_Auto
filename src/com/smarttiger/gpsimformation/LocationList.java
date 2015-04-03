package com.smarttiger.gpsimformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.widget.Toast;

import lib.MySDcardFile;


/**
 * 用于保存宝贝位置信息的列表
 * */
public class LocationList {
	
	private Context context;
	private MySDcardFile mySDcardFile;
	private List<Map<String, Object>> mList;
	private SaveSet saveSet;
	private int maxNum;
	
	public LocationList(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		mySDcardFile = new MySDcardFile();
		mList = new ArrayList<Map<String,Object>>();
		saveSet = new SaveSet(context);
		maxNum = saveSet.getChildLocationItemNum();
	}
	
	public List<Map<String, Object>> getList()
	{
		mList.clear();
		try {
			ArrayList<String> phoneList = mySDcardFile.getStringArrayList("phoneList");
			ArrayList<String> locationList = mySDcardFile.getStringArrayList("locationList");
			ArrayList<String> timeList = mySDcardFile.getStringArrayList("timeList");
			for (int i = 0; i < locationList.size(); i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("phone", phoneList.get(i));
				map.put("location", locationList.get(i));
		        map.put("time", timeList.get(i));
				mList.add(map);
			}	
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(context, "缓存数据错误，自动清空缓存。", 1).show();
			clearList();
		}
		return mList;
	}
	public List<Map<String, Object>> addLocation(String location, String time, String phone)
	{
		if(mList.size() >= maxNum)
		{
			mList.remove(0);
		}	
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("phone", phone);
		map.put("location", location);
        map.put("time", time);
		mList.add(map);
		
		saveList(mList);
		
		return mList;
	}
	
	private void saveList(List<Map<String, Object>> mList)
	{
		ArrayList<String> phoneList = new ArrayList<String>();
		ArrayList<String> locationList = new ArrayList<String>();
		ArrayList<String> timeList = new ArrayList<String>();
		for(int i = 0; i < mList.size(); i++)
		{
			Map<String, Object> map = mList.get(i);
			phoneList.add((String)map.get("phone"));
			locationList.add((String)map.get("location"));
			timeList.add((String)map.get("time"));
		}
		mySDcardFile.saveStringArrayList(phoneList, "phoneList");		
		mySDcardFile.saveStringArrayList(locationList, "locationList");
		mySDcardFile.saveStringArrayList(timeList, "timeList");
	}
	
	public void clearList()
	{
		mySDcardFile.deleteSDFile("phoneList");
		mySDcardFile.deleteSDFile("locationList");
		mySDcardFile.deleteSDFile("timeList");
	}
	
}
