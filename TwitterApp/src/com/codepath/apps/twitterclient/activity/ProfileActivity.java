package com.codepath.apps.twitterclient.activity;

import static com.codepath.apps.twitterclient.util.AppConstants.SCREEN_NAME_KEY;

import org.json.JSONObject;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.TwitterApp;
import com.codepath.apps.twitterclient.fragments.UserTimelineFragment;
import com.codepath.apps.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends FragmentActivity {

	TextView tvName;
	TextView tvTagline;
	TextView tvFollowers;
	TextView tvFollowing;
	ImageView ivProfileImage;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		ActionBar actionBar = getActionBar();
		String screenName = getIntent().getStringExtra(SCREEN_NAME_KEY);
		actionBar.setTitle("@" + screenName);
		
		setUpViews();
		loadProfileData(screenName);
		
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(screenName);
		ft.replace(R.id.frTimeline, userTimelineFragment);
		ft.commit();
	}
	
	private void setUpViews() {
		tvName = (TextView)findViewById(R.id.tvName);
		tvTagline = (TextView)findViewById(R.id.tvTagline);
		tvFollowers = (TextView)findViewById(R.id.tvFollowers);
		tvFollowing = (TextView)findViewById(R.id.tvFollowing);
		ivProfileImage = (ImageView)findViewById(R.id.ivProfileImage);
	}

	private void loadProfileData(String screenName){
		TwitterApp.getRestClient().getUserInfo(screenName,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject json) {
						User user = User.fromJson(json);
						tvName.setText(user.getName());
						tvTagline.setText(user.getTagline());
						tvFollowers.setText(user.getFollowersCount()+ " Followers");
						tvFollowing.setText(user.getFriendsCount() + " Following");
						ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), ivProfileImage);
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
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

}
