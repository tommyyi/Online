package com.tianyi.uppay;

public interface TianyiUppayCallback
{
	public void handSuccess(String orderid);
	public void handFail(String orderid);
}
