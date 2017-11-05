package com.tianyi.server;

public class Constant
{
	public final static  String registerURL = "http://103.20.248.170/openapi/index.php?r=user/register";
	public static final String	loginURL	= "http://103.20.248.170/openapi/index.php?r=auth/authorize";
	public static final String	changePasswordURL	= "http://103.20.248.170/openapi/index.php?r=user/changePassword";
	
	public static final String	resetkeyURL	= "http://103.20.248.170/openapi/index.php?r=user/resetkey";
	public static final String	resetPasswordURL	= "http://103.20.248.170/openapi/index.php?r=user/resetPassword";
	public static final String	bindPhoneURL	= "http://103.20.248.170/openapi/index.php?r=user/binding";
	
	public static final String	createOrderURL	= "http://103.20.248.170/openapi/index.php?r=order/create";
	public static final String	checkOrderURL	= "http://103.20.248.170/openapi/index.php?r=order/check";
	public static final String	cardPayURL	= "http://103.20.248.170/openapi/index.php?r=payment/card";
	
	public static final String	payListURL	= "http://103.20.248.170/openapi/index.php?r=order/list";
	
	public static final String	SharedpreferenceFilename	= "info";
	public static final String	identification_fieldname	= "identification";
}
