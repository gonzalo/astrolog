package net.zoogon.astrolog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AstrologDBOpenHelper extends SQLiteOpenHelper {

	// DB schema metadata
	public static final String DATABASE_NAME = "AstrologDB.db";
	public static final int DATABASE_VERSION = 1;
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"; //iso8601

	// DB table names
	public static final String DATABASE_SESSIONS_TABLE = "sessions";

	// DB columns names
	public static final String SESSION_ID = "_id";
	public static final String SESSION_TITLE = "title";
	public static final String SESSION_DATE = "session_date";
	public static final String SESSION_LOCATION = "location";
	public static final String SESSION_NOTES = "notes";

	// DB schema creation
	private static final String DATABASE_CREATE = "create table "
			+ DATABASE_SESSIONS_TABLE + " ("
			+ SESSION_ID + " integer primary key autoincrement, "
			+ SESSION_TITLE	+ " text not null, "
			+ SESSION_DATE + " date not null, "
			+ SESSION_LOCATION + " text not null, "
			+ SESSION_NOTES	+ " text not null "
			+ " );";

	public AstrologDBOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	// Called when no database exists in disk and the helper class needs
	// to create a new one.
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	// Called when there is a database version mismatch meaning that
	// the version of the database on disk needs to be upgraded to
	// the current version.
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Log the version upgrade.
		Log.w("TaskDBAdapter", "Upgrading from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		// Upgrade the existing database to conform to the new
		// version. Multiple previous versions can be handled by
		// comparing oldVersion and newVersion values.
		// The simplest case is to drop the old table and create a new one.
		db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_SESSIONS_TABLE);

		// KEEP ATENTION OLD DATA WILL BE DROPPED BY THIS METHOD.
		// NEWER VERSIONS SHOULD MIGRATE OLD DATA TO NEW DB SCHEMA
	}

	/**
	 * cleanUp function empties database tables keeping schema. Use carefully
	 * first declared as private to "avoid" temptations. Change to public if
	 * needed.
	 */

	@SuppressWarnings("unused")
	private void cleanUP() {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("DELETE FROM " + DATABASE_SESSIONS_TABLE);

	}
	
	public Cursor sampleQuery() {
		
		// Specify the result column projection. Return the minimum set
		// of columns required to satisfy your requirements.
		String[] result_columns = new String[] { SESSION_ID, SESSION_TITLE,
				SESSION_DATE, SESSION_LOCATION, SESSION_NOTES };
		// Specify the where clause that will limit our results.
		String where = null;
		// Replace these with valid SQL statements as necessary.
		String whereArgs[] = null;
		String groupBy = null;
		String having = null;
		String order = null;
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.query(DATABASE_SESSIONS_TABLE, result_columns,
				where, whereArgs, groupBy, having, order);
		return cursor;
	}
	
	public static final String formatDateToString(Date date){
		
		SimpleDateFormat iso8601Format = new SimpleDateFormat(DATE_FORMAT);
		String st_date = iso8601Format.format(date);

		return st_date; 
	}
	
	public static final Date formatStringToDate(String st_date){
		
		SimpleDateFormat iso8601Format = new SimpleDateFormat(DATE_FORMAT);
		Date date = new Date();
		try {
			date = iso8601Format.parse(st_date);
		} catch (Exception e) {
			Log.w("Error","Error parsing date from database");
		}

		return date; 
	}
}
