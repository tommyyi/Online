package com.tianyi.alipay;

import java.net.URLEncoder;

import com.tianyi.alipay.h5pay.H5PayTool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.Toast;

public class TianyiAlipay
{
	/**
	 * 支付成功
	 */
	public static final String	SUCCESS			= "9000";
	/**
	 * 需要安装
	 */
	public static final String	INSTALL			= "10000";
	/**
	 * 需要安装，点击了确定
	 */
	public static final String	INSTALL_OK		= "10001";
	/**
	 * 需要安装，点击了取消
	 */
	public static final String	INSTALL_CANCEL	= "10002";
	/**
	 * 进了APK后，不支付了
	 */
	public static final String	CANCEL			= "10003";
	/**
	 * 支付APK出现异常
	 */
	public static final String	FAIL			= "10004";
	/**
	 * 异常退出
	 */
	public static final String	ERROR			= "4444";
	public TianyiAlipayCallback	tianyiAlipayCallback;
	public String orderid;
	
	public TianyiAlipay(Activity activity, TianyiAlipayCallback tianyiAlipayCallback)
	{
		// 正式版本用
		setContext(activity);
		this.tianyiAlipayCallback = tianyiAlipayCallback;
	}

	// 模拟商户商品列表
	ListView				mproductListView	= null;
	static String			TAG					= "Aalipay";
	private ProgressDialog	mProgress			= null;
	static Activity			mContext;

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param signType
	 *            签名方式
	 * @param content
	 *            待签名订单信息
	 * @return
	 */
	String sign(String signType, String content)
	{
		return Rsa.sign(content, PartnerConfig.RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 * @return
	 */
	String getSignType()
	{
		String getSignType = "sign_type=" + "\"" + "RSA" + "\"";
		return getSignType;
	}

	/**
	 * get the char set we use. 获取字符集
	 * 
	 * @return
	 */
	String getCharset()
	{
		String charset = "charset=" + "\"" + "utf-8" + "\"";
		return charset;
	}

	/**
	 * check some info.the partner,seller etc. 检测配置信息
	 * partnerid商户id，seller收款帐号不能为空
	 * 
	 * @return
	 */
	private boolean isAccountOK()
	{
		String partner = PartnerConfig.PARTNER;
		String seller = PartnerConfig.SELLER;
		if (partner == null || partner.length() <= 0 || seller == null
				|| seller.length() <= 0)
			return false;

		return true;
	}

	public void setContext(Activity activity)
	{
		mContext = activity;
	}
	
	String getOrderInfo(String out_trade_no, String subject, String body, String total_fee)
	{
		String strOrderInfo = "partner=" + "\"" + PartnerConfig.PARTNER + "\"";
		strOrderInfo += "&";
		strOrderInfo += "seller=" + "\"" + PartnerConfig.SELLER + "\"";
		strOrderInfo += "&";
		strOrderInfo += "out_trade_no=" + "\"" + out_trade_no + "\"";
		strOrderInfo += "&";
		strOrderInfo += "subject=" + "\"" + subject + "\"";
		strOrderInfo += "&";
		strOrderInfo += "body=" + "\""	+ body + "\"";
		strOrderInfo += "&";
		strOrderInfo += "total_fee=" + "\"" + total_fee + "\"";
		strOrderInfo += "&";
		strOrderInfo += "notify_url=" + "\""	+ PartnerConfig.NOTIFYURL + "\"";
		strOrderInfo += "&";
		strOrderInfo += "service=" + "\""	+ "mobile.securitypay.pay" + "\"";
		strOrderInfo += "&";
		strOrderInfo += "_input_charset=" + "\""	+ "UTF-8" + "\"";
		System.out.print(strOrderInfo);
		return strOrderInfo;
	}
	
	public void QuickPayment(String out_trade_no, String subject, String body, String total_fee)
	{
		orderid = out_trade_no;
		String produceInfo = getOrderInfo(out_trade_no, subject, body, total_fee);
		
		isSuccee = false;
		//MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(mContext);
		//boolean isMobile_spExist = mspHelper.detectMobile_sp();
		if (/*!isMobile_spExist*/true)
		{
			H5PayTool.dopay(mContext, this, out_trade_no,subject, body, total_fee);
			return;
		}
		// check some info.
		// 检测配置信息
		if (!isAccountOK())
		{
			BaseHelper
					.showDialog(
							mContext,
							"提示",
							"缺少partner或者seller",
							null);
			tianyiAlipayCallback.handFail(orderid);
			return;
		}
		// start pay for this order.
		// 根据订单信息开始进行支付
		try
		{
			// prepare the order info.
			// 准备订单信息
			// 这里根据签名方式对订单信息进行签名
			// {"orderinfo":"partner=2088801744396752&seller=pay@winads.cn&out_trade_no="
			// +
			// "51bea974b04b2&subject=研发部-测试专用号&body=研发部-测试专用号&" +
			// "total_fee=1&notify_url=http://sdk.ddle.cn/user/index.php?r=order/appnotify"}
			// partner="2088111008275155"&seller="ddlegame@ddle.cn"&out_trade_no="5333a15a1c122"&subject="研发部-测试专用号"&body="研发部-测试专用号"&total_fee="0.01"&notify_url="http://user.ddle.cn/appnotifynew.php"&service="mobile.securitypay.pay"&_input_charset="UTF-8"
			// partner="2088801744396752"&seller="pay@winads.cn"&out_trade_no="032815343310497"&subject="新款耐克"&body="2010新款NIKE 耐克902第三代板鞋 耐克男女鞋 386201 白红"&total_fee="0.01"&notify_url="http://notify.java.jpxx.org/index.jsp"
			String signType = getSignType();
			String strsign = sign(signType, produceInfo);
			// WinadLogger.d(TAG,"strsign: "+ strsign);
			// 对签名进行编码
			strsign = URLEncoder.encode(strsign);
			// 组装好参数
			String info = produceInfo + "&sign=" + "\"" + strsign + "\"" + "&"
					+ getSignType();
			// WinadLogger.d(TAG,"orderInfo:"+ info);
			// start the pay.
			// 调用pay方法进行支付
			MobileSecurePayer msp = new MobileSecurePayer();
			boolean bRet = msp.pay(info, mHandler, AlixId.RQF_PAY, mContext);

			if (bRet)
			{
				// show the progress bar to indicate that we have started
				// paying.
				// 显示“正在支付”进度条
				closeProgress();
				// mProgress = BaseHelper.showProgress(mContext, null, "正在支付",
				// false,
				// true);
			}
			else
				;
		}
		catch (Exception ex)
		{
			Toast.makeText(mContext, "Failure calling remote service",
					Toast.LENGTH_SHORT).show();
			tianyiAlipayCallback.handFail(orderid);
		}
	}

	private static final String	ALIPAYJUMP	= "alipayjump";

	void resultCode(String meothed, String code)
	{
		if(code==SUCCESS)
			tianyiAlipayCallback.handSuccess(orderid);
		else
			tianyiAlipayCallback.handFail(orderid);
	}

	static boolean				isSuccee		= false;

	private final static String	MEMO			= "memo={";
	private final static String	RESULT_STATUS	= "resultStatus={";
	private final static String	RESULT			= "result={";

	public String payStatus(String str)
	{
		return String.valueOf(isSuccee);
	}

	// 这里接收支付结果，支付宝手机端同步通知
	// Drawable drawable = null;
	private Handler	mHandler	= new Handler() {
									public void handleMessage(Message msg)
									{
										try
										{
											String strRet = (String) msg.obj;

											// Log.e(TAG, strRet); //
											// strRet范例：resultStatus={9000};memo={};result={partner="2088201564809153"&seller="2088201564809153"&out_trade_no="050917083121576"&subject="123456"&body="2010新款NIKE 耐克902第三代板鞋 耐克男女鞋 386201 白红"&total_fee="0.01"&notify_url="http://notify.java.jpxx.org/index.jsp"&success="true"&sign_type="RSA"&sign="d9pdkfy75G997NiPS1yZoYNCmtRbdOP0usZIMmKCCMVqbSG1P44ohvqMYRztrB6ErgEecIiPj9UldV5nSy9CrBVjV54rBGoT6VSUF/ufjJeCSuL510JwaRpHtRPeURS1LXnSrbwtdkDOktXubQKnIMg2W0PreT1mRXDSaeEECzc="}
											switch(msg.what)
											{
											case AlixId.RQF_PAY:
											{
												//
												closeProgress();

												// 处理交易结果
												try
												{
													// 获取交易状态码，具体状态代码请参看文档
													String[] dataStrings = strRet
															.split(";");
													String memo = "";
													String resultStatus = "";
													String result = "";
													for (int i = 0; i < dataStrings.length; i++)
													{
														String tempString = dataStrings[i];
														int endIndex = tempString
																.indexOf("}");
														if (-1 != tempString
																.indexOf(MEMO))
														{
															memo = tempString
																	.substring(
																			MEMO.length(),
																			endIndex);
														}
														else if (-1 != tempString
																.indexOf(RESULT_STATUS))
														{
															resultStatus = tempString
																	.substring(
																			RESULT_STATUS
																					.length(),
																			endIndex);
														}
														else if (-1 != tempString
																.indexOf(RESULT))
														{
															result = tempString
																	.substring(
																			RESULT.length(),
																			endIndex);
														}
													}
													// drawable =
													// AssetPic.loadDrawable(mContext,
													// "info.png");
													// Drawable drawableInfo
													// =
													// AssetPic.loadDrawable(mContext,
													// "infoicon.png");
													// 先验签通知
													ResultChecker resultChecker = new ResultChecker(strRet);
													int retVal = resultChecker.checkSign();
													// 验签失败
													if (retVal == ResultChecker.RESULT_CHECK_SIGN_FAILED)
													{
														// System.out.println("==DdleAlipay==验证失败=="
														// + retVal);
														resultCode(ALIPAYJUMP,
																CANCEL);
														// BaseHelper.showDialog(
														// mContext,
														// "提示",
														// "您的订单信息已被非法篡改",
														// android.R.drawable.ic_dialog_alert);
													}
													else
													{// 验签成功。验签成功后再判断交易状态码
														if (resultStatus
																.equals("9000"))
														{
															// System.out.println("==DdleAlipay==支付成功=="
															// +
															// resultStatus);
															resultCode(
																	ALIPAYJUMP,
																	SUCCESS);
															isSuccee = true;
															// BaseHelper.showDialog(mContext,
															// "提示","支付成功。交易状态码："+tradeStatus,null);

														}// 判断交易状态码，只有9000表示交易成功
														else
														{
															// System.out.println("==DdleAlipay==支付失败=="
															// +
															// resultStatus);
															resultCode(
																	ALIPAYJUMP,
																	CANCEL);
															isSuccee = false;
															// BaseHelper.showDialog(mContext,
															// "提示",
															// "支付失败。交易状态码:"
															// +
															// tradeStatus,
															// null);
														}
													}
													// String tradeStatus =
													// "resultStatus={";
													// int imemoStart =
													// strRet.indexOf("resultStatus=");
													// imemoStart +=
													// tradeStatus.length();
													// int imemoEnd =
													// strRet.indexOf("};memo=");
													// tradeStatus =
													// strRet.substring(imemoStart,
													// imemoEnd);
													// // drawable =
													// AssetPic.loadDrawable(mContext,
													// "info.png");
													// // Drawable
													// drawableInfo =
													// AssetPic.loadDrawable(mContext,
													// "infoicon.png");
													// //先验签通知
													// ResultChecker
													// resultChecker = new
													// ResultChecker(strRet);
													// int retVal =
													// resultChecker.checkSign();
													// // 验签失败
													// if (retVal ==
													// ResultChecker.RESULT_CHECK_SIGN_FAILED)
													// {
													// resultCode(ALIPAYJUMP,
													// String.valueOf(retVal));
													// //
													// BaseHelper.showDialog(
													// // mContext,
													// // "提示",
													// // "您的订单信息已被非法篡改",
													// //
													// android.R.drawable.ic_dialog_alert);
													// } else {//
													// 验签成功。验签成功后再判断交易状态码
													// if(tradeStatus.equals("9000")){
													// resultCode(ALIPAYJUMP,
													// tradeStatus);
													// isSuccee = true;
													// //
													// BaseHelper.showDialog(mContext,
													// "提示","支付成功。交易状态码："+tradeStatus,null);
													//
													//
													// }//判断交易状态码，只有9000表示交易成功
													// else{
													// resultCode(ALIPAYJUMP,
													// tradeStatus);
													// isSuccee = false;
													// //
													// BaseHelper.showDialog(mContext,
													// "提示", "支付失败。交易状态码:"
													// // + tradeStatus,
													// null);
													// }
													// }

												}
												catch (Exception e)
												{
													e.printStackTrace();
													resultCode(ALIPAYJUMP,
															ERROR);
													// BaseHelper.showDialog(mContext,
													// "提示", strRet,
													// null);
												}
											}
												break;
											}

											super.handleMessage(msg);
										}
										catch (Exception e)
										{
											e.printStackTrace();
											resultCode(ALIPAYJUMP, ERROR);
										}
									}
								};

	//
	//
	/**
	 * the OnCancelListener for lephone platform. lephone系统使用到的取消dialog监听
	 */
	static class AlixOnCancelListener implements
			DialogInterface.OnCancelListener
	{
		Activity	mcontext;

		AlixOnCancelListener(Activity context)
		{
			mcontext = context;
		}

		public void onCancel(DialogInterface dialog)
		{
			mcontext.onKeyDown(KeyEvent.KEYCODE_BACK, null);
		}
	}

	//
	// close the progress bar
	// 关闭进度框
	void closeProgress()
	{
		try
		{
			if (mProgress != null)
			{
				mProgress.dismiss();
				mProgress = null;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
