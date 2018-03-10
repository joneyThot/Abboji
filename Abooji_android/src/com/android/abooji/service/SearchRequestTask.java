package com.android.abooji.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.android.abooji.R;
import com.android.abooji.logger.Logger;
import com.android.abooji.model.SearchModel;


public class SearchRequestTask extends AsyncTask<String,Integer, Object> {

	public Context mContext;
	private ProgressDialog mProgressDialog;
	private AsyncCallListener mAsyncCallListener;
	private String mErrorMessage;
	private boolean mIsError = false;

	public SearchRequestTask(Context mContext)
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
		return saveUser(params[0], params[1], params[2], params[3]);
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

	private Object saveUser(String keyword, String category, String card, String location) {
		ArrayList<SearchModel> searchModelArrayList = new ArrayList<SearchModel>();
//		ArrayList<SearchCategoryModel> searchCategoryModelArrayList = new ArrayList<SearchCategoryModel>();
//		ArrayList<SearchLocationModel> searchLocationModelArrayList = new ArrayList<SearchLocationModel>();
		
		String result = null;
		String getServerPath = Utils.URL_SERVER_ADDRESS + "search.php";
		try
		{
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(10);
			nameValuePairs.add(new BasicNameValuePair("keyword", keyword));
			nameValuePairs.add(new BasicNameValuePair("category", category));
			nameValuePairs.add(new BasicNameValuePair("card", card));
			nameValuePairs.add(new BasicNameValuePair("location", location));

			result = Utils.postRequest(getServerPath, nameValuePairs);

			JSONObject jObj = new JSONObject(result.toString());
			
//			JSONArray cardArray = jObj.getJSONArray("search_card");
//			for (int i = 0; i < cardArray.length(); i++) {
//				JSONObject issue = cardArray.getJSONObject(i);
//				SearchModel searchCardModel = new SearchModel();
//				searchCardModel.setId(issue.getString("id"));
//				searchCardModel.setUser_id(issue.getString("user_id"));
//				searchCardModel.setOrganization_type_id(issue.getString("organization_type_id"));
//				searchCardModel.setCard_id(issue.getString("card_id"));
//				searchCardModel.setPromo_name(issue.getString("promo_name"));
//				searchCardModel.setCard_type_id(issue.getString("card_type_id"));
//				searchCardModel.setCountry_id(issue.getString("country_id"));
//				searchCardModel.setLink_type(issue.getString("link_type"));
//				searchCardModel.setOutlet_id(issue.getString("outlet_id"));
//				searchCardModel.setPrivilefge1(issue.getString("privilefge1"));
//				searchCardModel.setValue1(issue.getString("value1"));
//				searchCardModel.setPrivilefge2(issue.getString("privilefge2"));
//				searchCardModel.setValue2(issue.getString("value2"));
//				searchCardModel.setPrivilefge3(issue.getString("privilefge3"));
//				searchCardModel.setValue3(issue.getString("value3"));
//				searchCardModel.setCard_name(issue.getString("card_name"));
//				searchCardModel.setCc_ids(issue.getString("cc_ids"));
//				searchCardModel.setCard_category_id(issue.getString("card_category_id"));
//				searchCardModel.setCc_card_id(issue.getString("cc_card_id"));
//				searchCardModel.setFirstname(issue.getString("firstname"));
//				searchCardModel.setLast_name(issue.getString("last_name"));
//				searchCardModel.setTitle(issue.getString("title"));
//				searchCardModel.setCompany_name(issue.getString("company_name"));
//				searchCardModel.setCompany_tel(issue.getString("company_tel"));
//				searchCardModel.setBuilding_name(issue.getString("building_name"));
//				searchCardModel.setBlock(issue.getString("block"));
//				searchCardModel.setStreet_name(issue.getString("street_name"));
//				searchCardModel.setUnit_number(issue.getString("unit_number"));
//				searchCardModel.setWebsite(issue.getString("website"));
//				searchCardModel.setEmail(issue.getString("email"));
//				searchCardModel.setPassword(issue.getString("password"));
//				searchCardModel.setPhone(issue.getString("phone"));
//				searchCardModel.setState_id(issue.getString("state_id"));
//				searchCardModel.setCard_id(issue.getString("city_id"));
//				searchCardModel.setArea_id(issue.getString("area_id"));
//				searchCardModel.setAddress(issue.getString("address"));
//				searchCardModel.setPincode(issue.getString("pincode"));
//				searchCardModel.setDate_joined(issue.getString("date_joined"));
//				searchCardModel.setStatus(issue.getString("status"));
//				searchCardModel.setFlag(issue.getString("flag"));
//				searchCardModel.setRejection_reason(issue.getString("rejection_reason"));
//				searchCardModel.setAvailable_credits(issue.getString("available_credits"));
//				searchCardModel.setBasic(issue.getString("basic"));
//				searchCardModel.setPremium(issue.getString("premium"));
//				searchCardModel.setBasic_premium_total(issue.getString("basic_premium_total"));
//				searchCardModel.setUEN_NUMBER(issue.getString("UEN_NUMBER"));
//				searchCardModel.setYtdrev(issue.getString("ytdrev"));
//				searchCardModel.setMtdrev(issue.getString("mtdrev"));
//				searchCardModel.setYtdads(issue.getString("ytdads"));
//				searchCardModel.setMtdads(issue.getString("mtdads"));
//				searchCardModel.setExp30(issue.getString("exp30"));
//				searchCardModel.setExp60(issue.getString("exp60"));
//				searchCardModel.setExp90(issue.getString("exp90"));
//				searchCardModel.setAdmall_id(issue.getString("admall_id"));			
//				
//				searchModelArrayList.add(searchCardModel);
//			}

			JSONArray categoryArray = jObj.getJSONArray("search_keyword_cat");
			for (int i = 0; i < categoryArray.length(); i++) {
				JSONObject issue = categoryArray.getJSONObject(i);
				SearchModel searchCategorydModel = new SearchModel();
				searchCategorydModel.setId(issue.getString("id"));
				searchCategorydModel.setUser_type(issue.getString("user_id"));
				searchCategorydModel.setUser_id(issue.getString("user_type"));
				searchCategorydModel.setOffer_type(issue.getString("offer_type"));
				searchCategorydModel.setOffer_ad_width(issue.getString("offer_ad_width"));
				searchCategorydModel.setStart_date(issue.getString("start_date"));
				searchCategorydModel.setEnd_date(issue.getString("end_date"));
				searchCategorydModel.setHours(issue.getString("hours"));
				searchCategorydModel.setOffer_image(issue.getString("offer_image"));
				searchCategorydModel.setDefault_image(issue.getString("default_image"));
				searchCategorydModel.setCategory_id(issue.getString("category_id"));
				searchCategorydModel.setMerchant_ids(issue.getString("merchant_ids"));
				searchCategorydModel.setCc_ids(issue.getString("cc_ids"));
				searchCategorydModel.setOutlet_id(issue.getString("outlet_id"));
				searchCategorydModel.setTitle(issue.getString("title"));
				searchCategorydModel.setOffer_text(issue.getString("offer_text"));
				searchCategorydModel.setDiscount_amt(issue.getString("discount_amt"));
				searchCategorydModel.setDisplay_location(issue.getString("display_location"));
				searchCategorydModel.setLocation(issue.getString("location"));
				searchCategorydModel.setLatitude(issue.getString("latitude"));
				searchCategorydModel.setLongitude(issue.getString("longitude"));
				searchCategorydModel.setDescription(issue.getString("description"));
				searchCategorydModel.setCard(issue.getString("card"));
				searchCategorydModel.setTime_slot(issue.getString("time_slot"));
				searchCategorydModel.setOffer_start_date(issue.getString("offer_start_date"));
				searchCategorydModel.setOffer_end_date(issue.getString("offer_end_date"));
				searchCategorydModel.setMerchant_merchant_name(issue.getString("merchant_merchant_name"));
				searchCategorydModel.setMerchant_employer_outlet_name(issue.getString("merchant_employer_outlet_name"));
				searchCategorydModel.setCard_company_company_name(issue.getString("card_company_company_name"));
				searchCategorydModel.setCc_card_card_name(issue.getString("cc_card_card_name"));
				searchCategorydModel.setMall_mall_name(issue.getString("mall_mall_name"));
				searchCategorydModel.setMall_name_name(issue.getString("mall_name_name"));
				searchCategorydModel.setOutlet_name(issue.getString("outlet_name"));
				searchCategorydModel.setPromo_name(issue.getString("promo_name"));
				searchCategorydModel.setDefault_image(issue.getString("default_image_path"));	
				
				searchModelArrayList.add(searchCategorydModel);				
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return searchModelArrayList;
	}
}
