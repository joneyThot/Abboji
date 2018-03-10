package com.android.abooji.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.android.abooji.AppConstants;
import com.android.abooji.DashboardLoginActivity;
import com.android.abooji.R;
import com.android.abooji.service.Utils;
import com.android.abooji.view.TextViewPlus;

public class SlidingMenuFragment extends BaseFragment implements OnClickListener
{
	public static View mRoot;

	private static LinearLayout mMenuLayout;
	public static ScrollView sc;
	public static int pos_x = 0, pos_y = 0;
	Fragment fr = null;
	TextViewPlus titleText;
	boolean locpressed=false;
	SharedPreferences sharedPreferences;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		mRoot = inflater.inflate(R.layout.list, null);
		sharedPreferences = getActivity().getSharedPreferences(Utils.PREF_NAME, Context.MODE_PRIVATE);
		mMenuLayout = (LinearLayout) mRoot.findViewById(R.id.menuLayout);
		titleText = (TextViewPlus)getActivity().findViewById(R.id.title);
		sc = (ScrollView)mRoot.findViewById(R.id.scrollLayout);
		
		sc.post(new Runnable() 
		{
			@Override
			public void run() 
			{
				pos_x = sc.getScrollX();
				pos_y = sc.getScrollY();
			}
		});

		for (int i = 1; i < mMenuLayout.getChildCount(); i++) 
		{
			if(i != 11)
			{
				mMenuLayout.getChildAt(i).setOnClickListener(this);
			}
		}

		return mRoot;
	}

	@Override
	public void onClick(View view) 
	{
		sc.post(new Runnable() 
		{
			@Override
			public void run() 
			{
				pos_x = sc.getScrollX();
				pos_y = sc.getScrollY();
				//				sc.scrollTo(pos_x, pos_y);
			}
		});

		//((TextView) view).setBackgroundResource(R.drawable.sidemenu_active_bg1);
		switch (view.getId()) 
		{
		case R.id.menu_homebtn:
			((DashboardLoginActivity) getActivity()).pushFragments(AppConstants.TAB_HOME, new HomeFragmentTab(), false, true);
			
			break;

		case R.id.menu_barcodebtn:
			((DashboardLoginActivity) getActivity()).pushFragments(AppConstants.TAB_BARCODE, new BarcodeTabFragment(), false, true);
			
			break;

		case R.id.menu_logoutbtn:
			Utils.storeString(sharedPreferences, Utils.USER_EMAIL, "");
			Utils.storeString(sharedPreferences, Utils.USER_PASSWORD, "");
			Utils.storeBoolean(sharedPreferences, Utils.LOGINSTATUS, false);
			getActivity().finish();
			break;

		case R.id.menu_edit_profilebtn:
			titleText.setText(R.string.title_editprofiletext);
			((DashboardLoginActivity) getActivity()).pushFragments(AppConstants.TAB_HOME, new PersonalFragmentLogin(), false, true);
			
			break;

		case R.id.menu_edit_cardsbtn:
			titleText.setText(R.string.title_editcardtext);
			((DashboardLoginActivity) getActivity()).pushFragments(AppConstants.TAB_HOME, new EditCardFragment(), false, true);
			
			break;

		case R.id.menu_edit_locationbtn:
			titleText.setText(R.string.title_editlocationtext);
			((DashboardLoginActivity) getActivity()).pushFragments(AppConstants.TAB_MAP, new EditLocationFragment(), false, true);
		
			break;

		case R.id.menu_edit_shopinglistbtn:
			titleText.setText(R.string.title_editshoppinglisttext);
			((DashboardLoginActivity) getActivity()).pushFragments(AppConstants.TAB_SHOPPING, new ShoppingListEditFragment(), false, true);
		
			break;

		case R.id.menu_edit_categorybtn:
			titleText.setText(R.string.title_editcategorytext);
			((DashboardLoginActivity) getActivity()).pushFragments(AppConstants.TAB_HOME, new CategoryFragmentLogin(), false, true);
			
			break;

		case R.id.menu_edit_notificationbtn:
			titleText.setText(R.string.title_editnotificationtext);
			((DashboardLoginActivity) getActivity()).pushFragments(AppConstants.TAB_HOME, new NotificationFragmentLogin(), false, true);
		
			break;

		case R.id.menu_changepasswordbtn:
			((DashboardLoginActivity) getActivity()).pushFragments(AppConstants.TAB_HOME, new ChangePasswordFragment(), false, true);
			
			break;

		case R.id.menu_helpbtn:
			((DashboardLoginActivity) getActivity()).pushFragments(AppConstants.TAB_HOME, new HelpFragment(), false, true);
			
			break;
			
		default:
			break;
				
		}
		((DashboardLoginActivity) getActivity()).getSlidingMenu().toggle();
				
	}

	public static void viewSelectedLayout(boolean org, int pos, int pos2) 
	{
		if(org)
		{
			//((TextView) mMenuLayout.getChildAt(pos)).setBackgroundResource(R.drawable.sidemenu_active_bg1);
			//((TextView) mMenuLayout.getChildAt(21)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.menuitem_arrow_active, 0, 0, 0);
		}
		/*else if(!org)
		{
			((TextView) mMenuLayout.getChildAt(pos)).setBackgroundResource(R.drawable.sidemenu_active_bg1);
			if(pos != pos2)
			{
				((TextView) mMenuLayout.getChildAt(pos2)).setBackgroundResource(R.drawable.sidemenu_normal_bg1);
			}
			//			((TextView) mMenuLayout.getChildAt(1)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.menuitem_arrow_active, 0, 0, 0);
		}*/
	}
	public static void viewSelectedPosition() 
	{
		sc.post(new Runnable() 
		{ 
			public void run() { 
				//				pos_x = sc.getScrollX();
				//				pos_y = sc.getScrollY();
				sc.scrollTo(pos_x, pos_y);
			} 
		});
	}
}
