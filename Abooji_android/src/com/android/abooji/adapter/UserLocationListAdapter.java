package com.android.abooji.adapter;
import java.util.TreeMap;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.android.abooji.R;
import com.android.abooji.model.SaveLocationModel;
import com.android.abooji.service.Utils;
import com.android.abooji.view.TextViewPlus;

public class UserLocationListAdapter extends BaseAdapter{

	private Context mContext;
	private LayoutInflater infalter;
	private TreeMap<Integer, SaveLocationModel> mSaveLocationModel;
	private OnClickListener clickListener;

	public UserLocationListAdapter(Context mContext, TreeMap<Integer, SaveLocationModel> mSaveLocationModel2,OnClickListener deleteClick) {
		super();
		this.mContext = mContext;
		this.mSaveLocationModel = mSaveLocationModel2;
		this.clickListener = deleteClick;
	}

	@Override
	public int getCount() {
		return mSaveLocationModel.size();
	}
	@Override
	public Object getItem(int position) {
		return mSaveLocationModel.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if(convertView == null)
			infalter = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		vi = infalter.inflate(R.layout.row_get_location, null);
		TextViewPlus txt_name = (TextViewPlus)vi.findViewById(R.id.row_get_location_textview);
		if(Utils.validateString(mSaveLocationModel.get(position).getLocation_name())){
			txt_name.setText(mSaveLocationModel.get(position).getLocation_name());
		}

		vi.findViewById(R.id.row_get_location_remove_btn).setTag(position);
		vi.findViewById(R.id.row_get_location_remove_btn).setOnClickListener(clickListener);

		return vi; 
	}

}