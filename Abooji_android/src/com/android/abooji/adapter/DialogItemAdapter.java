package com.android.abooji.adapter;

import java.util.ArrayList;
import com.android.abooji.R;
import com.android.abooji.logger.Logger;
import com.android.abooji.model.ItemModel;
import com.android.abooji.view.TextViewPlus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class DialogItemAdapter extends BaseAdapter {

	private ArrayList<ItemModel> list;
	private Context mContext;
	private LayoutInflater infalter;
	private TextView mCheckedTextView;

	public DialogItemAdapter(Context context, ArrayList<ItemModel> list, TextView SelectedTextView) {
		super();
		this.mContext = context;
		this.list = list;
		this.mCheckedTextView = SelectedTextView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}


	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}


	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if(convertView == null)
			infalter = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		vi = infalter.inflate(R.layout.row_dialog_spinner, null);
		TextViewPlus txt_name = (TextViewPlus)vi.findViewById(R.id.row_items_textview);
		txt_name.setText(list.get(position).getName());
		CheckBox checkBox = (CheckBox)vi.findViewById(R.id.CheckBox);


		if (list.get(position).selected) {
			checkBox.setChecked(true);
			Logger.i("Category", position + ":" + "true");
		} else {
			checkBox.setChecked(false);
			Logger.i("Category", position + ":" + "false");
		}

		
		checkBox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if (!list.get(position).selected)
					list.get(position).selected = true;
				else
					list.get(position).selected = false;

				setSelectedText();
				
			}
		});

		return vi; 
	}

	private void setSelectedText() {
		String selectedText = "";
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).selected) {
				selectedText = selectedText + list.get(i).getName() + ", ";
			} 
		}
		if(selectedText.contains(",")) {
			if(selectedText.substring(selectedText.length() - 2, selectedText.length() - 1).equalsIgnoreCase(",")) {
				selectedText = selectedText.substring(0, selectedText.length() - 2);
			}
		}
		mCheckedTextView.setText(selectedText);
	}

}