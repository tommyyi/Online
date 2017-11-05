package com.tianyi.alipay.h5pay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.app.Activity;
import android.os.Message;

import com.alipay.sdk.app.PayTask;
import com.tianyi.alipay.PartnerConfig;
import com.tianyi.alipay.TianyiAlipay;
import com.tianyi.alipay.TianyiAlipayCallback;

public class H5PayTool
{

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public static String getSignType() {
		return "sign_type=\"RSA\"";
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public static String sign(String content) {
		return SignUtils.sign(content, PartnerConfig.RSA_PRIVATE);
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	public static String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);
	
		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public static String getOrderInfo(String out_trade_no,String subject, String body, String price) {
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PartnerConfig.PARTNER + "\"";
	
		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + PartnerConfig.SELLER + "\"";
	
		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + out_trade_no + "\"";
	
		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";
	
		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";
	
		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";
	
		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + PartnerConfig.NOTIFYURL/*"http://notify.msp.hk/notify.htm"*/
				+ "\"";
	
		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";
	
		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";
	
		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";
	
		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";
	
		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";
	
		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";
	
		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";
	
		return orderInfo;
	}

	public static void dopay(final Activity activity, TianyiAlipay tianyiAlipay, String out_trade_no,String productname,String productDescribe,String amount)
	{
		final H5handler mHandler = new H5handler(activity,tianyiAlipay);
		
		// 订单
		String orderInfo = getOrderInfo(out_trade_no,productname, productDescribe, amount);
		
		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		try 
		{
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} 
		catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		}
	
		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"	+ getSignType();
	
		Runnable payRunnable = new Runnable() 
		{
			@Override
			public void run() 
			{
				// 构造PayTask 对象
				PayTask alipay = new PayTask(activity);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo);
	
				Message msg = new Message();
				msg.what = H5PayTool.SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};
	
		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	//商户PID
	//public static final String PARTNER = "2088611939528811";
	//商户收款账号
	//public static final String SELLER = "tomy_yi@1997game.com";
	//商户私钥，pkcs8格式
	//public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANbTFi/UkgNFlInEp7l2jv5DwJ87Mh/2/x/jOCvZI6QjnOMAJE2v2mSx5mqyS/2UsTK5sVJ2yRuLTPBrwL3rZI5YSsGbqVFqMXSXWapgJtZPpQbUgFiwFbHM1cd/Ld/m+ulPVPAV11BWJl+9zJimUnaKYpjspEqELUFoPWV5bCwfAgMBAAECgYBQJEV1qIakQ9PrJXsw3ELyJlJ41LuZYyrAOaMd8I60UDjaEpZ4iO8fe6KrEUL6IfuNIes+CWRzKx+T3Y71CxjitoCWhdZFoka6EbcSz+4rMdg6k9rhFmCXsGwxHlbomjpnasWCW63aNBqFxYaeeUOrRtcYPAYuET76DYqkZL7p8QJBAPPGfGm2T5F0b6AZgtgnNjBp3PCUEbIj2HZy+jL/qgV+egSDYwzc28sDQktP4G5yQgrQiFGlpqX2P+BXX8qHv6kCQQDhmPB4u9z5KZ5hBl8EDnYU6bevSBXvBvfD7rTv3q9/V+qYIrK0+sVGDcmQuQKlUJpqX7McU0udqX2FNfJBfYqHAkEAg6VtCqmpzqNSpOMiBhkm6n5BEqrj44/4Ff+BnojzCISE4GsT0p5zE+9unb1FNl/9yllTRMRweq/BTyXgVT96AQJAUS+58qkg9+YbYi1pykruAc6uVP0rL48hYTipkt1f6QMZoNBz4Z+RmQljLZr3MckVGQ0Bp0LupnmVe3gTi7ADRwJALxly6Ooj0H4HDQ9u5R/YM+kxiZ1khN/m1GqXC2b2kZuSx2oxGl1HhImzX2I4X+KpCOJj+8tu4lziDkeitYztAQ==";
	//支付宝公钥
	//public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
	public static final int SDK_PAY_FLAG = 1;
	public static final int SDK_CHECK_FLAG = 2;
}
