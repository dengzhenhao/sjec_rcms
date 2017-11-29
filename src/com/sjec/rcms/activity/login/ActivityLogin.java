package com.sjec.rcms.activity.login;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sjec.rcms.R;
import com.sjec.rcms.activity.BaseActivity;
import com.sjec.rcms.activity.main.ActivityMain;
import com.sjec.rcms.activity.qr.CaptureActivity;
import com.sjec.rcms.dao.EntityKnowledgeType;
import com.sjec.rcms.dao.EntityUser;
import com.sjec.rcms.data.Constant;
import com.sjec.rcms.data.GlobalData;
import com.sjec.rcms.http.AsyncHttpCallBack;
import com.sjec.rcms.http.AsyncHttpUtil;
import com.sjec.rcms.http.SJECHttpHandler;
import com.websharputil.code.DescUtil;
import com.websharputil.common.AppData;
import com.websharputil.common.ConvertUtil;
import com.websharputil.common.LogUtil;
import com.websharputil.common.PrefUtil;
import com.websharputil.common.Util;
import com.websharputil.json.JSONUtils;
import com.websharputil.widget.ThumbImageView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActivityLogin extends BaseActivity {

	private TextView tv_regist;
	private EditText et_username;
	private EditText et_password;
	private ThumbImageView iv_login;
	private LinearLayout loading;

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (Constant.ACTION_BROADCAST_REGIST_SUCCESS.equals(intent
					.getAction())) {
				// 注册成功了,这里在用户名和密码输入框进行赋值

				String username = intent.getStringExtra("username");
				String password = intent.getStringExtra("password");
				LogUtil.d("%s,%s", username, password);
				et_username.setText(username);
				et_password.setText(password);

			}
		}
	};

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_login);
		// Intent intent = new Intent(
		// android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		// startActivity(intent);

	}

	@Override
	public void init(Bundle savedInstanceState) {

		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.ACTION_BROADCAST_REGIST_SUCCESS);
		registerReceiver(receiver, filter);

		loading = (LinearLayout) findViewById(R.id.loading);
		tv_regist = (TextView) findViewById(R.id.tv_regist);
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		iv_login = (ThumbImageView) findViewById(R.id.iv_login);

		tv_regist.setOnClickListener(this);
		iv_login.setOnClickListener(this);
		et_username.setText(PrefUtil.getPref(ActivityLogin.this, "lastphone",
				"").trim());
		et_username.setSelection(et_username.getText().toString().trim()
				.length());
	}

	@Override
	public void bindData() {
		

		String prefUser = PrefUtil.getPref(ActivityLogin.this, "user", "");
		if (!prefUser.isEmpty()) {
			GlobalData.curUser = JSONUtils.fromJson(prefUser, EntityUser.class);
			try {
				GlobalData.WorkerStatus = ConvertUtil.ParsetStringToInt32(
						GlobalData.curUser.Description
								.substring(GlobalData.curUser.Description
										.trim().length() - 1), 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//Util.startActivity(ActivityLogin.this, ActivityMain.class, true);
			et_username.setText(GlobalData.curUser.UserID);
			et_password.setText(GlobalData.curUser.Password);
			try {
				AsyncLogin();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// et_username.setText("18934597841");
		// et_password.setText("123456");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_login:
			try {
				AsyncLogin();
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;
		case R.id.tv_regist:
			Util.startActivity(ActivityLogin.this, ActivityRegist.class, false);
			break;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	AsyncHttpCallBack cb = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);

				if (obj.optString("result").equals("true")) {
					String str = PrefUtil.getPref(ActivityLogin.this, "user",
							"");

					PrefUtil.setPref(ActivityLogin.this, "user", obj
							.getJSONObject("data").toString());
					GlobalData.curUser = JSONUtils.fromJson(
							obj.getJSONObject("data").toString(),
							EntityUser.class);
					try {
						GlobalData.WorkerStatus = ConvertUtil
								.ParsetStringToInt32(
										GlobalData.curUser.Description
												.substring(GlobalData.curUser.Description
														.trim().length() - 1),
										0);
					} catch (Exception e) {
						e.printStackTrace();
					}
					PrefUtil.setPref(ActivityLogin.this, "lastphone",
							GlobalData.curUser.Telephone.trim());
					Util.startActivity(ActivityLogin.this, ActivityMain.class,
							true);
				} else {
					Util.createToast(
							ActivityLogin.this,
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

	private void AsyncLogin() throws Exception {

		new SJECHttpHandler(cb, this).login(et_username.getText().toString()
				.trim(), et_password.getText().toString().trim());
	}
}
