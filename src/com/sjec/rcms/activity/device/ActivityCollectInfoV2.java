package com.sjec.rcms.activity.device;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Relation;
import android.provider.Settings.Global;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnCameraChangeListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.LocationSource.OnLocationChangedListener;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.VisibleRegion;
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

public class ActivityCollectInfoV2 extends BaseActivity implements LocationSource,
AMapLocationListener,OnCameraChangeListener{
	private LinearLayout layout_back;
	private TextView tv_title;

	boolean isFirstLoc = true;// 是否首次定位
	private ThumbImageView  btn_update_location,
			btn_location_device;
	// 概 要信息
	private TextView tv_xqmc, tv_xqdz, tv_sbh, tv_zh, tv_th, tv_latlng;
	// 基本信息
	private TextView tv_xmmc, tv_hth, tv_txtz, tv_dtxh, tv_jhrq, tv_zzcs,
			tv_wbgs, tv_wbsqh, tv_wbxx, tv_cpgg, tv_sydw, tv_sbdz;

	private double deviceNewLat = 0;
	private double deviceNewLng = 0;

	private RelativeLayout layout_map;
	private TextView tv_collect_map;
	
	private AMap aMap;
	private MapView mapView;
	private OnLocationChangedListener mListener;
	private AMapLocationClient mlocationClient;
	private AMapLocationClientOption mLocationOption;
	private LatLng latlng;
	private LatLng manualLatlng;
	private LatLng deviceLatLng;
	private Marker deviceMarker;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.layout_back:
			Util.finishActivity(this);
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
		case R.id.btn_update_location:
			updateDeviceLocation();
			break;
		case R.id.tv_collect_map:
			if(layout_map.getVisibility() == View.GONE){
				layout_map.setVisibility(View.VISIBLE);
			}else{
				layout_map.setVisibility(View.GONE);
			}
			break;
		}
	}

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_collect_info);
	}

	@Override
	public void init(Bundle savedInstanceState) {
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		btn_update_location = (ThumbImageView) findViewById(R.id.btn_update_location);
		btn_location_device = (ThumbImageView) findViewById(R.id.btn_location_device);

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
		layout_map = (RelativeLayout) findViewById(R.id.layout_map);
		tv_collect_map = (TextView) findViewById(R.id.tv_collect_map);
		btn_update_location.setVisibility(View.VISIBLE);
		btn_location_device.setVisibility(View.VISIBLE);
		layout_back.setOnClickListener(this);
		btn_update_location.setOnClickListener(this);
		btn_location_device.setOnClickListener(this);
		tv_collect_map.setOnClickListener(this);
		
		mapView = (MapView) findViewById(R.id.map_district);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
	}
	
	private void setUpMap() {
		// 自定义系统定位小蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.location_marker));// 设置小蓝点的图标
//		myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
//		myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
		myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色  
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色  
		// myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        
		myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setLocationSource(this);// 设置定位监听
		aMap.moveCamera(CameraUpdateFactory.zoomTo(19)) ;
		
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
	   // aMap.setMyLocationType()
		aMap.setOnCameraChangeListener(this);
	}

	@Override
	public void bindData() {
		Bundle b = getIntent().getExtras();
		int type = b.getInt("type");
		// 1:设备列表点击过来,2:扫描二维码过来,这时需要加载设备信息
		if (type == 2) {
			String result = b.getString("data");
			if (SJECUtil.CheckQrResult(result)) {
				String[] arrDeviceInfo = SJECUtil
						.GetDeviceInfoArrayFromQrCode(result);
				if (arrDeviceInfo != null) {
					// 加载设备信息
					queryDeviceByDeviceNum(arrDeviceInfo[0], arrDeviceInfo[2]);
				} else {
					Util.createToast(ActivityCollectInfoV2.this,
							R.string.str_maintain_qr_failed, Toast.LENGTH_LONG)
							.show();
					finish();
				}
			} else {
				Util.createToast(ActivityCollectInfoV2.this,
						R.string.str_qr_invalid, Toast.LENGTH_LONG).show();
				finish();
			}
			return;
		} else {
			BindDeviceData();
		}
	}

	private void BindDeviceData() {
		tv_title.setText(R.string.main_xxcj);
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

		BindDeviceOldLocation();
	}

	

	/**
	 * 如果电梯现在有坐标,那么绑定
	 */
	private void BindDeviceOldLocation() {
		// 120.742616,31.367646
		/*
		 * GlobalData.curDevice.Latitude = "31.367636";
		 * GlobalData.curDevice.Longitude = "120.742606";
		 */
		if (GlobalData.curDevice.Latitude.isEmpty()
				|| ConvertUtil.ParsetStringToDouble(
						GlobalData.curDevice.Latitude, 0) == 0) {
			btn_location_device.setVisibility(View.GONE);
		} else {
			btn_location_device.setVisibility(View.VISIBLE);
		}
		if (!GlobalData.curDevice.Latitude.isEmpty()
				&& !GlobalData.curDevice.Longitude.isEmpty()) {
			tv_latlng.setText(getString(R.string.str_device_location_value,
					GlobalData.curDevice.Latitude,
					GlobalData.curDevice.Longitude));
			LayoutInflater mInflater = LayoutInflater
					.from(ActivityCollectInfoV2.this);
			deviceLatLng = new LatLng(
					Double.parseDouble(GlobalData.curDevice.Latitude),
					Double.parseDouble(GlobalData.curDevice.Longitude));

			View convertView = mInflater.inflate(R.layout.item_marker, null);
			TextView tv = (TextView) convertView.findViewById(R.id.tv_marker);

			tv.setText(GlobalData.curDevice.Village_Name
					+ GlobalData.curDevice.Village_Group_Num + "组"
					+ GlobalData.curDevice.Village_Stage_Num + "台"
					+ GlobalData.curDevice.Device_Num);
//			mInfoWindow = new InfoWindow(
//					BitmapDescriptorFactory.fromView(convertView),
//					deviceLatLng, 0, null);
//
//			mBaiduMap.showInfoWindow(mInfoWindow);
			if(deviceMarker!=null){
				deviceMarker.remove();
				deviceMarker.destroy();
			}
			deviceMarker = aMap.addMarker(new MarkerOptions()
			.position(deviceLatLng)
			.icon(BitmapDescriptorFactory.fromView(convertView))
			.draggable(true));
			
			deviceMarker.showInfoWindow();// 设置默认显示一个infowinfow
		}
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
		// 退出时销毁定位
		// 关闭定位图层
		try {
	
			mapView.onDestroy();
			GlobalData.curDevice = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}

	private void queryDeviceByDeviceNum(String company_num, String device_num) {
		new SJECHttpHandler(callBackQueryDevice, this).getDeviceByDeviceNum(company_num, device_num);
	}
	
	
	public void ConfirmDialogUpdateDeviceLocation(){
		Builder dialogBuilder = new AlertDialog.Builder(
				this);
		dialogBuilder.setIcon(android.R.color.transparent);
		dialogBuilder.setCancelable(true);
		dialogBuilder.setNegativeButton(R.string.msg_dialog_cancel, null);
		dialogBuilder
				.setTitle(R.string.msg_dialog_add_order_type_title);

		dialogBuilder.setMessage(R.string.msg_confirm_control);
		dialogBuilder.setPositiveButton(R.string.msg_dialog_confirm,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						// 更新状态为开始
						if(manualLatlng!=null){
							deviceNewLat = manualLatlng.latitude;
							deviceNewLng = manualLatlng.longitude;
						}else{
							deviceNewLat = latlng.latitude;
							deviceNewLng = latlng.longitude;
						}
						new SJECHttpHandler(callBackUpdateLocation, ActivityCollectInfoV2.this).updateDeviceLocation( GlobalData.curDevice.InnerID,
								deviceNewLat + "", deviceNewLng + "");
					}
				});

		AlertDialog dialog = dialogBuilder.create();
		dialog.show();
	}
	
	

	private void updateDeviceLocation() {
		if (!GlobalData.curDevice.Latitude.isEmpty()
				&& !GlobalData.curDevice.Longitude.isEmpty()) {
			//不是第一次更新电梯位置，需要提示确认
			ConfirmDialogUpdateDeviceLocation();
		}else{ 
			if(manualLatlng!=null){
				deviceNewLat = manualLatlng.latitude;
				deviceNewLng = manualLatlng.longitude;
			}else{
				deviceNewLat = latlng.latitude;
				deviceNewLng = latlng.longitude;
			}
			new SJECHttpHandler(callBackUpdateLocation, this).updateDeviceLocation( GlobalData.curDevice.InnerID,
					deviceNewLat + "", deviceNewLng + "");
		}
	}

	private AsyncHttpCallBack callBackUpdateLocation = new AsyncHttpCallBack() {

		public void onSuccess(String response) {
			LogUtil.d("%s", response);
			JSONObject jobj;
			try {
				jobj = new JSONObject(response);

				if (jobj.optString("result", "false").equals("true")) {
					Util.createToast(ActivityCollectInfoV2.this,
							R.string.msg_update_device_location_success,
							Toast.LENGTH_SHORT).show();
					// 更新电梯的最新坐标地址

					GlobalData.curDevice.Longitude = deviceNewLng + "";
					GlobalData.curDevice.Latitude = deviceNewLat + "";
					// 更新deviceList中的从标
					for (int i = 0; i < GlobalData.listDevice.size(); i++) {
						if (GlobalData.curDevice.InnerID
								.equals(GlobalData.listDevice.get(i).InnerID)) {
							GlobalData.listDevice.get(i).Longitude = deviceNewLng
									+ "";
							GlobalData.listDevice.get(i).Latitude = deviceNewLat
									+ "";
							getApplicationContext()
									.sendBroadcast(
											new Intent(
													Constant.ACTION_REFRESH_DEVICE_LIST_ATTR));
							break;
						}
					}
					BindDeviceOldLocation();
				} else {
					Util.createToast(ActivityCollectInfoV2.this,
							R.string.msg_update_device_location_failed,
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};

		public void onFailure(String message) {
		};
	};

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

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		
		if (mListener != null && amapLocation != null) {
			if (amapLocation != null
					&& amapLocation.getErrorCode() == 0) {
				mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
				Log.e("location",amapLocation.getLongitude()+","+amapLocation.getLatitude());
				
				latlng =new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()) ;
			} else {
				String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
				Log.e("AmapErr",errText);
			}
		}
	}
	
	/**
	 * 对移动地图结束事件回调
	 */
	@Override
	public void onCameraChangeFinish(CameraPosition cameraPosition) {
//		mCameraTextView.setText("onCameraChangeFinish:"
//				+ cameraPosition.toString());
//		
//		VisibleRegion visibleRegion = aMap.getProjection().getVisibleRegion(); // 获取可视区域、
//
//		LatLngBounds latLngBounds = visibleRegion.latLngBounds;// 获取可视区域的Bounds
//		boolean isContain = latLngBounds.contains(Constants.SHANGHAI);// 判断上海经纬度是否包括在当前地图可见区域
//		if (isContain) {
//			ToastUtil.show(EventsActivity.this, "上海市在地图当前可见区域内");
//		} else {
//			ToastUtil.show(EventsActivity.this, "上海市超出地图当前可见区域");
//		}
//		LayoutInflater mInflater = LayoutInflater
//				.from(ActivityCollectInfoV2.this);
//		View convertView = mInflater.inflate(R.layout.item_marker, null);
//		TextView tv = (TextView) convertView.findViewById(R.id.tv_marker);
//	
//		Marker marker = aMap.addMarker(new MarkerOptions()
//		.position(cameraPosition.target)
//		.icon(BitmapDescriptorFactory.fromView(convertView))
//		.draggable(true));
//		marker.showInfoWindow();
	}

	@Override
	public void onCameraChange(CameraPosition arg0) {
		// TODO Auto-generated method stub
		//LogUtil.d(arg0.toString());
		manualLatlng = arg0.target;
	}
	
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mlocationClient == null) {
			mlocationClient = new AMapLocationClient(this);
			mLocationOption = new AMapLocationClientOption();
			//设置定位监听
			mlocationClient.setLocationListener(this);
			//设置为高精度定位模式
			mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
			//设置定位参数
			
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

	

}
