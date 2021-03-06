package com.sjec.rcms.activity.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sjec.rcms.R;
import com.sjec.rcms.activity.BaseActivity;
import com.sjec.rcms.activity.fragment.AdapterDeviceList;
import com.sjec.rcms.data.Constant;

public class ActivityPauseList extends BaseActivity {

	private PullToRefreshListView lv_pause;
	private AdapterPauseList adapterPauseList;
	private LinearLayout layout_back;
	private TextView tv_title;
	
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.layout_back:
			finish();
			break;
		}
	}

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_pause_list);
	}

	@Override
	public void init(Bundle savedInstanceState) {
		
		lv_pause = (PullToRefreshListView) findViewById(R.id.lv_pause);
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
	}

	@Override
	public void bindData() {
		tv_title.setText("暂停工单");
		layout_back.setOnClickListener(this);
		//lv_pause.setMode(Mode.)
		adapterPauseList = new AdapterPauseList(lv_pause , this);

	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
}
