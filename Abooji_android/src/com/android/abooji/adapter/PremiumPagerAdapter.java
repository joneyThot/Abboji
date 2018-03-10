package com.android.abooji.adapter;

import java.util.ArrayList;
import java.util.TreeMap;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Base64;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.abooji.R;
import com.android.abooji.logger.Logger;
import com.android.abooji.model.AdvertsModel;
import com.android.abooji.model.PremiumoffersModel;
import com.android.abooji.service.Utils;
import com.android.abooji.view.TextViewPlus;
import com.squareup.picasso.Picasso;

/**
 * Created by User20 on 6/10/2015.
 */
public class PremiumPagerAdapter extends PagerAdapter {
	
	private ArrayList<PremiumoffersModel> mPremiumoffersModel;
	private Context mContext;
	private LayoutInflater mInflater;
//	private String mUserName = "";
	private OnClickListener mListClickListener;
	private int mDeviceWidth = 480;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public PremiumPagerAdapter(Context context, ArrayList<PremiumoffersModel> PremiumoffersModel, OnClickListener onListClickListener) {
		this.mContext = context;
		this.mPremiumoffersModel = PremiumoffersModel;
		this.mListClickListener = onListClickListener;
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
		return mPremiumoffersModel.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((LinearLayout) object);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((LinearLayout) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View vi = mInflater.inflate(R.layout.row_primium_home_tab, container, false);

		if(Utils.validateString(mPremiumoffersModel.get(position).default_image_path)) {
//			Logger.e("Primium", "Image Name ::" + mPremiumoffersModel.get(position).default_image_path );
			Picasso.with(mContext).load(mPremiumoffersModel.get(position).default_image_path).resize(mDeviceWidth, (int) (mDeviceWidth * 0.55)).centerCrop().placeholder(R.drawable.image_not_found).error(R.drawable.image_not_found).into(((ImageView) vi.findViewById(R.id.primium_imageview)));
		}
		
		if(Utils.validateString(mPremiumoffersModel.get(position).title)){
			((TextView) vi.findViewById(R.id.title_primium)).setText(mPremiumoffersModel.get(position).title);
		}
		
		if(Utils.validateString(mPremiumoffersModel.get(position).offer_text)){
			((TextView) vi.findViewById(R.id.offer_text_primium)).setText(mPremiumoffersModel.get(position).offer_text);
		}
		
		if(Utils.validateString(mPremiumoffersModel.get(position).card)){
			((TextView) vi.findViewById(R.id.card_primium)).setText(mPremiumoffersModel.get(position).card);
		}
		
		if(Utils.validateString(mPremiumoffersModel.get(position).offer_type)){
			((TextView) vi.findViewById(R.id.primium_offer_type)).setText(mPremiumoffersModel.get(position).offer_type);
		}

		vi.setTag(position);
		vi.setOnClickListener(mListClickListener);
		((ViewPager) container).addView(vi);

		return vi;
	}
}
