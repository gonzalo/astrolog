package net.zoogon.astrolog;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListSessionsActivity extends Activity {

	AstrologDBOpenHelper astrologDBOpenHelper;
	SQLiteDatabase db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		Log.w(ACTIVITY_SERVICE, "MainActivity starting");
				
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		updateSessionList();
		
	}

	private void updateSessionList() {
		
		
		List<String> values = new ArrayList<String>();
		
		// DBHelper creation and DB connection
		astrologDBOpenHelper = new AstrologDBOpenHelper(this, AstrologDBOpenHelper.DATABASE_NAME, null, AstrologDBOpenHelper.DATABASE_VERSION);
		db = astrologDBOpenHelper.getWritableDatabase();
		Cursor sCursor = getSessions(db);

		int columnIndex = sCursor
				.getColumnIndexOrThrow(AstrologDBOpenHelper.SESSION_TITLE);

		sCursor.moveToPosition(-1);
		while (sCursor.moveToNext()) {
			if (columnIndex > -1) {
				values.add(sCursor.getString(columnIndex));
			} else {

				popUp("Column not found!");
				
			}
		}
		
		//TODO show message if there is no sessions (invite to create some)

		// filling the viewList
		ListView listView = (ListView) findViewById(R.id.vl_sessions);

		// First parameter - Context
		// Second parameter - Layout for the row
		// Third parameter - ID of the View to which the data is written
		// Forth - the Array of data
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, values);

		// Assign adapter to ListView
		listView.setAdapter(adapter);

		// close cursor
		sCursor.close();
		// close DB
		db.close();
		// close helper
		astrologDBOpenHelper.close();		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/**
	 * Launch editSession activity in creation mode
	 * 
	 * @param view
	 */
	public void addSession(View view) {
		Intent intent = new Intent(this, EditSessionActivity.class);
		intent.putExtra("session_id", EditSessionActivity.CREATE_SESSION);
		startActivityForResult(intent, EditSessionActivity.ADD_SESSION_REQUEST);
	}

	/**
	 * Launched after activity called with startActivityForResult finishes
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		String message = "";

		switch (requestCode) {
		case EditSessionActivity.ADD_SESSION_REQUEST:
			switch (resultCode) {
			case Activity.RESULT_OK:
				popUp(R.string.message_done);
				updateSessionList();
				break;

			case Activity.RESULT_CANCELED:
				popUp(R.string.message_canceled);
				break;

			default:
				// TODO combine with resources %d
				message = new String("Unknown resultCode " + resultCode
						+ ". Try it again");
				popUp(message);

				break;
			}
			break;
		default:
			popUp(R.string.message_unknow_requestCode);
			break;
		}
	}

	/**
	 * Just a code snippet for toast To use in conjuntion with resources class
	 * (ex:R.string.message_done) or a String object
	 * 
	 * @param message
	 */
	private void popUp(int message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	private void popUp(CharSequence message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	private Cursor getSessions(SQLiteDatabase db) {

		// configuring query
		// Specify the result column projection. Return the minimum set
		// of columns required to satisfy your requirements.
		String[] result_columns = new String[] {
				AstrologDBOpenHelper.SESSION_ID,
				AstrologDBOpenHelper.SESSION_TITLE };
		String where = null;
		String whereArgs[] = null;
		String groupBy = null;
		String having = null;
		String order = AstrologDBOpenHelper.SESSION_DATE + " DESC";

		// execute query
		Cursor cursor = db.query(AstrologDBOpenHelper.DATABASE_SESSIONS_TABLE,
				result_columns, where, whereArgs, groupBy, having, order);
		int SESSION_ID_COLUMN_INDEX = cursor.getColumnIndexOrThrow(AstrologDBOpenHelper.SESSION_ID);

		
		while (cursor.moveToNext()) {
			Log.w("database", "session row id" + cursor.getInt(SESSION_ID_COLUMN_INDEX));					
		}
		cursor.moveToFirst();

		// return result
		return cursor;
	}
}
