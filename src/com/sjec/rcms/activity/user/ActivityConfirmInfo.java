package com.sjec.rcms.activity.user;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sjec.rcms.R;
import com.sjec.rcms.activity.BaseActivity;
import com.sjec.rcms.activity.workorder.ActivityDoRepair;
import com.sjec.rcms.dao.EntityWorkorder;
import com.sjec.rcms.data.Constant;
import com.sjec.rcms.data.GlobalData;
import com.sjec.rcms.http.AsyncHttpCallBack;
import com.sjec.rcms.http.SJECHttpHandler;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;

public class ActivityConfirmInfo extends BaseActivity {

	private TextView tv_xqmc;
	private TextView tv_sbh;
	private TextView tv_fromstatus;
	private TextView tv_targetstatus;
	private TextView tv_req_user;
	private TextView tv_req_time;
	private TextView tv_req_remark;
	private TextView tv_gdms;
	private LinearLayout layout_back;
	private TextView tv_title;

	private Button btn_agree, btn_reject;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.layout_back:
			finish();
			break;
		case R.id.btn_agree:
			confirm(1);
			break;
		case R.id.btn_reject:
			confirm(-1);
			break;
		}
	}

	@Override
	public void initLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_confirm_info);
	}

	@Override
	public void init(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		tv_xqmc = (TextView) findViewById(R.id.tv_xqmc);
		tv_sbh = (TextView) findViewById(R.id.tv_sbh);
		tv_fromstatus = (TextView) findViewById(R.id.tv_fromstatus);
		tv_targetstatus = (TextView) findViewById(R.id.tv_targetstatus);
		tv_req_user = (TextView) findViewById(R.id.tv_req_user);
		tv_req_time = (TextView) findViewById(R.id.tv_req_time);
		tv_req_remark = (TextView)findViewById(R.id.tv_req_remark);
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		btn_agree = (Button) findViewById(R.id.btn_agree);
		btn_reject = (Button) findViewById(R.id.btn_reject);
		tv_gdms = (TextView)findViewById(R.id.tv_gdms);
	}

	@Override
	public void bindData() {
		tv_title.setText("审核工单");
		btn_agree.setOnClickListener(this);
		btn_reject.setOnClickListener(this);
		layout_back.setOnClickListener(this);
		tv_xqmc.setText(GlobalData.curPauseConfirm.Village_Name);
		tv_sbh.setText(GlobalData.curPauseConfirm.Device_Num);
		tv_fromstatus.setText(Constant.EnumWorkorderPauseStatus
				.GetName(GlobalData.curPauseConfirm.FromPauseStatus));
		tv_targetstatus.setText(Constant.EnumWorkorderPauseStatus
				.GetName(GlobalData.curPauseConfirm.TargetPauseStatus)); 
		tv_req_user.setText(GlobalData.curPauseConfirm.ChargeUserName);
		tv_req_time.setText(GlobalData.curPauseConfirm.OccurTime);
		tv_req_remark.setText(GlobalData.curPauseConfirm.Remark);
		tv_gdms.setText(GlobalData.curPauseConfirm.Workorder_Remark);
	}

	private void confirm(final int adminCheck) {
		Util.createDialog(
				this,
				null,
				R.string.msg_dialog_add_order_type_title,
				(adminCheck == 1 ? R.string.msg_pause_confirm_pass
						: R.string.msg_pause_confirm_reject), null, true,
				false, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						new SJECHttpHandler(callBackUpdateStatus,
								ActivityConfirmInfo.this)
								.confirmWorkorderPauseStatus(
										GlobalData.curPauseConfirm.InnerID,
										adminCheck + "");
					}
				}).show();

	}

	private AsyncHttpCallBack callBackUpdateStatus = new AsyncHttpCallBack() {

		public void onSuccess(String response) {
			LogUtil.d("%s", response);
			// 更新状态成功
			Util.createToast(ActivityConfirmInfo.this,
					R.string.msg_control_success, Toast.LENGTH_SHORT).show();
			JSONObject jobj;
			try {
				jobj = new JSONObject(response);
				if (jobj.optString("result", "false").equals("true")) {
					getApplication().sendBroadcast(
							new Intent(
									Constant.ACTION_REFRESH_PAUSE_CONFIRM_LIST));
					finish();  
				} else {
					Util.createToast(ActivityConfirmInfo.this,
							getString(R.string.msg_control_failed, ""),
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Util.createToast(ActivityConfirmInfo.this,
						getString(R.string.msg_control_failed, ""),
						Toast.LENGTH_SHORT).show();
			}
		};

		public void onFailure(String message) {
			Util.createToast(ActivityConfirmInfo.this,
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
