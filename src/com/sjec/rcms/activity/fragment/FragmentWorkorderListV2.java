package com.sjec.rcms.activity.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sjec.rcms.R;
import com.sjec.rcms.activity.BaseFragment;
import com.sjec.rcms.activity.main.ActivityMain;
import com.sjec.rcms.activity.workorder.ActivityAddWorkorder;
import com.sjec.rcms.dao.EntityVillage;
import com.sjec.rcms.data.GlobalData;
import com.sjec.rcms.http.AsyncHttpCallBack;
import com.sjec.rcms.http.SJECHttpHandler;
import com.sjec.rcms.util.SJECUtil;
import com.websharputil.common.ConvertUtil;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;
import com.websharputil.json.JSONUtils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class FragmentWorkorderListV2 extends BaseFragment implements
		View.OnClickListener {

	private ActivityMain objActivityMain;
	private Spinner sp_workorder_source;

	private PullToRefreshListView lv_workorder ;
	ArrayAdapter<String> adapterSource = null;
	List<String> listWorkorderSource = null;
	public String[] arrWorkorderSourceValue = null;
	public String workorderSource = "1";
	public String workorderType = "1";
	public String workorderStauts = "1";
	public boolean filter_pic_exists = false;
	public String keyword = "";
	private AdapterWorkorderList adapterWorkorderList;
	public RelativeLayout layout_source;

	public FragmentWorkorderListV2(String type) {
		super();
		LogUtil.e("FragmentWorkorderListV2:");
		workorderType = type;
		if (!type.equals("1")) {
			workorderSource = "";
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_workorder_list_v2,
				container, false);
		init(view);
		if (workorderType.equals("1")) {
			layout_source.setVisibility(View.VISIBLE);
		}
		return view;
	}

	private void init(View view) {
		// sp_village = (Spinner) view.findViewById(R.id.sp_village);
		sp_workorder_source = (Spinner) view
				.findViewById(R.id.sp_workorder_source);
		lv_workorder = (PullToRefreshListView) view
				.findViewById(R.id.lv_workorder);
		layout_source = (RelativeLayout) view.findViewById(R.id.layout_source);
		listWorkorderSource = new ArrayList<String>(
				Arrays.asList(getResources().getStringArray(
						R.array.arr_workorder_source)));

		arrWorkorderSourceValue = getResources().getStringArray(
				R.array.arr_workorder_source_value);

		adapterSource = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, listWorkorderSource);

		adapterSource
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_workorder_source.setAdapter(adapterSource);
		sp_workorder_source
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						arg0.setVisibility(View.VISIBLE);
						LogUtil.d("onItemSelected_sp_workorder_source:"+position);
						SJECUtil.SetPrefQueryConditon(FragmentWorkorderListV2.this.getActivity(), 2, position);
						workorderSource = arrWorkorderSourceValue[position];
						
						refreshWorkorder();
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});
		String lastPageIndex = SJECUtil.GetPrefQueryCondition(FragmentWorkorderListV2.this.getActivity(), 2);
		sp_workorder_source.setSelection(ConvertUtil.ParsetStringToInt32(lastPageIndex, 0));
		adapterWorkorderList = new AdapterWorkorderList(lv_workorder,
				getActivity(), FragmentWorkorderListV2.this);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		//LogUtil.e("onActivityCreated:"+getUserVisibleHint());
		
		if (getUserVisibleHint()
				&& lv_workorder.getVisibility() == View.VISIBLE ) {
			//refreshWorkorder();
		}
		super.onActivityCreated(savedInstanceState);
		objActivityMain = (ActivityMain) getActivity();
		

	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {

		super.setUserVisibleHint(isVisibleToUser);
		
		try {
			if (isVisibleToUser && isVisible()
					&& lv_workorder.getVisibility() == View.VISIBLE ) {
				//refreshWorkorder(); // 加载数据的方法
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onStart() {
		super.onStart();

	}

	@Override
	public void onClick(View v) {

	}

	public void refreshWorkorder() {
		LogUtil.d("%s", "刷新工单2222");
		lv_workorder.setRefreshing(true);
		adapterWorkorderList.refreshListViewStart();
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
