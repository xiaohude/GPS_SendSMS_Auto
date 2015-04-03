package lib;

import java.text.SimpleDateFormat;

import android.text.format.Time;

public class TimeUtils {

	public TimeUtils() {
		// TODO Auto-generated constructor stub
		//获取时间戳：
		Long tsLong = System.currentTimeMillis()/1000;
        String ts2 = tsLong.toString();
        System.out.println("当前时间戳为："+ts2);

		//获取当前时间：	
		Time time = new Time("GMT+8");       
		time.setToNow();      
		int year = time.year;      
		int month = time.month+1;      
		int day = time.monthDay;      
		int minute = time.minute;      
		int hour = time.hour;      
		int sec = time.second;      
		System.out.println("当前时间为：" 
							+ year  + "年 " 
							+ month + "月 " 
							+ day   + "日 " 
							+ hour  + "时 "
							+ minute+ "分 " 
							+ sec   + "秒"); 
	}
	
	/**
	 * 获取当前时间戳
	 * */
	public static String getTimeStamp()
	{
		Long tsLong = System.currentTimeMillis()/1000;
        return tsLong.toString();
	}
	
	/**
	 * 获取当前时间，
	 * type = 0用符号间隔
	 * type = 1用汉字间隔
	 * */
	public static String getTime(int type)
	{
		String result = "";
		SimpleDateFormat sDateFormat;
		//SimpleDateFormat("E HH:mm:ss");E 为周四，EEEE为星期四，要想换语言：SimpleDateFormat("yyyy年MM月dd日 E",Locale.JAPAN);
		//查资料发现，hh表示的是12小时制，HH才是24小时制        
		if(type == 0)
			sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
		else
			sDateFormat = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");   
		
		
			
		result = sDateFormat.format(new java.util.Date());  		
		
		return result;
	}
	
}
