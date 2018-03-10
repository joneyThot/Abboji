package com.android.abooji.adapter;

import java.util.ArrayList;
import java.util.TreeMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.abooji.R;
import com.android.abooji.model.SaveLocationModel;
import com.android.abooji.model.SaveShoppingListModel;
import com.android.abooji.model.SearchModel;
import com.android.abooji.model.TodaysSpecialModel;
import com.android.abooji.service.Utils;
import com.android.abooji.view.TextViewPlus;
import com.squareup.picasso.Picasso;

public class SearchDetailsAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater infalter;
	private ArrayList<SearchModel> mSearchModelList;
	private OnClickListener mListClickListener;
	private int mDeviceWidth = 480;

	@SuppressLint("NewApi")
	public SearchDetailsAdapter(Context c, ArrayList<SearchModel> mSearchModelList, OnClickListener mListClickListener) {
		this.infalter = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mContext = c;
		this.mSearchModelList = mSearchModelList;
		this.mListClickListener = mListClickListener;

		WindowManager w = ((Activity) mContext).getWindowManager();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			Point size = new Point();
			w.getDefaultDisplay().getSize(size);
			mDeviceWidth = size.x;
		} else {
			Display d = w.getDefaultDisplay();
			mDeviceWidth = d.getWidth();
		}

	}

	@Override
	public int getCount() {
		return mSearchModelList.size();
	}

	@Override
	public Object getItem(int position) {
		return mSearchModelList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	View vi ;
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		vi = convertView;
		if(convertView == null){
			infalter = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		vi = infalter.inflate(R.layout.row_primium_home_tab, null);


		if(Utils.validateString(mSearchModelList.get(position).default_image)) {
			Picasso.with(mContext).load(mSearchModelList.get(position).default_image).resize(mDeviceWidth, (int) (mDeviceWidth * 0.55)).centerCrop().placeholder(R.drawable.image_not_found).error(R.drawable.image_not_found).into(((ImageView) vi.findViewById(R.id.primium_imageview)));
		}
		
//		if(mSearchModelList.get(position).default_image == null){
//			
//			((ImageView) vi.findViewById(R.id.primium_imageview)).setVisibility(View.GONE);
//			((TextView) vi.findViewById(R.id.title_primium)).setVisibility(View.GONE);
//			((TextView) vi.findViewById(R.id.offer_text_primium)).setVisibility(View.GONE);
//			((TextView) vi.findViewById(R.id.card_primium)).setVisibility(View.GONE);
//			((TextView) vi.findViewById(R.id.primium_offer_type)).setVisibility(View.GONE);
//			((TextView) vi.findViewById(R.id.primium_mall_name)).setVisibility(View.GONE);
//			((LinearLayout) vi.findViewById(R.id.ll1)).setVisibility(View.GONE);
//		} else {
//			((ImageView) vi.findViewById(R.id.primium_imageview)).setVisibility(View.VISIBLE);
//			((TextView) vi.findViewById(R.id.title_primium)).setVisibility(View.VISIBLE);
//			((TextView) vi.findViewById(R.id.offer_text_primium)).setVisibility(View.VISIBLE);
//			((TextView) vi.findViewById(R.id.card_primium)).setVisibility(View.VISIBLE);
//			((TextView) vi.findViewById(R.id.primium_offer_type)).setVisibility(View.VISIBLE);
//			((TextView) vi.findViewById(R.id.primium_mall_name)).setVisibility(View.VISIBLE);
//			((LinearLayout) vi.findViewById(R.id.ll1)).setVisibility(View.VISIBLE);
//		}

		if(Utils.validateString(mSearchModelList.get(position).title)){
			((TextView) vi.findViewById(R.id.title_primium)).setText(mSearchModelList.get(position).title);
		} 
		
//		if(mSearchModelList.get(position).title.equalsIgnoreCase("aaa") || mSearchModelList.get(position).title.equalsIgnoreCase("CEO")){
//			vi.setVisibility(View.GONE);
//		} else {
//			vi.setVisibility(View.VISIBLE);
//		}

		if(Utils.validateString(mSearchModelList.get(position).merchant_merchant_name)){
			((TextView) vi.findViewById(R.id.offer_text_primium)).setText(mSearchModelList.get(position).merchant_merchant_name);
		}

		if(Utils.validateString(mSearchModelList.get(position).card)){
			((TextView) vi.findViewById(R.id.card_primium)).setText(mSearchModelList.get(position).card);
		}

		if(Utils.validateString(mSearchModelList.get(position).offer_type)){
			((TextView) vi.findViewById(R.id.primium_offer_type)).setText(mSearchModelList.get(position).offer_type);
		}

		if(Utils.validateString(mSearchModelList.get(position).mall_name_name)){
			((TextView) vi.findViewById(R.id.primium_mall_name)).setText(mSearchModelList.get(position).mall_name_name);
		}

		vi.setTag(position);
		vi.setOnClickListener(mListClickListener);
		return vi;
	}
}