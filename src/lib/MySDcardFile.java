package lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.http.util.EncodingUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;

/**
 * SD卡保存获取数据  File工具类
 * 可保存Stringlist，BitmapList，AppInfoList
 * @author 朱小虎
 * @since 2014-12-01
 */
public class MySDcardFile {
	
	public static String FirstFolder = "SmartTiger";// 一级目录
	public static String SecondFolder = "MyCache";// 二级目录
	/* ALBUM_PATH取得机器的SD卡位置，File.separator为分隔符“/” */
	private final static String ALBUM_PATH = Environment
			.getExternalStorageDirectory()
			+ File.separator
			+ FirstFolder
			+ File.separator;
	private static String Second_PATH = ALBUM_PATH + SecondFolder
			+ File.separator;
	
	private String directoryPath;//默认目录地址。
	
	//用于保存StringList的间隔符。这个间隔符得是不易出现的，似乎不能有：否则会识别出问题。似乎这个只能是一个字符。
	private String spaceMark = "$";
	
	//无参数表明二级目录名为MyCache
	public MySDcardFile() {
		
		File tongdaroot = new File(ALBUM_PATH);// 新建一级主目录，如果不存在则新建
		if (!tongdaroot.exists()) {
			tongdaroot.mkdir();
		}
		File downloadfilePath = new File(Second_PATH);// 新建二级主目录
		if (!downloadfilePath.exists()) {// 判断文件夹目录是否存在
			downloadfilePath.mkdir();// 如果不存在则创建
		}
		
		directoryPath = downloadfilePath + "/" ;
	}
	
	//参数为二级目录名。
	public MySDcardFile(String SecondFolder) {
		
		File tongdaroot = new File(ALBUM_PATH);// 新建一级主目录，如果不存在则新建
		if (!tongdaroot.exists()) {
			tongdaroot.mkdir();
		}
		Second_PATH = ALBUM_PATH + SecondFolder + File.separator;;
		File downloadfilePath = new File(Second_PATH);// 新建二级主目录
		if (!downloadfilePath.exists()) {// 判断文件夹目录是否存在
			downloadfilePath.mkdir();// 如果不存在则创建
		}
		
		directoryPath = downloadfilePath + "/" ;
	}
	
	//重置完整保存路径。不使用默认路径。
	public void SetFullPath(String fullPath)
	{
		if(fullPath.endsWith("/"))
			directoryPath = fullPath;
		else
			directoryPath = fullPath + "/";
	}
	
	//在二级目录下是否有此文件  
	public Boolean hasFile(String fileName) {    	  
		Boolean hasFile;		
        File file = new File(directoryPath + fileName);    
        hasFile = file.exists();      
        return hasFile;    
	}  

	//读文件  
	public String readSDFile(String fileName) throws IOException {    
	  
		String result;
		
        File file = new File(directoryPath + fileName);    
  
        FileInputStream fis = new FileInputStream(file);    
  
        int length = fis.available();   
  
        byte [] buffer = new byte[length];   
        fis.read(buffer);       
  
        result = EncodingUtils.getString(buffer, "UTF-8");   
  
        fis.close();       
        return result;    
	}    
	  
	//写文件  
	public void writeSDFile(String fileName, String write_str) throws IOException{    
	  
        File file = new File(directoryPath + fileName);    
  
        FileOutputStream fos = new FileOutputStream(file);    
  
        byte [] bytes = write_str.getBytes();   
  
        fos.write(bytes);   
  
        fos.close();   
	}   
	
