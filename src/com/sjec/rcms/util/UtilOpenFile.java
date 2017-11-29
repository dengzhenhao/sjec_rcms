package com.sjec.rcms.util;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sjec.rcms.R;
import com.sjec.rcms.data.Constant;
import com.websharputil.common.LogUtil;
import com.websharputil.file.FileUtil;

public class UtilOpenFile {

	public static void handlerFileUrl(String url, Context context,
			LinearLayout layout_downloading, TextView tv_downloading) {
		String fileName = FileUtil.getFileNameFromUrl(url);
		String fileNameAbs = Constant.SDCARD_IMAGE_DIR + fileName + "";
		if (new File(fileNameAbs).exists()) {
			openFile(fileNameAbs, context);
		} else {
			LogUtil.d(url);
			download(url, fileName, context, layout_downloading, tv_downloading);
		}
	}

	public static  void openFile(String fileNameAbs, Context context) {

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(fileNameAbs)),
				FileUtil.getMIMEType(fileNameAbs.replace(".tmp", "")));
		// intent.putExtra(Intent.EXTRA_MIME_TYPES,
		// v.getTag().toString());
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(Intent.createChooser(intent, "打开方式"));
	}

	public static  void download(String url, String fileName, final Context context,
			final LinearLayout layout_downloading, final TextView tv_downloading) {
		final AsyncDownloadFile down = new AsyncDownloadFile();
		down.fileName = fileName + "";
		down.fileUrl = url;
		down.savePath = Constant.SDCARD_IMAGE_DIR;

		down.setDownloadListener(new AsyncDownloadFile.DownloadListener() {

			@Override
			public void DownloadProgress(int progress) {
				LogUtil.d(progress + "");
				try {
					tv_downloading.setText(context.getResources().getString(
							R.string.common_downloading, progress + ""));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void DownloadComplete(boolean result) {
				LogUtil.d(result + "");
				try {
					layout_downloading.setVisibility(View.GONE);
					openFile(down.savePath + down.fileName, context);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void DownloadCancel() {

			}

			@Override
			public void DownlaodPre() {
				try {
					tv_downloading.setText(context.getResources().getString(
							R.string.common_downloading, "0"));
					layout_downloading.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		down.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

}
