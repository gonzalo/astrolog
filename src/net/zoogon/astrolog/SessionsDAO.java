package net.zoogon.astrolog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SessionsDAO {
	// Database fields
	private SQLiteDatabase database;
	private AstrologDBOpenHelper dbHelper;
	private String[] allColumns = { AstrologDBOpenHelper.SESSION_ID,
			AstrologDBOpenHelper.SESSION_TITLE,
			AstrologDBOpenHelper.SESSION_DATE,
			AstrologDBOpenHelper.SESSION_LOCATION,
			AstrologDBOpenHelper.SESSION_NOTES };

	public SessionsDAO(Context context) {
		dbHelper = new AstrologDBOpenHelper(context,
				AstrologDBOpenHelper.DATABASE_NAME, null,
				AstrologDBOpenHelper.DATABASE_VERSION);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Session createSession(String title, Date date, String location,
			String notes) {

		ContentValues newValues = new ContentValues();

		String st_date = AstrologDBOpenHelper.formatDateToString(date);

		// preparing row to be inserted
		newValues.put(AstrologDBOpenHelper.SESSION_TITLE, title);
		newValues.put(AstrologDBOpenHelper.SESSION_DATE, st_date);
		newValues.put(AstrologDBOpenHelper.SESSION_LOCATION, location);
		newValues.put(AstrologDBOpenHelper.SESSION_NOTES, notes);

		long insertId = database.insert(
				AstrologDBOpenHelper.DATABASE_SESSIONS_TABLE, null, newValues);
		Cursor cursor = database.query(
				AstrologDBOpenHelper.DATABASE_SESSIONS_TABLE, allColumns,
				AstrologDBOpenHelper.SESSION_ID + " = " + insertId, null, null,
				null, null);
		cursor.moveToFirst();
		Session newSession = cursorToSession(cursor);
		cursor.close();
		return newSession;
	}

	public void deleteSession(Session session) {
		long id = session.getId();
		Log.w("SessionDAO", "Comment deleted with id: " + id);
		database.delete(AstrologDBOpenHelper.DATABASE_SESSIONS_TABLE,
				AstrologDBOpenHelper.SESSION_ID + " = " + id, null);
	}

	public List<Session> getAllSessions() {
		List<Session> sessions = new ArrayList<Session>();

		// configuring query
		// Specify the result column projection. Return the minimum set
		// of columns required to satisfy your requirements.
		String[] result_columns = allColumns;
		String where = null;
		String whereArgs[] = null;
		String groupBy = null;
		String having = null;
		String order = AstrologDBOpenHelper.SESSION_DATE + " DESC";

		Cursor cursor = database.query(AstrologDBOpenHelper.DATABASE_SESSIONS_TABLE,
				result_columns, where, whereArgs, groupBy, having, order);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Session session = cursorToSession(cursor);
			sessions.add(session);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return sessions;
	}
	
	public Session getSession(long id){
		Session session = new Session();

		// configuring query
		// Specify the result column projection. Return the minimum set
		// of columns required to satisfy your requirements.
		String[] result_columns = allColumns;
		String where = AstrologDBOpenHelper.SESSION_ID + "=" + id;
		String whereArgs[] = null;
		String groupBy = null;
		String having = null;
		String order = null;

		Cursor cursor = database.query(AstrologDBOpenHelper.DATABASE_SESSIONS_TABLE,
				result_columns, where, whereArgs, groupBy, having, order);

		cursor.moveToFirst();
		if (!cursor.isAfterLast()) {
			session = cursorToSession(cursor);			
		}
		// Make sure to close the cursor
		cursor.close();
		return session;		
	}

	private Session cursorToSession(Cursor cursor) {
		Session session = new Session();
		
		int indexId = cursor
				.getColumnIndexOrThrow(AstrologDBOpenHelper.SESSION_ID);
		int indexTitle = cursor
				.getColumnIndexOrThrow(AstrologDBOpenHelper.SESSION_TITLE);
		int indexDate = cursor
				.getColumnIndexOrThrow(AstrologDBOpenHelper.SESSION_DATE);
		int indexLocation = cursor
				.getColumnIndexOrThrow(AstrologDBOpenHelper.SESSION_LOCATION);
		int indexNotes = cursor
				.getColumnIndexOrThrow(AstrologDBOpenHelper.SESSION_NOTES);
		
		session.setId(cursor.getLong(indexId));
		session.setTitle(cursor.getString(indexTitle));
		session.setDate(AstrologDBOpenHelper.formatStringToDate(cursor.getString(indexDate)));
		session.setLocation(cursor.getString(indexLocation));
		session.setNotes(cursor.getString(indexNotes));
		return session;
	}

}
