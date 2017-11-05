package com.tianyi.floatview;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class WebViewClient_fullscreen extends WebViewClient
{
	private ProgressBar progressBar;
	private WebViewActivity activity;
	public String adidOfcurrentPage;
	
	public WebViewClient_fullscreen(WebViewActivity activity)
	{
		super();
		this.progressBar = activity.getProgressBar();
		this.activity = activity;
	}

	@Override
	public void onPageFinished(WebView view, String url)
	{
		if(progressBar.getVisibility() == View.VISIBLE)
			progressBar.setVisibility(View.GONE);
		super.onPageFinished(view, url);
	}
}