package com.sjec.rcms.activity.main;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.mobstat.StatService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;
import com.sjec.rcms.R;
import com.sjec.rcms.activity.BaseActivity;
import com.sjec.rcms.activity.device.ActivityCollectInfo;
import com.sjec.rcms.activity.fragment.FragmentDeviceList;
import com.sjec.rcms.activity.fragment.FragmentMyInfo;
import com.sjec.rcms.activity.fragment.FragmentWorkorderList;
import com.sjec.rcms.activity.fragment.FragmentKnowledge;
import com.sjec.rcms.activity.login.ActivityLogin;
import com.sjec.rcms.baidu.BaiduTJUtil;
import com.sjec.rcms.baidu.MyApplication;
import com.sjec.rcms.dao.EntityKnowledgeType;
import com.sjec.rcms.dao.EntityVillage;
import com.sjec.rcms.dao.EntityWorkorder;
import com.sjec.rcms.dao.EntityWorkorderDao;
import com.sjec.rcms.data.Constant;
import com.sjec.rcms.data.GlobalData;
import com.sjec.rcms.http.AsyncHttpCallBack;
import com.sjec.rcms.http.SJECHttpHandler;
import com.sjec.rcms.service.ServiceSJEC;
import com.sjec.rcms.util.UpdateUtil;
import com.websharputil.common.AppData;
import com.websharputil.common.LogUtil;
import com.websharputil.common.PrefUtil;
import com.websharputil.common.Util;
import com.websharputil.json.JSONUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.TransactionTooLargeException;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityMainV2 extends SlidingActivity implements
		View.OnClickListener {

	private TextView tv_collect_info;
	private TextView tv_device_service;
	private TextView tv_knowledget;
	private TextView tv_myinfo;
	private SlidingMenu leftMenuKnowledgeType;
	private ListView lv_knowledge_type;

	public int curKnwoledgeType = 0;

	/**
	 * fragment
	 */
	public FragmentManager fm;
	public Fragment curFragment;
	public FragmentTransaction fTrans;
	public FragmentDeviceList fragment_device_list;
	public FragmentKnowledge fragment_knowledge;
	public FragmentWorkorderList fragment_workorder;
	public FragmentMyInfo fragment_myinfo;

	private AdapterKnowledgeType adapterKnowledgeType = new AdapterKnowledgeType(
			GlobalData.listKnowledgeType);
	private ImageView iv_confirm_count;

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (Constant.ACTION_REFRESH_WORKORDER_LIST.equals(intent
					.getAction())) {
				if (fragment_workorder != null
						&& fragment_workorder.isVisible()) {
					fragment_workorder.refreshWorkorder();
				}
			} else if (Constant.ACTION_REFRESH_DEVICE_LIST_ATTR.equals(intent
					.getAction())) {
				fragment_device_list.updateDeviceListAttr();
			} else if (intent.getAction().equals(
					DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
				long downloadID = intent.getLongExtra(
						DownloadManager.EXTRA_DOWNLOAD_ID, 0);
				validDownloadStatus(downloadID);
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		//获取全局的daoSession然后通过daoSession获取mydataDao
	
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		BaiduTJUtil.initBaiduStatstics(this);

		setBehindContentView(R.layout.activity_main_rightmenu);
		init();
		bindData();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	public void init() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.ACTION_REFRESH_WORKORDER_LIST);
		filter.addAction(Constant.ACTION_REFRESH_DEVICE_LIST_ATTR);
		filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		registerReceiver(receiver, filter);
		// fm = getFragmentManager();
		fm = getSupportFragmentManager();
		tv_collect_info = (TextView) findViewById(R.id.tv_collect_info);
		tv_device_service = (TextView) findViewById(R.id.tv_device_service);
		tv_knowledget = (TextView) findViewById(R.id.tv_knowledge);
		lv_knowledge_type = (ListView) findViewById(R.id.lv_knowledge_type);
		tv_myinfo = (TextView) findViewById(R.id.tv_myinfo);
		iv_confirm_count = (ImageView) findViewById(R.id.iv_confirm_count);

		fragment_device_list = new FragmentDeviceList();
		fragment_workorder = new FragmentWorkorderList();
		fragment_knowledge = new FragmentKnowledge();
		fragment_myinfo = new FragmentMyInfo();

		tv_collect_info.setOnClickListener(this);
		tv_device_service.setOnClickListener(this);
		tv_knowledget.setOnClickListener(this);
		tv_myinfo.setOnClickListener(this);
		initSlidingMenu();
		// 初始化百度推送
		PushManager.startWork(ActivityMainV2.this,
				PushConstants.LOGIN_TYPE_API_KEY,
				Util.getMetaValue(ActivityMainV2.this, "baidu_push_api_key"));
		initGpsConfig();
	}

	private void initGpsConfig() {

		LocationManager locationManager = (LocationManager) ActivityMainV2.this
				.getSystemService(Context.LOCATION_SERVICE);
		boolean isOpen = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (!isOpen) {
			// 打开定位设置的界面
			Util.createToast(ActivityMainV2.this, R.string.common_config_lbs,
					5000).show();
			Intent intent = new Intent();
			intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			// intent.setAction(Settings.ACTION_APPLICATION_SETTINGS);

			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			try {
				startActivity(intent);
			} catch (ActivityNotFoundException ex) {

				intent.setAction(Settings.ACTION_SETTINGS);
				try {
					startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void initSlidingMenu() {
		leftMenuKnowledgeType = getSlidingMenu();
		leftMenuKnowledgeType.setSlidingEnabled(false);
		leftMenuKnowledgeType.setMode(SlidingMenu.RIGHT);
		// 设置触摸屏幕的模式
		leftMenuKnowledgeType.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		leftMenuKnowledgeType.setShadowWidthRes(R.dimen.shadow_width);
		leftMenuKnowledgeType.setShadowDrawable(R.drawable.shadow);
		// 设置滑动菜单视图的宽度
		leftMenuKnowledgeType.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 设置渐入渐出效果的值
		leftMenuKnowledgeType.setFadeDegree(0.35f);

		lv_knowledge_type
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						curKnwoledgeType = position;

						adapterKnowledgeType.notifyDataSetChanged();
						fragment_knowledge.wv_knowledge.loadUrl(String
								.format(SJECHttpHandler.URL_KNOWLEDGE_LIST,
										GlobalData.listKnowledgeType
												.get(position).InnerID,
										GlobalData.listKnowledgeType
												.get(position).Tag.trim(), ""));
						leftMenuKnowledgeType.showContent();
					}
				});
	}

	public void ShowRightMenu() {
		leftMenuKnowledgeType.setSlidingEnabled(true);
		leftMenuKnowledgeType.showMenu();
	}

	public void bindData() {
		tv_device_service.performClick();
		lv_knowledge_type.setAdapter(adapterKnowledgeType);
		new SJECHttpHandler(cbConfirmCount, this)
				.getPauseConfirmListCountByUserID();
		new SJECHttpHandler(cbKnowledge, this).getKnowledgeType();
		checkUpdateApk(this);
	}

	AsyncHttpCallBack cbConfirmCount = new AsyncHttpCallBack() {

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

	AsyncHttpCallBack cbKnowledge = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {
			super.onSuccess(response);
			LogUtil.d("%s", response);
			try {

				JSONObject jobj = new JSONObject(response);
				if (jobj.optString("result", "false").equals("true")) {
					GlobalData.listKnowledgeType = JSONUtils.fromJson(jobj
							.optJSONArray("data").toString(),
							new TypeToken<ArrayList<EntityKnowledgeType>>() {
							});
					adapterKnowledgeType.mList = GlobalData.listKnowledgeType;
					adapterKnowledgeType.notifyDataSetChanged();
					if (GlobalData.listKnowledgeType.size() > 0
							&& curFragment == fragment_knowledge) {
						fragment_knowledge.wv_knowledge
								.loadUrl(SJECHttpHandler.URL_KNOWLEDGE_LIST
										+ "classID="
										+ GlobalData.listKnowledgeType.get(0).InnerID
										+ "&tag="
										+ GlobalData.listKnowledgeType.get(0).Tag
												.trim());
					}
				} else {

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

	public void switchContent(Fragment from, Fragment to) {
		switchContent(from, to, false);

	}

	public void switchContent(Fragment from, Fragment to, boolean isRemoveSelf) {
		if (from instanceof FragmentMyInfo) {
			isRemoveSelf = true;
		}
		if (curFragment != to) {
			curFragment = to;
			FragmentTransaction transaction = fm.beginTransaction();
			if (!to.isAdded()) { // 先判断是否被add过
				if (from == null) {
					transaction.add(R.id.layout_content, to)
							.commitAllowingStateLoss();
				} else {

					transaction.hide(from).add(R.id.layout_content, to)
							.commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到Activity中
				}

			} else {
				if (from == null) {
					transaction.show(to).commitAllowingStateLoss();
				} else {
					transaction.hide(from).show(to).commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
				}
			}

			if (isRemoveSelf) {
				transaction.remove(from);
			}

		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_collect_info:
			leftMenuKnowledgeType.setSlidingEnabled(false);
			switchContent(curFragment, fragment_device_list);
			ChangeBottomStyle(v);
			break;
		case R.id.tv_device_service:
			leftMenuKnowledgeType.setSlidingEnabled(false);
			switchContent(curFragment, fragment_workorder);
			ChangeBottomStyle(v);
			break;
		case R.id.tv_knowledge:
			leftMenuKnowledgeType.setSlidingEnabled(true);
			switchContent(curFragment, fragment_knowledge);
			ChangeBottomStyle(v);
			break;

		case R.id.tv_myinfo:
			leftMenuKnowledgeType.setSlidingEnabled(false);
			switchContent(curFragment, fragment_myinfo);
			ChangeBottomStyle(v);
			// Dialog dialog = new AlertDialog.Builder(ActivityMain.this)
			// .setTitle(R.string.msg_dialog_add_order_type_title)
			// .setMessage(R.string.msg_confirm_cancel)
			// .setPositiveButton(R.string.msg_dialog_confirm,
			// new DialogInterface.OnClickListener() {
			// @Override
			// public void onClick(DialogInterface dialog,
			// int which) {
			// dialog.dismiss();
			// PrefUtil.getEditor(ActivityMain.this)
			// .remove("user").commit();
			// new SJECHttpHandler(
			// new AsyncHttpCallBack() {
			// @Override
			// public void onSuccess(
			// String response) {
			//
			// super.onSuccess(response);
			// LogUtil.d("%s", response);
			// }
			//
			// }, ActivityMain.this)
			// .UpdatePushInfo(
			// GlobalData.curUser.InnerID,
			// "", "", "");
			// PushManager.stopWork(ActivityMain.this);
			// GlobalData.clear();
			// Util.startActivity(ActivityMain.this,
			// ActivityLogin.class, true);
			// }
			// })
			// .setNegativeButton(R.string.msg_dialog_cancel,
			// new DialogInterface.OnClickListener() {
			// public void onClick(DialogInterface dialog,
			// int whichButton) {
			// dialog.dismiss();
			// }
			// }).create();
			// dialog.setCanceledOnTouchOutside(false);
			// dialog.show();

			break;
		}
	}

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK) {
	// //exit();
	// return false;
	// } else {
	// return super.onKeyDown(keyCode, event);
	// }
	//
	// }

	boolean isExit = false;
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			isExit = false;
		}
	};

	public void exit() {
		if (!isExit) {
			isExit = true;
			Toast.makeText(getApplicationContext(),
					R.string.common_exit_repeat_press, Toast.LENGTH_SHORT)
					.show();
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			getApplicationContext().sendBroadcast(new Intent("finish"));
			GlobalData.clear();
			Util.finishActivity(this);
		}
	}

	/**
	 * 改变底下三个按钮的样式
	 * 
	 * @param v
	 */
	private void ChangeBottomStyle(View v) {
		tv_collect_info.setSelected(false);
		tv_device_service.setSelected(false);
		tv_knowledget.setSelected(false);
		tv_myinfo.setSelected(false);
		v.setSelected(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class ViewHolder {
		private TextView tv_knowledge_type;
	}

	class AdapterKnowledgeType extends BaseAdapter {
		private LayoutInflater mInflater;
		private ArrayList<EntityKnowledgeType> mList;

		public AdapterKnowledgeType(ArrayList<EntityKnowledgeType> list) {
			this.mList = list;
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (getCount() == 0)
				return null;
			ViewHolder holderNotice = null;

			try {
				if (mInflater == null) {
					mInflater = LayoutInflater.from(ActivityMainV2.this);
				}
				if (convertView == null) {
					convertView = mInflater.inflate(
							R.layout.item_knowledge_type, null);

					holderNotice = new ViewHolder();
					holderNotice.tv_knowledge_type = (TextView) convertView
							.findViewById(R.id.tv_knowledge_type);
					convertView.setTag(holderNotice);
				} else {
					holderNotice = (ViewHolder) convertView.getTag();
				}

				holderNotice.tv_knowledge_type
						.setText(mList.get(position).ClassName);
				if (position == curKnwoledgeType) {
					holderNotice.tv_knowledge_type
							.setBackgroundResource(R.drawable.cecece);
				} else {
					holderNotice.tv_knowledge_type
							.setBackgroundDrawable(getResources().getDrawable(
									R.drawable.selector_layout_knowledge_type));
				}
				return convertView;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

		}
	}

	public static boolean IS_NEED_UPDATE = false;
	public static String URL_NEW_VERSION = "";

	DownloadManager downloadmanager = null;

	public void checkUpdateApk(Context context) {
		new SJECHttpHandler(cb, context).CheckVersion();
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
					JSONObject json = obj.getJSONObject("data");
					String versionName = json.optString("VersionName");
					IS_NEED_UPDATE = !versionName.equals(AppData.VERSION_NAME);
					URL_NEW_VERSION = json.optString("DownloadUrl");
					if (IS_NEED_UPDATE) {

						showUpdateApkDialog(ActivityMainV2.this,
								Constant.DOWNLOAD_APK_NAME, URL_NEW_VERSION);
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

	public void showUpdateApkDialog(final Context context,
			final String savePath, final String url_download) {

		StringBuffer sb = new StringBuffer();
		sb.append(context.getString(R.string.welcome_dialog_version_msg));
		Dialog dialog = new AlertDialog.Builder(context)
				.setTitle(R.string.welcome_dialog_version_title)
				.setMessage(sb.toString())
				.setPositiveButton(R.string.welcome_dialog_version_confirm,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								startDownloadApk(context, savePath,
										url_download);
							}
						})
				.setNegativeButton(R.string.welcome_dialog_version_cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.dismiss();
							}
						}).create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		// }
	}

	public long startDownloadApk(Context context, String savePath,
			String url_download) {
		if (!new File(Constant.SAVE_PATH).exists()) {
			new File(Constant.SAVE_PATH).mkdirs();
		}
		if (new File(savePath).exists()) {
			new File(savePath).delete();
		}
		downloadmanager = (DownloadManager) context
				.getSystemService(DOWNLOAD_SERVICE);
		Request req = new DownloadManager.Request(Uri.parse(url_download));
		req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
				| DownloadManager.Request.NETWORK_WIFI);
		req.setAllowedOverRoaming(false);
		req.setNotificationVisibility(Request.VISIBILITY_VISIBLE);
		req.setDestinationUri(Uri.fromFile(new File(savePath)));
		long tmp = downloadmanager.enqueue(req);
		return tmp;
	}

	/**
	 * 监听是否下载apk成功,这个方法放在receiver中用来监听 android.intent.action.DOWNLOAD_COMPLETE
	 * android.intent.action.DOWNLOAD_NOTIFICATION_CLICKED
	 * 
	 * @param downloadmanager
	 * @param downloadID
	 * @param context
	 * @param apkPath
	 */
	public void validDownloadStatus(long downloadID) {
		DownloadManager.Query query = new DownloadManager.Query();
		query.setFilterById(downloadID);
		Cursor c = downloadmanager.query(query);
		int status = -1;
		if (c != null && c.moveToFirst()) {
			status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
		}
		c.close();
		if (status == 8) {
			LogUtil.d("%s", "下载成功");
			// 下载成功,调用系统的安装程序
			updateAndInstall();
		}
	}

	public BroadcastReceiver receiverDownload = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 这里可以取得下载的id，这样就可以知道哪个文件下载完成了。适用与多个下载任务的监听

			// Util.LogE("最终下载状态:" + status);
			// Util.LogE(DownloadManager.STATUS_SUCCESSFUL + "");
			// updateAndInstall();
		}
	};

	/**
	 * 安装/覆盖软件
	 * 
	 * @param context
	 * @param apkPath
	 */
	public void updateAndInstall() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(
				Uri.fromFile(new File(Constant.DOWNLOAD_APK_NAME)),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		StatService.onPause(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		StatService.onResume(this);
	}

}
