package com.sjec.rcms.activity.fragment;

import java.net.URLEncoder;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.extras.PullToRefreshWebView2;
import com.sjec.rcms.R;
import com.sjec.rcms.activity.BaseFragment;
import com.sjec.rcms.activity.knowledge.ActivityKnowledgeDetail;
import com.sjec.rcms.activity.main.ActivityMain;
import com.sjec.rcms.data.GlobalData;
import com.sjec.rcms.http.SJECHttpHandler;
import com.websharputil.common.LogUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.LinearGradient;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class FragmentKnowledge extends BaseFragment implements
		View.OnClickListener {

	private ActivityMain objActivityMain;
	private PullToRefreshWebView pr_wv_knowledge;
	public WebView wv_knowledge;
	private ImageButton btn_knowledge;
	public EditText et_search_keyword;
	private Button btn_search;

	private LinearLayout loading;
	public String typeID = "";
	public String tag = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_knowledge, container,
				false);
		init(view);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		objActivityMain = (ActivityMain) getActivity();
		
		if (GlobalData.listKnowledgeType.size() > 0
				&& wv_knowledge.getUrl() == null) {
			// if (!pr_wv_knowledge.isRefreshing()) {
			// wv_knowledge.loadUrl(String.format(
			// SJECHttpHandler.URL_KNOWLEDGE_LIST,
			// GlobalData.listKnowledgeType.get(0).InnerID,
			// GlobalData.listKnowledgeType.get(0).Tag.trim()));
			// }
		}
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	private void init(View view) {
		et_search_keyword = (EditText) view
				.findViewById(R.id.et_search_keyword);
		btn_search = (Button) view.findViewById(R.id.btn_search);
		btn_knowledge = (ImageButton) view.findViewById(R.id.btn_knowledge);
		pr_wv_knowledge = (PullToRefreshWebView) view
				.findViewById(R.id.pr_wv_knowledge);
		wv_knowledge = pr_wv_knowledge.getRefreshableView();
		wv_knowledge.setBackgroundColor(getResources().getColor(
				android.R.color.transparent));
		loading = (LinearLayout) view.findViewById(R.id.loading);
		initWebview();
		//pr_wv_knowledge.setMode(Mode.BOTH);
//		pr_wv_knowledge
//				.setOnPullEventListener(new PullToRefreshBase.OnPullEventListener<WebView>() {
//					@Override
//					public void onPullEvent(
//							PullToRefreshBase<WebView> refreshView,
//						State state, Mode direction) {
//						// LogUtil. (state+","+direction+",");
//						LogUtil.d("%s", state + "," + direction + ",");
//						
//
//					}
//				});
		
		pr_wv_knowledge.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				LogUtil.d("%s", "onRefreshwebview");
				wv_knowledge.reload();
			}
			
			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				LogUtil.d("%s", "onRefreshwebview");
				//wv_knowledge.reload();
			}
		});
//		pr_wv_knowledge
//				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
//
//					@Override
//					public void onRefresh(PullToRefreshBase refreshView) {
//						LogUtil.d("%s", "onRefreshwebview");
//						wv_knowledge.reload();
//					}
//				});

		btn_knowledge.setOnClickListener(this);
		btn_search.setOnClickListener(this);
		wv_knowledge.loadUrl(String.format(SJECHttpHandler.URL_KNOWLEDGE_LIST,
				typeID, tag, URLEncoder.encode(et_search_keyword.getText()
						.toString().trim())));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_knowledge:
			objActivityMain.ShowRightMenu();
			break;
		case R.id.btn_search:
			wv_knowledge.loadUrl(String.format(
					SJECHttpHandler.URL_KNOWLEDGE_LIST,
					typeID,
					tag,
					URLEncoder.encode(et_search_keyword.getText().toString()
							.trim())));
			break;
		}
	}

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
		String appCacheDir = FragmentKnowledge.this.getActivity()
				.getApplicationContext().getDir("cache", Context.MODE_PRIVATE)
				.getPath();
		mWebSettings.setAppCachePath(appCacheDir);
		mWebSettings.setAllowFileAccess(true);
		mWebSettings.setAppCacheEnabled(true);
		mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		WindowManager wm = (WindowManager) FragmentKnowledge.this.getActivity()
				.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		// if (width <= 480) {
		mWebSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
		// }
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

		wv_knowledge.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				LogUtil.d("%s", "shouldOverrideUrlLoading");
				return super.shouldOverrideUrlLoading(view, url);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				pr_wv_knowledge.onRefreshComplete();
				// loading.setVisibility(View.GONE);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				LogUtil.d("%s", "onPageStarted");
				LogUtil.d("url: %s", String.format(
						SJECHttpHandler.URL_KNOWLEDGE_LIST,
						typeID,
						tag,
						URLEncoder.encode(et_search_keyword.getText()
								.toString().trim())));
				pr_wv_knowledge.setRefreshing(true);
				// loading.setVisibility(View.VISIBLE);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
			}
		});

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
			// wv_knowledge.loadUrl(content_url);
			Intent intent = new Intent();
			intent.setClass(FragmentKnowledge.this.getActivity(),
					ActivityKnowledgeDetail.class);
			Bundle bundle = new Bundle();
			bundle.putString("url", content_url);
			bundle.putString("title", content_title);
			intent.putExtras(bundle);
			startActivity(intent);

		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		wv_knowledge.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		wv_knowledge.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
