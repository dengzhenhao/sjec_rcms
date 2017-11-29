package com.sjec.rcms.activity.workorder;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Global;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.sjec.rcms.R;
import com.sjec.rcms.activity.BaseActivity;
import com.sjec.rcms.activity.qr.CaptureActivity;
import com.sjec.rcms.baidu.MyApplication;
import com.sjec.rcms.dao.EntityAssistWorker;
import com.sjec.rcms.dao.EntityCheck;
import com.sjec.rcms.dao.EntityDevice;
import com.sjec.rcms.dao.EntityMaintain;
import com.sjec.rcms.dao.EntityPauseLog;
import com.sjec.rcms.dao.EntityRepair;
import com.sjec.rcms.dao.EntityUnSubmitWorkorder;
import com.sjec.rcms.dao.EntityUnSubmitWorkorderDao;
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
public class ActivityDoCheck extends BaseActivity implements LocationSource,
		AMapLocationListener {
	private TextView tv_title;
	private LinearLayout layout_back;
	private String workorder_id = "";

	boolean isFirstLoc = true;// 是否首次定位
	private ThumbImageView btn_location_device;

	// 概 要信息
	private TextView tv_xqmc, tv_xqdz, tv_sbh, tv_zh, tv_th, tv_latlng;

	// 基本信息
	private TextView tv_xmmc, tv_hth, tv_txtz, tv_dtxh, tv_jhrq, tv_zzcs,
			tv_wbgs, tv_wbsqh, tv_wbxx, tv_cpgg, tv_sydw, tv_sbdz;

	private TextView tv_base_info, tv_maintain, tv_assist_people,
			tv_workorder_status, tv_workorder_starttime, tv_workorder_begin,
			tv_workorder_complete, tv_workorder_result, tv_workorder_receiver,
			tv_workorder_desc, tv_workorder_device_num,
			tv_workorder_event_source, tv_workorder_endtime;

	private EditText et_workorder_remark;

	private Button btn_begin_workorder, btn_receive_workorder,
			btn_workorder_pics;

	private Button btn_complete_workorder_pass,
			btn_complete_workorder_need_abarbeitung;

	private TextView tv_workorder_status_pause;
	private Button btn_pause_workorder, btn_continue_workorder,
			btn_disable_workorder;
	// btn_quick_close;

	private LinearLayout layout_device_info, layout_maintain_step,
			layout_maintain_assist;

	private RelativeLayout layout_map;
	private TextView tv_collect_map, tv_collect_push_list;

	private LinearLayout layout_push_log;
	private LinearLayout layout_pause_log;
	private PullToRefreshScrollView scroll_maintain;

	private ArrayList<EntityWorkorderPushLog> listPushLog = new ArrayList<EntityWorkorderPushLog>();
	private ArrayList<EntityPauseLog> listPauseLog = new ArrayList<EntityPauseLog>();

	private AMap aMap;
	private MapView mapView;
	private OnLocationChangedListener mListener;
	private AMapLocationClient mlocationClient;
	private AMapLocationClientOption mLocationOption;
	private LatLng latlng;
	private LatLng deviceLatLng;

	// 协助人员
	private LinearLayout layout_assist_people;
	private Button btn_invite_assist_people, btn_delete_assist_people,
			btn_confirm_assist, btn_begin_assist, btn_close_assist;

	private LinearLayout layout_400;

	/**
	 * 是否所有协助人员都已经关闭呼叫,这时受理人才可以结束这个工单,否则不可结束本工单
	 */
	private boolean isAllAssistClosed = true;
	private boolean meInList = false;
	private int assistStatus = -1;

	String resultType = "";
	private CheckBox cb_result_type_ok,cb_result_type_other;
	private LinearLayout layout_remark;
	
	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Constant.ACTION_REFRESH_WORKORDER)) {
				queryDeviceAndWorkorderInfo();
			} else if (intent.getAction().equals(
					Constant.ACTION_REFRESH_ASSIST_LIST)) {
				// queryDeviceAndWorkorderInfo();
				queryAssistWorker();
			}
		}
	};

	@Override
	public void onClick(View v) {
		Bundle b = new Bundle();
		b.putString("workorder_id", workorder_id);
		Intent intent = null;
		switch (v.getId()) {
		case R.id.layout_400:
			ConfirmDialogCallUser("", getString(R.string.phonenum_400));
			break;
		case R.id.layout_back:
			finish();
			break;
		case R.id.btn_location_device:
			try {
				aMap.animateCamera(CameraUpdateFactory
						.newCameraPosition(new CameraPosition(deviceLatLng, 19,
								0, 0)), 1000, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
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
		case R.id.btn_begin_workorder:
			
			// 如果未选中小故障
//			if (GlobalData.listAsssitWorker.size() == 0
//					&& GlobalData.WorkerStatus != Constant.EnumWorkorderStatus.STATUS_WORKORDER_1) {
//				// 如果不在值班模式,并且协助人员数量为0,那么需要提示
//				Util.createToast(ActivityDoCheck.this,
//						R.string.msg_add_assist_worker, 3000).show();
//				return;
//			}

			Util.createDialog(this, null,
					R.string.msg_dialog_add_order_type_title,
					R.string.msg_begin_workorder_confirm, null, true, false,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 更新状态为开始
							updateWorkorderStatus(
									Constant.EnumWorkorderStatus.STATUS_WORKORDER_2
											+ "", "0");
						}
					}).show();
			break;
		case R.id.tv_base_info:
			layout_device_info.setVisibility(View.VISIBLE);
			layout_maintain_step.setVisibility(View.GONE);
			layout_maintain_assist.setVisibility(View.GONE);
			ChangeBottomStyle(v);
			break;
		case R.id.tv_maintain:
			layout_device_info.setVisibility(View.GONE);
			layout_maintain_step.setVisibility(View.VISIBLE);
			layout_maintain_assist.setVisibility(View.GONE);
			ChangeBottomStyle(v);
			break;
		case R.id.tv_assist_people:
			layout_device_info.setVisibility(View.GONE);
			layout_maintain_step.setVisibility(View.GONE);
			layout_maintain_assist.setVisibility(View.VISIBLE);
			ChangeBottomStyle(v);
			break;
		// case R.id.btn_complete_workorder:
		// Util.createDialog(this, null,
		// R.string.msg_dialog_add_order_type_title,
		// R.string.msg_complete_workorder_confirm, null, true, false,
		// new DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// // 更新状态为完成
		// updateWorkorderStatus(Constant.EnumWorkorderStatus.STATUS_WORKORDER_3
		// + "");
		// }
		// }).show();
		// break;
		case R.id.btn_complete_workorder_pass:
			if(resultType.isEmpty()){
				Util.createToast(this, "请选择处理结果类型", Toast.LENGTH_SHORT).show();
				return;
			}else{
				if(resultType.equals(cb_result_type_other.getTag().toString()) && getText(et_workorder_remark).isEmpty()){
					Util.createToast(this, "请填写维保结果与反馈", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			
			ConfirmDialogControl(
					R.string.dialog_title_confirm_workorder_complete,
					R.string.dialog_msg_confirm_workorder_complete,
					R.string.dialog_button_confirm_workorder_complete,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							Bundle bQr = new Bundle();
							bQr.putString("workorder_id", workorder_id);
							bQr.putInt("scan_type", Constant.SCAN_TYPE3);
							Intent intentQr = new Intent(ActivityDoCheck.this,
									CaptureActivity.class);
							intentQr.putExtras(bQr);
							startActivityForResult(intentQr, 99);
						}
					});

			break;
		case R.id.btn_complete_workorder_need_abarbeitung:
			if(resultType.isEmpty()){
				Util.createToast(this, "请选择处理结果类型", Toast.LENGTH_SHORT).show();
				return;
			}else{
				if(resultType.equals(cb_result_type_other.getTag().toString()) && getText(et_workorder_remark).isEmpty()){
					Util.createToast(this, "请填写维保结果与反馈", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			b.putInt("scan_type", Constant.SCAN_TYPE3);
			intent = new Intent(ActivityDoCheck.this, CaptureActivity.class);
			intent.putExtras(b);
			startActivityForResult(intent, 100);
			break;
		case R.id.btn_pause_workorder:
			b.putString("title", "请求暂停");
			b.putInt(
					"type",
					Constant.EnumWorkorderPauseStatus.PAUSE_STATUS_WORKORDER_PAUSE);
			Util.startActivity(ActivityDoCheck.this, ActivityPauseReason.class,
					b, false);
			break;
		case R.id.btn_continue_workorder:
			b.putString("title", "继续工作");
			b.putInt(
					"type",
					Constant.EnumWorkorderPauseStatus.PAUSE_STATUS_WORKORDER_CONTINUE);
			Util.startActivity(ActivityDoCheck.this, ActivityPauseReason.class,
					b, false);
			break;
		case R.id.btn_disable_workorder:
			b.putString("title", "请求作废");
			b.putInt(
					"type",
					Constant.EnumWorkorderPauseStatus.PAUSE_STATUS_WORKORDER_INVALID);
			Util.startActivity(ActivityDoCheck.this, ActivityPauseReason.class,
					b, false);
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
		case R.id.btn_invite_assist_people:
			Util.startActivity(ActivityDoCheck.this,
					ActivityCompanyWorkerList.class, false);
			break;
		case R.id.btn_delete_assist_people:
			Util.createDialog(this, null,
					R.string.msg_dialog_add_order_type_title,
					R.string.msg_confirm_control, null, true, false,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							deleteAssistWorker();
						}
					}).show();

			break;
		case R.id.btn_confirm_assist:
			Util.createDialog(this, null,
					R.string.msg_dialog_add_order_type_title,
					R.string.msg_confirm_control, null, true, false,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							UpdateAssistStatus(Constant.EnumAssistWorkerStatus.ASSIST_STATUS_CONFIRMED);
						}
					}).show();
			break;
		case R.id.btn_begin_assist:
			Util.createDialog(this, null,
					R.string.msg_dialog_add_order_type_title,
					R.string.msg_confirm_control, null, true, false,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							UpdateAssistStatus(Constant.EnumAssistWorkerStatus.ASSIST_STATUS_BEGINED);
						}
					}).show();
			break;
		case R.id.btn_close_assist:
			b.putInt("scan_type", Constant.SCAN_TYPE4);
			intent = new Intent(ActivityDoCheck.this, CaptureActivity.class);
			intent.putExtras(b);
			startActivityForResult(intent, 98);
			break;
		case R.id.btn_workorder_pics:
			Util.startActivity(this, ActivityPicSend.class, false);
			break;
		case R.id.cb_result_type_ok:
			if (cb_result_type_ok.isChecked()) {
				resultType  = v.getTag().toString();
			} else {
				resultType = "";
			}
			cb_result_type_other.setChecked(false);
			layout_remark.setVisibility(View.GONE);
			break;
		case R.id.cb_result_type_other:
			if (cb_result_type_other.isChecked()) {
				resultType  = v.getTag().toString();
			} else {
				resultType = "";
			}
			cb_result_type_ok.setChecked(false);
			layout_remark.setVisibility(View.VISIBLE);
			break;
		}
	}

	private void deleteAssistWorker() {
		String assist_user_id = "";
		for (int i = 0; i < layout_assist_people.getChildCount(); i++) {
			View view = layout_assist_people.getChildAt(i);
			CheckBox cbx = (CheckBox) view.findViewById(R.id.cb_delete_assist);
			if (cbx.isChecked()) {
				if (!assist_user_id.isEmpty()) {
					assist_user_id += "|";
				}
				assist_user_id += cbx.getTag().toString();
			}
		}
		new SJECHttpHandler(callBackDeleteAssistWorker, this)
				.deleteWorkOrderAssistUsers(workorder_id, assist_user_id);
	}

	@Override
	protected void onActivityResult(final int requestCode, int resultCode,
			Intent data) {
		// TODO Auto-generated method stub

		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK == resultCode) {
			if (requestCode == 99 || requestCode == 100) {
				try {
					String data_result = data.getExtras().getString("data", "");
					String[] arrDeviceInfo = SJECUtil
							.GetDeviceInfoArrayFromQrCode(data_result);
					String deviceNum = arrDeviceInfo[2].trim();

					if (!GlobalData.curWorkorder.Device_Num.trim()
							.toUpperCase()
							.equals(deviceNum.trim().toUpperCase())) {
						Util.createToast(
								ActivityDoCheck.this,
								getResources()
										.getString(
												R.string.str_show_manual_submit_workorder,
												deviceNum,
												GlobalData.curWorkorder.Device_Num),
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
										updateWorkorderStatus(
												Constant.EnumWorkorderStatus.STATUS_WORKORDER_3
														+ "",
												(requestCode == 99 ? "0" : "1"));
									}
								}).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
					Util.createToast(ActivityDoCheck.this,
							R.string.error_qr_scan, 3000).show();
				}
			} else if (requestCode == 98) {
				LogUtil.d("%s", requestCode);
				try {
					String data_result = data.getExtras().getString("data", "");
					String[] arrDeviceInfo = SJECUtil
							.GetDeviceInfoArrayFromQrCode(data_result);
					String deviceNum = arrDeviceInfo[2].trim();

					if (!GlobalData.curWorkorder.Device_Num.trim()
							.toUpperCase()
							.equals(deviceNum.trim().toUpperCase())) {
						Util.createToast(
								ActivityDoCheck.this,
								getResources()
										.getString(
												R.string.str_show_manual_submit_workorder,
												deviceNum,
												GlobalData.curWorkorder.Device_Num),
								3000).show();
					} else {
						UpdateAssistStatus(Constant.EnumAssistWorkerStatus.ASSIST_STATUS_CLOSED);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Util.createToast(ActivityDoCheck.this,
							R.string.error_qr_scan, 3000).show();
				}
			}
		}
	}

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_workorder_do_check);
	}

	@Override
	public void init(Bundle savedInstanceState) {
		IntentFilter filter = new IntentFilter(
				Constant.ACTION_REFRESH_WORKORDER);
		filter.addAction(Constant.ACTION_REFRESH_ASSIST_LIST);
		registerReceiver(receiver, filter);
		// mCurrentMode = LocationMode.NORMAL;
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		layout_400 = (LinearLayout) findViewById(R.id.layout_400);
		layout_400.setVisibility(View.VISIBLE);
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
		tv_workorder_endtime = (TextView) findViewById(R.id.tv_workorder_endtime);
		// btn_begin_maintain = (Button)findViewById(R.id.btn_begin_maintain);

		tv_workorder_event_source = (TextView) findViewById(R.id.tv_workorder_event_source);
		tv_workorder_status = (TextView) findViewById(R.id.tv_workorder_status);
		tv_base_info = (TextView) findViewById(R.id.tv_base_info);
		tv_maintain = (TextView) findViewById(R.id.tv_maintain);
		tv_assist_people = (TextView) findViewById(R.id.tv_assist_people);
		tv_workorder_begin = (TextView) findViewById(R.id.tv_workorder_begin);
		tv_workorder_complete = (TextView) findViewById(R.id.tv_workorder_complete);
		tv_workorder_result = (TextView) findViewById(R.id.tv_workorder_result);
		tv_workorder_desc = (TextView) findViewById(R.id.tv_workorder_desc);
		et_workorder_remark = (EditText) findViewById(R.id.et_workorder_remark);
		tv_workorder_device_num = (TextView) findViewById(R.id.tv_workorder_device_num);

		btn_begin_workorder = (Button) findViewById(R.id.btn_begin_workorder);
		// btn_complete_workorder = (Button)
		// findViewById(R.id.btn_complete_workorder);
		btn_complete_workorder_pass = (Button) findViewById(R.id.btn_complete_workorder_pass);
		btn_complete_workorder_need_abarbeitung = (Button) findViewById(R.id.btn_complete_workorder_need_abarbeitung);
		tv_workorder_status_pause = (TextView) findViewById(R.id.tv_workorder_status_pause);
		btn_pause_workorder = (Button) findViewById(R.id.btn_pause_workorder);
		btn_continue_workorder = (Button) findViewById(R.id.btn_continue_workorder);
		btn_disable_workorder = (Button) findViewById(R.id.btn_disable_workorder);
		// btn_init_workorder = (Button) findViewById(R.id.btn_init_workorder);
		btn_workorder_pics = (Button) findViewById(R.id.btn_workorder_pics);
		// btn_quick_close = (Button) findViewById(R.id.btn_quick_close);

		layout_device_info = (LinearLayout) findViewById(R.id.layout_device_info);
		layout_maintain_step = (LinearLayout) findViewById(R.id.layout_maintain_step);
		layout_maintain_assist = (LinearLayout) findViewById(R.id.layout_maintain_assist);

		tv_workorder_receiver = (TextView) findViewById(R.id.tv_workorder_receiver);
		btn_receive_workorder = (Button) findViewById(R.id.btn_receive_workorder);

		layout_map = (RelativeLayout) findViewById(R.id.layout_map);
		tv_collect_map = (TextView) findViewById(R.id.tv_collect_map);
		tv_collect_push_list = (TextView) findViewById(R.id.tv_collect_push_list);

		layout_push_log = (LinearLayout) findViewById(R.id.layout_push_log);
		layout_pause_log = (LinearLayout) findViewById(R.id.layout_pause_log);

		scroll_maintain = (PullToRefreshScrollView) findViewById(R.id.scroll_maintain);

		scroll_maintain.setMode(Mode.PULL_FROM_START);
		layout_400.setOnClickListener(this);
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

		// 协助维保
		layout_assist_people = (LinearLayout) findViewById(R.id.layout_assist_people);

		btn_invite_assist_people = (Button) findViewById(R.id.btn_invite_assist_people);
		btn_confirm_assist = (Button) findViewById(R.id.btn_confirm_assist);
		btn_begin_assist = (Button) findViewById(R.id.btn_begin_assist);
		btn_close_assist = (Button) findViewById(R.id.btn_close_assist);
		btn_delete_assist_people = (Button) findViewById(R.id.btn_delete_assist_people);
		btn_invite_assist_people.setOnClickListener(this);
		btn_confirm_assist.setOnClickListener(this);
		btn_begin_assist.setOnClickListener(this);
		btn_close_assist.setOnClickListener(this);
		btn_delete_assist_people.setOnClickListener(this);
		btn_workorder_pics.setOnClickListener(this);

		btn_location_device = (ThumbImageView) findViewById(R.id.btn_location_device);
		btn_location_device.setOnClickListener(this);
		tv_base_info.setOnClickListener(this);
		tv_maintain.setOnClickListener(this);
		tv_assist_people.setOnClickListener(this);
		layout_back.setOnClickListener(this);
		// btn_location.setOnClickListener(this);
		btn_begin_workorder.setOnClickListener(this);
		// btn_complete_workorder.setOnClickListener(this);
		btn_complete_workorder_pass.setOnClickListener(this);
		btn_complete_workorder_need_abarbeitung.setOnClickListener(this);
		btn_receive_workorder.setOnClickListener(this);

		btn_pause_workorder.setOnClickListener(this);
		btn_continue_workorder.setOnClickListener(this);
		btn_disable_workorder.setOnClickListener(this);
		// btn_init_workorder.setOnClickListener(this);
		// btn_quick_close.setOnClickListener(this);

		tv_collect_map.setOnClickListener(this);
		tv_collect_push_list.setOnClickListener(this);
		// btn_begin_maintain.setOnClickListener(this);

		// scroll_maintain.setRefreshing(true);

		cb_result_type_ok= (CheckBox) findViewById(R.id.cb_result_type_ok);
		cb_result_type_other = (CheckBox) findViewById(R.id.cb_result_type_other);
		cb_result_type_ok.setOnClickListener(this);
		cb_result_type_other.setOnClickListener(this);
		
		layout_remark = (LinearLayout)findViewById(R.id.layout_remark);
		
		disabledAllButton();

		mapView = (MapView) findViewById(R.id.map_district);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
	}

	@Override
	public void bindData() {
		ConfirmDialogControl(R.string.dialog_title_confirm_workorder_before,
				R.string.dialog_msg_confirm_workorder_before,
				R.string.dialog_button_confirm_workorder_before, null);
		tv_maintain.performClick();

		Bundle b = getIntent().getExtras();
		workorder_id = b.getString("workorder_id", "");
		tv_title.setText(R.string.title_repair);
		queryDeviceAndWorkorderInfo();
	}

	private void BindWorkorderData() {
		// 根据数据判断协助维保界面的按钮是否可用及显示

		// et_workorder_remark.setText(GlobalData.curRepair.RepairDesc);
		tv_workorder_event_source.setText(GlobalData.curWorkorder.EventSource);
		tv_workorder_device_num.setText(GlobalData.curWorkorder.Device_Num);

		if (GlobalData.curWorkorder.Status == Constant.EnumWorkorderStatus.STATUS_WORKORDER_3) {
			cb_result_type_ok.setEnabled(false);
		    cb_result_type_other.setEnabled(false);
		    et_workorder_remark.setEnabled(false);
			tv_workorder_endtime.setText(GlobalData.curWorkorder.EndTime);
		}

		if (GlobalData.curWorkorder.Status == Constant.EnumWorkorderStatus.STATUS_WORKORDER_INVALID) {
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

		try {
			// tv_workorder_desc.setText("[" + GlobalData.curRepair.Error_Info
			// + "]" + GlobalData.curRepair.Error_Detail);
			// if (!GlobalData.curWorkorder.SolutionInfo.isEmpty()) {
			// tv_workorder_desc.setText(tv_workorder_desc.getText() + "\r\n"
			// + GlobalData.curWorkorder.SolutionInfo);
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (GlobalData.curWorkorder.ResultDesc != null) {
			et_workorder_remark.setText(GlobalData.curWorkorder.ResultDesc);
		}
		
		if(GlobalData.curWorkorder.ResultType!=null){
			if(GlobalData.curWorkorder.ResultType.equals(cb_result_type_ok.getTag().toString())){
				cb_result_type_ok.setChecked(true);
				cb_result_type_other.setChecked(false);
				layout_remark.setVisibility(View.GONE);
			}else if(GlobalData.curWorkorder.ResultType.equals(cb_result_type_other.getTag().toString())){
				cb_result_type_ok.setChecked(false);
				cb_result_type_other.setChecked(true);
				layout_remark.setVisibility(View.VISIBLE);
			}
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

		BindDeviceLocation();
	}

	private void BindDeviceLocation() {

		if (GlobalData.curDevice.Latitude != null
				&& !GlobalData.curDevice.Latitude.isEmpty()
				&& !GlobalData.curDevice.Longitude.isEmpty()) {
			tv_latlng.setText(getString(R.string.str_device_location_value,
					GlobalData.curDevice.Latitude,
					GlobalData.curDevice.Longitude));
			LayoutInflater mInflater = LayoutInflater
					.from(ActivityDoCheck.this);
			deviceLatLng = new LatLng(
					Double.parseDouble(GlobalData.curDevice.Latitude),
					Double.parseDouble(GlobalData.curDevice.Longitude));

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
			Marker marker = aMap.addMarker(new MarkerOptions()
					.position(deviceLatLng)
					.icon(BitmapDescriptorFactory.fromView(convertView))
					.draggable(true));
			marker.showInfoWindow();// 设置默认显示一个infowinfow
		}
	}

	private void queryDeviceAndWorkorderInfo() {
		new SJECHttpHandler(callBackQueryDevice, this)
				.getDeviceByWorkorderID(workorder_id);
		new SJECHttpHandler(callBackWorkorder, this)
				.getWorkorderByID(workorder_id);
		new SJECHttpHandler(callBackPushLog, this)
				.getWorkorderPushLogByID(workorder_id);
		new SJECHttpHandler(callBackPauseLog, this)
				.queryWorkOrderPauseLog(workorder_id);
	}

	private void queryAssistWorker() {

		new SJECHttpHandler(callBackAssistWorker, this)
				.getCompanyAssistWorkerList(workorder_id);
	}

	private void updateWorkorderStatus(String status, String isNeedAbarbeitung) {
		callBackUpdateStatus.setExtra(status+"#"+isNeedAbarbeitung);
		new SJECHttpHandler(callBackUpdateStatus, this).updateWorkorderStatusForCheck(
				workorder_id, status, latlng == null ? "0" : latlng.latitude
						+ "", latlng == null ? "0" : latlng.longitude + "",
				getText(et_workorder_remark), "0", isNeedAbarbeitung,resultType,false);
	}

	private void updateWorkorderReceiver() {
		new SJECHttpHandler(callBackUpdateReceive, this)
				.updateWorkorderReceiver(workorder_id,"-1");
	}

	private AsyncHttpCallBack callBackUpdateStatus = new AsyncHttpCallBack() {
		
		public void onSuccess(String response) {
			LogUtil.d("%s", response);
			// 更新状态成功
			
			EntityUnSubmitWorkorderDao dao = MyApplication.daoSession
					.getEntityUnSubmitWorkorderDao();
			dao.deleteByKey(workorder_id);
			JSONObject jobj;
			try {
				jobj = new JSONObject(response);
				if (jobj.optString("result", "false").equals("true")) {
					Util.createToast(ActivityDoCheck.this,
							R.string.msg_control_success, Toast.LENGTH_SHORT).show();
					getApplicationContext().sendBroadcast(
							new Intent(Constant.ACTION_REFRESH_WORKORDER_LIST));
					Gson gson = new Gson();
					GlobalData.curWorkorder = gson.fromJson(
							jobj.optString("data", ""), EntityWorkorder.class);
					BindWorkorderData();
					BindAssistWorker();
					if (GlobalData.curWorkorder.EventSource.contains("400")
							&& GlobalData.curWorkorder.Status == Constant.EnumWorkorderStatus.STATUS_WORKORDER_3) {
						ConfirmDialogCallUser("",
								getString(R.string.phonenum_400));
					}
				} else {
					Util.createToast(ActivityDoCheck.this,
							getString(R.string.msg_control_failed, ""),
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Util.createToast(ActivityDoCheck.this,
						getString(R.string.msg_control_failed, ""),
						Toast.LENGTH_SHORT).show();
			}
		};

		public void onFailure(String message) {
			// 在这里记录到本地数据库中，后台服务监听到网络变化时，会自动提交表中数据
			if (getExtra().toString().equals(
					Constant.EnumWorkorderStatus.STATUS_WORKORDER_3 + "")) {
				EntityUnSubmitWorkorderDao dao = MyApplication.daoSession
						.getEntityUnSubmitWorkorderDao();
				EntityUnSubmitWorkorder unsubmit = new EntityUnSubmitWorkorder();
				String arr[] =  getExtra().toString().split("#");
				unsubmit.InnerID = workorder_id;
				unsubmit.Type = Constant.EnumWorkorderType.TYPE_WORKORDER_CHECK;
				unsubmit.Status = arr[0];
				unsubmit.IsNeedAbarbeitung = arr[1];
				unsubmit.Latitude = latlng == null ? "0" : latlng.latitude + "";
				unsubmit.Longitude = latlng == null ? "0" : latlng.longitude
						+ "";
				unsubmit.Remark = getText(et_workorder_remark);
				unsubmit.ResultType = resultType;
				unsubmit.UserID = GlobalData.curWorkorder.ChargeUserID;
				dao.insertOrReplace(unsubmit);
				Util.createToast(ActivityDoCheck.this,
						"提交失败,已保存到本地,网络状态改善后,将自动提交工单", Toast.LENGTH_SHORT)
						.show();
			} else {
				Util.createToast(ActivityDoCheck.this,
						getString(R.string.msg_control_failed, ""),
						Toast.LENGTH_SHORT).show();
			}
		};
	};
	
	private AsyncHttpCallBack callBackUpdateReceive = new AsyncHttpCallBack() {

		public void onSuccess(String response) {
			LogUtil.d("%s", response);
			// 更新状态成功
			Util.createToast(ActivityDoCheck.this,
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
					BindAssistWorker();
					if (GlobalData.curWorkorder.EventSource.contains("400")
							&& GlobalData.curWorkorder.Status == Constant.EnumWorkorderStatus.STATUS_WORKORDER_3) {
						ConfirmDialogCallUser("",
								getString(R.string.phonenum_400));
					}
				} else {
					Util.createToast(ActivityDoCheck.this,
							getString(R.string.msg_control_failed, ""),
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Util.createToast(ActivityDoCheck.this,
						getString(R.string.msg_control_failed, ""),
						Toast.LENGTH_SHORT).show();
			}
		};

		public void onFailure(String message) {
			Util.createToast(ActivityDoCheck.this,
					getString(R.string.msg_control_failed, ""),
					Toast.LENGTH_SHORT).show();
		};
	};

	private void BindPushLog() {
		layout_push_log.removeAllViews();
		LayoutInflater mInflater = LayoutInflater.from(ActivityDoCheck.this);

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
			tv_push_phone.setText(Html.fromHtml("<u>"
					+ listPushLog.get(i).Telephone + "</u>"));
			tv_push_name.setText(listPushLog.get(i).UserName);
			tv_push_result.setText(listPushLog.get(i).PushResult.trim().equals(
					"1") ? "成功" : "失败");
			tv_push_time.setText(DateUtil.TimeParseStringToFormatString(
					listPushLog.get(i).PushTime, "yyyy-MM-dd HH:mm"));
			convertView.setTag(listPushLog.get(i));
			convertView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					LogUtil.d(v.getTag().toString());
					final EntityWorkorderPushLog pushLog = (EntityWorkorderPushLog) v
							.getTag();
					ConfirmDialogCallUser(pushLog.UserName, pushLog.Telephone);
				}
			});
			layout_push_log.addView(convertView);
		}
	}

	private void BindPauseLog() {
		layout_pause_log.removeAllViews();
		LayoutInflater mInflater = LayoutInflater.from(ActivityDoCheck.this);

		for (int i = 0; i < listPauseLog.size(); i++) {
			View convertView = mInflater.inflate(R.layout.item_pause_log, null);
			TextView tv_pause_content = (TextView) convertView
					.findViewById(R.id.tv_pause_content);
			TextView tv_pause_remark = (TextView) convertView
					.findViewById(R.id.tv_pause_remark);
			TextView tv_check_result = (TextView) convertView
					.findViewById(R.id.tv_check_result);
			tv_pause_content
					.setText(Constant.EnumWorkorderPauseStatus
							.GetName(ConvertUtil.ParsetStringToInt32(
									listPauseLog.get(i).FromPauseStatus, 99))
							+ "->"
							+ Constant.EnumWorkorderPauseStatus.GetName(ConvertUtil.ParsetStringToInt32(
									listPauseLog.get(i).TargetPauseStatus, 99)));

			tv_check_result.setText(Constant.EnumWorkorderPauseStatus
					.GetAdminCheckName(ConvertUtil.ParsetStringToInt32(
							listPauseLog.get(i).AdminCheck, 99)));
			tv_pause_remark.setText(listPauseLog.get(i).Remark);

			String req_time = "";
			try {
				req_time = DateUtil.TimeParseStringToFormatString(
						listPauseLog.get(i).OccurTime, "MM-dd HH:mm");
			} catch (Exception e) {
				req_time = "-";
			}
			tv_pause_content.setText(tv_pause_content.getText() + "\n"
					+ req_time);
			String check_time = "";
			try {
				if (listPauseLog.get(i).CheckTime == null
						|| listPauseLog.get(i).CheckTime.isEmpty()) {
					check_time = "-";
				} else {
					check_time = DateUtil.TimeParseStringToFormatString(
							listPauseLog.get(i).CheckTime, "MM-dd HH:mm");
				}
			} catch (Exception e) {
				check_time = "-";
			}

			tv_check_result.setText(tv_check_result.getText() + "\n"
					+ check_time);

			layout_pause_log.addView(convertView);
		}
	}

	private boolean isMeReceiver() {
		return GlobalData.curWorkorder.ChargeUserID != null
				&& !GlobalData.curWorkorder.ChargeUserID.isEmpty()
				&& GlobalData.curWorkorder.ChargeUserID
						.equals(GlobalData.curUser.InnerID);
	}

	private boolean isReceived() {
		if (GlobalData.curWorkorder.ChargeUserID == null
				|| GlobalData.curWorkorder.ChargeUserID.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 绑定协助人员的信息,根据当前登录人的信息,与协助人员列表的信息,判断是否显示按钮及按钮的可用状态
	 */
	private void BindAssistWorker() {

		isAllAssistClosed = true;
		meInList = false;
		assistStatus = -1;
		LayoutInflater mInflater = LayoutInflater.from(ActivityDoCheck.this);
		layout_assist_people.removeAllViews();
		if (GlobalData.listAsssitWorker.size() == 0) {
			isAllAssistClosed = false;
		}

		for (int i = 0; i < GlobalData.listAsssitWorker.size(); i++) {
			View convertView = mInflater.inflate(R.layout.item_assist_people,
					null);
			CheckBox cbx = (CheckBox) convertView
					.findViewById(R.id.cb_delete_assist);
			TextView tv_assist_name = (TextView) convertView
					.findViewById(R.id.tv_assist_name);
			TextView tv_assist_staffno = (TextView) convertView
					.findViewById(R.id.tv_assist_staffno);
			TextView tv_assist_mobile = (TextView) convertView
					.findViewById(R.id.tv_assist_mobile);
			TextView tv_assist_status = (TextView) convertView
					.findViewById(R.id.tv_assist_status);

			if (!GlobalData.curWorkorder.ChargeUserID
					.equals(GlobalData.curUser.InnerID)) {
				cbx.setVisibility(View.INVISIBLE);
			} else {
				if (GlobalData.curWorkorder.Status != Constant.EnumWorkorderStatus.STATUS_WORKORDER_3) {
					// 判断工单状态是否为已开始,除了已开始状态,其余状态都不显示复选框
					cbx.setVisibility(View.VISIBLE);
				}
			}

			cbx.setTag(GlobalData.listAsssitWorker.get(i).UserID);
			tv_assist_name.setText(GlobalData.listAsssitWorker.get(i).Name);
			tv_assist_staffno
					.setText(GlobalData.listAsssitWorker.get(i).StaffNo);
			tv_assist_mobile.setText(Html.fromHtml("<u>"
					+ GlobalData.listAsssitWorker.get(i).Telephone + "</u>"));

			tv_assist_status.setText(Constant.EnumAssistWorkerStatus
					.GetName(GlobalData.listAsssitWorker.get(i).AssistStatus));

			// 判断协助人员列表中有没有自己
			if (GlobalData.curUser.InnerID.equals(GlobalData.listAsssitWorker
					.get(i).UserID)) {
				meInList = true;
				assistStatus = GlobalData.listAsssitWorker.get(i).AssistStatus;
				tv_assist_name.setTextColor(getResources().getColor(
						android.R.color.holo_red_light));
			}

			if (isAllAssistClosed
					&& GlobalData.listAsssitWorker.get(i).AssistStatus != Constant.EnumAssistWorkerStatus.ASSIST_STATUS_CLOSED) {
				isAllAssistClosed = false;
			}
			convertView.setTag(GlobalData.listAsssitWorker.get(i));
			convertView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					final EntityAssistWorker worker = (EntityAssistWorker) v
							.getTag();
					ConfirmDialogCallUser(worker.Name, worker.Telephone);
				}
			});
			layout_assist_people.addView(convertView);
		}

		initButton();

	}

	/**
	 * 设置按钮可用
	 * 
	 * @param btn
	 * @param enable
	 */
	private void changeButtonBg(Button btn, boolean enable) {
		btn.setEnabled(enable);
		if (enable) {
			btn.setBackgroundResource(R.drawable.frame_app);
		} else {
			btn.setBackgroundResource(R.drawable.frame_disabled);
		}
	}

	/**
	 * 初始化地图控件
	 */
	private void setUpMap() {
		// 自定义系统定位小蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.location_marker));// 设置小蓝点的图标
		// myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
		// myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));//
		// 设置圆形的填充颜色
		myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
		myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
		// myLocationStyle.anchor(int,int)//设置小蓝点的锚点

		myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setLocationSource(this);// 设置定位监听
		aMap.moveCamera(CameraUpdateFactory.zoomTo(19));

		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		// aMap.setMyLocationType()
	}

	private void disabledAllButton() {

		changeButtonBg(btn_workorder_pics, false);
		changeButtonBg(btn_receive_workorder, false);
		changeButtonBg(btn_begin_workorder, false);
		// changeButtonBg(btn_complete_workorder, false);
		changeButtonBg(btn_complete_workorder_pass, false);
		changeButtonBg(btn_complete_workorder_need_abarbeitung, false);

		changeButtonBg(btn_pause_workorder, false);
		changeButtonBg(btn_continue_workorder, false);
		changeButtonBg(btn_disable_workorder, false);
		// changeButtonBg(btn_init_workorder, false);
		// changeButtonBg(btn_quick_close, false);

		changeButtonBg(btn_invite_assist_people, false);
		changeButtonBg(btn_delete_assist_people, false);
		changeButtonBg(btn_confirm_assist, false);
		changeButtonBg(btn_begin_assist, false);
		changeButtonBg(btn_close_assist, false);

		tv_workorder_receiver.setVisibility(View.GONE);
		btn_receive_workorder.setVisibility(View.GONE);
		btn_begin_workorder.setVisibility(View.GONE);
		btn_invite_assist_people.setVisibility(View.GONE);
		btn_delete_assist_people.setVisibility(View.GONE);
		btn_confirm_assist.setVisibility(View.GONE);
		btn_begin_assist.setVisibility(View.GONE);
		btn_close_assist.setVisibility(View.GONE);
	}

	/**
	 * 判断按钮的状态
	 */
	private void initButton() {
		disabledAllButton();
		// 绑定接单信息如果是未接单,那么接单按钮可用,如果已经接单,那么显示接单信息,隐藏接单按钮
		bindReceiveInfo();
		if (GlobalData.curWorkorder.Status == Constant.EnumWorkorderStatus.STATUS_WORKORDER_1) {// 未开始
			// 未接单
			if (isReceived()) {
				// 已受理,未开始
				initButtonReceive();
				initControlStatusButton(false);
				BindInviteAssistButton();
				BindAssisterButton();
			} else {
				initButtonUnreceive();
			}
		} else if (GlobalData.curWorkorder.Status == Constant.EnumWorkorderStatus.STATUS_WORKORDER_2) {// 已开始
			initControlStatusButton(true);
			initButtonBegin();
			BindInviteAssistButton();
			BindAssisterButton();
		}

		if (GlobalData.curWorkorder.Status == Constant.EnumWorkorderStatus.STATUS_WORKORDER_2
				|| GlobalData.curWorkorder.Status == Constant.EnumWorkorderStatus.STATUS_WORKORDER_3
				|| GlobalData.curWorkorder.Status == Constant.EnumWorkorderStatus.STATUS_WORKORDER_INVALID) {
			if (isMeReceiver()) {
				changeButtonBg(btn_workorder_pics, true);
			}
		}
	}

	/**
	 * 根据数据绑定协助维保界面的按钮状态
	 */
	private void BindInviteAssistButton() {
		if (isMeReceiver()) {
			if (GlobalData.curWorkorder.Status == Constant.EnumWorkorderStatus.STATUS_WORKORDER_1
					|| GlobalData.curWorkorder.Status == Constant.EnumWorkorderStatus.STATUS_WORKORDER_2) {
				btn_invite_assist_people.setVisibility(View.VISIBLE);
				btn_delete_assist_people.setVisibility(View.VISIBLE);
				changeButtonBg(btn_invite_assist_people, true);
				changeButtonBg(btn_delete_assist_people, true);

			}
		}
	}

	/**
	 * 绑定协助人员的3个按钮,确定,开始,关闭
	 * 
	 * @param meInList
	 * @param assistStatus
	 */
	private void BindAssisterButton() {
		if (meInList) {
			btn_confirm_assist.setVisibility(View.VISIBLE);
			btn_begin_assist.setVisibility(View.VISIBLE);
			btn_close_assist.setVisibility(View.VISIBLE);
			// 自己在协助人员名单中时,确认,开始,关闭3个按钮才可见
			if (GlobalData.curWorkorder.Status == Constant.EnumWorkorderStatus.STATUS_WORKORDER_1
					|| GlobalData.curWorkorder.Status == Constant.EnumWorkorderStatus.STATUS_WORKORDER_2) {
				if (assistStatus == Constant.EnumAssistWorkerStatus.ASSIST_STATUS_UNCONFIRM) {
					// 当前登录人的协助状态为待确认
					changeButtonBg(btn_confirm_assist, true);
				}
			}

			if (GlobalData.curWorkorder.Status == Constant.EnumWorkorderStatus.STATUS_WORKORDER_2) {
				// 只有在工单状态为已开始状态下,才可操作,其它状态下,按钮都为不可用
				if (assistStatus == Constant.EnumAssistWorkerStatus.ASSIST_STATUS_CONFIRMED) {
					// 当前登录人的协助状态为已确认/待开始
					changeButtonBg(btn_begin_assist, true);
				} else if (assistStatus == Constant.EnumAssistWorkerStatus.ASSIST_STATUS_BEGINED) {
					// 当前登录人的协助状态为已开始
					changeButtonBg(btn_close_assist, true);
				}
			}
		}

	}

	/**
	 * 初始化各个执行状态的按钮
	 */
	private void initControlStatusButton(boolean isBegin) {
		if (isMeReceiver()) {
			// 如果单接单的人是自己
			if (GlobalData.curWorkorder.OrderPause == Constant.EnumWorkorderPauseStatus.PAUSE_STATUS_WORKORDER_PAUSE) {
				// 如果是暂停，
				if (isBegin) {
					changeButtonBg(btn_continue_workorder, true);
				}
				changeButtonBg(btn_disable_workorder, false);
				// changeButtonBg(btn_init_workorder, false);
			} else if (GlobalData.curWorkorder.OrderPause == Constant.EnumWorkorderPauseStatus.PAUSE_STATUS_WORKORDER_CONTINUE) {
				// 如果是正常执行状态，那么可以快速关闭，可以暂停，可以废弃
				if (isBegin) {
					changeButtonBg(btn_pause_workorder, true);
				}
				changeButtonBg(btn_disable_workorder, true);
				// changeButtonBg(btn_init_workorder, true);
			}

		}
	}

	/**
	 * 工单是初始状态
	 */
	private void initButtonUnreceive() {
		btn_receive_workorder.setVisibility(View.VISIBLE);
		changeButtonBg(btn_receive_workorder, true);
	}

	/**
	 * 工单已被受理,未开始
	 */
	private void initButtonReceive() {
		if (isMeReceiver()) {
			// 如果单接单的人是自己

			btn_begin_workorder.setVisibility(View.VISIBLE);
			if (GlobalData.curWorkorder.OrderPause == Constant.EnumWorkorderPauseStatus.PAUSE_STATUS_WORKORDER_CONTINUE) {
				changeButtonBg(btn_begin_workorder, true);
			}
		}
	}

	private void bindReceiveInfo() {
		if (isReceived()) {
			tv_workorder_receiver.setText(Html.fromHtml(getString(
					R.string.str_maintain_receiver,
					GlobalData.curWorkorder.ChargeUserName + "("
							+ "<u><font color='" + Constant.COLOR_LINK_BLUE
							+ "'>" + GlobalData.curWorkorder.ChargeUserPhone
							+ "</font></u>" + ")")));
			tv_workorder_receiver
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							ConfirmDialogCallUser(
									GlobalData.curWorkorder.ChargeUserName,
									GlobalData.curWorkorder.ChargeUserPhone);
						}
					});
			tv_workorder_receiver.setVisibility(View.VISIBLE);
		} else {
			btn_receive_workorder.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 已开始工单
	 */
	private void initButtonBegin() {
		if (isMeReceiver()) {
			changeButtonBg(btn_workorder_pics, true);
			if (GlobalData.curWorkorder.OrderPause == Constant.EnumWorkorderPauseStatus.PAUSE_STATUS_WORKORDER_CONTINUE) {
				if (GlobalData.WorkerStatus == 1) {
					// 值班模式,可以不用考虑协助人员,直接关闭
					changeButtonBg(btn_complete_workorder_pass, true);
					changeButtonBg(btn_complete_workorder_need_abarbeitung,
							true);
				} else {

					//if (isAllAssistClosed) {
						changeButtonBg(btn_complete_workorder_pass, true);
						changeButtonBg(btn_complete_workorder_need_abarbeitung,
								true);
					//}

				}
			}
		}
	}

	/**
	 * 更新协助人员的状态
	 */
	private void UpdateAssistStatus(int status) {
		String lat = latlng == null ? "" : (latlng.latitude + "");
		String lng = latlng == null ? "" : (latlng.longitude + "");
		new SJECHttpHandler(callBackUpdateAssistWorkerStatus, this)
				.updateAssistUserStatus(GlobalData.curUser.InnerID,
						workorder_id, status, lat, lng);
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
					Util.createToast(ActivityDoCheck.this,
							getString(R.string.msg_control_failed, ""),
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
				Util.createToast(ActivityDoCheck.this,
						getString(R.string.msg_control_failed, ""),
						Toast.LENGTH_SHORT).show();
			}
		};

		public void onFailure(String message) {
			Util.createToast(ActivityDoCheck.this,
					getString(R.string.msg_control_failed, ""),
					Toast.LENGTH_SHORT).show();
		};
	};

	private AsyncHttpCallBack callBackPauseLog = new AsyncHttpCallBack() {

		public void onSuccess(String response) {
			LogUtil.d("%s", response);
			// 更新状态成功

			try {
				JSONObject jobj;
				jobj = new JSONObject(response);
				if (jobj.optString("result", "false").equals("true")) {
					Gson gson = new Gson();
					java.lang.reflect.Type myType = new TypeToken<ArrayList<EntityPauseLog>>() {
					}.getType();
					listPauseLog = gson.fromJson(jobj.optString("data", ""),
							myType);
					BindPauseLog();
				} else {
					Util.createToast(ActivityDoCheck.this,
							getString(R.string.msg_control_failed, ""),
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
				Util.createToast(ActivityDoCheck.this,
						getString(R.string.msg_control_failed, ""),
						Toast.LENGTH_SHORT).show();
			}
		};

		public void onFailure(String message) {
			Util.createToast(ActivityDoCheck.this,
					getString(R.string.msg_control_failed, ""),
					Toast.LENGTH_SHORT).show();
		};
	};

	private AsyncHttpCallBack callBackAssistWorker = new AsyncHttpCallBack() {

		public void onSuccess(String response) {
			LogUtil.d("%s", response);
			// 更新状态成功

			try {
				JSONObject jobj;
				jobj = new JSONObject(response);
				if (jobj.optString("result", "false").equals("true")) {
					Gson gson = new Gson();
					java.lang.reflect.Type myType = new TypeToken<ArrayList<EntityAssistWorker>>() {
					}.getType();
					GlobalData.listAsssitWorker = gson.fromJson(
							jobj.optString("data", ""), myType);
					BindAssistWorker();
				} else {
					Util.createToast(ActivityDoCheck.this,
							getString(R.string.msg_control_failed, ""),
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
				Util.createToast(ActivityDoCheck.this,
						getString(R.string.msg_control_failed, ""),
						Toast.LENGTH_SHORT).show();
			}
		};

		public void onFailure(String message) {
			Util.createToast(ActivityDoCheck.this,
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

					GlobalData.curCheck = gson.fromJson(
							jobj.optJSONObject("data").optString("check", ""),
							EntityCheck.class);

					BindWorkorderData();
					queryAssistWorker();
				} else {
					Util.createToast(ActivityDoCheck.this,
							getString(R.string.msg_control_failed, ""),
							Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Util.createToast(ActivityDoCheck.this,
						getString(R.string.msg_control_failed, ""),
						Toast.LENGTH_SHORT).show();
			}
		};

		public void onFailure(String message) {
			Util.createToast(ActivityDoCheck.this,
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
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};

		public void onFailure(String message) {
		};
	};

	AsyncHttpCallBack callBackDeleteAssistWorker = new AsyncHttpCallBack() {

		public void onSuccess(String response) {
			LogUtil.d("%s", response);
			if (response.contains("true")) {
				Util.createToast(ActivityDoCheck.this,
						R.string.msg_control_success, Toast.LENGTH_SHORT)
						.show();
				queryAssistWorker();
			} else {
				Util.createToast(ActivityDoCheck.this,
						getString(R.string.msg_control_failed, ""),
						Toast.LENGTH_SHORT).show();
			}

		};

		public void onFailure(String message) {
			super.onFailure(message);
			Util.createToast(ActivityDoCheck.this,
					getString(R.string.msg_control_failed, ""),
					Toast.LENGTH_SHORT).show();
		}
	};

	AsyncHttpCallBack callBackUpdateAssistWorkerStatus = new AsyncHttpCallBack() {

		public void onSuccess(String response) {
			LogUtil.d("%s", response);
			if (response.contains("true")) {
				Util.createToast(ActivityDoCheck.this,
						R.string.msg_control_success, Toast.LENGTH_SHORT)
						.show();
				queryAssistWorker();
			} else {
				Util.createToast(ActivityDoCheck.this,
						getString(R.string.msg_control_failed, ""),
						Toast.LENGTH_SHORT).show();
			}

		};

		public void onFailure(String message) {
			super.onFailure(message);
			Util.createToast(ActivityDoCheck.this,
					getString(R.string.msg_control_failed, ""),
					Toast.LENGTH_SHORT).show();
		}
	};

	private void ChangeBottomStyle(View v) {
		tv_base_info.setSelected(false);
		tv_maintain.setSelected(false);
		tv_assist_people.setSelected(false);
		v.setSelected(true);
	}

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {

		if (mListener != null && amapLocation != null) {
			if (amapLocation != null && amapLocation.getErrorCode() == 0) {
				mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
				// Log.e("location", amapLocation.getLongitude() + ","
				// + amapLocation.getLatitude());

				latlng = new LatLng(amapLocation.getLatitude(),
						amapLocation.getLongitude());
			} else {
				String errText = "定位失败," + amapLocation.getErrorCode() + ": "
						+ amapLocation.getErrorInfo();
				Log.e("AmapErr", errText);
			}
		}
	}

	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mlocationClient == null) {
			mlocationClient = new AMapLocationClient(this);
			mLocationOption = new AMapLocationClientOption();
			// 设置定位监听
			mlocationClient.setLocationListener(this);
			// 设置为高精度定位模式
			mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
			// 设置定位参数

			mlocationClient.setLocationOption(mLocationOption);
			// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
			// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
			// 在定位结束后，在合适的生命周期调用onDestroy()方法
			// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
			mlocationClient.startLocation();
		}
	}

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mlocationClient != null) {
			mlocationClient.stopLocation();
			mlocationClient.onDestroy();
		}
		mlocationClient = null;
	}

	@Override
	public void onResume() {
		mapView.onResume();
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
		deactivate();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
		// 退出时销毁定位
		// 关闭定位图层
		try {
			unregisterReceiver(receiver);
			mapView.onDestroy();
			GlobalData.curDevice = null;
			GlobalData.listAsssitWorker.clear();
			GlobalData.listCompanyWorker.clear();
			listPushLog.clear();
			listPauseLog.clear();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.gc();
		}
	}

}
