package com.android.abooji.fragment;

import java.util.TreeMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.abooji.DashboardLoginActivity;
import com.android.abooji.R;
import com.android.abooji.adapter.ShoppingListAdapter;
import com.android.abooji.model.SaveShoppingListModel;
import com.android.abooji.service.AsyncCallListener;
import com.android.abooji.service.DeleteShoppingListNameRequestTask;
import com.android.abooji.service.GetShoppingListRequestTask;
import com.android.abooji.service.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class ShoppingListEditFragment extends BaseFragment{
	View mRootView;
	Fragment fm;
	ListView mShoppingList;
	ShoppingListAdapter mShoppingListAdapter;
//	ArrayList<SaveShoppingListModel> mSaveShoppingListModel = new ArrayList<SaveShoppingListModel>();
	TreeMap<Integer, SaveShoppingListModel> mSaveShoppingListModel = new TreeMap<Integer, SaveShoppingListModel>();
	private SharedPreferences mSharedPreferences;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_edit_shopping_lsit, container, false);
		initview();
		return mRootView;

	}

	private void initview() {
		mSharedPreferences = getActivity().getSharedPreferences(Utils.PREF_NAME, Context.MODE_PRIVATE); 
		((DashboardLoginActivity) getActivity()).setTitleText(getString(R.string.title_shoppingeditext), View.VISIBLE, View.VISIBLE, View.VISIBLE);
		
		mActivity.getSlidingMenu().setMode(SlidingMenu.LEFT);
		mActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
		mShoppingList = (ListView) mRootView.findViewById(R.id.shoppingList);

		getShoppingList(mSharedPreferences.getString(Utils.USER_ID, ""));
	}

	private void getShoppingList(String user_id) {
		if(Utils.checkInternetConnection(getActivity())){
			GetShoppingListRequestTask getShoppingListRequestTask = new GetShoppingListRequestTask(getActivity());
			getShoppingListRequestTask.setAsyncCallListener(new AsyncCallListener() {

				@Override
				public void onResponseReceived(Object response) {

					//					ArrayList<SaveShoppingListModel> temp = new ArrayList<SaveShoppingListModel>();
					//					temp = (ArrayList<SaveShoppingListModel>) response;
					//					if(temp != null){
					//						for (int i = temp.size() - 1; i >= 0; i--) {
					//							mSaveShoppingListModel.add(temp.get(i));
					//						}

					mSaveShoppingListModel = (TreeMap<Integer, SaveShoppingListModel>) response;
					if(mSaveShoppingListModel != null){
						mShoppingListAdapter = new ShoppingListAdapter(getActivity(), mSaveShoppingListModel,mDeleteShoppingname);
						mShoppingList.setAdapter(mShoppingListAdapter);
						mShoppingListAdapter.notifyDataSetChanged();
					}


				}

				@Override
				public void onErrorReceived(String error) {
					Toast.makeText(getActivity(), getString(R.string.try_again), Toast.LENGTH_SHORT).show();
				}
			});
			getShoppingListRequestTask.execute(user_id);
		}else{
			Utils.showMessageDialog(getActivity(), getResources().getString(R.string.alert), getResources().getString(R.string.connection));
		}
	}

	private OnClickListener mDeleteShoppingname = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(v.getTag() != null){
				int index = (Integer) v.getTag();
				deleteListItem(mSaveShoppingListModel.get(index).getId_of_user(),mSaveShoppingListModel.get(index).getList_id());
				mSaveShoppingListModel.remove(index);
				mShoppingListAdapter.notifyDataSetChanged();
			}
		}
	};

	private void deleteListItem(String id_of_user, String list_id) {
		if (Utils.checkInternetConnection(getActivity())) {
			DeleteShoppingListNameRequestTask deleteShoppingListNameRequestTask = new DeleteShoppingListNameRequestTask(getActivity());
			deleteShoppingListNameRequestTask.setAsyncCallListener(new AsyncCallListener() {
				@Override
				public void onResponseReceived(Object response) {
					Toast.makeText(getActivity(), "Delete Successfully", Toast.LENGTH_SHORT).show();
				}
				@Override
				public void onErrorReceived(String error) {
					Toast.makeText(getActivity(), "Try Again", Toast.LENGTH_SHORT).show();
				}
			});
			deleteShoppingListNameRequestTask.execute(id_of_user,list_id);
		} else {
			Toast.makeText(getActivity(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
		}

	}

}
