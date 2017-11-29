package com.sjec.rcms.activity.workorder;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mslibs.widget.CListViewParam;
import com.sjec.rcms.R;
import com.sjec.rcms.activity.BaseActivity;
import com.sjec.rcms.activity.fragment.AdapterDeviceList;
import com.sjec.rcms.dao.EntityCompanyWorker;
import com.sjec.rcms.dao.EntityWorkorderPushLog;
import com.sjec.rcms.data.Constant;
import com.sjec.rcms.data.GlobalData;
import com.sjec.rcms.http.AsyncHttpCallBack;
import com.sjec.rcms.http.SJECHttpHandler;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;

public class ActivityCompanyWorkerList extends BaseActivity {

	private LinearLayout layout_back;
	private TextView tv_title;
	private LinearLayout layout_company_worker_list;
	private Button btn_add_worker,btn_search;
	private EditText et_search_keyword;

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.layout_back:
			finish();
			break;
		case R.id.btn_search:
			new SJECHttpHandler(callBack, this).getCompanyWorkerList(GlobalData.curWorkorder.InnerID,getText(et_search_keyword));
			break;
		case R.id.btn_add_worker:
			Util.createDialog(this, null,
					R.string.msg_dialog_add_order_type_title,
					R.string.msg_confirm_control, null,
					true, false,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							AddWorker();
						}
					}).show();
			
			break;
		}
	}

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_company_worker_list);
	}

	@Override
	public void init(Bundle savedInstanceState) {
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		layout_company_worker_list = (LinearLayout) findViewById(R.id.layout_company_worker_list);
		btn_add_worker = (Button) findViewById(R.id.btn_add_worker);
		btn_search = (Button)findViewById(R.id.btn_search);
		et_search_keyword = (EditText)findViewById(R.id.et_search_keyword);
		btn_search.setOnClickListener(this);
		btn_add_worker.setOnClickListener(this);
	}

	@Override
	public void bindData() {
		tv_title.setText("选择协助人员");
		layout_back.setOnClickListener(this);
		new SJECHttpHandler(callBack, this).getCompanyWorkerList( GlobalData.curWorkorder.InnerID,getText(et_search_keyword));

	}

	private void assembItem() {
		LogUtil.d("%s", GlobalData.listCompanyWorker.size());
		LayoutInflater mInflater = LayoutInflater
				.from(ActivityCompanyWorkerList.this);
		layout_company_worker_list.removeAllViews(); 
		for (int i = 0; i < GlobalData.listCompanyWorker.size(); i++) {
			EntityCompanyWorker worker = GlobalData.listCompanyWorker.get(i);
			View view = mInflater.inflate(R.layout.item_company_worker, null);
			CheckBox cbx = (CheckBox) view.findViewById(R.id.cb_select);
			cbx.setTag(worker.InnerID);
			TextView tv_worker_name = (TextView) view
					.findViewById(R.id.tv_worker_name);
			TextView tv_worker_staffno = (TextView) view
					.findViewById(R.id.tv_worker_staffno);
			TextView tv_worker_telephone = (TextView) view
					.findViewById(R.id.tv_worker_telephone);
			tv_worker_name.setText(worker.Name);
			tv_worker_staffno.setText(worker.StaffNo);
			tv_worker_telephone.setText(Html.fromHtml("<u>"+worker.Telephone+"</u>"));
			layout_company_worker_list.addView(view);
		}
	}

	AsyncHttpCallBack callBack = new AsyncHttpCallBack() {

		public void onSuccess(String response) {
			LogUtil.d("%s", response);
			// 更新状态成功

			try {
				JSONObject jobj;
				jobj = new JSONObject(response);
				if (jobj.optString("result", "false").equals("true")) {
					Gson gson = new Gson();
					java.lang.reflect.Type myType = new TypeToken<ArrayList<EntityCompanyWorker>>() {
					}.getType();
					GlobalData.listCompanyWorker = gson.fromJson(
							jobj.optString("data", ""), myType);
					assembItem();
				} else {
					Util.createToast(ActivityCompanyWorkerList.this,
							getString(R.string.msg_control_failed, ""),
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
				Util.createToast(ActivityCompanyWorkerList.this,
						getString(R.string.msg_control_failed, ""),
						Toast.LENGTH_SHORT).show();
			}
		};

		public void onFailure(String message) {
			// TODO Auto-generated method stub
			super.onFailure(message);

			Util.createToast(ActivityCompanyWorkerList.this,
					getString(R.string.msg_control_failed, ""),
					Toast.LENGTH_SHORT).show();
		}
	};

	AsyncHttpCallBack callBackAddWorker = new AsyncHttpCallBack() {

		public void onSuccess(String response) {
			LogUtil.d("%s", response);
			if (response.contains("true")) {
				Util.createToast(ActivityCompanyWorkerList.this,
						R.string.msg_control_success, Toast.LENGTH_SHORT)
						.show();
				getApplication().sendBroadcast(
						new Intent(Constant.ACTION_REFRESH_ASSIST_LIST));
				finish();
			} else {
				Util.createToast(ActivityCompanyWorkerList.this,
						getString(R.string.msg_control_failed, ""),
						Toast.LENGTH_SHORT).show();
			}

		};

		public void onFailure(String message) {
			// TODO Auto-generated method stub
			super.onFailure(message);

			Util.createToast(ActivityCompanyWorkerList.this,
					getString(R.string.msg_control_failed, ""),
					Toast.LENGTH_SHORT).show();
		}
	};

	private void AddWorker() {
		String assist_user_id = "";
		for (int i = 0; i < layout_company_worker_list.getChildCount(); i++) {
			View view = layout_company_worker_list.getChildAt(i);
			CheckBox cbx = (CheckBox) view.findViewById(R.id.cb_select);
			if (cbx.isChecked()) {
				if (!assist_user_id.isEmpty()) {
					assist_user_id += "|";
				}
				assist_user_id += cbx.getTag().toString();
			}
		}
		Log.d("%s", assist_user_id);
		new SJECHttpHandler(callBackAddWorker, this).addWorkOrderAssistUsers(GlobalData.curWorkorder.InnerID,
				assist_user_id);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		layout_company_worker_list.removeAllViews();
		System.gc();
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
