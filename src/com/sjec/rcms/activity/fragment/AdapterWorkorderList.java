package com.sjec.rcms.activity.fragment;

import java.text.ParseException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mslibs.utils.JSONUtils;
import com.mslibs.utils.NotificationsUtil;
import com.mslibs.widget.CActivity;
import com.mslibs.widget.CListView;
import com.mslibs.widget.CListViewParam;
import com.sjec.rcms.R;
import com.sjec.rcms.activity.main.ActivityMain;
import com.sjec.rcms.activity.workorder.ActivityAddWorkorder;
import com.sjec.rcms.activity.workorder.ActivityDoCheck;
import com.sjec.rcms.activity.workorder.ActivityDoMaintain;
import com.sjec.rcms.activity.workorder.ActivityDoMaintainV2;
import com.sjec.rcms.activity.workorder.ActivityDoRepair;
import com.sjec.rcms.activity.workorder.ActivityDoRepairV2;
import com.sjec.rcms.baidu.MyApplication;
import com.sjec.rcms.dao.EntityWorkorder;
import com.sjec.rcms.dao.EntityWorkorderDao;
import com.sjec.rcms.data.Constant;
import com.sjec.rcms.data.GlobalData;
import com.sjec.rcms.http.AsyncHttpListCallBack;
import com.sjec.rcms.http.SJECHttpHandler;
import com.sjec.rcms.util.SJECUtil;
import com.websharputil.common.ConvertUtil;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;
import com.websharputil.date.DateUtil;

/**
 * 
 * @类名称：AdapterWorkorderList
 * @包名：com.sjec.rcms.activity.fragment @描述： 工单列表 @创建人： dengzh
 * @创建时间:2015-8-5 下午3:13:21
 * @版本 V1.0
 * @Copyright (c) 2015 by 苏州威博世网络科技有限公司.
 */
public class AdapterWorkorderList extends CListView {
	FragmentWorkorderListV2 fragment;

	public AdapterWorkorderList(PullToRefreshListView lv, Activity activity, FragmentWorkorderListV2 frag) {
		super(lv, activity);
		fragment = frag;
		// initListViewStart();
	}

	@Override
	public void initListItemResource() {
		setListItemResource(R.layout.item_list_workorder);
	}

