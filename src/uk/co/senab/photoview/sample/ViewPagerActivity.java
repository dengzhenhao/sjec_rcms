/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package uk.co.senab.photoview.sample;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.sjec.rcms.R;
import com.sjec.rcms.activity.BaseActivity;

import uk.co.senab.photoview.PhotoView;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.LinearGradient;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewPagerActivity extends BaseActivity {

	AsyncImageLoaderScrollImage asyncLoad = new AsyncImageLoaderScrollImage();
	private ViewPager mViewPager;
	private String[] arrImageUrls ;
	private RelativeLayout layout_scroll_img;
	private TextView tv_loading;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_scroll_image);
		mViewPager = new HackyViewPager(this);
		mViewPager.setBackgroundColor(getResources().getColor(android.R.color.black));
		layout_scroll_img = (RelativeLayout) findViewById(R.id.layout_scroll_img);
		tv_loading = (TextView) findViewById(R.id.tv_loading);
		layout_scroll_img.addView(mViewPager);

		Bundle b = getIntent().getExtras();
		int index = 0;
		try {
			index =Integer.parseInt( b.getString("index","0"));
			JSONArray arr = new JSONArray(b.getString("pics", "[]"));
			if (arr != null) {
				arrImageUrls = new String[arr.length()];
				for (int i = 0; i < arr.length(); i++) {
					JSONObject obj = arr.getJSONObject(i);
					arrImageUrls[i] = obj.getString("pic");
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mViewPager.setAdapter(new SamplePagerAdapter());
		mViewPager.setCurrentItem(index);
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	class SamplePagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return arrImageUrls.length;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			LayoutInflater mInflater = ViewPagerActivity.this.getLayoutInflater();
			View convertView = (LinearLayout)mInflater.inflate(R.layout.item_scroll_img, null);
			final PhotoView photoView =(PhotoView) convertView.findViewById(R.id.pv_scroll_img);
			final LinearLayout layout_loading = (LinearLayout)convertView.findViewById(R.id.layout_loading);
			// photoView.setImageResource(sDrawables[position]);
			Bitmap bitmap = asyncLoad.loadDrawable(arrImageUrls[position],
					new AsyncImageLoaderScrollImage.ImageCallback() {

						@Override
						public void imageLoaded(Bitmap imageDrawable,
								String imageUrl) {
							try { 
								layout_loading.setVisibility(View.GONE);
								photoView.setVisibility(View.VISIBLE);
								photoView.setImageBitmap(imageDrawable);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}, false);
			if (bitmap == null) {
				layout_loading.setVisibility(View.VISIBLE);
				photoView.setVisibility(View.GONE);
				//photoView.setImageResource(R.drawable.icon);
			} else {
				layout_loading.setVisibility(View.GONE);
				photoView.setVisibility(View.VISIBLE);
				photoView.setImageBitmap(bitmap);
			}
			// photoView.setImageURI(Uri.parse(urls[position]));
			// Now just add PhotoView to ViewPager and return it
			container.addView(convertView, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);

			return convertView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

	public void clearData() {
		try {
			mViewPager.removeAllViews();
			mViewPager = null;
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initLayout() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void bindData() {
		// TODO Auto-generated method stub
		
	}

}
