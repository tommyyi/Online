package com.tianyi.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.tianyi.server.TianyiPay;
import com.tianyi.tool.data.Tool;
import com.tianyi.ui.UIResource;
import com.tianyi.userdb.UserInfo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class UsernameSelectDialog extends Dialog
{
	Activity										context;
	private ArrayList<HashMap<String, Object>>	listviewItems;
	private UsernameListViewAdapter				listviewItemAdapter;
	private ListView							listView;
	private LayoutInflater						inflater;
	private EditText	usernameEdittext;
	private EditText	passwordEdittext;
	private List<UserInfo>	plist;

	public UsernameSelectDialog(Activity context)
	{
		super(context);
		this.context = context;
	}

	public UsernameSelectDialog(Activity context, int theme)
	{
		super(context, theme);
		this.context = context;
	}
	
	public UsernameSelectDialog(Activity context, int theme, TextView areaTextview, int dialogType, List<String> areaList, List<Boolean> isSelectedList)
	{
		super(context, theme);
		this.context = context;
	}

	public UsernameSelectDialog(Activity context, int theme, EditText usernameEdittext, EditText passwordEdittext, List<UserInfo>	plist)
	{
		super(context, theme);
		this.context = context;
		this.usernameEdittext=usernameEdittext;
		this.passwordEdittext=passwordEdittext;
		this.plist=plist;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(UIResource.getLayoutResIDByName(context, "tianyi_account_select_dialog"), null);
		setContentView(view);
		
		listView = (ListView) view.findViewById(UIResource.getIdResIDByName(context, "listview"));
		
		listviewItems = new ArrayList<HashMap<String, Object>>();
		listviewItemAdapter = new UsernameListViewAdapter(context, listviewItems,
				UIResource.getLayoutResIDByName(context, "tianyi_account_item"), new String[] { "username"},
				new int[] { UIResource.getIdResIDByName(context, "username")}, plist);
		listView.setAdapter(listviewItemAdapter);
		listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				usernameEdittext.setText(plist.get(arg2).getUsername());
				passwordEdittext.setText(plist.get(arg2).getPassword());
				dismiss();
			}
		});
		
		ImageView goback = (ImageView)view.findViewById(UIResource.getIdResIDByName(context, "goback"));
		goback.setOnClickListener(getCancelOnclick());
	}
	
	private android.view.View.OnClickListener getCancelOnclick()
	{
		android.view.View.OnClickListener onclick = new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				dismiss();
			}
		};
		return onclick;
	}
}