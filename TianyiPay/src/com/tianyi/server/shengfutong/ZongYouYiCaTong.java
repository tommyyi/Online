package com.tianyi.server.shengfutong;

public class ZongYouYiCaTong
{
	private static int[] supportList = {10,15,30,50,100};
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
