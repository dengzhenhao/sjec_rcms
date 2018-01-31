package com.sjec.rcms.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URLEncodedUtils;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Base64;

import com.loopj.android.http.RequestParams;
import com.sjec.rcms.BuildConfig;
import com.sjec.rcms.activity.login.ActivityLogin;
import com.sjec.rcms.activity.main.ActivityMain;
import com.sjec.rcms.dao.EntityUser;
import com.sjec.rcms.data.GlobalData;
import com.websharputil.code.DescUtil;
import com.websharputil.code.CodeUtil;
import com.websharputil.common.AppData;
import com.websharputil.common.ConvertUtil;
import com.websharputil.common.LogUtil;
import com.websharputil.common.PrefUtil;
import com.websharputil.common.Util;
import com.websharputil.date.DateUtil;
import com.websharputil.json.JSONUtils;

public class SJECHttpHandler extends BaseHttpHandler {

	private static final String CLIENT = "android";
   
	/**
	 * 接口中加密的密钥
	 */
	public final static String ENCRYPT_KEY = "SJEC_APP_Ver2015_API";

	/**
	 * 服务器地址
	 */

//	public static final String BASE_URL = "http://192.168.0.20:9797";
	 public static final String BASE_URL = "http://58.240.164.146:9999"; 
//	 public static final String BASE_URL = "http://58.240.164.146:9998";
	// public static final String BASE_URL ="http://sjecmobile.websharp.com.cn";

	// public static final String BASE_URL_SPARE_PART =
	// "http://192.168.0.16:3309";
	public static final String BASE_URL_SPARE_PART = "http://58.240.164.146:3303";
	// public static final String BASE_URL_SPARE_PART =
	// "http://58.240.164.146:7700";

	// f615f11a-51b6-408a-862e-ca215d0433d9

	/**
	 * 检查更新
	 */
	public static final String URL_CHECK_VERSION = "/handlers/version/GetLastestVersionHandler.ashx";

	/**
	 * 登录接口
	 */
	public static final String URL_LOGIN = "/handlers/user/LoginHandler.ashx";

	/**
	 * 获取用户的基本信息，如所在分公司等
	 */
	public static final String URL_GetStaffInfo = "/handlers/user/GetStaffInfoHandler.ashx";

	/**
	 * 修改密码
	 */
	public static final String URL_CHANGE_PWD = "/handlers/user/ChangePwdHandler.ashx";

	/**
	 * 注册接口
	 */
	public static final String URL_REGIST = "/handlers/user/RegisterHandler.ashx";

	/**
	 * 更新百度推送的userid
	 */
	public static final String URL_UPDATA_BAIDU_PUSH_ID = "/handlers/baidu/UserAccountSyncHandler.ashx";

	/**
	 * 获取验证码
	 */
	public static final String URL_GET_VERIFY_CODE = "/handlers/user/RequestCodeHandler.ashx";

	/******************** 知识库接口 ********************/

	/**
	 * 查询知识库的分类
	 */
	public static final String URL_QUERY_KNOWLEDGE_TYPE = "/handlers/knowledge/QueryKnowledgeType.ashx";
	/**
	 * 知识库的列表,网页
	 */
	public static final String URL_KNOWLEDGE_LIST = BASE_URL
			+ "/client/category.aspx?classID=%s&tag=%s&keyword=%s";
	/**
	 * 知识库的详情,网页
	 */
	public static final String URL_KNOWLEDGE_DETAIL = BASE_URL
			+ "/client/article.aspx?";

	/******************* 信息采集 ***********************/
	/**
	 * 用户所维保的电梯信息列表
	 */
	public static final String URL_DEVICELIST_BY_USER_ID = "/handlers/device/QueryDeviceListByUserID.ashx";

	/**
	 * 更新电梯所在经纬度
	 */
	public static final String URL_UPDATE_DEVICE_LOCATION = "/handlers/device/UpdateDeviceLocation.ashx";

	/**
	 * 根据电梯设备号查找单个电梯
	 */
	public static final String URL_QUERY_DEVICE_BY_DEVICE_NUM = "/handlers/device/QueryDeviceByDeviceNum.ashx";

	/**
	 * 根据工单号查询单个电梯的信息
	 */
	public static final String URL_QUERY_DEVICE_BY_WORKORDER_ID = "/handlers/device/QueryDeviceByWorkorderID.ashx";

