package com.tianyi.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianyi.server.TianyiPay;
import com.tianyi.server.order.CheckOrder;
import com.tianyi.server.order.CheckOrder.CheckStatus;
import com.tianyi.server.order.CreateOrder;
import com.tianyi.server.order.CreateOrderResult;
import com.tianyi.server.shengfutong.CardPay;
import com.tianyi.server.shengfutong.CardString;
import com.tianyi.tool.data.Tool;

public class TianyiCardpayActivity extends Activity
{
	private View			goback;
	private Button			submitButton;
	private Button			cancelButton;
	private TextView		card_amount_textview;
	private EditText		card_number_EditText;
	private EditText		card_password_eEditText;
	private LoadingDialog	loadingDialog;
	private Handler			handler;
	private String			amount;
	private String			cardname;
	private String			orderid;
	private TextView		cardname_textview;
	private TextView		cardpaytips_textview;
	private String	notify_url;
	private String	extend;
	private String	product_name;
	private String	server_name;
	private String	player_name;
	private String	cpOrderID;
	private String	remark;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(UIResource.getLayoutResIDByName(getApplicationContext(),
				"tianyi_card_pay"));

		try
		{
			amount = getIntent().getExtras().getString("amount");
			notify_url = getIntent().getExtras().getString("notify_url");
			extend = getIntent().getExtras().getString("extend");
			product_name = getIntent().getExtras().getString("product_name");
			server_name = getIntent().getExtras().getString("server_name");
			player_name = getIntent().getExtras().getString("player_name");
			cpOrderID = getIntent().getExtras().getString("cpOrderID");
			remark = getIntent().getExtras().getString("remark");
			cardname = getIntent().getExtras().getString("cardname");
		}
		catch (Exception e)
		{
			finish();
			return;
		}

		setOnclickGoback();

		submitButton = (Button) findViewById(UIResource.getIdResIDByName(
				getApplicationContext(), "tianyi_card_pay_ok"));
		cancelButton = (Button) findViewById(UIResource.getIdResIDByName(
				getApplicationContext(), "tianyi_card_pay_cancel"));
		cardname_textview = (TextView) findViewById(UIResource
				.getIdResIDByName(getApplicationContext(), "cardname"));
		cardname_textview.setText(cardname + cardname_textview.getText());

		cardpaytips_textview = (TextView) findViewById(UIResource
				.getIdResIDByName(getApplicationContext(), "tianyi_card_pay_tips"));
		setTip();
		
		card_amount_textview = (TextView) findViewById(UIResource
				.getIdResIDByName(getApplicationContext(), "card_amount"));
		card_amount_textview.setText(amount + getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_yuan")));

		card_number_EditText = (EditText) findViewById(UIResource
				.getIdResIDByName(getApplicationContext(), "card_number"));
		card_password_eEditText = (EditText) findViewById(UIResource
				.getIdResIDByName(getApplicationContext(), "card_password"));

		setOnclick();
		handler = new Handler();
		loadingDialog = new LoadingDialog(TianyiCardpayActivity.this,
				UIResource.getStyleResIDByName(getApplicationContext(),
						"tianyi_loading_dialog"), handler);
	}

