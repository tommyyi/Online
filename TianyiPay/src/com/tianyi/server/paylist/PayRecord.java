package com.tianyi.server.paylist;

import org.json.JSONException;
import org.json.JSONObject;

public class PayRecord
{
	public String orderid;
	public String amount;
	public String app_id;
	public String channel_id;
	public String uid;
	public String server_name;
	public String player_name;
	public String product_name;
	public String payment_support;
	public String payment_type;
	public String payment_amount;
	public String payment_status;
	public String payment_data;
	public String shipping_time;
	public String shipping_status;
	public String status;
	
	public PayRecord(String result)
	{
		try
		{
			JSONObject jsonObject = new JSONObject(result);
			orderid = jsonObject.getString("order_code");
			amount = jsonObject.getString("amount");
			app_id = jsonObject.getString("game_id");
			channel_id = jsonObject.getString("package_id");
			uid = jsonObject.getString("uid");
			server_name = jsonObject.getString("server_name");
			player_name = jsonObject.getString("player_name");
			product_name = jsonObject.getString("product_name");
			payment_support = jsonObject.getString("payment_support");
			payment_type = jsonObject.getString("payment_type");
			payment_amount = jsonObject.getString("payment_amount");
			payment_status = jsonObject.getString("payment_status");
			shipping_time = jsonObject.getString("shipping_time");
			shipping_status = jsonObject.getString("shipping_status");
			status = jsonObject.getString("status");
			payment_data = jsonObject.getString("payment_data");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			orderid = null;
			return;
		}
	}
}