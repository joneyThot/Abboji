package com.android.abooji.adapter;
import java.util.TreeMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.android.abooji.R;
import com.android.abooji.model.SaveLocationModel;
import com.android.abooji.view.TextViewPlus;

public class SearchLocationAdapter extends BaseAdapter{

	private Context mContext;
	private LayoutInflater infalter;
	private TreeMap<Integer, SaveLocationModel> data;

	public SearchLocationAdapter(Context mContext, TreeMap<Integer, SaveLocationModel> mSaveLocationModel) {
		super();
		this.mContext = mContext;
		this.data = mSaveLocationModel;

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
		vi = infalter.inflate(R.layout.row_dialog_spinner, null);

		TextViewPlus txt_name = (TextViewPlus)vi.findViewById(R.id.row_items_textview);
		txt_name.setText(data.get(position).location_name);
		CheckBox checkBox = (CheckBox)vi.findViewById(R.id.CheckBox);
		checkBox.setVisibility(View.GONE);
		
		return vi; 
	}
}
