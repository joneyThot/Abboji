package com.android.abooji.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.abooji.AppConstants;
import com.android.abooji.DashboardLoginActivity;
import com.android.abooji.R;
import com.android.abooji.model.SaveShoppingListModel;
import com.android.abooji.service.AsyncCallListener;
import com.android.abooji.service.SaveShoppingListRequestTask;
import com.android.abooji.service.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class ShoppingAddNewListFragment extends BaseFragment implements OnClickListener{
	View mRootView;
	Fragment fm;
	EditText addNewList;
	private SaveShoppingListModel mSaveShoppingListModel = new SaveShoppingListModel();
	private SharedPreferences mSharedPreferences;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_add_new_shopping_list, container, false);
		initview();
		return mRootView;
	}

	private void initview() {
		mSharedPreferences = getActivity().getSharedPreferences(Utils.PREF_NAME, Context.MODE_PRIVATE); 
		((DashboardLoginActivity) getActivity()).setTitleText(getString(R.string.title_shoppingaddext), View.VISIBLE, View.VISIBLE, View.VISIBLE);
		
		mActivity.getSlidingMenu().setMode(SlidingMenu.LEFT);
		mActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
		mRootView.findViewById(R.id.add_new_shopping_list_add_btn).setOnClickListener(this);
		addNewList = (EditText) mRootView.findViewById(R.id.add_new_shopping_list_edittext);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_new_shopping_list_add_btn:
				saveShoppingList(mSharedPreferences.getString(Utils.USER_ID, ""),addNewList.getText().toString(),"","");
			break;
		}
	}

	private void saveShoppingList(String user_id,String list_name, String list, String list_id) {
		if (Utils.checkInternetConnection(getActivity())) {
			SaveShoppingListRequestTask shoppingListRequestTask = new SaveShoppingListRequestTask(getActivity());
			shoppingListRequestTask.setAsyncCallListener(new AsyncCallListener() {

				@SuppressWarnings("unchecked")
				@Override
				public void onResponseReceived(Object response) {
									
					mSaveShoppingListModel = (SaveShoppingListModel) response;
					if(mSaveShoppingListModel != null){
						((DashboardLoginActivity) getActivity()).pushFragments(AppConstants.TAB_SHOPPING, new ShoppinListTabFragment(), false, true);
					}

				}

				@Override
				public void onErrorReceived(String error) {
					Toast.makeText(getActivity(), getString(R.string.try_again), Toast.LENGTH_SHORT).show();
				}
			});
			shoppingListRequestTask.execute(user_id,list_name,list,list_id);
		} else {
			Toast.makeText(getActivity(), getString(R.string.connection), Toast.LENGTH_SHORT).show();
		}
	}
}