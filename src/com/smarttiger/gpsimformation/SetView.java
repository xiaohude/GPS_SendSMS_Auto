package com.smarttiger.gpsimformation;

import lib.DialogUtils;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 点击登录界面的小i图标启动的activity，显示软件帮助和软件基本信息的
 * */
public class SetView extends Activity {
	/** Called when the activity is first created. */
	private SaveSet saveSet;
	private EditText editText;
	private int triggerCondition;
	private CheckBox isSaveSMSBox;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_view);
		
		saveSet = new SaveSet(SetView.this);
		
		triggerCondition = saveSet.getTriggerCondition();
		
		isSaveSMSBox = (CheckBox) findViewById(R.id.isSaveSMSBox);
		editText  = (EditText) findViewById(R.id.numEditText);
		editText.setText(""+saveSet.getChildLocationItemNum());
		
		RadioGroup conditionRadio = (RadioGroup) findViewById(R.id.conditionRadio);
		if(triggerCondition == 1)
			conditionRadio.check(R.id.missedcallRB);
		else if(triggerCondition == 2)
			conditionRadio.check(R.id.callRB);
		else
			conditionRadio.check(R.id.noCallRB);
		conditionRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				switch (arg1) {
				case R.id.missedcallRB:
					saveSet.setTriggerCondition(1);
					break;
				case R.id.callRB:
					saveSet.setTriggerCondition(2);
					break;
				case R.id.noCallRB:
					saveSet.setTriggerCondition(3);
					break;
				default:
					break;
				}
			}
		});
		
		
		isSaveSMSBox.setChecked(saveSet.getIsSaveSMS());
		isSaveSMSBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				saveSet.setIsSaveSMS(arg1);
				DialogUtils.debugShow(SetView.this, "IsSaveSMS", ""+saveSet.getIsSaveSMS());
			}
		});
		
		
	}

	public void onBackToMain(View view) {
		this.finish();
	}
	
	public void onSave(View view)
	{
		String s = editText.getText().toString();
		saveSet.saveChildLocationItemNum(Integer.valueOf(s));
		
		Toast.makeText(SetView.this, "保存成功", 1).show();

		this.finish();
	}
	public void onClean(View view)
	{
		LocationList locationList = new LocationList(SetView.this);
		locationList.clearList();
		
		Toast.makeText(SetView.this, "已清空记录", 1).show();

	}
}