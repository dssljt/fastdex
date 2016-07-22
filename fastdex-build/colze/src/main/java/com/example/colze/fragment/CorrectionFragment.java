package com.example.colze.fragment;

import com.example.colze.CorrectionAnalysisActivity;
import com.example.colze.ErrTableActivity;
import com.example.colze.R;
import com.example.colze.bean.ExamLocalBean;
import com.example.colze.utils.AllContacts;
import com.example.colze.utils.ObjectCacheToFile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class CorrectionFragment extends Fragment {
	private View view;
	private Button mButton_Correction;
	private TextView mTextView_Correction;
	private Button mButton_Err;
	private TextView mTextView_Err;
	private ExamLocalBean examLocalBean;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_correction, container, false);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		initData();
		initView();
		setAction();
	}

	private void initData() {
		examLocalBean = (ExamLocalBean) ObjectCacheToFile.getCache(
				AllContacts.EXAMLOCALBEAN, ExamLocalBean.class, getActivity());
	}
	
	private boolean canCorr = false, canErr = false;

	private void initView() {
		mButton_Correction = (Button) view.findViewById(R.id.button_xiudingben);
		mTextView_Correction = (TextView) view
				.findViewById(R.id.text_uncorrection_num);
		if (examLocalBean != null && examLocalBean.errBeans != null) {
			mTextView_Correction.setText(examLocalBean.errBeans.size() + "");
			canCorr = true;
		} else {
			canCorr = false;
			mTextView_Correction.setText("0");
		}
		mButton_Err = (Button) view.findViewById(R.id.button_cuoti);
		mTextView_Err = (TextView) view.findViewById(R.id.text_err_num);
		if (examLocalBean != null && examLocalBean.hasDoBeans != null) {
			mTextView_Err.setText(examLocalBean.hasDoBeans.size() + "");
			canErr = true;
		} else {
			mTextView_Err.setText("0");
		}
		mTextView_Correction.postInvalidate();
		mTextView_Err.postInvalidate();
	}

	private void setAction() {
		mButton_Correction.setOnClickListener(mOnClickListener);
		mButton_Err.setOnClickListener(mOnClickListener);
	}

	private OnClickListener mOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
//			switch (arg0.getId()) {
//			case R.id.button_xiudingben:
//				if(canCorr){
//					startActivity(new Intent(getActivity(),
//							CorrectionAnalysisActivity.class));
//				}
//				break;
//			case R.id.button_cuoti:
//				if(canErr){
//					startActivity(new Intent(getActivity(), ErrTableActivity.class));
//				}
//				break;
//			default:
//				break;
//			}
		}
	};
}
