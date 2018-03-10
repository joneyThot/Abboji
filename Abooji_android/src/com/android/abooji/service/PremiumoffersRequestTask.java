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
import com.android.abooji.model.PremiumoffersModel;

public class PremiumoffersRequestTask extends AsyncTask<String,Integer, Object> {

	public Context mContext;
	private ProgressDialog mProgressDialog;
	private AsyncCallListener mAsyncCallListener;
	private String mErrorMessage;
	private boolean mIsError = false;

	public PremiumoffersRequestTask(Context mContext)
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
		
		ArrayList<PremiumoffersModel> premiumoffersModelslist = new ArrayList<PremiumoffersModel>();
		String result = null;
		String getServerPath = Utils.URL_SERVER_ADDRESS + "premiumoffers.php"; 
		try
		{
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(10);
			
			result = Utils.postRequest(getServerPath, nameValuePairs);

			JSONObject jObj = new JSONObject(result.toString());
			
			JSONArray premiumListingArray = jObj.getJSONArray("premiumListing");
			for (int i=0; i<premiumListingArray.length(); i++) {
				JSONObject issue = premiumListingArray.getJSONObject(i);
				PremiumoffersModel premiumoffersModel = new PremiumoffersModel();
				premiumoffersModel.setId(issue.getString("id"));
				premiumoffersModel.setUser_type(issue.getString("user_type"));
				premiumoffersModel.setUser_id(issue.getString("user_id"));
				premiumoffersModel.setOffer_type(issue.getString("offer_type"));
				premiumoffersModel.setOffer_ad_width(issue.getString("offer_ad_width"));
				premiumoffersModel.setStart_date(issue.getString("start_date"));
				premiumoffersModel.setEnd_date(issue.getString("end_date"));
				premiumoffersModel.setHours(issue.getString("hours"));
				premiumoffersModel.setOffer_image(issue.getString("offer_image"));
				premiumoffersModel.setDefault_image(issue.getString("default_image"));
				premiumoffersModel.setCategory_id(issue.getString("category_id"));
				premiumoffersModel.setMerchant_ids(issue.getString("merchant_ids"));
				premiumoffersModel.setCc_ids(issue.getString("cc_ids"));
				premiumoffersModel.setOutlet_id(issue.getString("outlet_id"));
				premiumoffersModel.setTitle(issue.getString("title"));
				premiumoffersModel.setOffer_text(issue.getString("offer_text"));
				premiumoffersModel.setDiscount_amt(issue.getString("discount_amt"));
				premiumoffersModel.setDisplay_location(issue.getString("display_location"));
				premiumoffersModel.setLocation(issue.getString("location"));
				premiumoffersModel.setLatitude(issue.getString("latitude"));
				premiumoffersModel.setLongitude(issue.getString("longitude"));
				premiumoffersModel.setDescription(issue.getString("description"));
				premiumoffersModel.setCard(issue.getString("card"));
				premiumoffersModel.setTime_slot(issue.getString("time_slot"));
				premiumoffersModel.setOffer_start_date(issue.getString("offer_start_date"));
				premiumoffersModel.setOffer_end_date(issue.getString("offer_end_date"));
				premiumoffersModel.setMerchant_merchant_name(issue.getString("merchant_merchant_name"));
				premiumoffersModel.setMerchant_employer_outlet_name(issue.getString("merchant_employer_outlet_name"));
				premiumoffersModel.setCard_company_company_name(issue.getString("card_company_company_name"));
				premiumoffersModel.setCc_card_card_name(issue.getString("cc_card_card_name"));
				premiumoffersModel.setMall_mall_name(issue.getString("mall_mall_name"));
				premiumoffersModel.setMall_name_name(issue.getString("mall_name_name"));
				premiumoffersModel.setOutlet_name(issue.getString("outlet_name"));
				premiumoffersModel.setPromo_name(issue.getString("promo_name"));
				premiumoffersModel.setDefault_image_path(issue.getString("default_image_path"));

				premiumoffersModelslist.add(premiumoffersModel);
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return premiumoffersModelslist;
	}
}
