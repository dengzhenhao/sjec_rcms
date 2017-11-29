package com.sjec.rcms.activity.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;


import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sjec.rcms.R;
import com.sjec.rcms.activity.BaseFragment;
import com.sjec.rcms.activity.qr.CaptureActivity;
import com.sjec.rcms.dao.EntityDevice;
import com.sjec.rcms.dao.EntityKnowledgeType;
import com.sjec.rcms.data.Constant;
import com.sjec.rcms.data.GlobalData;
import com.sjec.rcms.http.AsyncHttpCallBack;
import com.sjec.rcms.http.SJECHttpHandler;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;
import com.websharputil.json.JSONUtils;

import android.app.Activity;
import android.os.Bundle;
import android.provider.Settings.Global;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 信息采集,设备列表
 * 
 * @类名称：FragmentDeviceList
 * @包名：com.sjec.rcms.fragment
 * @描述： TODO
 * @创建人： dengzh 
 * @创建时间:2015-7-29 下午2:55:58
 * @版本 V1.0
 * @Copyright (c) 2015 by 苏州威博世网络科技有限公司.
 */
public class FragmentDeviceList extends BaseFragment implements
		View.OnClickListener {

	private TextView tv_qr;
	private PullToRefreshListView lv_device;
	private AdapterDeviceList adapterDeviceList;
	private EditText et_search_keyword;
	private Button btn_search;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_device_list, container,
				false);
		init(view);

		return view;
	}

	private void init(View view) {
		tv_qr = (TextView) view.findViewById(R.id.tv_qr);
		lv_device = (PullToRefreshListView) view.findViewById(R.id.lv_device);
		et_search_keyword = (EditText)view.findViewById(R.id.et_search_keyword);
		btn_search = (Button)view.findViewById(R.id.btn_search);
		tv_qr.setOnClickListener(this);
		btn_search.setOnClickListener(this);
		
	}

	private void bindData() {
		adapterDeviceList = new AdapterDeviceList(lv_device, getActivity());

	}

	public void updateDeviceListAttr() {
		adapterDeviceList.mDateList.clear();
		adapterDeviceList.mListItems = (ArrayList<Object>)GlobalData.listDevice.clone();
		for (int i = 0; i < adapterDeviceList.mListItems.size(); i++) {
			adapterDeviceList.mDateList.add(adapterDeviceList.matchListItem(
					adapterDeviceList.mListItems.get(i), i));
		}
		adapterDeviceList.mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		bindData();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_qr:
			Bundle b = new Bundle();
			b.putInt("scan_type", Constant.SCAN_TYPE1);
			Util.startActivity(getActivity(), CaptureActivity.class, b, false);
			break;
		case R.id.btn_search:
			lv_device.setRefreshing(true);
			adapterDeviceList.keyword = et_search_keyword.getText().toString().trim();
			adapterDeviceList.refreshListViewStart();
			break;
		}
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