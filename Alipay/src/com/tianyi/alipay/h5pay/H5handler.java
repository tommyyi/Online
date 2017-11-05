package com.tianyi.alipay.h5pay;

import com.tianyi.alipay.TianyiAlipay;
import com.tianyi.alipay.TianyiAlipayCallback;

import android.app.Activity;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;
import android.os.Handler;

public class H5handler extends Handler
{
	public Activity activity;
	public TianyiAlipay tianyiAlipay;
	
	public H5handler(Activity activity, TianyiAlipay tianyiAlipay)
	{
		super();
		this.activity = activity;
		this.tianyiAlipay=tianyiAlipay;
	}
	
	public void handleMessage(Message msg) 
	{
		switch (msg.what) 
		{
			case H5PayTool.SDK_PAY_FLAG: 
			{
				PayResult payResult = new PayResult((String) msg.obj);
				
				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();
				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) 
				{
					Toast.makeText(activity, "支付成功",Toast.LENGTH_SHORT).show();
					tianyiAlipay.tianyiAlipayCallback.handSuccess(tianyiAlipay.orderid);
				} 
				else 
				{
					tianyiAlipay.tianyiAlipayCallback.handFail(tianyiAlipay.orderid);
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) 
					{
						Toast.makeText(activity, "支付结果确认中",Toast.LENGTH_SHORT).show();
					} 
					else 
					{
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(activity, "支付失败",Toast.LENGTH_SHORT).show();
					}
				}
				break;
			}
			case H5PayTool.SDK_CHECK_FLAG: 
			{
				Toast.makeText(activity, "检查结果为：" + msg.obj,Toast.LENGTH_SHORT).show();
				break;
			}
			default:
				break;
		}
	};
}