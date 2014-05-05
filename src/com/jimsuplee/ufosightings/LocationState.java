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
//import android.util.Log;

public class LocationState extends ListActivity {
	static final String TAG = "UFO";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Log.w(TAG,"In LocationState.onCreate(), checking Ufosightings.locationChoice");
		if (Ufosightings.locationChoice == "") {
			//Log.w(TAG,"In LocationState.onCreate(), Ufosightings.locationChoice==emptyString, calling displayListView()");
			displayListView();
		} else {
			//Log.w(TAG,"In LocationState.onCreate(), Ufosightings.locationChoice!=emptyString, setting locationChoice to Ufosightings.locationChoice and calling finish()");
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
		locationList.add("(Canada), AB");
		locationList.add("(Canada), BC");
		locationList.add("(Canada), MB");
		locationList.add("(Canada), NS");
		locationList.add("(Canada), ON");
		locationList.add("(Canada), QC");
		locationList.add("(Canada), SA");
		locationList.add("AK");
		locationList.add("AL");
		locationList.add("AR");
		locationList.add("AZ");
		locationList.add("AZ,");
		locationList.add("CA");
		locationList.add("CO");
		locationList.add("CT");
		locationList.add("DC");
		locationList.add("DE");
		locationList.add("FL");
		locationList.add("GA");
		locationList.add("IA");
		locationList.add("ID");
		locationList.add("IL");
		locationList.add("IN");
		locationList.add("KS");
		locationList.add("KY");
		locationList.add("LA");
		locationList.add("MA");
		locationList.add("MD");
		locationList.add("ME");
		locationList.add("MI");
		locationList.add("MN");
		locationList.add("MO");
		locationList.add("MS");
		locationList.add("MT");
		locationList.add("NC");
		locationList.add("ND");
		locationList.add("NE");
		locationList.add("NH");
		locationList.add("NJ");
		locationList.add("NM");
		locationList.add("NT");
		locationList.add("NV");
		locationList.add("NY");
		locationList.add("OH");
		locationList.add("OK");
		locationList.add("OR");
		locationList.add("PA");
		locationList.add("PE");
		locationList.add("PR");
		locationList.add("RI");
		locationList.add("SC");
		locationList.add("SD");
		locationList.add("TN");
		locationList.add("TX");
		locationList.add("UT");
		locationList.add("VA");
		locationList.add("VI");
		locationList.add("VT");
		locationList.add("WA");
		locationList.add("WI");
		locationList.add("WV");
		locationList.add("WY");

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				R.layout.locationtextview, locationList);
		setListAdapter(dataAdapter);
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent("");
				String locationChoice = ((TextView) view).getText().toString();
				Ufosightings.locationChoice = locationChoice;
				i.setData(Uri.parse(locationChoice));
				setResult(RESULT_OK, i);
				finish();
			}
		});
	}
}
