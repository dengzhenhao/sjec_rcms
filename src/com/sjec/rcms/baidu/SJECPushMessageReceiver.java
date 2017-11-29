package com.sjec.rcms.baidu;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.baidu.android.pushservice.PushMessageReceiver;
import com.sjec.rcms.R;
import com.sjec.rcms.activity.device.ActivityCollectInfo;
import com.sjec.rcms.activity.main.ActivityMain;
import com.sjec.rcms.activity.user.ActivityConfirmList;
import com.sjec.rcms.activity.workorder.ActivityDoMaintain;
import com.sjec.rcms.activity.workorder.ActivityDoMaintainV2;
import com.sjec.rcms.activity.workorder.ActivityDoRepair;
import com.sjec.rcms.activity.workorder.ActivityDoRepairV2;
import com.sjec.rcms.data.Constant;
import com.sjec.rcms.data.GlobalData;
import com.sjec.rcms.http.AsyncHttpCallBack;
import com.sjec.rcms.http.SJECHttpHandler;
import com.websharputil.common.ConvertUtil;
import com.websharputil.common.LogUtil;

/*
 * Push消息处理receiver。请编写您需要的回调函数， 一般来说： onBind是必须的，用来处理startWork返回值；
 *onMessage用来接收透传消息； onSetTags、onDelTags、onListTags是tag相关操作的回调；
 *onNotificationClicked在通知被点击时回调； onUnbind是stopWork接口的返回值回调

 * 返回值中的errorCode，解释如下：
 *0 - Success
 *10001 - Network Problem
 *10101  Integrate Check Error
 *30600 - Internal Server Error
 *30601 - Method Not Allowed
 *30602 - Request Params Not Valid
 *30603 - Authentication Failed
 *30604 - Quota Use Up Payment Required
 *30605 -Data Required Not Found
 *30606 - Request Time Expires Timeout
 *30607 - Channel Token Timeout
 *30608 - Bind Relation Not Found
 *30609 - Bind Number Too Many

 * 当您遇到以上返回错误时，如果解释不了您的问题，请用同一请求的返回值requestId和errorCode联系我们追查问题。
 *
 */

public class SJECPushMessageReceiver extends PushMessageReceiver {
	/** TAG to Log */
	public static final String TAG = SJECPushMessageReceiver.class
			.getSimpleName();
	private static int NOTIFICATION_ID = 0x30001;// 通知栏消息id

	/**
	 * 调用PushManager.startWork后，sdk将对push
	 * server发起绑定请求，这个过程是异步的。绑定请求的结果通过onBind返回。 如果您需要用单播推送，需要把这里获取的channel
	 * id和user id上传到应用server中，再调用server接口用channel id和user id给单个手机或者用户推送。
	 * 
	 * @param context
	 *            BroadcastReceiver的执行Context
	 * @param errorCode
	 *            绑定接口返回值，0 - 成功
	 * @param appid
	 *            应用id。errorCode非0时为null
	 * @param userId
	 *            应用user id。errorCode非0时为null
	 * @param channelId
	 *            应用channel id。errorCode非0时为null
	 * @param requestId
	 *            向服务端发起的请求id。在追查问题时有用；
	 * @return none
	 */
	@Override
	public void onBind(Context context, int errorCode, String appid,
			String userId, String channelId, String requestId) {
		String responseString = "onBind errorCode=" + errorCode + " appid="
				+ appid + " userId=" + userId + " channelId=" + channelId
				+ " requestId=" + requestId; 
		LogUtil.d(responseString);

		if (errorCode == 0) {
			// 绑定成功
			new SJECHttpHandler(new AsyncHttpCallBack() {
				@Override
				public void onSuccess(String response) {

					super.onSuccess(response);

					LogUtil.d("%s", response);
				}

			}, context).UpdatePushInfo(userId, channelId, "");
		}
	}

