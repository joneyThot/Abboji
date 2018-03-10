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
import com.android.abooji.service.Utils;
import com.squareup.picasso.Picasso;

/**
 * Created by User20 on 6/10/2015.
 */
public class AdvertsPagerAdapter extends PagerAdapter {

	private ArrayList<AdvertsModel> mAdverts;
	private Context mContext;
	private LayoutInflater mInflater;
	//	private String mUserName = "";
	private OnClickListener mListClickListener;
	private int mDeviceWidth = 480;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public AdvertsPagerAdapter(Context context, ArrayList<AdvertsModel> adverts, OnClickListener onListClickListener) {
		this.mContext = context;
		this.mAdverts = adverts;
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
		return mAdverts.size();
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
		final View vi = mInflater.inflate(R.layout.row_advert, container, false);

		if(Utils.validateString(mAdverts.get(position).default_image_path)) {
//			Logger.e("ADAVERT", "Image Name ::" + mAdverts.get(position).default_image_path);

			Picasso.with(mContext).load(mAdverts.get(position).default_image_path).resize(mDeviceWidth, (int) (mDeviceWidth * 0.55)).centerCrop().placeholder(R.drawable.image_not_found).error(R.drawable.image_not_found).into(((ImageView) vi.findViewById(R.id.image)));
			/*Transformation transformation = new Transformation() {

				@Override public Bitmap transform(Bitmap source) {
					int targetWidth = mDeviceWidth;

					double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
					int targetHeight = (int) (targetWidth * aspectRatio);

					if(targetHeight > targetWidth) {
						targetHeight = targetWidth;
					}

					Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
					if (result != source) {
						// Same bitmap is returned if sizes are the same
						source.recycle();
					}
					return result;
				}

				@Override public String key() {
					return "transformation" + " desiredWidth";
				}
			};

			Picasso.with(mContext)
			.load(mAdverts.get(position).image)
			.placeholder(R.drawable.sample_image3)
//			.resize(mDeviceWidth, mDeviceWidth*2/3)
			.error(R.drawable.sample_image3)
			.transform(transformation)
			.into((ImageView) vi.findViewById(R.id.feedImage));*/
		}
		//			Picasso.with(mContext).load(mAdverts.get(position).image).fit().centerCrop().into(((ImageView) vi.findViewById(R.id.feedImage)));

		/*LinearLayout linearLayout = (LinearLayout) vi.findViewById(R.id.venueDetailsLayout);
		for (int i = 0; i < mAdverts.get(position).couponsArray.size(); i++) {
			LayoutInflater rowView = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = rowView.inflate(R.layout.row_venue_details, null);

			if(Utils.validateString(mAdverts.get(position).couponsArray.get(i).name)) {
				((TextView) v.findViewById(R.id.name)).setText(mAdverts.get(position).couponsArray.get(i).name);
				((TextView) v.findViewById(R.id.name)).setSelected(true);
				((TextView) v.findViewById(R.id.name)).setVisibility(View.VISIBLE);
			} else {
				((TextView) v.findViewById(R.id.name)).setVisibility(View.GONE);
			}
			if(Utils.validateString(mAdverts.get(position).couponsArray.get(i).description)) {
				try {
					byte[] data = Base64.decode(mAdverts.get(position).couponsArray.get(i).description, Base64.DEFAULT);
					String text = new String(data, "UTF-8");
					((TextView) v.findViewById(R.id.detail)).setText(Html.fromHtml(text));
					((TextView) v.findViewById(R.id.detail)).setVisibility(View.VISIBLE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				((TextView) v.findViewById(R.id.detail)).setVisibility(View.GONE);
			}
			linearLayout.addView(v);
		}

		for (int i = 0; i < mAdverts.get(position).jobArray.size(); i++) {
			LayoutInflater rowView = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = rowView.inflate(R.layout.row_venue_details, null);

			if(Utils.validateString(mAdverts.get(position).jobArray.get(i).title)) {
				((TextView) v.findViewById(R.id.name)).setText(mAdverts.get(position).jobArray.get(i).title);
				((TextView) v.findViewById(R.id.name)).setSelected(true);
				((TextView) v.findViewById(R.id.name)).setVisibility(View.VISIBLE);
			} else {
				((TextView) v.findViewById(R.id.name)).setVisibility(View.GONE);
			}
			if(Utils.validateString(mAdverts.get(position).jobArray.get(i).description)) {
				((TextView) v.findViewById(R.id.detail)).setText(mAdverts.get(position).jobArray.get(i).description);
				((TextView) v.findViewById(R.id.detail)).setVisibility(View.VISIBLE);
			} else {
				((TextView) v.findViewById(R.id.detail)).setVisibility(View.GONE);
			}
			linearLayout.addView(v);
		}*/

		vi.setTag(position);
		vi.setOnClickListener(mListClickListener);
		((ViewPager) container).addView(vi);

		return vi;
	}
}
