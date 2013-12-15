package com.codepath.apps;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class TodoActivity extends Activity {

	private static final int REQUEST_CODE = 10;

	private ArrayList<String> items;
	private ArrayAdapter<String> itemsAdapter;
	private ListView lvItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo);
		lvItems = (ListView) findViewById(R.id.lvItems);
		readItems();
		itemsAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, items);
		lvItems.setAdapter(itemsAdapter);
		setUpListViewListner();
		setUpEditItemsListner();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.todo, menu);
		return true;
	}

	public void addTodoItem(View v) {
		EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
		itemsAdapter.add(etNewItem.getText().toString());
		etNewItem.setText("");
	}

	// Delete listner
	private void setUpListViewListner() {
		lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> aView, View item,
					int pos, long id) {
				
				Toast.makeText(TodoActivity.this,
						"Removed Item " + items.get(pos),
						Toast.LENGTH_SHORT).show();
				
				items.remove(pos);
				itemsAdapter.notifyDataSetInvalidated();
				saveItems();
				return true;
			}
		});
	}

	// Edit listner
	private void setUpEditItemsListner() {
		lvItems.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> aView, View item, int index,
					long id) {
				Intent i = new Intent(TodoActivity.this, EditItemActivity.class);

				i.putExtra("text", items.get(index));
				i.putExtra("index", index);
				startActivityForResult(i, REQUEST_CODE);
			}
		});
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			String itemText = data.getExtras().getString("text");
			int index = data.getIntExtra("index", 0);
			items.remove(index);
			items.add(index, itemText);
			itemsAdapter.notifyDataSetInvalidated();
			saveItems();
		}
	}

	private void readItems() {
		File filesDir = getFilesDir();
		File todoFile = new File(filesDir, "mytodo.txt");
		try {
			items = new ArrayList<String>(FileUtils.readLines(todoFile));
		} catch (IOException e) {
			items = new ArrayList<String>();
			e.printStackTrace();
		}
	}

	private void saveItems() {
		File filesDir = getFilesDir();
		File todoFile = new File(filesDir, "mytodo.txt");
		try {
			FileUtils.writeLines(todoFile, items);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}