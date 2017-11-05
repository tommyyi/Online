package com.tianyi.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.tianyi.server.TianyiPay;
import com.tianyi.server.TianyiPaySDK;
import com.tianyi.tool.data.Tool;
import com.tianyi.ui.LoadingImageView.SizeChangeListener;

public class AutomaticLoginningDialog extends Dialog
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
		messageTextview.setText(message);
	}

	public String getMessage()
	{
		return message;
	}

	private boolean isViewOk = false;
	private TextView	messageTextview;
	private Button switchButton;
	protected boolean	isButtonPressed = false;
	
	public AutomaticLoginningDialog(Context context, Handler handler)
	{
		super(context);
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(UIResource.getLayoutResIDByName(context,"tianyi_automaticlogin_dialog"), null);
		setCancelable(false);
		loadingmotionImageview = (LoadingImageView)view.findViewById(UIResource.getIdResIDByName(context,"loadingmotion"));
		messageTextview = (TextView)view.findViewById(UIResource.getIdResIDByName(context,"loginning_message"));
		switchButton = (Button)view.findViewById(UIResource.getIdResIDByName(context,"tianyi_change_account_btn"));
		this.handler = handler;
		message=messageTextview.getText().toString();
	}

	public AutomaticLoginningDialog(Context context, int theme, Handler handler)
	{
		super(context, theme);
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(UIResource.getLayoutResIDByName(context,"tianyi_automaticlogin_dialog"), null);
		setCancelable(false);
		loadingmotionImageview = (LoadingImageView)view.findViewById(UIResource.getIdResIDByName(context,"loadingmotion"));
		messageTextview = (TextView)view.findViewById(UIResource.getIdResIDByName(context,"loginning_message"));
		switchButton = (Button)view.findViewById(UIResource.getIdResIDByName(context,"tianyi_change_account_btn"));
		this.handler = handler;
		message=messageTextview.getText().toString();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(view);
		switchButton.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View arg0)
			{
				isButtonPressed=true;
				TianyiPay.tianyiPay.loginByActivity();
				dismiss();
			}
		});
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
					AutomaticLoginningDialog.super.show();
				}
			});
			
			new Thread(){
				@Override
				public void run()
				{
					Tool.sleep(3000);
					if(!isButtonPressed)
					{
						handler.post(new Runnable() {
							@Override
							public void run()
							{
								dismiss();
							}
						});
						TianyiPaySDK.autoLogin();
					}
					super.run();
				}
			}.start();
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
					AutomaticLoginningDialog.super.dismiss();
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