package com.sjec.rcms.data;

import android.os.Environment;

public class Constant {

	public static final String COLOR_LINK_BLUE = "#2319dc";

	public static final String APP_NAME = "SJEC_RCMS";

	public static final String ROOT_SRC = Environment
			.getExternalStorageDirectory().getAbsolutePath();
	public static final String SAVE_PATH = ROOT_SRC + "/sjecmobile/"; // 本地存放资源路径
	public static final String SDCARD_IMAGE_DIR = SAVE_PATH + "image/";// 图片存放
	public static final String DOWNLOAD_APK_NAME = SAVE_PATH + "sjec_rcms.apk";
	public static final String IMAGE_DIR_TAKE_PHOTO = SAVE_PATH + "image_take/";

	/**
	 * 注册成功
	 */
	public static final String ACTION_BROADCAST_REGIST_SUCCESS = "regist_success";

	/**
	 * 成功扫描到设备号
	 */
	public static final String ACTION_SCAN_DEVICE_NUM_SUCCESS = "scan_device_num";

	/**
	 * 成功扫描到设备号,手工维保
	 */
	public static final String ACTION_SCAN_DEVICE_NUM_SUCCESS_MAINTAIN = "scan_device_num_maintain";

	/**
	 * 刷新工单列表
	 */
	public static final String ACTION_REFRESH_WORKORDER_LIST = "refresh_workorder_list";

	/**
	 * 刷新工单详情
	 */
	public static final String ACTION_REFRESH_WORKORDER = "refresh_workorder";

	/**
	 * 刷新设备列表属性,已定位后,更新设备列表的 已定位图标
	 */
	public static final String ACTION_REFRESH_DEVICE_LIST_ATTR = "refresh_device_list_attr";

	/**
	 * 刷新待审核列表
	 */
	public static final String ACTION_REFRESH_PAUSE_CONFIRM_LIST = "refresh_confirm_list";

	/**
	 * 刷新协助人员列表
	 */
	public static final String ACTION_REFRESH_ASSIST_LIST = "refresh_assist_list";

	public static final String ACTION_ADD_PART_APPLY = "add_part_apply";

	public static final String ACTION_OPEN_ATTACH = "open_attach";

	public static final String ACTION_REFRESH_APPLY_INFO = "refresh_apply_info";

	/**
	 * 扫描类型1,扫描好了,跳转到信息采集页面
	 */
	public static final int SCAN_TYPE1 = 1;
	/**
	 * 扫描类型2,扫描完成,信息返回给手工维保页面
	 */
	public static final int SCAN_TYPE2 = 2;

	/**
	 * 扫描类型3，结束维保/抢修前，扫描二维码
	 */
	public static final int SCAN_TYPE3 = 3;

	/**
	 * 扫描类型4,协助人员扫码,关闭呼叫
	 */
	public static final int SCAN_TYPE4 = 4;

	/**
	 * 
	 * @类名称：EnumWorkorderType
	 * @包名：com.sjec.rcms.data
	 * @描述： 工单类型
	 * @创建人： dengzh
	 * @创建时间:2015-9-6 下午4:45:51
	 * @版本 V1.0
	 * @Copyright (c) 2015 by 苏州威博世网络科技有限公司.
	 */
	public class EnumWorkorderType {
		/**
		 * 工单类型,抢修
		 */
		public static final int TYPE_WORKORDER_REPAIR = 1;
		/**
		 * 工单类型,维保
		 */
		public static final int TYPE_WORKORDER_MAINTAIN = 2;

		/**
		 * 工单类型,协助工单
		 */
		public static final int TYPE_WORKORDER_ASSIST = 3;

		/**
		 * 工单类型,检查
		 */
		public static final int TYPE_WORKORDER_CHECK = 4;
	}

