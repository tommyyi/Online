package com.tianyi.tool.ui;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.tianyi.server.paylist.PayRecord;
import com.tianyi.ui.UIResource;

public class PayListAdapter extends SimpleAdapter
{
	private LayoutInflater inflater;
	private List<PayRecord> payRecordlist;
	private int resource;
	private Context context;
	
	public PayListAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to, List<PayRecord> payRecordlist)
	{
		super(context, data, resource, from, to);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
        this.resource = resource;
        this.payRecordlist=payRecordlist;
        this.context=context;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		if(convertView == null)
			convertView = inflater.inflate(resource, parent, false);

		TextView orderId_textView = (TextView)convertView.findViewById(UIResource.getIdResIDByName(context,"orderid"));
		orderId_textView.setText(payRecordlist.get(position).orderid);

		TextView payment_type_textView = (TextView)convertView.findViewById(UIResource.getIdResIDByName(context,"payment_type"));
		if(payRecordlist.get(position).payment_type.equals("shengpay"))
			payment_type_textView.setText(context.getString(UIResource.getStringResIDByName(context, "tianyi_payment_type_shengpay")));
		if(payRecordlist.get(position).payment_type.equals("unionpay"))
			payment_type_textView.setText(context.getString(UIResource.getStringResIDByName(context, "tianyi_payment_type_unionpay")));
		if(payRecordlist.get(position).payment_type.equals("alipayquick"))
			payment_type_textView.setText(context.getString(UIResource.getStringResIDByName(context, "tianyi_payment_type_alipayquick")));
		
		TextView product_textView = (TextView)convertView.findViewById(UIResource.getIdResIDByName(context,"product"));
		product_textView.setText(payRecordlist.get(position).product_name);

		TextView amount_textView = (TextView)convertView.findViewById(UIResource.getIdResIDByName(context,"amount"));
		amount_textView.setText(payRecordlist.get(position).amount);
		
		return convertView;
	}
}