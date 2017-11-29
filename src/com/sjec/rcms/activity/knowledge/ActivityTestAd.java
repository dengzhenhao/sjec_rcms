package com.sjec.rcms.activity.knowledge;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.co.senab.photoview.sample.ViewPagerActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.HttpAuthHandler;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.RenderPriority;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;
import com.sjec.rcms.R;
import com.sjec.rcms.activity.BaseActivity;
import com.sjec.rcms.http.SJECHttpHandler;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;
import com.websharputil.file.FileUtil;
import com.websharputil.network.HttpUtil;

/**
 * 
 * @类名称：ActivityKnowledgeDetail
 * @包名：com.sjec.rcms.activity.knowledge
 * @描述： 知识库的详情页面,webview
 * @创建人： dengzh
 * @创建时间:2015-8-11 上午9:59:32
 * @版本 V1.0
 * @Copyright (c) 2015 by 苏州威博世网络科技有限公司.
 */
public class ActivityTestAd extends BaseActivity {

	public WebView wv_knowledge;
	private LinearLayout layout_back;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_back:
			finish();
			break;
		}
	}

	@Override
	public void initLayout() {
		setContentView(R.layout.test_ad);
	}

	@Override
	public void init(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		wv_knowledge = (WebView) findViewById(R.id.wv);
		layout_back = (LinearLayout) findViewById(R.id.layout_back);

		initWebview();

	}

	@Override
	public void bindData() {

		// wv_knowledge.loadUrl("http://192.168.0.115:9797/testADa.aspx");
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 404) {// 主页不存在
				// 载入本地assets文件夹下面的错误提示页面404.html
				LogUtil.d("%s", "404");
			} else {
				wv_knowledge.loadUrl("http://192.168.0.115:9797/testAD.aspx");
			}
		}
	};

	private void initWebview() {

		WebSettings mWebSettings = wv_knowledge.getSettings();
		mWebSettings.setJavaScriptEnabled(true);
		mWebSettings.setLightTouchEnabled(true);
		mWebSettings.setDefaultTextEncodingName("gbk");
		mWebSettings.setSaveFormData(true);
		mWebSettings.setPluginState(PluginState.ON);
		mWebSettings.setRenderPriority(RenderPriority.HIGH);
		mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		mWebSettings.setDomStorageEnabled(true);
		mWebSettings.setAppCacheMaxSize(1024 * 1024 * 8);
		String appCacheDir = getApplicationContext().getDir("cache",
				Context.MODE_PRIVATE).getPath();
		mWebSettings.setAppCachePath(appCacheDir);
		mWebSettings.setAllowFileAccess(true);
		mWebSettings.setAppCacheEnabled(true);
		mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		WindowManager wm = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);

		int width = wm.getDefaultDisplay().getWidth();
		if (width <= 480) {
			mWebSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
		}
		// wv.setScrollBarStyle(0);
		wv_knowledge.setHapticFeedbackEnabled(false);
		wv_knowledge.setDrawingCacheEnabled(true);
		wv_knowledge.getSettings().setAppCacheEnabled(true);

		wv_knowledge.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK
							&& wv_knowledge.canGoBack()) {

						wv_knowledge.goBack(); // 后退

						// webview.goForward();//前进
						return true; // 已处理
					}
				}
				return false;
			}
		});

		wv_knowledge.addJavascriptInterface(new WebAppInterface(), "Android");
		wv_knowledge.removeJavascriptInterface("searchBoxJavaBridge_");

		wv_knowledge.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				LogUtil.d("%s", "加载完成");

//				Animation anim = AnimationUtils.loadAnimation(
//						ActivityTestAd.this, R.anim.fade_in);
//				anim.setFillAfter(true);
//				wv_knowledge.startAnimation(anim);
				wv_knowledge.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				LogUtil.d("%s", "开始加载");
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// super.onReceivedError(view, errorCode, description,
				// failingUrl);
				LogUtil.d("%s", "加载错误");
			}
		});

		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				// 此处判断主页是否存在，因为主页是通过loadUrl加载的，
				// 此时不会执行shouldOverrideUrlLoading进行页面是否存在的判断
				// 进入主页后，点主页里面的链接，链接到其他页面就一定会执行shouldOverrideUrlLoading方法了
				if (getRespStatus("http://192.168.0.115:9797/testAD.aspx") != 200) {
					msg.what = 404;
				}
				handler.sendMessage(msg);
			}
		}).start();

	}

	private int getRespStatus(String url) {
		int status = -1;
		try {
			HttpHead head = new HttpHead(url);
			HttpClient client = new DefaultHttpClient();
			HttpResponse resp = client.execute(head);
			status = resp.getStatusLine().getStatusCode();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return status;
	}

	class WebAppInterface {

		@JavascriptInterface
		public String toString() {
			return "injectedObject";
		}

		@JavascriptInterface
		public void OpenUrl(String params) {
			String[] result = params.split("\\|\\|");
			String content_title = result[0];
			String content_url = result[1];
			LogUtil.d("%s", content_url);
			if (content_title.equals("")) {
				content_title = "";
			}
			wv_knowledge.loadUrl(content_url);

		}

		@JavascriptInterface
		public void OpenResource(String param) {
			LogUtil.d("%s", param);

			String[] arr = param.trim().split("\\|\\|");
			if (arr[0].trim().equals("pic")) {
				String index = "0";
				String content = arr[1];
				String[] arrContent = content.split("@@@@");
				index = arrContent[0];
				Bundle b = new Bundle();
				JSONArray jsonarr = new JSONArray();

				try {
					for (int i = 0; i < arrContent.length; i++) {
						if (i != 0) {
							JSONObject obj = new JSONObject();
							String url = SJECHttpHandler.BASE_URL
									+ arrContent[i].toString();
							String name = FileUtil.GetFileNameFromUrl(url);
							url = url.substring(0, url.lastIndexOf("/") + 1)
									+ URLEncoder.encode(name);
							obj.put("pic", url);
							jsonarr.put(obj);
						}
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				b.putString("index", index);
				b.putString("pics", jsonarr.toString());
				Util.startActivity(ActivityTestAd.this,
						ViewPagerActivity.class, b, false);
			}
		}
	}

}
