package com.sjec.rcms.activity;

import com.baidu.mobstat.StatService;

import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		StatService.onPause(this);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		StatService.onResume(this);
	}
}
