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

//import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//import android.util.Log;
//import java.util.ArrayList;

public class DBAdapter {

	static final String TAG = "DBAdapter";

	static final String DATABASE_NAME = "ufos";
	static final String DATABASE_TABLE = "ufosightings";
	static final int DATABASE_VERSION = 2;

	static final String sighted_at_year = "sighted_at_year";
	static final String sighted_at_month = "sighted_at_month";
	static final String sighted_at_day = "sighted_at_day";
	static final String reported_at = "reported_at";
	static final String location_city = "location_city";
	static final String location_state = "location_state";
	static final String shape = "shape";
	static final String duration = "duration";
	static final String description = "description";

	// static final String DATABASE_CREATE =
	// "CREATE TABLE ufosighting (sighted_at integer default null,  reported_at integer default null,  location_city text default null,  location_state text default null, shape text default null,  duration text default null,  description text default null);";
	static final String DATABASE_CREATE = "CREATE TABLE ufosightings (sighted_at_year integer default null, sighted_at_month integer default null, sighted_at_day integer default null,  reported_at integer default null,  location text default null,  shape text default null,  duration text default null,  description text default null);";

	final Context context;

	DatabaseHelper DBHelper;
	SQLiteDatabase db;

	public DBAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				db.execSQL(DATABASE_CREATE);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			//Log.w(TAG, "Upgrading database from version " + oldVersion + " to "+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS contacts");
			onCreate(db);
		}
	}

	public DBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		DBHelper.close();
	}

	public Cursor getByLocation(String locationParam) throws SQLException {
		String[] columns = new String[] { sighted_at_year, sighted_at_month,
				sighted_at_day, reported_at, location_city, location_state,
				shape, duration, description };
		// String selection = "location_city LIKE ?";
		String selection;
		if (Ufosightings.pagerCounterTotal == 0) {
			//Log.w(TAG,"In DBAdapter.getByLocation(String location), pagerCounterTotal ==0");
			selection = "location_city LIKE ? LIMIT 10";
			//Log.w(TAG,"In DBAdapter.getByLocation(String location), set selection to:"+ selection);
		} else {
			//Log.w(TAG,		"In DBAdapter.getByLocation(String location), pagerCounterTotal NOT 0");
			selection = "location_city LIKE ? LIMIT "
					+ Integer.toString(Ufosightings.pagerCounterTotal) + ",10";
			//Log.w(TAG,					"In DBAdapter.getByLocation(String location), set selection to:"							+ selection);
		}
		// locationParam = " " + locationParam;
		// We only want a portion of the query parameter because the
		// location_city is only a prefix
		// subset of the value chosen from the list in the app.
		int indexToTrim = locationParam.indexOf(",");
		locationParam = locationParam.substring(0, indexToTrim);
		locationParam = " " + locationParam + "%";
		//Log.w(TAG,				"In DBAdapter.getByLocation(), searching location_city for: "						+ locationParam);
		String[] selectionArgs = new String[] { locationParam };
		//Log.w(TAG,				"In DBAdapter.getByLocation(), About to make Cursor, c, with db.query()");
		Cursor mCursor = db.query(DATABASE_TABLE, columns, selection,
				selectionArgs, null, null, null);
		//Log.w(TAG,				"In DBAdapter.getByLocation(String location), About to check if Cursor c is null");
		if (mCursor != null) {
			//Log.w(TAG,					"In DBAdapter.getByLocation(String location), c is NOT null, about to c.moveToFirst()");
			mCursor.moveToFirst();
		}
		//Log.w(TAG,				"In DBAdapter.getByLocation(String location), about to return cursor, c");
		return mCursor;
	}

	public Cursor getByLocationState(String locationParam) throws SQLException {
		String[] columns = new String[] { sighted_at_year, sighted_at_month,
				sighted_at_day, reported_at, location_city, location_state,
				shape, duration, description };
		// String selection = "location_state LIKE ?";
		String selection;
		// SELECT * FROM table LIMIT <offset>,<number-to-return>
		//Log.w(TAG,				"In DBAdapter.getByLocationState(String location), checking pagerCounterTotal");
		if (Ufosightings.pagerCounterTotal == 0) {
			//Log.w(TAG,					"In DBAdapter.getByLocationState(String location), pagerCounterTotal ==0");
			selection = "location_state LIKE ? LIMIT 10";
			//Log.w(TAG,					"In DBAdapter.getByLocationState(String location), set selection to:"							+ selection);
		} else {
			//Log.w(TAG,					"In DBAdapter.getByLocationState(String location), pagerCounterTotal NOT 0");
			selection = "location_state LIKE ? LIMIT "
					+ Integer.toString(Ufosightings.pagerCounterTotal) + ",10";
			//Log.w(TAG,					"In DBAdapter.getByLocationState(String location), set selection to:"							+ selection);
		}
		// locationParam = "%" + locationParam;
		locationParam = "%" + locationParam;
		String[] selectionArgs = new String[] { locationParam };
		//Log.w(TAG,				"In DBAdapter.getByLocationState(), About to make Cursor, c, with db.query()");
		Cursor mCursor = db.query(DATABASE_TABLE, columns, selection,
				selectionArgs, null, null, null);
		//Log.w(TAG,				"In DBAdapter.getByLocationState(String location), About to check if Cursor c is null");
		if (mCursor != null) {
			//Log.w(TAG,					"In DBAdapter.getByLocationState(String location), c is NOT null, about to c.moveToFirst()");
			mCursor.moveToFirst();
		}
		//Log.w(TAG,				"In DBAdapter.getByLocationState(String location), about to return cursor, c");
		return mCursor;
	}

	public Cursor getByLocationWorld(String locationParam) throws SQLException {
		String[] columns = new String[] { sighted_at_year, sighted_at_month,
				sighted_at_day, reported_at, location_city, location_state,
				shape, duration, description };
		String selection;
		if (Ufosightings.pagerCounterTotal == 0) {
			//Log.w(TAG,					"In DBAdapter.getByLocationWorld(String location), pagerCounterTotal ==0");
			selection = "location_state LIKE ? LIMIT 10";
			//Log.w(TAG,					"In DBAdapter.getByLocationWorld(String location), set selection to:"							+ selection);
		} else {
			//Log.w(TAG,					"In DBAdapter.getByLocationWorld(String location), pagerCounterTotal NOT 0");
			selection = "location_state LIKE ? LIMIT "+ Integer.toString(Ufosightings.pagerCounterTotal) + ",10";
			//Log.w(TAG,					"In DBAdapter.getByLocationWorld(String location), set selection to:"+ selection);
		}
		locationParam = "%" + locationParam;
		String[] selectionArgs = new String[] { locationParam };
		//Log.w(TAG,				"In DBAdapter.getByLocation(), About to make Cursor, c, with db.query()");
		Cursor mCursor = db.query(DATABASE_TABLE, columns, selection,
				selectionArgs, null, null, null);
		//Log.w(TAG,				"In DBAdapter.getByLocation(String location), About to check if Cursor c is null");
		if (mCursor != null) {
			//Log.w(TAG,					"In DBAdapter.getByLocation(String location), c is NOT null, about to c.moveToFirst()");
			mCursor.moveToFirst();
		}
		//Log.w(TAG,				"In DBAdapter.getByLocation(String location), about to return cursor, c");
		return mCursor;
	}

	public Cursor getByShape(String shapeParam) throws SQLException {
		String[] columns = new String[] { sighted_at_year, sighted_at_month,
				sighted_at_day, reported_at, location_city, location_state,
				shape, duration, description };
		String selection = "shape=?";
		String[] selectionArgs = new String[] { shapeParam };
		//Log.w(TAG,				"In DBAdapter.getByShape(), About to make Cursor, c, with db.query()");
		Cursor mCursor = db.query(DATABASE_TABLE, columns, selection,
				selectionArgs, null, null, null);
		//Log.w(TAG,				"In DBAdapter.getByShape(String shape), About to check if Cursor c is null");
		if (mCursor != null) {
			//Log.w(TAG,					"In DBAdapter.getByShape(String shape), c is NOT null, about to c.moveToFirst()");
			mCursor.moveToFirst();
		}
		//Log.w(TAG,				"In DBAdapter.getByShape(String shape), about to return cursor, c");
		return mCursor;
	}

	public Cursor getByDescription(String descriptionParam) throws SQLException {
		String[] columns = new String[] { sighted_at_year, sighted_at_month,
				sighted_at_day, reported_at, location_city, location_state,
				shape, duration, description };
		String selection;
		if (Ufosightings.pagerCounterTotal == 0) {
			//Log.w(TAG,					"In DBAdapter.getByDescription(String description), pagerCounterTotal ==0");
			selection = "description LIKE ? LIMIT 10";
			//Log.w(TAG,					"In DBAdapter.getByDescription(String description), set selection to:"							+ selection);
		} else {
			//Log.w(TAG,					"In DBAdapter.getByDescription(String description), pagerCounterTotal NOT 0");
			selection = "description LIKE ? LIMIT "	+ Integer.toString(Ufosightings.pagerCounterTotal) + ",10";
			//Log.w(TAG,					"In DBAdapter.getByDescription(String description), set selection to:"					+ selection);
		}

		// WORKS String selection = "description LIKE ? LIMIT 10";
		String[] selectionArgs = new String[] { "%" + descriptionParam + "%" };
		//Log.w(TAG,				"In DBAdapter.getByDescription(), About to make Cursor, c, with db.query()");
		Cursor mCursor = db.query(DATABASE_TABLE, columns, selection, selectionArgs, null, null, null);
		//Log.w(TAG,				"In DBAdapter.getByDescription(String description), About to check if Cursor c is null");
		if (mCursor != null) {
			//Log.w(TAG,					"In DBAdapter.getByDescription(String description), c is NOT null, about to c.moveToFirst()");
			mCursor.moveToFirst();
		}
		//Log.w(TAG,				"In DBAdapter.getByDescription(String description), about to return cursor, c");
		return mCursor;
	}

	public Cursor getByExactDate(String yearParam, String monthParam,
			String dayParam) throws SQLException {
		String[] columns = new String[] { sighted_at_year, sighted_at_month,
				sighted_at_day, reported_at, location_city, location_state,
				shape, duration, description };
		// String selection = "sighted_at=?";
		String selection = "sighted_at_year=? AND sighted_at_month=? AND sighted_at_day=?";
		// String selection = "sighted_at_month=? AND sighted_at_day=?";
		String[] selectionArgs = new String[] { yearParam, monthParam, dayParam };
		//Log.w(TAG,				"In DBAdapter.getByDate(), About to make Cursor, c, with db.query()");
		Cursor mCursor = db.query(DATABASE_TABLE, columns, selection,
				selectionArgs, null, null, null);
		//Log.w(TAG,				"In DBAdapter.getByDate(String date), About to check if Cursor c is null");
		if (mCursor != null) {
			//Log.w(TAG,					"In DBAdapter.getByDate(String date), c is NOT null, about to c.moveToFirst()");
			mCursor.moveToFirst();
		}
		//Log.w(TAG,				"In DBAdapter.getByDate(String date), about to return cursor, c");
		return mCursor;
	}

	public Cursor getByDate(String yearParam, String monthParam, String dayParam)
			throws SQLException {
		String[] columns = new String[] { sighted_at_year, sighted_at_month,
				sighted_at_day, reported_at, location_city, location_state,
				shape, duration, description };
		// String selection = "sighted_at=?";
		// String selection =
		// "sighted_at_year=? AND sighted_at_month=? AND sighted_at_day=?";
		// This line can return an enormous amount of records for days like july
		// 4th:
		// String selection = "sighted_at_month=? AND sighted_at_day=?";
		// ...and so add a limit:
		String selection = "sighted_at_month=? AND sighted_at_day=? LIMIT 10";
		String[] selectionArgs = new String[] { monthParam, dayParam };
		//Log.w(TAG,				"In DBAdapter.getByDate(), About to make Cursor, c, with db.query()");
		Cursor mCursor = db.query(DATABASE_TABLE, columns, selection,
				selectionArgs, null, null, null);
		//Log.w(TAG,				"In DBAdapter.getByDate(String date), About to check if Cursor c is null");
		if (mCursor != null) {
			//Log.w(TAG,					"In DBAdapter.getByDate(String date), c is NOT null, about to c.moveToFirst()");
			mCursor.moveToFirst();
		}
		//Log.w(TAG,				"In DBAdapter.getByDate(String date), about to return cursor, c");
		return mCursor;
	}
}
