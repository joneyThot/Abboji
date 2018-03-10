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
import android.widget.TextView;

import com.android.abooji.R;
import com.android.abooji.model.SaveLocationModel;
import com.android.abooji.model.SaveShoppingListModel;
import com.android.abooji.model.TodaysSpecialModel;
import com.android.abooji.service.Utils;
import com.android.abooji.view.TextViewPlus;
import com.squareup.picasso.Picasso;

public class TodaysDetailsAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater infalter;
    private ArrayList<TodaysSpecialModel> mTodaysDetailsList;
    private OnClickListener mListClickListener;
	private int mDeviceWidth = 480;
    
    @SuppressLint("NewApi")
	public TodaysDetailsAdapter(Context c, ArrayList<TodaysSpecialModel> mTodaysDetailsList, OnClickListener mListClickListener) {
        this.infalter = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mContext = c;
        this.mTodaysDetailsList = mTodaysDetailsList;
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
        return mTodaysDetailsList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTodaysDetailsList.get(position);
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
       
		if(Utils.validateString(mTodaysDetailsList.get(position).default_image)) {
			Picasso.with(mContext).load(mTodaysDetailsList.get(position).default_image).resize(mDeviceWidth, (int) (mDeviceWidth * 0.55)).centerCrop().placeholder(R.drawable.image_not_found).error(R.drawable.image_not_found).into(((ImageView) vi.findViewById(R.id.primium_imageview)));
		}
		
		if(Utils.validateString(mTodaysDetailsList.get(position).title)){
			((TextView) vi.findViewById(R.id.title_primium)).setText(mTodaysDetailsList.get(position).title);
		}
		
		if(Utils.validateString(mTodaysDetailsList.get(position).merchant_merchant_name)){
			((TextView) vi.findViewById(R.id.offer_text_primium)).setText(mTodaysDetailsList.get(position).merchant_merchant_name);
		}
		
		if(Utils.validateString(mTodaysDetailsList.get(position).card)){
			((TextView) vi.findViewById(R.id.card_primium)).setText(mTodaysDetailsList.get(position).card);
		}
		
		if(Utils.validateString(mTodaysDetailsList.get(position).offer_type)){
			((TextView) vi.findViewById(R.id.primium_offer_type)).setText(mTodaysDetailsList.get(position).offer_type);
		}
		
		if(Utils.validateString(mTodaysDetailsList.get(position).mall_name_name)){
			((TextView) vi.findViewById(R.id.primium_mall_name)).setText(mTodaysDetailsList.get(position).mall_name_name);
		}
		
		vi.setTag(position);
		vi.setOnClickListener(mListClickListener);
        return vi;
    }
}