	/******************* 电梯维保 ***********************/
	/**
	 * 获取用户维保的小区
	 */
	public static final String URL_USER_VILLAGE = "/handlers/user/GetUserVillage.ashx";

	/**
	 * 获取用户工单
	 */
	public static final String URL_USER_WORKORDER = "/handlers/workorder/QueryWorkOrderByUserID.ashx";

	/**
	 * 添加手工单
	 */
	public static final String URL_WORKORDER_ADD = "/handlers/workorder/AddWorkOrderManual.ashx";

	/**
	 * 获取单个工单
	 */
	public static final String URL_WORKORER_BY_ID = "/handlers/workorder/QueryWorkorderByID.ashx";

	/**
	 * 获取工单的推送记录
	 */
	public static final String URL_WORKORER_PUSH_LOG_BY_ID = "/handlers/workorder/QueryWorkorderPushLogByID.ashx";

	/**
	 * 更新工单的状态
	 */
	public static final String URL_UPDATE_WORKORDER_STATUS = "/handlers/workorder/UpdateWorkorderStatus.ashx";
	public static final String URL_REQUEST_WORKORDER_PAUSE_STATUS = "/handlers/workorder/RequestWorkorderPauseStatus.ashx";
	public static final String URL_CONFIRM_WORKORDER_PAUSE_STATUS = "/handlers/workorder/ConfirmWorkorderPauseStatus.ashx";

	public static final String URL_UPDATE_WORKORDER_RECEIVER = "/handlers/workorder/UpdateWorkorderReceiver.ashx";

	/**
	 * 用户的待审核工单列表
	 */
	public static final String URL_QueryPauseConfirmListByUserID = "/handlers/workorder/QueryPauseConfirmListByUserID.ashx";

	/**
	 * 处于暂停的工单
	 */
	public static final String URL_QueryWorkOrderPause = "/handlers/workorder/QueryWorkOrderPause.ashx";

	/**
	 * 某个工单的请求/审核记录
	 */
	public static final String URL_QueryWorkOrderPauseLog = "/handlers/workorder/QueryWorkOrderPauseLog.ashx";

	public static final String URL_QueryWorkOrderKunren = "/handlers/workorder/QueryWorkOrderKunRen.ashx";

	/**
	 * 待审核工单列表数量
	 */
	public static final String URL_QueryPauseConfirmListCountByUserID = "/handlers/workorder/QueryPauseConfirmListCountByUserID.ashx";

	/**
	 * 选择本公司所有的维保人员(已经在协助人员名单中的除外)
	 */
	public static final String URL_GetCompanyWorkerList = "/handlers/workorder/GetCompanyWorkerList.ashx";

	/**
	 * 为某个工单添加协助人员
	 */
	public static final String URL_AddWorkOrderAssistUsers = "/handlers/workorder/AddWorkOrderAssistUsers.ashx";

	/**
	 * 查找工单的协助人员
	 */
	public static final String URL_GetCompanyAssistWorkerList = "/handlers/workorder/GetCompanyAssistWorkerList.ashx";

	/**
	 * 删除选中的协助人员
	 */
	public static final String URL_DeleteWorkOrderAssistUsers = "/handlers/workorder/DeleteWorkOrderAssistUsers.ashx";

	/**
	 * 更新协助人员的状态
	 */
	public static final String URL_UpdateAssistUserStatus = "/handlers/workorder/UpdateAssistUserStatus.ashx";

	/**
	 * 获取电梯厂家代码的列表
	 */
	public static final String URL_FACTORY_CODE = "/handlers/user/GetFactoryCode.ashx";

	/**
	 * 上传图片
	 */
	public static final String URL_UPDATE_WORKORDER_PICS = "/handlers/workorder/UploadWorkorderPics.ashx";
	/**
	 * 查询工单中上传的图片
	 */
	public static final String URL_QUERY_WORKORDER_PICS = "/handlers/workorder/QueryWorkorderPics.ashx";

