package com.tianyi.alipay;

public interface TianyiAlipayCallback
{
	public void handSuccess(String orderid);
	public void handFail(String orderid);
}
