package com.tianyi.ui;

import android.content.Context;

public class UIResource
{
	public static int getLayoutResIDByName(Context context, String name)
	{
		return context.getResources().getIdentifier(name, "layout",
				context.getPackageName());
	}

	public static int getIdResIDByName(Context context, String name)
	{
		return context.getResources().getIdentifier(name, "id",
				context.getPackageName());
	}

	public static int getStringResIDByName(Context context, String name)
	{
		return context.getResources().getIdentifier(name, "string",
				context.getPackageName());
	}

	public static int getDrawableResIDByName(Context context, String name)
	{
		return context.getResources().getIdentifier(name, "drawable",
				context.getPackageName());
	}

	public static int getRawResIDByName(Context context, String name)
	{
		return context.getResources().getIdentifier(name, "raw",
				context.getPackageName());
	}

	public static int getStyleResIDByName(Context context, String name)
	{
		return context.getResources().getIdentifier(name, "style",
				context.getPackageName());
	}
}