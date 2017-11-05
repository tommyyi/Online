package com.tianyi.tool;

import java.util.List;
import java.util.Map;

import com.tianyi.ui.UIResource;
import com.tianyi.userdb.UserInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class UsernameListViewAdapter extends SimpleAdapter
{
	private LayoutInflater	inflater;
	private int				itemResourceid;
	private List<UserInfo> plist;
	private Context context;
	public UsernameListViewAdapter(Context context,
			List<? extends Map<String, ?>> listviewItems, int itemResourceid, String[] from,
			int[] to,List<UserInfo> plist)
	{
		super(context, listviewItems, itemResourceid, from, to);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.itemResourceid = itemResourceid;
		this.plist=plist;
		this.context = context;
	}

	@Override
	public int getCount()
	{
		return plist.size();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
			convertView = inflater.inflate(itemResourceid, parent, false);

		if(plist==null||plist.size()==0)
			return convertView;

		TextView username = (TextView) convertView.findViewById(UIResource.getIdResIDByName(context, "username"));
		username.setText(plist.get(position).getUsername());
		
		return convertView;
	}
}