	private void setOnclick()
	{
		submitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0)
			{
				loadingDialog.show(getString(UIResource.getStringResIDByName(
						getApplicationContext(), "tianyi_is_creating_order")));

				new Thread() {
					@Override
					public void run()
					{
						CreateOrderResult createOrderResult = CreateOrder
								.createOrder(TianyiCardpayActivity.this,
										amount, notify_url, extend,
										product_name, server_name, player_name,
										cpOrderID, remark, "shengpay");

						if (createOrderResult != null)
						{
							orderid = createOrderResult.orderid;
							loadingDialog.dismiss();
							final String number = card_number_EditText.getText().toString();
							final String password = card_password_eEditText.getText().toString();

							if (!isCheckOk(number, password,getApplicationContext()))
								return;

							loadingDialog.show(getString(UIResource.getStringResIDByName(
											getApplicationContext(),"tianyi_paying_tips")));
							String card_type = getCardtype();
							if (cardname.equals(getString(UIResource.getStringResIDByName(
											getApplicationContext(),CardString.tianyi_shengfutong)))
									|| cardname.equals(getString(UIResource.getStringResIDByName(
													getApplicationContext(),CardString.tianyi_shengdaka))))
							{
								if (CardPay.pay(getApplicationContext(),orderid, number, password, card_type))
								{
									loadingDialog.dismiss();
									Tool.showToast(handler,TianyiCardpayActivity.this,
											getString(UIResource.getStringResIDByName(
											getApplicationContext(),"tianyi_pay_success_tips")));

                                    TianyiPay.tianyiPay.getTianyiPayCallback().handSuccess(orderid);
									TianyiCardpayActivity.this.finish();
									TianyiPaymentCenterActivity.tianyiPaymentCenterActivity.finish();
								}
								else
								{
									loadingDialog.dismiss();
									Tool.showToastLONG(handler,TianyiCardpayActivity.this,
											getString(UIResource.getStringResIDByName(
											getApplicationContext(),"tianyi_pay_fail_tips")));
									//TianyiPay.tianyiPay.getTianyiPayCallback().handFail(orderid);
									//TianyiCardpayActivity.this.finish();
								}
							}
							else
							{
								if (CardPay.pay_then_you_need_check(getApplicationContext(), orderid,
										number, password, card_type))
								{
									int checkresult = CheckOrder.isOrderPayOK(TianyiCardpayActivity.this,orderid);
									if (checkresult == CheckStatus.SUCCESS)
									{
										loadingDialog.dismiss();
										Tool.showToast(handler,TianyiCardpayActivity.this,
												getString(UIResource.getStringResIDByName(
												getApplicationContext(),"tianyi_pay_success_tips")));

                                        TianyiPay.tianyiPay.getTianyiPayCallback().handSuccess(orderid);
										TianyiCardpayActivity.this.finish();
										TianyiPaymentCenterActivity.tianyiPaymentCenterActivity.finish();
									}
									else
									{
										loadingDialog.dismiss();
										Tool.showToast(handler,TianyiCardpayActivity.this,
												getString(UIResource.getStringResIDByName(
												getApplicationContext(),"tianyi_pay_notconfirm_tips")));

										TianyiPay.tianyiPay.getTianyiPayCallback().handSuccess(orderid);
										TianyiCardpayActivity.this.finish();
										TianyiPaymentCenterActivity.tianyiPaymentCenterActivity.finish();
									}
								}
								else
								{
									loadingDialog.dismiss();
									Tool.showToastLONG(handler,TianyiCardpayActivity.this,
											getString(UIResource.getStringResIDByName(
											getApplicationContext(),"tianyi_pay_submitfail_tips")));
									//TianyiPay.tianyiPay.getTianyiPayCallback().handFail(orderid);
									// TianyiCardpayActivity.this.finish();
								}
							}
							loadingDialog.dismiss();
						}
						else
						{
							loadingDialog.dismiss();
							Tool.showToast(handler, TianyiCardpayActivity.this,
									getString(UIResource.getStringResIDByName(
									getApplicationContext(),"tianyi_order_creating_fail")));
						}
						super.run();
					}
				}.start();
			}
		});

		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0)
			{
				TianyiCardpayActivity.this.finish();
			}
		});
	}

	private String getCardtype()
	{
		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_dianxin))))
			return "77";

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_yidong))))
			return "75";

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_liantong))))
			return "76";

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_shengdaka))))
			return "03";

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_shengfutong))))
			return "42";

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_shengfutongyuleyikatong))))
			return null;

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_tecent))))
			return "66";

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_tianxiayikatong))))
			return "73";

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_jiuyouyikatong))))
			return "68";

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_wanmeiyikatong))))
			return "62";

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_souhuyikatong))))
			return "64";

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_guangyuyikatong))))
			return null;

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_wangyiyikatong))))
			return "63";

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_zongyouyikatong))))
			return "65";

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_tianhongyikatong))))
			return "67";

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_junwangyikatong))))
			return "60";

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_zhengtouyikatong))))
			return "61";

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_lianhuaokka))))
			return "91";

		return null;
	}

	protected boolean isCheckOk(String number, String password, Context context)
	{
		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_dianxin))))
		{
			
		}

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_yidong))))
		{

		}

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_liantong))))
		{

		}

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_shengdaka))))
		{

		}

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_shengfutong))))
		{

		}

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_shengfutongyuleyikatong))))
		{

		}

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_tecent))))
		{

		}

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_tianxiayikatong))))
		{

		}

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_jiuyouyikatong))))
		{

		}

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_wanmeiyikatong))))
		{

		}

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_souhuyikatong))))
		{

		}

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_guangyuyikatong))))
		{

		}

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_wangyiyikatong))))
		{

		}

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_zongyouyikatong))))
		{

		}

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_tianhongyikatong))))
		{

		}

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_junwangyikatong))))
		{

		}

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_zhengtouyikatong))))
		{

		}

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_lianhuaokka))))
		{

		}

		return true;
	}

	protected void setTip()
	{
		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_dianxin))))
		{
			cardpaytips_textview.setText(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_dianxin_tip));
		}

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_yidong))))
		{
			cardpaytips_textview.setText(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_yidong_tip));
		}

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_liantong))))
		{
			cardpaytips_textview.setText(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_liantong_tip));
		}

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_shengdaka))))
		{
			cardpaytips_textview.setText(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_shengdaka_tip));
		}

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_shengfutong))))
		{
			cardpaytips_textview.setText(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_shengfutong_tip));
		}

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_shengfutongyuleyikatong))))
		{
			cardpaytips_textview.setText(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_shengfutongyuleyikatong_tip));
		}

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_tecent))))
		{
			cardpaytips_textview.setText(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_tecent_tip));
		}

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_tianxiayikatong))))
		{
			cardpaytips_textview.setText(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_tianxiayikatong_tip));
		}

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_jiuyouyikatong))))
		{
			cardpaytips_textview.setText(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_jiuyouyikatong_tip));
		}

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_wanmeiyikatong))))
		{
			cardpaytips_textview.setText(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_wanmeiyikatong_tip));
		}

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_souhuyikatong))))
		{
			cardpaytips_textview.setText(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_souhuyikatong_tip));
		}

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_guangyuyikatong))))
		{
			cardpaytips_textview.setText(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_guangyuyikatong_tip));
		}

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_wangyiyikatong))))
		{
			cardpaytips_textview.setText(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_wangyiyikatong_tip));
		}

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_zongyouyikatong))))
		{
			cardpaytips_textview.setText(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_zongyouyikatong_tip));
		}

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_tianhongyikatong))))
		{
			cardpaytips_textview.setText(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_tianhongyikatong_tip));
		}

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_junwangyikatong))))
		{
			cardpaytips_textview.setText(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_junwangyikatong_tip));
		}

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_zhengtouyikatong))))
		{
			cardpaytips_textview.setText(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_zhengtouyikatong_tip));
		}

		if (cardname.equals(getString(UIResource.getStringResIDByName(
				getApplicationContext(), CardString.tianyi_lianhuaokka))))
		{
			cardpaytips_textview.setText(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_lianhuaokka_tip));
		}
	}
	
	private void setOnclickGoback()
	{
		goback = (ImageView) findViewById(UIResource.getIdResIDByName(
				getApplicationContext(), "goback"));
		goback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
	}
}