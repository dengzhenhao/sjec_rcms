package com.sjec.rcms.activity.user;

import java.util.ArrayList;

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
import com.sjec.rcms.activity.main.ActivityMain;
import com.sjec.rcms.dao.EntityDevice;
import com.sjec.rcms.dao.EntityPauseConfirm;
import com.sjec.rcms.data.Constant;
import com.sjec.rcms.data.GlobalData;
import com.sjec.rcms.data.Constant.EnumWorkorderPauseStatus;
import com.sjec.rcms.http.AsyncHttpCallBack;
import com.sjec.rcms.http.AsyncHttpListCallBack;
import com.sjec.rcms.http.SJECHttpHandler;
import com.websharputil.common.ConvertUtil;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;

/**
 * 
 * @类名称：AdapterConfirmList
 * @包名：com.sjec.rcms.activity.fragment
 * @描述： 待审核列表
 * @创建人： dengzh
 * @创建时间:2015-8-5 下午3:13:08
 * @版本 V1.0
 * @Copyright (c) 2015 by 苏州威博世网络科技有限公司.
 */
public class AdapterConfirmList extends CListView {

	public AdapterConfirmList(PullToRefreshListView lv, Activity activity) {
		super(lv, activity);
		initListViewStart();
	}

	@Override
	public void initListItemResource() {
		setListItemResource(R.layout.item_list_pause_confirm);
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
				GlobalData.curPauseConfirm = GlobalData.listPauseConfirm.get(i);
				Util.startActivity(mActivity, ActivityConfirmInfo.class, false);

			}
		});
	}

	@Override
	public ArrayList<CListViewParam> matchListItem(Object obj, int index) {
		LogUtil.d("%s", index);
		final EntityPauseConfirm item = (EntityPauseConfirm) obj;
		ArrayList<CListViewParam> LVP = new ArrayList<CListViewParam>();

		LVP.add(new CListViewParam(R.id.tv_device_name, item.Village_Name
				+ item.Device_Num, true));
		LVP.add(new CListViewParam(R.id.tv_pause_step, EnumWorkorderPauseStatus
				.GetName(item.FromPauseStatus)
				+ "  >>>  "
				+ EnumWorkorderPauseStatus.GetName(item.TargetPauseStatus),
				true));
		LVP.add(new CListViewParam(R.id.tv_req_time, item.OccurTime, true));

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
		new SJECHttpHandler(callback, mActivity).getPauseConfirmListByUserID(
				page + "", mPerpage + "");
	}

	AsyncHttpListCallBack<ArrayList<EntityPauseConfirm>> callback = new AsyncHttpListCallBack<ArrayList<EntityPauseConfirm>>(
			AdapterConfirmList.this) {
		@Override
		public void setType() {
			myType = new TypeToken<ArrayList<EntityPauseConfirm>>() {
			}.getType();
		}

		@Override
		public void addItems() {
			if (mT != null && !mT.isEmpty()) {
				for (int i = 0; i < mT.size(); i++) {
					mListItems.add(mT.get(i));
				}
				GlobalData.listPauseConfirm = (ArrayList<EntityPauseConfirm>) mListItems
						.clone();
			} else {
				NotificationsUtil.ToastBottomMsg(mActivity, "没有搜索到相关信息");
				return;
			}
		}
	};

}
