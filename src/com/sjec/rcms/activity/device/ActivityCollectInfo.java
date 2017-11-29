package com.sjec.rcms.activity.device;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Relation;
import android.provider.Settings.Global;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.sjec.rcms.R;
import com.sjec.rcms.activity.BaseActivity;
import com.sjec.rcms.activity.workorder.ActivityAddWorkorder;
import com.sjec.rcms.dao.EntityDevice;
import com.sjec.rcms.data.Constant;
import com.sjec.rcms.data.GlobalData;
import com.sjec.rcms.http.AsyncHttpCallBack;
import com.sjec.rcms.http.SJECHttpHandler;
import com.sjec.rcms.util.SJECUtil;
import com.websharputil.common.ConvertUtil;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;
import com.websharputil.widget.ThumbImageView;

public class ActivityCollectInfo extends BaseActivity {

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initLayout() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void bindData() {
		// TODO Auto-generated method stub
		
	}
	

//	LocationClient mLocClient;
//	public MyLocationListenner myListener = new MyLocationListenner();
//	private LocationMode mCurrentMode;
//	BitmapDescriptor mCurrentMarker;
//	private LinearLayout layout_back;
//	private TextView tv_title;
//
//	MapView mMapView;
//	BaiduMap mBaiduMap;
//	boolean isFirstLoc = true;// 是否首次定位
//	private ThumbImageView btn_location, btn_update_location,
//			btn_location_device;
//	// 概 要信息
//	private TextView tv_xqmc, tv_xqdz, tv_sbh, tv_zh, tv_th, tv_latlng;
//	// 基本信息
//	private TextView tv_xmmc, tv_hth, tv_txtz, tv_dtxh, tv_jhrq, tv_zzcs,
//			tv_wbgs, tv_wbsqh, tv_wbxx, tv_cpgg, tv_sydw, tv_sbdz;
//
//	private LatLng latLng = null;
//	private LatLng deviceLatLng = null;
//	private double deviceNewLat = 0;
//	private double deviceNewLng = 0;
//	private InfoWindow mInfoWindow;
//
//	private RelativeLayout layout_map;
//	private TextView tv_collect_map;
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.btn_location:
//			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
//			mBaiduMap.animateMapStatus(u);
//			break;
//		case R.id.layout_back:
//			Util.finishActivity(this);
//			break;
//		case R.id.btn_location_device:
//			MapStatusUpdate uDevice = MapStatusUpdateFactory
//					.newLatLng(deviceLatLng);
//			mBaiduMap.animateMapStatus(uDevice);
//			break;
//		case R.id.btn_update_location:
//			updateDeviceLocation();
//			break;
//		case R.id.tv_collect_map:
//			if(layout_map.getVisibility() == View.GONE){
//				layout_map.setVisibility(View.VISIBLE);
//			}else{
//				layout_map.setVisibility(View.GONE);
//			}
//			break;
//		}
//	}
//
//	@Override
//	public void initLayout() {
//		setContentView(R.layout.activity_collect_info);
//	}
//
//	@Override
//	public void init() {
//		mCurrentMode = LocationMode.NORMAL;
//		layout_back = (LinearLayout) findViewById(R.id.layout_back);
//		tv_title = (TextView) findViewById(R.id.tv_title);
//		mMapView = (MapView) findViewById(R.id.map_district);
//		btn_location = (ThumbImageView) findViewById(R.id.btn_location);
//		btn_update_location = (ThumbImageView) findViewById(R.id.btn_update_location);
//		btn_location_device = (ThumbImageView) findViewById(R.id.btn_location_device);
//
//		tv_xqmc = (TextView) findViewById(R.id.tv_xqmc);
//		tv_xqdz = (TextView) findViewById(R.id.tv_xqdz);
//		tv_sbh = (TextView) findViewById(R.id.tv_sbh);
//		tv_zh = (TextView) findViewById(R.id.tv_zh);
//		tv_th = (TextView) findViewById(R.id.tv_th);
//		tv_latlng = (TextView) findViewById(R.id.tv_latlng);
//
//		tv_xmmc = (TextView) findViewById(R.id.tv_xmmc);
//		tv_hth = (TextView) findViewById(R.id.tv_hth);
//		tv_txtz = (TextView) findViewById(R.id.tv_txtz);
//		tv_dtxh = (TextView) findViewById(R.id.tv_dtxh);
//		tv_jhrq = (TextView) findViewById(R.id.tv_jhrq);
//		tv_zzcs = (TextView) findViewById(R.id.tv_zzcs);
//		tv_wbgs = (TextView) findViewById(R.id.tv_wbgs);
//		tv_wbsqh = (TextView) findViewById(R.id.tv_wbsqh);
//		tv_wbxx = (TextView) findViewById(R.id.tv_wbxx);
//		tv_cpgg = (TextView) findViewById(R.id.tv_cpgg);
//		tv_sydw = (TextView) findViewById(R.id.tv_sydw);
//		tv_sbdz = (TextView) findViewById(R.id.tv_sbdz);
//		layout_map = (RelativeLayout) findViewById(R.id.layout_map);
//		tv_collect_map = (TextView) findViewById(R.id.tv_collect_map);
//		btn_update_location.setVisibility(View.VISIBLE);
//		btn_location_device.setVisibility(View.VISIBLE);
//		layout_back.setOnClickListener(this);
//		btn_location.setOnClickListener(this);
//		btn_update_location.setOnClickListener(this);
//		btn_location_device.setOnClickListener(this);
//		tv_collect_map.setOnClickListener(this);
//
//	}
//
//	@Override
//	public void bindData() {
//		Bundle b = getIntent().getExtras();
//		int type = b.getInt("type");
//		// 1:设备列表点击过来,2:扫描二维码过来,这时需要加载设备信息
//		if (type == 2) {
//			String result = b.getString("data");
//			if (SJECUtil.CheckQrResult(result)) {
//				String[] arrDeviceInfo = SJECUtil
//						.GetDeviceInfoArrayFromQrCode(result);
//				if (arrDeviceInfo != null) {
//					// 加载设备信息
//					queryDeviceByDeviceNum(arrDeviceInfo[0], arrDeviceInfo[2]);
//				} else {
//					Util.createToast(ActivityCollectInfo.this,
//							R.string.str_maintain_qr_failed, Toast.LENGTH_LONG)
//							.show();
//					finish();
//				}
//			} else {
//				Util.createToast(ActivityCollectInfo.this,
//						R.string.str_qr_invalid, Toast.LENGTH_LONG).show();
//				finish();
//			}
//			return;
//		} else {
//			BindDeviceData();
//		}
//	}
//
//	private void BindDeviceData() {
//		tv_title.setText(R.string.main_xxcj);
//		tv_xqmc.setText(GlobalData.curDevice.Village_Name);
//		tv_xqdz.setText(GlobalData.curDevice.Village_Address);
//		tv_sbh.setText(GlobalData.curDevice.Device_Num);
//		tv_zh.setText(GlobalData.curDevice.Village_Group_Num);
//		tv_th.setText(GlobalData.curDevice.Village_Stage_Num);
//
//		tv_xmmc.setText(GlobalData.curDevice.Program_Name);
//		tv_hth.setText(GlobalData.curDevice.Contract_Num);
//		tv_txtz.setText(GlobalData.curDevice.Lift_Type);
//		tv_dtxh.setText(GlobalData.curDevice.Lift_Style);
//		tv_jhrq.setText(GlobalData.curDevice.Delivery_Date);
//		tv_zzcs.setText(GlobalData.curDevice.Manufacturer);
//		// tv_wbgs.setText(GlobalData.curDevice.);
//		// tv_wbsqh.setText();
//		// tv_wbxx.setText();
//		tv_cpgg.setText(GlobalData.curDevice.Specification);
//		tv_sydw.setText(GlobalData.curDevice.User_Company);
//		tv_sbdz.setText(GlobalData.curDevice.Village_Address + ""
//				+ GlobalData.curDevice.Lift_Address);
//
//		BindMap();
//		BindDeviceOldLocation();
//	}
//
//	private void BindMap() {
//		mBaiduMap = mMapView.getMap();
//		// 开启定位图层
//		mBaiduMap.setMyLocationEnabled(true);
//		// mCurrentMarker = BitmapDescriptorFactory
//		// .fromResource(R.drawable.icon_trans);
//		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
//				mCurrentMode, true, null));
//		MapStatus mMapStatus = new MapStatus.Builder().zoom(20).build();
//
//		// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
//		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
//				.newMapStatus(mMapStatus);
//		// 改变地图状态
//		mBaiduMap.animateMapStatus(mMapStatusUpdate);
//
//		// 定位初始化
//		LocationClientOption option = new LocationClientOption();
//		option.setLocationMode(com.baidu.location.LocationClientOption.LocationMode.Hight_Accuracy);
//		option.setOpenGps(true);// 打开gps
//		option.setCoorType("bd09ll"); // 设置坐标类型
//		option.setScanSpan(1000);
//
//		mLocClient = new LocationClient(this);
//		mLocClient.registerLocationListener(myListener);
//		mLocClient.setLocOption(option);
//		mLocClient.start();
//
//	}
//
//	/**
//	 * 如果电梯现在有坐标,那么绑定
//	 */
//	private void BindDeviceOldLocation() {
//		// 120.742616,31.367646
//		/*
//		 * GlobalData.curDevice.Latitude = "31.367636";
//		 * GlobalData.curDevice.Longitude = "120.742606";
//		 */
//		if (GlobalData.curDevice.Latitude.isEmpty()
//				|| ConvertUtil.ParsetStringToDouble(
//						GlobalData.curDevice.Latitude, 0) == 0) {
//			btn_location_device.setVisibility(View.GONE);
//		} else {
//			btn_location_device.setVisibility(View.VISIBLE);
//		}
//		if (!GlobalData.curDevice.Latitude.isEmpty()
//				&& !GlobalData.curDevice.Longitude.isEmpty()) {
//			tv_latlng.setText(getString(R.string.str_device_location_value,
//					GlobalData.curDevice.Latitude,
//					GlobalData.curDevice.Longitude));
//			LayoutInflater mInflater = LayoutInflater
//					.from(ActivityCollectInfo.this);
//			deviceLatLng = new LatLng(
//					Double.parseDouble(GlobalData.curDevice.Latitude),
//					Double.parseDouble(GlobalData.curDevice.Longitude));
//
//			View convertView = mInflater.inflate(R.layout.item_marker, null);
//			TextView tv = (TextView) convertView.findViewById(R.id.tv_marker);
//
//			tv.setText(GlobalData.curDevice.Village_Name
//					+ GlobalData.curDevice.Village_Group_Num + "组"
//					+ GlobalData.curDevice.Village_Stage_Num + "台"
//					+ GlobalData.curDevice.Device_Num);
//			mInfoWindow = new InfoWindow(
//					BitmapDescriptorFactory.fromView(convertView),
//					deviceLatLng, 0, null);
//
//			mBaiduMap.showInfoWindow(mInfoWindow);
//		}
//	}
//
//	// 120.742616,31.367646
//	public class MyLocationListenner implements BDLocationListener {
//
//		@Override
//		public void onReceiveLocation(BDLocation location) {
//			// map view 销毁后不在处理新接收的位置
//			LogUtil.d("%s,%s", location.getLongitude(), location.getLatitude());
//			mLocClient.stop();
//			if (location == null || mMapView == null)
//				return;
//			MyLocationData locData = new MyLocationData.Builder()
//					// 此处设置开发者获取到的方向信息，顺时针0-360
//					.direction(100).latitude(location.getLatitude())
//					.longitude(location.getLongitude()).build();
//			// 显示现在人所在的地址
//			mBaiduMap.setMyLocationData(locData);
//
//			latLng = new LatLng(location.getLatitude(), location.getLongitude());
//			// LayoutInflater mInflater = LayoutInflater
//			// .from(ActivityCollectInfo.this);
//			if (isFirstLoc) {
//				// 移动屏到我现在所处的点
//
//				isFirstLoc = false;
//				latLng = new LatLng(location.getLatitude(),
//						location.getLongitude());
//				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
//				mBaiduMap.animateMapStatus(u);
//
//				//
//				// View convertView = mInflater
//				// .inflate(R.layout.item_marker, null);
//				// TextView tv = (TextView) convertView
//				// .findViewById(R.id.tv_marker);
//				//
//				// tv.setText(GlobalData.curDevice.Village_Name
//				// + GlobalData.curDevice.Device_Num);
//				// mInfoWindow = new InfoWindow(
//				// BitmapDescriptorFactory.fromView(convertView), latLng,
//				// 0, null);
//				//
//				// mBaiduMap.showInfoWindow(mInfoWindow);
//			}
//
//		}
//
//		public void onReceivePoi(BDLocation poiLocation) {
//		}
//	}
//
//	@Override
//	public void onResume() {
//		// TODO Auto-generated method stub
//		mMapView.onResume();
//		super.onResume();
//	}
//
//	@Override
//	public void onDestroy() {
//		// 退出时销毁定位
//		// 关闭定位图层
//		try {
//			if (mLocClient != null) {
//				mLocClient.stop();
//			}
//			mBaiduMap.setMyLocationEnabled(false);
//			mMapView.onDestroy();
//			mMapView = null;
//			GlobalData.curDevice = null;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		super.onDestroy();
//	}
//
//	private void queryDeviceByDeviceNum(String company_num, String device_num) {
//		new SJECHttpHandler(callBackQueryDevice, this).getDeviceByDeviceNum(
//				GlobalData.curUser.InnerID, company_num, device_num);
//	}
//
//	private void updateDeviceLocation() {
//		deviceNewLat = latLng.latitude;
//		deviceNewLng = latLng.longitude;
//		new SJECHttpHandler(callBackUpdateLocation, this).updateDeviceLocation(
//				GlobalData.curUser.InnerID, GlobalData.curDevice.InnerID,
//				deviceNewLat + "", deviceNewLng + "");
//	}
//
//	private AsyncHttpCallBack callBackUpdateLocation = new AsyncHttpCallBack() {
//
//		public void onSuccess(String response) {
//			LogUtil.d("%s", response);
//			JSONObject jobj;
//			try {
//				jobj = new JSONObject(response);
//
//				if (jobj.optString("result", "false").equals("true")) {
//					Util.createToast(ActivityCollectInfo.this,
//							R.string.msg_update_device_location_success,
//							Toast.LENGTH_SHORT).show();
//					// 更新电梯的最新坐标地址
//
//					GlobalData.curDevice.Longitude = deviceNewLng + "";
//					GlobalData.curDevice.Latitude = deviceNewLat + "";
//					// 更新deviceList中的从标
//					for (int i = 0; i < GlobalData.listDevice.size(); i++) {
//						if (GlobalData.curDevice.InnerID
//								.equals(GlobalData.listDevice.get(i).InnerID)) {
//							GlobalData.listDevice.get(i).Longitude = deviceNewLng
//									+ "";
//							GlobalData.listDevice.get(i).Latitude = deviceNewLat
//									+ "";
//							getApplicationContext()
//									.sendBroadcast(
//											new Intent(
//													Constant.ACTION_REFRESH_DEVICE_LIST_ATTR));
//							break;
//						}
//					}
//					BindDeviceOldLocation();
//				} else {
//					Util.createToast(ActivityCollectInfo.this,
//							R.string.msg_update_device_location_failed,
//							Toast.LENGTH_SHORT).show();
//				}
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		};
//
//		public void onFailure(String message) {
//		};
//	};
//
//	private AsyncHttpCallBack callBackQueryDevice = new AsyncHttpCallBack() {
//
//		public void onSuccess(String response) {
//			LogUtil.d("%s", response);
//			JSONObject jobj;
//			try {
//				jobj = new JSONObject(response);
//
//				if (jobj.optString("result", "false").equals("true")) {
//					Gson gson = new Gson();
//					GlobalData.curDevice = gson.fromJson(
//							jobj.optString("data", ""), EntityDevice.class);
//					BindDeviceData();
//				} else {
//					// Util.createToast(ActivityCollectInfo.this,
//					// R.string.msg_update_device_location_failed,
//					// Toast.LENGTH_SHORT).show();
//				}
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		};
//
//		public void onFailure(String message) {
//		};
//	};

}
