package com.android.abooji.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.android.abooji.R;
import com.android.abooji.fragment.LocationFragment;
import com.android.abooji.logger.Logger;
import com.android.abooji.service.PlaceDetailsJSONParser;
import com.android.abooji.service.PlaceJSONParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapCustomDialog extends DialogFragment implements OnClickListener{
	private MapFragment fragment;
	View mRootView;
	TextViewPlus add, close; 	
	int hwidth,hHeight;
	AutoCompleteTextView atvPlaces;
	DownloadTask placesDownloadTask;
	DownloadTask placeDetailsDownloadTask;
	ParserTask placesParserTask;
	ParserTask placeDetailsParserTask;
	GoogleMap googleMap;
	final int PLACES = 0;
	final int PLACES_DETAILS = 1;
	Marker marker;
	ArrayList<Marker> marraylst_marker = new ArrayList<Marker>();
	LatLng point;
	Button ok, cancel;
	double latitude = 0.0, longitude = 0.0;
	//	Dialog mapDialog;
	EditText mLocationName, mLocationLatLong;
	SupportMapFragment myMAPF;
	LocationFragment locationFragment;

	public MapCustomDialog(LocationFragment m) {
		fragment = new MapFragment();
		locationFragment = m;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (mRootView != null) {
			ViewGroup parent = (ViewGroup) mRootView.getParent();
			if (parent != null)
				parent.removeView(mRootView);
		}
		try {
			mRootView = inflater.inflate(R.layout.fragment_location, container, false);

		} catch (InflateException e) {

		}
		
		Display display = getActivity().getWindowManager().getDefaultDisplay(); 

		hwidth = display.getWidth();  // deprecated
		hHeight = display.getHeight(); 
		
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setLayout((hwidth/2)+200, (hHeight/3)+200);
//		mapDialog.show();
		initview();

		return mRootView;
	}

	private void initview() {
		mRootView.findViewById(R.id.rl1).setVisibility(View.VISIBLE);
		mRootView.findViewById(R.id.add_btn).setVisibility(View.GONE);
		mRootView.findViewById(R.id.close_btn).setOnClickListener(this);
		atvPlaces = (AutoCompleteTextView) mRootView.findViewById(R.id.atv_places);
		atvPlaces.setThreshold(1);

		
		if(googleMap == null){
			FragmentManager myFM = getActivity().getFragmentManager();
			fragment = (MapFragment) myFM.findFragmentById(R.id.map);
			googleMap = fragment.getMap();
			googleMap.clear();
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		} 

		
		mapinit();
	}

	private void mapinit() {

		atvPlaces.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				if (atvPlaces.length() == 0) {
					if (!marraylst_marker.isEmpty()) {
						googleMap.clear();
						marker.remove();
						marraylst_marker.clear();
					}
				}
				placesDownloadTask = new DownloadTask(PLACES);
				String url = getAutoCompleteUrl(s.toString());
				placesDownloadTask.execute(url);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		atvPlaces.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long id) {

				ListView lv = (ListView) arg0;
				SimpleAdapter adapter = (SimpleAdapter) arg0.getAdapter();

				HashMap<String, String> hm = (HashMap<String, String>) adapter
						.getItem(index);
				placeDetailsDownloadTask = new DownloadTask(PLACES_DETAILS);
				String url = getPlaceDetailsUrl(hm.get("reference"));
				placeDetailsDownloadTask.execute(url);

			}
		});
	}

	private String getAutoCompleteUrl(String place) {
		String key = "key=AIzaSyAU93CCu4spbwvIXmAtbcvn-VDUMei4wTo";
		String input = "input=" + place;
		String types = "types=geocode";
		String sensor = "sensor=false";
		String parameters = input + "&" + types + "&" + sensor + "&" + key;
		String output = "json";
		String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"	+ output + "?" + parameters;
		return url;
	}

	private String getPlaceDetailsUrl(String ref) {
		String key = "key=AIzaSyAU93CCu4spbwvIXmAtbcvn-VDUMei4wTo";
		String reference = "reference=" + ref;
		String sensor = "sensor=false";
		String parameters = reference + "&" + sensor + "&" + key;
		String output = "json";
		String url = "https://maps.googleapis.com/maps/api/place/details/" + output + "?" + parameters;
		Logger.i("URL", "url :: " + url);
		return url;
	}

	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.connect();
			iStream = urlConnection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
			StringBuffer sb = new StringBuffer();
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			data = sb.toString();
			br.close();

		} catch (Exception e) {
			Logger.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

	private class DownloadTask extends AsyncTask<String, Void, String> {
		private int downloadType = 0;

		public DownloadTask(int type) {
			this.downloadType = type;
		}

		@Override
		protected String doInBackground(String... url) {
			String data = "";
			try {
				data = downloadUrl(url[0]);
			} catch (Exception e) {
				Logger.d("Background Task", e.toString());
			}
			return data;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			switch (downloadType) {
			case PLACES:
				placesParserTask = new ParserTask(PLACES);
				placesParserTask.execute(result);
				break;

			case PLACES_DETAILS:
				placeDetailsParserTask = new ParserTask(PLACES_DETAILS);
				placeDetailsParserTask.execute(result);
			}
		}
	}

	private class ParserTask extends
	AsyncTask<String, Integer, List<HashMap<String, String>>> {
		int parserType = 0;

		public ParserTask(int type) {
			this.parserType = type;
		}

		@Override
		protected List<HashMap<String, String>> doInBackground(
				String... jsonData) {

			JSONObject jObject;
			List<HashMap<String, String>> list = null;

			try {
				jObject = new JSONObject(jsonData[0]);

				switch (parserType) {
				case PLACES:
					PlaceJSONParser placeJsonParser = new PlaceJSONParser();
					list = placeJsonParser.parse(jObject);
					break;
				case PLACES_DETAILS:
					PlaceDetailsJSONParser placeDetailsJsonParser = new PlaceDetailsJSONParser();
					list = placeDetailsJsonParser.parse(jObject);
				}

			} catch (Exception e) {
				Logger.d("Exception", e.toString());
			}
			return list;
		}

		@Override
		protected void onPostExecute(List<HashMap<String, String>> result) {

			switch (parserType) {
			case PLACES:
				String[] from = new String[] { "description" };
				int[] to = new int[] { android.R.id.text1 };
				// Creating a SimpleAdapter for the AutoCompleteTextView
				SimpleAdapter adapter = new SimpleAdapter(getActivity(),result, android.R.layout.simple_list_item_1, from, to);
				// Setting the adapter
				atvPlaces.setAdapter(adapter);
				break;
			case PLACES_DETAILS:
				HashMap<String, String> hm = result.get(0);
				latitude = Double.parseDouble(hm.get("lat"));
				longitude = Double.parseDouble(hm.get("lng"));

				FragmentManager myFM = getActivity().getFragmentManager();
				fragment = (MapFragment) myFM.findFragmentById(R.id.map);
				googleMap = fragment.getMap();

				point = new LatLng(latitude, longitude);
				Logger.i("LATLANG", "Latitude : " + latitude + " & Longitude : "+ longitude);
				CameraPosition cameraPosition = new CameraPosition.Builder().target(point).zoom(12).build();
				googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
				MarkerOptions options = new MarkerOptions();
				options.position(point);
				options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
				options.title("Position");
				options.snippet("Latitude:" + latitude + ",Longitude:"+ longitude);
				marker = googleMap.addMarker(options);
				marraylst_marker.add(marker);
				String Location_name = atvPlaces.getText().toString();
				String LatLang = ""+ latitude + "," + longitude; 
				locationFragment.setLatLang(Location_name, LatLang);
				break;
			}
		}
	}

	public MapFragment getFragment() {
		return fragment;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_btn:

			break;

		case R.id.close_btn:
			getDialog().dismiss();
			break;

		default:
			break;
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (googleMap != null) {
			getActivity().getFragmentManager().beginTransaction().remove(getActivity().getFragmentManager().findFragmentById(R.id.map)).commit();
			googleMap = null;
		}
	}

}