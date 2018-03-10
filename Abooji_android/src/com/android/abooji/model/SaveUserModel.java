package com.android.abooji.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class SaveUserModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@SerializedName("user_id")
	public String user_id;
	
	@SerializedName("email")
	public String email;
	
	@SerializedName("gender")
	public String gender;
	
	@SerializedName("ageGroup")
	public String ageGroup;
	
	@SerializedName("age_range")
	public String age_range;	
	
	@SerializedName("maritalStatus")
	public String maritalStatus;
	
	@SerializedName("marrital_status")
	public String marrital_status;
	
	@SerializedName("children")
	public String children;
	
	@SerializedName("childAgeGroup")
	public String childAgeGroup;
	
	@SerializedName("card_cat")
	public String card_cat;
	
	@SerializedName("card_type")
	public String card_type;
	
	@SerializedName("friendsEmail")
	public String friendsEmail;
	
	@SerializedName("freinds_email")
	public String freinds_email;
	
	@SerializedName("cards")
	public String cards;
	
	@SerializedName("categoryPreference")
	public String categoryPreference;
	
	@SerializedName("category_preference")
	public String category_preference;
	
	@SerializedName("alert")
	public String alert;
	
	@SerializedName("notification_status")
	public String notification_status;
	
	@SerializedName("notification_alerts")
	public String notification_alerts;
	
	@SerializedName("alertTime")
	public String alertTime;
	
	@SerializedName("status")
	public String status;
}
