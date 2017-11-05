package cn.ipaynow.wx;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.ipaynow.wechatpay.plugin.api.WechatPayPlugin;
import com.ipaynow.wechatpay.plugin.utils.PreSignMessageUtil;
import com.ipaynow.wechatpay.plugin.view.IpaynowLoading;

import java.net.URLEncoder;

/**
 * 子线程获取签名，主线程中调起支付插件
 */
public class PayTool extends AsyncTask<String, Integer, String>
{
    private WechatPayPlugin mWeChatPlugin;
    private IpaynowLoading mLoading;
    private TianyiWeChatPayCallback mTianyiWeChatPayCallback;
    private WeChatPayResult mWeChatPayResult;
    private PreSignMessageUtil mSignUtil;
    private String mStr2sign;

    public PayTool(Context context, WechatPayPlugin weChatPlugin, IpaynowLoading loading, PreSignMessageUtil signUtil, String str2sign, TianyiWeChatPayCallback tianyiWeChatPayCallback)
    {
        mWeChatPlugin = weChatPlugin;
        mLoading = loading;
        mSignUtil = signUtil;
        mTianyiWeChatPayCallback = tianyiWeChatPayCallback;
        mWeChatPayResult = new WeChatPayResult(context, mTianyiWeChatPayCallback, mSignUtil.mhtOrderNo);
        mStr2sign = str2sign;
    }

    protected String doInBackground(String... params)
    {
        String msg = params[0];
        // String signature = HttpUtil.post(GETORDERMESSAGE_URL, msg);
        // 注意：这里只是在前端模拟生成签名，正式发布时请在后台生成签名。
        String appKey = Constant.mAppKey;
        String signature = "mhtSignature=" + Md5Util.md5(msg + "&" + Md5Util.md5(appKey)) + "&mhtSignType=MD5";
        signature = mStr2sign + "&" + signature;
        return signature;
    }

    @SuppressWarnings("deprecation")
    protected void onPostExecute(String signature)
    {
        super.onPostExecute(signature);
        // 如果商户保留域中包含特殊字符：& 请在调起插件时对该字段的value进行一次utf8编码
        String reserved = mSignUtil.mhtReserved;
        if (signature.contains(reserved) && !TextUtils.isEmpty(reserved))
        {
            signature = signature.replace(reserved, URLEncoder.encode(reserved));
        }
        mLoading.dismiss();
        Log.i("收到的请求信息", signature);

        mWeChatPlugin.setCallResultReceiver(mWeChatPayResult);
        mWeChatPlugin.pay(signature);// 传入请求数据
    }
}