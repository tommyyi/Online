package com.tianyi.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.tianyi.server.paylist.PayListGetter;
import com.tianyi.server.paylist.PayRecord;
import com.tianyi.tool.ui.PayListAdapter;

public class TianyiPaymentHistoryActivity extends Activity implements OnScrollListener
{
	private ImageView	goback;
	private ArrayList<HashMap<String, Object>>	listItem;
	private PayListAdapter	listItemAdapter;
	private List<PayRecord>	paymentList;
	private List<PayRecord>	added_paymentList;
	private ListView recordListView;
	private LinearLayout morePayRecord;
	private LinearLayout paylist_listview_l;
	private int	lastbottom;
	public boolean	hasmore = true;
	public Handler handler;
	private boolean	thread2getnewbookisrunning = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(UIResource.getLayoutResIDByName(getApplicationContext(),"tianyi_payment_history"));
		setOnclickGoback();
		
		morePayRecord = (LinearLayout)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"tianyi_more_pay_record"));
		recordListView = (ListView) findViewById(UIResource.getIdResIDByName(getApplicationContext(),"payment_listview"));
		recordListView.setOnScrollListener(this);
		paylist_listview_l = (LinearLayout)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"paylist_listview_l"));
		
		paymentList = new ArrayList<PayRecord>();
		added_paymentList = new ArrayList<PayRecord>();
		handler = new Handler();
		
		listItem = new ArrayList<HashMap<String, Object>>();
		listItemAdapter = new PayListAdapter(this, listItem,
				UIResource.getLayoutResIDByName(getApplicationContext(),"tianyi_pay_record_item"),
				new String[]
				{ "OrderId", "Payment_type", "Product", "Amount" },
				new int[]
				{ UIResource.getIdResIDByName(getApplicationContext(),"orderid"), UIResource.getIdResIDByName(getApplicationContext(),"payment_type"), UIResource.getIdResIDByName(getApplicationContext(),"product"), UIResource.getIdResIDByName(getApplicationContext(),"amount") }, paymentList);
		recordListView.setAdapter(listItemAdapter);
		
		morePayRecord.setVisibility(View.VISIBLE);
		if(thread2getnewbookisrunning ==false)
		{
			thread2getnewbookisrunning = true;
			MyThread myThread = new MyThread();
			myThread.start();
		}
	}
	
	private void setOnclickGoback()
	{
		goback = (ImageView)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"goback"));
		goback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
	}

	private class MyThread extends Thread
	{
		@Override
		public void run()
		{
			if(hasmore )
			{
				added_paymentList = PayListGetter.getPaylist(getApplicationContext(), paymentList.size()+"");
				
				if(added_paymentList!=null&&added_paymentList.size()!=0)
				{
					handler.post(new Runnable() {
						@Override
						public void run()
						{
							paymentList.addAll(added_paymentList);

							int length = added_paymentList.size();
							for (int index = 0; index < length; index++)
							{
								HashMap<String, Object> map = new HashMap<String, Object>();
								map.put("OrderId", added_paymentList.get(index).orderid);
								map.put("Payment_type", added_paymentList.get(index).payment_type);
								map.put("Product", added_paymentList.get(index).product_name);
								map.put("Amount", added_paymentList.get(index).amount);
								listItem.add(map);
							}
							
							listItemAdapter.notifyDataSetChanged();
							morePayRecord.setVisibility(View.GONE);
							paylist_listview_l.setVisibility(View.VISIBLE);
						}
					});
				}
				else
				{
					handler.post(new Runnable() {
						@Override
						public void run()
						{
							morePayRecord.setVisibility(View.GONE);
						}
					});
					hasmore=false;
				}
			}
			
			thread2getnewbookisrunning  = false;
			super.run();
		}
	}
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount)
	{
		if(lastbottom==(firstVisibleItem + visibleItemCount))
			return;
		else
			lastbottom  = firstVisibleItem + visibleItemCount;
		
		if((firstVisibleItem + visibleItemCount == totalItemCount)
				&& (totalItemCount != 0)&&hasmore)
		{
			if(thread2getnewbookisrunning ==false)
			{
				morePayRecord.setVisibility(View.VISIBLE);
				thread2getnewbookisrunning = true;
				MyThread myThread = new MyThread();
				myThread.start();
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1)
	{
	}
}