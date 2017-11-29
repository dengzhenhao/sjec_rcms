package com.sjec.rcms.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;

import com.baidu.mobstat.StatService;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.ResponseHandlerInterface;
import com.sjec.rcms.R;
import com.sjec.rcms.activity.login.ActivityLogin;
import com.sjec.rcms.activity.workorder.ActivityDoRepairV2;
import com.sjec.rcms.data.Constant;
import com.websharputil.common.AppData;
import com.websharputil.common.LogUtil;
import com.websharputil.common.PushUtils;
import com.websharputil.common.Util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @类名称：BaseActivity
 * @包名：com.websharp.mix.activity
 * @描述： TODO
 * @创建人： dengzh
 * @创建时间:2014-12-10 下午4:45:02
 * @版本 V1.0
 * @Copyright (c) 2014 by 苏州威博世网络科技有限公司.
 */
public abstract class BaseActivity extends Activity implements
		View.OnClickListener { 

	public static boolean isInitParam = false;

	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		try {
			// StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
			// .detectDiskReads().detectDiskWrites().detectNetwork()
			// .penaltyLog().build());
			// StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
			// .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
			// .penaltyLog().penaltyDeath().build());
		} catch (Exception e) {
		}

		super.onCreate(savedInstanceState);
		setLockPatternEnabled(false);
		if (!isInitParam) {
			initParam(this);
		}
		initLayout();
		init(savedInstanceState);
		bindData();
		// ActivityManager am = (ActivityManager) this
		// .getSystemService(Context.ACTIVITY_SERVICE);
		// List<RunningTaskInfo> list = am.getRunningTasks(100);
		// boolean isAppRunning = false;
		// String MY_PKG_NAME = "com.websharp.mix.activity";
		// for (RunningTaskInfo info : list) {
		// if (info.topActivity.getPackageName().equals(MY_PKG_NAME)
		// || info.baseActivity.getPackageName().equals(MY_PKG_NAME)) {
		// isAppRunning = true;
		// Util.LogD( info.topActivity.getPackageName()
		// + " info.baseActivity.getPackageName()="
		// + info.baseActivity.getClassName());
		// break;
		// }
		// }
	}

	public abstract void initLayout();

	public abstract void init(Bundle savedInstanceState);

	public abstract void bindData();

	public String getText(EditText et) {
		return et.getText().toString().trim();
	}

	public String getText(TextView tv) {
		return tv.getText().toString().trim();
	}

	public boolean isEmpty(EditText et) {
		return et.getText().toString().trim().isEmpty();
	}

	public void setLockPatternEnabled(boolean enabled) {
		setBoolean(android.provider.Settings.System.LOCK_PATTERN_ENABLED,
				enabled);
	}

	private void setBoolean(String systemSettingKey, boolean enabled) {
		android.provider.Settings.System.putInt(getContentResolver(),
				systemSettingKey, enabled ? 1 : 0);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onPause() {
		super.onPause();
		StatService.onPause(this);
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onResume() {
		super.onResume();
		StatService.onResume(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Util.finishActivity(this);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public static void initParam(Context context) {
		AppData.InitAppData(context, Constant.APP_NAME);
		isInitParam = true;
	}

	/**
	 * 收起软键盘并设置提示文字
	 */
	public void collapseSoftInputMethod(EditText et) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(et.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * 显示软键盘并设置提示文字
	 */
	public void showSoftInputMethod(EditText et) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
	}

	public boolean isExistShortCut() {
		boolean isInstallShortcut = false;
		final ContentResolver cr = BaseActivity.this.getContentResolver();
		final String AUTHORITY = "com.android.launcher.settings";
		final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
				+ "/favorites?notify=true");
		Cursor c = cr.query(CONTENT_URI,
				new String[] { "title", "iconResource" }, "title=?",
				new String[] { getString(R.string.app_name) }, null);
		if (c != null && c.getCount() > 0) {
			isInstallShortcut = true;
		}
		return isInstallShortcut;
	}

	/**
	 * 为程序创建桌面快捷方式
	 */
	public void createShortcut() {
		Intent shortcut = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				getString(R.string.app_name));
		shortcut.putExtra("duplicate", false);// 设置是否重复创建
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setClass(this, ActivityLogin.class);// 设置第一个页面
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
		ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(
				this, R.drawable.ic_launcher);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
		sendBroadcast(shortcut);
	}

	/**
	 * 删除程序的快捷方式
	 */
	public void delShortcut() {
		Intent shortcut = new Intent(
				"com.android.launcher.action.UNINSTALL_SHORTCUT");
		// 快捷方式的名称
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				getString(R.string.app_name));

		// 指定当前的Activity为快捷方式启动的对象: 如 com.everest.video.VideoPlayer
		// 注意: ComponentName的第二个参数必须是完整的类名（包名+类名），否则无法删除快捷方式
		String appClass = this.getPackageName() + "."
				+ this.getLocalClassName();
		ComponentName comp = new ComponentName(this.getPackageName(), appClass);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(
				Intent.ACTION_MAIN).setComponent(comp));
		sendBroadcast(shortcut);
	}

	public void ConfirmDialogCallUser(String name, final String telephone) {
		Builder dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setIcon(android.R.color.transparent);
		dialogBuilder.setCancelable(true);
		dialogBuilder.setNegativeButton(R.string.msg_dialog_cancel, null);
		dialogBuilder.setTitle(R.string.msg_dialog_add_order_type_title);

		dialogBuilder.setMessage(getResources().getString(
				R.string.msg_call_phone_confirm, name, telephone));
		dialogBuilder.setPositiveButton(R.string.msg_dialog_confirm,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 更新状态为开始
						LogUtil.d("%s", telephone);
						Intent intent = new Intent(Intent.ACTION_CALL, Uri
								.parse("tel:" + telephone));
						startActivity(intent);
					}
				});

		AlertDialog dialog = dialogBuilder.create();
		dialog.show();
	}

	// 进入工单界面这，弹出这个提示框，需要点确定后才能关闭
	public void ConfirmDialogControl(int titleResource, int msgResrouce,
			int btnResource, DialogInterface.OnClickListener clicker) {
		Builder dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setIcon(android.R.color.transparent);
		// dialogBuilder.setNegativeButton(R.string.msg_dialog_cancel, null);
		dialogBuilder.setTitle(titleResource);
		dialogBuilder.setMessage(msgResrouce);
		if (clicker == null) {
			dialogBuilder.setPositiveButton(btnResource,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
		} else {
			dialogBuilder.setPositiveButton(btnResource, clicker);
		}
		AlertDialog dialog = dialogBuilder.create();
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	public void collapseSoftInputMethod(Activity act) {

		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(
						act.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);

	}

}
