package com.codepath.apps.twitterclient.activity;

import static com.codepath.apps.twitterclient.util.AppConstants.SCREEN_NAME_KEY;
import static com.codepath.apps.twitterclient.util.AppConstants.TWEET_KEY;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.codepath.apps.twitterclient.EndlessScrollListener;
import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.TweetsAdapter;
import com.codepath.apps.twitterclient.TwitterApp;
import com.codepath.apps.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends Activity {

	private String screenName;
	private ListView lvTweets;
	private TweetsAdapter adapter;
	private List<Tweet> tweets = new ArrayList<Tweet>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		lvTweets = (ListView) findViewById(R.id.lvTweets);
		adapter = new TweetsAdapter(getBaseContext(), tweets);
		lvTweets.setAdapter(adapter);

		setUpActionBar();
		setUpTimelineView();
		setUpScrolling();
	}

	private void setUpTimelineView() {
		loadTweets(0);
	}

	private void setUpScrolling() {
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				loadTweets(tweets.get(tweets.size() - 1).getId() - 1);
			}
		});
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setUpActionBar() {
		TwitterApp.getRestClient().getAccounSettings(
				new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(JSONObject jsonObj) {
						try {
							screenName = (String) jsonObj.get("screen_name");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						ActionBar actionBar = getActionBar();
						actionBar.setTitle("@" + screenName);
					}
				});
	}

	private void loadTweets(long maxId) {
		TwitterApp.getRestClient().getHomeTimeline(maxId,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONArray jsonTweets) {
						// Log.d("DEBUG", jsonTweets.toString());
						tweets.addAll(Tweet.fromJson(jsonTweets));
						adapter.notifyDataSetChanged();
					}

					@Override
					public void onFailure(Throwable t) {
						t.printStackTrace();
						Log.d("DEBUG", "ERROR-" + t.getMessage());
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}

	public void composeTweet(MenuItem mi) {
		Intent i = new Intent(this, ComposeActivity.class);
		i.putExtra(SCREEN_NAME_KEY, screenName);
		startActivityForResult(i, 200);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == 200) {
			String tweetStr = data.getStringExtra(TWEET_KEY);
			try {
				Tweet tweet = Tweet.fromJson(new JSONObject(tweetStr));
				adapter.insert(tweet, 0);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

}
