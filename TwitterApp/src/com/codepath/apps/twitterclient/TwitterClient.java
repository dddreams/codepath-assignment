package com.codepath.apps.twitterclient;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class TwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
    public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY = "n4NPBLYfEKyp3J9X76gEA";       // Change this
    public static final String REST_CONSUMER_SECRET = "I7Mn9v5SIctBXsEKMvJvNLb8MxNY6vyLTHYmM6oNxc"; // Change this
    public static final String REST_CALLBACK_URL = "oauth://mytwitterapp"; // Change this (here and in manifest)
    
    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    public void getHomeTimeline(long maxId, AsyncHttpResponseHandler handler){
    	String apiUrl = getApiUrl("statuses/home_timeline.json");
    	RequestParams params = new RequestParams();
        if( maxId != 0 ){
            params.put("max_id", String.valueOf(maxId));
        }
        params.put("count", "25");
    	client.get(apiUrl, params, handler);
    }
    
    public void getMentionsTimeline(long maxId, AsyncHttpResponseHandler handler){
    	String apiUrl = getApiUrl("statuses/mentions_timeline.json");
    	RequestParams params = new RequestParams();
        if( maxId != 0 ){
            params.put("max_id", String.valueOf(maxId));
        }
        params.put("count", "25");
    	client.get(apiUrl, params, handler);
    }
    
    public void getUserTimeline(String screenName, long maxId, AsyncHttpResponseHandler handler){
    	String apiUrl = getApiUrl("statuses/user_timeline.json");
    	RequestParams params = new RequestParams();
        if( maxId != 0 ){
            params.put("max_id", String.valueOf(maxId));
        }
        params.put("screen_name", screenName);
        params.put("count", "25");
    	client.get(apiUrl, params, handler);
    }
    
    public void updateStatus(String status, AsyncHttpResponseHandler handler){
    	String apiUrl = getApiUrl("statuses/update.json");
    	RequestParams params = new RequestParams();
    	params.put("status", status);
    	client.post(apiUrl, params, handler);
    }
    
    public void getAccounSettings(AsyncHttpResponseHandler handler){
    	String apiUrl = getApiUrl("account/settings.json");
    	client.get(apiUrl, handler);
    }
    
    public void getMyInfo(AsyncHttpResponseHandler handler){
    	String apiUrl = getApiUrl("account/verify_credentials.json");
    	client.get(apiUrl, null, handler);
    }
 
    public void getUserInfo(String screenName, AsyncHttpResponseHandler handler){
    	String apiUrl = getApiUrl("users/show.json");
    	RequestParams params = new RequestParams();
    	params.put("screen_name", screenName);
    	client.get(apiUrl, params, handler);
    }
}