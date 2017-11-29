package com.sjec.rcms.activity.user;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings.Global;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.sjec.rcms.R;
import com.sjec.rcms.activity.BaseActivity;
import com.sjec.rcms.activity.login.ActivityLogin;
import com.sjec.rcms.activity.main.ActivityMain;
import com.sjec.rcms.activity.workorder.ActivityApplySparePart;
import com.sjec.rcms.dao.EntityKnowledgeType;
import com.sjec.rcms.dao.EntityQuotationStatusLog;
import com.sjec.rcms.dao.EntitySparePartApplyData;
import com.sjec.rcms.dao.EntitySparePartApplyPic;
import com.sjec.rcms.dao.EntitySparePartQuotation;
import com.sjec.rcms.dao.EntitySparePartQuotationData;
import com.sjec.rcms.dao.EntityUser;
import com.sjec.rcms.data.Constant;
import com.sjec.rcms.data.GlobalData;
import com.sjec.rcms.http.AsyncHttpCallBack;
import com.sjec.rcms.http.SJECHttpHandler;
import com.sjec.rcms.util.UtilOpenFile;
import com.websharputil.common.ConvertUtil;
import com.websharputil.common.LogUtil;
import com.websharputil.common.PrefUtil;
import com.websharputil.common.Util;
import com.websharputil.json.JSONUtils;

public class ActivityDetailSparePart extends BaseActivity {
	private LinearLayout layout_back;
	private TextView tv_title;

	TextView tv_program_name;
	TextView tv_device_num;
	TextView tv_customer_type;
	TextView tv_apply_user;
	TextView tv_apply_time;
	TextView tv_handler_user;
	TextView tv_handler_time;
	TextView tv_ordernum;
	TextView tv_arrow_apply, tv_status_name_apply, tv_time_apply;
	TextView tv_arrow_gen, tv_status_name_gen, tv_time_gen;
	TextView tv_arrow_effective, tv_status_name_effective, tv_time_effective;
	TextView tv_arrow_deliver, tv_status_name_deliver, tv_time_deliver;
	TextView tv_arrow_install, tv_status_name_install, tv_time_install;
	TextView tv_arrow_close, tv_status_name_close, tv_time_close;
	LinearLayout layout_detail, layout_apply_pic, layout_quotation_data;
	TextView tv_add_detail;

	String apply_id = "";
	String quo_id = "";

	LinearLayout layout_downloading;
	TextView tv_downloading;

