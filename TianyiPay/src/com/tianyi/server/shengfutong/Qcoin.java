package com.tianyi.server.shengfutong;

public class Qcoin
{
	private static int[] supportList = {5,10,15,20,30,60,100,200};
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
