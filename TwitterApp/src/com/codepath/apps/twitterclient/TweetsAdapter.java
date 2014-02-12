package com.codepath.apps.twitterclient;

import static com.codepath.apps.twitterclient.util.AppConstants.SCREEN_NAME_KEY;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.activity.ProfileActivity;
import com.codepath.apps.twitterclient.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetsAdapter extends ArrayAdapter<Tweet> {

	public TweetsAdapter(Context context, List<Tweet> tweets) {
		super(context, 0, tweets);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view==null){
			LayoutInflater inflater = LayoutInflater.from(getContext());
			view = inflater.inflate(R.layout.tweet_item, null);
		}
		
		final Tweet tweet = getItem(position);
		
		ImageView ivProfile = (ImageView)view.findViewById(R.id.ivProfile);
		ImageLoader.getInstance().displayImage(tweet.getUser().getProfileImageUrl(), ivProfile);
		
		TextView tvName = (TextView)view.findViewById(R.id.tvName);
		String formattedName = "<b>" + tweet.getUser().getName() + "</b>"
				+ "<small> <font color = '#777777'>@"
				+ tweet.getUser().getScreenName() + "</font></small>";
		tvName.setText(Html.fromHtml(formattedName));

		TextView tvBody = (TextView) view.findViewById(R.id.tvBody);
		tvBody.setText(Html.fromHtml(tweet.getBody()));
		
		TextView tvCreatedAt = (TextView) view.findViewById(R.id.tvCreatedAt);
		tvCreatedAt.setText(getDuration(tweet.getCreatedAt()));
		
		
		ivProfile.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getContext(), ProfileActivity.class);
				i.putExtra(SCREEN_NAME_KEY, tweet.getUser().getScreenName());
				getContext().startActivity(i);
			}
		});
		return view;
	}
	
	private String getDuration(Date createdDate){
		if(createdDate ==null){
			return "";
		}
		String date = (String) DateUtils.getRelativeDateTimeString(
                getContext(), createdDate.getTime(), DateUtils.MINUTE_IN_MILLIS,
                DateUtils.WEEK_IN_MILLIS, 0);
		String time = date.split(",")[0];
		time = time.replace(" ago", "")
				.replace(" minutes", "m")
				.replace(" minute", "m")
				.replace(" hours", "h")
				.replace(" hour", "h")
				.replace("yesterday", "1d")
				.replace(" days", "d");
		
		return time;
	}
	
	

}
