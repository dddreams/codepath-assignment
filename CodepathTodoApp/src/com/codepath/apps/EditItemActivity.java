package com.codepath.apps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);
		String text = getIntent().getStringExtra("text");
		EditText editField = (EditText)findViewById(R.id.etEditItem);
		editField.setText(text);
		editField.setSelection(text.length());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_item, menu);
		return true;
	}
	
	public void onSave(View v) {
		EditText editItem = (EditText) findViewById(R.id.etEditItem);
		Intent data = new Intent();
		data.putExtra("text", editItem.getText().toString());
		data.putExtra("index", getIntent().getIntExtra("index",0));
		setResult(RESULT_OK, data);
		finish();
	} 

}
