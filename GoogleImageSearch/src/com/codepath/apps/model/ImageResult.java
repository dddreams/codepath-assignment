package com.codepath.apps.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageResult {
	
	private String fullUrl;
	private String thumbUrl;

	public ImageResult(JSONObject json){
		try {
			this.fullUrl = json.getString("url");
			this.thumbUrl = json.getString("tbUrl");
		} catch (JSONException e) {}
	}
	
	public String getFullUrl() {
		return fullUrl;
	}
	public void setFullUrl(String fullUrl) {
		this.fullUrl = fullUrl;
	}
	public String getThumbUrl() {
		return thumbUrl;
	}
	public void setThumbUrl(String thumbUrl) {
		this.thumbUrl = thumbUrl;
	}

	@Override
	public String toString() {
		return "ImageResult [thumbUrl=" + thumbUrl + "]";
	}

	public static Collection<? extends ImageResult> fromJSONArray(
			JSONArray array) {
		List<ImageResult> results = new ArrayList<ImageResult>();
		for(int i=0; i<array.length();i++){
			try{
				results.add(new ImageResult(array.getJSONObject(i)));
			}
			catch(JSONException e){
				e.printStackTrace();
			}
		}
		return results;
	}
}
