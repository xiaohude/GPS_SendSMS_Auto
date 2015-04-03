package lib;


import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;


public class DialogUtils {
	
	/**
	 * 调试输出类型，1:Toast输出，2:Dialog输出。0:其他为不输出。这个在MainActivity中获取并修改了。
	 * */
	public static int debugType = 0;
	
	/**
	 * 类似登陆失败提示类型
	 * @param activity Activity
	 * @param title 标题
	 * @param message 显示信息
	 */
    public static void showDialog(Context activity, String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(title);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setMessage(message);
		builder.setPositiveButton("确认", null);
		builder.create().show();
    }
    
    
    /**
     * 用来显示调试信息的。
     * */
    public static void debugShow(Context context, String title, String message) {
    	if(debugType == 1)
    	{
    		Toast.makeText(context, title + "-" + message, 1).show();
    	}
    	else if(debugType == 2)
    	{
    		showDialog(context, title, message);
    	}
    } 
      
}
