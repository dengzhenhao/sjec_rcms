package com.sjec.rcms.util;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;

import com.sjec.rcms.R;
import com.sjec.rcms.data.Constant;
import com.websharputil.common.ConvertUtil;
import com.websharputil.common.PrefUtil;

/**
 * 
 * @类名称：AppUtil
 * @包名：com.sjec.rcms.util
 * @描述： 项目中的常用方法
 * @创建人： dengzh
 * @创建时间:2015-8-11 上午11:15:03
 * @版本 V1.0
 * @Copyright (c) 2015 by 苏州威博世网络科技有限公司.
 */
public class SJECUtil {
	static String[] arr_control_type_repair;
	static String[] arr_control_type_maintain;
	static String[] arr_control_type_check;
	
	public static void InitAppInfo(){
		if(!new File(Constant.SAVE_PATH).exists()){
			new File(Constant.SAVE_PATH).mkdirs();
		}
		
		if(!new File(Constant.SDCARD_IMAGE_DIR).exists()){
			new File(Constant.SDCARD_IMAGE_DIR).mkdirs();
		}
	}
	
	public static String GetControlTypeNameRepair(Context ctx,String control_type){
		if (arr_control_type_repair == null) {
			arr_control_type_repair = ctx.getResources().getStringArray(R.array.arr_control_type_repair);
		}
		try{
			return arr_control_type_repair[ConvertUtil.ParsetStringToInt32(control_type, 0)-1];
		}catch(Exception e){
			return "";
		}
	}
	
	public static String GetControlTypeNameMaintain (Context ctx,String control_type){
		if (arr_control_type_maintain == null) {
			arr_control_type_maintain = ctx.getResources().getStringArray(R.array.arr_control_type_maintain);
		}
		try{
			return arr_control_type_maintain[ConvertUtil.ParsetStringToInt32(control_type, 0)-1];
		}catch(Exception e){
			return "";
		}
	}
	
	public static String GetControlTypeNameCheck (Context ctx,String control_type){
		if (arr_control_type_check == null) {
			arr_control_type_check = ctx.getResources().getStringArray(R.array.arr_control_type_check);
		}
		try{
			return arr_control_type_check[ConvertUtil.ParsetStringToInt32(control_type, 0)-1];
		}catch(Exception e){
			return "";
		}
	}

	/**
	 * 检查扫描的二维码格式是否为厂商号(3位)是否改建(1位)设备号
	 * 001009J16753
	 * @param str
	 * @return
	 */
	public static boolean CheckQrResult(String str) {
		Pattern pattern = Pattern.compile("^[0-9]{3}[0,1]{1}.+$");
		Matcher matcher = pattern.matcher(str);
		boolean b = matcher.matches();
		return b;
	}

	/**
	 * 按二维码扫描的结果,得到数组
	 * 
	 * @param str
	 * @return
	 */
	public static String[] GetDeviceInfoArrayFromQrCode(String str) {
		try {
			String[] arr = new String[3];
			String factoryCode = str.substring(0, 3);
			String isRebuild = str.substring(3, 4);
			String deviceNum = str.substring(4);
			arr[0] = factoryCode;
			arr[1] = isRebuild;
			arr[2] = deviceNum;
			return arr;
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 保存查询的条件
	 * type:1,状态  2:抢修单的来源  3:工单类别pager的索引(0:抢修,1:保养,2:协助)
	 * 
	 * @param context
	 * @param type
	 * @param index
	 */
	public static void SetPrefQueryConditon(Context context, int type,int index){
		String key = "";
		String value = index+"";
		if(type==1){
			key = "query_condition_status";
		}else if(type==2){
			key = "query_condition_source";
		}else if(type==3){
			key = "query_condition_order_index";
		}
		PrefUtil.setPref(context, key, value);
	}
	
	/**
	 * 获取查询的条件
	 * type:1,状态  2:抢修单的来源  3:工单类别pager的索引(0:抢修,1:保养,2:协助)
	 * @param context
	 * @param type
	 * @return
	 */
	public static String GetPrefQueryCondition(Context context, int type){
		String result = "";
		String key = "";
		if(type==1){
			key = "query_condition_status";
		}else if(type==2){
			key = "query_condition_source";
		}else if(type==3){
			key = "query_condition_order_index";
		}
		result = PrefUtil.getPref(context, key, "0");
		return result;
	}
}
