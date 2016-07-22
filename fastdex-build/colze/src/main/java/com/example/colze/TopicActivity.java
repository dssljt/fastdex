package com.example.colze;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.colze.bean.AnalysisBean;
import com.example.colze.bean.DanTiBean;
import com.example.colze.bean.DanTiBean.Head;
import com.example.colze.bean.DanTiBean.Item;
import com.example.colze.bean.TaoTiBean;
import com.example.colze.bean.TaoTiBean.Body;
import com.example.colze.examsdk.ExamSDK;
import com.example.colze.examsdk.core.Lesson;
import com.example.colze.examsdk.core.SubjecType;
import com.example.colze.examsdk.core.SubjectGroup;
import com.example.colze.utils.AllContacts;
import com.example.colze.utils.Logger;
import com.example.colze.utils.LoggerHashMap;
import com.example.colze.utils.ToolUtils;
import com.example.colze.utils.UrlUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("UseSparseArrays")
@SuppressWarnings("rawtypes")
public class TopicActivity extends BaseActivity {

	/**
	 * 答题模式
	 */
	public static final String TYPE_TOPIC = "type_topic";
	private static final String TAG = TopicActivity.class.getSimpleName();
	private static final int TYPE_COLZE = 1;//填空
	private static final int TYPE_COMPREHENSION = 4;//选择题
	private int topic = 1;

	/**
	 * 当前的试卷内容
	 */
	public static HashMap<Integer, ItemExamData> exam;

	private static final String EXAMBODY = "examBody";
	private static final String TIME = "time";
	private static final String PASSSCORE = "passScore";
	private static final String RIGHT = "right";
	public static final String AVERAGE = "average";
	private static final String POSITION = "position";
	private static final String CURRENTINDEX = "currentIndex";
	public static String matchStr = "";

	/**
	 * 答题
	 */
	private TextView topic_time, topic_content;
	private EditText topic_answer;
	private Button button_next;
	private TimeCount timeCount;
	private float time;
	private Body body;
	private DanTiBean danTiBean;
	private float passScore;

	private HashMap<Integer, String> qusMap;

	private int qusPosition = 1;// 当前题目索引
	private int totalQuses = 1;

	private View menu_option, menu_option_sure, menu_analytical;

	private Item currentItem;
	private LinearLayout ll_qus_title;
	private ImageView image_result;
	private TextView topic_topic;
	private String lessonNo;
	private TextView tv_current_subject;
	DanTiBean.Head danHead;//单题元数据

	private Lesson lesson;
	private SubjectGroup subjectGroup;

