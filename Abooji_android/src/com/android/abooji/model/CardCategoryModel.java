package com.android.abooji.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class CardCategoryModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@SerializedName("id")
	public String id = "";

	@SerializedName("card_category_title")
	public String card_category_title = "";

	public CardCategoryModel(String id, String card_category_title) {
		super();
		this.id = id;
		this.card_category_title = card_category_title;
		
	}

	@SerializedName("status")
	public String status = "";

}
