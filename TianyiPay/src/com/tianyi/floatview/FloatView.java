package com.tianyi.floatview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tianyi.server.TianyiPay;
import com.tianyi.ui.UIResource;

public class FloatView extends View
{
	private Activity activity;
	private Handler handler;
	private DisplayMetrics displayMetrics;
	private Drawable drawableMiddle,drawableRight,drawableLeft;
	private int firstX, firstY,lastX, lastY;
	private View floatViewLayout;
	private ImageView imageview;
	
	private LinearLayout banner_right;
	private LinearLayout forum_right;
	private LinearLayout csc_right;
	private LinearLayout account_right;
	private LinearLayout close_right;
	
	private LinearLayout banner_left;
	private LinearLayout forum_left;
	private LinearLayout csc_left;
	private LinearLayout account_left;
	private LinearLayout close_left;
	
	private int mPreviousPosition_x;
	private int mPreviousPosition_y;
	private boolean isViewadded = false;
	private OnTouchListener mTouchListener = new OnTouchListener()
	{
		public boolean onTouch(View v, MotionEvent event)
		{
			final int action = event.getAction();

			if(action == MotionEvent.ACTION_DOWN)
			{
				mPreviousPosition_x = mWMParams.x;
				mPreviousPosition_y = mWMParams.y;
				firstX = (int)event.getRawX();
				firstY = (int)event.getRawY();
				lastX = (int)event.getRawX();
				lastY = (int)event.getRawY();
			}
			else if(action == MotionEvent.ACTION_MOVE)
			{
				lastX = (int) event.getRawX();
				lastY = (int) event.getRawY();
				
				int totalDeltaX = lastX - firstX;
				int totalDeltaY = lastY - firstY;
				
				if(Math.abs(totalDeltaX)>=10||Math.abs(totalDeltaY)>=10)
				{
					mWMParams.x = mPreviousPosition_x+totalDeltaX;
					mWMParams.y = mPreviousPosition_y+ totalDeltaY;
					imageview.setImageDrawable(drawableMiddle);
					banner_left.setVisibility(View.GONE);
					banner_right.setVisibility(View.GONE);
					mWManager.updateViewLayout(floatViewLayout, mWMParams);
				}
			}
			else if(action == MotionEvent.ACTION_UP)
			{
				if(isClick(firstX,firstY,lastX,lastY))
				{
					mWMParams.x = mPreviousPosition_x;
					mWMParams.y = mPreviousPosition_y;
					if(mWMParams.x>0)
					{
						imageview.setImageDrawable(drawableRight);
						if(banner_right.getVisibility()==View.GONE)
							banner_right.setVisibility(View.VISIBLE);
						else
							banner_right.setVisibility(View.GONE);
					}
					else
					{
						imageview.setImageDrawable(drawableLeft);
						if(banner_left.getVisibility()==View.GONE)
							banner_left.setVisibility(View.VISIBLE);
						else
							banner_left.setVisibility(View.GONE);
					}
					mWManager.updateViewLayout(floatViewLayout, mWMParams);
				}
				else
				{
					if(mWMParams.x>0)
					{
						imageview.setImageDrawable(drawableRight);
						mWMParams.x = displayMetrics.widthPixels/2;
						mWManager.updateViewLayout(floatViewLayout, mWMParams);
					}
					else
					{
						imageview.setImageDrawable(drawableLeft);
						mWMParams.x = -displayMetrics.widthPixels/2;
						mWManager.updateViewLayout(floatViewLayout, mWMParams);
					}
				}
				firstX = firstY = lastX = lastY = 0;
			}
			return true;
		}
	};
	private WindowManager mWManager;
	
