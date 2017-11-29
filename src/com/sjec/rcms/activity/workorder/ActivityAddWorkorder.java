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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.sjec.rcms.R;
import com.sjec.rcms.activity.BaseActivity;
import com.sjec.rcms.activity.qr.CaptureActivity;
import com.sjec.rcms.dao.EntityFactoryCode;
import com.sjec.rcms.dao.EntityVillage;
import com.sjec.rcms.dao.EntityWorkorder;
import com.sjec.rcms.data.Constant;
import com.sjec.rcms.data.GlobalData;
import com.sjec.rcms.http.AsyncHttpCallBack;
import com.sjec.rcms.http.SJECHttpHandler;
import com.sjec.rcms.util.SJECUtil;
import com.websharputil.common.ConvertUtil;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;
import com.websharputil.json.JSONUtils;

/**
 * 
 * @类名称：ActivityAddMaintain
 * @包名：com.sjec.rcms.activity.maintain
 * @描述：手工添加工单
 * @创建人： dengzh
 * @创建时间:2015-8-7 下午4:09:12
 * @版本 V1.0
 * @Copyright (c) 2015 by 苏州威博世网络科技有限公司.
 */
public class ActivityAddWorkorder extends BaseActivity {
	private TextView tv_title;
	private LinearLayout layout_back;
	private Button btn_submit, btn_qr;
	private EditText et_device_num;
	private String[] arrDeviceInfo;
	private CheckBox cb_order_type_maintain, cb_order_type_repair,
			cb_order_type_check;
	private CheckBox cb_order_control_type_guanren,
			cb_order_control_type_jixiu, cb_order_control_type_peijiangenghuan;
	private CheckBox cb_order_control_type_zcwb, cb_order_control_type_dxgz;
	private CheckBox cb_order_control_type_feijian, cb_order_control_type_zbjc,
			cb_order_control_type_fgszj, cb_order_control_type_njqzj,
			cb_order_control_type_nianjian, cb_order_control_type_qtjc;
	private int order_type = Constant.EnumWorkorderType.TYPE_WORKORDER_REPAIR;
	private int control_type = Constant.EnumControlType.CONTROL_TYPE_1;
	private ArrayList<String> listFactoryName = new ArrayList<String>();
	private String factoryCode = "";
	private Spinner sp_factory_code;
	private ArrayAdapter<String> aspnFactoryCode;
	private LinearLayout layout_error_info;
	private EditText et_error_code;
	private EditText et_error_desc;

