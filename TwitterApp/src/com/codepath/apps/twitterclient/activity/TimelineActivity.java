package com.codepath.apps.twitterclient.activity;

import static com.codepath.apps.twitterclient.util.AppConstants.SCREEN_NAME_KEY;
import static com.codepath.apps.twitterclient.util.AppConstants.TWEET_KEY;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.twitterclient.FragmentTabListener;
import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.TwitterApp;
import com.codepath.apps.twitterclient.fragments.HomeTimelineFragment;
import com.codepath.apps.twitterclient.fragments.MentionsTimelineFragment;
import com.codepath.apps.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends FragmentActivity {

	private String screenName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);

		setUpActionBar();
		setupTabs();
	}

    public void showProgressBar() {
        setProgressBarIndeterminateVisibility(true); 
    }

    public void hideProgressBar() {
        setProgressBarIndeterminateVisibility(false); 
    }
    
	private void setupTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		Tab home = actionBar.newTab()
				.setText("Home")
				// .setIcon(R.drawable.ic_home)
				.setTag("HomeTimelineFragment")
				.setTabListener(
						new FragmentTabListener<HomeTimelineFragment>(
								R.id.flContainer, this, "Home",
								HomeTimelineFragment.class));

		Tab mention = actionBar.newTab()
				.setText("Mentions")
				// .setIcon(R.drawable.ic_mentions)
				.setTag("MentionsTimelineFragment")
				.setTabListener(
						new FragmentTabListener<MentionsTimelineFragment>(
								R.id.flContainer, this, "Mentions",
								MentionsTimelineFragment.class));

		actionBar.addTab(home);
		actionBar.addTab(mention);
		actionBar.selectTab(home);
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
	
	public void showProfile(MenuItem mi) {
		Intent i = new Intent(this, ProfileActivity.class);
		i.putExtra(SCREEN_NAME_KEY, screenName);
		startActivity(i);
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == 200) {
			String tweetStr = data.getStringExtra(TWEET_KEY);
			try {
				Tweet tweet = Tweet.fromJson(new JSONObject(tweetStr));
				HomeTimelineFragment homeTimelineFragment = (HomeTimelineFragment)getSupportFragmentManager().findFragmentByTag("Home");
				homeTimelineFragment.getAdapter().insert(tweet, 0);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

}