	public SJECHttpHandler(AsyncHttpCallBack callback, Context context) {
		super(callback, context);

		if (GlobalData.curUser == null) {
			String prefUser = PrefUtil.getPref(context, "user", "");
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
	}

	/**
	 * 得到签名字符串
	 * 
	 * @param params
	 * @return
	 */
	public static String GetSignature(String... params) {
		StringBuilder sb = new StringBuilder();
		if (params != null && params.length != 0) {
			for (int i = 0; i < params.length; i++) {
				// if (params[i].trim().length() < 100) {
				sb = sb.append(params[i]);
				// }
			}
		}
		sb = sb.append(CLIENT).append(ENCRYPT_KEY);
		String str = CodeUtil.MD5(sb.toString()).substring(8, 24);
		return str;
	}

	/**
	 * 查询知识库的分类
	 * 
	 * @param user_ID
	 */
	public void getKnowledgeType() {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		params.add("signature", GetSignature(GlobalData.curUser.InnerID));
		new AsyncHttpUtil().get(BASE_URL + URL_QUERY_KNOWLEDGE_TYPE, params,
				handler);
	}

	/**
	 * 登录
	 * 
	 * @param userName
	 *            登录名
	 * @param password
	 *            密码
	 * @throws Exception
	 */
	public void login(String userName, String password) throws Exception {
		RequestParams params = new RequestParams();
		params.add("telephone", URLEncoder.encode(userName));
		params.add("password", DescUtil.toHexString(DescUtil.encrypt(password)));
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		params.add(
				"signature",
				GetSignature(userName,
						DescUtil.toHexString(DescUtil.encrypt(password))));
		new AsyncHttpUtil().post(BASE_URL + URL_LOGIN, params, handler);
	}

	/**
	 * 修改密码
	 * 
	 * @param password
	 * @param new_password
	 * @throws Exception
	 */
	public void changePwd(String password, String new_password)
			throws Exception {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.GetCurUser(mContext).InnerID);
		params.add("password", DescUtil.toHexString(DescUtil.encrypt(password)));
		params.add("new_password",
				DescUtil.toHexString(DescUtil.encrypt(new_password)));
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		params.add(
				"signature",
				GetSignature(GlobalData.GetCurUser(mContext).InnerID,
						DescUtil.toHexString(DescUtil.encrypt(password)),
						DescUtil.toHexString(DescUtil.encrypt(new_password))));
		new AsyncHttpUtil().post(BASE_URL + URL_CHANGE_PWD, params, handler);
	}

	/**
	 * 获取短信验证码
	 * 
	 * @param telephone
	 */
	public void getVerifyCode(String telephone) {
		RequestParams params = new RequestParams();
		params.add("telephone", telephone);
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		params.add("signature", GetSignature(telephone));
		new AsyncHttpUtil()
				.get(BASE_URL + URL_GET_VERIFY_CODE, params, handler);
	}

	/**
	 * 注册用户
	 * 
	 * @param username
	 * @param telephone
	 * @param password
	 * @param verify_code
	 * @throws Exception
	 */
	public void regist(String username, String telephone, String password,
			String verify_code) throws Exception {
		RequestParams params = new RequestParams();
		params.add("username", username);
		params.add("telephone", telephone);
		params.add("password", DescUtil.toHexString(DescUtil.encrypt(password)));
		params.add("verify_code", verify_code);
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		params.add(
				"signature",
				GetSignature(username, telephone,
						DescUtil.toHexString(DescUtil.encrypt(password)),
						verify_code));
		new AsyncHttpUtil().get(BASE_URL + URL_REGIST, params, handler);
	}

	/**
	 * 获取用户负责维保的电梯列表
	 * 
	 * @param user_id
	 */
	public void getDeviceListByUserID(String page, String size, String keyword) {
		RequestParams params = new RequestParams();
		params.add("page", page);
		params.add("pageSize", size);
		params.add("keyword", URLEncoder.encode(keyword));
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		params.add("signature",
				GetSignature(page, size, GlobalData.curUser.InnerID));
		new AsyncHttpUtil().get(BASE_URL + URL_DEVICELIST_BY_USER_ID, params,
				handler);
	}

