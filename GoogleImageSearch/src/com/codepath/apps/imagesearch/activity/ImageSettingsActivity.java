package com.codepath.apps.imagesearch.activity;

import static com.codepath.apps.util.AppConstants.KEY_SEARCH_CRITERIA;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.codepath.apps.R;
import com.codepath.apps.model.ImageSearchCriteria;

public class ImageSettingsActivity extends Activity {

	private Spinner spSize;
	private Spinner spColor;
	private Spinner spType;
	private EditText etSite;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_settings);
		setUpViews();
		ImageSearchCriteria imageSearchCriteria = (ImageSearchCriteria)getIntent().getSerializableExtra(KEY_SEARCH_CRITERIA);
		setSpinnerToValue(spSize, imageSearchCriteria.getSize());
		setSpinnerToValue(spColor, imageSearchCriteria.getColor());
		setSpinnerToValue(spType, imageSearchCriteria.getType());
		etSite.setText(imageSearchCriteria.getSite());
	}
	
	private void setUpViews() {
		spSize = (Spinner) findViewById(R.id.spSize);
		spColor = (Spinner) findViewById(R.id.spColor);
		spType = (Spinner) findViewById(R.id.spType);
		etSite = (EditText) findViewById(R.id.etSite);
	}

	public void setSpinnerToValue(Spinner spinner, String value) {
	    int index = 0;
	    SpinnerAdapter adapter = spinner.getAdapter();
	    for (int i = 0; i < adapter.getCount(); i++) {
	        if (adapter.getItem(i).equals(value)) {
	            index = i;
	        }
	    }
	    spinner.setSelection(index);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_settings, menu);
		return true;
	}
	
	public void saveSettings(View v){
		Toast.makeText(this, "Settings Saved ", Toast.LENGTH_SHORT).show();
		ImageSearchCriteria imageSearchCriteria = new ImageSearchCriteria(
				spSize.getSelectedItem().toString(), 
				spColor.getSelectedItem().toString(), 
				spType.getSelectedItem().toString(),
				etSite.getText().toString());
		
		Intent data= new Intent();
		data.putExtra(KEY_SEARCH_CRITERIA, imageSearchCriteria);
		setResult(RESULT_OK, data);
		finish();
	}

}
