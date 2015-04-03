package lib;

import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.nineoldandroids.view.ViewHelper;
import com.smarttiger.gpsimformation.R;

public class SlidingMenu extends HorizontalScrollView
{
	/**
	 * 屏幕宽度
	 */
	private int mScreenWidth;
	/**
	 * dp
	 */
	private int mMenuRightPadding;
	/**
	 * 菜单的宽度
	 */
	private int mMenuWidth;
	private int mHalfMenuWidth;

	private boolean isOpen;

	private boolean once;

	private Context context;
	private ViewGroup mMenu;
	private ViewGroup mContent;

	public SlidingMenu(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public SlidingMenu(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		this.context = context;
		mScreenWidth = ScreenUtils.getScreenWidth(context);

		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.SlidingMenu, defStyle, 0);
		int n = a.getIndexCount();
		for (int i = 0; i < n; i++)
		{
			int attr = a.getIndex(i);
			switch (attr)
			{
			case R.styleable.SlidingMenu_rightPadding:
				// 默认50
				mMenuRightPadding = a.getDimensionPixelSize(attr,
						(int) TypedValue.applyDimension(
								TypedValue.COMPLEX_UNIT_DIP, 50f,
								getResources().getDisplayMetrics()));// 默认为10DP
				break;
			}
		}
		a.recycle();
	}

	public SlidingMenu(Context context)
	{
		this(context, null, 0);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		/**
		 * 显示的设置一个宽度
		 */
		if (!once)
		{
			LinearLayout wrapper = (LinearLayout) getChildAt(0);
			mMenu = (ViewGroup) wrapper.getChildAt(0);
			mContent = (ViewGroup) wrapper.getChildAt(1);

			mMenuWidth = mScreenWidth - mMenuRightPadding;
			mHalfMenuWidth = mMenuWidth / 2;
			mMenu.getLayoutParams().width = mMenuWidth;
			mContent.getLayoutParams().width = mScreenWidth;

		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{
		super.onLayout(changed, l, t, r, b);
		if (changed)
		{
			// 将菜单隐藏
			this.scrollTo(mMenuWidth, 0);
			once = true;
		}
	}
	
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		//复写这个函数，来获取触摸初始值的原因是，onTouchEvent的ACTION_DOWN被子控件截取了，无法获取。而这个函数先于子控件调用。
		int action = event.getAction();
		if(action == MotionEvent.ACTION_DOWN)
		{
			System.out.println("onInterceptTouchEvent()----按下事件------DOWN");
			x = event.getX(); 
			y = event.getY();
			bool = true;
		}
		return super.onInterceptTouchEvent(event);
	}
	
	float x ,mx;
	float y ,my;///添加y的判断是为了防止用户在上下滑动邮件时不小心跳页.
	boolean bool = true;///防止move里的方法被触发多次,将方法写入move是因为在move中反应比在up中反应快..
	boolean openMenu = false;
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		int action = event.getAction();
		switch (action)
		{
		case MotionEvent.ACTION_MOVE:
			mx = event.getX();
			my = event.getY();
			System.out.println("移动事件");
			if(Math.abs(my-y)<20 )
				if((mx-x)>20)
				{
					System.out.println("++++++++++++++++++开");
					bool=false;
//					openMenu();
					openMenu = true;
				}
				else if ((x-mx)>10)
				{
					System.out.println("-------------------关");
					bool=false;
//					closeMenu();
					openMenu = false;
				}
				else if((mx-x)>0 && (mx-x)<20)
				{
					bool=false;
					openMenu = false;
				}
				else
					openMenu = true;
			break;				
		
		// Up时，进行判断，如果显示区域大于菜单宽度一半的则完全显示，否则隐藏
		case MotionEvent.ACTION_UP:
			System.out.println("抬起事件------UP");
			if(openMenu)
			{
				this.smoothScrollTo(0, 0);
				isOpen = true;
			}
			else
			{
				this.smoothScrollTo(mMenuWidth, 0);
				isOpen = false;
			}
			return true;
//			int scrollX = getScrollX();
//			if (scrollX > mHalfMenuWidth*1.5)
//			{
//				this.smoothScrollTo(mMenuWidth, 0);
//				isOpen = false;
//			} else
//			{
//				this.smoothScrollTo(0, 0);
//				isOpen = true;
//			}
//			return true;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 打开菜单
	 */
	public void openMenu()
	{
		if (isOpen)
			return;
		this.smoothScrollTo(0, 0);
		isOpen = true;
	}

	/**
	 * 关闭菜单
	 */
	public void closeMenu()
	{
		if (isOpen)
		{
			this.smoothScrollTo(mMenuWidth, 0);
			isOpen = false;
		}
	}

	/**
	 * 切换菜单状态
	 */
	public void toggle()
	{
		if (isOpen)
		{
			closeMenu();
		} else
		{
			openMenu();
		}
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt)
	{
		super.onScrollChanged(l, t, oldl, oldt);
		float scale = l * 1.0f / mMenuWidth;
		float leftScale = 1 - 0.3f * scale;
		float rightScale = 0.8f + scale * 0.2f;
		
		ViewHelper.setScaleX(mMenu, leftScale);
		ViewHelper.setScaleY(mMenu, leftScale);
		ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));
		ViewHelper.setTranslationX(mMenu, mMenuWidth * scale * 0.7f);

		ViewHelper.setPivotX(mContent, 0);
		ViewHelper.setPivotY(mContent, mContent.getHeight() / 2);
		ViewHelper.setScaleX(mContent, rightScale);
		ViewHelper.setScaleY(mContent, rightScale);

	}
	

	//如果已显示菜单，则点击返回键后，应关闭菜单，并返回true，否则返回false，父类就可以根据这个返回值来是否调用结束程序。
	public Boolean keyDown()
	{
		if(isOpen)
		{
			this.smoothScrollTo(mMenuWidth, 0);
			isOpen = false;
			return true;
		}
		else
			return false;
	}
}