	private LinearLayout layout_repair, layout_maintain, layout_check;

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					Constant.ACTION_SCAN_DEVICE_NUM_SUCCESS_MAINTAIN)) {
				// 取到设备号,这时候可以调用接口查询出设备信息
				LogUtil.d("%s", intent.getExtras().getString("data"));
				String result = intent.getExtras().getString("data");
				if (SJECUtil.CheckQrResult(result)) {
					arrDeviceInfo = SJECUtil
							.GetDeviceInfoArrayFromQrCode(result);
					if (arrDeviceInfo != null) {
						//
						try {
							for (int i = 0; i < listFactoryName.size(); i++) {
								if (GlobalData.listFactoryCode.get(i).CompanyCode
										.trim().equals(arrDeviceInfo[0])) {
									sp_factory_code.setSelection(i + 1);
									break;
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						// et_company_num.setText(arrDeviceInfo[0]);
						et_device_num.setText(arrDeviceInfo[2]);
						Util.createToast(ActivityAddWorkorder.this,
								R.string.str_maintain_qr_success,
								Toast.LENGTH_SHORT).show();
						// 验证设备是否已经在抢修/维保

					} else {
						Util.createToast(ActivityAddWorkorder.this,
								R.string.str_maintain_qr_failed,
								Toast.LENGTH_SHORT).show();
					}
				} else {

					et_device_num.setText("");
					Util.createToast(ActivityAddWorkorder.this,
							R.string.str_qr_invalid, Toast.LENGTH_SHORT).show();
				}

			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_back:
			finish();
			break;
		case R.id.btn_submit:
			if (factoryCode.isEmpty() || getText(et_device_num).isEmpty()) {
				Util.createToast(ActivityAddWorkorder.this, "厂商编号和设备号不能为空!",
						3000).show();
				return;
			}
			if (!cb_order_type_maintain.isChecked()
					&& !cb_order_type_repair.isChecked()
					&& !cb_order_type_check.isChecked()) {
				Util.createToast(ActivityAddWorkorder.this,
						R.string.msg_add_order_type, 3000).show();
				return;
			}

			if (cb_order_type_maintain.isChecked()) {
				if (!cb_order_control_type_zcwb.isChecked()
						&& !cb_order_control_type_dxgz.isChecked()) {
					Util.createToast(ActivityAddWorkorder.this,
							R.string.msg_add_order_type, 3000).show();
					return;
				}
			}

			if (cb_order_type_repair.isChecked()) {
				if (!cb_order_control_type_jixiu.isChecked()
						&& !cb_order_control_type_guanren.isChecked()
						&& !cb_order_control_type_peijiangenghuan.isChecked()) {
					Util.createToast(ActivityAddWorkorder.this,
							R.string.msg_add_order_type, 3000).show();
					return;
				}
			}

			if (cb_order_type_check.isChecked()) {
				if (!cb_order_control_type_feijian.isChecked()
						&& !cb_order_control_type_zbjc.isChecked()
						&& !cb_order_control_type_fgszj.isChecked()
						&& !cb_order_control_type_njqzj.isChecked()
						&& !cb_order_control_type_nianjian.isChecked()
						&& !cb_order_control_type_qtjc.isChecked()) {
					Util.createToast(ActivityAddWorkorder.this,
							R.string.msg_add_order_type, 3000).show();
					return;
				}
			}
			if (cb_order_type_repair.isChecked() && getText(et_error_desc).isEmpty()) {
				Util.createToast(ActivityAddWorkorder.this,
						"请填写故障描述", 3000).show();
				et_error_desc.requestFocus();
				return;
			}
			Util.createDialog(this, null,
					R.string.msg_dialog_add_order_type_title,
					R.string.msg_dialog_add_order_type_confirm, null, true,
					false, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							AddWorkorder();
						}
					}).show();

			break;
		case R.id.btn_qr:
			Bundle b = new Bundle();
			b.putInt("scan_type", Constant.SCAN_TYPE2);
			Util.startActivity(ActivityAddWorkorder.this,
					CaptureActivity.class, b, false);
			break;

		case R.id.cb_order_type_repair:
			layout_error_info.setVisibility(View.VISIBLE);
			layout_repair.setVisibility(View.VISIBLE);
			layout_maintain.setVisibility(View.GONE);
			layout_check.setVisibility(View.GONE);
			btn_submit.setText(R.string.str_maintain_clicktobegin_repair);
			cb_order_type_maintain.setChecked(false);
			cb_order_type_check.setChecked(false);

			cb_order_control_type_guanren.setChecked(false);
			cb_order_control_type_jixiu.setChecked(true);
			cb_order_control_type_peijiangenghuan.setChecked(false);

			cb_order_control_type_zcwb.setChecked(false);
			cb_order_control_type_dxgz.setChecked(false);
			// cb_order_control_type_fj.setChecked(false);
			// cb_order_control_type_nj.setChecked(false);

			cb_order_control_type_feijian.setChecked(false);
			cb_order_control_type_zbjc.setChecked(false);
			cb_order_control_type_fgszj.setChecked(false);
			cb_order_control_type_njqzj.setChecked(false);
			cb_order_control_type_nianjian.setChecked(false);
			cb_order_control_type_qtjc.setChecked(false);

			cb_order_control_type_guanren.setEnabled(true);
			cb_order_control_type_jixiu.setEnabled(true);
			cb_order_control_type_peijiangenghuan.setEnabled(true);

			cb_order_control_type_zcwb.setEnabled(false);
			cb_order_control_type_dxgz.setEnabled(false);
			// cb_order_control_type_fj.setEnabled(false);
			// cb_order_control_type_nj.setEnabled(false);

			cb_order_control_type_feijian.setEnabled(false);
			cb_order_control_type_zbjc.setEnabled(false);
			cb_order_control_type_fgszj.setEnabled(false);
			cb_order_control_type_njqzj.setEnabled(false);
			cb_order_control_type_nianjian.setEnabled(false);
			cb_order_control_type_qtjc.setEnabled(false);

			order_type = Constant.EnumWorkorderType.TYPE_WORKORDER_REPAIR;
			control_type = Constant.EnumControlType.CONTROL_TYPE_1;

			break;
		case R.id.cb_order_type_maintain:
			layout_error_info.setVisibility(View.GONE);

			layout_repair.setVisibility(View.GONE);
			layout_maintain.setVisibility(View.VISIBLE);
			layout_check.setVisibility(View.GONE);
			btn_submit.setText(R.string.str_maintain_clicktobegin_maintain);
			cb_order_type_repair.setChecked(false);
			cb_order_type_check.setChecked(false);

			cb_order_control_type_guanren.setChecked(false);
			cb_order_control_type_jixiu.setChecked(false);
			cb_order_control_type_peijiangenghuan.setChecked(false);

			cb_order_control_type_zcwb.setChecked(true);
			cb_order_control_type_dxgz.setChecked(false);
			// cb_order_control_type_fj.setChecked(false);
			// cb_order_control_type_nj.setChecked(false);

			cb_order_control_type_feijian.setChecked(false);
			cb_order_control_type_zbjc.setChecked(false);
			cb_order_control_type_fgszj.setChecked(false);
			cb_order_control_type_njqzj.setChecked(false);
			cb_order_control_type_nianjian.setChecked(false);
			cb_order_control_type_qtjc.setChecked(false);

			cb_order_control_type_guanren.setEnabled(false);
			cb_order_control_type_jixiu.setEnabled(false);
			cb_order_control_type_peijiangenghuan.setEnabled(false);

			cb_order_control_type_zcwb.setEnabled(true);
			cb_order_control_type_dxgz.setEnabled(true);
			// cb_order_control_type_fj.setEnabled(true);
			// cb_order_control_type_nj.setEnabled(true);

			cb_order_control_type_feijian.setEnabled(false);
			cb_order_control_type_zbjc.setEnabled(false);
			cb_order_control_type_fgszj.setEnabled(false);
			cb_order_control_type_njqzj.setEnabled(false);
			cb_order_control_type_nianjian.setEnabled(false);
			cb_order_control_type_qtjc.setEnabled(false);

			order_type = Constant.EnumWorkorderType.TYPE_WORKORDER_MAINTAIN;
			control_type = Constant.EnumControlType.CONTROL_TYPE_BY_1;
			break;

		case R.id.cb_order_type_check:
			layout_error_info.setVisibility(View.GONE);
			layout_repair.setVisibility(View.GONE);
			layout_maintain.setVisibility(View.GONE);
			layout_check.setVisibility(View.VISIBLE);
			btn_submit.setText(R.string.str_maintain_clicktobegin_maintain);
			cb_order_type_repair.setChecked(false);
			cb_order_type_maintain.setChecked(false);

			cb_order_control_type_guanren.setChecked(false);
			cb_order_control_type_jixiu.setChecked(false);
			cb_order_control_type_peijiangenghuan.setChecked(false);

			cb_order_control_type_zcwb.setChecked(false);
			cb_order_control_type_dxgz.setChecked(false);
			// cb_order_control_type_fj.setChecked(false);
			// cb_order_control_type_nj.setChecked(false);

			cb_order_control_type_feijian.setChecked(true);
			cb_order_control_type_zbjc.setChecked(false);
			cb_order_control_type_fgszj.setChecked(false);
			cb_order_control_type_njqzj.setChecked(false);
			cb_order_control_type_nianjian.setChecked(false);
			cb_order_control_type_qtjc.setChecked(false);

			cb_order_control_type_guanren.setEnabled(false);
			cb_order_control_type_jixiu.setEnabled(false);
			cb_order_control_type_peijiangenghuan.setEnabled(false);

			cb_order_control_type_zcwb.setEnabled(false);
			cb_order_control_type_dxgz.setEnabled(false);
			// cb_order_control_type_fj.setEnabled(true);
			// cb_order_control_type_nj.setEnabled(true);

			cb_order_control_type_feijian.setEnabled(true);
			cb_order_control_type_zbjc.setEnabled(true);
			cb_order_control_type_fgszj.setEnabled(true);
			cb_order_control_type_njqzj.setEnabled(true);
			cb_order_control_type_nianjian.setEnabled(true);
			cb_order_control_type_qtjc.setEnabled(true);

			order_type = Constant.EnumWorkorderType.TYPE_WORKORDER_CHECK;
			control_type = Constant.EnumControlType.CONTROL_TYPE_JC_1;
			break;
		case R.id.cb_order_control_type_zcwb:
			cb_order_control_type_dxgz.setChecked(false);
			// cb_order_control_type_fj.setChecked(false);
			// cb_order_control_type_nj.setChecked(false);
			control_type = Constant.EnumControlType.CONTROL_TYPE_BY_1;
			break;
		case R.id.cb_order_control_type_dxgz:
			cb_order_control_type_zcwb.setChecked(false);
			// cb_order_control_type_fj.setChecked(false);
			// cb_order_control_type_nj.setChecked(false);
			control_type = Constant.EnumControlType.CONTROL_TYPE_BY_2;
			break;
		// case R.id.cb_order_control_type_fj:
		// cb_order_control_type_zcwb.setChecked(false);
		// cb_order_control_type_dxgz.setChecked(false);
		// cb_order_control_type_nj.setChecked(false);
		// control_type = Constant.EnumControlType.CONTROL_TYPE__BY_3;
		// break;
		// case R.id.cb_order_control_type_nj:
		// cb_order_control_type_zcwb.setChecked(false);
		// cb_order_control_type_dxgz.setChecked(false);
		// cb_order_control_type_fj.setChecked(false);
		// control_type = Constant.EnumControlType.CONTROL_TYPE__BY_4;
		// break;
		case R.id.cb_order_control_type_jixiu:
			cb_order_control_type_guanren.setChecked(false);
			cb_order_control_type_peijiangenghuan.setChecked(false);
			control_type = Constant.EnumControlType.CONTROL_TYPE_1;
			break;
		case R.id.cb_order_control_type_guanren:
			cb_order_control_type_jixiu.setChecked(false);
			cb_order_control_type_peijiangenghuan.setChecked(false);
			control_type = Constant.EnumControlType.CONTROL_TYPE_2;
			break;

		case R.id.cb_order_control_type_peijiangenghuan:
			cb_order_control_type_guanren.setChecked(false);
			cb_order_control_type_jixiu.setChecked(false);
			control_type = Constant.EnumControlType.CONTROL_TYPE_3;
			break;

		case R.id.cb_order_control_type_feijian:
			// cb_order_control_type_feijian.setChecked(false);
			cb_order_control_type_zbjc.setChecked(false);
			cb_order_control_type_fgszj.setChecked(false);
			cb_order_control_type_njqzj.setChecked(false);
			cb_order_control_type_nianjian.setChecked(false);
			cb_order_control_type_qtjc.setChecked(false);
			control_type = Constant.EnumControlType.CONTROL_TYPE_JC_1;
			break;

		case R.id.cb_order_control_type_zbjc:
			cb_order_control_type_feijian.setChecked(false);
			// cb_order_control_type_zbjc.setChecked(true);
			cb_order_control_type_fgszj.setChecked(false);
			cb_order_control_type_njqzj.setChecked(false);
			cb_order_control_type_nianjian.setChecked(false);
			cb_order_control_type_qtjc.setChecked(false);
			control_type = Constant.EnumControlType.CONTROL_TYPE_JC_2;
			break;
		case R.id.cb_order_control_type_fgszj:
			cb_order_control_type_feijian.setChecked(false);
			cb_order_control_type_zbjc.setChecked(false);
			// cb_order_control_type_fgszj.setChecked(false);
			cb_order_control_type_njqzj.setChecked(false);
			cb_order_control_type_nianjian.setChecked(false);
			cb_order_control_type_qtjc.setChecked(false);
			control_type = Constant.EnumControlType.CONTROL_TYPE_JC_3;
			break;
		case R.id.cb_order_control_type_njqzj:
			cb_order_control_type_feijian.setChecked(false);
			cb_order_control_type_zbjc.setChecked(false);
			cb_order_control_type_fgszj.setChecked(false);
			// cb_order_control_type_njqzj.setChecked(false);
			cb_order_control_type_nianjian.setChecked(false);
			cb_order_control_type_qtjc.setChecked(false);
			control_type = Constant.EnumControlType.CONTROL_TYPE_JC_4;
			break;
		case R.id.cb_order_control_type_nianjian:
			cb_order_control_type_feijian.setChecked(false);
			cb_order_control_type_zbjc.setChecked(false);
			cb_order_control_type_fgszj.setChecked(false);
			cb_order_control_type_njqzj.setChecked(false);
			// cb_order_control_type_nianjian.setChecked(false);
			cb_order_control_type_qtjc.setChecked(false);
			control_type = Constant.EnumControlType.CONTROL_TYPE_JC_5;
			break;
		case R.id.cb_order_control_type_qtjc:
			cb_order_control_type_feijian.setChecked(false);
			cb_order_control_type_zbjc.setChecked(false);
			cb_order_control_type_fgszj.setChecked(false);
			cb_order_control_type_njqzj.setChecked(false);
			cb_order_control_type_nianjian.setChecked(false);
			// cb_order_control_type_qtjc.setChecked(false);
			control_type = Constant.EnumControlType.CONTROL_TYPE_JC_6;
			break;
		}
	}

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_workorder_add);
	}

	@Override
	public void init(Bundle savedInstanceState) {
		IntentFilter intentFilter = new IntentFilter(
				Constant.ACTION_SCAN_DEVICE_NUM_SUCCESS_MAINTAIN);
		registerReceiver(receiver, intentFilter);
		tv_title = (TextView) findViewById(R.id.tv_title);
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_qr = (Button) findViewById(R.id.btn_qr);
		et_device_num = (EditText) findViewById(R.id.et_device_num);
		// et_company_num = (EditText) findViewById(R.id.et_company_num);
		sp_factory_code = (Spinner) findViewById(R.id.sp_factory_code);

		layout_repair = (LinearLayout) findViewById(R.id.layout_repair);
		layout_maintain = (LinearLayout) findViewById(R.id.layout_maintain);
		layout_check = (LinearLayout) findViewById(R.id.layout_check);

		cb_order_type_maintain = (CheckBox) findViewById(R.id.cb_order_type_maintain);
		cb_order_type_repair = (CheckBox) findViewById(R.id.cb_order_type_repair);
		cb_order_type_check = (CheckBox) findViewById(R.id.cb_order_type_check);
		// cb_order_control_type_baoyang = (CheckBox)
		// findViewById(R.id.cb_order_control_type_baoyang);
		cb_order_control_type_zcwb = (CheckBox) findViewById(R.id.cb_order_control_type_zcwb);
		cb_order_control_type_dxgz = (CheckBox) findViewById(R.id.cb_order_control_type_dxgz);
		// cb_order_control_type_fj = (CheckBox)
		// findViewById(R.id.cb_order_control_type_fj);
		// cb_order_control_type_nj = (CheckBox)
		// findViewById(R.id.cb_order_control_type_nj);

		cb_order_control_type_feijian = (CheckBox) findViewById(R.id.cb_order_control_type_feijian);
		cb_order_control_type_zbjc = (CheckBox) findViewById(R.id.cb_order_control_type_zbjc);
		cb_order_control_type_fgszj = (CheckBox) findViewById(R.id.cb_order_control_type_fgszj);
		cb_order_control_type_njqzj = (CheckBox) findViewById(R.id.cb_order_control_type_njqzj);
		cb_order_control_type_nianjian = (CheckBox) findViewById(R.id.cb_order_control_type_nianjian);
		cb_order_control_type_qtjc = (CheckBox) findViewById(R.id.cb_order_control_type_qtjc);

		cb_order_control_type_guanren = (CheckBox) findViewById(R.id.cb_order_control_type_guanren);
		cb_order_control_type_jixiu = (CheckBox) findViewById(R.id.cb_order_control_type_jixiu);
		cb_order_control_type_peijiangenghuan = (CheckBox) findViewById(R.id.cb_order_control_type_peijiangenghuan);
		layout_error_info = (LinearLayout) findViewById(R.id.layout_error_info);
		et_error_code = (EditText) findViewById(R.id.et_error_code);
		et_error_desc = (EditText) findViewById(R.id.et_error_desc);

		tv_title.setText(R.string.main_sbwx);
		layout_back.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		btn_qr.setOnClickListener(this);
		cb_order_type_maintain.setOnClickListener(this);
		cb_order_type_repair.setOnClickListener(this);
		cb_order_type_check.setOnClickListener(this);

		cb_order_control_type_zcwb.setOnClickListener(this);
		cb_order_control_type_dxgz.setOnClickListener(this);
		// cb_order_control_type_fj.setOnClickListener(this);
		// cb_order_control_type_nj.setOnClickListener(this);
		cb_order_control_type_feijian.setOnClickListener(this);
		cb_order_control_type_zbjc.setOnClickListener(this);
		cb_order_control_type_fgszj.setOnClickListener(this);
		cb_order_control_type_njqzj.setOnClickListener(this);
		cb_order_control_type_nianjian.setOnClickListener(this);
		cb_order_control_type_qtjc.setOnClickListener(this);

		cb_order_control_type_guanren.setOnClickListener(this);
		cb_order_control_type_jixiu.setOnClickListener(this);
		cb_order_control_type_peijiangenghuan.setOnClickListener(this);
	}

	@Override
	public void bindData() {
		Bundle b = getIntent().getExtras();
		if (b != null) {
			// workorder_id = b.getString("workorder_id");
		} else {
			tv_title.setText(getString(R.string.main_sbwx) + "(手工单)");
		}
		new SJECHttpHandler(callbackVillage, ActivityAddWorkorder.this)
				.getFactoryCode();
	}

	AsyncHttpCallBack callbackVillage = new AsyncHttpCallBack() {
		@Override
		public void onSuccess(String response) {
			super.onSuccess(response);
			LogUtil.d("小区列表:%s", response);
			try {
				JSONObject jobj = new JSONObject(response);
				if (jobj.optString("result", "false").equals("true")) {
					GlobalData.listFactoryCode = JSONUtils.fromJson(jobj
							.optJSONArray("data").toString(),
							new TypeToken<ArrayList<EntityFactoryCode>>() {
							});
					listFactoryName.clear();
					for (int i = 0; i < GlobalData.listFactoryCode.size(); i++) {
						listFactoryName
								.add("("
										+ GlobalData.listFactoryCode.get(i).CompanyCode
										+ ")"
										+ GlobalData.listFactoryCode.get(i).CompanyName);
					}

					listFactoryName.add(0,
							getString(R.string.str_list_factory_code_default));

					aspnFactoryCode = new ArrayAdapter<String>(
							ActivityAddWorkorder.this,
							android.R.layout.simple_spinner_item,
							listFactoryName);
					aspnFactoryCode
							.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					sp_factory_code.setAdapter(aspnFactoryCode);
					sp_factory_code
							.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
								@Override
								public void onItemSelected(AdapterView<?> arg0,
										View arg1, int position, long arg3) {
									arg0.setVisibility(View.VISIBLE);
									if (position != 0) {
										factoryCode = GlobalData.listFactoryCode
												.get(position - 1).CompanyCode;
									} else {
										factoryCode = "";
									}
								}

								@Override
								public void onNothingSelected(
										AdapterView<?> arg0) {

								}
							});
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(String message) {
			super.onFailure(message);
			LogUtil.d("%s", message);
		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	private void AddWorkorder() {
		new SJECHttpHandler(callBack, this).addWorkorder(factoryCode,
				getText(et_device_num), order_type + "", control_type + "",
				getText(et_error_code), getText(et_error_desc));
	}

	private AsyncHttpCallBack callBack = new AsyncHttpCallBack() {

		public void onSuccess(String response) {
			LogUtil.d("%s", response);
			JSONObject jobj;
			try {
				jobj = new JSONObject(response);

				if (jobj.optString("result", "false").equals("true")) {
					Util.createToast(ActivityAddWorkorder.this,
							R.string.msg_add_workorder_success,
							Toast.LENGTH_SHORT).show();

					// 发送广播,刷新工单
					getApplication().sendBroadcast(
							new Intent(Constant.ACTION_REFRESH_WORKORDER_LIST));
					GlobalData.curWorkorder = new Gson().fromJson(
							jobj.optString("data", ""), EntityWorkorder.class);
					Bundle b = new Bundle();
					b.putString("workorder_id", GlobalData.curWorkorder.InnerID);
					// b.putInt("order_type", order_type);// 1:抢修,2:维保
					if (GlobalData.curWorkorder.Type == Constant.EnumWorkorderType.TYPE_WORKORDER_REPAIR) {
						Util.startActivity(ActivityAddWorkorder.this,
								ActivityDoRepairV2.class, b, true);
					} else if (GlobalData.curWorkorder.Type == Constant.EnumWorkorderType.TYPE_WORKORDER_MAINTAIN) {
						Util.startActivity(ActivityAddWorkorder.this,
								ActivityDoMaintainV2.class, b, true);
					} else if (GlobalData.curWorkorder.Type == Constant.EnumWorkorderType.TYPE_WORKORDER_CHECK) {
						Util.startActivity(ActivityAddWorkorder.this,
								ActivityDoCheck.class, b, true);
					}
				} else {
					String result = jobj.optString("desc", "");
					result = result.isEmpty() ? "" : "," + result;
					Util.createToast(
							ActivityAddWorkorder.this,
							getString(R.string.msg_add_workorder_failed, result),
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};

		public void onFailure(String message) {
			Util.createToast(ActivityAddWorkorder.this,
					getString(R.string.msg_add_workorder_failed, ""),
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
