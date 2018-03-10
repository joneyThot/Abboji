package com.android.abooji.adapter;

import java.util.TreeMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.abooji.R;
import com.android.abooji.model.SaveLocationModel;
import com.android.abooji.view.TextViewPlus;

public class TodaysLocationAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater infalter;
	private TreeMap<Integer, SaveLocationModel> data;
	    private OnClickListener mOnClickListener;

	public TodaysLocationAdapter(Context c, TreeMap<Integer, SaveLocationModel> mSaveLocationModel,OnClickListener mOnClickListener) {
		this.infalter = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mContext = c;
		this.data = mSaveLocationModel;
		this.mOnClickListener = mOnClickListener;

	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
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
		vi = infalter.inflate(R.layout.row_today, null);

		TextViewPlus txt_name = (TextViewPlus)vi.findViewById(R.id.name);
		TextViewPlus txt_offer = (TextViewPlus)vi.findViewById(R.id.offer);
		TextViewPlus txt_todayonly = (TextViewPlus)vi.findViewById(R.id.todayonly);

		
		try {
			txt_name.setText(data.get(position).location_name);
			txt_offer.setText("0");
			txt_todayonly.setText("0");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

				vi.setTag(position);
				vi.setOnClickListener(mOnClickListener);       
		return vi;
	}
}
