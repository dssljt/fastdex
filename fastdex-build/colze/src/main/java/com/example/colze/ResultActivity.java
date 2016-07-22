package com.example.colze;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.colze.TopicActivity.ItemExamData;
import com.example.colze.bean.ErrTableBean;
import com.example.colze.bean.ExamLocalBean;
import com.example.colze.bean.PinYu;
import com.example.colze.bean.TaoTiBean;
import com.example.colze.bean.UploadBean;
import com.example.colze.utils.AllContacts;
import com.example.colze.utils.ObjectCacheToFile;
import com.example.colze.utils.ToolUtils;
import com.example.colze.utils.UrlUtils;
import com.example.colze.utils.Pentagon.PentagonUtil;
import com.example.colze.utils.bingtu.suanfa;
import com.example.colze.view.MySeekBar;

@SuppressLint("SimpleDateFormat")
public class ResultActivity extends BaseActivity implements OnClickListener {

	private MySeekBar seekbar, seekbar2, seekbar3;
	private ListView lv_result;
	private ResultAdapter adapter;

	private TextView tv_total_score, tv_comment, tv_reset;

	private float score, passScore, averageScore;
	/**
	 * s1-s5计算
	 */
	private int s1, s2, s3, s4, s5, s1_right, s2_right, s3_right, s4_right,
			s5_right;
	private String quResult, set_id, result;
	private ExamLocalBean examLocalBean;
	private TaoTiBean taoTiBean;
	private TextView mTextView_Jibailv;
	private ImageView mImageView_Wubianxing;
	private TextView mTextView_Pinyu;
	private String pinyuStr = "";

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case UrlUtils.MethodType.TYPE_UPLOADRESULT:
			if (msg.obj != null) {
				UploadBean response = (UploadBean) msg.obj;
				if (response.status == 1) {
					System.out.println(response);
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		if (null != ReadyActivity.instance) {
			ReadyActivity.instance.finish();
			ReadyActivity.instance = null;
		}
		init();
	}

	public void initWidget() {
		seekbar = (MySeekBar) findViewById(R.id.seekbar_1);
		seekbar2 = (MySeekBar) findViewById(R.id.seekbar_2);
		seekbar3 = (MySeekBar) findViewById(R.id.seekbar_3);
		lv_result = (ListView) findViewById(R.id.lv_result);
		tv_total_score = (TextView) findViewById(R.id.tv_total_score);
		tv_comment = (TextView) findViewById(R.id.tv_comment);
		tv_reset = (TextView) findViewById(R.id.tv_reset);
		mTextView_Jibailv = (TextView) findViewById(R.id.jibailv);
		mImageView_Wubianxing = (ImageView) findViewById(R.id.image_wubianxing);
		mTextView_Pinyu = (TextView) findViewById(R.id.text_pinyu);

	}

	@Override
	public void onViewClick(View v) {
		super.onViewClick(v);
		if (v.getId() == R.id.ib_back) {
			finish();
		}
	}

	@Override
	public void doHttp() {
		super.doHttp();

		/**
		 * update
		 */
		update();
	}

	private void update() {
		// List<NameValuePair> paramList = new ArrayList<NameValuePair>();
		// paramList.add(new BasicNameValuePair("userName",
		// BaseApplication.loginBean.data.userName));
		// paramList
		// .add(new BasicNameValuePair("appname", ToolUtils.getAppInfo()));
		// paramList.add(new BasicNameValuePair("set_id", set_id));
		// paramList.add(new BasicNameValuePair("score", score + ""));
		// paramList.add(new BasicNameValuePair("s1", s1 + ""));
		// paramList.add(new BasicNameValuePair("s2", s2 + ""));
		// paramList.add(new BasicNameValuePair("s3", s3 + ""));
		// paramList.add(new BasicNameValuePair("s4", s4 + ""));
		// paramList.add(new BasicNameValuePair("s5", s5 + ""));
		// paramList.add(new BasicNameValuePair("s1_right", s1_right + ""));
		// paramList.add(new BasicNameValuePair("s2_right", s2_right + ""));
		// paramList.add(new BasicNameValuePair("s3_right", s3_right + ""));
		// paramList.add(new BasicNameValuePair("s4_right", s4_right + ""));
		// paramList.add(new BasicNameValuePair("s5_right", s5_right + ""));
		// paramList.add(new BasicNameValuePair("quResult", quResult));
		// getDataByPost(UrlUtils.getUrl(UrlUtils.MethodName.UPLOADRESULT),
		// UrlUtils.MethodType.TYPE_UPLOADRESULT, handler, paramList);
		String url;
		try {
			url = "http://app2.gaofy.com:8080/gaofy/uploadResult.do?userName="
					+ BaseApplication.loginBean.data.userName + "&appName="
					+ Config.appName
					+ "&set_id=" + set_id + "&score=" + (int) score
					+ "&result="
					+ URLEncoder.encode(AllContacts.getComment(score), "UTF-8")
					+ "&s1=" + s1 + "&s2=" + s2 + "&s3=" + s3 + "&s4=" + s4
					+ "&s5=" + s5 + "&s1_right=" + s1_right + "&s2_right="
					+ s2_right + "&s3_right=" + s3_right + "&s4_right="
					+ s4_right + "&s5_right=" + s5_right + "&quResult="
					+ quResult;
			getDataByGet(url, UrlUtils.MethodType.TYPE_UPLOADRESULT, handler);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void initData() {
		super.initData();
		score = getScore();
		tv_total_score.setText(score + "");
		tv_comment.setText(AllContacts.getComment(score));
		passScore = TopicActivity.exam.get(1).passScore;
		String average = String.valueOf(ObjectCacheToFile.getCache(
				AllContacts.testPath + BaseApplication.currentExamLesson + "_"
						+ TopicActivity.AVERAGE, String.class, this));
		if (average != null) {
			average = average.replaceAll("分", "");
		}
		if (!ToolUtils.isNullOrEmpter(average) && !"--".equals(average)) {
			averageScore = Float.valueOf(average);
		} else {
			averageScore = 0;
		}
		mTextView_Jibailv.setText((int) (suanfa.normal_cdf(score, averageScore,
				15) * 100.0) + "%的学生");
		seekbar.setThumb((int) score);
		seekbar2.setThumb((int) averageScore);
		seekbar3.setThumb((int) passScore);
		adapter = new ResultAdapter();
		lv_result.setAdapter(adapter);
		tv_reset.setOnClickListener(this);
		/**
		 * 当前试卷做完标示
		 */
		BaseApplication.taoBeans.get(BaseApplication.currentExamLesson).hasRead = true;
		/**
		 * 保存试卷
		 */
		ObjectCacheToFile
				.doCache(AllContacts.testPath
						+ BaseApplication.currentExamLesson,
						BaseApplication.taoBeans
								.get(BaseApplication.currentExamLesson), this);
		ObjectCacheToFile.doCache(AllContacts.testPath
				+ BaseApplication.currentExamLesson + "_"
				+ TopicActivity.AVERAGE, averageScore, this);
		set_id = BaseApplication.examName.substring(
				BaseApplication.examName.lastIndexOf("/") + 1,
				BaseApplication.examName.length());
		taoTiBean = BaseApplication.taoBeans
				.get(BaseApplication.currentExamLesson);
		result = AllContacts.getComment(score);
		new Thread(new Runnable() {

			@Override
			public void run() {
				Class clazz = null;
				try {
					//TODO
					clazz = TaoTiBean.Body.class;
					//clazz = Class.forName("com.example.comprehension.bean.TaoTiBean$Body");
					for (int i = 1; i <= Integer.MAX_VALUE; i++) {
						Field field = clazz.getField("Q" + i);
						String name = (String) field.get(taoTiBean.Body);
						if (ToolUtils.isNullOrEmpter(name)) {
							break;
						} else {
							quResult += (name
									+ "-"
									+ (TopicActivity.exam.get(i).sureAns
											.equals(TopicActivity.exam.get(i).userAns) ? 1
											: 0) + "-"
									+ TopicActivity.exam.get(i).userAns + ",");
						}
					}
				} catch (Exception e) {

				}
				quResult = quResult.substring(0, quResult.length() - 1)
						.replaceAll("null", "");
			}
		}).start();
		/**
		 * 记录错误题
		 */
		Set<Integer> keys = TopicActivity.exam.keySet();
		Iterator it = keys.iterator();
		ExamLocalBean localBean = new ExamLocalBean();
		ArrayList<ErrTableBean> beans = new ArrayList<ErrTableBean>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		while (it.hasNext()) {
			ErrTableBean err = new ErrTableBean();
			ItemExamData item = TopicActivity.exam.get(it.next());
			s1 += item.item.Key.S1;
			s2 += item.item.Key.S2;
			s3 += item.item.Key.S3;
			s4 += item.item.Key.S4;
			s5 += item.item.Key.S5;
			err.currentIndex = item.currentIndex;
			err.qusPosition = item.qusPosition;
			err.difficulty = BaseApplication.currentDifficulty;
			err.examLesson = BaseApplication.currentExamLesson;
			err.examPath = BaseApplication.currentExam;
			err.time = sdf.format(new Date(System.currentTimeMillis()));
			err.testCenter = item.item.Key.Point;
			err.source = BaseApplication.currentExamLesson;
			err.body = item.body;
			err.item = item.item;
			err.head = item.head;
			if (!item.userAns.equals(item.sureAns)) {
				beans.add(err);
				s1_right += 0;
				s2_right += 0;
				s3_right += 0;
				s4_right += 0;
				s5_right += 0;
			} else {
				s1_right += item.item.Key.S1;
				s2_right += item.item.Key.S2;
				s3_right += item.item.Key.S3;
				s4_right += item.item.Key.S4;
				s5_right += item.item.Key.S5;
			}
		}
		final PinYu pinyu = new PinYu();
		pinyuStr += "1.";
		double s1_result = 0.000000;
		if (s1 != 0) {
			s1_result = (double) (s1_right * 100 / s1) / 100.0;
		}
		pinyu.cihui = AllContacts.getComment((float) (s1_result * 100.0));
		if (s1_result >= 0.9) {
			pinyuStr += "你的词汇能力非常棒!请保持.";
		} else if (s1_result >= 0.75) {
			pinyuStr += "词汇能力较好,能准确理解词义,词性的选择和运用.多练习仍有提高的空间.";

		} else if (s1_result >= 0.5) {
			pinyuStr += "词汇需提高.注意巩固词汇基础,加强对词的用法及内在含义的理解.";
		} else {
			pinyuStr += "词汇能力差,急待提高.建议常背单词，迅速提高词汇量.";
		}
		pinyuStr += "\r\n\n";
		pinyuStr += "2.";
		double s2_result = 0.000000;
		if (s2 != 0) {
			s2_result = (double) (s2_right * 100 / s2) / 100.0;
		}
		pinyu.cizu = AllContacts.getComment((float) (s2_result * 100.0));
		if (s2_result >= 0.9) {
			pinyuStr += "单句理解是你的强项！";
		} else if (s2_result >= 0.75) {
			pinyuStr += "较好地掌握了句意和单句结构的知识点。建议多练习，进一步提高正确率！";
		} else if (s2_result >= 0.5) {
			pinyuStr += "需要加强单句结构理解，并熟练运用组词成句的能力。";
		} else {
			pinyuStr += "单句理解是你的弱项。或许操练不够，建议多做练习。";
		}
		pinyuStr += "\r\n\n";
		pinyuStr += "3.";
		double s3_result = 0.000000;
		if (s3 != 0) {
			s3_result = (double) (s3_right * 100 / s3) / 100.0;
		}
		pinyu.shitai = AllContacts.getComment((float) (s3_result * 100.0));
		if (s3_result >= 0.9) {
			pinyuStr += "上下文理解是你的强项！";
		} else if (s3_result >= 0.75) {
			pinyuStr += "阅读理解能力较好。建议平时广泛阅读，积累更多词汇，英语理解能力会更加提高多做练习，记住错题原因。";
		} else if (s3_result >= 0.5) {
			pinyuStr += "理解能力略显薄弱。若能精读课文，背熟单词，理解题型将会大大提高。";
		} else {
			pinyuStr += "理解类题型回答正确率非常低。需要尽快总结错题原因并提高。";
		}
		pinyuStr += "\r\n\n";
		pinyuStr += "4.";
		double s4_result = 0.000000;
		if (s4 != 0) {
			s4_result = (double) (s4_right * 100 / s4) / 100.0;
		}
		pinyu.juxing = AllContacts.getComment((float) (s4_result * 100.0));
		if (s4_result >= 0.9) {
			pinyuStr += "你已学会融会贯通的理解全文！";
		} else if (s4_result >= 0.75) {
			pinyuStr += "较好地掌握了全文的结构及文章的重心，但离优秀仍有一步之遥，加油！";
		} else if (s4_result >= 0.5) {
			pinyuStr += "透过字面意义推测作者弦外之音的能力尚需提高。建议着眼于全文结构，扩大阅读量。";
		} else {
			pinyuStr += "总结归纳文章及分析判断是你的短板，请加强相关知识点的练习。";
		}
		pinyuStr += "\r\n\n";
		pinyuStr += "5.";
		double s5_result = 0.000000;
		if (s5 != 0) {
			s5_result = (double) (s5_right * 100 / s5) / 100.0;
		}
		pinyu.lijie = AllContacts.getComment((float)(s5_result * 100.0));
		if (s5_result >= 0.9) {
			pinyuStr += "你的阅读已经达到了神速！";
		} else if (s5_result >= 0.75) {
			pinyuStr += "你的阅读速度尚可，若需达到神速，还要继续努力练习。";
		} else if (s5_result >= 0.5) {
			pinyuStr += "还好没有超时，请养成良好的阅读习惯，加强训练。";
		} else {
			pinyuStr += "阅读速度已超时，简直比蜗牛还要慢！";
		}
		pinyuStr += "\r\n";
		mTextView_Pinyu.setText(pinyuStr);
		pinyuStr = "";
		final Double[] localArr = { s1_result * 100, s2_result * 100,
				s3_result * 100, s4_result * 100, (1 - s5_result) * 100 };
		PentagonUtil ptg = new PentagonUtil(this);
		ptg.drawPentagonWithPointArr(ptg.getPentagonWithPointArr(localArr),
				mImageView_Wubianxing, R.drawable.pantage);
		localBean.errBeans = beans;
		examLocalBean = (ExamLocalBean) ObjectCacheToFile.getCache(
				AllContacts.EXAMLOCALBEAN, ExamLocalBean.class, this);
		if (examLocalBean != null) {
			examLocalBean.errBeans.addAll(examLocalBean.errBeans);
		} else {
			examLocalBean = localBean;
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				String arrStr = "";
				for (int i = 0; i < localArr.length; i++) {
					if (i == 0) {
						arrStr += localArr[i];
					} else {
						arrStr += "," + localArr[i];
					}
				}
				ObjectCacheToFile.doCache("localArr", arrStr, ResultActivity.this);
				ObjectCacheToFile.doCache("localResultName", AllContacts.getComment(score), ResultActivity.this);
				ObjectCacheToFile.doCache("averageResultName", AllContacts.getComment(averageScore), ResultActivity.this);
				ObjectCacheToFile.doCache(AllContacts.EXAMLOCALBEAN, examLocalBean, ResultActivity.this);
				ObjectCacheToFile.doCache("pinyu", pinyu, ResultActivity.this);
			}
		}).start();

		/**
		 * 切换下一试卷
		 */
		BaseApplication.nextExam();
	}

	private float getScore() {
		float score = 0;
		for (int i = 1; i <= TopicActivity.exam.size(); i++) {
			if (TopicActivity.exam.get(i).sureAns.equals(TopicActivity.exam
					.get(i).userAns)) {
				score += TopicActivity.exam.get(i).score;
			}
		}
		return score;
	}

	@SuppressLint("ViewHolder")
	private class ResultAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (TopicActivity.exam == null && TopicActivity.exam.size() == 0) {
				return 0;
			}
			return TopicActivity.exam.size();
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View conventView, ViewGroup arg2) {
			View view = layoutInflater.inflate(R.layout.result_item, null);
			TextView tv_index = (TextView) view.findViewById(R.id.tv_index);
			TextView tv_type = (TextView) view.findViewById(R.id.tv_type);
			TextView tv_userAns = (TextView) view.findViewById(R.id.tv_userAns);
			TextView tv_sureAns = (TextView) view.findViewById(R.id.tv_sureAns);
			Button btn_search = (Button) view.findViewById(R.id.btn_search);
			ItemExamData item = TopicActivity.exam.get(position + 1);
			tv_index.setText((position + 1) + "");
			tv_type.setText(item.sureAns.equals(item.userAns) ? "√" : "×");
			tv_type.setTextColor(!item.sureAns.equals(item.userAns) ? Color
					.parseColor("#BE211A") : Color.parseColor("#00ff00"));
			tv_userAns.setText(item.userAns);
			tv_sureAns.setText(item.sureAns);
			btn_search.setTag(item);
			btn_search.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ItemExamData item = (ItemExamData) v.getTag();
					startActivity(TopicActivity.createIntent(
							ResultActivity.this, 0, item.body, item.passScore,
							item.qusPosition, 3, item.currentIndex,
							item.sureAns.equals(item.userAns) ? true : false));
				}
			});
			return view;
		}

	}

	public static Intent createInten(Context context, String average) {
		return new Intent(context, ResultActivity.class);
	}

	@Override
	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.tv_reset:
//			TaoTiBean item = BaseApplication.taoBeans
//					.get(BaseApplication.currentExamLesson);
//			startActivity(TopicActivity.createIntent(this, item.Head.TestTime,
//					item.Body, item.Head.PassScore,
//					getIntent().getStringExtra(TopicActivity.AVERAGE)));
//			finish();
//			break;
//
//		default:
//			break;
//		}
	}

}
