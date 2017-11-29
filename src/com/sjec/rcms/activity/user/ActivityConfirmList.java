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

public class ActivityConfirmList extends BaseActivity {

	private PullToRefreshListView lv_confirm;
	private AdapterConfirmList adapterConfirmList;
	private LinearLayout layout_back;
	private TextView tv_title;
	
	BroadcastReceiver receiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(intent.getAction().equals(Constant.ACTION_REFRESH_PAUSE_CONFIRM_LIST)){
				lv_confirm.setRefreshing(true);
				adapterConfirmList.refreshListViewStart();
			}
		}
	};

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
		setContentView(R.layout.activity_confirm_list);
	}

	@Override
	public void init(Bundle savedInstanceState) {
		IntentFilter filter = new IntentFilter(Constant.ACTION_REFRESH_PAUSE_CONFIRM_LIST);
		registerReceiver(receiver, filter);
		lv_confirm = (PullToRefreshListView) findViewById(R.id.lv_confirm);
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
	}

	@Override
	public void bindData() {
		tv_title.setText("审核工单");
		layout_back.setOnClickListener(this);
		adapterConfirmList = new AdapterConfirmList(lv_confirm, this);

	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
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
