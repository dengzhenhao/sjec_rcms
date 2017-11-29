package com.sjec.rcms.activity.workorder;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Instrumentation.ActivityResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Global;
import android.renderscript.Type;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.sjec.rcms.R;
import com.sjec.rcms.activity.BaseActivity;
import com.sjec.rcms.activity.device.ActivityCollectInfo;
import com.sjec.rcms.activity.qr.CaptureActivity;
import com.sjec.rcms.dao.EntityDevice;
import com.sjec.rcms.dao.EntityMaintain;
import com.sjec.rcms.dao.EntityRepair;
import com.sjec.rcms.dao.EntityWorkorder;
import com.sjec.rcms.dao.EntityWorkorderPushLog;
import com.sjec.rcms.data.Constant;
import com.sjec.rcms.data.Constant.EnumWorkorderPauseStatus;
import com.sjec.rcms.data.Constant.EnumWorkorderType;
import com.sjec.rcms.data.GlobalData;
import com.sjec.rcms.http.AsyncHttpCallBack;
import com.sjec.rcms.http.SJECHttpHandler;
import com.sjec.rcms.util.SJECUtil;
import com.websharputil.common.AppData;
import com.websharputil.common.ConvertUtil;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;
import com.websharputil.date.DateUtil;
import com.websharputil.widget.ThumbImageView;

/**
 * 
 * @类名称：ActivityDoMaintain
 * @包名：com.sjec.rcms.activity.maintain
 * @描述： 抢修工单详细页面
 * @创建人： dengzh
 * @创建时间:2015-8-7 下午4:09:12
 * @版本 V1.0
 * @Copyright (c) 2015 by 苏州威博世网络科技有限公司.
 */
public class ActivityDoRepair extends BaseActivity {
	private TextView tv_title;
	private LinearLayout layout_back;
	// private int order_type = Constant.EnumWorkorderType.TYPE_WORKORDER_2;
	private String workorder_id = "";

	// LocationClient mLocClient;
	// public MyLocationListenner myListener = new MyLocationListenner();
	// private LocationMode mCurrentMode;
	// BitmapDescriptor mCurrentMarker;
	// MapView mMapView;
	// BaiduMap mBaiduMap;
	boolean isFirstLoc = true;// 是否首次定位
	private ThumbImageView btn_location;

	// 概 要信息
	private TextView tv_xqmc, tv_xqdz, tv_sbh, tv_zh, tv_th, tv_latlng;
	// 基本信息
	private TextView tv_xmmc, tv_hth, tv_txtz, tv_dtxh, tv_jhrq, tv_zzcs,
			tv_wbgs, tv_wbsqh, tv_wbxx, tv_cpgg, tv_sydw, tv_sbdz;

	private TextView tv_base_info, tv_maintain, tv_workorder_status,
			tv_workorder_starttime, tv_workorder_begin, tv_workorder_complete,
			tv_workorder_result, et_workorder_remark, tv_workorder_receiver,
			tv_workorder_desc, tv_workorder_device_num,
			tv_workorder_event_source;

	private Button btn_begin_workorder, btn_complete_workorder,
			btn_receive_workorder, btn_complete_workorder_qr;

	private TextView tv_workorder_status_pause;
	private Button btn_pause_workorder, btn_continue_workorder,
			btn_disable_workorder, btn_init_workorder;

	private LinearLayout layout_device_info, layout_maintain_step;

	// private LatLng latLng = null;
	// private InfoWindow mInfoWindow;
	// private LatLng deviceLatLng = null;
	private RelativeLayout layout_map;
	private TextView tv_collect_map, tv_collect_push_list;

	private LinearLayout layout_push_log;
	private PullToRefreshScrollView scroll_maintain;