	/**
	 * 查找单个的电梯
	 * 
	 * @param user_id
	 * @param company_num
	 * @param device_num
	 */
	public void getDeviceByDeviceNum(String company_num, String device_num) {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.GetCurUser(mContext).InnerID);
		params.add("company_num", company_num);
		params.add("device_num", device_num);
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		params.add(
				"signature",
				GetSignature(GlobalData.GetCurUser(mContext).InnerID,
						company_num, device_num));
		new AsyncHttpUtil().get(BASE_URL + URL_QUERY_DEVICE_BY_DEVICE_NUM,
				params, handler);
	}

	/**
	 * 根据工单号查询电梯信息
	 * 
	 * @param user_id
	 * @param workorderID
	 */
	public void getDeviceByWorkorderID(String workorderID) {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("workorder_id", workorderID);
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		params.add("signature",
				GetSignature(GlobalData.curUser.InnerID, workorderID));
		new AsyncHttpUtil().get(BASE_URL + URL_QUERY_DEVICE_BY_WORKORDER_ID,
				params, handler);
	}

	/**
	 * 更新电梯的经纬度
	 * 
	 * @param user_id
	 * @param device_id
	 * @param latitude
	 * @param longitude
	 */
	public void updateDeviceLocation(String device_id, String latitude,
			String longitude) {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("device_id", device_id);
		params.add("latitude", latitude);
		params.add("longitude", longitude);
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		params.add(
				"signature",
				GetSignature(GlobalData.curUser.InnerID, device_id, latitude,
						longitude));
		new AsyncHttpUtil().get(BASE_URL + URL_UPDATE_DEVICE_LOCATION, params,
				handler);
	}

	/**
	 * 获取用户所维保的小区
	 * 
	 * @param user_id
	 */
	public void getUserVillage() {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		params.add("signature", GetSignature(GlobalData.curUser.InnerID));
		new AsyncHttpUtil().get(BASE_URL + URL_USER_VILLAGE, params, handler);
	}

	/**
	 * 获取电梯厂家的代码列表
	 * 
	 * @param user_id
	 */
	public void getFactoryCode() {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		params.add("signature", GetSignature(GlobalData.curUser.InnerID));
		new AsyncHttpUtil().get(BASE_URL + URL_FACTORY_CODE, params, handler);
	}

	/**
	 * 获取用户的工单
	 * 
	 * @param user_id
	 * @param village_id
	 * @param keyword
	 * @throws Exception
	 */
	public void getUserWorkorder(String page, String size, String village_id,
			String workorderType, String workorderSource,
			String workorderStatus, String keyword,String filter_pic_exists,String provision_maintain_time_filter) {
		RequestParams params = new RequestParams();
		params.add("page", page);
		params.add("pageSize", size);
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("village_id", village_id);
		params.add("workorder_type", workorderType);
		params.add("workorder_source", workorderSource);
		params.add("workorder_status", workorderStatus);
		params.add("filter_pic_exists", filter_pic_exists);
		params.add("filter_3days", provision_maintain_time_filter);
		try {
			params.add("keyword", URLEncoder.encode(keyword));
		} catch (Exception e) {
			e.printStackTrace();
		}
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		params.add(
				"signature",
				GetSignature(page, size, GlobalData.curUser.InnerID,
						village_id, workorderType, workorderSource,
						workorderStatus, keyword));
		new AsyncHttpUtil().get(BASE_URL + URL_USER_WORKORDER, params, handler);
	}

	/**
	 * 添车手工单
	 * 
	 * @param user_id
	 * @param company_num
	 * @param device_num
	 * @param order_type
	 *            1:抢修,2:维保
	 */
	public void addWorkorder(String company_num, String device_num,
			String order_type, String control_type, String error_code,
			String error_desc) {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("company_num", company_num);
		params.add("device_num", device_num);
		params.add("order_type", order_type);
		params.add("control_type", control_type);

		try {
			params.add("error_code", URLEncoder.encode(error_code));
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			params.add("error_desc", URLEncoder.encode(error_desc));
		} catch (Exception e) {
			e.printStackTrace();
		}

		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		params.add(
				"signature",
				GetSignature(GlobalData.curUser.InnerID, company_num,
						device_num, order_type, control_type));
		new AsyncHttpUtil().get(BASE_URL + URL_WORKORDER_ADD, params, handler);
	}

	/**
	 * 得到单个工单信息
	 * 
	 * @param user_id
	 * @param workorder_id
	 */
	public void getWorkorderByID(String workorder_id) {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("workorder_id", workorder_id);
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		params.add("signature",
				GetSignature(GlobalData.curUser.InnerID, workorder_id));
		new AsyncHttpUtil().get(BASE_URL + URL_WORKORER_BY_ID, params, handler);
	}

	/**
	 * 获取工单的推送记录
	 * 
	 * @param user_id
	 * @param workorder_id
	 */
	public void getWorkorderPushLogByID(String workorder_id) {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("workorder_id", workorder_id);
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		params.add("signature",
				GetSignature(GlobalData.curUser.InnerID, workorder_id));
		new AsyncHttpUtil().get(BASE_URL + URL_WORKORER_PUSH_LOG_BY_ID, params,
				handler);
	}

	/**
	 * 更新工单的状态
	 * 
	 * @param user_id
	 * @param workorder_id
	 * @param status
	 */
	// public void updateWorkorderStatus(String workorder_id, String status,
	// String latitude, String longitude, String desc,String resultType) {
	// RequestParams params = new RequestParams();
	// params.add("user_id", GlobalData.curUser.InnerID);
	// params.add("workorder_id", workorder_id);
	// params.add("status", status);
	// params.add("latitude", latitude);
	// params.add("longitude", longitude);
	// try {
	// params.add("desc", URLEncoder.encode(desc));
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// params.add("resultType", resultType);
	// params.add("client", CLIENT);
	// params.add(
	// "signature",
	// GetSignature(GlobalData.curUser.InnerID, workorder_id, status,
	// latitude, longitude, desc));
	// new AsyncHttpUtil().get(BASE_URL + URL_UPDATE_WORKORDER_STATUS, params,
	// handler);
	// }

	public void updateWorkorderStatusForMaintain(String workorder_id,
			String status, String latitude, String longitude, String desc,
			String resultType, boolean isSync) {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("workorder_id", workorder_id);
		params.add("status", status);
		params.add("latitude", latitude);
		params.add("longitude", longitude);
		try {
			params.add("desc", URLEncoder.encode(desc));
		} catch (Exception e) {
			e.printStackTrace();
		}
		params.add("resultType", resultType);
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		params.add(
				"signature",
				GetSignature(GlobalData.curUser.InnerID, workorder_id, status,
						latitude, longitude, desc));
		if (isSync) {
			new AsyncHttpUtil().getSync(BASE_URL + URL_UPDATE_WORKORDER_STATUS,
					params, handler);
		} else {
			new AsyncHttpUtil().get(BASE_URL + URL_UPDATE_WORKORDER_STATUS,
					params, handler);
		}
	}

	/**
	 * 
	 * @param workorder_id
	 * @param status
	 * @param latitude
	 * @param longitude
	 * @param desc
	 * @param startType
	 */
	public void updateWorkorderStatusForRepair(String workorder_id,
			String status, String latitude, String longitude, String desc,
			String startType, String resultType) {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("workorder_id", workorder_id);
		params.add("status", status);
		params.add("latitude", latitude);
		params.add("longitude", longitude);
		params.add("start_type", startType);
		try {
			params.add("desc", URLEncoder.encode(desc));
		} catch (Exception e) {
			e.printStackTrace();
		}
		params.add("resultType", resultType);
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		params.add(
				"signature",
				GetSignature(GlobalData.curUser.InnerID, workorder_id, status,
						latitude, longitude, desc));
		new AsyncHttpUtil().get(BASE_URL + URL_UPDATE_WORKORDER_STATUS, params,
				handler);
	}

	public void updateWorkorderStatusForCheck(String workorder_id,
			String status, String latitude, String longitude, String desc,
			String startType, String isNeedAbarbeitung, String resultType,
			boolean isSync) {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("workorder_id", workorder_id);
		params.add("status", status);
		params.add("latitude", latitude);
		params.add("longitude", longitude);
		params.add("start_type", startType);
		params.add("isneedabarbeitung", isNeedAbarbeitung);
		try {
			params.add("desc", URLEncoder.encode(desc));
		} catch (Exception e) {
			e.printStackTrace();
		}
		params.add("resultType", resultType);
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		params.add(
				"signature",
				GetSignature(GlobalData.curUser.InnerID, workorder_id, status,
						latitude, longitude, desc));
		if (isSync) {
			new AsyncHttpUtil().getSync(BASE_URL + URL_UPDATE_WORKORDER_STATUS,
					params, handler);
		} else {
			new AsyncHttpUtil().get(BASE_URL + URL_UPDATE_WORKORDER_STATUS,
					params, handler);
		}
	}

	/**
	 * 更新工单的暂停、作废、继续工作的状态
	 * 
	 * @param user_id
	 * @param workorder_id
	 * @param status
	 */
	public void requestWorkorderPauseStatus(String workorder_id, String status,
			String init, String reason, String caller_name,
			String caller_telephone) {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("workorder_id", workorder_id);
		params.add("status", status);
		params.add("init", init);
		params.add("reason", URLEncoder.encode(reason));
		params.add("caller_name", URLEncoder.encode(caller_name));
		params.add("caller_telephone", URLEncoder.encode(caller_telephone));
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		params.add(
				"signature",
				GetSignature(GlobalData.curUser.InnerID, workorder_id, status,
						init, reason));
		new AsyncHttpUtil().get(BASE_URL + URL_REQUEST_WORKORDER_PAUSE_STATUS,
				params, handler);
	}

	/**
	 * 更新用户的审核结果
	 * 
	 * @param user_id
	 * @param pause_id
	 * @param check_status
	 *            1:通过,-1:否决
	 */
	public void confirmWorkorderPauseStatus(String pause_id, String check_status) {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("pause_id", pause_id);
		params.add("check_status", check_status);
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		params.add(
				"signature",
				GetSignature(GlobalData.curUser.InnerID, pause_id, check_status));
		new AsyncHttpUtil().get(BASE_URL + URL_CONFIRM_WORKORDER_PAUSE_STATUS,
				params, handler);
	}

	/**
	 * 更新工单的受理人
	 * 
	 * @param user_id
	 * @param workorder_id
	 */
	public void updateWorkorderReceiver(String workorder_id, String maintainType) {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("workorder_id", workorder_id);
		params.add("maintainType", maintainType);
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		params.add("signature",
				GetSignature(GlobalData.curUser.InnerID, workorder_id));
		new AsyncHttpUtil().get(BASE_URL + URL_UPDATE_WORKORDER_RECEIVER,
				params, handler);
	}

	/**
	 * 更新百度推送的信息
	 * 
	 * @param user_id
	 * @param baidu_userID
	 * @param baidu_channelID
	 * @param baidu_tag
	 */
	public void UpdatePushInfo(String baidu_userID, String baidu_channelID,
			String baidu_tag) {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("baiduUserId", baidu_userID);
		params.add("baiduChannelId", baidu_channelID);
		params.add("baiduTag", baidu_tag);
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		params.add(
				"signature",
				GetSignature(GlobalData.curUser.InnerID, baidu_userID,
						baidu_channelID, baidu_tag));
		new AsyncHttpUtil().get(BASE_URL + URL_UPDATA_BAIDU_PUSH_ID, params,
				handler);
	}

	/**
	 * 检查更新
	 */
	public void CheckVersion() {
		RequestParams params = new RequestParams();
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		params.add("signature", GetSignature());
		new AsyncHttpUtil().get(BASE_URL + URL_CHECK_VERSION, params, handler);
	}

	/**
	 * 查询用户待审核的列表
	 * 
	 * @param page
	 * @param size
	 * @param user_id
	 */
	public void getPauseConfirmListByUserID(String page, String size) {
		RequestParams params = new RequestParams();
		params.add("page", page);
		params.add("pageSize", size);
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		params.add("signature",
				GetSignature(page, size, GlobalData.curUser.InnerID));
		new AsyncHttpUtil().get(BASE_URL + URL_QueryPauseConfirmListByUserID,
				params, handler);
	}

	/**
	 * 查询待审核工单的数量
	 * 
	 * @param page
	 * @param size
	 * @param user_id
	 */
	public void getPauseConfirmListCountByUserID() {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		params.add("signature", GetSignature(GlobalData.curUser.InnerID));
		new AsyncHttpUtil().get(BASE_URL
				+ URL_QueryPauseConfirmListCountByUserID, params, handler);
	}

	/**
	 * 查询公司的维保人员(已经在协助人员名单中的除外)
	 */
	public void getCompanyWorkerList(String workorder_id, String keyword) {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("workorder_id", workorder_id);
		params.add("keyword", URLEncoder.encode(keyword));
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		params.add("signature",
				GetSignature(GlobalData.curUser.InnerID, workorder_id));
		new AsyncHttpUtil().get(BASE_URL + URL_GetCompanyWorkerList, params,
				handler);
	}

	/**
	 * 为工单添加协助人员
	 * 
	 * @param workorder_id
	 * @param assist_user_id
	 *            多个ID用|分隔
	 */
	public void addWorkOrderAssistUsers(String workorder_id, String assist_users) {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("workorder_id", workorder_id);
		params.add("assist_users", assist_users);
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		params.add(
				"signature",
				GetSignature(GlobalData.curUser.InnerID, workorder_id,
						assist_users));
		new AsyncHttpUtil().get(BASE_URL + URL_AddWorkOrderAssistUsers, params,
				handler);
	}

	/**
	 * 查询工单的协助人员
	 * 
	 * @param workorder_id
	 */
	public void getCompanyAssistWorkerList(String workorder_id) {
		RequestParams params = new RequestParams();
		params.add("workorder_id", workorder_id);
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		params.add("signature", GetSignature(workorder_id));
		new AsyncHttpUtil().get(BASE_URL + URL_GetCompanyAssistWorkerList,
				params, handler);
	}

	/**
	 * 删除选中的协助人员
	 * 
	 * @param workorder_id
	 * @param assist_user_id
	 */
	public void deleteWorkOrderAssistUsers(String workorder_id,
			String assist_user_id) {
		RequestParams params = new RequestParams();
		params.add("workorder_id", workorder_id);
		params.add("assist_user_id", assist_user_id);
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		params.add("signature", GetSignature(workorder_id, assist_user_id));
		new AsyncHttpUtil().get(BASE_URL + URL_DeleteWorkOrderAssistUsers,
				params, handler);
	}

	/**
	 * 更新协助人员的状态
	 * 
	 * @param user_id
	 * @param workorder_id
	 * @param status
	 */
	public void updateAssistUserStatus(String user_id, String workorder_id,
			int status, String lat, String lng) {
		RequestParams params = new RequestParams();
		params.add("user_id", user_id);
		params.add("workorder_id", workorder_id);
		params.add("status", status + "");
		params.add("lat", lat);
		params.add("lng", lng);
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		params.add("signature",
				GetSignature(user_id, workorder_id, status + "", lat, lng));
		new AsyncHttpUtil().get(BASE_URL + URL_UpdateAssistUserStatus, params,
				handler);
	}

	/**
	 * 处于暂停状态的工单
	 */
	public void queryWorkOrderPause() {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		params.add("signature", GetSignature(GlobalData.curUser.InnerID));
		new AsyncHttpUtil().get(BASE_URL + URL_QueryWorkOrderPause, params,
				handler);
	}

	/**
	 * 某个工单的请求/审核记录
	 * 
	 * @param workorderID
	 */
	public void queryWorkOrderPauseLog(String workorderID) {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("workorder_id", workorderID);
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		params.add("signature",
				GetSignature(GlobalData.curUser.InnerID, workorderID));
		new AsyncHttpUtil().get(BASE_URL + URL_QueryWorkOrderPauseLog, params,
				handler);
	}

	public void queryWorkOrderKunren() {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		params.add("signature", GetSignature(GlobalData.curUser.InnerID));
		new AsyncHttpUtil().get(BASE_URL + URL_QueryWorkOrderKunren, params,
				handler);
	}

	public void updateWorkorderPics(String workorder_id, String json_img) {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("workorder_id", workorder_id);
		params.add("json_img", json_img);
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		params.add("signature",
				GetSignature(GlobalData.curUser.InnerID, workorder_id));
		new AsyncHttpUtil().post(BASE_URL + URL_UPDATE_WORKORDER_PICS, params,
				handler);
	}

	public void queryWorkOrderPics(String workorderID) {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("workorder_id", workorderID);
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		params.add("signature",
				GetSignature(GlobalData.curUser.InnerID, workorderID));
		new AsyncHttpUtil().get(BASE_URL + URL_QUERY_WORKORDER_PICS, params,
				handler);
	}

	public void getStaffInfo() {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		params.add("signature", GetSignature(GlobalData.curUser.InnerID));
		new AsyncHttpUtil().get(BASE_URL + URL_GetStaffInfo, params, handler);
	}

	/********************************************
	 * 这里开始的方法，都是调用备品备件系统的
	 * *******************************************/
	/*****************************
	 * 备品备件的接口
	 *****************************/
	public static final String URL_AddSparePartApply = "/handlers/app/AddSparePartApply.ashx";

	public static final String URL_QueryUserSparePartList = "/handlers/app/QueryUserSparePartList.ashx";

	public static final String URL_QuerySparePartDetailByID = "/handlers/app/QuerySparePartDetailByID.ashx";

	public static final String URL_QuerySparePartDataByID = "/handlers/app/QuerySparePartDataByID.ashx";

	public static final String URL_QuerySparePartPicByID = "/handlers/app/QuerySparePartPicByID.ashx";

	public static final String URL_QuerySparePartStatusLogByID = "/handlers/app/QuerySparePartStatusLogByID.ashx";

	public static final String URL_QueryBigCustomerList = "/handlers/app/QueryBigCustomerList.ashx";

	public static final String URL_QuerySparePartQuotationDataByID = "/handlers/app/QuerySparePartQuotationDataByID.ashx";

	public static final String URL_UpdateQuotationDataSetup = "/handlers/app/UpdateQuotationDataSetup.ashx";

	public void SparePart_AddSparePartApply(String program_name,
			String device_num, String workorder_id, String apply_content,
			String json_img, String big_customer_id, String big_customer_name,
			String apply_id) {
		RequestParams params = new RequestParams();

		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("staff_no", GlobalData.userStaff.StaffNo);
		params.add("telephone", GlobalData.curUser.Telephone);
		try {
			params.add("user_name",
					URLEncoder.encode(GlobalData.curUser.UserName, "utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
			params.add("user_name", "");
		}
		try {
			params.add("program_name", URLEncoder.encode(program_name, "utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
			params.add("program_name", "");
		}
		try {
			params.add("apply_content",
					URLEncoder.encode(apply_content, "utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
			params.add("apply_content", "");
		}
		params.add("company_code", GlobalData.userCompany.CompanyCode);
		params.add("device_num", device_num);
		params.add("workorder_id", workorder_id);
		params.add("json_img", json_img);
		params.add("apply_id", apply_id);
		params.add("big_customer_id", big_customer_id);
		try {
			params.add("big_customer_name",
					URLEncoder.encode(big_customer_name, "utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
			params.add("apply_content", "");
		}
		params.add("version", AppData.VERSION_NAME);
		new AsyncHttpUtil().post(BASE_URL_SPARE_PART + URL_AddSparePartApply,
				params, handler);
	}

	public void SparePart_GetUserSparePartList(String pageIndex,
			String pageSize, String closeStatus) {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("pageIndex", pageIndex);
		params.add("pageSize", pageSize);
		params.add("closeStatus", closeStatus);
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		new AsyncHttpUtil().get(BASE_URL_SPARE_PART
				+ URL_QueryUserSparePartList, params, handler);
	}

	public void SparePart_GetSparePartDetailByID(String id) {
		RequestParams params = new RequestParams();
		params.add("app_quo_id", id);
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		new AsyncHttpUtil().get(BASE_URL_SPARE_PART
				+ URL_QuerySparePartDetailByID, params, handler);
	}

	public void SparePart_GetSparePartDataByID(String id) {
		RequestParams params = new RequestParams();
		params.add("app_quo_id", id);
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		new AsyncHttpUtil().get(BASE_URL_SPARE_PART
				+ URL_QuerySparePartDataByID, params, handler);
	}

	public void SparePart_GetSparePartPicByID(String id) {
		RequestParams params = new RequestParams();
		params.add("app_quo_id", id);
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		new AsyncHttpUtil().get(
				BASE_URL_SPARE_PART + URL_QuerySparePartPicByID, params,
				handler);
	}

	public void SparePart_GetSparePartQuotationDataByID(String id) {
		RequestParams params = new RequestParams();
		params.add("quo_id", id);
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		new AsyncHttpUtil().get(BASE_URL_SPARE_PART
				+ URL_QuerySparePartQuotationDataByID, params, handler);
	}

	public void SparePart_QuerySparePartStatusLogByID(String id) {
		RequestParams params = new RequestParams();
		params.add("quo_id", id);
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		new AsyncHttpUtil().get(BASE_URL_SPARE_PART
				+ URL_QuerySparePartStatusLogByID, params, handler);
	}

	public void SparePart_UpdateQuotationDataSetup(String quo_id,
			String quo_data_id) {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		try {
			params.add("user_name",
					URLEncoder.encode(GlobalData.curUser.UserName, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		params.add("quo_id", quo_id);
		params.add("quo_data_id", quo_data_id);
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		new AsyncHttpUtil().get(BASE_URL_SPARE_PART
				+ URL_UpdateQuotationDataSetup, params, handler);
	}

	public void SparePart_GetBigCustomerList() {
		RequestParams params = new RequestParams();
		params.add("user_id", GlobalData.curUser.InnerID);
		params.add("client", CLIENT);
		params.add("version", AppData.VERSION_NAME);
		new AsyncHttpUtil().get(BASE_URL_SPARE_PART + URL_QueryBigCustomerList,
				params, handler);
	}
}
