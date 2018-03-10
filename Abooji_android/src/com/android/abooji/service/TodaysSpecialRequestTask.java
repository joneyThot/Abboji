package com.android.abooji.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.android.abooji.R;
import com.android.abooji.logger.Logger;
import com.android.abooji.model.TodaysSpecialModel;

public class TodaysSpecialRequestTask extends AsyncTask<String,Integer, Object> {

	public Context mContext;
	private ProgressDialog mProgressDialog;
	private AsyncCallListener mAsyncCallListener;
	private String mErrorMessage;
	private boolean mIsError = false;

	public TodaysSpecialRequestTask(Context mContext)
	{
		this.mContext = mContext;
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setMessage(mContext.getResources().getString(R.string.loading));
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.setCancelable(false);
	}
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (mProgressDialog != null && !mProgressDialog.isShowing())
		{
			mProgressDialog.show();
		}
	}

	@Override
	protected Object doInBackground(String... params) {
		return permiumoffers();
	}
	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		if (mProgressDialog != null && mProgressDialog.isShowing())
		{
			mProgressDialog.dismiss();
		}
		if(mAsyncCallListener != null) {
			if(mIsError){
				Logger.e("","In Asnyc call->errorMessage:"+mErrorMessage);
				mAsyncCallListener.onErrorReceived(mErrorMessage);
			}
			else
				mAsyncCallListener.onResponseReceived(result);
		}
	}

	public void setAsyncCallListener(AsyncCallListener listener) {
		this.mAsyncCallListener = listener;
	}

	private Object permiumoffers() {
		
		ArrayList<TodaysSpecialModel> todaysSpecialModelsList = new ArrayList<TodaysSpecialModel>();
		String result = null;
		String getServerPath = Utils.URL_SERVER_ADDRESS + "todayspecial.php"; 
		try
		{
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(10);	
			result = Utils.postRequest(getServerPath, nameValuePairs);
			JSONObject jObj = new JSONObject(result.toString());
			
			JSONArray todaysSpecialArray = jObj.getJSONArray("todaysspecial");
			for (int i = 0; i < todaysSpecialArray.length(); i++) {
				JSONObject issue = todaysSpecialArray.getJSONObject(i);
				TodaysSpecialModel todaysSpecialModel = new TodaysSpecialModel();
				todaysSpecialModel.setId(issue.getString("id"));
				todaysSpecialModel.setUser_type(issue.getString("user_type"));
				todaysSpecialModel.setUser_id(issue.getString("user_id"));
				todaysSpecialModel.setOffer_type(issue.getString("offer_type"));
				todaysSpecialModel.setOffer_ad_width(issue.getString("offer_ad_width"));
				todaysSpecialModel.setStart_date(issue.getString("start_date"));
				todaysSpecialModel.setEnd_date(issue.getString("end_date"));
				todaysSpecialModel.setHours(issue.getString("hours"));
				todaysSpecialModel.setOffer_image(issue.getString("offer_image"));
				todaysSpecialModel.setDefault_image(issue.getString("default_image"));
				todaysSpecialModel.setCategory_id(issue.getString("category_id"));
				todaysSpecialModel.setMerchant_ids(issue.getString("merchant_ids"));
				todaysSpecialModel.setCc_ids(issue.getString("cc_ids"));
				todaysSpecialModel.setOutlet_id(issue.getString("outlet_id"));
				todaysSpecialModel.setTitle(issue.getString("title"));
				todaysSpecialModel.setOffer_text(issue.getString("offer_text"));
				todaysSpecialModel.setDiscount_amt(issue.getString("discount_amt"));
				todaysSpecialModel.setDisplay_location(issue.getString("display_location"));
				todaysSpecialModel.setLocation(issue.getString("location"));
				todaysSpecialModel.setLatitude(issue.getString("latitude"));
				todaysSpecialModel.setLongitude(issue.getString("longitude"));
				todaysSpecialModel.setDescription(issue.getString("description"));
				todaysSpecialModel.setCard(issue.getString("card"));
				todaysSpecialModel.setTime_slot(issue.getString("time_slot"));
				todaysSpecialModel.setOffer_start_date(issue.getString("offer_start_date"));
				todaysSpecialModel.setOffer_end_date(issue.getString("offer_end_date"));
				todaysSpecialModel.setMerchant_merchant_name(issue.getString("merchant_merchant_name"));
				todaysSpecialModel.setMerchant_employer_outlet_name(issue.getString("merchant_employer_outlet_name"));
				todaysSpecialModel.setCard_company_company_name(issue.getString("card_company_company_name"));
				todaysSpecialModel.setCc_card_card_name(issue.getString("cc_card_card_name"));
				todaysSpecialModel.setMall_mall_name(issue.getString("mall_mall_name"));
				todaysSpecialModel.setMall_name_name(issue.getString("mall_name_name"));
				todaysSpecialModel.setOutlet_name(issue.getString("outlet_name"));
				todaysSpecialModel.setPromo_name(issue.getString("promo_name"));
				todaysSpecialModel.setDefault_image_path(issue.getString("default_image_path"));
				
				todaysSpecialModelsList.add(todaysSpecialModel);
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return todaysSpecialModelsList;
	}
}
