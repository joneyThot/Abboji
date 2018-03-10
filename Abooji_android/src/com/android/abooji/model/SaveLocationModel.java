package com.android.abooji.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class SaveLocationModel implements Serializable {
	public SaveLocationModel(String id, String user_id, String location_name,
			String latitude, String longitude, String date_created) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.location_name = location_name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.date_created = date_created;
	}
	

	public SaveLocationModel() {
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 1L;

	@SerializedName("id")
	public String id = "";

	@SerializedName("user_id")
	public String user_id = "";

	@SerializedName("location_name")
	public String location_name = "";

	@SerializedName("latitude")
	public String latitude = "";

	@SerializedName("longitude")
	public String longitude = "";

	@SerializedName("date_created")
	public String date_created = "";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getLocation_name() {
		return location_name;
	}

	public void setLocation_name(String location_name) {
		this.location_name = location_name;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getDate_created() {
		return date_created;
	}

	public void setDate_created(String date_created) {
		this.date_created = date_created;
	}

	@SerializedName("status")
	public String status;

}
