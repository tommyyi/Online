package com.tianyi.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class LoadingImageView extends ImageView
{
	public LoadingImageView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public LoadingImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public LoadingImageView(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
	}

	SizeChangeListener l;

	public void setSizeChangeListener(SizeChangeListener orlExt) {
		l = orlExt;
	}

	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		l.sizeChanged(w, h, oldw, oldh);
		super.onSizeChanged(w, h, oldw, oldh);
	}

	public interface SizeChangeListener {
		public void sizeChanged(int w, int h, int oldw, int oldh);
	}
}
