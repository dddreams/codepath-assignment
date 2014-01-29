package com.codepath.apps.imagesearch.activity;

import static com.codepath.apps.util.AppConstants.API_PARAM_COLOR;
import static com.codepath.apps.util.AppConstants.API_PARAM_QUERY;
import static com.codepath.apps.util.AppConstants.API_PARAM_SITE;
import static com.codepath.apps.util.AppConstants.API_PARAM_SIZE;
import static com.codepath.apps.util.AppConstants.API_PARAM_START;
import static com.codepath.apps.util.AppConstants.API_PARAM_TYPE;
import static com.codepath.apps.util.AppConstants.BASE_URL;
import static com.codepath.apps.util.AppConstants.IMAGES_PER_PAGE;
import static com.codepath.apps.util.AppConstants.KEY_FULL_URL;
import static com.codepath.apps.util.AppConstants.KEY_SEARCH_CRITERIA;
import static com.codepath.apps.util.AppConstants.REQUEST_CODE_SETTINGS;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml.Encoding;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.codepath.apps.R;
import com.codepath.apps.imagesearch.adapters.ImageResultArrayAdapter;
import com.codepath.apps.model.ImageResult;
import com.codepath.apps.model.ImageSearchCriteria;
import com.codepath.apps.util.EndlessScrollListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity {
	
	private EditText etQuery;
	private GridView gvResults;
	private List<ImageResult> imageResults = new ArrayList<ImageResult>(); 
	private ImageResultArrayAdapter imageAdapter;
	private ImageSearchCriteria imageSearchCriteria;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		setUpViews();
		imageAdapter = new ImageResultArrayAdapter(this, imageResults);
		gvResults.setAdapter(imageAdapter);
		gvResults.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adpater, View view, int pos,
					long arg3) {
				Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
				i.putExtra(KEY_FULL_URL, imageResults.get(pos).getFullUrl());
				startActivity(i);
			}
		});
		
		setUpScrolling();
	}

	private void setUpScrolling() {
		gvResults.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				searchImages(page*IMAGES_PER_PAGE, etQuery.getText().toString());
			}
		});
	}

	private void setUpViews() {
		etQuery = (EditText)findViewById(R.id.etQuery);
		gvResults = (GridView) findViewById(R.id.gvResults);
	}
	
	public void onImageSearch(View v){
		String query = etQuery.getText().toString();
		if(query!=null && query.length()>0){
			Toast.makeText(this, "Searching for - "+query, Toast.LENGTH_SHORT).show();
			imageResults.clear();
			searchImages(0, query);	
		}
		else{
			Toast.makeText(this, "Please Enter Search Text", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void searchImages(int start, String query){
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(buildAPIUrl(start, query),
			new JsonHttpResponseHandler(){
				@Override
				public void onSuccess(JSONObject response) {
					JSONArray imageJsonResults = null;
					try{
						if (JSONObject.NULL!=response.get("responseData")) {
							imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
							imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));	
						}
						else{
							Log.d("DEBUG", "Data is null");
						}
					}
					catch(JSONException e){
						Log.d("DEBUG", e.getMessage());
						e.printStackTrace();
					}
				}
			});
	}

	private String buildAPIUrl(int start, String query){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(API_PARAM_START, String.valueOf(start)));
		params.add(new BasicNameValuePair(API_PARAM_QUERY, query));
		if(imageSearchCriteria!=null){
			if(!"all".equalsIgnoreCase(imageSearchCriteria.getSize())){
				params.add(new BasicNameValuePair(API_PARAM_SIZE, imageSearchCriteria.getSize()));	
			}
			if(!"all".equalsIgnoreCase(imageSearchCriteria.getColor())){
				params.add(new BasicNameValuePair(API_PARAM_COLOR, imageSearchCriteria.getColor()));
			}
			if(!"all".equalsIgnoreCase(imageSearchCriteria.getType())){
				params.add(new BasicNameValuePair(API_PARAM_TYPE, imageSearchCriteria.getType()));
			}
			if(imageSearchCriteria.getSite() !=null && imageSearchCriteria.getSite().length()>0){
				params.add(new BasicNameValuePair(API_PARAM_SITE, imageSearchCriteria.getSite()));	
			}
		}
		return BASE_URL+URLEncodedUtils.format(params, Encoding.UTF_8.name());
	}
	
	public void onSettingsAction(MenuItem mi){
		if(imageSearchCriteria==null){
			imageSearchCriteria = new ImageSearchCriteria("all", "all", "all", "");
		}
		Intent i = new Intent(getApplicationContext(), ImageSettingsActivity.class);
		i.putExtra(KEY_SEARCH_CRITERIA, imageSearchCriteria);
		startActivityForResult(i, REQUEST_CODE_SETTINGS);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_SETTINGS) {
			imageSearchCriteria = (ImageSearchCriteria)data.getSerializableExtra(KEY_SEARCH_CRITERIA);
		}
	} 
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

}
