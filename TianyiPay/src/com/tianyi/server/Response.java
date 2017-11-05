package com.tianyi.server;

import org.json.JSONException;
import org.json.JSONObject;

public class Response
{
	public Response(String body)
	{
		JSONObject jsonObject = null;
		try
		{
			jsonObject = new JSONObject(body);
			code = jsonObject.getString("code");
			message = jsonObject.getString("message");
			result = jsonObject.getString("result");
		}
		catch (Exception e)
		{
			code = null;
			message = null;
			result = null;
			e.printStackTrace();
		}
	}

	public String	code = null;
	public String	message = null;
	public String	result = null;
}
