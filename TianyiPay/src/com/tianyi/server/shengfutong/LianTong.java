package com.tianyi.server.shengfutong;

public class LianTong
{
	private static int[] supportList = {20,30,50,100,200,300,500};
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
