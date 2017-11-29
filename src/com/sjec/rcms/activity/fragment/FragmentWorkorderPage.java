package com.sjec.rcms.activity.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sjec.rcms.R;
import com.sjec.rcms.activity.BaseFragment;
import com.sjec.rcms.activity.main.ActivityMain;
import com.sjec.rcms.activity.workorder.ActivityAddWorkorder;
import com.sjec.rcms.dao.EntityVillage;
import com.sjec.rcms.data.Constant;
import com.sjec.rcms.data.GlobalData;
import com.sjec.rcms.http.AsyncHttpCallBack;
import com.sjec.rcms.http.SJECHttpHandler;
import com.sjec.rcms.util.SJECUtil;
import com.websharputil.common.ConvertUtil;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;
import com.websharputil.json.JSONUtils;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class FragmentWorkorderPage extends BaseFragment implements
		View.OnClickListener {

	private Spinner sp_workorder_status;
	// private Spinner sp_village;
	public EditText et_search_keyword;
	private Button btn_search;

	ArrayAdapter<String> adapterStatus = null;

	List<String> listWorkorderStatus = null;
	private String[] arrWorkorderStatusValue = null;
	public String workorderStauts = "1";
	public String keyword = "";

	private TextView tv_manual_maintain;

	private ViewPager mPageVp ;

	private List<android.support.v4.app.Fragment> mFragmentList = new ArrayList<android.support.v4.app.Fragment>();
	private FragmentAdapter mFragmentAdapter;
	private TextView tv_tab_qx, tv_tab_wb, tv_tab_xz, tv_tab_jc;
	private int tagCount = 4;
	
	/**
	 * Tab的那个引导线
	 */
	private ImageView mTabLineIv;
	/**
	 * Fragment
	 */
	private FragmentWorkorderListV2 fragmentQx;
	private FragmentWorkorderListV2 fragmentWb;
	private FragmentWorkorderListV2 fragmentXz;
	private FragmentWorkorderListV2 fragmentJc;

	/**
	 * ViewPager的当前选中页
	 */
	private int currentIndex;

	/**
	 * 屏幕的宽度
	 */
	private int screenWidth;

	private boolean isInit = true;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_workorder_page,
				container, false);
		init(view);
		initViewPage();
		initTabLineWidth();
		bindData();
		return view;
	}

	private void initViewPage() {
		tv_tab_qx.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mPageVp.setCurrentItem(0, true);
			}
		});
		tv_tab_wb.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mPageVp.setCurrentItem(1, true);
			}
		});
		tv_tab_xz.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mPageVp.setCurrentItem(2, true);
			}
		});
		
		tv_tab_jc.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mPageVp.setCurrentItem(3, true);
			}
		});
		

		fragmentQx = new FragmentWorkorderListV2(Constant.EnumWorkorderType.TYPE_WORKORDER_REPAIR+"");
		fragmentWb = new FragmentWorkorderListV2(Constant.EnumWorkorderType.TYPE_WORKORDER_MAINTAIN+"");
		fragmentXz = new FragmentWorkorderListV2(Constant.EnumWorkorderType.TYPE_WORKORDER_ASSIST+"");
		fragmentJc = new FragmentWorkorderListV2(Constant.EnumWorkorderType.TYPE_WORKORDER_CHECK+"");
		mFragmentList.add(fragmentQx);
		mFragmentList.add(fragmentWb);
		mFragmentList.add(fragmentXz);
		mFragmentList.add(fragmentJc);
		
		mFragmentAdapter = new FragmentAdapter(
				((ActivityMain) this.getActivity()).getSupportFragmentManager(),
				mFragmentList);
		mPageVp.setAdapter(mFragmentAdapter);

		mPageVp.setOnPageChangeListener(new OnPageChangeListener() {

			/**
			 * state滑动中的状态 有三种状态（0，1，2） 1：正在滑动 2：滑动完毕 0：什么都没做。
			 */
			@Override
			public void onPageScrollStateChanged(int state) {
				if (state == 2) {
					LogUtil.d(mPageVp.getCurrentItem() + "");
					SJECUtil.SetPrefQueryConditon(
							FragmentWorkorderPage.this.getActivity(), 3,
							mPageVp.getCurrentItem());
				}
			}

			/**
			 * position :当前页面，及你点击滑动的页面 offset:当前页面偏移的百分比
			 * offsetPixels:当前页面偏移的像素位置
			 */
			@Override
			public void onPageScrolled(int position, float offset,
					int offsetPixels) {
				LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
						.getLayoutParams();

				//Log.e("offset:", currentIndex + "   " + position);
				/**
				 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来
				 * 设置mTabLineIv的左边距 滑动场景： 记3个页面, 从左到右分别为0,1,2 0->1; 1->2; 2->1;
				 * 1->0
				 */

				if (currentIndex == 0 && position == 0)// 0->1
				{
					lp.leftMargin = (int) (offset * (screenWidth * 1.0 / tagCount) + currentIndex
							* (screenWidth / tagCount));

				} else if (currentIndex == 1 && position == 0) // 1->0
				{
					lp.leftMargin = (int) (-(1 - offset)
							* (screenWidth * 1.0 / tagCount) + currentIndex
							* (screenWidth / tagCount));

				} else if (currentIndex == 1 && position == 1) // 1->2
				{
					lp.leftMargin = (int) (offset * (screenWidth * 1.0 / tagCount) + currentIndex
							* (screenWidth / tagCount));
					
				} else if (currentIndex == 2 && position == 1) // 2->1
				{
					lp.leftMargin = (int) (-(1 - offset)
							* (screenWidth * 1.0 / tagCount) + currentIndex
							* (screenWidth / tagCount));
				} else if (currentIndex == 2 && position == 2) // 2->2
				{
					lp.leftMargin = (int) (offset * (screenWidth * 1.0 / tagCount) + currentIndex
							* (screenWidth / tagCount));
				}  else if (currentIndex == 3 && position == 2) 
				{
					lp.leftMargin = (int) (-(1 - offset)
							* (screenWidth * 1.0 / tagCount) + currentIndex
							* (screenWidth / tagCount));
				} 
				else if (currentIndex == 3 && position == 3) 
				{
					lp.leftMargin = (int) (offset * (screenWidth * 1.0 / tagCount) + currentIndex
							* (screenWidth / tagCount));
				}
				mTabLineIv.setLayoutParams(lp);
			}

			@Override
			public void onPageSelected(int position) {
				resetTextView();
				switch (position) {
				case 0:
					tv_tab_qx.setTextColor(getResources().getColor(
							R.color.color_btn));
					break;
				case 1:
					tv_tab_wb.setTextColor(getResources().getColor(
							R.color.color_btn));
					break;
				case 2:
					tv_tab_xz.setTextColor(getResources().getColor(
							R.color.color_btn));
					break;
				case 3:
					tv_tab_jc .setTextColor(getResources().getColor(
							R.color.color_btn));
					break;
				}
				currentIndex = position;
			}
		});

		int lastPageIndex = ConvertUtil.ParsetStringToInt32(
				SJECUtil.GetPrefQueryCondition(
						FragmentWorkorderPage.this.getActivity(), 3), 0);
		// mPageVp.setCurrentItem(ConvertUtil.ParsetStringToInt32(lastPageIndex,
		// 0),true);
		currentIndex = lastPageIndex;
		if (lastPageIndex == 0) {
			tv_tab_qx.performClick();
		} else if (lastPageIndex == 1) {
			tv_tab_wb.performClick();
		} else if (lastPageIndex == 2) {
			tv_tab_xz.performClick();
		} else {
			tv_tab_jc.performClick();
		}

	}

	/**
	 * 设置滑动条的宽度为屏幕的1/3(根据Tab的个数而定)
	 */
	private void initTabLineWidth() {
		DisplayMetrics dpMetrics = new DisplayMetrics();
		this.getActivity().getWindow().getWindowManager().getDefaultDisplay()
				.getMetrics(dpMetrics);
		screenWidth = dpMetrics.widthPixels;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
				.getLayoutParams();
		lp.width = screenWidth / tagCount;
		mTabLineIv.setLayoutParams(lp);
	}

	/**
	 * 重置颜色
	 */
	private void resetTextView() {
		tv_tab_qx.setTextColor(getResources().getColor(
				android.R.color.darker_gray));
		tv_tab_wb.setTextColor(getResources().getColor(
				android.R.color.darker_gray));
		tv_tab_xz.setTextColor(getResources().getColor(
				android.R.color.darker_gray));
		tv_tab_jc.setTextColor(getResources().getColor(
				android.R.color.darker_gray));
	}

	private void init(View view) {
		// sp_village = (Spinner) view.findViewById(R.id.sp_village);
		tv_tab_qx = (TextView) view.findViewById(R.id.tv_tab_qx);
		tv_tab_wb = (TextView) view.findViewById(R.id.tv_tab_wb);
		tv_tab_xz = (TextView) view.findViewById(R.id.tv_tab_xz);
		tv_tab_jc = (TextView)view.findViewById(R.id.tv_tab_jc);
		mTabLineIv = (ImageView) view.findViewById(R.id.id_tab_line_iv);
		mPageVp = (ViewPager) view.findViewById(R.id.id_page_vp);
		mPageVp.setOffscreenPageLimit(tagCount);
		sp_workorder_status = (Spinner) view
				.findViewById(R.id.sp_workorder_status);
		et_search_keyword = (EditText) view
				.findViewById(R.id.et_search_keyword);
		btn_search = (Button) view.findViewById(R.id.btn_search);

		tv_manual_maintain = (TextView) view
				.findViewById(R.id.tv_manual_maintain);
		btn_search.setOnClickListener(this);
		tv_manual_maintain.setOnClickListener(this);
	}

	private void bindData() {

		listWorkorderStatus = new ArrayList<String>(
				Arrays.asList(getResources().getStringArray(
						R.array.arr_workorder_status)));

		arrWorkorderStatusValue = getResources().getStringArray(
				R.array.arr_workorder_status_value);

		adapterStatus = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, listWorkorderStatus);
		adapterStatus
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_workorder_status.setAdapter(adapterStatus);
		sp_workorder_status
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						arg0.setVisibility(View.VISIBLE);
						LogUtil.d("onItemSelected:" + position);
						SJECUtil.SetPrefQueryConditon(
								FragmentWorkorderPage.this.getActivity(), 1,
								position);
						workorderStauts = arrWorkorderStatusValue[position];
						refreshWorkorder();
						isInit = false;
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

		String lastPageIndex = SJECUtil.GetPrefQueryCondition(
				FragmentWorkorderPage.this.getActivity(), 1);
		sp_workorder_status.setSelection(ConvertUtil.ParsetStringToInt32(
				lastPageIndex, 1));
		// workorderStauts =
		// arrWorkorderStatusValue[ConvertUtil.ParsetStringToInt32(lastPageIndex,
		// 1)];
		// refreshWorkorder();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// bindData();
	}

	@Override
	public void onStart() {
		super.onStart();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_search:
			keyword = et_search_keyword.getText().toString().trim();
			refreshWorkorder();
			break;
		case R.id.tv_manual_maintain:
			Util.startActivity(getActivity(), ActivityAddWorkorder.class, false);
			break;
		}

	}

	public void refreshWorkorder() {
		LogUtil.d("%s", "刷新工单111");
		fragmentQx.keyword = keyword;
		fragmentWb.keyword = keyword;
		fragmentXz.keyword = keyword;
		fragmentJc.keyword = keyword;
		fragmentQx.workorderStauts = workorderStauts;
		fragmentWb.workorderStauts = workorderStauts;
		fragmentXz.workorderStauts = workorderStauts;
		fragmentJc.workorderStauts = workorderStauts;
		if (!isInit) {
			fragmentQx.refreshWorkorder();
		}
		fragmentWb.refreshWorkorder();
		fragmentXz.refreshWorkorder();
		fragmentJc.refreshWorkorder();
	} 

	@Override
	public void onPause() {
		
		super.onPause();
	}

	@Override
	public void onResume() {
		
		super.onResume();
	}
}
