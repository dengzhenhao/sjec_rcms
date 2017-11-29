package com.sjec.rcms.activity.workorder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sjec.rcms.R;
import com.sjec.rcms.activity.BaseActivity;
import com.sjec.rcms.activity.workorder.ActivityPicSend.AdapterPic;
import com.sjec.rcms.activity.workorder.ActivityPicSend.AsyncLoadImage;
import com.sjec.rcms.activity.workorder.ActivityPicSend.ViewHolder;
import com.sjec.rcms.dao.EntitySparePartQuotation;
import com.sjec.rcms.data.Constant;
import com.sjec.rcms.data.GlobalData;
import com.sjec.rcms.http.AsyncHttpCallBack;
import com.sjec.rcms.http.SJECHttpHandler;
import com.websharputil.common.AppUtil;
import com.websharputil.common.ConvertUtil;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;
import com.websharputil.date.DateUtil;
import com.websharputil.file.FileUtil;
import com.websharputil.image.ImageUtil;

public class ActivityApplySparePartAdd extends BaseActivity {

	private TextView tv_title;
	private LinearLayout layout_back;
	private Button btn_submit;

	private EditText et_remark, et_part_name, et_count;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_back:
			finish();
			break;
		case R.id.btn_submit:
			if(getText(et_part_name).isEmpty()|| getText(et_count).isEmpty()){
				Util.createToast(this, "名数与数量不能为空！", Toast.LENGTH_SHORT).show();
				return;
			}
			Intent intent = new Intent(Constant.ACTION_ADD_PART_APPLY);
			intent.putExtra("partName", getText(et_part_name));
			intent.putExtra("count",getText(et_count));
			intent.putExtra("remark", getText(et_remark));
			getApplicationContext().sendBroadcast(intent);
			collapseSoftInputMethod(this);
			finish();
			break;
		}
	}

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_apply_spare_part_add_part);
	}

	@Override
	public void init(Bundle savedInstanceState) {
		tv_title = (TextView) findViewById(R.id.tv_title);
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		et_remark = (EditText) findViewById(R.id.et_remark);
		et_part_name = (EditText) findViewById(R.id.et_part_name);
		et_count = (EditText) findViewById(R.id.et_count);

	}

	@Override
	public void bindData() {
		tv_title.setText("申请备件");
		layout_back.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
	}

}
