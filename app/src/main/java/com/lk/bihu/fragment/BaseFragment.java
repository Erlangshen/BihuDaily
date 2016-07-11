package com.lk.bihu.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public abstract class BaseFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(getLayoutId(), container, false);
		initView(view);
		initData();
		return view;
	}
	/**
	 * 得到布局id
	 */
	protected abstract int getLayoutId();
	/**
	 * 初始化布局文件
	 */
	protected abstract void initView(View v);
	/**
	 * 逻辑代码
	 */
	protected abstract void initData();
	/**
	 * 短的吐司
	 */
	protected void showToast(String text){
		Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
	}
	/**
	 *得到EditText的值
	 */
	protected String getEtInfo(EditText edit){
		return edit.getText().toString().trim();
	}
}