	/**
	 * 接收透传消息的函数。
	 * 
	 * @param context
	 *            上下文
	 * @param message
	 *            推送的消息
	 * @param customContentString
	 *            自定义内容,为空或者json字符串
	 */
	@Override
	public void onMessage(Context context, String message,
			String customContentString) {
		String messageString = "透传消息 message=\"" + message
				+ "\" customContentString=" + customContentString;
		Log.d(TAG, messageString);

		// 自定义内容获取方式，mykey和myvalue对应透传消息推送时自定义内容中设置的键和值
		// if (!TextUtils.isEmpty(customContentString)) {
		// JSONObject customJson = null;
		// try {
		// customJson = new JSONObject(customContentString);
		// String myvalue = null;
		// if (!customJson.isNull("mykey")) {
		// myvalue = customJson.getString("mykey");
		// }
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		message = message.replaceAll("#", "\"");
		String title = "";
		String content = "";
		String innerID = "";
		String type = "";// 1:抢修,2:维保,3:提示审核,4:提示审核结果,5:协助抢修
		// {"title":"电梯抢修单","content":"苏州火车站,南地下出口东北1组31台,08HJJ9043电梯出现故障75,,请立刻派人进行抢修","type":"1","innerid":"01fbd79c-5546-49ef-bb6a-a748dc946cb5"}
		// message =
		// "{\"title\":\"电梯抢修单\",\"content\":\"苏州火车站,南地下出口东北1组31台,08HJJ9043电梯出现故障75,,请立刻派人进行抢修\",\"type\":\"1\",\"innerid\":\"01fbd79c-5546-49ef-bb6a-a748dc946cb5\"}";
		try {
 
			JSONObject obj = new JSONObject(message);
			title = obj.optString("title", "");
			content = obj.optString("content", "");
			innerID = obj.optString("innerid", "");
			type = obj.optString("type", "");

			// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
			ActivityManager am = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningTaskInfo> list = am.getRunningTasks(1);
			boolean isAppRunning = false;

			String MY_PKG_NAME = "";
			PackageManager pm = context.getPackageManager();
			try {
				PackageInfo packageInfo = pm.getPackageInfo(
						context.getPackageName(), 0);
				MY_PKG_NAME = packageInfo.packageName;
				LogUtil.d("packageName:%s", MY_PKG_NAME);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (list.size() > 0) {
				for (RunningTaskInfo info : list) {
					if (info.topActivity.getPackageName().equals(MY_PKG_NAME)
							|| info.baseActivity.getPackageName().equals(
									MY_PKG_NAME)) {
						isAppRunning = true;
						LogUtil.d("%s", "运行中");
						break;
					}
				}
			}
			Intent i1 = new Intent(context, ActivityMain.class);
			Intent i2 = null;
			if (type.equals("1")) {
				i2 = new Intent(context, ActivityDoRepairV2.class);
			} else if (type.equals("2")) {
				// i2 = new Intent(context, ActivityDoMaintainV2.class);
			} else if (type.equals("3")) {
				i2 = new Intent(context, ActivityConfirmList.class);
			} else if (type.equals("5")) {
				i2 = new Intent(context, ActivityDoRepairV2.class);
			}

			i1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			PendingIntent pi = null;

			if (i2 != null) {
				i2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				Bundle b = new Bundle();
				b.putString("workorder_id", innerID);
				// b.putInt("order_type", ConvertUtil.ParsetStringToInt32(type,
				// Constant.EnumWorkorderType.TYPE_WORKORDER_2));
				i2.putExtras(b);
			}
			if (isAppRunning) {
				if (type.equals("2")) {
					pi = PendingIntent.getActivity(context,
							(int) (Math.random() * 1000), i1,
							PendingIntent.FLAG_CANCEL_CURRENT);
				} else {
					pi = PendingIntent.getActivity(context,
							(int) (Math.random() * 1000), i2,
							PendingIntent.FLAG_CANCEL_CURRENT);
				}
			} else {
				if (type.equals("2")) {
					pi = PendingIntent.getActivity(context,
							(int) (Math.random() * 1000), i1,
							PendingIntent.FLAG_CANCEL_CURRENT);
				} else {
					pi = PendingIntent.getActivities(context,
							(int) (Math.random() * 1000),
							new Intent[] { i1, i2 },
							PendingIntent.FLAG_CANCEL_CURRENT);
				}
			}

			Notification notify = new Notification();
			notify.icon = R.drawable.ic_launcher;
			// notify.flags = Notification.FLAG_AUTO_CANCEL
			// | Notification.FLAG_NO_CLEAR;
			notify.flags = Notification.FLAG_AUTO_CANCEL;
			notify.tickerText = content;
			notify.when = System.currentTimeMillis();
			notify.defaults = Notification.DEFAULT_ALL;
			notify.setLatestEventInfo(context, title, content, pi);

			// String click = "点击查看";

			NotificationManager notificationManager = (NotificationManager) context
					.getSystemService(context.NOTIFICATION_SERVICE);
			notificationManager.notify(NOTIFICATION_ID++, notify);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 接收通知点击的函数。
	 * 
	 * @param context
	 *            上下文
	 * @param title
	 *            推送的通知的标题
	 * @param description
	 *            推送的通知的描述
	 * @param customContentString
	 *            自定义内容，为空或者json字符串
	 */
	@Override
	public void onNotificationClicked(Context context, String title,
			String description, String customContentString) {
		String notifyString = "通知点击 title=\"" + title + "\" description=\""
				+ description + "\" customContent=" + customContentString;
		Log.d(TAG, notifyString);

		// 自定义内容获取方式，mykey和myvalue对应通知推送时自定义内容中设置的键和值
		if (!TextUtils.isEmpty(customContentString)) {
			JSONObject customJson = null;
			try {
				customJson = new JSONObject(customContentString);
				String myvalue = null;
				if (!customJson.isNull("mykey")) {
					myvalue = customJson.getString("mykey");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
	}

	// /**
	// * 接收通知到达的函数。
	// *
	// * @param context
	// * 上下文
	// * @param title
	// * 推送的通知的标题
	// * @param description
	// * 推送的通知的描述
	// * @param customContentString
	// * 自定义内容，为空或者json字符串
	// */
	//
	// @Override
	// public void onNotificationArrived(Context context, String title,
	// String description, String customContentString) {
	//
	// String notifyString = "onNotificationArrived  title=\"" + title
	// + "\" description=\"" + description + "\" customContent="
	// + customContentString;
	// Log.d(TAG, notifyString);
	//
	// // 自定义内容获取方式，mykey和myvalue对应通知推送时自定义内容中设置的键和值
	// if (!TextUtils.isEmpty(customContentString)) {
	// JSONObject customJson = null;
	// try {
	// customJson = new JSONObject(customContentString);
	// String myvalue = null;
	// if (!customJson.isNull("mykey")) {
	// myvalue = customJson.getString("mykey");
	// }
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
	// // 你可以參考 onNotificationClicked中的提示从自定义内容获取具体值
	// }

	/**
	 * setTags() 的回调函数。
	 * 
	 * @param context
	 *            上下文
	 * @param errorCode
	 *            错误码。0表示某些tag已经设置成功；非0表示所有tag的设置均失败。
	 * @param successTags
	 *            设置成功的tag
	 * @param failTags
	 *            设置失败的tag
	 * @param requestId
	 *            分配给对云推送的请求的id
	 */
	@Override
	public void onSetTags(Context context, int errorCode,
			List<String> sucessTags, List<String> failTags, String requestId) {
		String responseString = "onSetTags errorCode=" + errorCode
				+ " sucessTags=" + sucessTags + " failTags=" + failTags
				+ " requestId=" + requestId;
		Log.d(TAG, responseString);

		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
	}

	/**
	 * delTags() 的回调函数。
	 * 
	 * @param context
	 *            上下文
	 * @param errorCode
	 *            错误码。0表示某些tag已经删除成功；非0表示所有tag均删除失败。
	 * @param successTags
	 *            成功删除的tag
	 * @param failTags
	 *            删除失败的tag
	 * @param requestId
	 *            分配给对云推送的请求的id
	 */
	@Override
	public void onDelTags(Context context, int errorCode,
			List<String> sucessTags, List<String> failTags, String requestId) {
		String responseString = "onDelTags errorCode=" + errorCode
				+ " sucessTags=" + sucessTags + " failTags=" + failTags
				+ " requestId=" + requestId;
		Log.d(TAG, responseString);

		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
	}

	/**
	 * listTags() 的回调函数。
	 * 
	 * @param context
	 *            上下文
	 * @param errorCode
	 *            错误码。0表示列举tag成功；非0表示失败。
	 * @param tags
	 *            当前应用设置的所有tag。
	 * @param requestId
	 *            分配给对云推送的请求的id
	 */
	@Override
	public void onListTags(Context context, int errorCode, List<String> tags,
			String requestId) {
		String responseString = "onListTags errorCode=" + errorCode + " tags="
				+ tags;
		Log.d(TAG, responseString);

		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
	}

	/**
	 * PushManager.stopWork() 的回调函数。
	 * 
	 * @param context
	 *            上下文
	 * @param errorCode
	 *            错误码。0表示从云推送解绑定成功；非0表示失败。
	 * @param requestId
	 *            分配给对云推送的请求的id
	 */
	@Override
	public void onUnbind(Context context, int errorCode, String requestId) {
		String responseString = "onUnbind errorCode=" + errorCode
				+ " requestId = " + requestId;
		Log.d(TAG, responseString);

		if (errorCode == 0) {
			// 解绑定成功

		}
		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
	}

	@Override
	public void onNotificationArrived(Context arg0, String arg1, String arg2,
			String arg3) {
		// TODO Auto-generated method stub

	}

}
