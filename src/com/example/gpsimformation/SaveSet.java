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
		phoneNumber=pref.getString("PhoneNumber", "0");//�ڶ��������൱�ڶ�time��ʼ��Ϊ30��
		bool=pref.getBoolean("rightPhone", false);
	}
	
	
	public String getNum()
	{
		phoneNumber=pref.getString("PhoneNumber", "0");
		return phoneNumber;
	}
	
	public void saveNum(String num)
	{
	//����Ͷ�ȡ�û����ã�		
		System.out.println("saveSet�е�save����������");
					
//		time= Integer.parseInt(timeEdit.getText().toString());
		editor.putString("PhoneNumber", num);
		
					
		editor.commit();

		Toast.makeText(context, "���뱣��ɹ���" , Toast.LENGTH_SHORT).show();
								
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
