package cn.ipaynow.wx;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.widget.Toast;

import com.ipaynow.wechatpay.plugin.api.WechatPayPlugin;
import com.ipaynow.wechatpay.plugin.utils.PreSignMessageUtil;
import com.ipaynow.wechatpay.plugin.view.IpaynowLoading;

public class WeChatPayManager
{
    private WechatPayPlugin weChatPlugin;
    private IpaynowLoading loading;
    private Activity mContext;

    public WeChatPayManager(Activity context)
    {
        mContext = context;
    }

    public void init()
    {
        // 插件初始化
        weChatPlugin = WechatPayPlugin.getInstance().init(mContext);
        loading = weChatPlugin.getDefaultLoading();
    }

    /**
     * 处理支付事件
     * @param mhtOrderNo
     * @param orderName
     * @param orderDetail
     * @param amount
     * @param reserved
     * @param tianyiWeChatPayCallback
     */
    public void pay(String mhtOrderNo, String mhtOrderBeginTime, String orderName, String orderDetail, String amount, String reserved, TianyiWeChatPayCallback tianyiWeChatPayCallback)
    {
        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (weChatIsNotOk(orderDetail, amount, networkInfo))
            return;

        // 创建订单列表
        String appId = Constant.mAppID;
        String notifyUrl = Constant.notifyUrl;
        PreSignMessageUtil signUtil = prePayMessage(mhtOrderNo, mhtOrderBeginTime, appId, orderName, amount, orderDetail, reserved, notifyUrl);
        loading.setLoadingMsg("创建订单...");
        loading.show();

        // 生成待签名串
        String str2Sign = signUtil.generatePreSignMessage();//待签名串
        PayTool payTool = new PayTool(mContext, weChatPlugin, loading, signUtil, str2Sign, tianyiWeChatPayCallback);
        payTool.execute(str2Sign);
    }

    private boolean weChatIsNotOk(String orderDetail, String amount, NetworkInfo networkInfo)
    {
        if (TextUtils.isEmpty(amount))
        {
            Toast.makeText(null, "请输入金额", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (TextUtils.isEmpty(orderDetail))
        {
            Toast.makeText(null, "请输入订单详情", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (networkInfo == null || !networkInfo.isConnected())
        {
            Toast.makeText(null, "请检查手机网络", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    /**
     * 创建订单
     * @param mhtOrderNo 商户订单号
     * @param mhtOrderStartTime 创建订单时间
     * @param appid
     * @param orderName
     * @param orderAmount
     * @param orderDetail
     * @param reserved
     * @param notifyUrl
     */
    private PreSignMessageUtil prePayMessage(String mhtOrderNo, String mhtOrderStartTime, String appid, String orderName, String orderAmount, String orderDetail, String reserved, String notifyUrl)
    {
        PreSignMessageUtil preSignMessageUtil = new PreSignMessageUtil();//待签名串生成工具
        preSignMessageUtil.appId = appid;
        preSignMessageUtil.mhtOrderNo = mhtOrderNo;
        preSignMessageUtil.mhtOrderName = orderName;
        preSignMessageUtil.mhtOrderAmt = orderAmount;
        preSignMessageUtil.mhtOrderDetail = orderDetail;
        preSignMessageUtil.mhtOrderStartTime = mhtOrderStartTime;
        preSignMessageUtil.mhtReserved = reserved;
        preSignMessageUtil.notifyUrl = notifyUrl;
        preSignMessageUtil.mhtOrderType = "01";
        preSignMessageUtil.mhtCurrencyType = "156";
        preSignMessageUtil.mhtOrderTimeOut = "3600";
        preSignMessageUtil.mhtCharset = "UTF-8";
        preSignMessageUtil.payChannelType = "13";
        preSignMessageUtil.consumerId = "456123";
        preSignMessageUtil.consumerName = "yuyang";
        preSignMessageUtil.mhtLimitPay = "no_credit";

        return preSignMessageUtil;
    }

    public void destroy()
    {
        weChatPlugin.onActivityDestroy();
    }
}