	/**
	 * 
	 * @类名称：EnumControlType
	 * @包名：com.sjec.rcms.data
	 * @描述： 生成手工单时选择的操作类型
	 * @创建人： dengzh
	 * @创建时间:2015-9-6 下午4:45:10
	 * @版本 V1.0
	 * @Copyright (c) 2015 by 苏州威博世网络科技有限公司.
	 */
	public class EnumControlType {
		/**
		 * 操作类型,正常保养
		 */
		public static final int CONTROL_TYPE_BY_1 = 1;
		/**
		 * 操作类型,大修改造
		 */
		public static final int CONTROL_TYPE_BY_2 = 2;
		// /**
		// * 操作类型,飞检
		// */
		// public static final int CONTROL_TYPE__BY_3 = 3;
		// /**
		// * 操作类型,年检
		// */
		// public static final int CONTROL_TYPE__BY_4 = 4;

		/**
		 * 操作类型,急修
		 */
		public static final int CONTROL_TYPE_1 = 1;
		/**
		 * 操作类型,关人
		 */
		public static final int CONTROL_TYPE_2 = 2;
		/**
		 * 操作类型,配件更换
		 */
		public static final int CONTROL_TYPE_3 = 3;

		/**
		 * 操作类型,飞检
		 */
		public static final int CONTROL_TYPE_JC_1 = 1;
		/**
		 * 操作类型,总部稽查
		 */
		public static final int CONTROL_TYPE_JC_2 = 2;
		/**
		 * 操作类型 分公司自检
		 */
		public static final int CONTROL_TYPE_JC_3 = 3;
		/**
		 * 操作类型 年检前自检
		 */
		public static final int CONTROL_TYPE_JC_4 = 4;
		/**
		 * 操作类型,年检
		 */
		public static final int CONTROL_TYPE_JC_5 = 5;
		/**
		 * 其他检查
		 */
		public static final int CONTROL_TYPE_JC_6 = 6;
	}

	/**
	 * 
	 * @类名称：EnumWorkorderStatus
	 * @包名：com.sjec.rcms.data
	 * @描述： 工单的状态
	 * @创建人： dengzh
	 * @创建时间:2015-9-6 下午4:46:38
	 * @版本 V1.0
	 * @Copyright (c) 2015 by 苏州威博世网络科技有限公司.
	 */
	public class EnumWorkorderStatus {
		/**
		 * 工单状态,无效
		 */
		public static final int STATUS_WORKORDER_INVALID = -1;

		/**
		 * 工单状态,未开始
		 */
		public static final int STATUS_WORKORDER_1 = 1;

		/**
		 * 工单状态,已开始
		 */
		public static final int STATUS_WORKORDER_2 = 2;

		/**
		 * 工单状态,已完成
		 */
		public static final int STATUS_WORKORDER_3 = 3;
	}

	/**
	 * 
	 * @类名称：EnumWorkorderPauseStatus
	 * @包名：com.sjec.rcms.data
	 * @描述： 执行状态
	 * @创建人： dengzh
	 * @创建时间:2016-1-7 下午3:33:21
	 * @版本 V1.0
	 * @Copyright (c) 2016 by 苏州威博世网络科技有限公司.
	 */
	public static class EnumWorkorderPauseStatus {

		/**
		 * 废弃
		 */
		public static final int PAUSE_STATUS_WORKORDER_INVALID = -1;

		/**
		 * 暂停
		 */
		public static final int PAUSE_STATUS_WORKORDER_PAUSE = 0;

		/**
		 * 正常
		 */
		public static final int PAUSE_STATUS_WORKORDER_CONTINUE = 1;

		/**
		 * 小故障快速关闭
		 */
		public static final int PAUSE_STATUS_WORKORDER_QUICKCLOSE = 999;

		/**
		 * 待审核
		 */
		public static final int PAUSE_STATUS_WORKORDER_WAIT_CHECK = 99;
		/**
		 * 延期
		 */
		public static final int PAUSE_STATUS_WORKORDER_MAINTAIN_CHANGEDATE = 1000;
		/**
		 * 特殊情况关闭
		 */
		public static final int PAUSE_STATUS_WORKORDER_MAINTAIN_QUICK_CLOSE = 1001;

