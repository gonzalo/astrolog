package net.zoogon.astrolog;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AstrologDBOpenHelper extends SQLiteOpenHelper {

	// DB schema metadata
	public static final String DATABASE_NAME = "AstrologDB.db";
	public static final int DATABASE_VERSION = 5;
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"; //iso8601

	// DB table names
	public static final String DATABASE_SESSIONS_TABLE = "sessions";
	public static final String DATABASE_OBSERVATIONS_TABLE = "observations";

	// DB sessions table columns names
	public static final String SESSION_ID = "_id";
	public static final String SESSION_TITLE = "title";
	public static final String SESSION_DATE = "date";
	public static final String SESSION_LOCATION = "location";
	public static final String SESSION_NOTES = "notes";
	
	// DB observations table columns names
	public static final String OBSERVATION_ID = "_id";
	public static final String OBSERVATION_SESSION_ID = "session_id";
	public static final String OBSERVATION_DATE = "date";
	public static final String OBSERVATION_OBJECT_ID = "object_id";
	public static final String OBSERVATION_TELESCOPE = "telescope";
	public static final String OBSERVATION_EYEPIECE = "eyepiece";
	public static final String OBSERVATION_BARLOW = "barlow";
	public static final String OBSERVATION_SEEING = "seeing";
	public static final String OBSERVATION_RATE = "rate";
	public static final String OBSERVATION_NOTES = "notes";
	
	// DB schema creation
	private static final String TABLE_SESSIONS_CREATE = "create table "
			+ DATABASE_SESSIONS_TABLE + " ("
			+ SESSION_ID + " integer primary key autoincrement, "
			+ SESSION_TITLE	+ " text not null, "
			+ SESSION_DATE + " date not null, "
			+ SESSION_LOCATION + " text not null, "
			+ SESSION_NOTES	+ " text"
			+ " );";
	
	private static final String TABLE_OBSERVATIONS_CREATE = "create table "
			+ DATABASE_OBSERVATIONS_TABLE + " ("
			+ OBSERVATION_ID + " integer primary key autoincrement, "
			+ OBSERVATION_SESSION_ID + " integer not null, "
			+ OBSERVATION_DATE + " date not null, "
			+ OBSERVATION_OBJECT_ID + " text not null, "
			+ OBSERVATION_TELESCOPE + " text not null, "
			+ OBSERVATION_EYEPIECE + " text not null, "
			+ OBSERVATION_BARLOW + " text not null, "
			+ OBSERVATION_SEEING + " real not null, "
			+ OBSERVATION_RATE + " real not null, "
			+ OBSERVATION_NOTES + " text"
			+ " );";
	
	public AstrologDBOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	// Called when no database exists in disk and the helper class needs
	// to create a new one.
	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL(TABLE_SESSIONS_CREATE);
			db.execSQL(TABLE_OBSERVATIONS_CREATE);
		} catch (SQLException e) {
			Log.w("AstrologDBOpenHelper", "Failed to create DB, SQL is not valid");
		}
		
	}

	// Called when there is a database version mismatch meaning that
	// the version of the database on disk needs to be upgraded to
	// the current version.
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Log the version upgrade.
		Log.w("AstrologDBOpenHelper", "Upgrading from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		// Upgrade the existing database to conform to the new
		// version. Multiple previous versions can be handled by
		// comparing oldVersion and newVersion values.
		// The simplest case is to drop the old table and create a new one.
		try {
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_SESSIONS_TABLE +";");
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_OBSERVATIONS_TABLE + ";" );

			// KEEP ATENTION OLD DATA WILL BE DROPPED BY THIS METHOD.
			// ADVANCED VERSIONS SHOULD MIGRATE OLD DATA TO NEW DB SCHEMA	
			
			db.execSQL(TABLE_SESSIONS_CREATE);
			db.execSQL(TABLE_OBSERVATIONS_CREATE);
			
		} catch (SQLException e) {
			Log.w("AstrologDBOpenHelper", "Failed to DELETE DB, SQL is not valid");
			System.exit(1);
		}

		Log.w("AstrologDBOpenHelper", "Database updated to version " + newVersion);

	}

	/**
	 * cleanUp function empties database tables keeping schema. Use carefully
	 * first declared as private to "avoid" temptations. Change to public if
	 * needed.
	 */

	public void cleanUP() {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("DELETE FROM " + DATABASE_SESSIONS_TABLE);
		db.execSQL("DELETE FROM " + DATABASE_OBSERVATIONS_TABLE);

	}
	
	/**
	 * useless function just added as example to developers
	 * @return cursor with all sessions on sessions table
	 */
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
	
	/**
	 * function to format dates in standard string to insert it in DB
	 * all dates in DB should be formatted with this function
	 * @param date
	 * @return
	 */
	public static final String formatDateToString(Date date){
		
		SimpleDateFormat iso8601Format = new SimpleDateFormat(DATE_FORMAT);
		String st_date = iso8601Format.format(date);

		return st_date; 
	}
	
	/**
	 * function to parse DB date strings into standard Java date
	 * used by DAO to get dates from DB
	 * @param st_date
	 * @return
	 */
	public static final Date formatStringToDate(String st_date){
		
		SimpleDateFormat iso8601Format = new SimpleDateFormat(DATE_FORMAT);
		Date date = new Date();
		try {
			date = iso8601Format.parse(st_date);
		} catch (Exception e) {
			Log.w("AstrologDBOpenHelper","Error parsing date from database");
		}

		return date; 
	}
}
