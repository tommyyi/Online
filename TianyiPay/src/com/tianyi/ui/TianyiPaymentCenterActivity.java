package com.tianyi.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tianyi.alipay.TianyiAlipay;
import com.tianyi.alipay.TianyiAlipayCallback;
import com.tianyi.server.TianyiPay;
import com.tianyi.server.order.CreateOrder;
import com.tianyi.server.order.CreateOrderResult;
import com.tianyi.server.shengfutong.CardString;
import com.tianyi.server.shengfutong.DianXin;
import com.tianyi.server.shengfutong.GuangYuYiCaTong;
import com.tianyi.server.shengfutong.JiuYouYiCaTong;
import com.tianyi.server.shengfutong.JunWangYiCaTong;
import com.tianyi.server.shengfutong.LianHuaOkCa;
import com.tianyi.server.shengfutong.LianTong;
import com.tianyi.server.shengfutong.Qcoin;
import com.tianyi.server.shengfutong.ShengDaKa;
import com.tianyi.server.shengfutong.ShengFuTongKa;
import com.tianyi.server.shengfutong.SouHuYiCaTong;
import com.tianyi.server.shengfutong.TianHongYiCaTong;
import com.tianyi.server.shengfutong.TianXiaYiCaTong;
import com.tianyi.server.shengfutong.WanMeiYiCaTong;
import com.tianyi.server.shengfutong.WangYiYiCaTong;
import com.tianyi.server.shengfutong.Yidong;
import com.tianyi.server.shengfutong.ZhengTuYiCaTong;
import com.tianyi.server.shengfutong.ZongYouYiCaTong;
import com.tianyi.tool.data.Tool;
import com.tianyi.userdb.UserDatabaseAdapater;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.ipaynow.wx.TianyiWeChatPayCallback;
import cn.ipaynow.wx.WeChatPayManager;

//import com.tianyi.uppay.TianyiUppayCallback;
//import com.tianyi.uppay.UPpayPlugin;

public class TianyiPaymentCenterActivity extends Activity
{
    public static TianyiPaymentCenterActivity tianyiPaymentCenterActivity;
    private String amount;
    private String notify_url;
    private String extend;
    private String product_name;
    private String server_name;
    private String player_name;
    private String cpOrderID;
    private String remark;
    private TextView payment_center_username;
    private TextView payment_center_productname;
    private TextView payment_center_price;
    private Button tianyi_payment_center_alipay;
    private Button tianyi_payment_center_weixin;
    private Button tianyi_payment_center_yl;
    private Button tianyi_payment_center_yd;
    private Button tianyi_payment_center_liantong;
    private Button tianyi_payment_center_shengfutongka;
    private Button tianyi_payment_center_shengdaka;
    private Button tianyi_payment_center_dianxin;
    private Button tianyi_payment_center_zhengtouyikatong;
    private Button tianyi_payment_center_junwangyikatong;
    private Button tianyi_payment_center_wanmeiyikatong;
    private Button tianyi_payment_center_wangyiyikatong;
    private Button tianyi_payment_center_souhuyikatong;
    private Button tianyi_payment_center_zongyouyikatong;
    private Button tianyi_payment_center_tecent;
    private Button tianyi_payment_center_tianhongyikatong;
    private Button tianyi_payment_center_jiuyouyikatong;
    private Button tianyi_payment_center_tianxiayikatong;
    private Button tianyi_payment_center_lianhuaokka;
    private Handler handler;
    private ImageView goback;
    private LoadingDialog loadingDialog;
    private WeChatPayManager mWeChatPayManager;
    //private UPpayPlugin	uPpayPlugin;

    @Override
    protected void onDestroy()
    {
        mWeChatPayManager.destroy();
        tianyiPaymentCenterActivity = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        mWeChatPayManager = new WeChatPayManager(this);
        mWeChatPayManager.init();

        tianyiPaymentCenterActivity = this;
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
        }
        catch (Exception e)
        {
            finish();
            return;
        }

        setContentView(UIResource.getLayoutResIDByName(getApplicationContext(), "tianyi_payment_center"));

        handler = new Handler();
        loadingDialog = new LoadingDialog(TianyiPaymentCenterActivity.this, UIResource.getStyleResIDByName(getApplicationContext(), "tianyi_loading_dialog"), handler);
        goback = (ImageView) findViewById(UIResource.getIdResIDByName(getApplicationContext(), "goback"));
        setOnclickGoback();

