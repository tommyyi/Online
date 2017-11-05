package com.tianyi.server.shengfutong;

public class ZhengTuYiCaTong
{
	private static int[] supportList = {4,5,6,10,15,20,30,50,100,200,500};
	public static boolean isInclude(int amount)
	{
		int length = supportList.length;
		for(int index=0;index<length;index++)
		{
			if(amount==supportList[index])
				return true;
		}
		return false;
	}
}
