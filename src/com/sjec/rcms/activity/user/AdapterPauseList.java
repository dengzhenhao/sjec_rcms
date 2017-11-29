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
import com.sjec.rcms.activity.fragment.AdapterWorkorderList;
import com.sjec.rcms.activity.fragment.FragmentWorkorderListV2;
import com.sjec.rcms.activity.main.ActivityMain;
import com.sjec.rcms.activity.workorder.ActivityDoMaintainV2;
import com.sjec.rcms.activity.workorder.ActivityDoRepairV2;
import com.sjec.rcms.dao.EntityDevice;
import com.sjec.rcms.dao.EntityPauseConfirm;
import com.sjec.rcms.dao.EntityWorkorder;
import com.sjec.rcms.data.Constant;
import com.sjec.rcms.data.GlobalData;
import com.sjec.rcms.data.Constant.EnumWorkorderPauseStatus;
import com.sjec.rcms.http.AsyncHttpCallBack;
import com.sjec.rcms.http.AsyncHttpListCallBack;
import com.sjec.rcms.http.SJECHttpHandler;
import com.sjec.rcms.util.SJECUtil;
import com.websharputil.common.ConvertUtil;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;

/**
 * 
* @类名称：AdapterPauseList 
* @包名：com.sjec.rcms.activity.user
* @描述： TODO
* @创建人： dengzh
* @创建时间:2016-4-5 下午2:28:55
* @版本 V1.0
* @Copyright (c) 2016 by 苏州威博世网络科技有限公司.
 */
public class AdapterPauseList extends CListView {

	public AdapterPauseList(PullToRefreshListView lv, Activity activity) {
		super(lv, activity);
		
		initListViewStart();
	}

	@Override
	public void initListItemResource() {
		setListItemResource(R.layout.item_list_workorder);
	}

	@Override
	public void ensureUi() {
//		mPerpage = 10;
//		super.setGetMoreResource(R.layout.item_list_getmore,
//				R.id.list_item_getmore_title, "加载更多");
		
		super.ensureUi();
//		mListViewPTR
//				.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
//					@Override
//					public void onLastItemVisible() {
//						if (actionType == IDLE && !mAdapter.isNotMore) {
//							// 如果并没有在加载过程中,可以加载更多
//							getmoreListViewStart();
//						}
//					}
//				});

		// super.setGetMoreClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// getmoreListViewStart();
		// }
		// });

		super.setItemOnclickLinstener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 打开工单详情页
				int i = (Integer) v.getTag();
				LogUtil.d("%s", "点击" + i);
				EntityWorkorder item = (EntityWorkorder) mListItems.get(i);
				Bundle b = new Bundle();
				b.putString("workorder_id", item.InnerID);
				// b.putInt("order_type", item.Type);// 1:抢修,2:维保
				if (item.Type == 1) {
					Util.startActivity(mActivity, ActivityDoRepairV2.class, b,
							false);
				} else if (item.Type == 2) {
					Util.startActivity(mActivity, ActivityDoMaintainV2.class,
							b, false);
				}
			}
		});
	}

	@Override
	public ArrayList<CListViewParam> matchListItem(Object obj, int index) {

		final EntityWorkorder item = (EntityWorkorder) obj;
		ArrayList<CListViewParam> LVP = new ArrayList<CListViewParam>();
		LVP.add(new CListViewParam(
				R.id.tv_device_name,
				mActivity.getResources()
						.getString(
								R.string.str_item_device_name,
								(item.Remark.isEmpty() ? "" : "[故障"
										+ item.Remark + "]")
										+ "["
										+ SJECUtil.GetControlTypeNameRepair(
												mActivity, item.ControlType)
										+ "]" + item.Village_Name,
								item.Village_Group_Num, item.Village_Stage_Num),
				true));
		LVP.add(new CListViewParam(R.id.tv_device_num, mActivity.getResources()
				.getString(R.string.str_item_device_num, item.Device_Num), true));
		LVP.add(new CListViewParam(R.id.tv_workorder_add_time, item.Add_Time,
				true));
		if (item.EndTime == null || item.EndTime.isEmpty()) {
			LVP.add(new CListViewParam(R.id.tv_workorder_complete_time, "",
					true));
		} else {
			LVP.add(new CListViewParam(R.id.tv_workorder_complete_time,
					mActivity.getResources().getString(
							R.string.str_order_end_time, item.EndTime), true));
		}
		if (item.Type == Constant.EnumWorkorderType.TYPE_WORKORDER_REPAIR) {
			// 抢修
			LVP.add(new CListViewParam(R.id.iv_qx, null, true));
			LVP.add(new CListViewParam(R.id.iv_wb, null, false));
		} else {
			// 维保
			LVP.add(new CListViewParam(R.id.iv_qx, null, false));
			LVP.add(new CListViewParam(R.id.iv_wb, null, true));
		}

		return LVP;
	}

	@Override
	public void asyncData() {
		super.asyncData();
		new SJECHttpHandler(callback, mActivity).queryWorkOrderPause();
	}

	AsyncHttpListCallBack<ArrayList<EntityWorkorder>> callback = new AsyncHttpListCallBack<ArrayList<EntityWorkorder>>(
			AdapterPauseList.this) {

		@Override
		public void setType() {
			myType = new TypeToken<ArrayList<EntityWorkorder>>() {
			}.getType();
		}

		@Override
		public void addItems() {
			if (mT != null && !mT.isEmpty()) {
				for (int i = 0; i < mT.size(); i++) {
					mListItems.add(mT.get(i));
				}
				GlobalData.listWorkorder = (ArrayList<EntityWorkorder>) mListItems
						.clone();
			} else {
				NotificationsUtil.ToastBottomMsg(mActivity, "没有搜索到工单");
				return;
			}
		}
	};


}
