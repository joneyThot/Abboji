package com.android.abooji.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.android.abooji.R;

public class TextViewPlus extends TextView {

	public TextViewPlus(Context context) 
	{
		super(context);
	}
	public TextViewPlus(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		setCustomFont(context, attrs);
	}
	
	public TextViewPlus(Context context, AttributeSet attrs, int defStyle) 
	{
		super(context, attrs, defStyle);
		setCustomFont(context, attrs);
	}
	
	private void setCustomFont(Context ctx, AttributeSet attrs) {
		TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.TextViewPlus);
		String customFont = a.getString(R.styleable.TextViewPlus_customFont);
		setCustomFont(ctx, customFont);
		a.recycle();
	}
	public boolean setCustomFont(Context ctx, String asset)
	{
		Typeface tf = null;
		try
		{
			tf = Typeface.createFromAsset(ctx.getAssets(), asset);
		}
		catch(Exception e)
		{
			return false;
		}
		setTypeface(tf);
		return true;
	}

}