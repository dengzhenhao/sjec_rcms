package com.sjec.rcms.activity.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.android.pushservice.PushManager;
import com.baidu.mobstat.StatService;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sjec.rcms.R;
import com.sjec.rcms.activity.BaseFragment;
import com.sjec.rcms.activity.login.ActivityLogin;
import com.sjec.rcms.activity.main.ActivityMain;
import com.sjec.rcms.activity.qr.CaptureActivity;
import com.sjec.rcms.activity.user.ActivityChangePwd;
import com.sjec.rcms.activity.user.ActivityConfirmList;
import com.sjec.rcms.activity.user.ActivityKunrenList;
import com.sjec.rcms.activity.user.ActivityListSparePart;
import com.sjec.rcms.activity.user.ActivityPauseList;
import com.sjec.rcms.dao.EntityDevice;
import com.sjec.rcms.dao.EntityKnowledgeType;
import com.sjec.rcms.dao.EntityUser;
import com.sjec.rcms.data.Constant;
import com.sjec.rcms.data.GlobalData;
import com.sjec.rcms.http.AsyncHttpCallBack;
import com.sjec.rcms.http.SJECHttpHandler;
import com.websharputil.common.LogUtil;
import com.websharputil.common.PrefUtil;
import com.websharputil.common.Util;
import com.websharputil.json.JSONUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Relation;
import android.provider.Settings.Global;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 个人其他操作
 * 
 * @类名称：FragmentMyInfo
 * @包名：com.sjec.rcms.fragment
 * @描述： TODO
 * @创建人： dengzh
 * @创建时间:2015-7-29 下午2:55:58
 * @版本 V1.0
 * @Copyright (c) 2015 by 苏州威博世网络科技有限公司.
 */
public class FragmentMyInfo extends BaseFragment implements
		View.OnClickListener {

	private TextView tv_login_name, tv_user_name, tv_user_telephone;
	private RelativeLayout layout_confirm_list;
	private RelativeLayout layout_pause_list;
	private RelativeLayout layout_kunren_list;
	private RelativeLayout layout_spare_part_list;
	private RelativeLayout layout_change_password;
	private RelativeLayout layout_cancel;
	private ImageView iv_confirm_count;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_myinfo, container, false);
		init(view);

		return view;
	}

	private void init(View view) {

		tv_login_name = (TextView) view.findViewById(R.id.tv_login_name);
		tv_user_name = (TextView) view.findViewById(R.id.tv_user_name);
		tv_user_telephone = (TextView) view
				.findViewById(R.id.tv_user_telephone);
		layout_confirm_list = (RelativeLayout) view
				.findViewById(R.id.layout_confirm_list);
		layout_change_password = (RelativeLayout) view
				.findViewById(R.id.layout_change_password);
		layout_pause_list = (RelativeLayout) view
				.findViewById(R.id.layout_pause_list);
		layout_kunren_list = (RelativeLayout) view
				.findViewById(R.id.layout_kunren_list);
		layout_spare_part_list = (RelativeLayout) view
				.findViewById(R.id.layout_spare_part_list);
		layout_cancel = (RelativeLayout) view.findViewById(R.id.layout_cancel);
		iv_confirm_count = (ImageView) view.findViewById(R.id.iv_confirm_count);

		layout_confirm_list.setOnClickListener(this);
		layout_change_password.setOnClickListener(this);
		layout_cancel.setOnClickListener(this);
		layout_pause_list.setOnClickListener(this);
		layout_kunren_list.setOnClickListener(this);
		layout_spare_part_list.setOnClickListener(this);
	}

	public void refreshConfirmCount() {
		new SJECHttpHandler(cb, getActivity())
				.getPauseConfirmListCountByUserID();
	}

	private void bindData() {
		tv_login_name.setText(getResources().getString(
				R.string.lab_setting_login_name, GlobalData.curUser.UserID));
		tv_user_name.setText(getResources().getString(
				R.string.lab_setting_user_name, GlobalData.curUser.UserName));
		tv_user_telephone.setText(getResources().getString(
				R.string.lab_setting_user_telephone,
				GlobalData.curUser.Telephone));
		refreshConfirmCount();

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
					String count = obj.optString("desc", "").trim();
					if (!count.isEmpty() && !count.equals("0")) {
						iv_confirm_count.setVisibility(View.VISIBLE);
					} else {
						iv_confirm_count.setVisibility(View.GONE);
					}
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
		Bundle b = new Bundle();
		switch (v.getId()) {
		case R.id.layout_confirm_list:
			Util.startActivity(getActivity(), ActivityConfirmList.class, false);
			break;
		case R.id.layout_pause_list:
			Util.startActivity(getActivity(), ActivityPauseList.class, false);
			break;
		case R.id.layout_kunren_list:
			Util.startActivity(getActivity(), ActivityKunrenList.class, false);
			break;
		case R.id.layout_spare_part_list:
			Util.startActivity(getActivity(), ActivityListSparePart.class,
					false);
			break;
		case R.id.layout_change_password:
			Util.startActivity(getActivity(), ActivityChangePwd.class, false);
			break;
		case R.id.layout_cancel:
			Dialog dialog = new AlertDialog.Builder(getActivity())
					.setTitle(R.string.msg_dialog_add_order_type_title)
					.setMessage(R.string.msg_confirm_cancel)
					.setPositiveButton(R.string.msg_dialog_confirm,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									PrefUtil.getEditor(getActivity())
											.remove("user").commit();
									new SJECHttpHandler(
											new AsyncHttpCallBack() {
												@Override
												public void onSuccess(
														String response) {

													super.onSuccess(response);
													LogUtil.d("%s", response);
												}

											}, getActivity()).UpdatePushInfo(
											"", "", "");
									PushManager.stopWork(getActivity());
									GlobalData.clear();
									Util.startActivity(getActivity(),
											ActivityLogin.class, true);
								}
							})
					.setNegativeButton(R.string.msg_dialog_cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									dialog.dismiss();
								}
							}).create();
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();

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
