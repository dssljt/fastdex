package com.example.colze.fragment;

import java.util.Date;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.example.colze.BaseApplication;
import com.example.colze.HomeActivity;
import com.example.colze.R;
import com.example.colze.bean.PinYu;
import com.example.colze.utils.AllContacts;
import com.example.colze.utils.HttpUtils;
import com.example.colze.utils.ObjectCacheToFile;
import com.example.colze.utils.Pentagon.PentagonUtil;

public class AlreadyBuyFragment extends Fragment {
	private TextView mTextView_Wubianxing;
	private ImageView mImageView_EveryDay;
	private TextView mTextView_EveryDay;
	private NetworkImageView mImageView_Notice;
	private View view;
	private Context mContext;
	private RequestQueue mQueue = null;
	private ImageLoader mImageLoader;
	private ImageView mImageView_Wubianxing;
	private Button mButton_Wubianxing;
	private boolean isLocal = true;
	private PinYu zuhePinYu;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_alreadybuy, null);
		initView();
		setData();
//		getLineView();
		getLocalView();
		return view;
	}

	private void initView() {
		zuhePinYu = new PinYu();
		mContext = getActivity();
		mTextView_Wubianxing = (TextView) view
				.findViewById(R.id.text_wubianxing);
		mImageView_EveryDay = (ImageView) view
				.findViewById(R.id.image_everyday);
		mTextView_EveryDay = (TextView) view.findViewById(R.id.text_everyday);
		mImageView_Notice = (NetworkImageView) view
				.findViewById(R.id.notice_imageview);
		mImageView_Wubianxing = (ImageView) view
				.findViewById(R.id.image_wubianxing);
		mButton_Wubianxing = (Button) view.findViewById(R.id.button_wubianxing);
		mButton_Wubianxing.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (isLocal) {
					getLineView();
					mButton_Wubianxing.setText("上一次");
				} else {
					getLocalView();
					mButton_Wubianxing.setText("查看综合");
				}
				isLocal = !isLocal;
			}
		});
		mQueue = Volley.newRequestQueue(mContext);
		mImageLoader = new ImageLoader(mQueue, new ImageCache() {
			@Override
			public void putBitmap(String url, Bitmap bitmap) {
			}

			@Override
			public Bitmap getBitmap(String url) {
				return null;
			}
		});
	}

	private void getLocalView() {
		Message message = new Message();
		HomeActivity activity = (HomeActivity) getActivity();
		message.what = 1;
		activity.mHandler.sendMessage(message);
	}

	private void getLineView() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String request = HttpUtils
						.getPostResult("http://app2.gaofy.com:8080/gaofy/getSInfo/"
								+ BaseApplication.loginBean.data.userName
								+ "/14");
				try {
					JSONObject json = new JSONObject(request);
					if (json.opt("status").toString().trim().equals("0")) {
						JSONObject dataJson = json.optJSONObject("data");
						if (null != dataJson) {
							int s1_right = Integer.parseInt(String
									.valueOf(dataJson.opt("s1_right")));
							int s2_right = Integer.parseInt(String
									.valueOf(dataJson.opt("s2_right")));
							int s3_right = Integer.parseInt(String
									.valueOf(dataJson.opt("s3_right")));
							int s4_right = Integer.parseInt(String
									.valueOf(dataJson.opt("s4_right")));
							int s5_right = Integer.parseInt(String
									.valueOf(dataJson.opt("s5_right")));
							int s1_count = Integer.parseInt(String
									.valueOf(dataJson.opt("s1_count")));
							int s2_count = Integer.parseInt(String
									.valueOf(dataJson.opt("s2_count")));
							int s3_count = Integer.parseInt(String
									.valueOf(dataJson.opt("s3_count")));
							int s4_count = Integer.parseInt(String
									.valueOf(dataJson.opt("s4_count")));
							int s5_count = Integer.parseInt(String
									.valueOf(dataJson.opt("s5_count")));
							double s1 = 0.000000;
							if (s1_count != 0) {
								s1 = (double) (s1_right * 10000 / s1_count) / 10000.00000;
							}
							zuhePinYu.cihui = AllContacts
									.getComment((int) (s1 * 100));
							double s2 = 0.000000;
							if (s2_count != 0) {
								s2 = (double) (s2_right * 10000 / s2_count) / 10000.00000;
							}
							zuhePinYu.cizu = AllContacts
									.getComment((int) (s2 * 100));
							double s3 = 0.000000;
							if (s3_count != 0) {
								s3 = (double) (s3_right * 10000 / s3_count) / 10000.00000;
							}
							zuhePinYu.shitai = AllContacts
									.getComment((int) (s3 * 100));
							double s4 = 0.000000;
							if (s4_count != 0) {
								s4 = (double) (s4_right * 10000 / s4_count) / 10000.00000;
							}
							zuhePinYu.juxing = AllContacts
									.getComment((int) (s4 * 100));
							double s5 = 0.000000;
							if (s5_count != 0) {
								s5 = (double) (s5_right * 10000 / s5_count) / 10000.00000;
							}
							zuhePinYu.lijie = AllContacts
									.getComment((int) ((1 - s5) * 100));
							Double[] arr = { s1 * 100, s2 * 100, s3 * 100,
									s4 * 100, (1 - s5) * 100 };
							Message message = new Message();
							HomeActivity activity = (HomeActivity) getActivity();
							message.what = 0;
							message.obj = arr;
							activity.mHandler.sendMessage(message);
						}
					}
				} catch (Exception e) {

				}
			}
		}).start();
	}

	public void updateView(Double[] arr, int type) {
		PentagonUtil ptg = new PentagonUtil(getActivity());
		ptg.drawPentagonWithPointArr(ptg.getPentagonWithPointArr(arr),
				mImageView_Wubianxing, R.drawable.pantage);
		switch (type) {
		case 0:
			mTextView_Wubianxing.setText(zuhePinYu.getString());
			// if (null != ObjectCacheToFile.getCache("averageResultName",
			// String.class, getActivity())) {
			// mTextView_Wubianxing.setText(ObjectCacheToFile
			// .getCache("averageResultName", String.class,
			// getActivity()).toString());
			// } else {
			// mTextView_Wubianxing.setText("合格");
			// }
			break;
		case 1:
			if (null != ObjectCacheToFile.getCache("pinyu", PinYu.class,
					getActivity())) {
				PinYu pinYu = (PinYu) ObjectCacheToFile.getCache("pinyu",
						PinYu.class, getActivity());
				mTextView_Wubianxing.setText(pinYu.getString());
			} else {
				mTextView_Wubianxing.setText("暂未做题");
			}
			// if (null != ObjectCacheToFile.getCache("averageResultName",
			// String.class, getActivity())) {
			// mTextView_Wubianxing.setText(ObjectCacheToFile.getCache(
			// "localResultName", String.class, getActivity())
			// .toString());
			// } else {
			// mTextView_Wubianxing.setText("合格");
			// }
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("deprecation")
	private void setData() {
		Date data = new Date();
		int day = data.getDate();
		mTextView_EveryDay.setText(AllContacts.everyDayTextArr[day - 1]);
		int everyDayImageId = getResources().getIdentifier("day" + day,
				"drawable", mContext.getApplicationInfo().packageName);
		if (everyDayImageId > 0) {
			mImageView_EveryDay.setImageResource(everyDayImageId);
		}
		mImageView_Notice.setImageUrl(
				"http://app.gaofy.com/upload/urlpng/prireading/noticepic.png",
				mImageLoader);
	}
}
