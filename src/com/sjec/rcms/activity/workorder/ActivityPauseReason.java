package com.sjec.rcms.activity.workorder;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.opengl.ETC1Util;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.sjec.rcms.R;
import com.sjec.rcms.activity.BaseActivity;
import com.sjec.rcms.dao.EntityWorkorder;
import com.sjec.rcms.data.Constant;
import com.sjec.rcms.data.GlobalData;
import com.sjec.rcms.http.AsyncHttpCallBack;
import com.sjec.rcms.http.SJECHttpHandler;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;

public class ActivityPauseReason extends BaseActivity {
	private Spinner sp_reason;
	private EditText et_reason,et_caller_name,et_caller_telephone;
	private Button btn_submit;

//	public static int TYPE_REQ_PAUSE = 0;
//	public static int TYPE_REQ_CONTINUE = 1;
//	public static int TYPE_REQ_DISABLE = -1;
//	public static int TYPE_REQ_QUICK_CLOSE= 999;
//	
//	public static int TYPE_REQ_MAINTAIN_CHANGEDATE = 1000;
	
	public static String IsInit = "0";
	public static int type = -1;

	public String workorder_id = "";

	private TextView tv_title;
	private LinearLayout layout_back;
	private int str_confirm = R.string.msg_pause_workorder_confirm;
	
	private String selectReason = "";
	
