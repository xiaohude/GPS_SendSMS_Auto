package com.smarttiger.gpsimformation;

import lib.DialogUtils;
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
		phoneNumber = pref.getString("PhoneNumber", "11111111111");//第二个参数相当于对time初始化为111。
		bool = pref.getBoolean("rightPhone", false);
	}
	
	
	public String getNum()
	{
		phoneNumber=pref.getString("PhoneNumber", "");
		return phoneNumber;
	}
	//保存和读取用户设置；
	public void saveNum(String num)
	{
		editor.putString("PhoneNumber", num);
		editor.commit();
		Toast.makeText(context, "号码保存成功！" , Toast.LENGTH_SHORT).show();
	}
	
	//保存和读取短信目接收号码，属于临时变量。
	public String getReceiverNum()
	{
		return pref.getString("ReceiverNum", "");
	}
	public void saveReceiverNum(String num)
	{
		editor.putString("ReceiverNum", num);
		editor.commit();
	}
	
	/**
	 * 记录下是否是家长来电。挂断电话后是否要发送地址信息
	 * */
	public boolean getBool()
	{
		bool=pref.getBoolean("rightPhone", false);
		return bool;
		
	}
	/**
	 * 挂断电话后是否要发送地址信息
	 * */
	public void setBool(boolean b)
	{
		editor.putBoolean("rightPhone", b);		
		editor.commit();
	}
	
	
	public boolean getIsChild()
	{
		bool = pref.getBoolean("isChild", true);
		return bool;		
	}
	public void setIsChild(boolean b)
	{
		editor.putBoolean("isChild", b);	
		editor.commit();
	}

	/**
	 * 通话状态触发发送地址信息短信的条件
	 * 3：都不自动返回孩子地址信息。
	 * 2：只要家长来电话就自动返回孩子地址信息。
	 * 1：未接家长电话才返回，已接不返回。
	 * */
	public int getTriggerCondition()
	{
		return pref.getInt("triggerCondition", 1);
	}
	public void setTriggerCondition(int i)
	{
		editor.putInt("triggerCondition", i);	
		editor.commit();
	}
	
	
	/**
	 * 获取最大记录多少条位置信息
	 * */
	public int getChildLocationItemNum()
	{
		return pref.getInt("itemNum", 20);
	}
	/**
	 * 保存最大记录多少条位置信息
	 * */
	public void saveChildLocationItemNum(int num)
	{
		editor.putInt("itemNum", num);	
		editor.commit();
	}
	
	
	/**
	 * 获取用户角色类型
	 * */
	public int getRoleType()
	{
		return pref.getInt("roleType", 1);
	}
	/**
	 * 保存用户角色类型
	 * */
	public void saveRoleType(int roleType)
	{
		editor.putInt("roleType", roleType);	
		editor.commit();
	}
	
	
	/**
	 * 获取调试输出类型
	 * */
	public int getDebugType()
	{
		return pref.getInt("debugType", 0);
	}
	/**
	 * 保存调试输出类型
	 * */
	public void saveDebugType(int debugType)
	{
		editor.putInt("debugType", debugType);	
		editor.commit();
	}
	
	/**
	 * 是否保存位置信息到短信
	 * */
	public boolean getIsSaveSMS()
	{
		return pref.getBoolean("isSaveSMS", false);	
	}
	/**
	 * 是否保存位置信息到短信
	 * */
	public void setIsSaveSMS(boolean b)
	{
		editor.putBoolean("isSaveSMS", b);	
		editor.commit();
	}
	
}
