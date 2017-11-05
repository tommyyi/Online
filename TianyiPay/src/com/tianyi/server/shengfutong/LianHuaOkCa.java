package com.tianyi.server.shengfutong;

public class LianHuaOkCa
{
	private static int[] supportList = {50,100,200,500,1000};
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
