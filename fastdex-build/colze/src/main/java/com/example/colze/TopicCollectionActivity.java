package com.example.colze;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.example.colze.bean.TaoTiBean;

public class TopicCollectionActivity extends BaseActivity {
	private GridView mGridView;
	private GridViewAdapter adapter;
	private List<TaoTiBean> taoTiList = new ArrayList<TaoTiBean>();
	private HashMap<String, TaoTiBean> taoBeans;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_topiccollection);
		init();
	}

	@Override
	public void initData() {
		super.initData();
		taoBeans = BaseApplication.taoBeans;
		int count = taoBeans.size();
		for (int i = 1; i <= count; i++) {
			String key = "Lesson" + i;
			TaoTiBean bean = taoBeans.get(key);
			if (null != bean) {
				taoTiList.add(bean);
			}
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public void initWidget() {
		super.initWidget();
		mGridView = (GridView) findViewById(R.id.gridview_topiccollection);
		adapter = new GridViewAdapter();
		mGridView.setAdapter(adapter);
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

	private class GridViewAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return taoTiList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return taoTiList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View arg1, ViewGroup arg2) {
			View view = layoutInflater.inflate(
					R.layout.adapter_topiccollection, null);
			Button button = (Button) view
					.findViewById(R.id.item_topiccollection);
			TaoTiBean item = taoTiList.get(position);
			button.setText(position + 1 + "");
			if (item.hasRead) {
				button.setBackgroundResource(R.drawable.topic_isread_rect);
				button.setTag(item);
				button.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						TaoTiBean item = (TaoTiBean) v.getTag();
						if (item != null) {
							startActivity(TopicActivity.createIntent(
									TopicCollectionActivity.this,
									item.Head.TestTime, item.Body,
									item.Head.PassScore));
						}
					}
				});
			} else {
				button.setBackgroundResource(R.drawable.topic_unread_rect);
			}
			return view;
		}
	}

	public static Intent createIntent(Context context) {
		Intent intent = new Intent(context, TopicCollectionActivity.class);
		return intent;
	}
}
