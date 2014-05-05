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
//import android.app.Activity;
import android.content.Intent;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
//import android.widget.ArrayAdapter;
import android.net.Uri;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;

public class LocationWorld extends ListActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Ufosightings.locationChoice == "") {
			displayListView();
		} else {
			Intent i = new Intent("");
			String locationChoice = Ufosightings.locationChoice;
			i.setData(Uri.parse(locationChoice));
			setResult(RESULT_OK, i);
			finish();
		}
	}

	private void displayListView() {
		// This strange World list, comes like this:
		// select DISTINCT location_state from ufosightings ORDER BY
		// location_state;
		// cat ufos4_distinctlocationstate082813.txt|sed 's/.*,
		// //'|sort|uniq|grep "),$"|sed 's/.*(//'|sort|uniq|sed
		// 's/^/locationList.add("/'|sed 's/),/");/'
		List<String> locationList = new ArrayList<String>();
		locationList.add("Argentina");
		locationList.add("Australia");
		locationList.add("Australia)");
		locationList.add("Bahamas");
		locationList.add("Belgium");
		locationList.add("Black Pearl");
		locationList.add("board Sea Princess");
		locationList.add("Brazil");
		locationList.add("British Virgin Islands");
		locationList.add("Canary Islands");
		locationList.add("China");
		locationList.add("Croatia");
		locationList.add("Cyprus");
		locationList.add("east coast");
		locationList.add("East Coast");
		locationList.add("Ecuador");
		locationList.add("Egypt");
		locationList.add("England");
		locationList.add("France");
		locationList.add("French Polynesia");
		locationList.add("Germany");
		locationList.add("Greece");
		locationList.add("Guatamala");
		locationList.add("Guernsey");
		locationList.add("Hellenic Republic");
		locationList.add("India");
		locationList.add("inflight");
		locationList.add("Italy");
		locationList.add("Jamaica");
		locationList.add("Japan");
		locationList.add("Japan-Los Angeles");
		locationList.add("Korea");
		locationList.add("Kosovo");
		locationList.add("Lebanon");
		locationList.add("Lithuania");
		locationList.add("Malaysia");
		locationList.add("Mexico");
		locationList.add("midway between");
		locationList.add("Near");
		locationList.add("NEAR");
		locationList.add("Netherlands");
		locationList.add("New Zealand");
		locationList.add("north country");
		locationList.add("Norway");
		locationList.add("on large highway");
		locationList.add("out of Japan");
		locationList.add("Pakistan");
		locationList.add("Peru");
		locationList.add("Philippines");
		locationList.add("Poland");
		locationList.add("Portugal");
		locationList.add("PRC");
		locationList.add("Puerto Rico");
		locationList.add("Republic of Ireland");
		locationList.add("Seychelles");
		locationList.add("somewhere");
		locationList.add("South Africa");
		locationList.add("South Australia ");
		locationList.add("Spain");
		locationList.add("Sweden");
		locationList.add("Sweden/Denmark");
		locationList.add("Thailand");
		locationList.add("UK");
		locationList.add("UK/England");
		locationList.add("UK/Scotland");
		locationList.add("UK/Wales??");
		locationList.add("UK/Wales");
		locationList.add("USN P-3");
		locationList.add("US Virgin Islands");
		locationList.add("Venezuela");
		locationList.add("VIC");
		locationList.add("West");
		locationList.add("Yugoslavia");

		// create an ArrayAdaptar from the String Array
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				R.layout.locationtextview, locationList);
		setListAdapter(dataAdapter);
		ListView listView = getListView();
		// enables filtering for the contents of the given ListView
		listView.setTextFilterEnabled(true);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, show a toast with the TextView text
				// Toast toast = Toast.makeText(getApplicationContext(),
				// ((TextView) view).getText(), Toast.LENGTH_LONG);
				// toast.setGravity(Gravity.TOP, 25, 300);
				// toast.show();
				Intent i = new Intent("");
				// The extra ")," appended here is an artifact of the data
				// storage
				// We'd rather not have the ")," in the list of choices
				String locationChoice = ((TextView) view).getText().toString()
						+ "),";
				Ufosightings.locationChoice = locationChoice;
				i.setData(Uri.parse(locationChoice));
				setResult(RESULT_OK, i);
				finish();
			}
		});
	}
}
