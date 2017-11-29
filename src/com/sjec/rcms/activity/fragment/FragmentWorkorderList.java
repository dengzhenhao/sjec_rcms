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

public class FragmentWorkorderList extends BaseFragment implements
		View.OnClickListener {

	private ActivityMain objActivityMain;
	private Spinner sp_workorder_type, sp_workorder_status,
			sp_workorder_source;
	// private Spinner sp_village;
	public EditText et_search_keyword;
	private Button btn_search;
	private PullToRefreshListView lv_workorder;
	private ArrayList<String> listVillageName = new ArrayList<String>();

	ArrayAdapter<String> adapterSource = null;
	ArrayAdapter<String> adapterType = null;
	ArrayAdapter<String> adapterStatus = null;
	List<String> listWorkorderSource = null;
	List<String> listWorkorderSourceBaoyang = null;
	List<String> listWorkorderType = null;
	List<String> listWorkorderStatus = null;
	private String[] arrWorkorderSourceValue = null;
	private String[] arrWorkorderSourceValueBaoyang = null;
	private String[] arrWorkorderTypeValue = null;
	private String[] arrWorkorderStatusValue = null;
	// private ArrayAdapter<String> aspnCountries;
	public String villageID = "";
	public String workorderSource = "1";
	public String workorderType = "1";
	public String workorderStauts = "";
	public String keyword = "";
	private AdapterWorkorderList adapterWorkorderList;
	private TextView tv_manual_maintain;
	private RelativeLayout layout_source;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_workorder_list_v2,
				container, false);
		init(view);

		return view;
	}

	private void init(View view) {
		// sp_village = (Spinner) view.findViewById(R.id.sp_village);
		sp_workorder_source = (Spinner) view
				.findViewById(R.id.sp_workorder_source);
		sp_workorder_type = (Spinner) view.findViewById(R.id.sp_workorder_type);
		sp_workorder_status = (Spinner) view
				.findViewById(R.id.sp_workorder_status);
		et_search_keyword = (EditText) view
				.findViewById(R.id.et_search_keyword);
		btn_search = (Button) view.findViewById(R.id.btn_search);
		lv_workorder = (PullToRefreshListView) view
				.findViewById(R.id.lv_workorder);

		tv_manual_maintain = (TextView) view
				.findViewById(R.id.tv_manual_maintain);
		btn_search.setOnClickListener(this);
		tv_manual_maintain.setOnClickListener(this);
	}

	private void bindData() {
		listWorkorderSource = new ArrayList<String>(
				Arrays.asList(getResources().getStringArray(
						R.array.arr_workorder_source)));
		listWorkorderSourceBaoyang = new ArrayList<String>(
				Arrays.asList(getResources().getStringArray(
						R.array.arr_workorder_source_baoyang)));
		listWorkorderType = new ArrayList<String>(Arrays.asList(getResources()
				.getStringArray(R.array.arr_workorder_type)));
		listWorkorderStatus = new ArrayList<String>(
				Arrays.asList(getResources().getStringArray(
						R.array.arr_workorder_status)));

		arrWorkorderSourceValue = getResources().getStringArray(
				R.array.arr_workorder_source_value);
		arrWorkorderSourceValueBaoyang = getResources().getStringArray(
				R.array.arr_workorder_source_value_baoyang);
		arrWorkorderTypeValue = getResources().getStringArray(
				R.array.arr_workorder_type_value);
		arrWorkorderStatusValue = getResources().getStringArray(
				R.array.arr_workorder_status_value);

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
						LogUtil.d("%s", "testetst:" + position);
						if (workorderType.equals("1")) {
						workorderSource = arrWorkorderSourceValue[position];
						}else{
							workorderSource = arrWorkorderSourceValueBaoyang[position];
						}

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

		adapterType = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, listWorkorderType);
		adapterType
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_workorder_type.setAdapter(adapterType);
		sp_workorder_type
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						arg0.setVisibility(View.VISIBLE);
						workorderType = arrWorkorderTypeValue[position];
						if (workorderType.equals("1")) {
							adapterSource = new ArrayAdapter<String>(
									getActivity(),
									android.R.layout.simple_spinner_item,
									listWorkorderSource);
							adapterSource
									.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							sp_workorder_source.setAdapter(adapterSource);
						} else {
							// 如果是保养，那么来源选项就只有一种
							adapterSource = new ArrayAdapter<String>(
									getActivity(),
									android.R.layout.simple_spinner_item,
									listWorkorderSourceBaoyang);
							adapterSource
									.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							sp_workorder_source.setAdapter(adapterSource);
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

		adapterStatus = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, listWorkorderStatus);
		adapterStatus
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_workorder_status.setAdapter(adapterStatus);
		sp_workorder_status
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						arg0.setVisibility(View.VISIBLE);
						workorderStauts = arrWorkorderStatusValue[position];
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});
		sp_workorder_status.setSelection(1);
		workorderStauts = arrWorkorderStatusValue[1];
//
//		adapterWorkorderList = new AdapterWorkorderList(lv_workorder,
//				getActivity(), FragmentWorkorderList.this);
		
		// 绑定小区列表
		// new SJECHttpHandler(callbackVillage, getActivity())
		// .getUserVillage(GlobalData.curUser.InnerID);
	}

	// AsyncHttpCallBack callbackVillage = new AsyncHttpCallBack() {
	// @Override
	// public void onSuccess(String response) {
	// super.onSuccess(response);
	// LogUtil.d("小区列表:%s", response);
	// try {
	// JSONObject jobj = new JSONObject(response);
	// if (jobj.optString("result", "false").equals("true")) {
	// GlobalData.listVillage = JSONUtils.fromJson(jobj
	// .optJSONArray("data").toString(),
	// new TypeToken<ArrayList<EntityVillage>>() {
	// });
	// listVillageName.clear();
	// for (int i = 0; i < GlobalData.listVillage.size(); i++) {
	// listVillageName
	// .add(GlobalData.listVillage.get(i).Village_Name);
	// }
	//
	// listVillageName.add(0,
	// getString(R.string.str_list_village_default));
	//
	// aspnCountries = new ArrayAdapter<String>(getActivity(),
	// android.R.layout.simple_spinner_item,
	// listVillageName);
	// aspnCountries
	// .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	// sp_village.setAdapter(aspnCountries);
	// sp_village
	// .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
	// @Override
	// public void onItemSelected(AdapterView<?> arg0,
	// View arg1, int position, long arg3) {
	// arg0.setVisibility(View.VISIBLE);
	// if (position != 0) {
	// villageID = GlobalData.listVillage
	// .get(position - 1).InnerID;
	// } else {
	// villageID = "";
	// }
	// }
	//
	// @Override
	// public void onNothingSelected(
	// AdapterView<?> arg0) {
	//
	// }
	// });
	// }
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// @Override
	// public void onFailure(String message) {
	// super.onFailure(message);
	// LogUtil.d("%s", message);
	// }
	// };

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		objActivityMain = (ActivityMain) getActivity();
		bindData();
	}

	@Override
	public void onStart() {
		super.onStart();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_search:
			keyword = et_search_keyword.getText().toString().trim();
			refreshWorkorder();
			break;
		case R.id.tv_manual_maintain:
			Util.startActivity(getActivity(), ActivityAddWorkorder.class, false);
			break;
		}

	}

	public void refreshWorkorder() {
		LogUtil.d("%s", "刷新工单");
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
