package com.android.abooji.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class CardTypeModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@SerializedName("user_id")
	public String id = "";

	@SerializedName("card_type_title")
	public String card_type_title = "";

	@SerializedName("status")
	public String status = "";

	public CardTypeModel(String id, String card_type_title) {
		super();
		this.id = id;
		this.card_type_title = card_type_title;
	}

}
