package com.tianyi.server.shengfutong;

public class SouHuYiCaTong
{
	private static int[] supportList = {5,15,30,40,100};
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
