package com.sjec.rcms.activity.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mslibs.utils.JSONUtils;
import com.mslibs.utils.NotificationsUtil;
import com.mslibs.widget.CActivity;
import com.mslibs.widget.CListView;
import com.mslibs.widget.CListViewParam;
import com.sjec.rcms.R;
import com.sjec.rcms.activity.device.ActivityCollectInfo;
import com.sjec.rcms.activity.device.ActivityCollectInfoV2;
import com.sjec.rcms.activity.main.ActivityMain;
import com.sjec.rcms.dao.EntityDevice;
import com.sjec.rcms.data.Constant;
import com.sjec.rcms.data.GlobalData;
import com.sjec.rcms.http.AsyncHttpCallBack;
import com.sjec.rcms.http.AsyncHttpListCallBack;
import com.sjec.rcms.http.SJECHttpHandler;
import com.websharputil.common.ConvertUtil;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;
import com.websharputil.date.DateUtil;

/**
 * 
 * @类名称：AdapterDeviceList
 * @包名：com.sjec.rcms.activity.fragment
 * @描述： 设备列表
 * @创建人： dengzh
 * @创建时间:2015-8-5 下午3:13:08
 * @版本 V1.0
 * @Copyright (c) 2015 by 苏州威博世网络科技有限公司.
 */
public class AdapterDeviceList extends CListView {

	public String keyword = "";

	public AdapterDeviceList(PullToRefreshListView lv, Activity activity) {
		super(lv, activity);
		initListViewStart();
	}

	@Override
	public void initListItemResource() {
		setListItemResource(R.layout.item_list_device);
	}

	@Override
	public void ensureUi() {
		mPerpage = 10;

		super.setGetMoreResource(R.layout.item_list_getmore,
				R.id.list_item_getmore_title, "加载更多");
		super.ensureUi();
		mListViewPTR
				.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
					@Override
					public void onLastItemVisible() {
						if (actionType == IDLE && !mAdapter.isNotMore) {
							// 如果并没有在加载过程中,可以加载更多
							getmoreListViewStart();
						}
					}
				});
		// super.setGetMoreClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// getmoreListViewStart();
		// }
		// });

		super.setItemOnclickLinstener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 打开设备详情页
				int i = (Integer) v.getTag();
				LogUtil.d("%s", "点击" + i);
				GlobalData.curDevice = GlobalData.listDevice.get(i);
				Bundle b = new Bundle();
				b.putString("data", "");
				b.putInt("type", 1);
				Util.startActivity(mActivity, ActivityCollectInfoV2.class, b,
						false);
			}
		});
	}

	@Override
	public ArrayList<CListViewParam> matchListItem(Object obj, int index) {
		LogUtil.d("%s", index);
		final EntityDevice item = (EntityDevice) obj;
		ArrayList<CListViewParam> LVP = new ArrayList<CListViewParam>();

		LVP.add(new CListViewParam(R.id.tv_device_name, mActivity
				.getResources().getString(R.string.str_item_device_name,
						item.Village_Name, item.Village_Group_Num,
						item.Village_Stage_Num), true));
		LVP.add(new CListViewParam(R.id.tv_device_num, mActivity.getResources()
				.getString(R.string.str_item_device_num, item.Device_Num), true));
		LVP.add(new CListViewParam(R.id.tv_device_lastmaintain, "上次维保："
				+ item.LastMaintainTime));

		CListViewParam param = new CListViewParam(R.id.iv_maintain, null,true);
		String maintainTime = item.LastMaintainTime;
		if (maintainTime == null) {
			param.setItemBackgroundDrawable(mActivity.getResources()
					.getDrawable(R.drawable.icon_sbwx_02));
		} else {
			try {
				int days = DateUtil.daysBetween(new SimpleDateFormat(
						"yyyy-MM-dd").parse(maintainTime.replaceAll("/", "-")),
						new Date());
				LogUtil.d(days + "天");
				if (days >= 11 && days <= 14) {
					param.setItemBackgroundDrawable(mActivity.getResources()
							.getDrawable(R.drawable.icon_sbwx_yellow));
				} else if (days >= 15) {
					param.setItemBackgroundDrawable(mActivity.getResources()
							.getDrawable(R.drawable.icon_sbwx_red));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		LVP.add(param);

		if (item.Latitude.isEmpty()
				|| ConvertUtil.ParsetStringToDouble(item.Latitude, 0) == 0) {
			// 抢修
			LVP.add(new CListViewParam(R.id.iv_location, null, false));
		} else {
			// 维保
			LVP.add(new CListViewParam(R.id.iv_location, null, true));
		}
		// CListViewParam delLVP = new CListViewParam(R.id.btn_del, true);
		// delLVP.setOnclickLinstener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// System.err.println(item.id);
		// AsyncHttpCallBack collectCB = new AsyncHttpCallBack() {
		// @Override
		// public void onSuccess(String response) {
		// ((CActivity) mActivity).dismissProgress();
		//
		// NotificationsUtil.ToastBottomMsg(mActivity, "删除成功");
		// refreshListViewStart();
		// }
		//
		// @Override
		// public void onFailure(String message) {
		// super.onFailure(message);
		// ((CActivity) mActivity).dismissProgress();
		// NotificationsUtil.ToastBottomMsg(mActivity, "删除失败");
		// }
		// };
		// ((CActivity) mActivity).showProgress();
		// new SJECHttpHandler(collectCB,
		// mActivity).delPopMsg(PuApp.get().getToken(),
		// item.id);
		// }
		// });
		// LVP.add(delLVP);
		// LVP.add(new CListViewParam(R.id.text_tips, item.getTips(), true));
		return LVP;
	}

	@Override
	public void asyncData() {
		super.asyncData();
		new SJECHttpHandler(callback, mActivity).getDeviceListByUserID(page
				+ "", mPerpage + "", keyword);
	}

	AsyncHttpListCallBack<ArrayList<EntityDevice>> callback = new AsyncHttpListCallBack<ArrayList<EntityDevice>>(
			AdapterDeviceList.this) {
		@Override
		public void setType() {
			myType = new TypeToken<ArrayList<EntityDevice>>() {
			}.getType();
		}

		@Override
		public void addItems() {
			if (mT != null && !mT.isEmpty()) {
				for (int i = 0; i < mT.size(); i++) {
					mListItems.add(mT.get(i));
				}
				GlobalData.listDevice = (ArrayList<EntityDevice>) mListItems
						.clone();
			} else {
				NotificationsUtil.ToastBottomMsg(mActivity, "没有搜索到相关信息");
				return;
			}
		}
	};

}