		/**
		 * 审核通过
		 */
		public static final int PAUSE_CHECK_STATUS_PASS = 1;

		/**
		 * 审核没通过
		 */
		public static final int PAUSE_CHECK_STATUS_REJECT = -1;

		public static String GetAdminCheckName(int status) {
			String name = "";
			switch (status) {
			case PAUSE_CHECK_STATUS_PASS:
				name = "通过";
				break;
			case PAUSE_CHECK_STATUS_REJECT:
				name = "否决";
				break;
			case PAUSE_STATUS_WORKORDER_WAIT_CHECK:
				name = "待审核";
				break;

			default:
				name = "";
			}
			return name;
		}

		public static String GetName(int status) {
			String name = "";
			switch (status) {
			case PAUSE_STATUS_WORKORDER_INVALID:
				name = "作废";
				break;
			case PAUSE_STATUS_WORKORDER_PAUSE:
				name = "暂停";
				break;
			case PAUSE_STATUS_WORKORDER_CONTINUE:
				name = "正常";
				break;
			case PAUSE_STATUS_WORKORDER_QUICKCLOSE:
				name = "小故障快速关闭";
				break;
			case PAUSE_STATUS_WORKORDER_WAIT_CHECK:
				name = "待审核";
				break;
			case PAUSE_STATUS_WORKORDER_MAINTAIN_CHANGEDATE:
				name = "保养延期";
				break;
			case PAUSE_STATUS_WORKORDER_MAINTAIN_QUICK_CLOSE:
				name = "特殊情况关闭";
				break;
			default:
				name = "";
			}
			return name;
		}

	}

	/**
	 * 
	 * @类名称：EnumAssistWorkerStatus
	 * @包名：com.sjec.rcms.data
	 * @描述： 协助人员的状态
	 * @创建人： dengzh
	 * @创建时间:2016-1-7 上午9:54:33
	 * @版本 V1.0
	 * @Copyright (c) 2016 by 苏州威博世网络科技有限公司.
	 */
	public static class EnumAssistWorkerStatus {

		/**
		 * 待确认
		 */
		public static final int ASSIST_STATUS_UNCONFIRM = 1;// 待确认

		/**
		 * 已确认
		 */
		public static final int ASSIST_STATUS_CONFIRMED = 2;// 已确认
		/**
		 * 已开始
		 */
		public static final int ASSIST_STATUS_BEGINED = 3;// 已开始
		/**
		 * 已关闭
		 */
		public static final int ASSIST_STATUS_CLOSED = 4;// 已关闭

		public static String GetName(int status) {
			String name = "";
			switch (status) {
			case ASSIST_STATUS_UNCONFIRM:
				name = "待确认";
				break;
			case ASSIST_STATUS_CONFIRMED:
				name = "已确认";
				break;
			case ASSIST_STATUS_BEGINED:
				name = "已开始";
				break;
			case ASSIST_STATUS_CLOSED:
				name = "已关闭";
				break;
			default:
				name = "";
			}
			return name;
		}

	}

	public static class EnumMaintainType {
		public final static int MAINTAIN_TYPE_HALF_MONTH = 1;
		public final static int MAINTAIN_TYPE_SEASON = 2;
		public final static int MAINTAIN_TYPE_HALF_YEAR = 3;
		public final static int MAINTAIN_TYPE_YEAR = 4;

		public static String GetName(int status) {
			String name = "";
			switch (status) {
			case MAINTAIN_TYPE_HALF_MONTH:
				name = "半月度";
				break;
			case MAINTAIN_TYPE_SEASON:
				name = "季度";
				break;
			case MAINTAIN_TYPE_HALF_YEAR:
				name = "半年度";
				break;
			case MAINTAIN_TYPE_YEAR:
				name = "年度";
				break;
			default:
				name = "-";
			}
			return name;
		}
	}
	// 0:暂停,1:正常,-1废弃,99:待审核
	// ADMINCHECK 99待审核,-1:不通过,1:通过
}
