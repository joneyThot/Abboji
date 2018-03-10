package com.android.abooji.adapter;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.android.abooji.R;
import com.android.abooji.model.CardTypeModel;
import com.android.abooji.service.Utils;
import com.android.abooji.view.TextViewPlus;

public class CardTypeAdapter extends BaseAdapter{

	private Context mContext;
	private LayoutInflater infalter;
	private ArrayList<CardTypeModel> data;

	public CardTypeAdapter(Context mContext, ArrayList<CardTypeModel> data) {
		super();
		this.mContext = mContext;
		this.data = data;

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
		txt_name.setText(data.get(position).card_type_title);
		CheckBox checkBox = (CheckBox)vi.findViewById(R.id.CheckBox);
		checkBox.setVisibility(View.GONE);
		/*if(Utils.validateString(data.get(position).getCard_type_title())){
			txt_name.setText(data.get(position).getCard_type_title());	
		}*/


		return vi; 
	}
}