        payment_center_username = (TextView) findViewById(UIResource.getIdResIDByName(getApplicationContext(), "payment_center_username"));
        String username1 = null;
        try
        {
            username1 = new UserDatabaseAdapater(TianyiPaymentCenterActivity.this).getUserWhichIsLastestLogin().getUsername();
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "账号未登录，支付失败(可能是因为网络无连接导致了账号登录的失败)", Toast.LENGTH_LONG).show();
            super.onCreate(savedInstanceState);
            finish();
            TianyiPay.tianyiPay.getTianyiPayCallback().handFail(null);
            return;
        }
        payment_center_username.setText(username1);
        payment_center_productname = (TextView) findViewById(UIResource.getIdResIDByName(getApplicationContext(), "payment_center_productname"));
        payment_center_productname.setText(product_name);
        payment_center_price = (TextView) findViewById(UIResource.getIdResIDByName(getApplicationContext(), "payment_center_price"));
        payment_center_price.setText(amount + getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_yuan")));

        tianyi_payment_center_alipay = (Button) findViewById(UIResource.getIdResIDByName(getApplicationContext(), "tianyi_payment_center_alipay"));
        tianyi_payment_center_yl = (Button) findViewById(UIResource.getIdResIDByName(getApplicationContext(), "tianyi_payment_center_yl"));
        tianyi_payment_center_weixin = (Button) findViewById(UIResource.getIdResIDByName(getApplicationContext(), "tianyi_payment_center_weixin"));
        tianyi_payment_center_yd = (Button) findViewById(UIResource.getIdResIDByName(getApplicationContext(), "tianyi_payment_center_yd"));
        tianyi_payment_center_liantong = (Button) findViewById(UIResource.getIdResIDByName(getApplicationContext(), "tianyi_payment_center_liantong"));
        tianyi_payment_center_shengfutongka = (Button) findViewById(UIResource.getIdResIDByName(getApplicationContext(), "tianyi_payment_center_shengfutong"));
        tianyi_payment_center_shengdaka = (Button) findViewById(UIResource.getIdResIDByName(getApplicationContext(), "tianyi_payment_center_shengdaka"));
        tianyi_payment_center_dianxin = (Button) findViewById(UIResource.getIdResIDByName(getApplicationContext(), "tianyi_payment_center_dianxin"));
        tianyi_payment_center_zhengtouyikatong = (Button) findViewById(UIResource.getIdResIDByName(getApplicationContext(), "tianyi_payment_center_zhengtuyikatong"));
        tianyi_payment_center_junwangyikatong = (Button) findViewById(UIResource.getIdResIDByName(getApplicationContext(), "tianyi_payment_center_junwang"));
        tianyi_payment_center_wanmeiyikatong = (Button) findViewById(UIResource.getIdResIDByName(getApplicationContext(), "tianyi_payment_center_wanmeiyikatong"));
        tianyi_payment_center_wangyiyikatong = (Button) findViewById(UIResource.getIdResIDByName(getApplicationContext(), "tianyi_payment_center_wangyiyikatong"));
        tianyi_payment_center_souhuyikatong = (Button) findViewById(UIResource.getIdResIDByName(getApplicationContext(), "tianyi_payment_center_souhuyikatong"));
        tianyi_payment_center_zongyouyikatong = (Button) findViewById(UIResource.getIdResIDByName(getApplicationContext(), "tianyi_payment_center_zongyouyikatong"));
        tianyi_payment_center_tecent = (Button) findViewById(UIResource.getIdResIDByName(getApplicationContext(), "tianyi_payment_center_tecent"));
        tianyi_payment_center_tianhongyikatong = (Button) findViewById(UIResource.getIdResIDByName(getApplicationContext(), "tianyi_payment_center_tianhongyikatong"));
        tianyi_payment_center_jiuyouyikatong = (Button) findViewById(UIResource.getIdResIDByName(getApplicationContext(), "tianyi_payment_center_jiuyouyikatong"));
        tianyi_payment_center_tianxiayikatong = (Button) findViewById(UIResource.getIdResIDByName(getApplicationContext(), "tianyi_payment_center_tianxiayikatong"));
        tianyi_payment_center_lianhuaokka = (Button) findViewById(UIResource.getIdResIDByName(getApplicationContext(), "tianyi_payment_center_lianhuaokka"));

        setOnclick_payment_center_alipay();
        setOnclick_payment_center_yl();
        setOnclick_payment_center_weixin();
        setOnclick_payment_center_yd();
        setOnclick_payment_center_liantong();
        setOnclick_payment_center_shengfutong();
        setOnclick_payment_center_shengdaka();
        setOnclick_payment_center_dianxin();
        setOnclick_payment_center_zhengtou();
        setOnclick_payment_center_junwang();
        setOnclick_payment_center_wanmei();
        setOnclick_payment_center_wangyi();
        setOnclick_payment_center_souhu();
        setOnclick_payment_center_zongyou();
        setOnclick_payment_center_tecent();
        setOnclick_payment_center_tianhong();
        setOnclick_payment_center_jiuyou();
        setOnclick_payment_center_tianxia();
        setOnclick_payment_center_lianhua();
        super.onCreate(savedInstanceState);
    }

    private void setOnclick_payment_center_lianhua()
    {
        tianyi_payment_center_lianhuaokka.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                new CardPayThread(getString(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_lianhuaokka))).start();
            }
        });
    }

    private void setOnclick_payment_center_tianxia()
    {
        tianyi_payment_center_tianxiayikatong.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                new CardPayThread(getString(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_tianxiayikatong))).start();
            }
        });
    }

    private void setOnclick_payment_center_jiuyou()
    {
        tianyi_payment_center_jiuyouyikatong.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                new CardPayThread(getString(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_jiuyouyikatong))).start();
            }
        });
    }

    private void setOnclick_payment_center_tianhong()
    {
        tianyi_payment_center_tianhongyikatong.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                new CardPayThread(getString(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_tianhongyikatong))).start();
            }
        });
    }

    private void setOnclick_payment_center_tecent()
    {
        tianyi_payment_center_tecent.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                new CardPayThread(getString(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_tecent))).start();
            }
        });
    }

    private void setOnclick_payment_center_zongyou()
    {
        tianyi_payment_center_zongyouyikatong.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                new CardPayThread(getString(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_zongyouyikatong))).start();
            }
        });
    }

    private void setOnclick_payment_center_souhu()
    {
        tianyi_payment_center_souhuyikatong.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                new CardPayThread(getString(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_souhuyikatong))).start();
            }
        });
    }

    private void setOnclick_payment_center_wangyi()
    {
        tianyi_payment_center_wangyiyikatong.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                new CardPayThread(getString(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_wangyiyikatong))).start();
            }
        });
    }

    private void setOnclick_payment_center_wanmei()
    {
        tianyi_payment_center_wanmeiyikatong.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                new CardPayThread(getString(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_wanmeiyikatong))).start();
            }
        });
    }

    private void setOnclick_payment_center_junwang()
    {
        tianyi_payment_center_junwangyikatong.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                new CardPayThread(getString(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_junwangyikatong))).start();
            }
        });
    }

    private void setOnclick_payment_center_zhengtou()
    {
        tianyi_payment_center_zhengtouyikatong.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                new CardPayThread(getString(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_zhengtouyikatong))).start();
            }
        });
    }

    private void setOnclick_payment_center_dianxin()
    {
        tianyi_payment_center_dianxin.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                new CardPayThread(getString(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_dianxin))).start();
            }
        });
    }

    private void setOnclick_payment_center_liantong()
    {
        tianyi_payment_center_liantong.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                new CardPayThread(getString(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_liantong))).start();
            }
        });
    }

    private void setOnclick_payment_center_shengfutong()
    {
        tianyi_payment_center_shengfutongka.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                new CardPayThread(getString(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_shengfutong))).start();
            }
        });
    }

    private void setOnclick_payment_center_weixin()
    {
        tianyi_payment_center_weixin.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                new Thread()
                {
                    @Override
                    public void run()
                    {
                        loadingDialog.show(getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_is_creating_order")));
                        CreateOrderResult createOrderResult = CreateOrder.createOrder(TianyiPaymentCenterActivity.this, amount, notify_url, extend, product_name, server_name, player_name, cpOrderID, remark, "weixin");

                        if (createOrderResult != null)
                        {
                            loadingDialog.dismiss();
                            final String out_trade_no = createOrderResult.orderid;
                            final String subject = createOrderResult.product_name;
                            final String body = extend;
                            final String total_fee = amount;
                            handler.post(new Runnable()
                            {

                                @Override
                                public void run()
                                {
                                    String localAmount;
                                    if (amount.contains("."))
                                    {
                                        String[] dividearray = amount.split("\\.");
                                        String integerpart = dividearray[0];
                                        String decimalpart = dividearray[1];
                                        int i = java.lang.Integer.valueOf(integerpart) * 100 + java.lang.Integer.valueOf(decimalpart);
                                        localAmount = i + "";
                                    }
                                    else
                                        localAmount = java.lang.Integer.valueOf(amount) * 100 + "";


                                    TianyiWeChatPayCallback tianyiWeChatPayCallback = new TianyiWeChatPayCallback()
                                    {
                                        @Override
                                        public void handSuccess(String orderid)
                                        {
                                            TianyiPay.tianyiPay.getTianyiPayCallback().handSuccess(orderid);
                                            tianyiPaymentCenterActivity.finish();
                                        }

                                        @Override
                                        public void handFail(String orderid)
                                        {

                                        }
                                    };
                                    String mhtOrderNo = out_trade_no;
                                    String mhtOrderBeginTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(new Date());
                                    String orderName = TianyiPaymentCenterActivity.this.product_name;
                                    String orderDetail = TianyiPaymentCenterActivity.this.remark;
                                    String amount = localAmount;
                                    String reserved = "";
                                    mWeChatPayManager.pay(mhtOrderNo, mhtOrderBeginTime, orderName, orderDetail, amount, reserved, tianyiWeChatPayCallback);
                                }
                            });
                        }
                        else
                        {
                            loadingDialog.dismiss();
                            Tool.showToast(handler, TianyiPaymentCenterActivity.this, getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_order_creating_fail")));
                        }
                        super.run();
                    }
                }.start();
            }
        });
    }

    private void setOnclick_payment_center_yl()
    {
        /*tianyi_payment_center_yl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0)
			{
				TianyiUppayCallback tianyiUppayCallback = initiateUppayCallback(TianyiPaymentCenterActivity.this);
				uPpayPlugin = new UPpayPlugin(TianyiPaymentCenterActivity.this, tianyiUppayCallback);
				new Thread(){
					@Override
					public void run()
					{
						loadingDialog.show(getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_is_creating_order")));
						CreateOrderResult createOrderResult = CreateOrder.createOrder(TianyiPaymentCenterActivity.this, amount, notify_url, extend, product_name, server_name, player_name, cpOrderID, remark, "unionpay");
						loadingDialog.dismiss();
						if(createOrderResult!=null)
						{
							JSONObject jsonObject = null;
							String localtn = "";
							try
							{
								jsonObject = new JSONObject(createOrderResult.payment_data);
								localtn = jsonObject.getString("tn");
							}
							catch (Exception e)
							{
								Tool.showToast(handler, TianyiPaymentCenterActivity.this, getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_order_creating_fail")));
								e.printStackTrace();
								return;
							}
							
							uPpayPlugin.setOrderid(createOrderResult.orderid);
							final String tn =localtn;
							
							handler.post(new Runnable() {
								@Override
								public void run()
								{
									uPpayPlugin.exec(tn);
								}
							});
						}
						else
						{
							Tool.showToast(handler, TianyiPaymentCenterActivity.this, getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_order_creating_fail")));
						}
						super.run();
					}
				}.start();
			}
		});*/
    }

    private class CardPayThread extends Thread
    {
        private String cardname;
        private String tip;

        public CardPayThread(String cardname)
        {
            this.cardname = cardname;
            this.tip = getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_card_no_support_tips"));
            this.tip = String.format(tip, amount, cardname);
        }

        @Override
        public void run()
        {
            try
            {
                if (!isCheckAmount())
                {
                    Tool.showToast(handler, TianyiPaymentCenterActivity.this, tip);
                    return;
                }
            }
            catch (Exception e)
            {
                Tool.showToast(handler, TianyiPaymentCenterActivity.this, tip);
                e.printStackTrace();
                return;
            }

            Intent intent = new Intent(TianyiPaymentCenterActivity.this, TianyiCardpayActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("amount", amount);
            intent.putExtra("notify_url", notify_url);
            intent.putExtra("extend", extend);
            intent.putExtra("product_name", product_name);
            intent.putExtra("server_name", server_name);
            intent.putExtra("player_name", player_name);
            intent.putExtra("cpOrderID", cpOrderID);
            intent.putExtra("remark", remark);
            intent.putExtra("cardname", cardname);
            startActivity(intent);
            super.run();
        }

        private boolean isCheckAmount()
        {
            if (cardname.equals(getString(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_dianxin))) && DianXin.isInclude((int) (Float.parseFloat(amount))))
                return true;

            if (cardname.equals(getString(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_yidong))) && Yidong.isInclude((int) (Float.parseFloat(amount))))
                return true;

            if (cardname.equals(getString(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_liantong))) && LianTong.isInclude((int) (Float.parseFloat(amount))))
                return true;

            if (cardname.equals(getString(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_shengdaka))) && ShengDaKa.isInclude((int) (Float.parseFloat(amount))))
                return true;

            if (cardname.equals(getString(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_shengfutong))) && ShengFuTongKa.isInclude((int) (Float.parseFloat(amount))))
                return true;

            if (cardname.equals(getString(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_shengfutongyuleyikatong))) && ShengFuTongKa.isInclude((int) (Float.parseFloat(amount))))
                return true;

            if (cardname.equals(getString(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_tecent))) && Qcoin.isInclude((int) (Float.parseFloat(amount))))
                return true;

            if (cardname.equals(getString(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_tianxiayikatong))) && TianXiaYiCaTong.isInclude((int) (Float.parseFloat(amount))))
                return true;

            if (cardname.equals(getString(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_jiuyouyikatong))) && JiuYouYiCaTong.isInclude((int) (Float.parseFloat(amount))))
                return true;

            if (cardname.equals(getString(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_wanmeiyikatong))) && WanMeiYiCaTong.isInclude((int) (Float.parseFloat(amount))))
                return true;

            if (cardname.equals(getString(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_souhuyikatong))) && SouHuYiCaTong.isInclude((int) (Float.parseFloat(amount))))
                return true;

            if (cardname.equals(getString(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_guangyuyikatong))) && GuangYuYiCaTong.isInclude((int) (Float.parseFloat(amount))))
                return true;

            if (cardname.equals(getString(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_wangyiyikatong))) && WangYiYiCaTong.isInclude((int) (Float.parseFloat(amount))))
                return true;

            if (cardname.equals(getString(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_zongyouyikatong))) && ZongYouYiCaTong.isInclude((int) (Float.parseFloat(amount))))
                return true;

            if (cardname.equals(getString(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_tianhongyikatong))) && TianHongYiCaTong.isInclude((int) (Float.parseFloat(amount))))
                return true;

            if (cardname.equals(getString(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_junwangyikatong))) && JunWangYiCaTong.isInclude((int) (Float.parseFloat(amount))))
                return true;

            if (cardname.equals(getString(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_zhengtouyikatong))) && ZhengTuYiCaTong.isInclude((int) (Float.parseFloat(amount))))
                return true;

            if (cardname.equals(getString(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_lianhuaokka))) && LianHuaOkCa.isInclude((int) (Float.parseFloat(amount))))
                return true;

            return false;
        }
    }

    private void setOnclick_payment_center_yd()
    {
        tianyi_payment_center_yd.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                new CardPayThread(getString(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_yidong))).start();
            }
        });
    }

    private void setOnclick_payment_center_shengdaka()
    {
        tianyi_payment_center_shengdaka.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                new CardPayThread(getString(UIResource.getStringResIDByName(getApplicationContext(), CardString.tianyi_shengdaka))).start();
            }
        });
    }

    private void setOnclickGoback()
    {
        goback.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                TianyiPay.tianyiPay.getTianyiPayCallback().handFail("");
                TianyiPaymentCenterActivity.this.finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            TianyiPay.tianyiPay.getTianyiPayCallback().handFail("");
            TianyiPaymentCenterActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setOnclick_payment_center_alipay()
    {
        tianyi_payment_center_alipay.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View arg0)
            {
                TianyiAlipayCallback tianyiAlipayCallback = initiateAlipayCallback(TianyiPaymentCenterActivity.this);
                final TianyiAlipay tianyiAlipay = new TianyiAlipay(TianyiPaymentCenterActivity.this, tianyiAlipayCallback);
                new Thread()
                {
                    @Override
                    public void run()
                    {
                        loadingDialog.show(getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_is_creating_order")));
                        CreateOrderResult createOrderResult = CreateOrder.createOrder(TianyiPaymentCenterActivity.this, amount, notify_url, extend, product_name, server_name, player_name, cpOrderID, remark, "alipayquick");

                        if (createOrderResult != null)
                        {
                            loadingDialog.dismiss();
                            final String out_trade_no = createOrderResult.orderid;
                            final String subject = createOrderResult.product_name;
                            final String body = extend;
                            final String total_fee = amount;
                            handler.post(new Runnable()
                            {

                                @Override
                                public void run()
                                {
                                    tianyiAlipay.QuickPayment(out_trade_no, subject, body, total_fee);
                                }
                            });
                        }
                        else
                        {
                            loadingDialog.dismiss();
                            Tool.showToast(handler, TianyiPaymentCenterActivity.this, getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_order_creating_fail")));
                        }
                        super.run();
                    }
                }.start();
            }
        });
    }

    private TianyiAlipayCallback initiateAlipayCallback(final Activity activity)
    {
        TianyiAlipayCallback tianyiAlipayCallback = new TianyiAlipayCallback()
        {

            @Override
            public void handSuccess(String orderid)
            {
                TianyiPay.tianyiPay.getTianyiPayCallback().handSuccess(orderid);
                //Tool.showToast(handler, TianyiPaymentCenterActivity.this, getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_pay_success_tips")));
                TianyiPaymentCenterActivity.this.finish();
				
				/*new Thread()
				{
					@Override
					public void run()
					{
						int checkResult=CheckOrder.isOrderPayOK(activity, orderid);
						if(checkResult==CheckStatus.SUCCESS)
						{
							TianyiPay.tianyiPay.getTianyiPayCallback().handSuccess(orderid);
							Tool.showToast(handler, TianyiPaymentCenterActivity.this, getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_pay_success_tips")));
							TianyiPaymentCenterActivity.this.finish();
						}
						else if (checkResult==CheckStatus.NOTCONFIRM)
						{
							TianyiPay.tianyiPay.getTianyiPayCallback().handSuccess(orderid);
							Tool.showToast(handler, TianyiPaymentCenterActivity.this, getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_pay_success_tips")));
							TianyiPaymentCenterActivity.this.finish();
						}
						super.run();
					}
				};*/
            }

            @Override
            public void handFail(String orderid)
            {
                //TianyiPay.tianyiPay.getTianyiPayCallback().handFail(orderid);
                //Tool.showToast(handler, TianyiPaymentCenterActivity.this, getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_pay_fail_tips")));

                //TianyiPaymentCenterActivity.this.finish();
            }
        };
        return tianyiAlipayCallback;
    }
	
	/*private TianyiUppayCallback initiateUppayCallback(final Activity activity)
	{
		TianyiUppayCallback tianyiUppayCallback = new TianyiUppayCallback() {
			
			@Override
			public void handSuccess(String orderid)
			{
				TianyiPay.tianyiPay.getTianyiPayCallback().handSuccess(orderid);
				Tool.showToast(handler, TianyiPaymentCenterActivity.this, getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_pay_success_tips")));
				TianyiPaymentCenterActivity.this.finish();
				
				*//*if(CheckOrder.isOrderPayOK(activity, orderid))
				{
					TianyiPay.tianyiPay.getTianyiPayCallback().handSuccess(orderid);
					Tool.showToast(handler, TianyiPaymentCenterActivity.this, getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_pay_success_tips")));
					TianyiPaymentCenterActivity.this.finish();
				}*//*
			}
			
			@Override
			public void handFail(String orderid)
			{
				//TianyiPay.tianyiPay.getTianyiPayCallback().handFail(orderid);
				Tool.showToast(handler, TianyiPaymentCenterActivity.this, getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_pay_fail_tips")));

                //TianyiPaymentCenterActivity.this.finish();
			}
		};
		return tianyiUppayCallback;
	}*/
}