package com.android.abooji.service;

import java.security.MessageDigest;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.Base64;
import android.widget.EditText;
import android.widget.Toast;

import com.android.abooji.R;
import com.android.abooji.logger.Logger;

public class Utils {

	public static String URL_SERVER_ADDRESS = "http://108.163.162.132/abooji/";
	public static String IMAGE_URL = "http://108.163.162.132/abooji/uploads/discount/";

	
        
	public static String ACTIVITY_STATUS = "actvity_status";
	public static String PREF_NAME = "abooji_prefs";
	public static String TAG = "Utils";
	public static String USER_PROFILE = "user_profile";
	public static String USER_ID = "user_id";
	public static String USER_EMAIL = "email";
	public static String USER_PASSWORD = "password";
	
	public static String GENDER = "gender";
	public static String GENDER_POS = "gender_pos";
	public static String AGE_GROUP = "age_group";
	public static String AGE_GROUP_POS = "age_group_pos";
	public static String MARITAL_STATUS = "marital_Status";
	public static String MARITAL_STATUS_POS = "marital_Status_pos";
	public static String CHILDREN = "children";
	public static String CHILDREN_ID = "children_id";
	public static String CHILDREN_AGE_GROUP = "children_age_group";
	public static String CARD_CAT = "card_cat";
	public static String CARD_TYPE = "card_type";
	public static String CARDS_TYPE_POS = "card_type_POS";
	public static String FRIENDS_EMAIL = "frinends_email";
	public static String CARDS = "cards";
	public static String CARDS_POS = "cards_POS";
	public static String CATEGORY_PREFERENCE = "category_preference";
	public static String ALERT = "alert";
	public static String ALERT_POS = "alert_pos";
	public static String ALERT_TIME = "alert_time";
	public static String LIST_ID = "list_id";
	public static String SHARE_IDS = "share_ids";
	
	public static String LOCATIONDETAIL = "location_detail";
	public static String ADVERT_DETAIL = "advert_detail";
	public static String PREMIUM_DETAIL = "premium_detail";
	public static String TODAYS_DETAIL = "todays_detail";
	public static String TODAYS_CATEGORY = "todays_category";
	public static String TODAYS_CARD = "todays_card";
	public static String TODAYS_LOCATION = "todays_location";
	public static String TODAYS_ITEM_DETAIL = "todays_item_detail";
	public static String SEARCH_ITEM_DETAIL = "search_item_detail";
	public static String SEARCH_DATA = "search_data";
	
		
	public static String LOGINSTATUS = "false";
	
	public static boolean validateString(String object) {
		boolean flag = false;
		if (object != null && !object.isEmpty() && object.equalsIgnoreCase("null") != true && object.trim().length() > 0 && !object.equalsIgnoreCase("(null)")) {
			flag = true;
		}
		return flag;
	}

	public static void storeString(SharedPreferences sharedPreferences, String key, String value) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static void storeInt(SharedPreferences sharedPreferences, String key, int value) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static void storeBoolean(SharedPreferences sharedPreferences, String key, boolean value) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}


	public static String postRequest(String url, List<NameValuePair> nameValuePairs) {
		String request = "";
		String result = null;
		try {
			Logger.e(TAG, "url:: " + url);
			for (NameValuePair nvp : nameValuePairs) {
				request += nvp.getName() + "=" + nvp.getValue() + "&";
			}
			Logger.e(TAG, "request:: " + request);
			// Execute HTTP Post Request
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			HttpParams httpParameters = new BasicHttpParams();
			// Set the timeout in milliseconds until a connection is
			// established.
			int timeoutConnection = 200000;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			// Set the default socket timeout (SO_TIMEOUT) in milliseconds which
			// is the timeout for waiting for data.
			int timeoutSocket = 200000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse response1 = httpclient.execute(httppost);

			result = EntityUtils.toString(response1.getEntity());
			//			Logger.i("TAG", "result -- " + result);
			int maxLogSize = 1000;
			int start = 0, end = 0;
			for (int i = 0; i <= result.length() / maxLogSize; i++) {
				start = i * maxLogSize;
				end = (i + 1) * maxLogSize;
				end = end > result.length() ? result.length() : end;
				Logger.i("TAG", "result str -- " + result.substring(start, end));
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}
	}

	public static String postRequest(String url) {
		String result = null;
		try {
			Logger.e("TAG", "url:: " + url);
			/*
			 * for (NameValuePair nvp : nameValuePairs) { String name =
			 * nvp.getName(); String value = nvp.getValue(); Loggger.e("TAG",
			 * name +"="+value); }
			 */
			// Execute HTTP Post Request
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			HttpParams httpParameters = new BasicHttpParams();
			// Set the timeout in milliseconds until a connection is
			// established.
			int timeoutConnection = 200000;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			// Set the default socket timeout (SO_TIMEOUT) in milliseconds which
			// is the timeout for waiting for data.
			int timeoutSocket = 200000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			// httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse response1 = httpclient.execute(httppost);

			result = EntityUtils.toString(response1.getEntity());

			int maxLogSize = 1000;
			int start = 0, end = 0;
			for (int i = 0; i <= result.length() / maxLogSize; i++) {
				start = i * maxLogSize;
				end = (i + 1) * maxLogSize;
				end = end > result.length() ? result.length() : end;
				Logger.i("TAG", "" + result.substring(start, end));
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}
	}

	public static boolean checkInternetConnection(Context context) {

		try {
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			// test for connection
			if (cm.getActiveNetworkInfo() != null
					&& cm.getActiveNetworkInfo().isAvailable()
					&& cm.getActiveNetworkInfo().isConnected()) {
				return true;
			} else {
				Logger.v("", "Internet Connection Not Available");
				Toast.makeText(context, context.getResources().getString(R.string.connection), Toast.LENGTH_SHORT).show();
				return false;
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static final void showMessageDialog(Context context, String title, String message) {

		if (message != null && message.trim().length() > 0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle(title);
			builder.setCancelable(false);
			builder.setMessage(message);
			builder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.dismiss();
				}
			});
			// create alert dialog
			AlertDialog alertDialog = builder.create();
			// show it
			alertDialog.show();
		}

	}

	public static boolean isEmailValid(String email) {
		String expression = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		CharSequence inputStr = email;
		boolean flag = false;
		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			flag = true;
		}
		return flag;
	}

	public static void clearData(EditText objEdit)
	{
		objEdit.getText().clear();
	}
	
	public static String getHashKey(Context context) {
        String hashKey = "";
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                hashKey = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Logger.e("KeyHash:", hashKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hashKey;
    }
	
	public static GPSTracker getCurrentLocation(Context context) {
		GPSTracker gps = new GPSTracker(context);
		// check if GPS enabled
		if (gps.canGetLocation()) {
			// double latitude = gps.getLatitude();
			// double longitude = gps.getLongitude();
			return gps;
		} else {
			// can't get location // GPS or Network is not enabled // Ask user
			// to enable GPS/network in settings
			gps.showSettingsAlert();
		}
		return null;
	}
	
	public static void turnGPSOn(Context context){
		/*Intent intent=new Intent("android.location.GPS_ENABLED_CHANGE");
		intent.putExtra("enabled", true);
		context.sendBroadcast(intent);*/
	   String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

	    if(!provider.contains("gps")){ //if gps is disabled
	        final Intent poke = new Intent();
	        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); 
	        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
	        poke.setData(Uri.parse("3")); 
	        context.sendBroadcast(poke);
	    }
	}
}
