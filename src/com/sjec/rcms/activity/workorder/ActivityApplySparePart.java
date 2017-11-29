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
import android.content.IntentFilter;
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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.sjec.rcms.R;
import com.sjec.rcms.activity.BaseActivity;
import com.sjec.rcms.activity.fragment.FragmentWorkorderPage;
import com.sjec.rcms.activity.workorder.ActivityPicSend.AdapterPic;
import com.sjec.rcms.activity.workorder.ActivityPicSend.AsyncLoadImage;
import com.sjec.rcms.activity.workorder.ActivityPicSend.ViewHolder;
import com.sjec.rcms.baidu.MyApplication;
import com.sjec.rcms.dao.EntityBigCustomer;
import com.sjec.rcms.dao.EntitySparePartQuotation;
import com.sjec.rcms.dao.EntityWorkorderDao;
import com.sjec.rcms.data.Constant;
import com.sjec.rcms.data.GlobalData;
import com.sjec.rcms.http.AsyncHttpCallBack;
import com.sjec.rcms.http.SJECHttpHandler;
import com.sjec.rcms.util.SJECUtil;
import com.websharputil.common.AppUtil;
import com.websharputil.common.ConvertUtil;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;
import com.websharputil.date.DateUtil;
import com.websharputil.file.FileUtil;
import com.websharputil.image.ImageUtil;

import de.greenrobot.dao.internal.DaoConfig;

public class ActivityApplySparePart extends BaseActivity {
	private final int PIC_WIDTH = 150;
	private final int PIC_HEIGHT = 150;
	ContentResolver resolver;
	Uri uri;
	// ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
	ArrayList<EntityPicture> listImage = new ArrayList<EntityPicture>();

	SharedPreferences sharedPreferences;
	public String innerID, districtID;

	public String topic_content = null;
	String str;

	private static String TEMP_IMG_FILE = "sjec_tmp.jpg";
	private static String TEMP_IMG_PATH = Constant.IMAGE_DIR_TAKE_PHOTO
			+ TEMP_IMG_FILE;
	private static String PREFIX_IMG = "sjec_name.jpg";
	private AdapterPic adapterPic;
	private GridView gv_pic;
	private LinearLayout loading;
	EntityPicture epAdd = new EntityPicture(false, "", null, null);

	private String classID = "";

	private TextView tv_title;
	private LinearLayout layout_back;
	private Button btn_submit;
	// private TextView tv_lab_count,tv_lab_remark;
	// private EditText et_draw_code, et_code, et_count,
	// et_remark,et_part_name,et_spcf;

	private EditText et_remark;
	private TextView tv_program_name, tv_company_name, tv_apply_user;

	private LinearLayout layout_detail;
	private TextView tv_add_detail;
	ArrayAdapter<String> adapterStatus = null;
	private Spinner sp_big_customer;

	private String json_img = "";
	private int widthScreen = 0;
	private int widthGridViewItem = 0;

	ArrayList<EntityPartApply> listDetail = new ArrayList<EntityPartApply>();

	String apply_id = "";
	String program_name = "";
	String device_num = "";
	String workorder_id = "";
	String big_customer_id = "-1";

	class EntityPartApply {
		public String partName;
		public String count;
		public String remark;

