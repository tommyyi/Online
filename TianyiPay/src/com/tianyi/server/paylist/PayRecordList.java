package com.tianyi.server.paylist;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

public class PayRecordList
{
	public List<PayRecord> payRecordList=null;
	
	public PayRecordList(String result)
	{
		try
		{
			JSONArray jsonArray = new JSONArray(result);
			int length = jsonArray.length();
			payRecordList = new ArrayList<PayRecord>();
			for(int index=0;index<length;index++)
			{
				PayRecord payRecord = new PayRecord(jsonArray.getString(index));
				payRecordList.add(payRecord);
			}
		}
		catch (Exception e)
		{
			payRecordList=null;
			e.printStackTrace();
		}
	}
}