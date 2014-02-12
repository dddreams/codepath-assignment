package com.codepath.apps.twitterclient.fragments;

import static com.codepath.apps.twitterclient.util.AppConstants.SCREEN_NAME_KEY;

import org.json.JSONArray;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.twitterclient.TwitterApp;
import com.codepath.apps.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class UserTimelineFragment extends TweetsFragment {

	public static UserTimelineFragment newInstance(String screenName){
		UserTimelineFragment fragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString(SCREEN_NAME_KEY, screenName);
        fragment.setArguments(args);
        return fragment;
    }
	
	@Override
	public void loadTweets(long maxId) {
		TwitterApp.getRestClient().getUserTimeline(getArguments().getString(SCREEN_NAME_KEY), maxId,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONArray jsonTweets) {
						// Log.d("DEBUG", jsonTweets.toString());
						adapter.addAll(Tweet.fromJson(jsonTweets));
					}

					@Override
					public void onFailure(Throwable t) {
						t.printStackTrace();
						Log.d("DEBUG", "ERROR-" + t.getMessage());
					}
				});
	}
	
}
