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
import com.android.abooji.model.CardDataModel;

public class CardDataRequestTask extends AsyncTask<String,Integer, Object> {

	public Context mContext;
	private ProgressDialog mProgressDialog;
	private AsyncCallListener mAsyncCallListener;
	private String mErrorMessage;
	private boolean mIsError = false;

	public CardDataRequestTask(Context mContext)
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
		return card_data();
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

	private Object card_data() {
		ArrayList<CardDataModel> cardDataModelList = new ArrayList<CardDataModel>();
		String result = null;
		String getServerPath = Utils.URL_SERVER_ADDRESS + "card_data.php"; 
		try
		{
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(10);
			
			result = Utils.postRequest(getServerPath, nameValuePairs);

			JSONObject jObj = new JSONObject(result.toString());
			
			JSONArray cardDataArray = jObj.getJSONArray("card_listing_offer");
			for (int i=0; i<cardDataArray.length(); i++) {
				JSONObject issue = cardDataArray.getJSONObject(i);
				CardDataModel cardDataModel = new CardDataModel();
				cardDataModel.setId(issue.getString("id"));
				cardDataModel.setUser_type(issue.getString("user_type"));
				cardDataModel.setUser_id(issue.getString("user_id"));
				cardDataModel.setOffer_type(issue.getString("offer_type"));
				cardDataModel.setOffer_ad_width(issue.getString("offer_ad_width"));
				cardDataModel.setStart_date(issue.getString("start_date"));
				cardDataModel.setEnd_date(issue.getString("end_date"));
				cardDataModel.setHours(issue.getString("hours"));
				cardDataModel.setOffer_image(issue.getString("offer_image"));
				cardDataModel.setDefault_image(issue.getString("default_image"));
				cardDataModel.setCategory_id(issue.getString("category_id"));
				cardDataModel.setMerchant_ids(issue.getString("merchant_ids"));
				cardDataModel.setCc_ids(issue.getString("cc_ids"));
				cardDataModel.setOutlet_id(issue.getString("outlet_id"));
				cardDataModel.setTitle(issue.getString("title"));
				cardDataModel.setOffer_text(issue.getString("offer_text"));
				cardDataModel.setDiscount_amt(issue.getString("discount_amt"));
				cardDataModel.setDisplay_location(issue.getString("display_location"));
				cardDataModel.setLocation(issue.getString("location"));
				cardDataModel.setLatitude(issue.getString("latitude"));
				cardDataModel.setLongitude(issue.getString("longitude"));
				cardDataModel.setDescription(issue.getString("description"));
				cardDataModel.setCard(issue.getString("card"));
				cardDataModel.setTime_slot(issue.getString("time_slot"));
				cardDataModel.setOffer_start_date(issue.getString("offer_start_date"));
				cardDataModel.setOffer_end_date(issue.getString("offer_end_date"));

				cardDataModelList.add(cardDataModel);
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return cardDataModelList;
	}
}
