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

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Button;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.widget.Toast;
import android.content.Intent;
import android.database.Cursor;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.DatePicker;
import android.widget.Toast;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Ufosightings extends Activity {
	static final String TAG = "UFO";
	static int pagerCounter = 0;
	static int pagerCounterTotal = 0;
	static String locationChoice = "";
	static String descriptionChoice = "";
	// Another global static (sorry!) which should hold locationState
	// locationWorld Description
	static String locationType = "";
	DBAdapter db;
	DatePicker datePicker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ufosightings);
		try {
			String destPath = "/data/data/" + getPackageName() + "/databases";
			File f = new File(destPath);
			if (!f.exists()) {
				f.mkdirs();
				f.createNewFile();
				Toast.makeText(this, "Please Wait...Database Being Created",
						Toast.LENGTH_LONG).show();
				CopyDB(getBaseContext().getAssets().open("ufos"),
						new FileOutputStream(destPath + "/ufos"));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Now that we (hopefully) have an SQLiteDatabase available, lets make
		// an adapter and put it
		// into our global variable.
		db = new DBAdapter(this);
	}

	public void CopyDB(InputStream inputStream, OutputStream outputStream)
			throws IOException {
		// copy 1K bytes of file at a time
		byte[] buffer = new byte[1024];
		int length;
		while ((length = inputStream.read(buffer)) > 0) {
			outputStream.write(buffer, 0, length);
		}
		inputStream.close();
		outputStream.close();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// ---check if the request code is 0 1 2 3 4 5---
		Log.w(TAG, "In Ufosightings.onActivityResult(), checking reqCod");
		// onClickLocation startActivityForResult section
		if (requestCode == 0) {
			Log.w(TAG,
					"In Ufosightings.onActivityResult(int-reqCode=0,int-resCode,Intent-data)");
			if (resultCode == RESULT_OK) {
				Log.w(TAG,
						"In Ufosightings.onActivityResult(), reqCode=0, resultCode=RESULT_OK");
				Log.w(TAG,
						"In Ufosightings.onActivityResult(), about to db.open()");
				db.open();
				Log.w(TAG,
						"In Ufosightings.onActivityResult(), about to get location from Intent");
				String location = data.getData().toString();
				Log.w(TAG, "In Ufosightings.onActivityResult(), got location: "
						+ location);
				Log.w(TAG,
						"In Ufosightings.onActivityResult(), about to db.getByLocation(location)");
				Cursor c = db.getByLocation(location);
				String results = "";
				if (c != null) {
					Log.w(TAG,
							"In Ufosightings.onActivityResult(), cursor c is not null.");
				}
				Log.w(TAG,
						"In Ufosightings.onActivityResult(), about to c.moveToFirst()");
				if (c.moveToFirst()) {
					Log.w(TAG,
							"In Ufosightings.onActivityResult(), c.moveToFirst() returned true");

					do {
						pagerCounter++;
						results += "Occurred: " + c.getString(0) + "-"
								+ c.getString(1) + "-" + c.getString(2)
								+ "\nCity/State: " + c.getString(4) + "/"
								+ c.getString(5) + "\nUfo Shape: "
								+ c.getString(6) + "\nEvent Duration: "
								+ c.getString(7) + "\nDescription: "
								+ c.getString(8) + "___";
						// results += "Occurred: " + c.getString(0) +
						// "\nReported: " + c.getString(1) + "\nCity/State: " +
						// c.getString(2) + "\nUfo Shape: " + c.getString(3) +
						// "\nEvent Duration: " + c.getString(4) +
						// "\nDescription: " + c.getString(5) + "___";
					} while (c.moveToNext());
				} else {
					Log.w(TAG,
							"In Ufosightings.onActivityResult(), c.moveToFirst() returned false");
				}

				if (pagerCounter == 10) {
					pagerCounterTotal += 10;
					pagerCounter = 0;
					locationType = "locationCity";
					Log.w(TAG,
							"In Ufosightings.onActivityResult(), pagerCounter is ten: "
									+ Integer.toString(pagerCounter));
					Log.w(TAG,
							"In Ufosightings.onActivityResult(), pagerCounterTotal is: "
									+ Integer.toString(pagerCounterTotal));
					Log.w(TAG,
							"In Ufosightings.onActivityResult(), locationChoice is: "
									+ locationChoice);
					Log.w(TAG,
							"In Ufosightings.onActivityResult(), locationType is: "
									+ locationType);
					// results+=Integer.toString(pagerCounterTotal);
				} else {
					// results+=Integer.toString(pagerCounter);
					pagerCounterTotal = 0;
					pagerCounter = 0;
					locationChoice = "";
					locationType = "";
					Log.w(TAG,
							"In Ufosightings.onActivityResult(), pagerCounter is NOT ten: "
									+ Integer.toString(pagerCounter));
					Log.w(TAG,
							"In Ufosightings.onActivityResult(), pagerCounterTotal is: "
									+ Integer.toString(pagerCounterTotal));
					Log.w(TAG,
							"In Ufosightings.onActivityResult(), locationChoice is: "
									+ locationChoice);
					Log.w(TAG,
							"In Ufosightings.onActivityResult(), locationType is: "
									+ locationType);

				}

				Log.w(TAG,
						"In Ufosightings.onActivityResult(), about to db.close()");
				Intent iResults = new Intent(
						"com.jimsuplee.ufosightings.Results");
				iResults.putExtra("results", results);
				startActivityForResult(iResults, 3);
				db.close();
			}
		}
		// onClickShape startActivityForResult section
		if (requestCode == 1) {
			Log.w(TAG,
					"In Ufosightings.onActivityResult(int-reqCode=1,int-resCode,Intent-data)");
			if (resultCode == RESULT_OK) {
				Log.w(TAG,
						"In Ufosightings.onActivityResult(), reqCode=1, resultCode=RESULT_OK");
				Log.w(TAG,
						"In Ufosightings.onActivityResult(), about to db.open()");
				db.open();
				Log.w(TAG,
						"In Ufosightings.onActivityResult(), about to get shape from Intent");
				String shape = data.getData().toString();
				Log.w(TAG, "In Ufosightings.onActivityResult(), got shape: "
						+ shape);
				Log.w(TAG,
						"In Ufosightings.onActivityResult(), about to db.getByLocation(location)");
				Cursor c = db.getByShape(shape);
				String results = "";
				if (c != null) {
					Log.w(TAG,
							"In Ufosightings.onActivityResult(), cursor c is not null.");
				}
				Log.w(TAG,
						"In Ufosightings.onActivityResult(), about to c.moveToFirst()");
				if (c.moveToFirst()) {
					Log.w(TAG,
							"In Ufosightings.onActivityResult(), c.moveToFirst() returned true");
					do {
						pagerCounter++;
						results += "Occurred: " + c.getString(0) + "-"
								+ c.getString(1) + "-" + c.getString(2)
								+ "\nCity/State: " + c.getString(4) + "/"
								+ c.getString(5) + "\nUfo Shape: "
								+ c.getString(6) + "\nEvent Duration: "
								+ c.getString(7) + "\nDescription: "
								+ c.getString(8) + "___";
						// results += "Occurred: " + c.getString(0) +
						// "\nReported: " + c.getString(1) + "\nCity/State: " +
						// c.getString(2) + "\nUfo Shape: " + c.getString(3) +
						// "\nEvent Duration: " + c.getString(4) +
						// "\nDescription: " + c.getString(5) + "___";
					} while (c.moveToNext());
				}
				Log.w(TAG,
						"In Ufosightings.onActivityResult(), about to db.close()");
				Intent iResults = new Intent(
						"com.jimsuplee.ufosightings.Results");
				iResults.putExtra("results", results);
				startActivityForResult(iResults, 3);
				db.close();
			}
		}
		// UNUSED code 2:
		if (requestCode == 2) {
			Log.w(TAG,
					"In Ufosightings.onActivityResult(int-reqCode=2,int-resCode,Intent-data)");
			if (resultCode == RESULT_OK) {
				Log.w(TAG,
						"In Ufosightings.onActivityResult(), reqCode=2, resultCode=RESULT_OK");
				Log.w(TAG,
						"In Ufosightings.onActivityResult(), about to db.open()");
				db.open();
				Log.w(TAG,
						"In Ufosightings.onActivityResult(), about to get description from Intent");
				String descriptionParam = data.getData().toString();
				Log.w(TAG,
						"In Ufosightings.onActivityResult(), got description: "
								+ descriptionParam);
				Log.w(TAG,
						"In Ufosightings.onActivityResult(), about to db.getByDescription(description)");
				Cursor c = db.getByDescription(descriptionParam);
				String results = "";
				if (c != null) {
					Log.w(TAG,
							"In Ufosightings.onActivityResult(), cursor c is not null.");
				}
				Log.w(TAG,
						"In Ufosightings.onActivityResult(), about to c.moveToFirst()");
				if (c.moveToFirst()) {
					Log.w(TAG,
							"In Ufosightings.onActivityResult(), c.moveToFirst() returned true");
					do {
						results += "Occurred: " + c.getString(0) + "-"
								+ c.getString(1) + "-" + c.getString(2)
								+ "\nCity/State: " + c.getString(4) + "/"
								+ c.getString(5) + "\nUfo Shape: "
								+ c.getString(6) + "\nEvent Duration: "
								+ c.getString(7) + "\nDescription: "
								+ c.getString(8) + "___";
						// results += "Occurred: " + c.getString(0) +
						// "\nReported: " + c.getString(1) + "\nCity/State: " +
						// c.getString(2) + "\nUfo Shape: " + c.getString(3) +
						// "\nEvent Duration: " + c.getString(4) +
						// "\nDescription: " + c.getString(5) + "___";
					} while (c.moveToNext());
				}
				Log.w(TAG,
						"In Ufosightings.onActivityResult(), about to db.close()");
				Intent iResults = new Intent(
						"com.jimsuplee.ufosightings.Results");
				iResults.putExtra("results", results);
				startActivityForResult(iResults, 3);
				db.close();
			}
		}
		// Results startActivityForResult section
		Log.w(TAG, "In Ufosightings.onActivityResult(), checking reqCod");
		if (requestCode == 3) {
			Log.w(TAG,
					"In Ufosightings.onActivityResult(int-reqCode=3,int-resCode,Intent-data)");
			if (resultCode == RESULT_OK) {
				// OK, but still has html entities, like &amp; and &apos;
				// String sighting = data.getData().toString();
				// still has html entities, like &amp; and &apos;
				// String sighting =
				// Html.fromHtml(data.getData().toString()).toString();
				String sighting = Html.fromHtml(data.getData().toString())
						.toString();
				TextView tv = (TextView) findViewById(R.id.txt_Ufo);

				// The next if/else lines decide whether there are more (than
				// the last 10) records
				// If so, then make-visible the proper 10-more-records button in
				// activity_ufosightings.xml
				// Decide and set visibility of the LocationPager
				Button pagerButton = (Button) findViewById(R.id.btn_LocationPager);
				if (pagerCounterTotal > 0 && locationType == "locationCity") {
					pagerButton.setVisibility(View.VISIBLE);
				} else {
					pagerButton.setVisibility(View.INVISIBLE);
				}
				pagerButton = (Button) findViewById(R.id.btn_LocationStatePager);
				if (pagerCounterTotal > 0 && locationType == "locationState") {
					pagerButton.setVisibility(View.VISIBLE);
				} else {
					pagerButton.setVisibility(View.INVISIBLE);
				}
				// Decide and set visibility of the LocationWorldPager
				pagerButton = (Button) findViewById(R.id.btn_LocationWorldPager);
				if (pagerCounterTotal > 0 && locationType == "locationWorld") {
					pagerButton.setVisibility(View.VISIBLE);
				} else {
					pagerButton.setVisibility(View.INVISIBLE);
				}
				// We also need to decide and set visibility of the
				// DescriptionPagerButton
				pagerButton = (Button) findViewById(R.id.btn_DescriptionPager);
				if (pagerCounterTotal > 0 && descriptionChoice != "") {
					pagerButton.setVisibility(View.VISIBLE);
				} else {
					pagerButton.setVisibility(View.INVISIBLE);
				}
				tv.setVisibility(View.VISIBLE);
				tv.setText(sighting);
			}
		}
		// onClickLocationState startActivityForResult section
		if (requestCode == 4) {
			Log.w(TAG,
					"In Ufosightings.onActivityResult(int-reqCode=0,int-resCode,Intent-data)");
			if (resultCode == RESULT_OK) {
				Log.w(TAG,
						"In Ufosightings.onActivityResult(), reqCode=0, resultCode=RESULT_OK");
				Log.w(TAG,
						"In Ufosightings.onActivityResult(), about to db.open()");
				db.open();
				Log.w(TAG,
						"In Ufosightings.onActivityResult(), about to get location from Intent");
				String location = data.getData().toString();
				Log.w(TAG, "In Ufosightings.onActivityResult(), got location: "
						+ location);
				Log.w(TAG,
						"In Ufosightings.onActivityResult(), about to db.getByLocationState(location)");

				// This cursor will take forever with a state like Arizona, AZ,
				// with 2000+ results.
				// Therefore, we want instead to give the user another choice of
				// location_city in AZ.
				// One way is to get DISTINCT location_city where
				// location_state=~AZ and make a list for
				// them to chose from, and feed that choice to getByLocation
				Cursor c = db.getByLocationState(location);
				String results = "";
				if (c != null) {
					Log.w(TAG,
							"In Ufosightings.onActivityResult(), cursor c is not null.");
				}
				Log.w(TAG,
						"In Ufosightings.onActivityResult(), about to c.moveToFirst()");
				if (c.moveToFirst()) {

					Log.w(TAG,
							"In Ufosightings.onActivityResult(), c.moveToFirst() returned true");

					do {
						pagerCounter++;
						// results += "Occurred: " + c.getString(0) +
						// "\nReported: " + c.getString(1) + "\nCity/State: " +
						// c.getString(2) + "\nUfo Shape: " + c.getString(3) +
						// "\nEvent Duration: " + c.getString(4) +
						// "\nDescription: " + c.getString(5) + "___";
						results += "Occurred: " + c.getString(0) + "-"
								+ c.getString(1) + "-" + c.getString(2)
								+ "\nCity/State: " + c.getString(4) + "/"
								+ c.getString(5) + "\nUfo Shape: "
								+ c.getString(6) + "\nEvent Duration: "
								+ c.getString(7) + "\nDescription: "
								+ c.getString(8) + "___";

					} while (c.moveToNext());
				}
				// if(pagerCounter == 10) {
				// if(pagerCounter == 9) {
				if (pagerCounter > 8) {
					pagerCounterTotal += 10;
					pagerCounter = 0;
					locationType = "locationState";
					Log.w(TAG,
							"In Ufosightings.onActivityResult(), pagerCounter is ten: "
									+ Integer.toString(pagerCounter));
					Log.w(TAG,
							"In Ufosightings.onActivityResult(), pagerCounterTotal is: "
									+ Integer.toString(pagerCounterTotal));
					Log.w(TAG,
							"In Ufosightings.onActivityResult(), locationChoice is: "
									+ locationChoice);
					Log.w(TAG,
							"In Ufosightings.onActivityResult(), locationType is: "
									+ locationType);
					// results+=Integer.toString(pagerCounterTotal);
				} else {
					// results+=Integer.toString(pagerCounter);
					pagerCounterTotal = 0;
					pagerCounter = 0;
					locationChoice = "";
					locationType = "";
					Log.w(TAG,
							"In Ufosightings.onActivityResult(), pagerCounter is NOT ten: "
									+ Integer.toString(pagerCounter));
					Log.w(TAG,
							"In Ufosightings.onActivityResult(), pagerCounterTotal is: "
									+ Integer.toString(pagerCounterTotal));
					Log.w(TAG,
							"In Ufosightings.onActivityResult(), locationChoice is: "
									+ locationChoice);
					Log.w(TAG,
							"In Ufosightings.onActivityResult(), locationType is: "
									+ locationType);

				}

				Log.w(TAG,
						"In Ufosightings.onActivityResult(), about to db.close()");
				Intent iResults = new Intent(
						"com.jimsuplee.ufosightings.Results");
				iResults.putExtra("results", results);
				startActivityForResult(iResults, 3);
			}
		}
		// onClickWorld startActivityForResult section
		if (requestCode == 5) {
			Log.w(TAG,
					"In Ufosightings.onActivityResult(int-reqCode=0,int-resCode,Intent-data)");
			if (resultCode == RESULT_OK) {
				Log.w(TAG,
						"In Ufosightings.onActivityResult(), reqCode=0, resultCode=RESULT_OK");
				Log.w(TAG,
						"In Ufosightings.onActivityResult(), about to db.open()");
				db.open();
				Log.w(TAG,
						"In Ufosightings.onActivityResult(), about to get location from Intent");
				String location = data.getData().toString();
				Log.w(TAG, "In Ufosightings.onActivityResult(), got location: "
						+ location);
				Log.w(TAG,
						"In Ufosightings.onActivityResult(), about to db.getByLocationWorld(location)");
				Cursor c = db.getByLocationWorld(location);
				String results = "";
				if (c != null) {
					Log.w(TAG,
							"In Ufosightings.onActivityResult(), cursor c is not null.");
				}
				Log.w(TAG,
						"In Ufosightings.onActivityResult(), about to c.moveToFirst()");
				if (c.moveToFirst()) {
					Log.w(TAG,
							"In Ufosightings.onActivityResult(), c.moveToFirst() returned true");
					do {
						pagerCounter++;
						results += "Occurred: " + c.getString(0) + "-"
								+ c.getString(1) + "-" + c.getString(2)
								+ "\nCity/State: " + c.getString(4) + "/"
								+ c.getString(5) + "\nUfo Shape: "
								+ c.getString(6) + "\nEvent Duration: "
								+ c.getString(7) + "\nDescription: "
								+ c.getString(8) + "___";
						// results += "Occurred: " + c.getString(0) +
						// "\nReported: " + c.getString(1) + "\nCity/State: " +
						// c.getString(2) + "\nUfo Shape: " + c.getString(3) +
						// "\nEvent Duration: " + c.getString(4) +
						// "\nDescription: " + c.getString(5) + "___";
					} while (c.moveToNext());
				}
				if (pagerCounter == 10) {
					pagerCounterTotal += 10;
					pagerCounter = 0;
					locationType = "locationWorld";
					Log.w(TAG,
							"In Ufosightings.onActivityResult(), pagerCounter was ten, now: "
									+ Integer.toString(pagerCounter));
					Log.w(TAG,
							"In Ufosightings.onActivityResult(), pagerCounterTotal is: "
									+ Integer.toString(pagerCounterTotal));
					Log.w(TAG,
							"In Ufosightings.onActivityResult(), locationChoice is: "
									+ locationChoice);
					Log.w(TAG,
							"In Ufosightings.onActivityResult(), locationType is: "
									+ locationType);
					// results+=Integer.toString(pagerCounterTotal);
				} else {
					// results+=Integer.toString(pagerCounter);
					pagerCounterTotal = 0;
					pagerCounter = 0;
					locationChoice = "";
					locationType = "";
					Log.w(TAG,
							"In Ufosightings.onActivityResult(), pagerCounter is NOT ten: "
									+ Integer.toString(pagerCounter));
					Log.w(TAG,
							"In Ufosightings.onActivityResult(), pagerCounterTotal is: "
									+ Integer.toString(pagerCounterTotal));
					Log.w(TAG,
							"In Ufosightings.onActivityResult(), locationChoice is: "
									+ locationChoice);
					Log.w(TAG,
							"In Ufosightings.onActivityResult(), locationType is: "
									+ locationType);

				}

				Log.w(TAG,
						"In Ufosightings.onActivityResult(), about to db.close()");
				Intent iResults = new Intent(
						"com.jimsuplee.ufosightings.Results");
				iResults.putExtra("results", results);
				startActivityForResult(iResults, 3);
				db.close();
			}
		}
	}

	public void onClickLocation(View view) {
		Log.w(TAG, "In Ufosightings.onClickLocation()");
		locationChoice = "";
		locationType = "";
		descriptionChoice = "";
		pagerCounter = 0;
		pagerCounterTotal = 0;
		Intent iLocation = new Intent("com.jimsuplee.ufosightings.Location");
		startActivityForResult(iLocation, 0);
	}

	public void onClickLocationMore(View view) {
		Log.w(TAG, "In Ufosightings.onClickLocationMore()");
		Intent iLocation = new Intent("com.jimsuplee.ufosightings.Location");
		startActivityForResult(iLocation, 0);
	}

	public void onClickLocationState(View view) {
		Log.w(TAG, "In Ufosightings.onClickLocationState()");
		locationChoice = "";
		locationType = "";
		descriptionChoice = "";
		pagerCounter = 0;
		pagerCounterTotal = 0;
		Intent iLocationState = new Intent(
				"com.jimsuplee.ufosightings.LocationState");
		startActivityForResult(iLocationState, 4);
	}

	public void onClickLocationStateMore(View view) {
		Log.w(TAG, "In Ufosightings.onClickLocationStateMore()");
		Intent iLocationState = new Intent(
				"com.jimsuplee.ufosightings.LocationState");
		startActivityForResult(iLocationState, 4);
	}

	public void onClickLocationWorld(View view) {
		Log.w(TAG, "In Ufosightings.onClickLocationWorld()");
		locationChoice = "";
		locationType = "";
		descriptionChoice = "";
		pagerCounter = 0;
		pagerCounterTotal = 0;
		Intent iLocationWorld = new Intent(
				"com.jimsuplee.ufosightings.LocationWorld");
		startActivityForResult(iLocationWorld, 5);
	}

	public void onClickLocationWorldMore(View view) {
		Log.w(TAG, "In Ufosightings.onClickLocationWorldMore()");
		Intent iLocationWorld = new Intent(
				"com.jimsuplee.ufosightings.LocationWorld");
		startActivityForResult(iLocationWorld, 5);
	}

/*	public void onClickShape(View view) {
		Log.w(TAG, "In Ufosightings.onClickShape()");
		Intent iShape = new Intent("com.jimsuplee.ufosightings.Shape");
		startActivityForResult(iShape, 1);
	}*/

	public void onClickDescription(View view) {
		Log.w(TAG, "In Ufosightings.onClickDescription()");
		locationChoice = "";
		locationType = "";
		// descriptionChoice = "";
		pagerCounter = 0;
		// pagerCounterTotal = 0;

		EditText txt_description = (EditText) findViewById(R.id.txt_description);
		// Intent iDescription = new
		// Intent("com.jimsuplee.ufosightings.Description");
		// iDescription.setData(Uri.parse(txt_description.getText().toString()));
		// Bundle extras = new Bundle();
		// extras.putString("description",
		// txt_description.getText().toString());
		// iDescription.putExtras(extras);
		// startActivityForResult(iDescription, 2);
		db.open();
		// String descriptionParam = txt_description.getData().toString();
		// if we are NOT in a paging-results situation, the capture the typed-in
		// description
		// otherwise, we will be using the Ufosightings.descriptionChoice
		// remembered string choice
		String descriptionParam;
		// if(descriptionChoice == "" && pagerCounterTotal == 0) {
		if (descriptionChoice == "") {
			// txt_description was obtained a few lines above
			descriptionParam = txt_description.getText().toString();
			descriptionChoice = descriptionParam;
			Log.w(TAG,
					"In Ufosightings.onClickDescription(), descriptionChoice WAS empty.");
			Log.w(TAG,
					"In Ufosightings.onClickDescription(), descriptionChoice NOW: "
							+ descriptionChoice);
			pagerCounterTotal = 0;
		} else {
			Log.w(TAG,
					"In Ufosightings.onClickDescription(), descriptionChoice IS NOT empty.");
			Log.w(TAG,
					"In Ufosightings.onClickDescription(), reuse descriptionChoice: "
							+ descriptionChoice);
			String currentDescriptionParam = txt_description.getText()
					.toString();
			// If the user hasn't typed in a different search item:
			if (descriptionChoice.equals(currentDescriptionParam)) {
				Log.w(TAG,
						"In Ufosightings.onClickDescription(), same search choice: "
								+ descriptionChoice + currentDescriptionParam);
				descriptionParam = descriptionChoice;
			} else {
				// In this case, Ufosightings.descriptionChoice is not empty.
				// However, we cannot reuse Ufosightings.descriptionChoice if
				// the user has since typed in a different value to search by.
				// Remember that onClickDescription() is called for new
				// searches and for "next 10" pager requests. Therefore, this
				// logic is required. Further, if this is a new search, then
				// we also want to reset pagerCountTotal to 0.
				Log.w(TAG,
						"In Ufosightings.onClickDescription(), NOT same search choice: "
								+ descriptionChoice + currentDescriptionParam);

				descriptionChoice = currentDescriptionParam;
				descriptionParam = currentDescriptionParam;
				pagerCounterTotal = 0;
			}
			// pagerCounterTotal = 0;
			// pagerCounter = 0;
		}
		Cursor c = db.getByDescription(descriptionParam);
		String results = "";
		if (c != null) {
			Log.w(TAG,
					"In Ufosightings.onClickDescription(), cursor c is not null.");
		}
		Log.w(TAG,
				"In Ufosightings.onClickDescription(), about to c.moveToFirst()");
		if (c.moveToFirst()) {
			Log.w(TAG,
					"In Ufosightings.onClickDescription(), c.moveToFirst() returned true");
			do {
				pagerCounter++;
				// results += "Occurred: " + c.getString(0) + "\nReported: " +
				// c.getString(1) + "\nCity/State: " + c.getString(2) +
				// "\nUfo Shape: " + c.getString(3) + "\nEvent Duration: " +
				// c.getString(4) + "\nDescription: " + c.getString(5) + "___";
				results += "Occurred: " + c.getString(0) + "-" + c.getString(1)
						+ "-" + c.getString(2) + "\nCity/State: "
						+ c.getString(4) + "/" + c.getString(5)
						+ "\nUfo Shape: " + c.getString(6)
						+ "\nEvent Duration: " + c.getString(7)
						+ "\nDescription: " + c.getString(8) + "___";

			} while (c.moveToNext());
		}
		// if(pagerCounter == 10) {
		// if(pagerCounter > 8) {
		// for search term 'urgent' 8 results were returned??? actual DB has 27
		// hmmm??
		// if(pagerCounter > 7) {
		if (pagerCounter == 10) {
			pagerCounterTotal += 10;
			pagerCounter = 0;
			Log.w(TAG,
					"In Ufosightings.onClickDescription(), pagerCounter was ten, now: "
							+ Integer.toString(pagerCounter));
			Log.w(TAG,
					"In Ufosightings.onClickDescription(), pagerCounterTotal is: "
							+ Integer.toString(pagerCounterTotal));
			Log.w(TAG,
					"In Ufosightings.onClickDescription(), descriptionChoice is: "
							+ descriptionChoice);
		} else {
			// results+=Integer.toString(pagerCounter);
			Log.w(TAG,
					"In Ufosightings.onClickDescription(), pagerCounter is NOT ten, it is: "
							+ Integer.toString(pagerCounter));
			pagerCounterTotal = 0;
			pagerCounter = 0;
			locationChoice = "";
			locationType = "";
			Log.w(TAG,
					"In Ufosightings.onClickDescription(), pagerCounter is NOT ten: "
							+ Integer.toString(pagerCounter));
			Log.w(TAG,
					"In Ufosightings.onClickDescription(), pagerCounterTotal is: "
							+ Integer.toString(pagerCounterTotal));
			Log.w(TAG,
					"In Ufosightings.onClickDescription(), descriptionChoice is: "
							+ descriptionChoice);
		}
		Log.w(TAG, "In Ufosightings.onClickDescription(), about to db.close()");
		Intent iResults = new Intent("com.jimsuplee.ufosightings.Results");
		iResults.putExtra("results", results);
		startActivityForResult(iResults, 3);
		db.close();
	}

	public void onClickDate(View view) {
		Log.w(TAG, "In Ufosightings.onClickDate()");
		// This XML, in uifile.xml, will place a DatePicker in UI:
		// <DatePicker android:id="@+id/datePicker"
		// android:layout_width="wrap_content"
		// android:layout_height="wrap_content"
		// android:startYear="1950"
		// android:endYear="2010"/>
		// This java will grab the value:
		datePicker = (DatePicker) findViewById(R.id.datePicker);
		// String dateParam =
		// Integer.toString(datePicker.getYear())+Integer.toString(datePicker.getMonth())+Integer.toString(datePicker.getDayOfMonth());
		String yearParam = Integer.toString(datePicker.getYear());
		String monthParam = Integer.toString(datePicker.getMonth() + 1);
		String dayParam = Integer.toString(datePicker.getDayOfMonth());

		// EditText txt_date = (EditText) findViewById(R.id.txt_date);
		// String dateParam = txt_date.getText().toString();
		// try {
		// Date d = new SimpleDateFormat("yyyyMMdd").parse(dateParam);
		// }catch(Exception e) {
		// Toast.makeText(this,"Bad date: "+dateParam,
		// Toast.LENGTH_LONG).show();
		// Toast.makeText(this,"YYYYMMDD format required",
		// Toast.LENGTH_LONG).show();
		// }

		db.open();

		Cursor c = db.getByDate(yearParam, monthParam, dayParam);
		String results = "";
		if (c != null) {
			Log.w(TAG, "In Ufosightings.Ufosightings(), cursor c is not null.");
		}
		Log.w(TAG, "In Ufosightings.Ufosightings(), about to c.moveToFirst()");
		if (c.moveToFirst()) {
			Log.w(TAG,
					"In Ufosightings.Ufosightings(), c.moveToFirst() returned true");
			do {
				// results += "Occurred: " + c.getString(0) + "\nReported: " +
				// c.getString(1) + "\nCity/State: " + c.getString(2) +
				// "\nUfo Shape: " + c.getString(3) + "\nEvent Duration: " +
				// c.getString(4) + "\nDescription: " + c.getString(5) + "___";
				results += "Occurred: " + c.getString(0) + "-" + c.getString(1)
						+ "-" + c.getString(2) + "\nCity/State: "
						+ c.getString(4) + "/" + c.getString(5)
						+ "\nUfo Shape: " + c.getString(6)
						+ "\nEvent Duration: " + c.getString(7)
						+ "\nDescription: " + c.getString(8) + "___";
			} while (c.moveToNext());
		} else {
			Toast.makeText(
					this,
					"No records for " + yearParam + "-" + monthParam + "-"
							+ dayParam, Toast.LENGTH_LONG).show();
		}
		Log.w(TAG, "In Ufosightings.Ufosightings(), about to db.close()");
		Intent iResults = new Intent("com.jimsuplee.ufosightings.Results");
		iResults.putExtra("results", results);
		startActivityForResult(iResults, 3);
		db.close();
	}

	public void onClickExactDate(View view) {
		Log.w(TAG, "In Ufosightings.onClickDate()");
		// This XML, in uifile.xml, will place a DatePicker in UI:
		// <DatePicker android:id="@+id/datePicker"
		// android:layout_width="wrap_content"
		// android:layout_height="wrap_content"
		// android:startYear="1950"
		// android:endYear="2010"/>
		// This java will grab the value:
		datePicker = (DatePicker) findViewById(R.id.datePicker);
		// String dateParam =
		// Integer.toString(datePicker.getYear())+Integer.toString(datePicker.getMonth())+Integer.toString(datePicker.getDayOfMonth());
		String yearParam = Integer.toString(datePicker.getYear());
		String monthParam = Integer.toString(datePicker.getMonth() + 1);
		String dayParam = Integer.toString(datePicker.getDayOfMonth());

		// EditText txt_date = (EditText) findViewById(R.id.txt_date);
		// String dateParam = txt_date.getText().toString();
		// try {
		// Date d = new SimpleDateFormat("yyyyMMdd").parse(dateParam);
		// }catch(Exception e) {
		// Toast.makeText(this,"Bad date: "+dateParam,
		// Toast.LENGTH_LONG).show();
		// Toast.makeText(this,"YYYYMMDD format required",
		// Toast.LENGTH_LONG).show();
		// }

		db.open();

		Cursor c = db.getByExactDate(yearParam, monthParam, dayParam);
		String results = "";
		if (c != null) {
			Log.w(TAG, "In Ufosightings.Ufosightings(), cursor c is not null.");
		}
		Log.w(TAG, "In Ufosightings.Ufosightings(), about to c.moveToFirst()");
		if (c.moveToFirst()) {
			Log.w(TAG,
					"In Ufosightings.Ufosightings(), c.moveToFirst() returned true");
			do {
				// results += "Occurred: " + c.getString(0) + "\nReported: " +
				// c.getString(1) + "\nCity/State: " + c.getString(2) +
				// "\nUfo Shape: " + c.getString(3) + "\nEvent Duration: " +
				// c.getString(4) + "\nDescription: " + c.getString(5) + "___";
				results += "Occurred: " + c.getString(0) + "-" + c.getString(1)
						+ "-" + c.getString(2) + "\nCity/State: "
						+ c.getString(4) + "/" + c.getString(5)
						+ "\nUfo Shape: " + c.getString(6)
						+ "\nEvent Duration: " + c.getString(7)
						+ "\nDescription: " + c.getString(8) + "___";
			} while (c.moveToNext());
		} else {
			Toast.makeText(
					this,
					"No records for " + yearParam + "-" + monthParam + "-"
							+ dayParam, Toast.LENGTH_LONG).show();
		}
		Log.w(TAG, "In Ufosightings.Ufosightings(), about to db.close()");
		Intent iResults = new Intent("com.jimsuplee.ufosightings.Results");
		iResults.putExtra("results", results);
		startActivityForResult(iResults, 3);
		db.close();
	}

	public void onClickHelp(View view) {
		Log.w(TAG, "In Ufosightings.onClickHelp()");
		TextView tv = (TextView) findViewById(R.id.txt_Ufo);
		tv.setVisibility(View.VISIBLE);
		// String help =
		// "Searching by Description works by matching exact strings.  For example, searching for \"not drunk\" would return all reports that have the exact string.";
		String help = "Thanks to UFO reporters everywhere, the National UFO Reporting Center, Ganglion and visiting aliens.  Searching by Description returns UFO reports that exactly match your search phrase.  No quotes.  The search is NOT case-sensitive...uppercase and lowercase do NOT matter.  Searching by Exact_Date allows you to specify the exact date.  Searching by Date_Any_Year permits any year, and allows you to find sightings on a certain date, like Halloween, for any year.  Searching by Locations or Description only returns 10 records at a time.  Clicking on the 10_more_records button shows the next 10.  The 10 at a time limit is a problem for some searches, like California (CA), which has 7,557 records.";
		tv.setText(help);
	}
	/*
	 * Basic operation goes like this: 1. Ufosightings.java starts and sets UI
	 * via ufosightings.xml 2. User clicks on a button, possibly after choosing
	 * a data or typing a description to search for. 3. When user clicks button,
	 * a specific Activity is started with startActivityForResult() 3a.
	 * Ufosightings sets locationType and locationChoice to empty string, to
	 * start anew. 3aa. Ufosightings sets pagerCount and pagerCountTotal to 0,
	 * to start anew. 3aaa. If searching by State, then
	 * startActivityForResults() starts LocationState() with reqCode 4 3b.
	 * LocationState.java checks that locationChoice=="" and so runs
	 * displayListView() 3bb. Otherwise, if locationChoice!="", then it would
	 * reuse the choice/makeIntent/setData/finish 3bbb. LocationState.java runs
	 * for the first time, which displays a list of states to choose from.
	 * 
	 * 3c. When user clicks a state, it sets static var
	 * Ufosightings.locationChoice to remember the choice. 3d. LocationState
	 * then creates an Intent, sets the choice data in the Intent and calls
	 * finish() 4. Ufosightings.java sees the returning Intent and runs
	 * onActivityForResult() for LocationState 5.
	 * Ufosightings.onActivityResult() runs the block of code where reqCode==4
	 * and resultCode==OK 6. Ufosightings calls
	 * DBAdapter.getByLocationState(location) to query the database. 7.
	 * DBAdapter.getByLocationState() gets the location parameter for the query
	 * as an argument. 7a. Ufosightings.pagerCounterTotal == 0, and so the sql
	 * selection = "location_state LIKE ? LIMIT 10" 7aa. In DBAdapter, the
	 * parameter used to compare to db.location_state is %locationChoice 7aaa.
	 * DBAdapter checks if cursor (where location_state="%locationChoice") is
	 * null and returns it. 8. Ufosightings.onActivityResult(reqCode=4) gets the
	 * cursor and loops through the results 8a. cursor.moveToFirst and
	 * c.moveToNext, it builds a long String called results, delimited by ___
	 * 8b. For each record in the cursor, it increments by 1 pagerCounter 8c.
	 * When finished, it checks if pagerCounter==10, which would indicate there
	 * are more results 8d. If pagerCounter==10, it sets
	 * pagerCounterTotal+=10;pagerCounter=0;locationType="locationState"; 8e. If
	 * pagerCounter
	 * !=10,pagerCounterTotal=0;pagerCounter=0;locationChoice="";locationType
	 * =""; 9. That pagerCounter/locationChoice/locationType business allows for
	 * paging through more results. 9a. That
	 * pagerCounter/locationChoice/locationType are static variables so
	 * DBAdapter can check. 10. Ufosightings.onActivityResult() creates an
	 * Intent and passes it the long results string 10a.
	 * Ufosightings.onActivityResult() then calls
	 * startActivityForResult(Results/3) 11. Results.java splits the passed-in
	 * results data string by its delimiter: ___ 11a. Results.java makes a
	 * clickable ListView and listens for which choice is clicked on 11b. When
	 * user clicks on a Results.java item, it creates an intent, passes data,
	 * calls finish() 12. Ufosightings.onActivityResult() checks for reqCode==3
	 * (meaning Results.java) 12a. Ufosightings.onActivityResult(3) finds the
	 * TextView and the PagerButton 12b. Ufosightings.onActivityResult(3) sets
	 * the Button to VISIBLE if more results are available 12bb. if
	 * (pagerCounterTotal > 0 && locationType=="locationState") then there are
	 * more results 12bbb. If there are more results, the button should become
	 * visible, onClickLocationStateMore 12c. Ufosightings.onActivityResult(3)
	 * sets the TextView with the data that was chosen in Results 13. At this
	 * point, a record is showing, all buttons are visible, including any pager
	 * buttons. 14. If more results are available for the State, the Next 10
	 * Records button displays 14a. Clicking on next 10 records button, calls
	 * onClickLocationStateMore(), starts LocationState 15. LocationState
	 * notices nonempty Ufosightings.locationChoice (NOT == "") and starts
	 * Intent now 16. Ufosightings.onActivityResult(reqCode=4) runs again, but
	 * with pagerCountTotal !=0 17. Ufosightings.onActivityResult(reqCode=4)
	 * gets Cursor c = db.getByLocationState(location); 18. This time, in
	 * DBAdapter.getByLocationState(), pagerCountTotal is NOT==0 18a.
	 * DBAdapter.getByLocationState() makes a paging query, to fetch next ten
	 * records like this: 18b. selection = "location_state LIKE ? LIMIT " +
	 * Integer.toString(Ufosightings.pagerCounterTotal) + ",10"; 18c. Which
	 * queries the database like this fetching records 11-20 for state YZ: 18d.
	 * SELECT * FROM ufosightings WHERE location_state LIKE "%YZ" LIMIT 10,10;
	 * 19. Ufosightings.onActivityResult() gets cursor, loops through it,
	 * creates ___-delim results String 19a. Ufosightings.onActivityResult()
	 * increments-by-10 pagerCounterTotal if more results exist 20.
	 * Ufosightings.onActivityResult() calls startActivityForResult(Results/3)
	 * passing in long string 21. Results.java parses string again, creates
	 * clickable ListView, waits for click. 22.
	 * Ufosightings.onActivityResult(Results/3) sets up the main page again with
	 * buttons/etc.
	 * 
	 * 
	 * NOTES: 1. States like AZ have thousands of records (2093) and all have
	 * hundreds--10 at a time is slow. 1a. Better would be clicking on AZ shows
	 * a choice of cities in AZ. 1b. The Search By City is good and shows
	 * "Bouse, AZ", a city in AZ with 17 records. 1c. The Search By City is
	 * tedious because it has 1701 cities to scroll through and choose from 1d.
	 * The Search By City is fun because every city with a report is in there.
	 * 1e. The Search By City is not fun if you're in Arizona because Bouse is
	 * hundreds below Phoenix 1f. The Search By City is not fun because some
	 * cities have lots Phoenix 417, LA has 333. 1g. Breaking states into cities
	 * will be nice after choosing a state is better than paging 2093 1h. Power
	 * searching with two parameters, by state and description, for example. 1i.
	 * Reporting the number of results will be better, perhaps with a Toast that
	 * says something like 1j.
	 * """Your query returns 12,000 records, try screening better than that"""
	 * 1k.
	 * 
	 * 2. Searching by date is a pain to move more than a few years, by
	 * scrolling the month column. 2a. Searching "any year" is confusing. 2b.
	 * Searching "any year" on July 4th returns too many results.
	 */

}
