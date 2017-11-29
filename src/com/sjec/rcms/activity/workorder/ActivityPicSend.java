package com.sjec.rcms.activity.workorder;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sjec.rcms.R;
import com.sjec.rcms.activity.BaseActivity;
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

public class ActivityPicSend extends BaseActivity {
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
	private String json_img = "";
	private int widthScreen = 0;
	private int widthGridViewItem = 0;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_back:
			finish();
			break;
		case R.id.btn_submit:
			if (listImage.size() == 1 && !listImage.get(0).isImage) {
				Util.createToast(ActivityPicSend.this, "请至少选择一张图片", 3000)
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
				// ByteArrayOutputStream baos = new ByteArrayOutputStream();
				//
				// bitmap = listImage.get(i).bitmap;
				// bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				// byte[] arr = baos.toByteArray();
				System.err.println("bitmapbytearr2:"
						+ listImage.get(i).byteArr.length);
				// try {
				// baos.flush();
				// baos.close();
				// } catch (IOException e) {
				// e.printStackTrace();
				// }

				String topic_image = new String(Base64.encodeBase64(listImage
						.get(i).byteArr));
				// bitmap.recycle();
				System.gc();
				try {
					obj.put("data", topic_image);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				image_jsonArray.put(obj);
			}

			sb.append(image_jsonArray.toString());

			new SJECHttpHandler(new AsyncHttpCallBack() {
				@Override
				public void onSuccess(String response) {
					super.onSuccess(response);
					Util.createToast(ActivityPicSend.this,
							R.string.msg_control_success, 3000).show();
					finish();
				}

				@Override
				public void onFailure(String message) {
					super.onFailure(message);
					Util.createToast(ActivityPicSend.this, message, 3000)
							.show();
				}
			}, ActivityPicSend.this).updateWorkorderPics(
					GlobalData.curWorkorder.InnerID, sb.toString());
			break;
		}
	}

	@Override
	public void initLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_pic_send);

	}

	@Override
	public void init(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		gv_pic = (GridView) findViewById(R.id.gv_pic);
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		loading = (LinearLayout) findViewById(R.id.loading);
		btn_submit.setOnClickListener(this);
		tv_title.setText("上传工单照片");
		layout_back.setOnClickListener(this);
		listImage.add(epAdd);
		adapterPic = new AdapterPic(this);
		gv_pic.setAdapter(adapterPic);
	}

	@Override
	public void bindData() {
		widthScreen = AppUtil.getScreenSize(this).widthPixels;
		widthGridViewItem = (widthScreen - ConvertUtil.dip2px(this, 5) * 4) / 3;
		// TODO Auto-generated method stub
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
		System.err.println("shan");
		new AlertDialog.Builder(ActivityPicSend.this)
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
		Bitmap bitmap_thumb = null;
		Bitmap bitmap = null;
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
		Bitmap bitmap_thumb = null;
		Bitmap bitmap = null;
		if (new File(TEMP_IMG_PATH).exists()) {
			String targetFileName = Constant.IMAGE_DIR_TAKE_PHOTO
					+ PREFIX_IMG.replace("name", DateUtil
							.TimeParseNowToFormatString("yyyyMMddHHmmssSSS"));
			FileUtil.FileCopy(new File(TEMP_IMG_PATH), new File(targetFileName));
			new AsyncLoadImage().execute(targetFileName);
			// EntityPicture ep = new EntityPicture(true, targetFileName,
			// getThumbImage(targetFileName),
			// compressImage(targetFileName));
			// listImage.remove(epAdd);
			// listImage.add(ep);
			// if (listImage.size() < 9) {
			// listImage.add(epAdd);
			// }
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
						ImageUtil.getThumbImage(path), ImageUtil.compressImage(path));
				listImage.remove(epAdd);
				listImage.add(ep);
				if (listImage.size() < 9) {
					listImage.add(epAdd);
				}
			} catch (Exception e) {
				Util.createToast(ActivityPicSend.this,
						R.string.msg_error_pic_send_take, 3000).show();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			loading.setVisibility(View.GONE);
			adapterPic.notifyDataSetChanged();
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
			adapterPic.notifyDataSetChanged();
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
								}

							});
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return convertView;
		}

	}

	


}
