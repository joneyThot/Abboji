package com.android.abooji.adapter;
import java.util.TreeMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.abooji.R;
import com.android.abooji.model.SaveShoppingListModel;
import com.android.abooji.service.Utils;
import com.android.abooji.view.TextViewPlus;

public class ShoppingListAdapter extends BaseAdapter{

	private Context mContext;
	private LayoutInflater infalter;
	private TreeMap<Integer, SaveShoppingListModel> mSaveShoppingListModel;
	private OnClickListener clickListener;


	public ShoppingListAdapter(Context mContext, TreeMap<Integer, SaveShoppingListModel> mSaveShoppingListModel2,OnClickListener deleteClick) {
		super();
		this.mContext = mContext;
		this.mSaveShoppingListModel = mSaveShoppingListModel2;
		this.clickListener = deleteClick;

	}

	@Override
	public int getCount() {
		return mSaveShoppingListModel.size();
	}
	@Override
	public Object getItem(int position) {
		return mSaveShoppingListModel.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if(convertView == null)
			infalter = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		vi = infalter.inflate(R.layout.row_get_location, null);
		TextViewPlus txt_name = (TextViewPlus)vi.findViewById(R.id.row_get_location_textview);
		if(Utils.validateString(mSaveShoppingListModel.get(position).getList_name())){
			txt_name.setText(mSaveShoppingListModel.get(position).getList_name());
		}

		vi.findViewById(R.id.row_get_location_remove_btn).setTag(position);
		vi.findViewById(R.id.row_get_location_remove_btn).setOnClickListener(clickListener);

		return vi; 
	}

}