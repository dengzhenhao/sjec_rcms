package com.sjec.rcms.activity.login;

import org.json.JSONException;
import org.json.JSONObject;


import com.sjec.rcms.R;
import com.sjec.rcms.activity.BaseActivity;
import com.sjec.rcms.data.Constant;
import com.sjec.rcms.http.AsyncHttpCallBack;
import com.sjec.rcms.http.SJECHttpHandler;
import com.websharputil.common.LogUtil;
import com.websharputil.common.PatterUtil;
import com.websharputil.common.Util;
import com.websharputil.widget.ThumbImageView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActivityRegist extends BaseActivity {

	private LinearLayout layout_back;
	private TextView tv_title, tv_get_verify_code, tv_time_countdown,
			tv_back_to_login;
	private EditText et_username, et_verify_code, et_password,
			et_password_confirm;
	private ThumbImageView iv_regist;
	private static final int VALID_TIME = 120;
	private int timeCountDown = 0;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (timeCountDown == 0) {
					tv_get_verify_code.setVisibility(View.VISIBLE);
					tv_time_countdown.setVisibility(View.GONE);
					iv_regist.setEnabled(false);
				} else {
					timeCountDown--;
					tv_time_countdown.setText("(" + timeCountDown + "s)");
					Message message = new Message();
					message.what = 1;
					handler.sendMessageDelayed(message, 1000);
				}
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_regist);
	}

	@Override
	public void init(Bundle savedInstanceState) {
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_get_verify_code = (TextView) findViewById(R.id.tv_get_verify_code);
		tv_time_countdown = (TextView) findViewById(R.id.tv_time_countdown);
		tv_back_to_login = (TextView) findViewById(R.id.tv_back_to_login);
		et_username = (EditText) findViewById(R.id.et_username);
		et_verify_code = (EditText) findViewById(R.id.et_verify_code);
		et_password = (EditText) findViewById(R.id.et_password);
		et_password_confirm = (EditText) findViewById(R.id.et_password_confirm);
		iv_regist = (ThumbImageView) findViewById(R.id.iv_regist);
		layout_back.setOnClickListener(this);
		tv_get_verify_code.setOnClickListener(this);
		tv_back_to_login.setOnClickListener(this);
		iv_regist.setOnClickListener(this);
		// iv_regist.setEnabled(false);
	}

	@Override
	public void bindData() {
		tv_title.setText(R.string.title_label_regist);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_back:
			finish();
			break;
		case R.id.tv_get_verify_code:
			getVerifyCode();
			break;
		case R.id.tv_back_to_login:
			finish();
			break;
		case R.id.iv_regist:
			try {
				regist();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
	}

	private void regist() throws Exception {
		int msgID = 0;
		if (et_username.getText().toString().trim().isEmpty()
				|| !PatterUtil
						.isMobile(et_username.getText().toString().trim())) {
			msgID = R.string.msg_error_value_telephone;
		} else if (et_verify_code.getText().toString().trim().isEmpty()) {
			msgID = R.string.msg_error_value_verify_code;
		} else if (et_password.getText().toString().trim().isEmpty()) {
			msgID = R.string.msg_error_password;
		} else if (et_password_confirm.getText().toString().trim().isEmpty()) {
			msgID = R.string.msg_error_password_confirm;
		} else if (!et_password.getText().toString().trim()
				.equals(et_password_confirm.getText().toString().trim())) {
			msgID = R.string.msg_error_password_repeat;
			et_password.setText("");
			et_password_confirm.setText("");
		}

		if (msgID != 0) {
			Util.createToast(ActivityRegist.this, msgID, 3000).show();
			return;
		}
		new SJECHttpHandler(new AsyncHttpCallBack() {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				LogUtil.d("%s", response);
				try {
					JSONObject obj = new JSONObject(response);
					if (obj.optString("result", "").equals("true")) {
						Intent intent = new Intent(
								Constant.ACTION_BROADCAST_REGIST_SUCCESS);
						Bundle b = new Bundle();
						b.putString("username", getText(et_username));
						b.putString("password", getText(et_password));
						intent.putExtras(b);
						getApplication().sendBroadcast(intent);
						Util.createToast(ActivityRegist.this,
								R.string.msg_error_regist_success, 3000).show();
						finish();
					} else {
						Util.createToast(ActivityRegist.this,
								obj.optString("desc", ""), 3000).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(String message) {
				super.onFailure(message);
				LogUtil.d("%s", message);
			}

		}, ActivityRegist.this).regist(getText(et_username),
				getText(et_username), getText(et_password),
				getText(et_verify_code));
	}

	private void getVerifyCode() {
		if (et_username.getText().toString().trim().isEmpty()
				|| !PatterUtil
						.isMobile(et_username.getText().toString().trim())) {
			Util.createToast(ActivityRegist.this,
					R.string.msg_error_value_telephone, 3000).show();
			return;
		}
		new SJECHttpHandler(new AsyncHttpCallBack() {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				LogUtil.d("%s", response);
				// 判断是否发送成功,把注册按钮置为可用,并且显示倒显示
				try {
					JSONObject obj = new JSONObject(response);
					// if (obj.optString("result", "").equals("true")) {
					// iv_regist.setEnabled(true);
					// tv_get_verify_code.setVisibility(View.GONE);
					// tv_time_countdown.setVisibility(View.VISIBLE);
					// timeCountDown = 120;
					// Message msg = new Message();
					// msg.what = 1;
					// handler.sendMessageDelayed(msg, 1000);
					// }
					tv_get_verify_code.setVisibility(View.GONE);
					tv_time_countdown.setVisibility(View.VISIBLE);
					timeCountDown = VALID_TIME;
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessageDelayed(msg, 1000);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(String message) {
				super.onFailure(message);
				LogUtil.d("%s", message);
			}
		}, ActivityRegist.this).getVerifyCode(et_username.getText().toString()
				.trim());
	}
}
