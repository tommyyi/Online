package com.tianyi.alipay;

import java.io.IOException;
import java.io.InputStream;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;

public class AssetPic {
	
	static String  filename_pic = "ddlesdk/alipay/";
	public static Drawable loadDrawable(Context mContext,String filename){

		Drawable d =null;
		try {
			d = Drawable.createFromStream(mContext.getAssets().open(filename_pic+filename), filename);
			if (d instanceof BitmapDrawable) {
				((BitmapDrawable)d).setTargetDensity(mContext.getResources().getDisplayMetrics());
			} else if (d instanceof NinePatchDrawable) {
				((NinePatchDrawable)d).setTargetDensity(mContext.getResources().getDisplayMetrics());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		 return d ;
	}
	
//	private static int b;
//
	public static Bitmap getBitmap(Context mContext, String path) {

		try {
			InputStream inputStream = mContext.getAssets().open(filename_pic+path);
			Bitmap localBitmap = BitmapFactory.decodeStream(inputStream);
			return localBitmap;
		} catch (IOException localIOException) {
		}
		return Bitmap.createBitmap(480, 50, Bitmap.Config.RGB_565);
	}
//
//	public static Drawable getDrawable(Context mContext, String path) {
//		Bitmap bitmap = getBitmap(mContext, path);
//		return dr(mContext, bitmap);
//	}
//
//	public static Drawable dr(Context mContext, Bitmap bitmap) {
//		if (b == 0) {
//			DisplayMetrics localObject = new DisplayMetrics();
//			WindowManager localWindowManager = (WindowManager) mContext
//					.getSystemService("window");
//			localWindowManager.getDefaultDisplay().getMetrics(
//					(DisplayMetrics) localObject);
//			b = ((DisplayMetrics) localObject).densityDpi;
//		}
//		Object localObject = new BitmapDrawable(bitmap);
//		((BitmapDrawable) localObject)
//				.setTargetDensity((int) (b * (b * 1.0F / 240.0F)));
//		return (Drawable) localObject;
//	}
	/**
	 * 从Assets中读取布局文件,用于传入inflate
	 * @param mContext
	 * @param fileName 布局文件名称,必须带后缀名
	 * @return
	 * @throws IOException
	 */
	public static XmlResourceParser getXMLFromAsset(Context mContext,String fileName){

		XmlResourceParser parser = null;
		try {
			parser = mContext.getAssets().openXmlResourceParser("assets/"+filename_pic+fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return parser;
	}
}
