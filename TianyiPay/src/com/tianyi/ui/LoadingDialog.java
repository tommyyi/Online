package com.tianyi.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.tianyi.ui.LoadingImageView.SizeChangeListener;

public class LoadingDialog extends Dialog
{
	Context										context;
	private LayoutInflater						inflater;
	private LoadingImageView	loadingmotionImageview;
	private View	view;
	private String message;
	private Handler handler;
	
	public Handler getHandler()
	{
		return handler;
	}

	public void setMessage(String message)
	{
		this.message = message;
		messageTextview.setText(this.message);
	}

	private boolean isViewOk = false;
	private TextView	messageTextview;
	public LoadingDialog(Context context, Handler handler)
	{
		super(context);
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(UIResource.getLayoutResIDByName(context,"tianyi_loading_dialog"), null);
		setCancelable(false);
		loadingmotionImageview = (LoadingImageView)view.findViewById(UIResource.getIdResIDByName(context,"loadingmotion"));
		messageTextview = (TextView)view.findViewById(UIResource.getIdResIDByName(context,"loading_message"));
		this.handler = handler;
	}

	public LoadingDialog(Context context, int theme, Handler handler)
	{
		super(context, theme);
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(UIResource.getLayoutResIDByName(context,"tianyi_loading_dialog"), null);
		setCancelable(false);
		loadingmotionImageview = (LoadingImageView)view.findViewById(UIResource.getIdResIDByName(context,"loadingmotion"));
		messageTextview = (TextView)view.findViewById(UIResource.getIdResIDByName(context,"loading_message"));
		this.handler = handler;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(view);
	}

	public void show(final String message)
	{
		try
		{
			handler.post(new Runnable() {
				
				@Override
				public void run()
				{
					setMessage(message);
					if(!isViewOk)
						loadingmotionImageview.setSizeChangeListener(new SizeChangeListener() {
							
							@Override
							public void sizeChanged(int w, int h, int oldw, int oldh)
							{
								isViewOk=true;
								float width = loadingmotionImageview.getWidth();
								float height = loadingmotionImageview.getWidth();
								RotateAnimation rotateAnimation = new RotateAnimation(0, 360, width/2,height/2);
								rotateAnimation.setDuration(2000);
								rotateAnimation.setRepeatCount(1000);
								rotateAnimation.setRepeatMode(Animation.RESTART);
								rotateAnimation.setFillAfter(true);
								loadingmotionImageview.startAnimation(rotateAnimation);
							}
						});
					else
					{
						float width = loadingmotionImageview.getWidth();
						float height = loadingmotionImageview.getWidth();
						RotateAnimation rotateAnimation = new RotateAnimation(0, 360, width/2,height/2);
						rotateAnimation.setDuration(2000);
						rotateAnimation.setRepeatCount(1000);
						rotateAnimation.setRepeatMode(Animation.RESTART);
						rotateAnimation.setFillAfter(true);
						loadingmotionImageview.startAnimation(rotateAnimation);
					}
					LoadingDialog.super.show();
				}
			});
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void dismiss()
	{
		handler.post(new Runnable() {
			
			@Override
			public void run()
			{
				try
				{
					LoadingDialog.super.dismiss();
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
}