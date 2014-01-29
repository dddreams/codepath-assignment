package com.codepath.apps.imagesearch.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.codepath.apps.R;
import com.codepath.apps.model.ImageResult;
import com.loopj.android.image.SmartImageView;

public class ImageResultArrayAdapter extends ArrayAdapter<ImageResult> {

	public ImageResultArrayAdapter(Context context, List<ImageResult> images) {
		super(context, R.layout.item_image_result, images);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageResult imageInfo = this.getItem(position);
		SmartImageView smartImageView;
		if(convertView == null){
			LayoutInflater inflater = LayoutInflater.from(getContext());
			smartImageView = (SmartImageView)inflater.inflate(R.layout.item_image_result, parent, false);
		}
		else{
			smartImageView = (SmartImageView)convertView;
			smartImageView.setImageResource(android.R.color.transparent);
		}
		
		smartImageView.setImageUrl(imageInfo.getThumbUrl());
		return smartImageView;
	}
}
