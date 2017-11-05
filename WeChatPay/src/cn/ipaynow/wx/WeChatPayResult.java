package cn.ipaynow.wx;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.ipaynow.wechatpay.plugin.manager.route.dto.ResponseParams;
import com.ipaynow.wechatpay.plugin.manager.route.impl.ReceivePayResult;

public class WeChatPayResult implements ReceivePayResult
{
    private Context mContext;
    private TianyiWeChatPayCallback mTianyiWeChatPayCallback;
    private String mMhtOrderNo;
    private final Handler mHandler;

    public WeChatPayResult(Context context, TianyiWeChatPayCallback tianyiWeChatPayCallback, String mhtOrderNo)
    {
        mContext = context;
        mTianyiWeChatPayCallback = tianyiWeChatPayCallback;
        mMhtOrderNo = mhtOrderNo;
        mHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 支付回调接口实现
     */
    @Override
    public void onIpaynowTransResult(ResponseParams resp)
    {
        if (resp == null)
        {
            return;
        }
        String respCode = resp.respCode;
        String errorCode = resp.errorCode;
        String respMsg = resp.respMsg;
        final StringBuilder temp = new StringBuilder();
        if (respCode.equals("00"))
        {
            temp.append("交易状态:成功");
            mTianyiWeChatPayCallback.handSuccess(mMhtOrderNo);
        }
        else if (respCode.equals("02"))
        {
            temp.append("交易状态:取消");
            mTianyiWeChatPayCallback.handFail(mMhtOrderNo);
        }
        else if (respCode.equals("01"))
        {
            temp.append("交易状态:失败").append("\n").append("错误码:").append(errorCode).append("原因:" + respMsg);
            mTianyiWeChatPayCallback.handFail(mMhtOrderNo);
        }
        else if (respCode.equals("03"))
        {
            temp.append("交易状态:未知").append("\n").append("原因:" + respMsg);
            mTianyiWeChatPayCallback.handFail(mMhtOrderNo);
        }
        else
        {
            temp.append("respCode=").append(respCode).append("\n").append("respMsg=").append(respMsg);
            mTianyiWeChatPayCallback.handFail(mMhtOrderNo);
        }
        Runnable runnable = new Runnable()
        {
            public void run()
            {
                Toast.makeText(mContext, temp.toString(), Toast.LENGTH_LONG).show();
            }
        };
        mHandler.post(runnable);
    }
}