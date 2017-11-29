package com.sjec.rcms.activity.user;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.sjec.rcms.R;
import com.sjec.rcms.activity.BaseActivity;
import com.sjec.rcms.dao.EntityDevice;
import com.sjec.rcms.dao.EntityUser;
import com.sjec.rcms.data.GlobalData;
import com.sjec.rcms.http.AsyncHttpCallBack;
import com.sjec.rcms.http.SJECHttpHandler;
import com.websharputil.code.DescUtil;
import com.websharputil.common.LogUtil;
import com.websharputil.common.PrefUtil;
import com.websharputil.common.Util;
import com.websharputil.json.JSONUtils;

/**
 * 
 * @类名称：ActivityChangePwd
 * @包名：com.sjec.rcms.activity.main
 * @描述： 修改密码
 * @创建人： dengzh
 * @创建时间:2015-12-9 下午2:13:14
 * @版本 V1.0
 * @Copyright (c) 2015 by 苏州威博世网络科技有限公司.
 */
public class ActivityChangePwd extends BaseActivity {

	private EditText et_password_old, et_password_new, et_password_new_confirm;
	private Button btn_change_pwd;
	private TextView tv_title;
	private LinearLayout layout_back;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_change_pwd:
			changePwd();
			break;
		case R.id.layout_back:
			finish();
			break;
		}
	}

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_change_pwd);
	}

	@Override
	public void init(Bundle savedInstanceState) {
		et_password_old = (EditText) findViewById(R.id.et_password_old);
		et_password_new = (EditText) findViewById(R.id.et_password_new);
		et_password_new_confirm = (EditText) findViewById(R.id.et_password_new_confirm);
		btn_change_pwd = (Button) findViewById(R.id.btn_change_pwd);
		tv_title = (TextView) findViewById(R.id.tv_title);
		layout_back = (LinearLayout) findViewById(R.id.layout_back);

	}

	@Override
	public void bindData() {
		tv_title.setText("修改密码");
		btn_change_pwd.setOnClickListener(this);
		layout_back.setOnClickListener(this);
	}

	private void changePwd() {
		if (isEmpty(et_password_new) || isEmpty(et_password_new_confirm)
				|| isEmpty(et_password_old)) {
			Util.createToast(this, "输入内容不能为空!", 3000).show();
			return;
		}

		try {
			if (!GlobalData.curUser.Password.equals(getText(et_password_old))) {
				Util.createToast(this, "原密码输入错误!", 3000).show();
				return;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		if (!getText(et_password_new).equals(getText(et_password_new_confirm))) {
			Util.createToast(this, "两次新密码输入不一致!", 3000).show();
			return;
		}
		try {
			new SJECHttpHandler(cbChangePwd, this).changePwd(
					getText(et_password_old), getText(et_password_new));
		} catch (Exception e) {
			Util.createToast(this, "操作失败!", 3000).show();
		}
	}

	AsyncHttpCallBack cbChangePwd = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);

				if (obj.optString("result").equals("true")) {
					Util.createToast(
							ActivityChangePwd.this,
							obj.optString("desc",
									getString(R.string.common_login_failed)),
							3000).show();

					try {
						GlobalData.curUser.Password = DescUtil
								.toHexString(DescUtil
										.encrypt(getText(et_password_new)));
					} catch (Exception e) {
						e.printStackTrace();
					}
					GsonBuilder builder = new GsonBuilder();
					Gson gson = builder.create();
					String jason = gson.toJson(GlobalData.curUser);
					PrefUtil.setPref(ActivityChangePwd.this, "user", jason);
					finish();
				} else {
					Util.createToast(
							ActivityChangePwd.this,
							obj.optString("desc",
									getString(R.string.common_login_failed)),
							3000).show();
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
