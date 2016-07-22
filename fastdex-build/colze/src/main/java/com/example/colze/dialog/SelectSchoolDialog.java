package com.example.colze.dialog;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.colze.BaseActivity;
import com.example.colze.R;
import com.example.colze.bean.School;
import com.example.colze.bean.School.Data;
import com.example.colze.bean.SchoolBean;
import com.example.colze.bean.SchoolBean.City;
import com.example.colze.utils.StringUtil;
import com.example.colze.utils.UrlUtils;

@SuppressLint("HandlerLeak")
public class SelectSchoolDialog extends Dialog {
	private Context mContext;
	private SchoolSelectListener listener;
	private TextView mTextView;
	private ListView mListView;
	private Adapter adapter;
	private List<SchoolBean> beans = new ArrayList<SchoolBean>();
	private List<Object> showList = new ArrayList<Object>();

	private static final int show_type_province = 0;
	private static final int show_type_city = 1;
	private static final int show_type_area = 2;
	private static final int show_type_school = 3;
	private int showType = show_type_province;

	private SchoolBean province = null;
	private City city = null;
	private String area = "";
	private String school = "";

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case UrlUtils.MethodType.TYPE_SEARCHSCHOOL:
				if (msg.obj != null) {
					School response = (School) msg.obj;
					if (response.status == 1) {
						if (response.data != null) {
							showList = new ArrayList<Object>();
							showList.addAll(response.data);
							adapter.notifyDataSetChanged();
						}
					} else {
						((BaseActivity) mContext).showToast(response.msg + "");
					}
				}
				break;

			default:
				break;
			}
		}
	};

	public SelectSchoolDialog(Context context, List<SchoolBean> list,
			SchoolSelectListener l) {
		super(context, R.style.dialog);
		this.mContext = context;
		this.listener = l;
		if (null != list) {
			beans.clear();
			beans.addAll(list);
		}
		getWindow().setWindowAnimations(R.style.PopupAnimation);
		setContentView(R.layout.dialog_selectschool);
		initView();
		setData();
		setAction();
	}

	private void initView() {
		mTextView = (TextView) findViewById(R.id.text_select);
		mListView = (ListView) findViewById(R.id.listview);
		adapter = new Adapter();
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		int screenWidth = dm.widthPixels;
		int screenHeigh = dm.heightPixels;
		Window dialogWindow = this.getWindow();
		WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
		p.height = (int) (screenHeigh * 0.9);
		p.width = (int) (screenWidth / 2.4);
		dialogWindow.setAttributes(p);
	}

	private void setData() {
		mListView.setAdapter(adapter);
		showType = show_type_province;
		showList.clear();
		showList.addAll(beans);
		adapter.notifyDataSetChanged();
	}

	private void setAction() {
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switch (showType) {
				case show_type_province:
					province = (SchoolBean) showList.get(arg2);
					if (null != province) {
						showList.clear();
						showList.addAll(province.cities);
						showType = show_type_city;
						adapter.notifyDataSetChanged();
					}
					break;
				case show_type_city:
					city = (City) showList.get(arg2);
					if (null != city) {
						showList.clear();
						showList.addAll(city.area);
						showType = show_type_area;
						adapter.notifyDataSetChanged();
					}
					break;
				case show_type_area:
					area = showList.get(arg2).toString();
					if (!StringUtil.isEmpty(area)) {
						showType = show_type_school;
						showList.clear();
						adapter.notifyDataSetChanged();
						doHttp();
					}
					break;
				case show_type_school:
					if (null != listener) {
						school = ((Data) (showList.get(arg2))).xiao;
						if (!StringUtil.isEmpty(school)) {
							listener.onSchoolSelect(province.province,
									city.city, area, school);
						}
					}
					break;
				default:
					break;
				}
			}
		});
	}

	private void doHttp() {
		((BaseActivity) mContext).getDataByGet(
				UrlUtils.getUrl(UrlUtils.MethodName.SEARCHSCHOOL, new String[] {
						province.province, city.city, area, "0" }),
				UrlUtils.MethodType.TYPE_SEARCHSCHOOL, mHandler);
	}

	private class Adapter extends BaseAdapter {
		@Override
		public int getCount() {
			return showList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return showList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View view, ViewGroup group) {
			ViewHolder viewHolder = null;
			if (null == view) {
				view = LayoutInflater.from(mContext).inflate(
						R.layout.adapter_selectschool, null);
				viewHolder = new ViewHolder();
				viewHolder.mTextView = (TextView) view
						.findViewById(R.id.text_item);
				viewHolder.mImageView = (ImageView) view
						.findViewById(R.id.image_item);
				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}
			viewHolder.mImageView.setVisibility(View.VISIBLE);
			switch (showType) {
			case show_type_province:
				SchoolBean p = (SchoolBean) showList.get(position);
				viewHolder.mTextView.setText(p.province);
				break;
			case show_type_city:
				City c = (City) showList.get(position);
				viewHolder.mTextView.setText(c.city);
				break;
			case show_type_area:
				String a = showList.get(position).toString();
				viewHolder.mTextView.setText(a);
				break;
			case show_type_school:
				Data data = (Data) showList.get(position);
				viewHolder.mTextView.setText(data.xiao + "");
				viewHolder.mImageView.setVisibility(View.GONE);
				break;
			default:
				break;
			}
			return view;
		}

		class ViewHolder {
			TextView mTextView;
			ImageView mImageView;
		}
	}

	public interface SchoolSelectListener {
		public void onSchoolSelect(String province, String city, String area,
				String school);
	}

	@Override
	public void dismiss() {
		showType = show_type_province;
		province = null;
		city = null;
		area = "";
		school = "";
		beans.clear();
		showList.clear();
		adapter = null;
		mTextView.setText("");
		super.dismiss();
	}
}
