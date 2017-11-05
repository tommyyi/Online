package com.tianyi.floatview;

import android.R.color;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class WebViewActivity extends Activity
{
	private WebViewActivity activity;
	private ProgressBar progressBar;
	private RelativeLayout relativeLayout;
	private String url;
	private WebView webView;
	private int	orientation;
	public ProgressBar getProgressBar()
	{
		return progressBar;
	}
	
	private void initiateRelativeLayout(RelativeLayout relativeLayout, WebView webView, ProgressBar progressBar)
	{
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams
				(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		((RelativeLayout) relativeLayout).setGravity(Gravity.CENTER);
		((RelativeLayout) relativeLayout).addView(webView,	(ViewGroup.LayoutParams) layoutParams);
		
		layoutParams = new RelativeLayout.LayoutParams
				(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
		((RelativeLayout) relativeLayout).addView(progressBar,	(ViewGroup.LayoutParams) layoutParams);
	}
	
	private void initiateWebView(WebView webView)
	{
		webView.getSettings().setDefaultTextEncodingName("UTF-8") ;
		//webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setSaveFormData(true);
		webView.getSettings().setSavePassword(true);
		webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		webView.getSettings().setSupportZoom(true);
		webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		webView.setHorizontalScrollBarEnabled(false);
		webView.setVerticalScrollBarEnabled(false);
		
		WebViewClient_fullscreen webViewClient_fullscreen = new WebViewClient_fullscreen(activity);
		webView.setWebViewClient(webViewClient_fullscreen);
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		setTheme(android.R.style.Theme_Black_NoTitleBar);
		super.onCreate(savedInstanceState);
		
		activity = this;
		progressBar = new ProgressBar(getApplicationContext(), null,	android.R.attr.progressBarStyleLarge);
		progressBar.setVisibility(View.VISIBLE);
		progressBar.setBackgroundColor(color.background_dark);
		
		Bundle bundle = getIntent().getExtras();
		url = bundle.getString("url");
		//orientation = bundle.getInt("orientation");
		
		webView = new WebView(this);
		initiateWebView(this.webView);
		
		relativeLayout = new RelativeLayout(this);
		initiateRelativeLayout(relativeLayout, webView, progressBar);
		
		//setRequestedOrientation(orientation);
		setContentView((View) relativeLayout);
		
		webView.loadUrl(url);
	}

	@Override
	protected void onDestroy()
	{
		try
		{
			relativeLayout.removeAllViews();
			if(webView!=null)
				webView.destroy();
		}
		catch (Exception e)
		{
		}
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack())
		{
			webView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}