	private Button btn_a,btn_b,btn_c,btn_d;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_topic);

		btn_a = (Button) findViewById(R.id.button_a);
		btn_b = (Button) findViewById(R.id.button_b);
		btn_c = (Button) findViewById(R.id.button_c);
		btn_d = (Button) findViewById(R.id.button_d);

		if (topic == 1 && exam != null) {
			exam.clear();
		}
		init();
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case UrlUtils.MethodType.TYPE_GETANALYSIS:
			if (msg.obj != null) {
				AnalysisBean response = (AnalysisBean) msg.obj;
				if (response.status == 1) {
					if (response.data.qu_allCount > 0) {
						int all = response.data.qu_allCount;
						int a = response.data.qu_A_count;
						int b = response.data.qu_B_count;
						int c = response.data.qu_C_count;
						int d = response.data.qu_D_count;
						String content = "选择A的人数占: "
								+ ToolUtils.getStringByFloat(Float
										.valueOf((float) a / all)) + "\n";
						content += "选择B的人数占: "
								+ ToolUtils.getStringByFloat(Float
										.valueOf((float) b / all)) + "\n";
						content += "选择C的人数占: "
								+ ToolUtils.getStringByFloat(Float
										.valueOf((float) c / all)) + "\n";
						content += "选择D的人数占: "
								+ ToolUtils.getStringByFloat(Float
										.valueOf((float) d / all)) + "";
						showToast(content);
					} else {
						showToast("暂无数据");
					}
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
	public void initWidget() {
		super.initWidget();
		topic_time = (TextView) findViewById(R.id.topic_time);
		topic_content = (TextView) findViewById(R.id.topic_content);
		topic_answer = (EditText) findViewById(R.id.topic_answer);
		topic_topic = (TextView) findViewById(R.id.topic_topic);
		button_next = (Button) findViewById(R.id.button_next);
		menu_option = findViewById(R.id.menu_option);
		menu_option_sure = findViewById(R.id.menu_option_sure);
		menu_analytical = findViewById(R.id.menu_analytical);
		ll_qus_title = (LinearLayout) findViewById(R.id.ll_qus_title);
		ll_qus_title = (LinearLayout) findViewById(R.id.ll_qus_title);
		image_result = (ImageView) findViewById(R.id.image_result);
	}

	private void initTopicOption() {
		menu_option.setVisibility(topic == 1 ? View.VISIBLE : View.GONE);
		menu_option_sure.setVisibility(topic == 2 ? View.VISIBLE : View.GONE);
		menu_analytical.setVisibility(topic == 3 ? View.VISIBLE : View.GONE);
		switch (topic) {
		case 1:
		case 3:
			if (qusMap == null) {
				qusMap = new LoggerHashMap<Integer, String>("qusMap");
			}
			if (exam == null) {
				exam = new LoggerHashMap<Integer, ItemExamData>("exam");
			}
			time = getIntent().getFloatExtra(TIME, 0);
			passScore = getIntent().getFloatExtra(PASSSCORE, 0);
			body = (Body) getIntent().getSerializableExtra(EXAMBODY);

			if (body != null) {
				lessonNo = body.getLessonNoNumber();

				lesson = ExamSDK.getLession(Lesson.ID_PERFIX + lessonNo);

				Logger.d("<< lesson: " + lesson);
				if (lesson != null) {
					subjectGroup = lesson.getSubjectGroup();
				}
			}
			qusPosition = getIntent().getIntExtra(POSITION, 1);
			currentPosition = getIntent().getIntExtra(CURRENTINDEX, 1);


			Log.d(TAG, "<<<lessonNo " + lessonNo);
			Log.d(TAG, "<<<< qusPosition: " + qusPosition);
			Log.d(TAG,"<<<< currentPosition: " + currentPosition);
			Log.d(TAG,"<<<< body: " + body.toString());

			if (topic == 3) {
				topic_time.setText("题目解析");
				ll_qus_title.setVisibility(View.VISIBLE);
			}
			/**
			 * 获取所有页面相关数据
			 */
			getAllQus();

			if (isColzeSubject()) {
				//填空
				btn_a.setEnabled(false);
				btn_b.setEnabled(false);
				btn_c.setEnabled(false);
				btn_d.setEnabled(false);

				btn_a.setTextColor(Color.GRAY);
				btn_b.setTextColor(Color.GRAY);
				btn_c.setTextColor(Color.GRAY);
				btn_d.setTextColor(Color.GRAY);

				topic_answer.setEnabled(true);
				topic_answer.setSingleLine(true);
				topic_answer.setText("");
				topic_answer.setHint("请输入恰当的单词");
				tv_current_subject = (TextView)findViewById(R.id.tv_current_subject);
				tv_current_subject.setVisibility(View.VISIBLE);
				topic_answer.addTextChangedListener(new TextWatcher() {
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {

					}

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						daan = s.toString();

						Logger.d("<< onTextChanged: " + daan);
					}

					@Override
					public void afterTextChanged(Editable s) {

					}
				});
			} else {
				topic_answer.setBackgroundDrawable(null);
			}
			/**
			 * 初始化数据
			 */
			initPageData();
			break;

		default:
			break;
		}
	}

	@Override
	public void initData() {
		super.initData();
		topic = getIntent().getIntExtra(TYPE_TOPIC, topic);
		initTopicOption();
	}

	private int currentPosition = 1;
	private boolean hasNext = true;

	//是否是填空题
	public boolean isColzeSubject() {
		try {
			if (topic != 1) {
				return false;
			}
			if (subjectGroup != null && subjectGroup.getSubjecType() == SubjecType.colze) {
				return true;
			}
			if (danHead.Type2.equals("1")) {
				return true;
			}
		} catch (Throwable e) {
			//e.printStackTrace();
		}
		return false;
	}

	private void initPageData() {
		timeCount = new TimeCount(topic_time, (int) time * 1000, 1000);
		String name = qusMap.get(qusPosition);
		if (!ToolUtils.isNullOrEmpter(name)) {
			String qus = "";
			danTiBean = (DanTiBean) BaseApplication.examData.get(name);

			Logger.d("<<<danTiBean: " + danTiBean);
			currentItem = getQusItem(currentPosition);
			if (currentItem == null && qusPosition < totalQuses) {
				if (currentPosition == 1) {
					hasNext = false;
					return;
				}
				currentPosition = 1;
				initPageData();
				return;
			}
			if (currentItem != null) {
				String content = (String) BaseApplication.examData.get(name
						+ "_content");
				if (ToolUtils.isNullOrEmpter(content)) {
					content = "";
				}
				Bitmap bp = (Bitmap) BaseApplication.examData.get(name
						+ "_image");
				if (bp != null) {
					ToolUtils.setImageToTextView(this, topic_content, bp,
							content);
				} else {
					topic_content.setText(content);
				}
				switch (topic) {
				case 1:
					qus = ToolUtils.getQusContent(currentItem.Body);

					if (!isColzeSubject()) {
						topic_answer.setText(qus);
					}
					Logger.d("<<< 设置文字 " + qus);
					break;
				case 3:
					qus = ToolUtils.getQusAns(currentItem.Key);
					if (!isColzeSubject()) {
						topic_answer.setText(qus);
					}

					Logger.d("<<< 设置文字 " + qus);
					break;

				default:
					break;
				}
				if (topic == 3) {
					boolean isRight = getIntent().getBooleanExtra(RIGHT, false);
					image_result.setImageResource(isRight ? R.drawable.right
							: R.drawable.wrong);

					try {
						if ((subjectGroup != null && subjectGroup.getSubjecType() == SubjecType.colze )
								|| (danHead != null && danHead.Type2 != null && danHead.Type2.equals("1"))) {

						} else {
							topic_topic.setText(ToolUtils.getQusContent(currentItem.Body));
						}
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			qusPosition--;
			// showToast("这是最后一题");
			if (topic == 1) {
				startActivity(ResultActivity.createInten(this, getIntent()
						.getStringExtra(AVERAGE)));
			}
			finish();
		}
	}

	private void getAllQus() {
		try {
			//TODO
			Class clazz = TaoTiBean.Body.class;
			//Class clazz = Class.forName("com.example.comprehension.bean.TaoTiBean$Body");
			ArrayList<String> ques = new ArrayList<String>();
			String quePath = "";
			for (int i = 1; i <= Integer.MAX_VALUE; i++) {
				Field field = clazz.getField("Q" + i);
				String name = (String) field.get(body);
				if (ToolUtils.isNullOrEmpter(name)) {
					break;
				} else {
					totalQuses = i;
				}
				ques.add(name);
				name = name.substring(0, name.lastIndexOf("_"));
				BaseApplication.currentDifficulty = Integer.valueOf(name
						.split("_")[1]);
				qusMap.put(i, name);
				if (BaseApplication.examData.get(name) == null) {
					String[] folderIndex = name.split("_");
					String forwordName = AllContacts
							.getPathByType(BaseApplication.currentDifficulty)
							+ lessonNo + "/" + name;
					/**
					 * 题目内容1-5题
					 */
					quePath = forwordName + ".json";
					String content = ToolUtils.readFileByLines(quePath);
					DanTiBean danTiBean = (DanTiBean) ToolUtils.jsStrToJson(
							content, DanTiBean.class);
					if (danTiBean.T.Head != null) {
						danHead = danTiBean.T.Head;
					}
					BaseApplication.examData.put(name, danTiBean);

					/**
					 * 题目
					 */
					String queContentPath = forwordName + ".txt";
					if (ToolUtils.isFileExists(queContentPath)) {
						BaseApplication.examData.put(name + "_content",
								ToolUtils.readFileByLines(queContentPath));
					}
					/**
					 * 题目图片
					 */
					String queImgPath = forwordName + ".png";
					if (ToolUtils.isFileExists(queImgPath)) {
						BaseApplication.examData.put(name + "_image", ToolUtils
								.bytes2Bimap(ToolUtils
										.readFileByBytes(queImgPath)));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		timeCount.start();
	}

	private void setButton(int type) {
		switch (type) {
		case 0:
			((Button) findViewById(R.id.button_a))
					.setBackgroundResource(R.drawable.button_selector_default);
			((Button) findViewById(R.id.button_a)).setTextColor(getResources()
					.getColor(R.color.text_color));
			((Button) findViewById(R.id.button_b))
					.setBackgroundResource(R.drawable.button_selector_default);
			((Button) findViewById(R.id.button_b)).setTextColor(getResources()
					.getColor(R.color.text_color));
			((Button) findViewById(R.id.button_c))
					.setBackgroundResource(R.drawable.button_selector_default);
			((Button) findViewById(R.id.button_c)).setTextColor(getResources()
					.getColor(R.color.text_color));
			((Button) findViewById(R.id.button_d))
					.setBackgroundResource(R.drawable.button_selector_default);
			((Button) findViewById(R.id.button_d)).setTextColor(getResources()
					.getColor(R.color.text_color));
			break;
		case 1:
			((Button) findViewById(R.id.button_a))
					.setBackgroundResource(R.drawable.xuanti_button_click);
			((Button) findViewById(R.id.button_a)).setTextColor(getResources()
					.getColor(R.color.white));
			((Button) findViewById(R.id.button_b))
					.setBackgroundResource(R.drawable.button_selector_default);
			((Button) findViewById(R.id.button_b)).setTextColor(getResources()
					.getColor(R.color.text_color));
			((Button) findViewById(R.id.button_c))
					.setBackgroundResource(R.drawable.button_selector_default);
			((Button) findViewById(R.id.button_c)).setTextColor(getResources()
					.getColor(R.color.text_color));
			((Button) findViewById(R.id.button_d))
					.setBackgroundResource(R.drawable.button_selector_default);
			((Button) findViewById(R.id.button_d)).setTextColor(getResources()
					.getColor(R.color.text_color));
			break;
		case 2:
			((Button) findViewById(R.id.button_a))
					.setBackgroundResource(R.drawable.button_selector_default);
			((Button) findViewById(R.id.button_a)).setTextColor(getResources()
					.getColor(R.color.text_color));
			((Button) findViewById(R.id.button_b))
					.setBackgroundResource(R.drawable.xuanti_button_click);
			((Button) findViewById(R.id.button_b)).setTextColor(getResources()
					.getColor(R.color.white));
			((Button) findViewById(R.id.button_c))
					.setBackgroundResource(R.drawable.button_selector_default);
			((Button) findViewById(R.id.button_c)).setTextColor(getResources()
					.getColor(R.color.text_color));
			((Button) findViewById(R.id.button_d))
					.setBackgroundResource(R.drawable.button_selector_default);
			((Button) findViewById(R.id.button_d)).setTextColor(getResources()
					.getColor(R.color.text_color));
			break;
		case 3:
			((Button) findViewById(R.id.button_a))
					.setBackgroundResource(R.drawable.button_selector_default);
			((Button) findViewById(R.id.button_a)).setTextColor(getResources()
					.getColor(R.color.text_color));
			((Button) findViewById(R.id.button_b))
					.setBackgroundResource(R.drawable.button_selector_default);
			((Button) findViewById(R.id.button_b)).setTextColor(getResources()
					.getColor(R.color.text_color));
			((Button) findViewById(R.id.button_c))
					.setBackgroundResource(R.drawable.xuanti_button_click);
			((Button) findViewById(R.id.button_c)).setTextColor(getResources()
					.getColor(R.color.white));
			((Button) findViewById(R.id.button_d))
					.setBackgroundResource(R.drawable.button_selector_default);
			((Button) findViewById(R.id.button_d)).setTextColor(getResources()
					.getColor(R.color.text_color));
			break;
		case 4:
			((Button) findViewById(R.id.button_a))
					.setBackgroundResource(R.drawable.button_selector_default);
			((Button) findViewById(R.id.button_a)).setTextColor(getResources()
					.getColor(R.color.text_color));
			((Button) findViewById(R.id.button_b))
					.setBackgroundResource(R.drawable.button_selector_default);
			((Button) findViewById(R.id.button_b)).setTextColor(getResources()
					.getColor(R.color.text_color));
			((Button) findViewById(R.id.button_c))
					.setBackgroundResource(R.drawable.button_selector_default);
			((Button) findViewById(R.id.button_c)).setTextColor(getResources()
					.getColor(R.color.text_color));
			((Button) findViewById(R.id.button_d))
					.setBackgroundResource(R.drawable.xuanti_button_click);
			((Button) findViewById(R.id.button_d)).setTextColor(getResources()
					.getColor(R.color.white));
			break;
		default:
			break;
		}
	}

	private String daan = "";

	@Override
	public void onViewClick(View v) {
		ItemExamData examData = exam.get(currentPosition);

		super.onViewClick(v);


//		switch (v.getId()) {
//		case R.id.ib_back:
//
//			break;
//		case R.id.button_a:
//			daan = "A";
//			setButton(1);
//			// setAns("A", false);
//
//			if (topic == 1 && examData != null) {
//				examData.userAns = "A";
//			}
//			break;
//		case R.id.button_b:
//			daan = "B";
//			setButton(2);
//			// setAns("B", false);
//
//			if (topic == 1 && examData != null) {
//				examData.userAns = "B";
//			}
//			break;
//		case R.id.button_c:
//			daan = "C";
//			setButton(3);
//			// setAns("C", false);
//
//			if (topic == 1 && examData != null) {
//				examData.userAns = "C";
//			}
//			break;
//		case R.id.button_d:
//			daan = "D";
//			setButton(4);
//			// setAns("D", false);
//
//			if (topic == 1 && examData != null) {
//				examData.userAns = "D";
//			}
//
//			break;
//		case R.id.button_last:
//		case R.id.button_last_analytical:
//			if (isColzeSubject()) {
//				String userAns = topic_answer.getText().toString();
//				if (topic == 1 && examData != null) {
//					examData.userAns = userAns;
//				}
//			}
//			if (qusPosition == 1) {
//				showToast("已至第一题");
//				if (isColzeSubject()) {
//					tv_current_subject.setText("第" + 1 + "题");
//					topic_answer.setText("");
//				}
//				Logger.d("<<<已至第一题 currentPosition: " + currentPosition);
//			} else {
//				if (hasNext) {
//					qusPosition--;
//					currentPosition--;
//					initPageData();
//				}
//
//				if (isColzeSubject()) {
//					tv_current_subject.setText("第" + currentPosition + "题");
//					topic_answer.setText("");
//				}
//				Logger.d("<<< currentPosition: " + currentPosition);
//			}
//			echoAns();
//			break;
//		case R.id.button_next:
//		case R.id.button_next_analytical:
//			setAns(daan, false);
//			setButton(0);
//			// setAns("", true);
//			if (!ToolUtils.isNullOrEmpter(daan) && hasNext) {
//				qusPosition++;
//				currentPosition++;
//				initPageData();
//			}
//			echoAns();
//			Logger.d("<<< currentPosition: " + currentPosition);
//			break;
//		/**
//		 * 解析
//		 */
//		case R.id.button_translation:
//			//topic_answer.setText(BaseApplication.examData.get(qusMap.get(currentPosition) + "_CH").toString());
//			topic_answer.setText(currentItem.Key.Content + "");
//			break;
//		case R.id.button_vocabulary:
//			//topic_answer.setText(BaseApplication.examData.get(qusMap.get(currentPosition) + "_WD").toString());
//			//词汇
//			topic_answer.setText(currentItem.Key.Word);
//			break;
//		case R.id.button_detailed:
//			topic_answer.setText(currentItem.Key.Point + "");
//			break;
//		case R.id.button_skill:
//			topic_answer.setText(currentItem.Key.Skill + "");
//			break;
//		case R.id.button_analysis:
//			String txt = "";
//			if (!ToolUtils.isNullOrEmpter(danTiBean.T.Head.Txt)) {
//				txt = danTiBean.T.Head.Txt.replaceAll(".txt", "");
//			}
//			if (!ToolUtils.isNullOrEmpter(danTiBean.T.Head.Pic)) {
//				txt = danTiBean.T.Head.Pic.replaceAll(".png", "");
//			}
//			txt += "_Q" + currentPosition;
//			getDataByGet(
//					UrlUtils.getUrl(UrlUtils.MethodName.GETANALYSIS,
//							new String[] {
//									BaseApplication.loginBean.data.userName,
//									txt }),
//					UrlUtils.MethodType.TYPE_GETANALYSIS, handler);
//			break;
//
//		default:
//			break;
//		}
	}

	/**
	 * 显示历史答案
	 */
	private void echoAns() {
		ItemExamData examData = exam.get(currentPosition);
		if (topic != 1 || examData == null || TextUtils.isEmpty(examData.userAns)) {
			return;
		}

		if (isColzeSubject()) {
			topic_answer.setText(examData.userAns);
			topic_answer.setSelection(examData.userAns.length());

			Logger.d("<< 回显: " + currentPosition + " ans: " + examData.userAns);
		} else {
			String userAns = examData.userAns.toLowerCase();

			if (userAns.equals("a")) {
				daan = "A";
				setButton(1);
			} else if (userAns.equals("b")) {
				daan = "B";
				setButton(2);
			} else if (userAns.equals("c")) {
				daan = "C";
				setButton(3);
			} else if (userAns.equals("d")) {
				daan = "D";
				setButton(4);
			}
		}
	}

	private void setAns(String ans, boolean next) {
		if (!hasNext) {
			showToast("缺少试题内容!");
			return;
		}
		if (ans != null) {
			ans = ans.replaceAll(" ","").replaceAll("\\s*", "");
		}
		ItemExamData itemExam = new ItemExamData();
		itemExam.currentIndex = currentPosition;
		itemExam.passScore = passScore;
		itemExam.userAns = ans;
		Logger.d("<< qusPosition" + qusPosition + "答案: " + ans);
		itemExam.item = getQusItem(currentPosition);
		itemExam.sureAns = itemExam.item.Key.Ans;
		itemExam.body = body;
		itemExam.qusPosition = qusPosition;
		itemExam.score = 100 / totalQuses + 100 % totalQuses;
		itemExam.head = danTiBean.T.Head;
		exam.put(qusPosition, itemExam);
		if (!next) {
			qusPosition++;
			currentPosition++;
			initPageData();
		}
		daan = "";

		if (isColzeSubject()) {
			tv_current_subject.setText("第" + currentPosition + "题");
			topic_answer.setText("");
		}

	}

	private Item getQusItem(int position) {
		Item item = null;
		try {
			Class clazz = DanTiBean.DanTi.class;
			//Class clazz = Class.forName("com.example.comprehension.bean.DanTiBean$DanTi");
			Field field = clazz.getField("Q" + position);
			item = (Item) field.get(danTiBean.T);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}

	public static class TimeCount extends CountDownTimer {
		private TextView tv;

		public TimeCount(TextView tv, long millisInFuture,
				long countDownInterval) {
			super(millisInFuture, countDownInterval);
			this.tv = tv;
		}

		@Override
		public void onTick(long millisUntilFinished) {
			tv.setText("剩余时间：" + millisUntilFinished / 1000 / 60 + "分钟"
					+ (millisUntilFinished / 1000) % 60 + "秒");
		}

		@Override
		public void onFinish() {
		}
	}

	public static Intent createIntent(Context context, float time, Body body,
			float passScore, String average) {
		Intent intent = new Intent(context, TopicActivity.class);
		intent.putExtra(TIME, time);
		intent.putExtra(EXAMBODY, body);
		intent.putExtra(PASSSCORE, passScore);
		intent.putExtra(AVERAGE, average);
		return intent;
	}

	public static Intent createIntent(Context context, float time, Body body,
			float passScore) {
		Intent intent = new Intent(context, TopicActivity.class);
		intent.putExtra(TIME, time);
		intent.putExtra(EXAMBODY, body);
		intent.putExtra(PASSSCORE, passScore);
		return intent;
	}

	public static Intent createIntent(Context context, float time, Body body,
			float passScore, int position, int topicType, int currentIndex,
			boolean isRight) {
		Intent intent = new Intent(context, TopicActivity.class);
		intent.putExtra(POSITION, position);
		intent.putExtra(CURRENTINDEX, currentIndex);
		intent.putExtra(TYPE_TOPIC, topicType);
		intent.putExtra(TIME, time);
		intent.putExtra(EXAMBODY, body);
		intent.putExtra(PASSSCORE, passScore);
		intent.putExtra(RIGHT, isRight);
		return intent;
	}

	public static class ItemExamData implements Serializable {
		public float score;// 单题得分
		public String userAns;// 用户答案
		public String sureAns;// 真实答案
		public float passScore;// 合格分数
		public int currentIndex;// 当前题目索引
		public int qusPosition;// 当前题目索引(所有题目)
		public Body body;// 试卷整体
		public Item item;// 题目详解
		public Head head;// 题目内容

		@Override
		public String toString() {
			return "ItemExamData{" +
					"score=" + score +
					", userAns='" + userAns + '\'' +
					", sureAns='" + sureAns + '\'' +
					", passScore=" + passScore +
					", currentIndex=" + currentIndex +
					", qusPosition=" + qusPosition +
					", body=" + body +
					", item=" + item +
					", head=" + head +
					'}';
		}
	}

}
