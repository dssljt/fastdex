package com.example.colze;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.colze.bean.AnalysisBean;
import com.example.colze.bean.DanTiBean;
import com.example.colze.bean.ErrTableBean;
import com.example.colze.bean.ExamLocalBean;
import com.example.colze.bean.TaoTiBean;
import com.example.colze.utils.AllContacts;
import com.example.colze.utils.ObjectCacheToFile;
import com.example.colze.utils.ToolUtils;
import com.example.colze.utils.UrlUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CorrectionAnalysisActivity extends BaseActivity {

	private static final String TOSEE = "toSee";
	private static final String POSITION = "position";
	private List<ErrTableBean> errTables = new ArrayList<ErrTableBean>();
	private ExamLocalBean examLocalBean;

	private LinearLayout ll_qus_title;
	private ImageView image_result;
	private View menu_option, menu_analytical, menu_option_sure;
	private TextView topic_topic, topic_time, topic_content, topic_answer;
	private int currentPosition, total;
	private HashMap<Integer, String> names;
	private ArrayList<Integer> needRemove;

	private boolean toSee = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_correction_analysis);
		init();
	}

	@Override
	public void initData() {
		super.initData();
		toSee = getIntent().getBooleanExtra(TOSEE, false);
		names = new HashMap<Integer, String>();
		needRemove = new ArrayList<Integer>();
		examLocalBean = (ExamLocalBean) ObjectCacheToFile.getCache(
				AllContacts.EXAMLOCALBEAN, ExamLocalBean.class, this);
		if (examLocalBean != null) {
			if(toSee){
				errTables.addAll(examLocalBean.hasDoBeans);
			}else{
				errTables.addAll(examLocalBean.errBeans);
			}
		} else {
			errTables = new ArrayList<ErrTableBean>();
		}
		if (toSee) {
			ll_qus_title.setVisibility(View.VISIBLE);
			menu_analytical.setVisibility(View.VISIBLE);
			menu_option_sure.setVisibility(View.GONE);
			findViewById(R.id.button_last_analytical).setVisibility(View.VISIBLE);
		}
		/**
		 * 头部
		 */
		total = errTables.size();
		currentPosition = getIntent().getIntExtra(POSITION, 0);
		postData();
	}

	private void postData() {
		ErrTableBean item = errTables.get(currentPosition);
		topic_time.setText((currentPosition+1) + "/" + total);
		setItemTitle(item, item.currentIndex, topic_content);
		topic_topic.setText(item.item.Body.Sub);
		topic_answer.setText(ToolUtils.getQusContent(item.item.Body));
	}

	private void setItemTitle(ErrTableBean item, int currentIndex,
			TextView topic_content) {
		Class clazz;
		String name = "";
		String quePath = "";
		try {
			//TODO
			clazz = TaoTiBean.Body.class;
			//clazz = Class.forName("com.example.comprehension.bean.TaoTiBean$Body");
			Field field = clazz.getField("Q" + currentIndex);
			name = (String) field.get(item.body);
			name = name.substring(0, name.lastIndexOf("_"));
			names.put(currentPosition, name);
			BaseApplication.currentDifficulty = Integer
					.valueOf(name.split("_")[1]);
			if (BaseApplication.examData.get(name) == null) {
				String[] folderIndex = name.split("_");
				String forwordName = AllContacts
						.getPathByType(BaseApplication.currentDifficulty)
						+ Integer.valueOf(folderIndex[3]) + "/" + name;
				/**
				 * 题目内容1-5题
				 */
				quePath = forwordName + ".json";
				String content = ToolUtils.readFileByLines(quePath);
				DanTiBean danTiBean = (DanTiBean) ToolUtils.jsStrToJson(
						content, DanTiBean.class);
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
					BaseApplication.examData
							.put(name + "_image", ToolUtils
									.bytes2Bimap(ToolUtils
											.readFileByBytes(queImgPath)));
				}
//				/**
//				 * 题目译文
//				 */
//				String queCh = forwordName + "_CH.txt";
//				if (ToolUtils.isFileExists(queCh)) {
//					BaseApplication.examData.put(name + "_CH",
//							ToolUtils.readFileByLines(queCh));
//				}
//				/**
//				 * 题目关键字
//				 */
//				String queWD = forwordName + "_WD.txt";
//				if (ToolUtils.isFileExists(queWD)) {
//					BaseApplication.examData.put(name + "_WD",
//							ToolUtils.readFileByLines(queWD));
//				}
			}
			String content = (String) BaseApplication.examData.get(name
					+ "_content");
			if (ToolUtils.isNullOrEmpter(content)) {
				content = "";
			}
			Bitmap bp = (Bitmap) BaseApplication.examData.get(name + "_image");
			if (bp != null) {
				ToolUtils.setImageToTextView(this, topic_content, bp, content);
			} else {
				topic_content.setText(content);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initWidget() {
		super.initWidget();
		ll_qus_title = (LinearLayout) findViewById(R.id.ll_qus_title);
		image_result = (ImageView) findViewById(R.id.image_result);
		topic_topic = (TextView) findViewById(R.id.topic_topic);
		topic_time = (TextView) findViewById(R.id.topic_time);
		topic_content = (TextView) findViewById(R.id.topic_content);
		topic_answer = (TextView) findViewById(R.id.topic_answer);
		menu_option_sure = findViewById(R.id.menu_option_sure);
		menu_analytical = findViewById(R.id.menu_analytical);
		menu_option = findViewById(R.id.menu_option);
		findViewById(R.id.button_last_analytical).setVisibility(View.GONE);
	}

	private String userAns = "";
	
	private void setButton(int type) {
		switch (type) {
		case 0:
			((Button) findViewById(R.id.button_aa))
					.setBackgroundResource(R.drawable.button_selector_default);
			((Button) findViewById(R.id.button_aa)).setTextColor(getResources()
					.getColor(R.color.text_color));
			((Button) findViewById(R.id.button_bb))
					.setBackgroundResource(R.drawable.button_selector_default);
			((Button) findViewById(R.id.button_bb)).setTextColor(getResources()
					.getColor(R.color.text_color));
			((Button) findViewById(R.id.button_cc))
					.setBackgroundResource(R.drawable.button_selector_default);
			((Button) findViewById(R.id.button_cc)).setTextColor(getResources()
					.getColor(R.color.text_color));
			((Button) findViewById(R.id.button_dd))
					.setBackgroundResource(R.drawable.button_selector_default);
			((Button) findViewById(R.id.button_dd)).setTextColor(getResources()
					.getColor(R.color.text_color));
			break;
		case 1:
			((Button) findViewById(R.id.button_aa))
					.setBackgroundResource(R.drawable.xuanti_button_click);
			((Button) findViewById(R.id.button_aa)).setTextColor(getResources()
					.getColor(R.color.white));
			((Button) findViewById(R.id.button_bb))
					.setBackgroundResource(R.drawable.button_selector_default);
			((Button) findViewById(R.id.button_bb)).setTextColor(getResources()
					.getColor(R.color.text_color));
			((Button) findViewById(R.id.button_cc))
					.setBackgroundResource(R.drawable.button_selector_default);
			((Button) findViewById(R.id.button_cc)).setTextColor(getResources()
					.getColor(R.color.text_color));
			((Button) findViewById(R.id.button_dd))
					.setBackgroundResource(R.drawable.button_selector_default);
			((Button) findViewById(R.id.button_dd)).setTextColor(getResources()
					.getColor(R.color.text_color));
			break;
		case 2:
			((Button) findViewById(R.id.button_aa))
					.setBackgroundResource(R.drawable.button_selector_default);
			((Button) findViewById(R.id.button_aa)).setTextColor(getResources()
					.getColor(R.color.text_color));
			((Button) findViewById(R.id.button_bb))
					.setBackgroundResource(R.drawable.xuanti_button_click);
			((Button) findViewById(R.id.button_bb)).setTextColor(getResources()
					.getColor(R.color.white));
			((Button) findViewById(R.id.button_cc))
					.setBackgroundResource(R.drawable.button_selector_default);
			((Button) findViewById(R.id.button_cc)).setTextColor(getResources()
					.getColor(R.color.text_color));
			((Button) findViewById(R.id.button_dd))
					.setBackgroundResource(R.drawable.button_selector_default);
			((Button) findViewById(R.id.button_dd)).setTextColor(getResources()
					.getColor(R.color.text_color));
			break;
		case 3:
			((Button) findViewById(R.id.button_aa))
					.setBackgroundResource(R.drawable.button_selector_default);
			((Button) findViewById(R.id.button_aa)).setTextColor(getResources()
					.getColor(R.color.text_color));
			((Button) findViewById(R.id.button_bb))
					.setBackgroundResource(R.drawable.button_selector_default);
			((Button) findViewById(R.id.button_bb)).setTextColor(getResources()
					.getColor(R.color.text_color));
			((Button) findViewById(R.id.button_cc))
					.setBackgroundResource(R.drawable.xuanti_button_click);
			((Button) findViewById(R.id.button_cc)).setTextColor(getResources()
					.getColor(R.color.white));
			((Button) findViewById(R.id.button_dd))
					.setBackgroundResource(R.drawable.button_selector_default);
			((Button) findViewById(R.id.button_dd)).setTextColor(getResources()
					.getColor(R.color.text_color));
			break;
		case 4:
			((Button) findViewById(R.id.button_aa))
					.setBackgroundResource(R.drawable.button_selector_default);
			((Button) findViewById(R.id.button_aa)).setTextColor(getResources()
					.getColor(R.color.text_color));
			((Button) findViewById(R.id.button_bb))
					.setBackgroundResource(R.drawable.button_selector_default);
			((Button) findViewById(R.id.button_bb)).setTextColor(getResources()
					.getColor(R.color.text_color));
			((Button) findViewById(R.id.button_cc))
					.setBackgroundResource(R.drawable.button_selector_default);
			((Button) findViewById(R.id.button_cc)).setTextColor(getResources()
					.getColor(R.color.text_color));
			((Button) findViewById(R.id.button_dd))
					.setBackgroundResource(R.drawable.xuanti_button_click);
			((Button) findViewById(R.id.button_dd)).setTextColor(getResources()
					.getColor(R.color.white));
			break;
		default:
			break;
		}
	}
	@Override
	public void onViewClick(View v) {
		super.onViewClick(v);
//		switch (v.getId()) {
//		case R.id.ib_back:
//			finish();
//			break;
//		case R.id.button_aa:
//			setButton(1);
//			userAns = "A";
//			break;
//		case R.id.button_bb:
//			setButton(2);
//			userAns = "B";
//			break;
//		case R.id.button_cc:
//			setButton(3);
//			userAns = "C";
//			break;
//		case R.id.button_dd:
//			setButton(4);
//			userAns = "D";
//			break;
//		case R.id.button_translation:
//			topic_answer.setText(getBean().item.Key.Content + "");
//			//topic_answer.setText(BaseApplication.examData.get(names.get(currentPosition) + "_CH").toString());
//			break;
//		case R.id.button_vocabulary:
//			topic_answer.setText(getBean().item.Key.Word);
//			//topic_answer.setText(BaseApplication.examData.get(names.get(currentPosition) + "_WD").toString());
//			break;
//		case R.id.button_detailed:
//			topic_answer.setText(getBean().item.Key.Point + "");
//			break;
//		case R.id.button_skill:
//			topic_answer.setText(getBean().item.Key.Skill + "");
//			break;
//		case R.id.button_analysis:
//			String txt = "";
//			if (!ToolUtils.isNullOrEmpter(getBean().head.Txt)) {
//				txt = getBean().head.Txt.replaceAll(".txt", "");
//			}
//			if (!ToolUtils.isNullOrEmpter(getBean().head.Pic)) {
//				txt = getBean().head.Pic.replaceAll(".png", "");
//			}
//			txt += "_Q" + getBean().currentIndex;
//			getDataByGet(
//					UrlUtils.getUrl(UrlUtils.MethodName.GETANALYSIS,
//							new String[] {
//									BaseApplication.loginBean.data.userName,
//									txt }),
//					UrlUtils.MethodType.TYPE_GETANALYSIS, handler);
//			break;
//		case R.id.button_last_analytical:
//			if (currentPosition > 0) {
//				currentPosition--;
//			} else {
//				showToast("这是第一题");
//				return;
//			}
//			postData();
//			break;
//		case R.id.button_next_analytical:
//			currentPosition++;
//			if (currentPosition >= total) {
//				showToast("这是最后一题");
//				currentPosition--;
//				return;
//			}
//			if (!toSee) {
//				resumeView();
//			}
//			postData();
//			break;
//		case R.id.button_sure:
//			setButton(0);
//			checkAns(userAns);
//			break;
//
//		default:
//			break;
//		}
	}

	private void resumeView() {
		ll_qus_title.setVisibility(View.GONE);
		menu_analytical.setVisibility(View.GONE);
		menu_option_sure.setVisibility(View.VISIBLE);
	}

	private void checkAns(String useAns) {
		ll_qus_title.setVisibility(View.VISIBLE);
		menu_analytical.setVisibility(View.VISIBLE);
		menu_option.setVisibility(View.GONE);
		menu_option_sure.setVisibility(View.GONE);
		topic_answer.setText(ToolUtils.getQusAns(getBean().item.Key));
		if (useAns.equals(getBean().item.Key.Ans)) {
			image_result.setImageResource(R.drawable.right);
			needRemove.add(currentPosition);
		} else {
			image_result.setImageResource(R.drawable.wrong);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (examLocalBean != null) {
			for (Integer i : needRemove) {
				if (examLocalBean.hasDoBeans == null) {
					examLocalBean.hasDoBeans = new ArrayList<ErrTableBean>();
				}
				examLocalBean.hasDoBeans.add(examLocalBean.errBeans.get(i));
			}
			for (Integer i : needRemove) {
				examLocalBean.errBeans.remove(errTables.get(i));
			}
		}
		ObjectCacheToFile.doCache(AllContacts.EXAMLOCALBEAN, examLocalBean,
				this);
	}

	private ErrTableBean getBean() {
		return errTables.get(currentPosition);
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

	public static Intent createIntent(Context context, boolean toSee, int position) {
		Intent intent = new Intent(context, CorrectionAnalysisActivity.class);
		intent.putExtra(TOSEE, toSee);
		intent.putExtra(POSITION, position);
		return intent;
	}
}