	private WindowManager.LayoutParams mWMParams = null;
	public FloatView(Activity activity)
	{
		super(activity);
		this.activity = activity;
		this.handler=new Handler();
		mWManager = (WindowManager) activity.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		displayMetrics = new DisplayMetrics();
		mWManager.getDefaultDisplay().getMetrics(displayMetrics);
		
		floatViewLayout = LayoutInflater.from(activity).inflate(UIResource.getLayoutResIDByName(activity, "tianyi_floatviewlayout"),null);
		floatViewLayout.setBackgroundColor(Color.TRANSPARENT);
		imageview = (ImageView) floatViewLayout.findViewById(UIResource.getIdResIDByName(activity, "floatimage"));
		initiateAnchor();
		
		floatViewLayout.setOnTouchListener(mTouchListener);
		
		drawableMiddle = activity.getResources().getDrawable(UIResource.getDrawableResIDByName(activity, "tianyi_floaticon_middle"));
		drawableRight = activity.getResources().getDrawable(UIResource.getDrawableResIDByName(activity, "tianyi_floaticon_right"));
		drawableLeft = activity.getResources().getDrawable(UIResource.getDrawableResIDByName(activity, "tianyi_floaticon_left"));
		
		mWMParams = new WindowManager.LayoutParams();
		mWMParams.type = 2003; 
		mWMParams.flags = 40;
		mWMParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mWMParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mWMParams.format = -3; 
		if(getOrientation(activity)==ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
		mWMParams.x = -displayMetrics.widthPixels/2;
		else
			mWMParams.x = -displayMetrics.heightPixels/2;
		imageview.setAlpha(128);
	}

	private static int getOrientation(Context context)
	{
		Display display = ((WindowManager) context	.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int orientation = display.getOrientation();
		return orientation;
	}
	
	private void openUrlActivity(Activity activity, String url)
	{
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setClass(activity, WebViewActivity.class);
		intent.putExtra("url", url);
		intent.putExtra("orientation", activity.getRequestedOrientation());
		activity.startActivity(intent );
	}
	
	private void initiateAnchor()
	{
		banner_left = (LinearLayout)floatViewLayout.findViewById(UIResource.getIdResIDByName(activity, "banner_left"));
		forum_left = (LinearLayout)floatViewLayout.findViewById(UIResource.getIdResIDByName(activity, "forum_left"));
		forum_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0)
			{
				openUrlActivity(activity, activity.getString(UIResource.getStringResIDByName(activity, "tianyi_forumaddress")));
			}
		});
		csc_left = (LinearLayout)floatViewLayout.findViewById(UIResource.getIdResIDByName(activity, "csc_left"));
		csc_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0)
			{
				Intent intent = new Intent();
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setClass(activity, CustomerActivity.class);
				intent.putExtra("orientation", activity.getRequestedOrientation());
				activity.startActivity(intent );
			}
		});
		account_left = (LinearLayout)floatViewLayout.findViewById(UIResource.getIdResIDByName(activity, "account_left"));
		account_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0)
			{
				TianyiPay.tianyiPay.startUsercenter();
				banner_left.setVisibility(View.GONE);
			}
		});
		close_left = (LinearLayout)floatViewLayout.findViewById(UIResource.getIdResIDByName(activity, "close_left"));
		close_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0)
			{
				closeFloatview();
			}
		});
		
		banner_right = (LinearLayout)floatViewLayout.findViewById(UIResource.getIdResIDByName(activity, "banner_right"));
		forum_right = (LinearLayout)floatViewLayout.findViewById(UIResource.getIdResIDByName(activity, "forum_right"));
		forum_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0)
			{
				openUrlActivity(activity, activity.getString(UIResource.getStringResIDByName(activity, "tianyi_forumaddress")));
			}
		});
		csc_right = (LinearLayout)floatViewLayout.findViewById(UIResource.getIdResIDByName(activity, "csc_right"));
		csc_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0)
			{
				Intent intent = new Intent();
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setClass(activity, CustomerActivity.class);
				intent.putExtra("orientation", activity.getRequestedOrientation());
				activity.startActivity(intent );
			}
		});
		account_right = (LinearLayout)floatViewLayout.findViewById(UIResource.getIdResIDByName(activity, "account_right"));
		account_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0)
			{
				TianyiPay.tianyiPay.startUsercenter();
				banner_right.setVisibility(View.GONE);
			}
		});
		close_right = (LinearLayout)floatViewLayout.findViewById(UIResource.getIdResIDByName(activity, "close_right"));
		close_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0)
			{
				closeFloatview();
			}
		});
		
		banner_left.setVisibility(View.GONE);
		banner_right.setVisibility(View.GONE);
	}

	public void closeFloatview()
	{
		if(isViewadded)
		{
			banner_right.setVisibility(View.GONE);
			banner_left.setVisibility(View.GONE);
			mWManager.removeView(floatViewLayout);
		}
		isViewadded = false;
	}
	
	private boolean  isClick(int firstX, int firstY, int lastX, int lastY)
	{
		float xDifference = Math.abs(firstX - lastX);
		float yDifference = Math.abs(firstY - lastY);
		double difference = Math.sqrt(Math.pow(xDifference, 2)
				+ Math.pow(yDifference, 2));
		if(difference - 10 > 0)
		{
			Log.i("distance", "distance "+(difference - 10));
			return false;
		}
		return true;
	}
	
	private void showFloatview()
	{
		if(!isViewadded)
			mWManager.addView(floatViewLayout, mWMParams);
		isViewadded = true;
	}
	
	private static FloatView floatView = null;
	public static void show(final Activity activity)
	{
		try
		{
			if(floatView==null)
				floatView = new FloatView(activity);
			
			floatView.handler.post(new Runnable() {
				@Override
				public void run()
				{
					floatView.showFloatview();
				}
			});
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void close()
	{
		try
		{
			if(floatView!=null)
			{
				floatView.handler.post(new Runnable() {
					@Override
					public void run()
					{
						floatView.closeFloatview();
					}
				});
			}
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}