package com.example.gpsimformation;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;



public class SaveSet {
	
	Context context;
	SharedPreferences.Editor editor;
	SharedPreferences pref;
	
	String phoneNumber;
	boolean bool;
	
	
	public SaveSet(Context context)
	{
		this.context=context;
		
		pref=context.getSharedPreferences("xiaohu", 0);
		editor = pref.edit();
		phoneNumber=pref.getString("PhoneNumber", "0");//第二个参数相当于对time初始化为30。
		bool=pref.getBoolean("rightPhone", false);
	}
	
	
	public String getNum()
	{
		phoneNumber=pref.getString("PhoneNumber", "0");
		return phoneNumber;
	}
	
	public void saveNum(String num)
	{
	//保存和读取用户设置；		
		System.out.println("saveSet中的save函数！！！");
					
//		time= Integer.parseInt(timeEdit.getText().toString());
		editor.putString("PhoneNumber", num);
		
					
		editor.commit();

		Toast.makeText(context, "号码保存成功！" , Toast.LENGTH_SHORT).show();
								
	}
	
	public boolean getBool()
	{
		bool=pref.getBoolean("rightPhone", false);
		return bool;
		
	}
	public void setBool(boolean b)
	{
		editor.putBoolean("rightPhone", b);
		
		editor.commit();
	}

}
