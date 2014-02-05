package com.codepath.apps.twitterclient.activity;

import static com.codepath.apps.twitterclient.util.AppConstants.SCREEN_NAME_KEY;
import static com.codepath.apps.twitterclient.util.AppConstants.TWEET_KEY;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.TwitterApp;
import com.codepath.apps.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ComposeActivity extends Activity {
	
	private EditText etStatus;
	private TextView txScreenName;
	private ImageView ivUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);
		setUpViews();
	}

	private void setUpViews() {
		etStatus = (EditText)findViewById(R.id.etStatus);
		txScreenName = (TextView) findViewById(R.id.txScreenName);
		ivUser = (ImageView)findViewById(R.id.ivUser);
		String screenName = getIntent().getStringExtra(SCREEN_NAME_KEY);
		txScreenName.setText("@"+screenName);
		
		TwitterApp.getRestClient().getUserInfo(screenName, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int arg0, JSONObject jsonObject) {
				User user = User.fromJson(jsonObject);
				ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), ivUser);
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose, menu);
		return true;
	}
	
	public void onCancel(View v){
		setResult(RESULT_CANCELED);
		finish();
	}
	
	public void onTweet(View v){
		String status = etStatus.getText().toString();
		TwitterApp.getRestClient().updateStatus(status, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int arg0, JSONObject jsonObject) {
				Toast.makeText(getApplicationContext(), "Tweet Posted", Toast.LENGTH_SHORT).show();
				Intent data = new Intent();
				data.putExtra(TWEET_KEY, jsonObject.toString());
				setResult(RESULT_OK, data);
				finish();
			}
		});
	}

}
