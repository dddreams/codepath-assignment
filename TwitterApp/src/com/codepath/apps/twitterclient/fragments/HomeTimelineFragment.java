package com.codepath.apps.twitterclient.fragments;

import org.json.JSONArray;

import android.util.Log;

import com.codepath.apps.twitterclient.TwitterApp;
import com.codepath.apps.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;


public class HomeTimelineFragment extends TweetsFragment {

	@Override
	public void loadTweets(long maxId) {
		TwitterApp.getRestClient().getHomeTimeline(maxId,
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
