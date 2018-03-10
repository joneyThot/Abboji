package com.android.abooji.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class BarcodeModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@SerializedName("account_id")
	public String account_id;
	
	@SerializedName("account_type")
	public String account_type;
	
	@SerializedName("status")
	public String status;
}
