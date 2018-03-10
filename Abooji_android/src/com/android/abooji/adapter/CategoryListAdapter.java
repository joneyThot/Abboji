package com.android.abooji.adapter;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.abooji.R;
import com.android.abooji.logger.Logger;
import com.android.abooji.model.CategoryTypeModel;
import com.android.abooji.view.TextViewPlus;

public class CategoryListAdapter extends BaseAdapter{

	private Context mContext;
	private LayoutInflater infalter;
	private ArrayList<CategoryTypeModel> mCategoryNames;
	private TextView mCheckedTextView;


	public CategoryListAdapter(Context mContext, ArrayList<CategoryTypeModel> mCategoryNames, TextView SelectedTextView) {
		super();
		this.mContext = mContext;
		this.mCategoryNames = mCategoryNames;
		this.mCheckedTextView = SelectedTextView;

	}
		
	@Override
	public int getCount() {
		return mCategoryNames.size();
	}
	@Override
	public Object getItem(int position) {
		return mCategoryNames.get(position);
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

		vi = infalter.inflate(R.layout.row_dialog_spinner, null);
		TextViewPlus txt_name = (TextViewPlus)vi.findViewById(R.id.row_items_textview);
		txt_name.setText(mCategoryNames.get(position).category_name);
		CheckBox checkBox = (CheckBox)vi.findViewById(R.id.CheckBox);


		if (mCategoryNames.get(position).isChecked) {
			checkBox.setChecked(true);
			Logger.i("Category", position + ":" + "true");
		} else {
			checkBox.setChecked(false);
			Logger.i("Category", position + ":" + "false");
		}

		
		checkBox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if (!mCategoryNames.get(position).isChecked)
					mCategoryNames.get(position).isChecked = true;
				else
					mCategoryNames.get(position).isChecked = false;

				setSelectedText();
				
			}
		});

		return vi; 
	}

	private void setSelectedText() {
		String selectedText = "";
		for (int i = 0; i < mCategoryNames.size(); i++) {
			if(mCategoryNames.get(i).isChecked) {
				selectedText = selectedText + mCategoryNames.get(i).category_name + ", ";
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