package com.example.colze;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.colze.bean.ErrTableBean;
import com.example.colze.bean.ExamLocalBean;
import com.example.colze.utils.AllContacts;
import com.example.colze.utils.ObjectCacheToFile;

public class ErrTableActivity extends BaseActivity {
	private ListView mListView;
	private Adapter adapter;
	private View mView_Header;
	private List<ErrTableBean> errTables = new ArrayList<ErrTableBean>();
	private ExamLocalBean examLocalBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_errtable);
		init();
	}

	@Override
	public void initData() {
		super.initData();
		examLocalBean = (ExamLocalBean) ObjectCacheToFile.getCache(
				AllContacts.EXAMLOCALBEAN, ExamLocalBean.class, this);
		if (examLocalBean != null) {
			errTables = examLocalBean.hasDoBeans;
		} else {
			errTables = new ArrayList<ErrTableBean>();
		}
		mListView.addHeaderView(mView_Header);
		mListView.setAdapter(adapter);
	}

	@Override
	public void initWidget() {
		super.initWidget();
		mListView = (ListView) findViewById(R.id.listview);
		adapter = new Adapter();
		mView_Header = LayoutInflater.from(this).inflate(
				R.layout.view_err_header, null);
	}

	@Override
	public void onViewClick(View v) {
		super.onViewClick(v);
//		switch (v.getId()) {
//		case R.id.ib_back:
//			finish();
//			break;
//		default:
//			break;
//		}
	}

	private class Adapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (errTables == null) {
				return 0;
			}
			return errTables.size();
		}

		@Override
		public Object getItem(int arg0) {
			return errTables.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int position, View view, ViewGroup arg2) {
			ViewHolder viewHolder = null;
			if (null == view) {
				viewHolder = new ViewHolder();
				view = LayoutInflater.from(ErrTableActivity.this).inflate(
						R.layout.adapter_errtable, null);
				viewHolder.mTextView_Position = (TextView) view
						.findViewById(R.id.text_xuhao);
				viewHolder.mTextView_Difficulty = (TextView) view
						.findViewById(R.id.text_difficulty);
				viewHolder.mTextView_Source = (TextView) view
						.findViewById(R.id.text_source);
				viewHolder.mTextView_TestCenter = (TextView) view
						.findViewById(R.id.text_testcenter);
				viewHolder.mTextView_Time = (TextView) view
						.findViewById(R.id.text_time);
				viewHolder.mButton = (Button) view
						.findViewById(R.id.button_delete);
				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}
			viewHolder.mTextView_Position.setText("" + (position + 1));
			viewHolder.mTextView_Difficulty.setText(AllContacts.getDf(errTables
					.get(position).difficulty));
			viewHolder.mTextView_Source.setText(errTables.get(position).source);
			viewHolder.mTextView_TestCenter
					.setText(errTables.get(position).testCenter);
			viewHolder.mTextView_Time.setText(errTables.get(position).time);
			viewHolder.mButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					errTables.remove(errTables.get(position));
					adapter.notifyDataSetChanged();
				}
			});
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					startActivity(CorrectionAnalysisActivity.createIntent(
							ErrTableActivity.this, true, position));
				}
			});
			return view;
		}

		class ViewHolder {
			public TextView mTextView_Position;
			public TextView mTextView_Difficulty;
			public TextView mTextView_Source;
			public TextView mTextView_TestCenter;
			public TextView mTextView_Time;
			public Button mButton;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (examLocalBean != null) {
			examLocalBean.hasDoBeans = errTables;
		}
		ObjectCacheToFile.doCache(AllContacts.EXAMLOCALBEAN, examLocalBean,
				this);
	}
}
