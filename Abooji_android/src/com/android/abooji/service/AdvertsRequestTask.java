package com.android.abooji.service;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.android.abooji.R;
import com.android.abooji.logger.Logger;
import com.android.abooji.model.AdvertsModel;

public class AdvertsRequestTask extends AsyncTask<String,Integer, Object> {

	public Context mContext;
	private ProgressDialog mProgressDialog;
	private AsyncCallListener mAsyncCallListener;
	private String mErrorMessage;
	private boolean mIsError = false;

	public AdvertsRequestTask(Context mContext)
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
		return adverts();
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

	private Object adverts() {

//		TreeMap<Integer, AdvertsModel> advertsModelList = new TreeMap<Integer, AdvertsModel>();
		ArrayList<AdvertsModel> advertsModelList = new ArrayList<AdvertsModel>();
		String result = null;
		String getServerPath = Utils.URL_SERVER_ADDRESS + "adverts.php"; 
		try
		{
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(10);

			result = Utils.postRequest(getServerPath, nameValuePairs);

			JSONObject jObj = new JSONObject(result.toString());

			//			Iterator iterator = jObj.keys();
			//			while (iterator.hasNext()) {
			//				String key = (String) iterator.next();
			//				if (Utils.validateString(key) /*&& key.equalsIgnoreCase("status")*/) {
			//					
			//				} else {
			JSONArray advrtsArray = jObj.getJSONArray("advrts");
			for (int i=0; i<advrtsArray.length(); i++) {
				JSONObject issue = advrtsArray.getJSONObject(i);
				AdvertsModel advertsModel = new AdvertsModel();
				advertsModel.setId(issue.getString("id"));
				advertsModel.setUser_type(issue.getString("user_type"));
				advertsModel.setUser_id(issue.getString("user_id"));
				advertsModel.setOffer_type(issue.getString("offer_type"));
				advertsModel.setOffer_ad_width(issue.getString("offer_ad_width"));
				advertsModel.setStart_date(issue.getString("start_date"));
				advertsModel.setEnd_date(issue.getString("end_date"));
				advertsModel.setHours(issue.getString("hours"));
				advertsModel.setOffer_image(issue.getString("offer_image"));
				advertsModel.setDefault_image(issue.getString("default_image"));
				advertsModel.setCategory_id(issue.getString("category_id"));
				advertsModel.setMerchant_ids(issue.getString("merchant_ids"));
				advertsModel.setCc_ids(issue.getString("cc_ids"));
				advertsModel.setOutlet_id(issue.getString("outlet_id"));
				advertsModel.setTitle(issue.getString("title"));
				advertsModel.setOffer_text(issue.getString("offer_text"));
				advertsModel.setDiscount_amt(issue.getString("discount_amt"));
				advertsModel.setDisplay_location(issue.getString("display_location"));
				advertsModel.setLocation(issue.getString("location"));
				advertsModel.setLatitude(issue.getString("latitude"));
				advertsModel.setLongitude(issue.getString("longitude"));
				advertsModel.setDescription(issue.getString("description"));
				advertsModel.setCard(issue.getString("card"));
				advertsModel.setTime_slot(issue.getString("time_slot"));
				advertsModel.setOffer_start_date(issue.getString("offer_start_date"));
				advertsModel.setOffer_end_date(issue.getString("offer_end_date"));
				advertsModel.setDefault_image_path(issue.getString("default_image_path"));

				advertsModelList.add(advertsModel);
			}
			//				}
			//			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return advertsModelList;
	}
}