	@Override
	public void ensureUi() {
		mPerpage = 10;
		super.setGetMoreResource(R.layout.item_list_getmore, R.id.list_item_getmore_title, "加载更多");
		super.ensureUi();
		mListViewPTR.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
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
				// 打开工单详情页
				int i = (Integer) v.getTag();
				LogUtil.d("%s", "点击" + i);
				EntityWorkorder item = (EntityWorkorder) mListItems.get(i);
				Bundle b = new Bundle();
				b.putString("workorder_id", item.InnerID);
				// b.putInt("order_type", item.Type);// 1:抢修,2:维保
				if (item.Type == Constant.EnumWorkorderType.TYPE_WORKORDER_REPAIR) {
					Util.startActivity(mActivity, ActivityDoRepairV2.class, b, false);
				} else if (item.Type == Constant.EnumWorkorderType.TYPE_WORKORDER_MAINTAIN) {
					Util.startActivity(mActivity, ActivityDoMaintainV2.class, b, false);
				} else if (item.Type == Constant.EnumWorkorderType.TYPE_WORKORDER_CHECK) {
					// 检查
					Util.startActivity(mActivity, ActivityDoCheck.class, b, false);
				}
			}
		});
	}

	@Override
	public ArrayList<CListViewParam> matchListItem(Object obj, int index) {

		final EntityWorkorder item = (EntityWorkorder) obj;
		ArrayList<CListViewParam> LVP = new ArrayList<CListViewParam>();
		// CListViewParam cp = new CListViewParam();

		LVP.add(new CListViewParam(R.id.tv_workorder_add_time, item.Add_Time, true));
		if (item.EndTime == null || item.EndTime.isEmpty()) {
			LVP.add(new CListViewParam(R.id.tv_workorder_complete_time, "", true));
		} else {
			if (item.Type != Constant.EnumWorkorderType.TYPE_WORKORDER_CHECK) {
				LVP.add(new CListViewParam(R.id.tv_workorder_complete_time,
						mActivity.getResources().getString(R.string.str_order_end_time, item.EndTime), true));
			}
		}

		if (item.is_pic_exists != null) {
			LVP.add(new CListViewParam(R.id.iv_upload_pic, null, !item.is_pic_exists.isEmpty()));
		}

		if (item.Type == Constant.EnumWorkorderType.TYPE_WORKORDER_REPAIR) {
			// 抢修
			LVP.add(new CListViewParam(R.id.tv_device_num,
					mActivity.getResources().getString(R.string.str_item_device_num, item.Device_Num) + "["
							+ item.EventSource + "]",
					true));
			LVP.add(new CListViewParam(R.id.iv_qx, null, true));
			LVP.add(new CListViewParam(R.id.iv_jc, null, false));
			LVP.add(new CListViewParam(R.id.iv_wb, null, false));

			LVP.add(new CListViewParam(R.id.tv_device_name,
					Html.fromHtml(mActivity.getResources().getString(R.string.str_item_device_name,
							(item.Remark.isEmpty() ? "" : "[故障" + item.Remark + "]") + "["
									+ SJECUtil.GetControlTypeNameRepair(mActivity, item.ControlType) + "]"
									+ item.Village_Name,
							item.Village_Group_Num, item.Village_Stage_Num)),
					item.Remark.toString().indexOf("困人") >= 0 ? false : true));

			LVP.add(new CListViewParam(R.id.tv_device_name_kunren,
					Html.fromHtml(
					mActivity.getResources().getString(R.string.str_item_device_name,
							"[" + SJECUtil.GetControlTypeNameRepair(mActivity, item.ControlType) + "]"
									+ item.Village_Name,
							item.Village_Group_Num, item.Village_Stage_Num)),
					item.Remark.toString().indexOf("困人") >= 0 ? true : false));

		} else if (item.Type == Constant.EnumWorkorderType.TYPE_WORKORDER_MAINTAIN) {
			LVP.add(new CListViewParam(R.id.tv_device_num,
					mActivity.getResources().getString(R.string.str_item_device_num, item.Device_Num), true));
			// 维保
			LVP.add(new CListViewParam(R.id.iv_qx, null, false));
			LVP.add(new CListViewParam(R.id.iv_jc, null, false));
			LVP.add(new CListViewParam(R.id.iv_wb, null, true));

			// tv_workorder_maintain_type.setText(Constant.EnumMaintainType
			// .GetName(ConvertUtil.ParsetStringToInt32(
			// GlobalData.curWorkorder.MaintainType, -1)));

			if (item.ProvisionMaintainTime != null && !item.ProvisionMaintainTime.isEmpty()) {
				LVP.add(new CListViewParam(R.id.tv_provisionMaintainTime,
						mActivity.getString(R.string.str_maintain_lab_provision_time,
								DateUtil.TimeParseStringToFormatString(item.ProvisionMaintainTime, "yyyy-MM-dd")),
						true));
			} else {
				LVP.add(new CListViewParam(R.id.tv_provisionMaintainTime,
						mActivity.getString(R.string.str_maintain_lab_provision_time, ""), true));
			}
			String maintain_type = Constant.EnumMaintainType
					.GetName(ConvertUtil.ParsetStringToInt32(item.MaintainType, -1));

			CListViewParam paramDeviceName = new CListViewParam(R.id.tv_device_name,
					mActivity.getResources().getString(R.string.str_item_device_name,
							"[" + SJECUtil.GetControlTypeNameMaintain(mActivity, item.ControlType) + "]" + "["
									+ maintain_type + "]"
									// + (item.Remark.isEmpty() ? "" : "["
									// + item.Remark + "]")
									+ item.Village_Name,
							item.Village_Group_Num, item.Village_Stage_Num),
					true);
			try {

				if (item.Status == Constant.EnumWorkorderStatus.STATUS_WORKORDER_1) {
					int daysBetween = DateUtil.daysBetween(DateUtil.TimeParseNowToFormatString("yyyy-MM-dd"),
							DateUtil.TimeParseStringToFormatString(item.ProvisionMaintainTime, "yyyy-MM-dd"));
					if (daysBetween <= 0) {
						paramDeviceName.setItemTextColor(mActivity.getResources().getColor(R.color.color_red));
					} else if (daysBetween > 0 && daysBetween <= 3) {
						paramDeviceName.setItemTextColor(mActivity.getResources().getColor(R.color.color_yellow));
					}
				}

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			LVP.add(paramDeviceName);

			LVP.add(new CListViewParam(R.id.tv_device_name_kunren, "", false));

		} else if (item.Type == Constant.EnumWorkorderType.TYPE_WORKORDER_CHECK) {
			if (item.Status == Constant.EnumWorkorderStatus.STATUS_WORKORDER_3) {
				if (item.IsNeedAbarbeitung != null && item.IsNeedAbarbeitung.equals("0")) {
					LVP.add(new CListViewParam(R.id.tv_workorder_complete_time, "不须整改", true));
				} else {
					if (item.CompleteDate != null) {
						if (item.CompleteDate.isEmpty()) {
							LVP.add(new CListViewParam(R.id.tv_workorder_complete_time, "整改未完成", true));
						} else {
							LVP.add(new CListViewParam(R.id.tv_workorder_complete_time,
									"整改完成" + DateUtil.TimeParseStringToFormatString(item.CompleteDate, "yyyy-MM-dd"),
									true));
						}
					}
				}
			} else {
				LVP.add(new CListViewParam(R.id.tv_workorder_complete_time, "", false));
			}

			LVP.add(new CListViewParam(R.id.tv_device_num,
					mActivity.getResources().getString(R.string.str_item_device_num, item.Device_Num), true));
			// 维保
			LVP.add(new CListViewParam(R.id.iv_jc, null, true));
			LVP.add(new CListViewParam(R.id.iv_qx, null, false));
			LVP.add(new CListViewParam(R.id.iv_wb, null, false));

			LVP.add(new CListViewParam(R.id.tv_provisionMaintainTime, "", false));

			CListViewParam paramDeviceName = new CListViewParam(R.id.tv_device_name,
					mActivity.getResources().getString(R.string.str_item_device_name, "["
							+ SJECUtil.GetControlTypeNameCheck(mActivity, item.ControlType) + "]" + item.Village_Name,
							item.Village_Group_Num, item.Village_Stage_Num),
					true);

			LVP.add(paramDeviceName);

			LVP.add(new CListViewParam(R.id.tv_device_name_kunren, "", false));

		} else {
			LVP.add(new CListViewParam(R.id.tv_device_num,
					mActivity.getResources().getString(R.string.str_item_device_num, item.Device_Num), true));
			// 维保
			LVP.add(new CListViewParam(R.id.iv_qx, null, false));
			LVP.add(new CListViewParam(R.id.iv_wb, null, true));
			if (item.ProvisionMaintainTime != null && !item.ProvisionMaintainTime.isEmpty()) {
				LVP.add(new CListViewParam(R.id.tv_provisionMaintainTime,
						mActivity.getString(R.string.str_maintain_lab_provision_time,
								DateUtil.TimeParseStringToFormatString(item.ProvisionMaintainTime, "yyyy-MM-dd")),
						true));
			} else {
				LVP.add(new CListViewParam(R.id.tv_provisionMaintainTime,
						mActivity.getString(R.string.str_maintain_lab_provision_time, ""), true));
			}

			CListViewParam paramDeviceName = new CListViewParam(R.id.tv_device_name,
					mActivity.getResources().getString(R.string.str_item_device_name,
							"[" + SJECUtil.GetControlTypeNameMaintain(mActivity, item.ControlType) + "]"
									+ (item.Remark.isEmpty() ? "" : "[" + item.Remark + "]") + item.Village_Name,
							item.Village_Group_Num, item.Village_Stage_Num),
					true);
			try {
				if (item.Status == Constant.EnumWorkorderStatus.STATUS_WORKORDER_1) {
					int daysBetween = DateUtil.daysBetween(DateUtil.TimeParseNowToFormatString("yyyy-MM-dd"),
							DateUtil.TimeParseStringToFormatString(item.ProvisionMaintainTime, "yyyy-MM-dd"));
					if (daysBetween <= 0) {
						paramDeviceName.setItemTextColor(mActivity.getResources().getColor(R.color.color_red));
					} else if (daysBetween > 0 && daysBetween <= 3) {
						paramDeviceName.setItemTextColor(mActivity.getResources().getColor(R.color.color_yellow));
					}
				}

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			LVP.add(paramDeviceName);

			LVP.add(new CListViewParam(R.id.tv_device_name_kunren, "", false));

		}

		// CListViewParam avatarLVP = new CListViewParam(R.id.img_avatar,
		// R.drawable.img_default,
		// true);
		// avatarLVP.setImgAsync(true);
		// avatarLVP.setItemTag(item.pic);
		return LVP;
	}

	@Override
	public void asyncData() {
		super.asyncData();
		new SJECHttpHandler(callback, mActivity).getUserWorkorder(page + "", mPerpage + "", "", fragment.workorderType,
				fragment.workorderSource, fragment.workorderStauts, fragment.keyword,
				fragment.filter_pic_exists ? "1" : "0");
	}

	AsyncHttpListCallBack<ArrayList<EntityWorkorder>> callback = new AsyncHttpListCallBack<ArrayList<EntityWorkorder>>(
			AdapterWorkorderList.this) {

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
				GlobalData.listWorkorder = (ArrayList<EntityWorkorder>) mListItems.clone();
				// EntityWorkorderDao dao =
				// MyApplication.daoSession.getEntityWorkorderDao();
				//
				//
				// //for(int i = 0; i<GlobalData.listWorkorder.size();i++){
				// ArrayList<EntityWorkorder> listWork =
				// (ArrayList<EntityWorkorder>) dao.queryBuilder().list();
				// for(int i = 0; i<listWork.size();i++){
				// LogUtil.d("from
				// db:"+listWork.get(i).InnerID+","+listWork.get(i).Program_Name);
				// }

				// }
			} else {
				// NotificationsUtil.ToastBottomMsg(mActivity, "没有搜索到工单");
				return;
			}
		}
	};

}