		public EntityPartApply(String partName, String count, String remark) {
			this.partName = partName;
			this.count = count;
			this.remark = remark;
		}
	}

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(Constant.ACTION_ADD_PART_APPLY)) {
				// add detail
				listDetail.add(new EntityPartApply(intent.getExtras()
						.getString("partName"), intent.getExtras().getString(
						"count"), intent.getExtras().getString("remark")));
				bindDetail();
				LogUtil.d(listDetail.toString());
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_back:
			finish();
			break;
		case R.id.btn_submit:
			if (listImage.size() == 1 && !listImage.get(0).isImage
					&& listDetail.size() == 0) {
				Util.createToast(ActivityApplySparePart.this,
						"请至少申请一个备件或选择一张图片", 3000).show();
				return;
			}

			int bigCustomerPosition = sp_big_customer.getSelectedItemPosition();
			String bigCustomerID = GlobalData.listBigCustomer
					.get(bigCustomerPosition).ID.toString();
			String bigCustsomerName = GlobalData.listBigCustomer
					.get(bigCustomerPosition).BitCustomerName;
			if (bigCustomerID.equals("-1")) {
				Util.createToast(ActivityApplySparePart.this, "请选择申请类型", 3000)
						.show();
				return;
			}

			StringBuilder sb = new StringBuilder();
			JSONArray image_jsonArray = new JSONArray();
			Bitmap bitmap = null;
			for (int i = 0; i < listImage.size(); i++) {
				if (!listImage.get(i).isImage) {
					continue;
				}
				JSONObject obj = new JSONObject();

				String topic_image = new String(Base64.encodeBase64(listImage
						.get(i).byteArr));
				System.gc();
				try {
					obj.put("data", topic_image);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				image_jsonArray.put(obj);
			}
			sb.append(image_jsonArray.toString());

			JSONArray jsonArr = new JSONArray();
			try {
				for (int k = 0; k < listDetail.size(); k++) {
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("part_name", listDetail.get(k).partName);
					jsonObj.put("count", listDetail.get(k).count);
					jsonObj.put("remark", listDetail.get(k).remark);
					jsonArr.put(jsonObj);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			new SJECHttpHandler(new AsyncHttpCallBack() {
				@Override
				public void onSuccess(String response) {
					super.onSuccess(response);
					getApplicationContext().sendBroadcast(
							new Intent(Constant.ACTION_REFRESH_APPLY_INFO));
					Util.createToast(ActivityApplySparePart.this,
							R.string.msg_control_success, 3000).show();
					finish();
				}

				@Override
				public void onFailure(String message) {
					super.onFailure(message);
					Util.createToast(ActivityApplySparePart.this, message, 3000)
							.show();
				}
			}, ActivityApplySparePart.this).SparePart_AddSparePartApply(
					program_name, device_num, workorder_id, jsonArr.toString(),
					sb.toString(), bigCustomerID, bigCustsomerName, apply_id);
			break;
		case R.id.tv_add_detail:
			Util.startActivity(this, ActivityApplySparePartAdd.class, false);
			break;
		}
	}

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_apply_spare_part);
	}

	@Override
	public void init(Bundle savedInstanceState) {
		tv_title = (TextView) findViewById(R.id.tv_title);
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		// et_draw_code = (EditText) findViewById(R.id.et_draw_code);
		// et_code = (EditText) findViewById(R.id.et_code);
		// et_count = (EditText) findViewById(R.id.et_count);
		et_remark = (EditText) findViewById(R.id.et_remark);
		// et_part_name = (EditText)findViewById(R.id.et_part_name);
		// et_spcf = (EditText)findViewById(R.id.et_spcf);
		gv_pic = (GridView) findViewById(R.id.gv_pic);
		loading = (LinearLayout) findViewById(R.id.loading);
		// tv_lab_count = (TextView)findViewById(R.id.tv_lab_count);
		// tv_lab_remark = (TextView)findViewById(R.id.tv_lab_remark);
		tv_program_name = (TextView) findViewById(R.id.tv_program_name);
		tv_company_name = (TextView) findViewById(R.id.tv_company_name);
		tv_apply_user = (TextView) findViewById(R.id.tv_apply_user);
		layout_detail = (LinearLayout) findViewById(R.id.layout_detail);
		tv_add_detail = (TextView) findViewById(R.id.tv_add_detail);
		sp_big_customer = (Spinner) findViewById(R.id.sp_big_customer);
		if(GlobalData.curWorkorder!=null){
			Util.createToast(this, "项目名称："+GlobalData.curDevice.Program_Name, 5000).show();
		}
	}

	@Override
	public void bindData() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.ACTION_ADD_PART_APPLY);
		registerReceiver(receiver, filter);
		tv_title.setText("申请备件");
		// tv_lab_count.setText(Html.fromHtml("<span style='color:red;'>*</span>数量："));
		// tv_lab_remark.setText(Html.fromHtml("<span style='color:red;'>*</span>备注："));
		Bundle b = getIntent().getExtras();
		if (b != null && b.getString("edit_spare_part") != null) {
			program_name = GlobalData.curApply.ProgramName;
			device_num = GlobalData.curApply.DeviceCode;
			workorder_id = GlobalData.curApply.WorkorderID;
			apply_id = GlobalData.curApply.ID.toString();
			sp_big_customer.setEnabled(false);
			try {
				
				
		        
				
				big_customer_id = GlobalData.curApply.BigCustomerID.toString();
			} catch (Exception e) {

			}
			// sp_big_customer.setVisibility(View.GONE);
		} else {
			program_name = GlobalData.curDevice.Program_Name;
			device_num = GlobalData.curWorkorder.Device_Num;
			workorder_id = GlobalData.curWorkorder.InnerID;
			apply_id = "";
		}

		tv_program_name.setText(program_name);
		tv_company_name.setText(GlobalData.userCompany.CompanyName);
		tv_apply_user.setText(GlobalData.curUser.UserName);

		tv_add_detail.setOnClickListener(this);
		layout_back.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		listImage.add(epAdd);
		adapterPic = new AdapterPic(this);
		gv_pic.setAdapter(adapterPic);

		widthScreen = AppUtil.getScreenSize(this).widthPixels;
		widthGridViewItem = (widthScreen - ConvertUtil.dip2px(this, 5) * 4) / 3;
		gv_pic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				LogUtil.d(arg2 + "");
				if (!listImage.get(arg2).isImage) {
					TakePhoto();
				}
			}
		});

		boolean isAddDefault = false;
		for (int i = 0; i < GlobalData.listBigCustomer.size(); i++) {
			if (GlobalData.listBigCustomer.get(i).ID == -1) {
				isAddDefault = true;
				break;
			}
		}
		if (!isAddDefault) {
			EntityBigCustomer entityEmpty = new EntityBigCustomer();
			entityEmpty.BitCustomerName = "普通客户";
			entityEmpty.ID = 0;
			GlobalData.listBigCustomer.add(0, entityEmpty);

			EntityBigCustomer entityDefault = new EntityBigCustomer();
			entityDefault.BitCustomerName = "---请选择---";
			entityDefault.ID = -1;
			GlobalData.listBigCustomer.add(0, entityDefault);
		}

		ArrayList<String> listBigCustomerString = new ArrayList<String>();
		for (int i = 0; i < GlobalData.listBigCustomer.size(); i++) {
			listBigCustomerString.add(GlobalData.listBigCustomer.get(i)
					.getBitCustomerName());
		}

		adapterStatus = new ArrayAdapter<String>(ActivityApplySparePart.this,
				android.R.layout.simple_spinner_item, listBigCustomerString);
		adapterStatus
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_big_customer.setAdapter(adapterStatus);
		sp_big_customer.setSelection(0, false);
		for (int i = 0; i < GlobalData.listBigCustomer.size(); i++) {
			if (big_customer_id.equals(GlobalData.listBigCustomer.get(i).ID
					.toString())) {
				sp_big_customer.setSelection(i, false);
				break;
			}
		}
		sp_big_customer
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						arg0.setVisibility(View.VISIBLE);
						LogUtil.d("onItemSelected:"
								+ GlobalData.listBigCustomer.get(position).BitCustomerName);

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});
	}

	private void TakePhoto() {
		File f = new File(TEMP_IMG_PATH);
		if (!f.exists()) {
			try {
				f.getParentFile().mkdirs();
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		new AlertDialog.Builder(ActivityApplySparePart.this)
				.setTitle("图片来源...")
				.setNegativeButton("相册", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Intent openAlbumIntent = new Intent(
								Intent.ACTION_PICK,
								android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						startActivityForResult(openAlbumIntent, 999);
					}
				})
				.setPositiveButton("拍照", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
						Intent intent = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE); // 调用系统相机
						Uri imageUri = Uri.fromFile(new File(
								Constant.IMAGE_DIR_TAKE_PHOTO, TEMP_IMG_FILE));
						intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
						startActivityForResult(intent, 888);
					}
				}).show();

	}

	/**
	 * 处理相册中选取的照片
	 * 
	 * @param iv
	 * @param data
	 */
	private void handlerPhoto(Intent data) {
		if (data != null) {
			resolver = getContentResolver();
			uri = data.getData();
			String path = FileUtil.getAbsoluteImagePathFromUri(this, uri);
			new AsyncLoadImage().execute(path);
		}
	}

	/**
	 * 处理拍摄的照片
	 * 
	 * @param iv
	 */
	private void handlerCamera() {
		if (new File(TEMP_IMG_PATH).exists()) {
			String targetFileName = Constant.IMAGE_DIR_TAKE_PHOTO
					+ PREFIX_IMG.replace("name", DateUtil
							.TimeParseNowToFormatString("yyyyMMddHHmmssSSS"));
			FileUtil.FileCopy(new File(TEMP_IMG_PATH), new File(targetFileName));
			new AsyncLoadImage().execute(targetFileName);
		}
	}

	class AsyncLoadImage extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			loading.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				String path = params[0];
				EntityPicture ep = new EntityPicture(true, path,
						ImageUtil.getThumbImage(path),
						ImageUtil.compressImage(path));
				listImage.remove(epAdd);
				listImage.add(ep);
				if (listImage.size() < 9) {
					listImage.add(epAdd);
				}
			} catch (Exception e) {
				Util.createToast(ActivityApplySparePart.this,
						R.string.msg_error_pic_send_take, 3000).show();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			loading.setVisibility(View.GONE);
			adapterPic.notifyDataSetChanged();
			measureGvHeight();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK == resultCode) {
			switch (requestCode) {
			case 888:
				// 拍照
				handlerCamera();
				break;
			case 999:
				// 相册
				handlerPhoto(data);
				break;
			default:
				break;
			}
			// adapterPic.notifyDataSetChanged();
		}
	}

	class ViewHolder {
		private ImageView iv_pic;
		private ImageView iv_delete;
		private TextView tv_thumb_take_pic;
	}

	class AdapterPic extends BaseAdapter {
		private LayoutInflater mInflater;
		private Context ctx;

		public AdapterPic(Context context) {
			this.ctx = context;
		}

		@Override
		public int getCount() {
			return listImage.size();
		}

		@Override
		public Object getItem(int position) {
			return listImage.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			try {
				if (mInflater == null) {
					mInflater = LayoutInflater.from(ctx);
				}
				if (listImage.get(position).isImage) {
					convertView = mInflater.inflate(R.layout.item_grid_img,
							null);
					holder = new ViewHolder();
					holder.iv_pic = (ImageView) convertView
							.findViewById(R.id.iv_thumb);
					holder.iv_delete = (ImageView) convertView
							.findViewById(R.id.iv_delete);
					LayoutParams lp = holder.iv_pic.getLayoutParams();
					lp.width = widthGridViewItem;
					lp.height = widthGridViewItem;
					holder.iv_pic.setLayoutParams(lp);
				} else {
					convertView = mInflater.inflate(
							R.layout.item_grid_take_pic, null);
					holder = new ViewHolder();
					holder.tv_thumb_take_pic = (TextView) convertView
							.findViewById(R.id.tv_thumb_take_pic);
					LayoutParams lp = holder.tv_thumb_take_pic
							.getLayoutParams();
					lp.width = widthGridViewItem;
					lp.height = widthGridViewItem;
					holder.tv_thumb_take_pic.setLayoutParams(lp);
				}

				if (listImage.get(position).isImage) {
					holder.iv_pic.setImageBitmap(ImageUtil.toRoundCorner(
							listImage.get(position).bitmap_thumb, 5));
					BitmapDrawable drawable = (BitmapDrawable) getResources()
							.getDrawable(R.drawable.delete);
					holder.iv_delete.setImageBitmap(ImageUtil.toRoundCorner(
							drawable, 5).getBitmap());
					holder.iv_delete.setTag(position + "");
					holder.iv_delete
							.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									listImage.remove(Integer.parseInt(v
											.getTag().toString()));
									System.gc();
									adapterPic.notifyDataSetChanged();
									measureGvHeight();
								}

							});
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return convertView;
		}

	}

	private void bindDetail() {
		layout_detail.removeAllViews();
		if (listDetail.size() > 0) {
			layout_detail.setVisibility(View.VISIBLE);
			LayoutInflater mInflater = LayoutInflater.from(this);
			View headerView = mInflater.inflate(R.layout.headview_apply_part,
					null);
			layout_detail.addView(headerView);
			for (int i = 0; i < listDetail.size(); i++) {
				View convertView = mInflater.inflate(R.layout.item_apply_part,
						null);
				TextView tv_part_name = (TextView) convertView
						.findViewById(R.id.tv_part_name);
				TextView tv_part_count = (TextView) convertView
						.findViewById(R.id.tv_part_count);
				ImageView iv_delete = (ImageView) convertView
						.findViewById(R.id.iv_delete);
				tv_part_name.setText(listDetail.get(i).partName);
				tv_part_count.setText(listDetail.get(i).count + "");
				iv_delete.setTag(i);
				iv_delete.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						int position = ConvertUtil.ParsetStringToInt32(v
								.getTag().toString(), -1);
						if (position != -1) {
							listDetail.remove(position);
							// 因为有一个headview, 所以在removeView的时候,索引要+1
							bindDetail();
						}
					}
				});
				layout_detail.addView(convertView);
			}
		} else {
			layout_detail.setVisibility(View.GONE);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	private void measureGvHeight() {
		int itemsCount = adapterPic.getCount();
		LogUtil.d(itemsCount + "");
		int rows = (int) Math.ceil(itemsCount / 3.0);
		LogUtil.d(rows + "行");
		if (rows == 0)
			return;
		android.widget.LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) gv_pic
				.getLayoutParams();
		lp.height = rows * widthGridViewItem + (rows - 1)
				* ConvertUtil.dip2px(this, 5);
		LogUtil.d(lp.height + "行");
		gv_pic.setLayoutParams(lp);
	}

}
