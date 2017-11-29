package com.sjec.rcms.data;

import java.util.ArrayList;

import android.content.Context;

import com.sjec.rcms.activity.login.ActivityLogin;
import com.sjec.rcms.activity.main.ActivityMain;
import com.sjec.rcms.dao.EntityAssistWorker;
import com.sjec.rcms.dao.EntityBigCustomer;
import com.sjec.rcms.dao.EntityCheck;
import com.sjec.rcms.dao.EntityCompanyWorker;
import com.sjec.rcms.dao.EntityDevice;
import com.sjec.rcms.dao.EntityFactoryCode;
import com.sjec.rcms.dao.EntityKnowledgeType;
import com.sjec.rcms.dao.EntityMaintain;
import com.sjec.rcms.dao.EntityPauseConfirm;
import com.sjec.rcms.dao.EntityQuotationStatusLog;
import com.sjec.rcms.dao.EntityRepair;
import com.sjec.rcms.dao.EntitySparePartApply;
import com.sjec.rcms.dao.EntitySparePartApplyData;
import com.sjec.rcms.dao.EntitySparePartApplyPic;
import com.sjec.rcms.dao.EntitySparePartQuotation;
import com.sjec.rcms.dao.EntitySparePartQuotationData;
import com.sjec.rcms.dao.EntityUser;
import com.sjec.rcms.dao.EntityUserCompany;
import com.sjec.rcms.dao.EntityUserStaff;
import com.sjec.rcms.dao.EntityVillage;
import com.sjec.rcms.dao.EntityWorkorder;
import com.websharputil.common.ConvertUtil;
import com.websharputil.common.PrefUtil;
import com.websharputil.common.Util;
import com.websharputil.json.JSONUtils;

public class GlobalData {

	public static void clear() {
		curUser = null;
		userCompany = null;
		userStaff = null;
		curDevice = null;
		curWorkorder = null;
		curRepair = null;
		curMaintain = null;
		curPauseConfirm = null;
		listKnowledgeType.clear();
		listDevice.clear();
		listVillage.clear();
		listWorkorder.clear();
		listFactoryCode.clear();
		listPauseConfirm.clear();
		listCompanyWorker.clear();
		listAsssitWorker.clear();
		listBigCustomer.clear();
		System.gc();
	}

	/**
	 * 当前登录用户
	 */
	public static EntityUser curUser = null;
	public static EntityUserCompany userCompany = null;
	public static EntityUserStaff userStaff = null;

	public static EntityUser GetCurUser(Context ctx) {
		if (curUser == null) {
			String prefUser = PrefUtil.getPref(ctx, "user", "");
			if (!prefUser.isEmpty()) {

				GlobalData.curUser = JSONUtils.fromJson(prefUser,
						EntityUser.class);

				try {
					GlobalData.WorkerStatus = ConvertUtil.ParsetStringToInt32(
							GlobalData.curUser.Description
									.substring(GlobalData.curUser.Description
											.trim().length() - 1), 0);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
		return curUser;
	}

	public static int WorkerStatus = 0;

	/**
	 * 知识库类别列表
	 */
	public static ArrayList<EntityKnowledgeType> listKnowledgeType = new ArrayList<EntityKnowledgeType>();

	/**
	 * 用户负责维保的电梯列表
	 */
	public static ArrayList<EntityDevice> listDevice = new ArrayList<EntityDevice>();

	/**
	 * 当前选中的电梯
	 */
	public static EntityDevice curDevice = new EntityDevice();

	/**
	 * 小区列表
	 */
	public static ArrayList<EntityVillage> listVillage = new ArrayList<EntityVillage>();

	/**
	 * 工单列表
	 */
	public static ArrayList<EntityWorkorder> listWorkorder = new ArrayList<EntityWorkorder>();

	/**
	 * 正在操作的工单
	 */
	public static EntityWorkorder curWorkorder = new EntityWorkorder();
	public static EntityRepair curRepair = new EntityRepair();
	public static EntityMaintain curMaintain = new EntityMaintain();
	public static EntityCheck curCheck = new EntityCheck();

	public static ArrayList<EntityFactoryCode> listFactoryCode = new ArrayList<EntityFactoryCode>();

	/**
	 * 审核工单的列表
	 */
	public static ArrayList<EntityPauseConfirm> listPauseConfirm = new ArrayList<EntityPauseConfirm>();
	public static EntityPauseConfirm curPauseConfirm = new EntityPauseConfirm();

	public static ArrayList<EntityCompanyWorker> listCompanyWorker = new ArrayList<EntityCompanyWorker>();
	public static ArrayList<EntityAssistWorker> listAsssitWorker = new ArrayList<EntityAssistWorker>();

	/*********************************************
	 * 备品备件
	 *********************************************/
	public static ArrayList<EntitySparePartApply> listSparePart = new ArrayList<EntitySparePartApply>();
	public static EntitySparePartApply curApply = null;
	public static EntitySparePartQuotation curSparePartQuotation = null;
	public static ArrayList<EntitySparePartQuotationData> listSparePartQuotationData =new ArrayList<EntitySparePartQuotationData>();
	public static ArrayList<EntitySparePartApplyData> listSparePartData = new ArrayList<EntitySparePartApplyData>();
	public static ArrayList<EntitySparePartApplyPic> listSparePartPic = new ArrayList<EntitySparePartApplyPic>();
	public static ArrayList<EntityQuotationStatusLog> listQuotationStatusLog = new ArrayList<EntityQuotationStatusLog>();
	public static ArrayList<EntityBigCustomer> listBigCustomer = new ArrayList<EntityBigCustomer>();

}
