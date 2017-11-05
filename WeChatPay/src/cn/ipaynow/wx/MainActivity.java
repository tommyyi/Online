package cn.ipaynow.wx;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity
{
    private WeChatPayManager mWeChatPayManager;
    private EditText mAmtET;
    private EditText mReservedET;
    private EditText mOrderDatailET;
    private EditText mOrderNameET;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWeChatPayManager = new WeChatPayManager(this);
        mWeChatPayManager.init();
        initUI();
    }

    private void initUI()
    {
        // 可自定义loading界面
        mAmtET = (EditText) findViewById(R.id.et_amt);
        mReservedET = (EditText) findViewById(R.id.et_resever);
        mOrderNameET = (EditText) findViewById(R.id.et_name);
        mOrderDatailET = (EditText) findViewById(R.id.et_detal);
    }

    /**
     * 触发支付事件
     * @param v
     */
    public void goToPay(View v)
    {
        String orderName = mOrderNameET.getText().toString();
        String amount = mAmtET.getText().toString();
        String orderDetail = mOrderDatailET.getText().toString();
        String reserved = mReservedET.getText().toString();
        String mhtOrderBeginTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(new Date());
        String mhtOrderNo = mhtOrderBeginTime;
        TianyiWeChatPayCallback tianyiWeChatPayCallback = new TianyiWeChatPayCallback()
        {
            @Override
            public void handSuccess(String orderid)
            {

            }

            @Override
            public void handFail(String orderid)
            {

            }
        };
        mWeChatPayManager.pay(mhtOrderNo, mhtOrderBeginTime, orderName, orderDetail, amount, reserved, tianyiWeChatPayCallback);
    }

    @Override
    protected void onDestroy()
    {
        mWeChatPayManager.destroy();
        super.onDestroy();
    }
}