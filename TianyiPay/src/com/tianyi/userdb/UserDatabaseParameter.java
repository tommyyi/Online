package com.tianyi.userdb;

public class UserDatabaseParameter
{
	public static final int		VERSION					= 1;
	public static final String	DATABASE_USER			= "user.db";
	public static final String	TABLE_USER			= "user";
	public static final String	COLUMN_NAME_ID				= "id";//or _id
	public static final String	COLUMN_USER_NAME			= "username";
	public static final String	COLUMN_PASSWORD			= "password";
	public static final String	COLUMN_BINDEDNUM			= "bindednum";
	public static final String	COLUMN_ACCESSTOKEN			= "accesstoken";
	public static final String	COLUMN_REMEMBER_PASSWORD	= "remember";
	public static final String	COLUMN_IS_LATEST_LOGIN_ACCOUNT	= "islatestlogin";

	public static final String	CREATE_TABLE_FILTER		= "create table "
																+ TABLE_USER
																+ " ("
																+ COLUMN_NAME_ID
																+ " INTEGER  PRIMARY KEY autoincrement, "
																+ COLUMN_USER_NAME
																+ " text not null, "
																+ COLUMN_PASSWORD
																+ " text, "
																+ COLUMN_ACCESSTOKEN
																+ " text, "
																+ COLUMN_BINDEDNUM
																+ " text, "
																+ COLUMN_REMEMBER_PASSWORD
																+ " text, "
																+ COLUMN_IS_LATEST_LOGIN_ACCOUNT
																+ " text);";
}