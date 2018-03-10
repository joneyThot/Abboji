package com.android.abooji.model;

public class ItemModel {

	public String name;
	public boolean selected = false;

	public ItemModel(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
