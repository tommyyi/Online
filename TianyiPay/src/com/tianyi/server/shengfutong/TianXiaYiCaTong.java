package com.tianyi.server.shengfutong;

public class TianXiaYiCaTong
{
	private static int[] supportList = {10,20,30,40,50,60,70,80,90,100};
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