	//保存Bitmap
	public void saveBitMap(String fileName,Bitmap bitmap)
	{
		File file = new File(directoryPath + fileName);  
		
		try {
            file.createNewFile();
            OutputStream outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);//如果选用JPEG格式，就会导致透明背景变成黑色。
            outStream.flush();
            outStream.close();
        } catch (FileNotFoundException e) {
        	System.out.println("SDcardFile-----saveBitMap()---异常--无文件");
        } catch (IOException e) {
        	System.out.println("SDcardFile-----saveBitMap()---异常--IOException");
        }
	}
	//获取bitmap
	private static BitmapFactory.Options sBitmapOptions;
	static {
		sBitmapOptions = new BitmapFactory.Options();
		sBitmapOptions.inPurgeable=true; //bitmap can be purged to disk
	}
	public Bitmap getBitmap(String fileName) throws FileNotFoundException{
		File file = new File(directoryPath + fileName); 
		if(file != null){
			Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, sBitmapOptions);
//			Bitmap bitmap2 = BitmapFactory.decodeFile(directoryPath + fileName); //还可以直接这么用
			if(bitmap != null){
				return bitmap;
			}
		}
		return null;
	}
	//保存Drawable
	public void saveDrawable(String fileName,Drawable drawable)
	{
		Bitmap bitmap=((BitmapDrawable)drawable).getBitmap();
		saveBitMap(fileName, bitmap);
	}
	//获取Drawable
	public Drawable getDrawable(String fileName) throws FileNotFoundException{
		File file = new File(directoryPath + fileName); 
		if(file.exists()){
			Drawable drawable = Drawable.createFromStream(new FileInputStream(file), "src");
			return drawable;
		}
		return null;
	}
	
	//保存ArrayList<String>  StringList里面不能有""。否则保存后无法识别。已在下面判断解决。
	public void saveStringArrayList(ArrayList<String> sList, String fileName)
	{
		String saveString = "";
		for(int i=0;i<sList.size();i++)
		{
			if("".equals(sList.get(i)))
				saveString = saveString + spaceMark + " ";
			else
				saveString = saveString + spaceMark + sList.get(i);
		}
		try {
			writeSDFile(fileName, saveString);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//获取ArrayList<String>
	public ArrayList<String> getStringArrayList(String fileName)
	{
		
		ArrayList<String> sList = new ArrayList<String>();
		if(!hasFile(fileName))
			return sList;
		String sResult = null;
		try {
			sResult = readSDFile(fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//按"-$-"将sResult解析出字符串
		StringTokenizer st = new StringTokenizer(sResult, spaceMark);
		int strLeng = st.countTokens();
		for (int i=0; i<strLeng; i++) {
			sList.add(st.nextToken());
		}
		System.out.println("getStringArrayList()--------sList===" + sList);
		return sList;
	}
	
	//保存ArrayList<Bitmap>
	public void saveBitmapArrayList(ArrayList<Bitmap> btmList, String dirName)
	{		
		File bitmapListFile = new File( directoryPath + dirName );// 新建三级目录
		if (!bitmapListFile.exists()) {// 判断文件夹目录是否存在
			bitmapListFile.mkdir();// 如果不存在则创建
		}
		String fileName = dirName + "/" ;
		for(int i=0;i<btmList.size();i++)
		{
			saveBitMap(fileName+i, btmList.get(i));
		}
	}
	//获取ArrayList<Bitmap>
	public ArrayList<Bitmap> getBitmapArrayList(String dirName)
	{
		ArrayList<Bitmap> btmList = new ArrayList<Bitmap>();
		if(!hasFile(dirName))
			return btmList;
		File bitmapListFile = new File( directoryPath + dirName );
		int length = bitmapListFile.list().length;
		String fileName = dirName + "/" ;
		try {
			for(int i = 0 ; i < length; i ++)
			{				
				btmList.add(getBitmap(fileName+i));				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		System.out.println("getBitmapArrayList()--------btmList===" + btmList);
		return btmList;
	}
	
	
	/**
	 * 删除文件
	 * 要想直接删除根目录，将文件名写成""即可。
	 * */
	public void deleteSDFile(String fileName)
	{
		File file = new File(directoryPath + fileName);  
		deleteFile(file);
	}
	//删除文件函数，里面用到递归调用了。
	private static void  deleteFile(File file)
	{				
		if(file.exists())
		{
			if(file.isFile())			
				file.delete();						
			else if(file.isDirectory())// 如果它是一个目录
			{
				// 声明目录下所有的文件 files[];
				File files[] = file.listFiles();
				for(int i = 0; i < files.length; i++)// 遍历目录下所有的文件
					deleteFile(files[i]); // 把每个文件 用这个方法进行迭代				
			}
			
			file.delete();
		}		
	}
}