	PullToRefreshScrollView scroll_refresh;

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Constant.ACTION_OPEN_ATTACH)) {
				Bundle b = intent.getExtras();
				String url = b.getString("url", "");
				UtilOpenFile.handlerFileUrl(url, ActivityDetailSparePart.this,
						layout_downloading, tv_downloading);
			} else if (intent.getAction().equals(
					Constant.ACTION_REFRESH_APPLY_INFO)) {
				
				new SJECHttpHandler(cbDetail, ActivityDetailSparePart.this)
						.SparePart_GetSparePartDetailByID(apply_id);
				new SJECHttpHandler(cbApplyData, ActivityDetailSparePart.this)
						.SparePart_GetSparePartDataByID(apply_id);
				new SJECHttpHandler(cbApplyPic, ActivityDetailSparePart.this)
						.SparePart_GetSparePartPicByID(apply_id);
				if (!quo_id.isEmpty()) {
					new SJECHttpHandler(cbStatusLog,
							ActivityDetailSparePart.this)
							.SparePart_QuerySparePartStatusLogByID(quo_id);
					new SJECHttpHandler(cbQuotationData,
							ActivityDetailSparePart.this)
							.SparePart_GetSparePartQuotationDataByID(quo_id);
				}
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_back:
			finish();
			break;
		case R.id.layout_item_attach:
			Intent intentOpenAttach = new Intent();
			Bundle bAttach = new Bundle();
			bAttach.putString("url", v.getTag().toString());
			intentOpenAttach.putExtras(bAttach);
			intentOpenAttach.setAction(Constant.ACTION_OPEN_ATTACH);
			getApplicationContext().sendBroadcast(intentOpenAttach);
			break;
		case R.id.tv_add_detail:
			Bundle b = new Bundle();
			b.putString("edit_spare_part", "1");
			Util.startActivity(ActivityDetailSparePart.this,
					ActivityApplySparePart.class, b, false);
			break;
		case R.id.tv_setup:
			final EntitySparePartQuotationData data = (EntitySparePartQuotationData) v
					.getTag();
			Util.createDialog(ActivityDetailSparePart.this, null,
					R.string.msg_dialog_add_order_type_title,
					R.string.msg_confirm_setup_parts, null, true, false,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							new SJECHttpHandler(cbUpdateQuotationDataSetup,
									ActivityDetailSparePart.this)
									.SparePart_UpdateQuotationDataSetup(
											data.Quotation_ID,
											data.ID.toString());
						}
					}).show();
			break;
		}
	}

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_detail_spare_part);

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	@Override
	public void init(Bundle savedInstanceState) {
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_program_name = (TextView) findViewById(R.id.tv_program_name);
		tv_device_num = (TextView) findViewById(R.id.tv_device_num);
		tv_customer_type = (TextView) findViewById(R.id.tv_customer_type);
		tv_apply_user = (TextView) findViewById(R.id.tv_apply_user);
		tv_apply_time = (TextView) findViewById(R.id.tv_apply_time);
		tv_handler_user = (TextView) findViewById(R.id.tv_handler_user);
		tv_handler_time = (TextView) findViewById(R.id.tv_handler_time);
		tv_ordernum = (TextView) findViewById(R.id.tv_ordernum);
		tv_arrow_apply = (TextView) findViewById(R.id.tv_arrow_apply);
		tv_status_name_apply = (TextView) findViewById(R.id.tv_status_name_apply);
		tv_time_apply = (TextView) findViewById(R.id.tv_time_apply);
		tv_arrow_gen = (TextView) findViewById(R.id.tv_arrow_gen);
		tv_status_name_gen = (TextView) findViewById(R.id.tv_status_name_gen);
		tv_time_gen = (TextView) findViewById(R.id.tv_time_gen);
		tv_arrow_effective = (TextView) findViewById(R.id.tv_arrow_effective);
		tv_status_name_effective = (TextView) findViewById(R.id.tv_status_name_effective);
		tv_time_effective = (TextView) findViewById(R.id.tv_time_effective);
		tv_arrow_deliver = (TextView) findViewById(R.id.tv_arrow_deliver);
		tv_status_name_deliver = (TextView) findViewById(R.id.tv_status_name_deliver);
		tv_time_deliver = (TextView) findViewById(R.id.tv_time_deliver);
		tv_arrow_install = (TextView) findViewById(R.id.tv_arrow_install);
		tv_status_name_install = (TextView) findViewById(R.id.tv_status_name_install);
		tv_time_install = (TextView) findViewById(R.id.tv_time_install);
		tv_arrow_close = (TextView) findViewById(R.id.tv_arrow_close);
		tv_status_name_close = (TextView) findViewById(R.id.tv_status_name_close);
		tv_time_close = (TextView) findViewById(R.id.tv_time_close);
		layout_detail = (LinearLayout) findViewById(R.id.layout_detail);
		layout_apply_pic = (LinearLayout) findViewById(R.id.layout_apply_pic);
		layout_quotation_data = (LinearLayout) findViewById(R.id.layout_quotation_data);
		tv_add_detail = (TextView) findViewById(R.id.tv_add_detail);
		layout_downloading = (LinearLayout) findViewById(R.id.layout_downloading);
		tv_downloading = (TextView) findViewById(R.id.tv_downloading);
		scroll_refresh = (PullToRefreshScrollView) findViewById(R.id.scroll_refresh);

		scroll_refresh.setMode(Mode.PULL_FROM_START);

		scroll_refresh
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase refreshView) {
						
						// TODO Auto-generated method stub
						new SJECHttpHandler(cbDetail, ActivityDetailSparePart.this)
								.SparePart_GetSparePartDetailByID(apply_id);
						new SJECHttpHandler(cbApplyData, ActivityDetailSparePart.this)
								.SparePart_GetSparePartDataByID(apply_id);
						new SJECHttpHandler(cbApplyPic, ActivityDetailSparePart.this)
								.SparePart_GetSparePartPicByID(apply_id);
						if (!quo_id.isEmpty()) {
							new SJECHttpHandler(cbStatusLog, ActivityDetailSparePart.this)
									.SparePart_QuerySparePartStatusLogByID(quo_id);
							new SJECHttpHandler(cbQuotationData, ActivityDetailSparePart.this)
									.SparePart_GetSparePartQuotationDataByID(quo_id);
						}
					}

					@Override
					public void onPullUpToRefresh(PullToRefreshBase refreshView) {
						// TODO Auto-generated method stub

					}
				});
		
	}

	@Override
	public void bindData() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.ACTION_OPEN_ATTACH);
		filter.addAction(Constant.ACTION_REFRESH_APPLY_INFO);
		registerReceiver(receiver, filter);

		tv_title.setText("备件申请");
		Bundle b = getIntent().getExtras();
		apply_id = b.getString("app_quo_id", "");
		quo_id = b.getString("quo_id", "");
		tv_program_name.setText(GlobalData.curApply.ProgramName);
		tv_customer_type.setText("客户类型：" + GlobalData.curApply.BigCustomerName);
		tv_device_num.setText("　设备号：" + GlobalData.curApply.DeviceCode);
		tv_apply_user.setText("　申请人：" + GlobalData.curApply.ApplyUserName + "("
				+ GlobalData.curApply.ApplyUserStaffNo + ")");
		tv_apply_time.setText("申请时间：" + GlobalData.curApply.ApplyTime);
		//
		tv_time_apply.setText(GlobalData.curApply.ApplyTime);
		tv_add_detail.setOnClickListener(this);
		layout_back.setOnClickListener(this);
		new SJECHttpHandler(cbDetail, this)
				.SparePart_GetSparePartDetailByID(apply_id);
		new SJECHttpHandler(cbApplyData, this)
				.SparePart_GetSparePartDataByID(apply_id);
		new SJECHttpHandler(cbApplyPic, this)
				.SparePart_GetSparePartPicByID(apply_id);
		if (!quo_id.isEmpty()) {
			new SJECHttpHandler(cbStatusLog, this)
					.SparePart_QuerySparePartStatusLogByID(quo_id);
			new SJECHttpHandler(cbQuotationData, this)
					.SparePart_GetSparePartQuotationDataByID(quo_id);
		}
	}

	private void bindAppQuotationData() {
		// if (GlobalData.listSparePartData.size() > 0) {
		// EntitySparePart data = GlobalData.listSparePartData
		// .get(0);
		// tv_draw_code.setText("图号:" + data.Draw_Code);
		// tv_code.setText("代号:" + data.Code);
		// tv_count.setText("数量:" + data.Quantity);
		// tv_remark.setText("备注:" + data.Description);
		// }
		tv_handler_user.setText("　受理人："
				+ GlobalData.curSparePartQuotation.Creator);
		tv_handler_time.setText("受理时间："
				+ GlobalData.curSparePartQuotation.CreateTime);
		tv_ordernum
				.setText("　订单号：" + GlobalData.curSparePartQuotation.OrderNum);
	}

	private void bindApplyData() {
		layout_detail.removeAllViews();
		if (GlobalData.listSparePartData.size() > 0) {
			layout_detail.setVisibility(View.VISIBLE);
			LayoutInflater mInflater = LayoutInflater.from(this);
			View headerView = mInflater.inflate(R.layout.headview_apply_part,
					null);
			layout_detail.addView(headerView);
			for (int i = 0; i < GlobalData.listSparePartData.size(); i++) {
				View convertView = mInflater.inflate(R.layout.item_apply_part,
						null);
				TextView tv_part_name = (TextView) convertView
						.findViewById(R.id.tv_part_name);
				TextView tv_part_count = (TextView) convertView
						.findViewById(R.id.tv_part_count);
				ImageView iv_delete = (ImageView) convertView
						.findViewById(R.id.iv_delete);
				tv_part_name
						.setText(GlobalData.listSparePartData.get(i).Part_Name);
				tv_part_count
						.setText(GlobalData.listSparePartData.get(i).Quantity
								+ "");
				iv_delete.setVisibility(View.INVISIBLE);
				layout_detail.addView(convertView);
			}
		} else {
			layout_detail.setVisibility(View.GONE);
		}
	}

	private void bindApplyPic() {
		layout_apply_pic.removeAllViews();
		LayoutInflater mInFlater = LayoutInflater.from(this);
		if (GlobalData.listSparePartPic.size() > 0) {
			layout_apply_pic.setVisibility(View.VISIBLE);
			View headerView = mInFlater.inflate(
					R.layout.headview_apply_part_pic, null);
			layout_apply_pic.addView(headerView);
			for (int i = 0; i < GlobalData.listSparePartPic.size(); i++) {
				RelativeLayout layoutAttach = (RelativeLayout) mInFlater
						.inflate(R.layout.item_attach_list, null);
				TextView tv_attach_name = (TextView) layoutAttach
						.findViewById(R.id.tv_attach_name);

				LinearLayout layout_split = (LinearLayout) layoutAttach
						.findViewById(R.id.layout_split);
				if (i == GlobalData.listSparePartPic.size() - 1) {
					layout_split.setVisibility(View.INVISIBLE);
				}
				tv_attach_name
						.setText(Html.fromHtml("<u>"
								+ GlobalData.listSparePartPic.get(i).PicPath
										.substring(GlobalData.listSparePartPic
												.get(i).PicPath
												.lastIndexOf("/") + 1) + "</u>"));
				layoutAttach.setTag(SJECHttpHandler.BASE_URL_SPARE_PART
						+ GlobalData.listSparePartPic.get(i).PicPath.replace(
								".", "_big."));
				layoutAttach.setOnClickListener(this);
				layout_apply_pic.addView(layoutAttach);
			}
		}
	}

	private void bindQuotationData() {
		layout_quotation_data.removeAllViews();
		LayoutInflater mInFlater = LayoutInflater.from(this);
		if (GlobalData.listSparePartQuotationData.size() > 0) {
			layout_quotation_data.setVisibility(View.VISIBLE);
			View headerView = mInFlater.inflate(
					R.layout.headview_quotation_data, null);
			layout_quotation_data.addView(headerView);
			for (int i = 0; i < GlobalData.listSparePartQuotationData.size(); i++) {
				LinearLayout layoutAttach = (LinearLayout) mInFlater.inflate(
						R.layout.item_quotation_data, null);
				TextView tv_part_name = (TextView) layoutAttach
						.findViewById(R.id.tv_part_name);
				TextView tv_part_count = (TextView) layoutAttach
						.findViewById(R.id.tv_part_count);
				TextView tv_delivery_period = (TextView) layoutAttach
						.findViewById(R.id.tv_delivery_period);
				TextView tv_setup = (TextView) layoutAttach
						.findViewById(R.id.tv_setup);
				TextView tv_time = (TextView) layoutAttach
						.findViewById(R.id.tv_time);
				tv_part_name.setText(GlobalData.listSparePartQuotationData
						.get(i).Name);
				tv_part_count
						.setText(ConvertUtil.ParseDoubleToString(ConvertUtil
								.ParsetStringToDouble(
										GlobalData.listSparePartQuotationData
												.get(i).Quantity, 0),
								ConvertUtil.FORMAT_DECIMAL_FORMAT_3));
				tv_delivery_period
						.setText(GlobalData.listSparePartQuotationData.get(i).Delivery_Period);
				if (GlobalData.listSparePartQuotationData.get(i).DeliveryTime == null) {
					tv_setup.setVisibility(View.GONE);
					tv_time.setVisibility(View.VISIBLE);
				} else {
					if (GlobalData.listSparePartQuotationData.get(i).SetupTime == null) {
						tv_setup.setVisibility(View.VISIBLE);
						tv_time.setVisibility(View.GONE);
					} else {
						tv_setup.setVisibility(View.GONE);
						tv_time.setVisibility(View.VISIBLE);
						tv_time.setText("已安装");
					}

				}
				tv_setup.setTag(GlobalData.listSparePartQuotationData.get(i));
				tv_setup.setOnClickListener(this);
				// layoutAttach.setTag(SJECHttpHandler.BASE_URL_SPARE_PART
				// + GlobalData.listSparePartPic.get(i).PicPath.replace(
				// ".", "_big."));
				// layoutAttach.setOnClickListener(this);
				layout_quotation_data.addView(layoutAttach);
			}
		}
	}

	private void bindQuotationStatusLog() {

		for (int i = 0; i < GlobalData.listQuotationStatusLog.size(); i++) {
			EntityQuotationStatusLog log = GlobalData.listQuotationStatusLog
					.get(i);
			if (log.TargetStatus == 2) {
				tv_time_gen.setText(log.CreateTime);
				setStatusTextViewStyle(tv_arrow_gen, tv_status_name_gen,
						tv_time_gen);
			}
			if (log.TargetStatus == 3) {
				tv_time_effective.setText(log.CreateTime);
				setStatusTextViewStyle(tv_arrow_effective,
						tv_status_name_effective, tv_time_effective);
			}
			if (log.TargetStatus == 4) {
				tv_time_deliver.setText(log.CreateTime);
				setStatusTextViewStyle(tv_arrow_deliver,
						tv_status_name_deliver, tv_time_deliver);
			}
			// if (log.TargetStatus == 5) {
			// tv_time_transport.setText(log.CreateTime);
			// setStatusTextViewStyle(tv_arrow_transport,
			// tv_status_name_transport, tv_time_transport);
			// }
			if (log.TargetStatus == 6) {
				tv_time_install.setText(log.CreateTime);
				setStatusTextViewStyle(tv_arrow_install,
						tv_status_name_install, tv_time_install);
			}
			if (log.TargetStatus == 10) {
				tv_time_close.setText(log.CreateTime);
				setStatusTextViewStyle(tv_arrow_close, tv_status_name_close,
						tv_time_close);
			}

		}
	}

	private void setStatusTextViewStyle(TextView... arrTvs) {
		for (int i = 0; i < arrTvs.length; i++) {
			arrTvs[i].setTextColor(getResources().getColor(
					R.color.color_light_green));
			arrTvs[i].setVisibility(View.VISIBLE);
		}
	}

	AsyncHttpCallBack cbDetail = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				scroll_refresh.onRefreshComplete();
				obj = new JSONObject(response);

				if (obj.optInt("code") == 0) {
					tv_add_detail.setText("已被受理，不能再添加备件申请");
					tv_add_detail.setEnabled(false);
					tv_add_detail.setTextColor(getResources().getColor(
							android.R.color.darker_gray));
					GlobalData.curSparePartQuotation = JSONUtils.fromJson(obj
							.optJSONObject("data").toString(),
							EntitySparePartQuotation.class);
					bindAppQuotationData();
				} else {
					Util.createToast(ActivityDetailSparePart.this, "申请暂未被受理",
							3000).show();
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

	AsyncHttpCallBack cbApplyData = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);

				if (obj.optInt("code") == 0) {
					GlobalData.listSparePartData = JSONUtils
							.fromJson(
									obj.optJSONArray("data").toString(),
									new TypeToken<ArrayList<EntitySparePartApplyData>>() {
									});
					bindApplyData();
				} else {
					Util.createToast(ActivityDetailSparePart.this, "查询失败", 3000)
							.show();
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

	AsyncHttpCallBack cbApplyPic = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);

				if (obj.optInt("code") == 0) {
					GlobalData.listSparePartPic = JSONUtils
							.fromJson(
									obj.optJSONArray("data").toString(),
									new TypeToken<ArrayList<EntitySparePartApplyPic>>() {
									});
					bindApplyPic();
				} else {
					Util.createToast(ActivityDetailSparePart.this, "查询图片失败",
							3000).show();
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

	AsyncHttpCallBack cbStatusLog = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);

				if (obj.optInt("code") == 0) {
					GlobalData.listQuotationStatusLog = JSONUtils
							.fromJson(
									obj.optJSONArray("data").toString(),
									new TypeToken<ArrayList<EntityQuotationStatusLog>>() {
									});
					bindQuotationStatusLog();
				} else {
					Util.createToast(ActivityDetailSparePart.this, "查询失败", 3000)
							.show();
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

	AsyncHttpCallBack cbQuotationData = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);

				if (obj.optInt("code") == 0) {
					GlobalData.listSparePartQuotationData = JSONUtils
							.fromJson(
									obj.optJSONArray("data").toString(),
									new TypeToken<ArrayList<EntitySparePartQuotationData>>() {
									});
					bindQuotationData();
				} else {
					// Util.createToast(ActivityDetailSparePart.this, "查询失败",
					// 3000)
					// .show();
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

	AsyncHttpCallBack cbUpdateQuotationDataSetup = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {
 
			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);

				if (obj.optInt("code") == 0) {
					Util.createToast(ActivityDetailSparePart.this, "操作成功", 3000)
							.show();
					if (!quo_id.isEmpty()) {
						new SJECHttpHandler(cbQuotationData,
								ActivityDetailSparePart.this)
								.SparePart_GetSparePartQuotationDataByID(quo_id);

						new SJECHttpHandler(cbStatusLog,
								ActivityDetailSparePart.this)
								.SparePart_QuerySparePartStatusLogByID(quo_id);
					}
				} else {
					Util.createToast(ActivityDetailSparePart.this, "操作失败", 3000)
							.show();
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

}
