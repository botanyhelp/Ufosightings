/**Copyright (C) 2013 Thomas Maher
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.jimsuplee.ufosightings;

import android.app.ListActivity;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.view.Gravity;

public class Results extends ListActivity {
	String taxcd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String results = getIntent().getStringExtra("results");
		// taxcd = getIntent().getStringExtra("taxcd");
		// setContentView(R.layout.familylist);
		displayListView(results);
	}

	private void displayListView(String results) {
		List<String> resultsList = new ArrayList<String>(Arrays.asList(results
				.split("___")));
		// familyList.add("Zygophyllaceae");

		// ArrayAdaptar of String Array
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				R.layout.locationtextview, resultsList);
		setListAdapter(dataAdapter);
		ListView listView = getListView();
		// enables filtering for the contents of the given ListView
		listView.setTextFilterEnabled(true);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Show a toast with the TextView text
				// Toast toast = Toast.makeText(getApplicationContext(),
				// ((TextView) view).getText(), Toast.LENGTH_LONG);
				// toast.setGravity(Gravity.TOP, 25, 300);
				// toast.show();
				Intent i = new Intent("");
				String resultChoice = ((TextView) view).getText().toString();
				i.setData(Uri.parse(resultChoice));
				// i.putExtra("taxcd", taxcd);
				setResult(RESULT_OK, i);
				finish();
			}
		});
	}
}