	private String []ARR_MAINTAIN_CHANGEDATE ; 
	private String []ARR_WORKORDER_REPAIR_PAUSE ; 
	private String []ARR_WORKORDER_REPAIR_DISABLE ; 
	private String []ARR_WORKORDER_REPAIR_INIT ; 
	private String [] ARR_WORKORDER_CONTINUE;
	private String [] ARR_WORKORDER_QUICKCLOSE;
	private String [] ARR_CURRENT ;
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_submit:
			Util.createDialog(this, null,
					R.string.msg_dialog_add_order_type_title,
					str_confirm, null, true, false,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							updateWorkorderPauseStatus();
						}
					}).show();
			break;
		case R.id.layout_back:
			finish();
			break;
		}
	}

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_pause_reason);

	}

	@Override
	public void init(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		sp_reason = (Spinner) findViewById(R.id.sp_pause_reason);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		et_reason = (EditText) findViewById(R.id.et_reason);
		tv_title = (TextView) findViewById(R.id.tv_title);
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		et_caller_name = (EditText)findViewById(R.id.et_caller_name);
		et_caller_telephone = (EditText)findViewById(R.id.et_caller_telephone);
		btn_submit.setOnClickListener(this);
		layout_back.setOnClickListener(this);
	}

	@Override
	public void bindData() {
	
		Bundle b = getIntent().getExtras();
		IsInit = b.getString("init", "0");
		workorder_id = b.getString("workorder_id", "");
		type = b.getInt("type", 1);
		tv_title.setText(b.getString("title", ""));
		
		ARR_MAINTAIN_CHANGEDATE = getResources().getStringArray(R.array.arr_workorder_reason_maintain_changedate); 
		ARR_WORKORDER_REPAIR_PAUSE = getResources().getStringArray(R.array.arr_workorder_reason_pause); 
		ARR_WORKORDER_REPAIR_DISABLE = getResources().getStringArray(R.array.arr_workorder_reason_disable); 
		ARR_WORKORDER_REPAIR_INIT = getResources().getStringArray(R.array.arr_workorder_reason_init); 
		ARR_WORKORDER_CONTINUE = getResources().getStringArray(R.array.arr_workorder_reason_continue); 
		ARR_WORKORDER_QUICKCLOSE =  getResources().getStringArray(R.array.arr_workorder_reason_quickclose); 
		if (IsInit.equals("1")) {
			str_confirm = R.string.msg_init_workorder_confirm;
			ARR_CURRENT = ARR_WORKORDER_REPAIR_INIT;
		} else {
			if (type == Constant.EnumWorkorderPauseStatus.PAUSE_STATUS_WORKORDER_PAUSE) {
				str_confirm = R.string.msg_pause_workorder_confirm;
				ARR_CURRENT = ARR_WORKORDER_REPAIR_PAUSE;
			} else if (type ==  Constant.EnumWorkorderPauseStatus.PAUSE_STATUS_WORKORDER_CONTINUE) {
				str_confirm = R.string.msg_continue_workorder_confirm;
				ARR_CURRENT = ARR_WORKORDER_CONTINUE;
			} else if (type == Constant.EnumWorkorderPauseStatus.PAUSE_STATUS_WORKORDER_INVALID) {
				str_confirm = R.string.msg_disable_workorder_confirm;
				ARR_CURRENT = ARR_WORKORDER_REPAIR_DISABLE;
			}else if(type == Constant.EnumWorkorderPauseStatus.PAUSE_STATUS_WORKORDER_MAINTAIN_CHANGEDATE){
				str_confirm = R.string.msg_changedate_workorder_confirm; 
				ARR_CURRENT = ARR_MAINTAIN_CHANGEDATE;
			}else if(type == Constant.EnumWorkorderPauseStatus.PAUSE_STATUS_WORKORDER_QUICKCLOSE){
				et_caller_name.setVisibility(View.VISIBLE);
				et_caller_telephone.setVisibility(View.VISIBLE);
				ARR_CURRENT = ARR_WORKORDER_QUICKCLOSE;
			}else if(type == Constant.EnumWorkorderPauseStatus.PAUSE_STATUS_WORKORDER_MAINTAIN_QUICK_CLOSE){
				str_confirm = R.string.msg_changedate_workorder_confirm; 
				ARR_CURRENT = ARR_MAINTAIN_CHANGEDATE;
			}
		}

		ArrayAdapter<String> adapterType = new ArrayAdapter<String>(
				ActivityPauseReason.this, android.R.layout.simple_spinner_item,ARR_CURRENT);
		adapterType
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_reason.setAdapter(adapterType); 
		
		sp_reason
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						arg0.setVisibility(View.VISIBLE);
						selectReason = ARR_CURRENT[position];
						// workorderStauts = arrWorkorderStatusValue[position];
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});
		sp_reason.setSelection(0);
	}

	private void updateWorkorderPauseStatus() {
		if(type == Constant.EnumWorkorderPauseStatus.PAUSE_STATUS_WORKORDER_QUICKCLOSE){
			//必填
			if(getText(et_reason).isEmpty() || getText(et_caller_name).isEmpty() || getText(et_caller_telephone).isEmpty())
			{
				Util.createToast(ActivityPauseReason.this, R.string.msg_empty_quickclose_reason, 3000).show();
				return;
			}
			 
		}
		new SJECHttpHandler(callBackUpdatePauseStatus, this)
				.requestWorkorderPauseStatus(
						workorder_id, type+"",IsInit,selectReason+","+getText(et_reason),getText(et_caller_name),getText(et_caller_telephone));
	}

	private AsyncHttpCallBack callBackUpdatePauseStatus = new AsyncHttpCallBack() {

		public void onSuccess(String response) {
			LogUtil.d("%s", response);
			// 更新状态成功
			Util.createToast(ActivityPauseReason.this,
					R.string.msg_control_success, Toast.LENGTH_SHORT).show();
			JSONObject jobj;
			try {
				jobj = new JSONObject(response);
				if (jobj.optString("result", "false").equals("true")) {

					Gson gson = new Gson();
					GlobalData.curWorkorder = gson.fromJson(
							jobj.optString("data", ""), EntityWorkorder.class);
					getApplication().sendBroadcast(new Intent(Constant.ACTION_REFRESH_WORKORDER));
					getApplicationContext().sendBroadcast(
							new Intent(Constant.ACTION_REFRESH_WORKORDER_LIST));
					finish();
				} else {
					Util.createToast(ActivityPauseReason.this,
							getString(R.string.msg_control_failed, ""),
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Util.createToast(ActivityPauseReason.this,
						getString(R.string.msg_control_failed, ""),
						Toast.LENGTH_SHORT).show();
			}
		};

		public void onFailure(String message) {
			Util.createToast(ActivityPauseReason.this,
					getString(R.string.msg_control_failed, ""),
					Toast.LENGTH_SHORT).show();
		};
	};

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
