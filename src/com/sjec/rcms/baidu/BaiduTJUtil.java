package com.sjec.rcms.baidu;

import android.content.Context;

import com.baidu.mobstat.StatService;
import com.websharputil.common.Util;

public class BaiduTJUtil {

	/**
	 * 初始化百度统计3.7
	 * @param context
	 */
	public static void initBaiduStatstics(Context context) {
		// /**
		// * 如果您的app是为电视盒子准备的，请务必调用该接口来进行统计�?�调用时机应该优于其他统计接口调用�??
		// * 如果您的app不是电视上使用，请不要调用该接口, 否则在有历史数据的情况下，数据统计会出现异常
		// * 此接口调用类似setAppChannel，需要在其他接口之前调用
		// */
		// StatService.setForTv(this, true);

		// 设置AppKey
		StatService.setAppKey(Util.getMetaValue(context, "BaiduMobAd_STAT_ID"));//
		// appkey必须在mtj网站上注册生成，该设置建议在AndroidManifest.xml中填写，代码中填写容易丢�?
		// 设置渠道
		/*
		 * 设置渠道的推荐方法�?�该方法同setAppChannel（String），
		 * 如果第三个参数设置为true（防止渠道代码设置会丢失的情况），将会保存该渠道，每次设置都会更新保存的渠道�?
		 * 如果之前的版本使用了该函数设置渠�?
		 * ，�?�后来的版本�?要AndroidManifest.xml设置渠道，那么需要将第二个参数设置为空字符串
		 * ,并且第三个参数设置为false即可 ，如StatService.setAppChannel(this, "", false);�?
		 * appChannel是应用的发布渠道，不�?要在mtj网站上注册，直接填写就可�?
		 */
		StatService.setAppChannel(context, "testmarket", true);

		// 设置每次启动session的间隔失效时间，可以不设置默�?30S
		// 测试时，可以使用1秒钟session过期，这样不断的间隔1S启动�?出会产生大量日志�?
		StatService.setSessionTimeOut(30);

		// 打开崩溃收集
		// setOn也可以在AndroidManifest.xml文件中填写，BaiduMobAd_EXCEPTION_LOG，打�?崩溃错误收集，默认是关闭�?
		StatService.setOn(context, StatService.EXCEPTION_LOG);

		/*
		 * 设置启动时日志发送延时的秒数<br/> 单位为秒，大小为0s�?30s之间<br/>
		 * 注：请在StatService.setSendLogStrategy之前调用，否则设置不起作�?
		 * 
		 * 如果设置的是发�?�策略是启动时发送，那么这个参数就会在发送前�?查您设置的这个参数，表示延迟多少秒发送�??<br/>
		 * 这个参数的设置暂时只支持代码加入�? 在您的首个启动的Activity中的onCreate函数中使用就可以�?<br/>
		 */
		StatService.setLogSenderDelayed(1);

		/*
		 * 用于设置日志发�?�策�?<br /> 嵌入位置：Activity的onCreate()函数�? <br />
		 * 
		 * 调用方式：StatService.setSendLogStrategy(this,SendStrategyEnum.
		 * SET_TIME_INTERVAL, 1, false); 第二个参数可选： SendStrategyEnum.APP_START
		 * SendStrategyEnum.ONCE_A_DAY SendStrategyEnum.SET_TIME_INTERVAL 第三个参数：
		 * 这个参数在第二个参数选择SendStrategyEnum.SET_TIME_INTERVAL时生效�??
		 * 取�?��?�为1-24之间的整�?,�?1<=rtime_interval<=24，以小时为单�? 第四个参数：
		 * 表示是否仅支持wifi下日志发送，若为true，表示仅在wifi环境下发送日志；若为false，表示可以在任何联网环境下发送日�?
		 */
		// StatService.setSendLogStrategy(this, SendStrategyEnum.APP_START, 1,
		// false);

		// 调试百度统计SDK的Log�?关，可以在Eclipse中看到sdk打印的日志，发布时去除调用，或�?�设置为false
		StatService.setDebugOn(true);
	}
	
	
}

 
