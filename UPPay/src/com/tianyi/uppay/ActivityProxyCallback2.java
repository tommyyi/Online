package com.tianyi.uppay;

/**
 * ActivityProxyCallback的扩展接口，需要销毁服务用这个接口
 * 
 * @author yongleZheng
 * 
 */
public interface ActivityProxyCallback2 extends ActivityProxyCallback
{
	void onActivityDestroy();
}
