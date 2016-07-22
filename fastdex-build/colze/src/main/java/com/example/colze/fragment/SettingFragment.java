package com.example.colze.fragment;

import com.example.colze.HomeActivity;
import com.example.colze.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class SettingFragment extends Fragment {
	private Button mButton_Help;
	private Button mButton_About;
	private Button mButton_More;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_setting, container,
				false);
		mButton_Help = (Button) view.findViewById(R.id.setting_help);
		mButton_About = (Button) view.findViewById(R.id.setting_about);
		mButton_More = (Button) view.findViewById(R.id.setting_more);
		mButton_Help.setOnClickListener(mOnClickListener);
		mButton_About.setOnClickListener(mOnClickListener);
		mButton_More.setOnClickListener(mOnClickListener);
		return view;
	}
	private OnClickListener mOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
//			switch (view.getId()) {
//			case R.id.setting_help:
//				((HomeActivity) getActivity())
//				.setFragemnt(new SeetingHelpFragment());
//				break;
//			case R.id.setting_about:
//				((HomeActivity) getActivity())
//				.setFragemnt(new SeetingAboutFragment());
//				break;
//			case R.id.setting_more:
//				((HomeActivity) getActivity())
//				.setFragemnt(new SeetingMoreFragment());
//				break;
//			default:
//				break;
//			}
		}
	};
}
