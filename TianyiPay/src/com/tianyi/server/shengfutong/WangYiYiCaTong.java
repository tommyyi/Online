package com.tianyi.server.shengfutong;

public class WangYiYiCaTong
{
	private static int[] supportList = {5,10,15,20,30,50};
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
