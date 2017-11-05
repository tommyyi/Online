package cn.ipaynow.wx;

public interface TianyiWeChatPayCallback
{
	public void handSuccess(String orderid);
	public void handFail(String orderid);
}
