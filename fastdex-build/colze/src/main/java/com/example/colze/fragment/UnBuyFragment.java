package com.example.colze.fragment;

import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import com.example.colze.BuyActivity;
import com.example.colze.R;
import com.example.colze.utils.AllContacts;

public class UnBuyFragment extends Fragment {
	private ImageView mImageView_EveryDay;
	private TextView mTextView_EveryDay;
	private NetworkImageView mImageView_Notice;
	private View view;
	private Context mContext;
	private RequestQueue mQueue = null;
	private ImageLoader mImageLoader;
	
	private Button mButton_Try;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_unbuy, null);
		initView();
		setData();
		return view;
	}
	private void initView()
	{
		mContext = getActivity();
		mImageView_EveryDay = (ImageView) view.findViewById(R.id.image_everyday);
		mTextView_EveryDay = (TextView) view.findViewById(R.id.text_everyday);
		mImageView_Notice = (NetworkImageView) view.findViewById(R.id.notice_imageview);
		mButton_Try = (Button) view.findViewById(R.id.button_trial);
		mButton_Try.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(getActivity(), BuyActivity.class));
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
	@SuppressWarnings("deprecation")
	private void setData()
	{
		Date data = new Date();
		int day = data.getDate();
		mTextView_EveryDay.setText(AllContacts.everyDayTextArr[day-1]);
		int everyDayImageId = getResources().getIdentifier("day"+day, "drawable", mContext.getApplicationInfo().packageName);
		if(everyDayImageId > 0)
		{
			mImageView_EveryDay.setImageResource(everyDayImageId);
		}
		mImageView_Notice.setImageUrl("http://app.gaofy.com/upload/urlpng/prireading/noticepic.png", mImageLoader);
	}
}
