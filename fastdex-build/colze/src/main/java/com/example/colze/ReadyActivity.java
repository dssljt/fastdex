package com.example.colze;

import java.util.ArrayList;

import com.example.colze.bean.ExamInfoBean;
import com.example.colze.bean.TaoTiBean;
import com.example.colze.utils.AllContacts;
import com.example.colze.utils.ObjectCacheToFile;
import com.example.colze.utils.ToolUtils;
import com.example.colze.utils.UrlUtils;
import com.example.colze.utils.bingtu.suanfa;
import com.example.colze.view.PieChart;
import com.example.colze.view.TitleValueColorEntity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ReadyActivity extends BaseActivity implements OnClickListener {

	private static final String TAOTI = "TAOTI";
	private static final String EXAMNAME = "EXAMNAME";

	private TaoTiBean taoTiBean;
	private String examName;

	private PieChart pieChart;
	private TextView tv_title, tv_total_que, tv_difficulty, tv_time,
			tv_passScore, tv_average, collection, title;

	public static ReadyActivity instance = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ready);
		instance = this;
		init();
	}

	@Override
	public void initData() {
		super.initData();
		taoTiBean = (TaoTiBean) getIntent().getSerializableExtra(TAOTI);
		examName = getIntent().getStringExtra(EXAMNAME);
		collection.setOnClickListener(this);
		initHead();
	}

	private void initHead() {
		if (taoTiBean != null && taoTiBean.Head != null) {
			tv_title.setText(taoTiBean.Head.Name);
			tv_total_que.setText(taoTiBean.Head.NumQ);
			tv_difficulty.setText(taoTiBean.Head.Difficulty + "星");
			tv_time.setText(ToolUtils.limitDecimal(taoTiBean.Head.TestTime / 60)
					+ "分钟");
			tv_passScore.setText(taoTiBean.Head.PassScore + "分");
		}

		tv_average.setText("--分");
		title.setText(BaseApplication.currentExamLesson + "");
	}

	public void onViewClick(View view) {
		super.onViewClick(view);
//		switch (view.getId()) {
//		case R.id.ib_back:
//			finish();
//			break;
//		case R.id.ib_topic:
//			if (checkContent()) {
//				startActivity(TopicActivity.createIntent(this,
//						taoTiBean.Head.TestTime, taoTiBean.Body,
//						taoTiBean.Head.PassScore, tv_average.getText()
//								.toString()));
//			} else {
//				showToast("缺少试题内容!");
//			}
//			break;
//
//		default:
//			break;
//		}
	}

	private boolean checkContent() {
		return true;
	}

	public void initWidget() {
		pieChart = (PieChart) findViewById(R.id.pieChart);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_total_que = (TextView) findViewById(R.id.tv_total_que);
		tv_difficulty = (TextView) findViewById(R.id.tv_difficulty);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_passScore = (TextView) findViewById(R.id.tv_passScore);
		tv_average = (TextView) findViewById(R.id.tv_average);
		collection = (TextView) findViewById(R.id.collection);
		title = (TextView) findViewById(R.id.title);
	}

	public static Intent createIntent(Context context, TaoTiBean taoTi,
			String examName) {
		Intent intent = new Intent(context, ReadyActivity.class);
		intent.putExtra(TAOTI, taoTi);
		intent.putExtra(EXAMNAME, examName);
		return intent;
	}

	@Override
	public void doHttp() {
		super.doHttp();
		/**
		 * 获取套题信息
		 */
		examName = examName.substring(examName.lastIndexOf("/") + 1,
				examName.length()).replaceAll(".json", "")
				+ "_L";
		examName += BaseApplication.currentExamLesson.replaceAll("Lesson", "");
		getDataByGet(UrlUtils.getUrl(UrlUtils.MethodName.GETSETINFO,
				new String[] { examName }),
				UrlUtils.MethodType.TYPE_GETSETINFO, handler);
		Log.e("0000", UrlUtils.getUrl(UrlUtils.MethodName.GETSETINFO,
				new String[] { examName }));
	}

	@Override
	public void onClick(View view) {
//		switch (view.getId()) {
//		case R.id.collection:
//			startActivity(TopicCollectionActivity.createIntent(this));
//			break;
//
//		default:
//			break;
//		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case UrlUtils.MethodType.TYPE_GETSETINFO:
			if (msg.obj != null) {
				ExamInfoBean response = (ExamInfoBean) msg.obj;
				if (response.status == 1) {
					double pingjunfen = 0.0;
					int persons = Integer.valueOf(response.data.personNum);
					int scores = Integer.valueOf(response.data.scoreNum);
					if (persons > 0) {
						tv_average.setText(scores / persons + "分");
						pingjunfen = scores / persons;
					} else {
						tv_average.setText(scores + "分");
						pingjunfen = scores;
					}
					ArrayList<TitleValueColorEntity> mData = new ArrayList<TitleValueColorEntity>();
					float fen_90 = (float) suanfa.normal_cdf(90.0, pingjunfen,
							15);
					float fen_75 = (float) suanfa.normal_cdf(75.0, pingjunfen,
							15);
					float fen_60 = (float) suanfa.normal_cdf(60.0, pingjunfen,
							15);
					
					float youxiu = (float) ((1-fen_90)*100.0);
					float lianghao = (float) ((fen_90-fen_75)*100.0);
					float zhong = (float) ((fen_75-fen_60)*100.0);
					float xia = (float) ((fen_60)*100.0);
					
					for (int i = 0; i < 4; i++) {
						mData.add(new TitleValueColorEntity(
								"Ratio",
								i == 0 ? youxiu : i == 1 ? lianghao
										: i == 2 ? zhong
												: i == 3 ? xia : 0,
								this.getResources()
										.getColor(
												i == 0 ? R.color.excellent
														: i == 1 ? R.color.well
																: i == 2 ? R.color.qualified
																		: i == 3 ? R.color.required
																				: R.color.required)));
					}
					pieChart.setData(mData);
					pieChart.postInvalidate();
					ObjectCacheToFile.doCache(AllContacts.testPath
							+ BaseApplication.currentExamLesson + "_"
							+ TopicActivity.AVERAGE, tv_average.getText()
							.toString(), this);
				} else {
					showToast(response.msg + "");
				}
			}
			break;

		default:
			break;
		}
		return super.handleMessage(msg);
	}

}
