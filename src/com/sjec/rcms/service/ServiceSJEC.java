package com.sjec.rcms.service;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.sjec.rcms.R;
import com.sjec.rcms.activity.user.ActivityConfirmList;
import com.sjec.rcms.activity.workorder.ActivityDoCheck;
import com.sjec.rcms.activity.workorder.ActivityDoMaintainV2;
import com.sjec.rcms.activity.workorder.ActivityDoRepairV2;
import com.sjec.rcms.baidu.MyApplication;
import com.sjec.rcms.dao.EntityUnSubmitWorkorder;
import com.sjec.rcms.dao.EntityUnSubmitWorkorderDao;
import com.sjec.rcms.dao.EntityWorkorder;
import com.sjec.rcms.data.Constant;
import com.sjec.rcms.data.GlobalData;
import com.sjec.rcms.http.AsyncHttpCallBack;
import com.sjec.rcms.http.SJECHttpHandler;
import com.sjec.rcms.service.NetBroadcastReceiver.NetEvent;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class ServiceSJEC extends Service {

	// public static NetEvent event;
	/**
	 * 网络类型
	 */
	private int netMobile;
	private static int NOTIFICATION_ID = 0x30001;// 通知栏消息id

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					ConnectivityManager.CONNECTIVITY_ACTION)) {
				int netWorkState = NetUtil.getNetWorkState(context);
				// 接口回调传过去状态的类型
				try {
					onNetChange(netWorkState);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};

	Handler handler = new Handler() {
		public void dispatchMessage(android.os.Message msg) {
			if (msg.what == 0) {
				// LogUtil.d("time:" + new Date().toLocaleString());
				boolean isConnect = isNetConnect();
				LogUtil.d("onNetChange:" + isConnect);
				if (isConnect) {
					new AsyncSubmit()
							.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				}
				handler.sendEmptyMessageDelayed(0, 180000);
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		LogUtil.d("create service");
		handler.sendEmptyMessageDelayed(0, 1000);
		// event = this;
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(receiver, filter);
		inspectNet();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtil.d("close service");
		unregisterReceiver(receiver);
		handler.removeMessages(0);
	}

	/**
	 * 初始化时判断有没有网络
	 */

	public boolean inspectNet() {
		this.netMobile = NetUtil.getNetWorkState(ServiceSJEC.this);
		if (netMobile == 1) {
			System.err.println("inspectNet:连接wifi");
		} else if (netMobile == 0) {
			System.err.println("inspectNet:连接移动数据");
		} else if (netMobile == -1) {
			System.err.println("inspectNet:当前没有网络");
		}
		return isNetConnect();
	}

	EntityUnSubmitWorkorderDao daoUnSubmit = null;

	// /**
	// * 网络变化之后的类型
	// */
	// @Override
	// public void onNetChange(int netMobile) {
	// // TODO Auto-generated method stub
	// this.netMobile = netMobile;
	// boolean isConnect = isNetConnect();
	// LogUtil.d("onNetChange:" + isConnect);
	// if (isConnect) {
	// for (int i = 0; i < 3; i++) {
	// submitUncompleteOrder();
	// }
	// //new AsyncSubmit().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	// }
	// }

	public void onNetChange(int netMobile) {
		// TODO Auto-generated method stub
		this.netMobile = netMobile;
		boolean isConnect = isNetConnect();
		LogUtil.d("onNetChange:" + isConnect);
		if (isConnect) {
			new AsyncSubmit().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
	}

	class AsyncSubmit extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			for (int i = 0; i < 3; i++) {
				submitUncompleteOrder();
			}
			return null;
		}
	}

	/**
	 * 判断有无网络 。
	 * 
	 * @return true 有网, false 没有网络.
	 */
	public boolean isNetConnect() {
		if (netMobile == 1) {
			System.err.println("inspectNet：连接wifi");
			return true;
		} else if (netMobile == 0) {
			System.err.println("inspectNet:连接移动数据");
			return true;
		} else if (netMobile == -1) {
			System.err.println("inspectNet:当前没有网络");
			return false;
		}
		return false;
	}

	private void submitUncompleteOrder() {
		if (daoUnSubmit == null) {
			daoUnSubmit = MyApplication.daoSession
					.getEntityUnSubmitWorkorderDao();
		}
		if (GlobalData.curUser != null) {
			ArrayList<EntityUnSubmitWorkorder> listUnSubmit = (ArrayList<EntityUnSubmitWorkorder>) daoUnSubmit
					.queryBuilder()
					.where(EntityUnSubmitWorkorderDao.Properties.UserID
							.eq(GlobalData.curUser.InnerID)).list();
			LogUtil.d("unsubmitcount:" + listUnSubmit.size());

			for (int i = 0; i < listUnSubmit.size(); i++) {
				if (listUnSubmit.get(i).Type == Constant.EnumWorkorderType.TYPE_WORKORDER_CHECK) {
					new SJECHttpHandler(callBackUpdateStatus, this)
							.updateWorkorderStatusForCheck(
									listUnSubmit.get(i).InnerID,
									listUnSubmit.get(i).Status,
									listUnSubmit.get(i).Latitude,
									listUnSubmit.get(i).Longitude,
									listUnSubmit.get(i).Remark, "0",
									listUnSubmit.get(i).IsNeedAbarbeitung,
									listUnSubmit.get(i).ResultType, true);
				} else {
					new SJECHttpHandler(callBackUpdateStatus, this)
							.updateWorkorderStatusForMaintain(
									listUnSubmit.get(i).InnerID,
									listUnSubmit.get(i).Status,
									listUnSubmit.get(i).Latitude,
									listUnSubmit.get(i).Longitude,
									listUnSubmit.get(i).Remark,
									listUnSubmit.get(i).ResultType, true);
				}
			}
		} else {
			LogUtil.d("unlogin:");
		}

	}

	private AsyncHttpCallBack callBackUpdateStatus = new AsyncHttpCallBack() {

		public void onSuccess(String response) {
			LogUtil.d("%s", response);
			// 更新状态成功

			JSONObject jobj;
			try {
				jobj = new JSONObject(response);
				if (jobj.optString("result", "false").equals("true")) {

					Gson gson = new Gson();
					EntityWorkorder workorder = gson.fromJson(
							jobj.optString("data", ""), EntityWorkorder.class);
					if (daoUnSubmit == null) {
						daoUnSubmit = MyApplication.daoSession
								.getEntityUnSubmitWorkorderDao();
					}
					daoUnSubmit.deleteByKey(workorder.InnerID);
					showNotification(workorder);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		};

		public void onFailure(String message) {

		};
	};

	private void showNotification(EntityWorkorder workorder) {
		PendingIntent pi = null;
		Intent intent = null;
		if (workorder.Type.equals(Constant.EnumWorkorderType.TYPE_WORKORDER_REPAIR)) {
			intent = new Intent(ServiceSJEC.this, ActivityDoRepairV2.class);
		} else if (workorder.Type.equals(Constant.EnumWorkorderType.TYPE_WORKORDER_MAINTAIN)) {
			intent = new Intent(ServiceSJEC.this, ActivityDoMaintainV2.class);
		} else if (workorder.Type.equals(Constant.EnumWorkorderType.TYPE_WORKORDER_CHECK)) {
			intent = new Intent(ServiceSJEC.this, ActivityDoCheck.class);
		}

		if (intent != null) {
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			Bundle b = new Bundle();
			b.putString("workorder_id", workorder.InnerID);
			intent.putExtras(b);
		}

		pi = PendingIntent.getActivity(ServiceSJEC.this,
				(int) (Math.random() * 1000), intent,
				PendingIntent.FLAG_CANCEL_CURRENT);

		Notification notify = new Notification();
		notify.icon = R.drawable.ic_launcher;
		// notify.flags = Notification.FLAG_AUTO_CANCEL
		// | Notification.FLAG_NO_CLEAR;
		notify.flags = Notification.FLAG_AUTO_CANCEL;
		notify.tickerText = "工单[" + workorder.Device_Num + "]已自动提交完成,点击可查看工单详情";
		notify.when = System.currentTimeMillis();
		notify.defaults = Notification.DEFAULT_ALL;
		notify.setLatestEventInfo(ServiceSJEC.this, "自动提交工单",notify.tickerText.toString(), pi);
		// String click = "点击查看";
		NotificationManager notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
		notificationManager.notify(NOTIFICATION_ID++, notify);
	}

}
