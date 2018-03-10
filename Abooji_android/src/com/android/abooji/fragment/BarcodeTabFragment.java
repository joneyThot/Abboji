package com.android.abooji.fragment;

import net.sourceforge.zbar.Symbol;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.abooji.DashboardLoginActivity;
import com.android.abooji.R;
import com.android.abooji.service.AsyncCallListener;
import com.android.abooji.service.BarcodeRequestTask;
import com.android.abooji.service.Utils;
import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class BarcodeTabFragment extends BaseFragment{
	Fragment fr;
	View mRootView;
	private SharedPreferences mSharedPreferences;

	//	private static final int ZBAR_SCANNER_REQUEST = 0;
	private static final int ZBAR_QR_SCANNER_REQUEST = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_barcode, container, false);
		initview();
		return mRootView;
	}

	private void initview() {

		//		  if (isCameraAvailable()) {
		//	            Intent intent = new Intent(getActivity(), ZBarScannerActivity.class);
		//	            startActivityForResult(intent, ZBAR_SCANNER_REQUEST);
		//	        } else {
		//	            Toast.makeText(this, "Rear Facing Camera Unavailable", Toast.LENGTH_SHORT).show();
		//	        }
		
		mSharedPreferences = getActivity().getSharedPreferences(Utils.PREF_NAME, Context.MODE_PRIVATE); 
//		((DashboardLoginActivity) getActivity()).setTitleText(getString(R.string.menu_barcode), View.VISIBLE, View.VISIBLE, View.VISIBLE);
//		((DashboardLoginActivity) getActivity()).findViewById(R.id.titleImageBg).setVisibility(View.VISIBLE);

		mActivity.getSlidingMenu().setMode(SlidingMenu.LEFT);
		mActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
		if (isCameraAvailable()) {
			Intent intent = new Intent(getActivity(), ZBarScannerActivity.class);
			intent.putExtra(ZBarConstants.SCAN_MODES, new int[]{Symbol.QRCODE});
			startActivityForResult(intent, ZBAR_QR_SCANNER_REQUEST);
		} else {
			Toast.makeText(getActivity(), "Rear Facing Camera Unavailable", Toast.LENGTH_SHORT).show();
		}
	}

	public boolean isCameraAvailable() {
		PackageManager pm = getActivity().getPackageManager();
		return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
	}

	@SuppressWarnings("static-access")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		//		 case ZBAR_SCANNER_REQUEST:
		case ZBAR_QR_SCANNER_REQUEST:
			if (resultCode == getActivity().RESULT_OK) {
//				Toast.makeText(getActivity(), "Scan Result = " + data.getStringExtra(ZBarConstants.SCAN_RESULT), Toast.LENGTH_SHORT).show();
				Utils.showMessageDialog(getActivity(), "Alert", data.getStringExtra(ZBarConstants.SCAN_RESULT));
				String barcode = data.getStringExtra(ZBarConstants.SCAN_RESULT);
				String[] s = barcode.split("\n");
//				for (int i = 0; i < s.length; i++) {
//					s[i] = s[i].trim();
//				}
				s[0] = s[0].trim();
				s[1] = s[1].trim();
				s[2] = s[2].trim();
				s[3] = s[3].trim();
				
				barcodeList(s[3],s[2]);
					
				Log.v("Barcode", "barcode :: " + barcode);
			} else if(resultCode == getActivity().RESULT_CANCELED && data != null) {
				String error = data.getStringExtra(ZBarConstants.ERROR_INFO);
				if(!TextUtils.isEmpty(error)) {
					Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
				}
			}
			break;
		}
	}

	private void barcodeList(String account_id, String account_type) {
		if(Utils.checkInternetConnection(getActivity())){
			BarcodeRequestTask barcodeRequestTask = new BarcodeRequestTask(getActivity());
			barcodeRequestTask.setAsyncCallListener(new AsyncCallListener() {
				@Override
				public void onResponseReceived(Object response) {
				}
				@Override
				public void onErrorReceived(String error) {
					Toast.makeText(getActivity(), getString(R.string.try_again), Toast.LENGTH_SHORT).show();
				}
			});
			barcodeRequestTask.execute(account_id,account_type);
		}else{
			Utils.showMessageDialog(getActivity(), getResources().getString(R.string.alert), getResources().getString(R.string.connection));
		}
	}
}