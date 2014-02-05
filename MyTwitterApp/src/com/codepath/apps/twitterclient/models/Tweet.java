package com.codepath.apps.twitterclient.models;

import static com.codepath.apps.twitterclient.util.AppConstants.DATE_FORMAT;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Tweet extends BaseModel{
	private static final long serialVersionUID = 1L;
	private User user;

	public User getUser() {
		return user;
	}

	public String getBody(){
		return getString("text");
	}

	public long getId(){
		return getLong("id");
	}

	public boolean isFavorited(){
		return getBoolean("favorited");
	}

	public boolean isRetweeted(){
		return getBoolean("retweeted");
	}
	
	public Date getCreatedAt(){
		try{
            return DATE_FORMAT.parse(getString("created_at"));
        } catch (ParseException e) { 
        	e.printStackTrace();
        	return null;
        }
		 
	}

	public static Tweet fromJson(JSONObject jo){
		Tweet tweet = new Tweet();
		try{
			tweet.jsonObject = jo;
			tweet.user = User.fromJson(jo.getJSONObject("user"));
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return tweet;
	}

	public static ArrayList<Tweet> fromJson(JSONArray ja){
		ArrayList<Tweet> tweets = new ArrayList<Tweet>(ja.length());

		for(int i =0 ; i < ja.length(); i++){
			JSONObject jo = null;
			try{
				jo = ja.getJSONObject(i);
			}catch(JSONException e){
				e.printStackTrace();
				continue;
			}
			Tweet tweet = Tweet.fromJson(jo);
			if(tweet != null){
				tweets.add(tweet);
			}
		}
		return tweets;

	}
}
