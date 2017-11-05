/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 */

package com.tianyi.alipay;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * 模拟商户应用的商品列表，交易步骤。
 * 
 * 1.  将商户ID，收款帐号，外部订单号，商品名称，商品介绍，价格，通知地址封装成订单信息 
 * 2. 对订单信息进行签名 
 * 3. 将订单信息，签名，签名方式封装成请求参数 4. 调用pay方法
 * 
 * @version v4_0413 2012-03-02
 */
public class TestEntrance extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		String out_trade_no = getOutTradeNo();
		String subject = "新款耐克";
		String body = "2010新款NIKE 耐克902第三代板鞋 耐克男女鞋 386201 白红";
		String total_fee = "0.01";
		
		TianyiAlipayCallback tianyiAlipayCallback = new TianyiAlipayCallback() {
			
			@Override
			public void handSuccess(String orderid)
			{
				int ok = 1;
				ok++;
				Log.e("payment result", "success, orderid="+orderid);
			}
			
			@Override
			public void handFail(String orderid)
			{
				int ok = 0;
				ok--;
				Log.e("payment result", "fail, orderid="+orderid);
			}
		};
		TianyiAlipay tianyiAlipay = new TianyiAlipay(this, tianyiAlipayCallback);
		tianyiAlipay.QuickPayment(out_trade_no, subject, body, total_fee);
		
		/*try
		{
			ddleAlipay.QuickPayment(getDDleOrderInfo().getString("orderinfo"));
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}*/
	}

	/*private JSONObject getDDleOrderInfo() throws JSONException
	{
		JSONObject jsonObject = new JSONObject();
		jsonObject
				.put("orderinfo",
						"partner=\"2088111008275155\"&seller=\"ddlegame@ddle.cn\"&out_trade_no=\"5333a15a1c127\"&subject=\"研发部-测试专用号\"&body=\"研发部-测试专用号\"&total_fee=\"0.01\"&notify_url=\"http://user.ddle.cn/appnotifynew.php\"&service=\"mobile.securitypay.pay\"&_input_charset=\"UTF-8\"");
		return jsonObject;
	}*/

	/**
	 * get the out_trade_no for an order. 获取外部订单号
	 * 
	 * @return
	 */
	String getOutTradeNo()
	{
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");
		Date date = new Date();
		String strKey = format.format(date);

		java.util.Random r = new java.util.Random();
		strKey = strKey + r.nextInt();
		strKey = strKey.substring(0, 15);
		return strKey;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		this.finish();
	}
}