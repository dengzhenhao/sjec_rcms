package com.sjec.rcms.activity.user;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

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
import com.sjec.rcms.dao.EntitySparePartApply;
import com.sjec.rcms.dao.EntitySparePartQuotation;
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
 * @类名称：AdapterListSparePart
 * @包名：com.sjec.rcms.activity.user
 * @描述： TODO
 * @创建人： dengzh
 * @创建时间:2016-4-5 下午2:28:55
 * @版本 V1.0
 * @Copyright (c) 2016 by 苏州威博世网络科技有限公司.
 */
public class AdapterListSparePart extends CListView {

	public String closeStatus = "";
	
	public AdapterListSparePart(PullToRefreshListView lv, Activity activity) {
		super(lv, activity);

		initListViewStart();
	}

	@Override
	public void initListItemResource() {
		setListItemResource(R.layout.item_list_spare_part);
	}

	@Override
	public void ensureUi() {
		// mPerpage = 10;
		// super.setGetMoreResource(R.layout.item_list_getmore,
		// R.id.list_item_getmore_title, "加载更多");

		super.ensureUi();
		// mListViewPTR
		// .setOnLastItemVisibleListener(new
		// PullToRefreshBase.OnLastItemVisibleListener() {
		// @Override
		// public void onLastItemVisible() {
		// if (actionType == IDLE && !mAdapter.isNotMore) {
		// // 如果并没有在加载过程中,可以加载更多
		// getmoreListViewStart();
		// }
		// }
		// });

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
				EntitySparePartApply item = (EntitySparePartApply) mListItems
						.get(i);
				Bundle b = new Bundle();
				b.putString("app_quo_id", item.ID.toString());
				b.putString("quo_id", item.QuotationID == null ? ""
						: item.QuotationID);
				GlobalData.curApply = (EntitySparePartApply) mListItems.get(i);
				Util.startActivity(mActivity, ActivityDetailSparePart.class, b,
						false);
			}
		});
	}

	@Override
	public ArrayList<CListViewParam> matchListItem(Object obj, int index) {

		final EntitySparePartApply item = (EntitySparePartApply) obj;
		ArrayList<CListViewParam> LVP = new ArrayList<CListViewParam>();
		LVP.add(new CListViewParam(R.id.tv_program_name, "[" + item.DeviceCode
				+ "]" + item.ProgramName, true));
		// LVP.add(new CListViewParam(R.id.tv_device_num, item.DeviceCode,
		// true));
		LVP.add(new CListViewParam(R.id.tv_add_time, "申请时间：" + item.ApplyTime,
				true));
		LVP.add(new CListViewParam(R.id.tv_handler_user, "受理信息："
				+ item.HandlerUser + "      " + item.HandlerTelephone, true));
		LVP.add(new CListViewParam(R.id.tv_handler_time, "受理时间："
				+ item.HandlerTime, true));
		return LVP;
	}

	@Override
	public void asyncData() {
		super.asyncData();
		new SJECHttpHandler(callback, mActivity)
				.SparePart_GetUserSparePartList(page + "", mPerpage + "",closeStatus);
	}

	AsyncHttpListCallBack<ArrayList<EntitySparePartApply>> callback = new AsyncHttpListCallBack<ArrayList<EntitySparePartApply>>(
			AdapterListSparePart.this) {

		@Override
		public void setType() {
			myType = new TypeToken<ArrayList<EntitySparePartApply>>() {
			}.getType();
		}

		@Override
		public void addItems() {
			if (mT != null && !mT.isEmpty()) {
				for (int i = 0; i < mT.size(); i++) {
					mListItems.add(mT.get(i));
				}
				GlobalData.listSparePart = (ArrayList<EntitySparePartApply>) mListItems
						.clone();

			} else {
				NotificationsUtil.ToastBottomMsg(mActivity, "没有搜索到你提交的备件申请");
				return;
			}
		}
	};

}
