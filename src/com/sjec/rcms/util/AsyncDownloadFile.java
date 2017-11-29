package com.sjec.rcms.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.websharputil.file.FileUtil;

import android.os.AsyncTask;

public class AsyncDownloadFile extends AsyncTask<Void, Integer, Boolean> {

	public String fileUrl = "";
	public String fileName = "";
	public String savePath = "";
	public DownloadListener listener;
	public boolean stop = false;

	protected void onPreExecute() {
		if (listener != null)
			listener.DownlaodPre();
		
		
	};

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			int count;
 
			File f = new File(savePath + fileName);
			if (f.exists()) {
				f.delete();
			}
			f = new File(savePath);
			if (! f.exists()) {
				try {
					f.mkdirs();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			URL url = new URL(fileUrl);
			URLConnection conexion = url.openConnection();
			conexion.connect();
			int lenghtOfFile = conexion.getContentLength();
			InputStream input = new BufferedInputStream(url.openStream());
			OutputStream output = new FileOutputStream(savePath + fileName);
			byte data[] = new byte[1024];
			long total = 0;
			while ((count = input.read(data)) != -1 && !stop) {
				total += count;
				publishProgress((int) ((total * 100) / lenghtOfFile));
				output.write(data, 0, count);
			}
			output.flush();
			output.close();
			input.close();

		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		if (listener != null)
			listener.DownloadProgress(values[0]);
	}

	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
		if (listener != null)
			listener.DownloadCancel();
	}

	protected void onPostExecute(Boolean result) {
		if (listener != null)
			listener.DownloadComplete(result);
	};

	public void setDownloadListener(DownloadListener lis) {
		this.listener = lis;
	}

	public interface DownloadListener {
		public void DownlaodPre();

		public void DownloadComplete(boolean result);

		public void DownloadCancel();

		public void DownloadProgress(int progress);
	}
}
