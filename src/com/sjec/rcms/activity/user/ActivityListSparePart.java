package com.sjec.rcms.activity.user;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sjec.rcms.R;
import com.sjec.rcms.activity.BaseActivity;
import com.sjec.rcms.activity.fragment.AdapterDeviceList;
import com.sjec.rcms.data.Constant;
import com.websharputil.common.LogUtil;

public class ActivityListSparePart extends BaseActivity {

	private PullToRefreshListView lv_spare_part;
	private AdapterListSparePart adapterListSparePart;
	private LinearLayout layout_back;
	private TextView tv_title;
	private Spinner sp_closestatus;
	ArrayAdapter<String> adapterSource;
	ArrayList<String> listCloseStatusKey = new ArrayList<String>();
	ArrayList<String> listCloseStatusValue = new ArrayList<String>();

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_back:
			finish();
			break;
		}
	}

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_list_spare_part);
	}

	@Override
	public void init(Bundle savedInstanceState) {

		lv_spare_part = (PullToRefreshListView) findViewById(R.id.lv_spare_part);
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		sp_closestatus = (Spinner) findViewById(R.id.sp_closestatus);
	}

	@Override
	public void bindData() {
		listCloseStatusKey = new ArrayList<String>(Arrays.asList(getResources()
				.getStringArray(R.array.arr_spare_closestatus_key)));
		listCloseStatusValue = new ArrayList<String>(
				Arrays.asList(getResources().getStringArray(
						R.array.arr_spare_closestatus_value)));

		adapterSource = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, listCloseStatusKey);

		adapterSource
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_closestatus.setAdapter(adapterSource);
		sp_closestatus
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						arg0.setVisibility(View.VISIBLE);
						LogUtil.d("%s", "testetst:" + position);
						adapterListSparePart.closeStatus = listCloseStatusValue.get(position);
						adapterListSparePart.refreshListViewStart();
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});
		
		tv_title.setText("备件申请列表");
		layout_back.setOnClickListener(this);
		adapterListSparePart = new AdapterListSparePart(lv_spare_part, this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}
}
