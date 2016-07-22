package com.example.colze;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.colze.bean.LoginBean;
import com.example.colze.utils.HttpUtils;
import com.example.colze.utils.ObjectCacheToFile;
import com.example.colze.utils.pay.MyPayUtil;
import com.example.colze.utils.pay.PayListener;

@SuppressLint("HandlerLeak")
public class BuyActivity extends BaseActivity {
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
		}
	};
	private Timer time = new Timer();
	private TimerTask task = new TimerTask() {
		@Override
		public void run() {
			mHandler.sendMessage(new Message());
		}
	};
	private ViewPager mViewPager;
	private ViewPagerAdapter mViewPagerAdapter;
	private GridView examGridView;
	private ExamGridAdapter adapter;
	private ViewHoldler holdler;
	private List<ImageView> mImageViewList = new ArrayList<ImageView>();
	private int[] imageIdsArr = { R.drawable.buypic1, R.drawable.buypic2,
			R.drawable.buypic3, R.drawable.buypic1, R.drawable.buypic2,
			R.drawable.buypic3 };
	private String buyUrl = "http://app2.gaofy.com:8080/gaofy/uploadBuyInfo";

	private TextView mTextView_Title;
	private LinearLayout mLinearLayout_Buy;
	public static String Point_Key = "pointKey";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buy);
		init();
	}

	@Override
	public void initData() {
		super.initData();
		initImageView();
		mViewPager.setAdapter(mViewPagerAdapter);
		examGridView.setAdapter(adapter);
		examGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				String name = BaseApplication.allTaoBeanPath.get(position);
				BaseApplication.examName = name;
				name += ".json";
				BaseApplication.examData = new HashMap<String, Object>();
				BaseApplication.judgeDifficulty(name);
				BaseApplication.taoBeans = BaseApplication.allTaoBeans
						.get(name);
				startActivity(ReadyActivity.createIntent(BuyActivity.this,
						BaseApplication.taoBeans
								.get(BaseApplication.currentExamLesson), name));
			}
		});
		time.schedule(task, 10000, 10000);
	}

	@Override
	public void initWidget() {
		super.initWidget();
		mViewPager = (ViewPager) findViewById(R.id.view_pager);
		mViewPagerAdapter = new ViewPagerAdapter();
		examGridView = (GridView) findViewById(R.id.examGridView);
		adapter = new ExamGridAdapter();
		mTextView_Title = (TextView) findViewById(R.id.text_title);
		mLinearLayout_Buy = (LinearLayout) findViewById(R.id.buy_layout);
		
		int point = getIntent().getIntExtra(Point_Key, 0);
		if(point==1)
		{
			mTextView_Title.setText("课程");
			mLinearLayout_Buy.setVisibility(View.GONE);
		}
	}

	public void onBuyClick(View v) {
		MyPayUtil pay = new MyPayUtil(BuyActivity.this, new PayListener() {

			@Override
			public void onPaying() {
				Toast.makeText(BuyActivity.this, "支付结果确认中", Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onPaySuccess() {
				Toast.makeText(BuyActivity.this, "支付成功", Toast.LENGTH_SHORT)
						.show();

				if (null != ObjectCacheToFile.getCache("LoginBean",
						LoginBean.class, BuyActivity.this)) {
					LoginBean olderLogin = (LoginBean) ObjectCacheToFile
							.getCache("LoginBean", LoginBean.class,
									BuyActivity.this);
					if ((null != olderLogin) && (null != olderLogin.data)) {
						buyUrl += "?userName=" + olderLogin.data.userName
								+ "&appName=14&point1=1&buyWhat=point1";
						HttpUtils.getPostResult(buyUrl);
					}
				}
			}

			@Override
			public void onPayErr() {
				Toast.makeText(BuyActivity.this, "支付失败", Toast.LENGTH_SHORT)
						.show();

			}
		}, "购买题目", "25元购买题目", "25.00");
		pay.start();
	}

	@Override
	public void onViewClick(View v) {
		super.onViewClick(v);
//		switch (v.getId()) {
//		case R.id.ib_back:
//			time.cancel();
//			task.cancel();
//			finish();
//			break;
//
//		default:
//			break;
//		}
	}

	private void initImageView() {
		for (int i = 0; i < imageIdsArr.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(imageIdsArr[i]);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			mImageViewList.add(imageView);
		}
	}

	private class ViewPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0.equals(arg1);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			position = position % mImageViewList.size();
			container.addView(mImageViewList.get(position));
			return mImageViewList.get(position);
		}
	}

	private class ExamGridAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return BaseApplication.allTaoBeans.size();
		}

		@Override
		public Object getItem(int arg0) {
			return BaseApplication.allTaoBeans.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			if (convertView == null) {
				holdler = new ViewHoldler();
				convertView = LayoutInflater.from(BuyActivity.this).inflate(
						R.layout.read_grid_item, null);
				holdler.btn_item = (Button) convertView
						.findViewById(R.id.btn_item);
				holdler.tv_title_1 = (TextView) convertView
						.findViewById(R.id.tv_title_1);
				holdler.tv_title_2 = (TextView) convertView
						.findViewById(R.id.tv_title_2);
				convertView.setTag(holdler);
			} else {
				holdler = (ViewHoldler) convertView.getTag();
			}
			String name = BaseApplication.allTaoBeanPath.get(position);
			String[] titles = name.split("_");
			holdler.tv_title_1.setText(titles[1].equals("1") ? "初级题"
					: titles[1].equals("2") ? "中级题"
							: titles[1].equals("3") ? "高级题" : "初级题");
			holdler.tv_title_2.setText("练习" + Integer.valueOf(titles[2]));
			holdler.btn_item.setTag(name);
			holdler.btn_item.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String name = (String) v.getTag();
					BaseApplication.examName = name;
					name += ".json";
					BaseApplication.examData = new HashMap<String, Object>();
					BaseApplication.judgeDifficulty(name);
					BaseApplication.initLesson();
					BaseApplication.taoBeans = BaseApplication.allTaoBeans
							.get(name);
					startActivity(ReadyActivity.createIntent(BuyActivity.this,
							BaseApplication.taoBeans
									.get(BaseApplication.currentExamLesson),
							name));
				}
			});
			return convertView;
		}

	}

	private class ViewHoldler {
		public Button btn_item;
		public TextView tv_title_1, tv_title_2;
	}
}
