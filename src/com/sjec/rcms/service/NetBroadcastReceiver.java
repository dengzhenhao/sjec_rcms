package com.sjec.rcms.service;

import com.websharputil.common.LogUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

public class NetBroadcastReceiver extends BroadcastReceiver {

	//public NetEvent evevt = ServiceSJEC.event;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		// 如果相等的话就说明网络状态发生了变化
		if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			int netWorkState = NetUtil.getNetWorkState(context);
			// 接口回调传过去状态的类型
//			try {
//				if (evevt == null) {
//					LogUtil.d("onreceive:null"); 
//				} else {
//					evevt.onNetChange(netWorkState);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
		}
	}

	// 自定义接口
	public interface NetEvent {
		public void onNetChange(int netMobile);
	}
}
