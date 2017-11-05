package com.tianyi.server.shengfutong;

public class ShengDaKa
{
	private static int[] supportList = {5,10,15};
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
