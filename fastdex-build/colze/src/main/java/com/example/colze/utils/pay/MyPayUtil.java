package com.example.colze.utils.pay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

@SuppressLint("HandlerLeak")
public class MyPayUtil {
	public static final String PARTNER = "2088711339692155";
	public static final String SELLER = "highlyfly@gaofy.com";
	public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANkrT1u+HFByd4reKssvfOj6cqJ/bgW0u7HG5PtjQhJKn+rSKYjS3o0rdKxnrJjcG9FEJHDrXUbGHQSIbtpMJuajcGBQogwP7i1DXEe3E7o6Rz03ZvYkyolFwOUNDUGXSGPB6XYoaGft4RQBe+xmIuyApZz+FW2dZ4gjGw+yTceLAgMBAAECgYB5VKguwoVBMOK29C8GfA6Wf65iOT239GTVCyFNjgQRAh5cbHSGHAH47yltVTLp6DemcCLH78eaTf9SVANS9S7Zruuyrncnjscix1Mcb8tDOF7QZpwI7zMR+MX8KcjDjVcfayGs9oEJ6fHgGN8kYpdC1i+dDP7PqXL8l1bXprptWQJBAPh/kVe0yQ9craLXNA9NNAzqCm4qyVglJAHDDxHs9z6bsvaNtlVXN0w9yZ3Y+faIpXmOX/lNmg/Im2ZqzdxRTmcCQQDfuaBCh32mGSk4gzRl5SP7VNhNzRLUqJNZSdmIyreOOBQGfkfPq0+5NU+ymUhhIeKIhkAcmib2RkqDmTZzD389AkBRNg+f5DtZQ+aTM1WWMpryJYKnImCO6ARudvrz1seutF+2Z/XQyiIOTpsXdROr2FiL81W2OgBBbEQtInaIrCG9AkEAr13G1D92azsvx395FCIACWMhRiLojs8w6P1tSb91EEK+17QmwA7NqCS0uw9R3+l6s39gQ8tFnSfOAseXGavdFQJAYlkT+J9IuHbe8MBpg+kriwlIIk6fF36bokqIwhkZwgMv9LJD72lzVlUOiw8yLjONg6xwV73tgYkxKbFq7GZWxA==";
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDZK09bvhxQcneK3irLL3zo+nKif24FtLuxxuT7Y0ISSp/q0imI0t6NK3SsZ6yY3BvRRCRw611Gxh0EiG7aTCbmo3BgUKIMD+4tQ1xHtxO6Okc9N2b2JMqJRcDlDQ1Bl0hjwel2KGhn7eEUAXvsZiLsgKWc/hVtnWeIIxsPsk3HiwIDAQAB";

	private Activity mContext;
	private PayListener listener;
	private String Title;
	private String Desc;
	private String Price;
	private final int PAY_SUCCESS = 0;
	private final int PAY_ING = 1;
	private final int PAY_ERR = 2;
	private final int PAY_CHECK= 3;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case PAY_SUCCESS:
				if (null != listener) {
					listener.onPaySuccess();
				}
				break;
			case PAY_ING:
				if (null != listener) {
					listener.onPaying();
				}
				break;
			case PAY_ERR:
				if (null != listener) {
					listener.onPayErr();
				}
				break;
			case PAY_CHECK:
				boolean isExist = (Boolean) msg.obj;
				if(isExist)
				{
					pay();
				}else
				{
					Toast.makeText(mContext, "未安装支付宝", 500).show();
				}
				break;	
			default:
				break;
			}
		}
	};

	public MyPayUtil(Activity context, PayListener l,String title,String desc,String price) {
		this.mContext = context;
		this.listener = l;
		this.Title = title;
		this.Desc = desc;
		this.Price = price;
	}

	public void start()
	{
		check();
	}
	private void pay() {
		// 订单
		String orderInfo = getOrderInfo(Title, Desc, Price);
		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		Runnable payRunnable = new Runnable() {
			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(mContext);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo);
				PayResult payResult = new PayResult(result);
				String resultInfo = payResult.getResult();
				String resultStatus = payResult.getResultStatus();
				if (TextUtils.equals(resultStatus, "9000")) {
					mHandler.sendEmptyMessage(PAY_SUCCESS);
				} else {
					if (TextUtils.equals(resultStatus, "8000")) {
						mHandler.sendEmptyMessage(PAY_ING);
					} else {
						mHandler.sendEmptyMessage(PAY_ERR);
					}
				}
			}
		};
		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	private void check() {
		Toast.makeText(mContext, "检查支付环境", 500).show();
		Runnable checkRunnable = new Runnable() {
			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask payTask = new PayTask(mContext);
				// 调用查询接口，获取查询结果
				boolean isExist = payTask.checkAccountIfExist();
				Message msg = new Message();
				msg.what = PAY_CHECK;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};
		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	public String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo(String subject, String body, String price) {

		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm"
				+ "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}
}