	private ArrayList<EntityWorkorderPushLog> listPushLog = new ArrayList<EntityWorkorderPushLog>();

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Constant.ACTION_REFRESH_WORKORDER)) {
				queryDeviceAndWorkorderInfo();
			}
		}
	};

	@Override
	public void onClick(View v) {
		Bundle b = new Bundle();
		b.putString("workorder_id", workorder_id);
		switch (v.getId()) {

		case R.id.layout_back:
			finish();
			break;
		case R.id.btn_receive_workorder:
			Util.createDialog(this, null,
					R.string.msg_dialog_add_order_type_title,
					R.string.msg_receive_workorder_confirm, null, true, false,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 更新状态为开始
							updateWorkorderReceiver();
						}
					}).show();
			break;
		case R.id.tv_base_info:
			layout_device_info.setVisibility(View.VISIBLE);
			layout_maintain_step.setVisibility(View.GONE);
			ChangeBottomStyle(v);
			break;
		case R.id.tv_maintain:
			layout_device_info.setVisibility(View.GONE);
			layout_maintain_step.setVisibility(View.VISIBLE);
			ChangeBottomStyle(v);
			break;
		case R.id.btn_begin_workorder:
			Util.createDialog(this, null,
					R.string.msg_dialog_add_order_type_title,
					R.string.msg_begin_workorder_confirm, null, true, false,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 更新状态为开始
							updateWorkorderStatus(Constant.EnumWorkorderStatus.STATUS_WORKORDER_2
									+ "");
						}
					}).show();
			break;
		case R.id.btn_complete_workorder:
			Util.createDialog(this, null,
					R.string.msg_dialog_add_order_type_title,
					R.string.msg_complete_workorder_confirm, null, true, false,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 更新状态为完成
							updateWorkorderStatus(Constant.EnumWorkorderStatus.STATUS_WORKORDER_3
									+ "");
						}
					}).show();
			break;
		case R.id.btn_complete_workorder_qr:

			b.putInt("scan_type", Constant.SCAN_TYPE3);
			Intent intent = new Intent(ActivityDoRepair.this,
					CaptureActivity.class);
			intent.putExtras(b);
			startActivityForResult(intent, 99);
			break;
		case R.id.btn_pause_workorder:
			b.putString("title", "请求暂停");
			b.putInt(
					"type",
					Constant.EnumWorkorderPauseStatus.PAUSE_STATUS_WORKORDER_PAUSE);
			Util.startActivity(ActivityDoRepair.this,
					ActivityPauseReason.class, b, false);
			// Util.createDialog(this, null,
			// R.string.msg_dialog_add_order_type_title,
			// R.string.msg_pause_workorder_confirm, null, true, false,
			// new DialogInterface.OnClickListener() {
			// @Override
			// public void onClick(DialogInterface dialog, int which) {
			// // 更新状态为完成
			// updateWorkorderPauseStatus(0
			// + "");
			// }
			// }).show();
			break;
		case R.id.btn_continue_workorder:
			b.putString("title", "继续工作");
			b.putInt(
					"type",
					Constant.EnumWorkorderPauseStatus.PAUSE_STATUS_WORKORDER_CONTINUE);
			Util.startActivity(ActivityDoRepair.this,
					ActivityPauseReason.class, b, false);
			break;
		case R.id.btn_disable_workorder:
			b.putString("title", "请求作废");
			b.putInt(
					"type",
					Constant.EnumWorkorderPauseStatus.PAUSE_STATUS_WORKORDER_INVALID);
			Util.startActivity(ActivityDoRepair.this,
					ActivityPauseReason.class, b, false);
			break;
		case R.id.btn_init_workorder:
			b.putString("title", "放弃本单");
			b.putString("init", "1");
			b.putInt(
					"type",
					Constant.EnumWorkorderPauseStatus.PAUSE_STATUS_WORKORDER_CONTINUE);
			Util.startActivity(ActivityDoRepair.this,
					ActivityPauseReason.class, b, false);
			break;
		case R.id.tv_collect_map:
			if (layout_map.getVisibility() == View.GONE) {
				layout_map.setVisibility(View.VISIBLE);
			} else {
				layout_map.setVisibility(View.GONE);
			}
			break;
		case R.id.tv_collect_push_list:
			if (layout_push_log.getVisibility() == View.GONE) {
				layout_push_log.setVisibility(View.VISIBLE);
				Handler handler = new Handler();
				handler.post(new Runnable() {
					@Override
					public void run() {
						scroll_maintain.getRefreshableView().fullScroll(
								ScrollView.FOCUS_DOWN);
					}
				});
			} else {
				layout_push_log.setVisibility(View.GONE);
			}
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK == resultCode) {
			if (requestCode == 99) {
				try {
					String data_result = data.getExtras().getString("data", "");
					String[] arrDeviceInfo = SJECUtil
							.GetDeviceInfoArrayFromQrCode(data_result);
					String deviceNum = arrDeviceInfo[2].trim();
					if (!deviceNum.equals(GlobalData.curDevice.Device_Num)) {
						btn_complete_workorder.setVisibility(View.VISIBLE);
						Util.createToast(
								ActivityDoRepair.this,
								getResources()
										.getString(
												R.string.str_show_manual_submit_workorder,
												deviceNum,
												GlobalData.curDevice.Device_Num),
								3000).show();
					} else {
						Util.createDialog(this, null,
								R.string.msg_dialog_add_order_type_title,
								R.string.msg_complete_workorder_confirm, null,
								true, false,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// 更新状态为完成
										updateWorkorderStatus(Constant.EnumWorkorderStatus.STATUS_WORKORDER_3
												+ "");
									}
								}).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
					Util.createToast(ActivityDoRepair.this,
							R.string.error_qr_scan, 3000).show();
				}
			}
		}
	}

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_workorder_do_repair);
	}

	@Override
	public void init(Bundle savedInstanceState) {
		IntentFilter filter = new IntentFilter(
				Constant.ACTION_REFRESH_WORKORDER);
		registerReceiver(receiver, filter);
		// mCurrentMode = LocationMode.NORMAL;
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		// mMapView = (MapView) findViewById(R.id.map_district);
		tv_xqmc = (TextView) findViewById(R.id.tv_xqmc);
		tv_xqdz = (TextView) findViewById(R.id.tv_xqdz);
		tv_sbh = (TextView) findViewById(R.id.tv_sbh);
		tv_zh = (TextView) findViewById(R.id.tv_zh);
		tv_th = (TextView) findViewById(R.id.tv_th);
		tv_latlng = (TextView) findViewById(R.id.tv_latlng);

		tv_xmmc = (TextView) findViewById(R.id.tv_xmmc);
		tv_hth = (TextView) findViewById(R.id.tv_hth);
		tv_txtz = (TextView) findViewById(R.id.tv_txtz);
		tv_dtxh = (TextView) findViewById(R.id.tv_dtxh);
		tv_jhrq = (TextView) findViewById(R.id.tv_jhrq);
		tv_zzcs = (TextView) findViewById(R.id.tv_zzcs);
		tv_wbgs = (TextView) findViewById(R.id.tv_wbgs);
		tv_wbsqh = (TextView) findViewById(R.id.tv_wbsqh);
		tv_wbxx = (TextView) findViewById(R.id.tv_wbxx);
		tv_cpgg = (TextView) findViewById(R.id.tv_cpgg);
		tv_sydw = (TextView) findViewById(R.id.tv_sydw);
		tv_sbdz = (TextView) findViewById(R.id.tv_sbdz);
		tv_workorder_starttime = (TextView) findViewById(R.id.tv_workorder_starttime);
		// btn_begin_maintain = (Button)findViewById(R.id.btn_begin_maintain);

		tv_workorder_event_source = (TextView) findViewById(R.id.tv_workorder_event_source);
		tv_workorder_status = (TextView) findViewById(R.id.tv_workorder_status);
		tv_base_info = (TextView) findViewById(R.id.tv_base_info);
		tv_maintain = (TextView) findViewById(R.id.tv_maintain);
		tv_workorder_begin = (TextView) findViewById(R.id.tv_workorder_begin);
		tv_workorder_complete = (TextView) findViewById(R.id.tv_workorder_complete);
		tv_workorder_result = (TextView) findViewById(R.id.tv_workorder_result);
		tv_workorder_desc = (TextView) findViewById(R.id.tv_workorder_desc);
		et_workorder_remark = (TextView) findViewById(R.id.et_workorder_remark);
		tv_workorder_device_num = (TextView) findViewById(R.id.tv_workorder_device_num);

		btn_begin_workorder = (Button) findViewById(R.id.btn_begin_workorder);
		btn_complete_workorder = (Button) findViewById(R.id.btn_complete_workorder);
		btn_complete_workorder_qr = (Button) findViewById(R.id.btn_complete_workorder_qr);
		tv_workorder_status_pause = (TextView) findViewById(R.id.tv_workorder_status_pause);
		btn_pause_workorder = (Button) findViewById(R.id.btn_pause_workorder);
		btn_continue_workorder = (Button) findViewById(R.id.btn_continue_workorder);
		btn_disable_workorder = (Button) findViewById(R.id.btn_disable_workorder);
		btn_init_workorder = (Button) findViewById(R.id.btn_init_workorder);

		layout_device_info = (LinearLayout) findViewById(R.id.layout_device_info);
		layout_maintain_step = (LinearLayout) findViewById(R.id.layout_maintain_step);

		tv_workorder_receiver = (TextView) findViewById(R.id.tv_workorder_receiver);
		btn_receive_workorder = (Button) findViewById(R.id.btn_receive_workorder);

		layout_map = (RelativeLayout) findViewById(R.id.layout_map);
		tv_collect_map = (TextView) findViewById(R.id.tv_collect_map);
		tv_collect_push_list = (TextView) findViewById(R.id.tv_collect_push_list);

		layout_push_log = (LinearLayout) findViewById(R.id.layout_push_log);

		scroll_maintain = (PullToRefreshScrollView) findViewById(R.id.scroll_maintain);

		scroll_maintain.setMode(Mode.PULL_FROM_START);

		scroll_maintain
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase refreshView) {
						// TODO Auto-generated method stub
						queryDeviceAndWorkorderInfo();
					}

					@Override
					public void onPullUpToRefresh(PullToRefreshBase refreshView) {
						// TODO Auto-generated method stub

					}
				});

		tv_base_info.setOnClickListener(this);
		tv_maintain.setOnClickListener(this);
		layout_back.setOnClickListener(this);
		btn_location.setOnClickListener(this);
		btn_begin_workorder.setOnClickListener(this);
		btn_complete_workorder.setOnClickListener(this);
		btn_complete_workorder_qr.setOnClickListener(this);
		btn_receive_workorder.setOnClickListener(this);

		btn_pause_workorder.setOnClickListener(this);
		btn_continue_workorder.setOnClickListener(this);
		btn_disable_workorder.setOnClickListener(this);
		btn_init_workorder.setOnClickListener(this);
		tv_collect_map.setOnClickListener(this);
		tv_collect_push_list.setOnClickListener(this);
		// btn_begin_maintain.setOnClickListener(this);

		// scroll_maintain.setRefreshing(true);
		btn_pause_workorder.setEnabled(false);
		btn_continue_workorder.setEnabled(false);
		btn_disable_workorder.setEnabled(false);
		btn_init_workorder.setEnabled(false);
		btn_pause_workorder.setBackgroundResource(R.drawable.frame_disabled);
		btn_continue_workorder.setBackgroundResource(R.drawable.frame_disabled);
		btn_disable_workorder.setBackgroundResource(R.drawable.frame_disabled);
		btn_init_workorder.setBackgroundResource(R.drawable.frame_disabled);

		btn_begin_workorder.setEnabled(false);
		btn_complete_workorder.setEnabled(false);
		btn_receive_workorder.setEnabled(false);
		btn_complete_workorder_qr.setEnabled(false);
		btn_begin_workorder.setBackgroundResource(R.drawable.frame_disabled);
		btn_complete_workorder.setBackgroundResource(R.drawable.frame_disabled);
		btn_receive_workorder.setBackgroundResource(R.drawable.frame_disabled);
		btn_complete_workorder_qr
				.setBackgroundResource(R.drawable.frame_disabled);
	}

	@Override
	public void bindData() {
		if (!BaseActivity.isInitParam) {
			BaseActivity.initParam(this);
		}
		tv_maintain.performClick();

		Bundle b = getIntent().getExtras();
		workorder_id = b.getString("workorder_id", "");
		// order_type = b.getInt("order_type",
		// Constant.EnumWorkorderType.TYPE_WORKORDER_2);
		// 加载工单信息,和梯的基本信息
		tv_title.setText(R.string.title_repair);

		BindMap();
		queryDeviceAndWorkorderInfo();

	}

	private void BindWorkorderData() {
		tv_workorder_event_source.setText(GlobalData.curWorkorder.EventSource);
		tv_workorder_device_num.setText(GlobalData.curWorkorder.Device_Num);
		if (GlobalData.curWorkorder.Status == -1) {
			tv_workorder_status.setText(getResources().getStringArray(
					R.array.arr_status_workorder)[0]);
		} else {
			tv_workorder_status
					.setText(getResources().getStringArray(
							R.array.arr_status_workorder)[GlobalData.curWorkorder.Status]);
		}

		try {
			tv_workorder_starttime.setText(GlobalData.curWorkorder.StartTime);
		} catch (Exception e) {
			tv_workorder_starttime.setText("-");
		}

		if (GlobalData.curWorkorder.OrderPause == null) {
			tv_workorder_status_pause
					.setText(EnumWorkorderPauseStatus
							.GetName(EnumWorkorderPauseStatus.PAUSE_STATUS_WORKORDER_CONTINUE));
		} else {
			tv_workorder_status_pause.setText(EnumWorkorderPauseStatus
					.GetName(GlobalData.curWorkorder.OrderPause));

		}
		// if (order_type == Constant.EnumWorkorderType.TYPE_WORKORDER_1) {
		try {
			tv_workorder_desc.setText(getString(
					R.string.str_maintain_desc_template_repair,
					GlobalData.curRepair.Error_Info,
					GlobalData.curRepair.Error_Detail));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// } else {
		// try {
		//
		// tv_workorder_desc.setText(getString(
		// R.string.str_maintain_desc_template_maintain, DateUtil
		//
		// .TimeParseStringToFormatString(
		// GlobalData.curMaintain.ProvisionMaintainTime,
		// "yyyy年MM月dd日")));
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// 判断有没有受理人,如果没有,就显示受理按 钮
		// 如果有,就显示受理人名称
		if (GlobalData.curWorkorder.ChargeUserID == null
				|| GlobalData.curWorkorder.ChargeUserID.isEmpty()) {
			tv_workorder_receiver.setVisibility(View.GONE);
			btn_receive_workorder.setVisibility(View.VISIBLE);
			btn_begin_workorder.setVisibility(View.GONE);

			btn_receive_workorder.setEnabled(true);
			btn_receive_workorder.setBackgroundResource(R.drawable.frame_app);
		} else {
			// 赋值
			tv_workorder_receiver.setText(getString(
					R.string.str_maintain_receiver,
					GlobalData.curWorkorder.ChargeUserName + "("
							+ GlobalData.curWorkorder.ChargeUserPhone + ")"));
			tv_workorder_receiver.setVisibility(View.VISIBLE);
			btn_receive_workorder.setVisibility(View.GONE);
			btn_begin_workorder.setVisibility(View.VISIBLE);
			btn_begin_workorder.setEnabled(true);
			btn_begin_workorder.setBackgroundResource(R.drawable.frame_app);
		}

		// 根据工单状态处理各类显示
		if (GlobalData.curWorkorder.Status == Constant.EnumWorkorderStatus.STATUS_WORKORDER_1) {
			btn_complete_workorder.setEnabled(false);
			btn_complete_workorder
					.setBackgroundResource(R.drawable.frame_disabled);

			btn_complete_workorder_qr.setEnabled(false);
			btn_complete_workorder_qr
					.setBackgroundResource(R.drawable.frame_disabled);
			btn_pause_workorder.setEnabled(false);
			btn_continue_workorder.setEnabled(false);
			btn_disable_workorder.setEnabled(false);

			btn_pause_workorder
					.setBackgroundResource(R.drawable.frame_disabled);
			btn_continue_workorder
					.setBackgroundResource(R.drawable.frame_disabled);
			btn_disable_workorder
					.setBackgroundResource(R.drawable.frame_disabled);

			if (GlobalData.curWorkorder.ChargeUserID == null
					|| GlobalData.curWorkorder.ChargeUserID.isEmpty()) {
				// 未受理
				btn_init_workorder.setEnabled(false);
				btn_init_workorder
						.setBackgroundResource(R.drawable.frame_disabled);
			} else {
				btn_init_workorder.setEnabled(true);
				btn_init_workorder.setBackgroundResource(R.drawable.frame_app);

				if (GlobalData.curWorkorder.OrderPause == null) {
					btn_pause_workorder.setEnabled(false);
					btn_continue_workorder.setEnabled(false);
					btn_disable_workorder.setEnabled(false);

					btn_pause_workorder
							.setBackgroundResource(R.drawable.frame_disabled);
					btn_continue_workorder
							.setBackgroundResource(R.drawable.frame_disabled);
					btn_disable_workorder
							.setBackgroundResource(R.drawable.frame_disabled);
				} else {
					if (GlobalData.curWorkorder.OrderPause == -1
							|| GlobalData.curWorkorder.OrderPause == 0
							|| GlobalData.curWorkorder.OrderPause == 99) {
						btn_pause_workorder.setEnabled(false);
						btn_continue_workorder.setEnabled(true);
						btn_disable_workorder.setEnabled(false);

						btn_pause_workorder
								.setBackgroundResource(R.drawable.frame_disabled);
						btn_continue_workorder
								.setBackgroundResource(R.drawable.frame_app);
						btn_disable_workorder
								.setBackgroundResource(R.drawable.frame_disabled);
					} else {
						btn_pause_workorder.setEnabled(true);
						btn_continue_workorder.setEnabled(false);
						btn_disable_workorder.setEnabled(true);

						btn_pause_workorder
								.setBackgroundResource(R.drawable.frame_app);
						btn_continue_workorder
								.setBackgroundResource(R.drawable.frame_disabled);
						btn_disable_workorder
								.setBackgroundResource(R.drawable.frame_app);
					}
				}
			}

		} else if (GlobalData.curWorkorder.Status == Constant.EnumWorkorderStatus.STATUS_WORKORDER_2) {
			btn_begin_workorder.setEnabled(false);
			btn_begin_workorder
					.setBackgroundResource(R.drawable.frame_disabled);
			btn_complete_workorder.setEnabled(true);
			btn_complete_workorder.setBackgroundResource(R.drawable.frame_app);

			btn_complete_workorder_qr.setEnabled(true);
			btn_complete_workorder_qr
					.setBackgroundResource(R.drawable.frame_app);

			btn_init_workorder.setEnabled(true);
			btn_init_workorder.setBackgroundResource(R.drawable.frame_app);
			if (GlobalData.curWorkorder.OrderPause == null) {
				btn_pause_workorder.setEnabled(false);
				btn_continue_workorder.setEnabled(false);
				btn_disable_workorder.setEnabled(false);

				btn_pause_workorder
						.setBackgroundResource(R.drawable.frame_disabled);
				btn_continue_workorder
						.setBackgroundResource(R.drawable.frame_disabled);
				btn_disable_workorder
						.setBackgroundResource(R.drawable.frame_disabled);
			} else {
				if (GlobalData.curWorkorder.OrderPause == -1
						|| GlobalData.curWorkorder.OrderPause == 0
						|| GlobalData.curWorkorder.OrderPause == 99) {
					btn_pause_workorder.setEnabled(false);
					btn_continue_workorder.setEnabled(true);
					btn_disable_workorder.setEnabled(false);

					btn_pause_workorder
							.setBackgroundResource(R.drawable.frame_disabled);
					btn_continue_workorder
							.setBackgroundResource(R.drawable.frame_app);
					btn_disable_workorder
							.setBackgroundResource(R.drawable.frame_disabled);
				} else {
					btn_pause_workorder.setEnabled(true);
					btn_continue_workorder.setEnabled(false);
					btn_disable_workorder.setEnabled(true);

					btn_pause_workorder
							.setBackgroundResource(R.drawable.frame_app);
					btn_continue_workorder
							.setBackgroundResource(R.drawable.frame_disabled);
					btn_disable_workorder
							.setBackgroundResource(R.drawable.frame_app);
				}
			}

		} else if (GlobalData.curWorkorder.Status == Constant.EnumWorkorderStatus.STATUS_WORKORDER_3
				|| GlobalData.curWorkorder.Status == Constant.EnumWorkorderStatus.STATUS_WORKORDER_INVALID) {
			btn_begin_workorder.setEnabled(false);
			btn_complete_workorder.setEnabled(false);
			btn_complete_workorder_qr.setEnabled(false);

			btn_begin_workorder
					.setBackgroundResource(R.drawable.frame_disabled);
			btn_complete_workorder
					.setBackgroundResource(R.drawable.frame_disabled);
			btn_complete_workorder_qr
					.setBackgroundResource(R.drawable.frame_disabled);

			btn_pause_workorder.setEnabled(false);
			btn_continue_workorder.setEnabled(false);
			btn_disable_workorder.setEnabled(false);
			btn_init_workorder.setEnabled(false);

			btn_pause_workorder
					.setBackgroundResource(R.drawable.frame_disabled);
			btn_continue_workorder
					.setBackgroundResource(R.drawable.frame_disabled);
			btn_disable_workorder
					.setBackgroundResource(R.drawable.frame_disabled);
			btn_init_workorder.setBackgroundResource(R.drawable.frame_disabled);
		}
	}

	private void BindDeviceData() {

		tv_xqmc.setText(GlobalData.curDevice.Village_Name);
		tv_xqdz.setText(GlobalData.curDevice.Village_Address);
		tv_sbh.setText(GlobalData.curDevice.Device_Num);
		tv_zh.setText(GlobalData.curDevice.Village_Group_Num);
		tv_th.setText(GlobalData.curDevice.Village_Stage_Num);

		tv_xmmc.setText(GlobalData.curDevice.Program_Name);
		tv_hth.setText(GlobalData.curDevice.Contract_Num);
		tv_txtz.setText(GlobalData.curDevice.Lift_Type);
		tv_dtxh.setText(GlobalData.curDevice.Lift_Style);
		tv_jhrq.setText(GlobalData.curDevice.Delivery_Date);
		tv_zzcs.setText(GlobalData.curDevice.Manufacturer);
		// tv_wbgs.setText(GlobalData.curDevice.);
		// tv_wbsqh.setText();
		// tv_wbxx.setText();
		tv_cpgg.setText(GlobalData.curDevice.Specification);
		tv_sydw.setText(GlobalData.curDevice.User_Company);
		tv_sbdz.setText(GlobalData.curDevice.Village_Address + ""
				+ GlobalData.curDevice.Lift_Address);
		tv_latlng.setText(getString(R.string.str_device_location_value,
				GlobalData.curDevice.Latitude, GlobalData.curDevice.Longitude));

		BindDeviceOldLocation();
	}

	private void BindDeviceOldLocation() {

		if (GlobalData.curDevice.Latitude != null
				&& !GlobalData.curDevice.Latitude.isEmpty()
				&& !GlobalData.curDevice.Longitude.isEmpty()) {
			tv_latlng.setText(getString(R.string.str_device_location_value,
					GlobalData.curDevice.Latitude,
					GlobalData.curDevice.Longitude));
			LayoutInflater mInflater = LayoutInflater
					.from(ActivityDoRepair.this);
			// deviceLatLng = new LatLng(
			// Double.parseDouble(GlobalData.curDevice.Latitude),
			// Double.parseDouble(GlobalData.curDevice.Longitude));

			View convertView = mInflater.inflate(R.layout.item_marker, null);
			TextView tv = (TextView) convertView.findViewById(R.id.tv_marker);

			tv.setText(GlobalData.curDevice.Village_Name
					+ GlobalData.curDevice.Village_Group_Num + "组"
					+ GlobalData.curDevice.Village_Stage_Num + "台"
					+ GlobalData.curDevice.Device_Num);
			// mInfoWindow = new InfoWindow(
			// BitmapDescriptorFactory.fromView(convertView),
			// deviceLatLng, 0, null);
			// mBaiduMap.showInfoWindow(mInfoWindow);
		}
	}

	@Override
	public void onDestroy() {
		// 退出时销毁定位
		try {
			unregisterReceiver(receiver);
			// mLocClient.stop();
			// // 关闭定位图层
			// mBaiduMap.setMyLocationEnabled(false);
			// mMapView.onDestroy();
			// mMapView = null;
			super.onDestroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void BindMap() {
		// mBaiduMap = mMapView.getMap();
		// // 开启定位图层
		// mBaiduMap.setMyLocationEnabled(true);
		// // mCurrentMarker = BitmapDescriptorFactory
		// // .fromResource(R.drawable.icon_trans);
		// mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
		// mCurrentMode, true, null));
		// MapStatus mMapStatus = new MapStatus.Builder().zoom(20).build();
		//
		// // 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
		// MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
		// .newMapStatus(mMapStatus);
		// // 改变地图状态
		// mBaiduMap.animateMapStatus(mMapStatusUpdate);
		//
		// // 定位初始化
		// mLocClient = new LocationClient(this);
		// mLocClient.registerLocationListener(myListener);
		// LocationClientOption option = new LocationClientOption();
		// //
		// option.setLocationMode(com.baidu.location.LocationClientOption.LocationMode.Hight_Accuracy);
		// option.setOpenGps(true);// 打开gps
		// option.setCoorType("bd09ll"); // 设置坐标类型
		// option.setScanSpan(10000);
		//
		// mLocClient.setLocOption(option);
		// mLocClient.start();

	}

	// public class MyLocationListenner implements BDLocationListener {
	//
	// @Override
	// public void onReceiveLocation(BDLocation location) {
	// // map view 销毁后不在处理新接收的位置
	// LogUtil.d("%s,%s", location.getLongitude(), location.getLatitude());
	// if (location == null || mMapView == null)
	// return;
	// // 此处设置开发者获取到的方向信息，顺时针0-360
	// MyLocationData locData = new MyLocationData.Builder()
	// .direction(100).latitude(location.getLatitude())
	// .longitude(location.getLongitude()).build();
	// // 显示现在人所在的地址
	// mBaiduMap.setMyLocationData(locData);
	//
	// latLng = new LatLng(location.getLatitude(), location.getLongitude());
	// if (isFirstLoc) {
	// // 移动屏到我现在所处的点
	// isFirstLoc = false;
	// latLng = new LatLng(location.getLatitude(),
	// location.getLongitude());
	// MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
	// mBaiduMap.animateMapStatus(u);
	// }
	//
	// }
	//
	// public void onReceivePoi(BDLocation poiLocation) {
	// }
	// }

	private void queryDeviceAndWorkorderInfo() {
		new SJECHttpHandler(callBackQueryDevice, this)
				.getDeviceByWorkorderID(workorder_id);
		new SJECHttpHandler(callBackWorkorder, this)
				.getWorkorderByID(workorder_id);

		new SJECHttpHandler(callBackPushLog, this)
				.getWorkorderPushLogByID(workorder_id);

	}

	private void updateWorkorderStatus(String status) {
		// new SJECHttpHandler(callBackUpdateStatus,
		// this).updateWorkorderStatus(
		// GlobalData.curUser.InnerID, workorder_id, status,
		// latLng == null ? "0" : latLng.latitude + "",
		// latLng == null ? "0" : latLng.longitude + "",
		// getText(et_workorder_remark));
	}

	private void updateWorkorderReceiver() {
		new SJECHttpHandler(callBackUpdateStatus, this)
				.updateWorkorderReceiver(workorder_id,"");
	}

	private AsyncHttpCallBack callBackUpdateStatus = new AsyncHttpCallBack() {

		public void onSuccess(String response) {
			LogUtil.d("%s", response);
			// 更新状态成功
			Util.createToast(ActivityDoRepair.this,
					R.string.msg_control_success, Toast.LENGTH_SHORT).show();
			JSONObject jobj;
			try {
				jobj = new JSONObject(response);
				if (jobj.optString("result", "false").equals("true")) {
					getApplicationContext().sendBroadcast(
							new Intent(Constant.ACTION_REFRESH_WORKORDER_LIST));
					Gson gson = new Gson();
					GlobalData.curWorkorder = gson.fromJson(
							jobj.optString("data", ""), EntityWorkorder.class);
					BindWorkorderData();
				} else {
					Util.createToast(ActivityDoRepair.this,
							getString(R.string.msg_control_failed, ""),
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Util.createToast(ActivityDoRepair.this,
						getString(R.string.msg_control_failed, ""),
						Toast.LENGTH_SHORT).show();
			}
		};

		public void onFailure(String message) {
			Util.createToast(ActivityDoRepair.this,
					getString(R.string.msg_control_failed, ""),
					Toast.LENGTH_SHORT).show();
		};
	};

	private void BindPushLog() {
		layout_push_log.removeAllViews();
		LayoutInflater mInflater = LayoutInflater.from(ActivityDoRepair.this);

		for (int i = 0; i < listPushLog.size(); i++) {
			View convertView = mInflater.inflate(R.layout.item_push_log, null);
			TextView tv_push_phone = (TextView) convertView
					.findViewById(R.id.tv_push_phone);
			TextView tv_push_name = (TextView) convertView
					.findViewById(R.id.tv_push_name);
			TextView tv_push_result = (TextView) convertView
					.findViewById(R.id.tv_push_result);
			TextView tv_push_time = (TextView) convertView
					.findViewById(R.id.tv_push_time);
			tv_push_phone.setText(listPushLog.get(i).Telephone);
			tv_push_name.setText(listPushLog.get(i).UserName);
			tv_push_result.setText(listPushLog.get(i).PushResult.trim().equals(
					"1") ? "成功" : "失败");

			tv_push_time.setText(DateUtil.TimeParseStringToFormatString(
					listPushLog.get(i).PushTime, "yyyy-MM-dd HH:mm"));
			layout_push_log.addView(convertView);
		}
	}

	private AsyncHttpCallBack callBackPushLog = new AsyncHttpCallBack() {

		public void onSuccess(String response) {
			LogUtil.d("%s", response);
			// 更新状态成功

			try {
				JSONObject jobj;
				jobj = new JSONObject(response);
				if (jobj.optString("result", "false").equals("true")) {
					Gson gson = new Gson();
					java.lang.reflect.Type myType = new TypeToken<ArrayList<EntityWorkorderPushLog>>() {
					}.getType();
					listPushLog = gson.fromJson(jobj.optString("data", ""),
							myType);
					BindPushLog();
				} else {
					Util.createToast(ActivityDoRepair.this,
							getString(R.string.msg_control_failed, ""),
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
				Util.createToast(ActivityDoRepair.this,
						getString(R.string.msg_control_failed, ""),
						Toast.LENGTH_SHORT).show();
			}
		};

		public void onFailure(String message) {
			Util.createToast(ActivityDoRepair.this,
					getString(R.string.msg_control_failed, ""),
					Toast.LENGTH_SHORT).show();
		};
	};

	/**
	 * 查询工单信息的回调
	 */
	private AsyncHttpCallBack callBackWorkorder = new AsyncHttpCallBack() {

		public void onSuccess(String response) {
			LogUtil.d("%s", response);
			JSONObject jobj;
			try {
				scroll_maintain.onRefreshComplete();
				jobj = new JSONObject(response);

				if (jobj.optString("result", "false").equals("true")) {
					Gson gson = new Gson();
					GlobalData.curWorkorder = gson.fromJson(
							jobj.optJSONObject("data").optString("workorder",
									""), EntityWorkorder.class);
					if (GlobalData.curWorkorder.Type == EnumWorkorderType.TYPE_WORKORDER_REPAIR) {
						GlobalData.curRepair = gson.fromJson(jobj
								.optJSONObject("data").optString("repair", ""),
								EntityRepair.class);
					} else {
						GlobalData.curMaintain = gson.fromJson(jobj
								.optJSONObject("data")
								.optString("maintain", ""),
								EntityMaintain.class);
					}
					BindWorkorderData();
				} else {
					Util.createToast(ActivityDoRepair.this,
							getString(R.string.msg_control_failed, ""),
							Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Util.createToast(ActivityDoRepair.this,
						getString(R.string.msg_control_failed, ""),
						Toast.LENGTH_SHORT).show();
			}
		};

		public void onFailure(String message) {
			Util.createToast(ActivityDoRepair.this,
					getString(R.string.msg_control_failed, ""),
					Toast.LENGTH_SHORT).show();
		};
	};

	/**
	 * 查询设备信息的回调
	 */
	private AsyncHttpCallBack callBackQueryDevice = new AsyncHttpCallBack() {

		public void onSuccess(String response) {
			LogUtil.d("%s", response);
			JSONObject jobj;
			try {
				jobj = new JSONObject(response);

				if (jobj.optString("result", "false").equals("true")) {
					Gson gson = new Gson();
					GlobalData.curDevice = gson.fromJson(
							jobj.optString("data", ""), EntityDevice.class);
					BindDeviceData();
				} else {
					// Util.createToast(ActivityCollectInfo.this,
					// R.string.msg_update_device_location_failed,
					// Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};

		public void onFailure(String message) {
		};
	};

	private void ChangeBottomStyle(View v) {
		tv_base_info.setSelected(false);
		tv_maintain.setSelected(false);
		v.setSelected(true);
	}

}
