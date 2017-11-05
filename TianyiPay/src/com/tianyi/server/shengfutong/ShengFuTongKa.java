package com.tianyi.server.shengfutong;

public class ShengFuTongKa
{
	private static int[] supportList = {10,15,30,50,100};
	
	public static boolean isInclude(int amount)
	{
		return true;
		/*int length = supportList.length;
		for(int index=0;index<length;index++)
		{
			if(amount==supportList[index])
				return true;
		}
		return false;*/
	}
}
