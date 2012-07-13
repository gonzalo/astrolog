package net.zoogon.astrolog;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

public class EditSession extends Activity {

	public static final int ADD_SESSION_REQUEST = 1;
	public static final int CREATE_SESSION = -1;
	
	private int session_id;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_session);

		// check if main activity wants to create a new session
		// or edit an existing one
		session_id = getIntent().getExtras().getInt("session_id");
		
		if (session_id != CREATE_SESSION)
			loadSession(session_id);
		//else
			//loadDefaultValues();

	}

	private void loadDefaultValues() {
		Log.w("EditSession", "New session, setting default values");
		
		EditText tf_to_fill;
		tf_to_fill = (EditText) findViewById(R.id.tf_title);
		tf_to_fill.setText(getText(R.string.title));
		
		tf_to_fill = (EditText) findViewById(R.id.tf_location);
		tf_to_fill.setText(getText(R.string.location));
		
		tf_to_fill = (EditText) findViewById(R.id.tf_notes);
		tf_to_fill.setText(getText(R.string.notes));

		Log.w("EditSession", "Default values have been setted");

	}

	/**
	 * takes session_id and looks on DB for that row filling the layout fields
	 * 
	 * @param session_id
	 */
	private void loadSession(int session_id) {

		Log.w("EditSession", "Editing session, retrieving session row");
		// TODO Auto-generated method stub
		// retrieve session_row from DB

		// fill the text views

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_edit_session, menu);
		return true;
	}

	/**
	 * Checks if input files are valid to be inserted in DB
	 * 
	 * @return true if them are valid
	 */
	public boolean validateInput() {

		boolean flag = true;

		EditText tf_to_validate;

		// TODO validate date

		// Title not empty
		tf_to_validate = (EditText) findViewById(R.id.tf_title);
		if (tf_to_validate.getText().toString().length() == 0) {
			tf_to_validate.setError(getText(R.string.error_title_length_0));
			flag = false;
		} else {
			tf_to_validate.setError(null);
		}
		// Location not empty
		tf_to_validate = (EditText) findViewById(R.id.tf_location);
		if (tf_to_validate.getText().toString().length() == 0) {
			tf_to_validate.setError(getText(R.string.error_location_length_0));
			flag = false;
		} else {
			tf_to_validate.setError(null);
		}

		return flag;
	}

	public void saveSession(View view) {

		if (validateInput()) {

			AstrologDBOpenHelper astrologDBOpenHelper = new AstrologDBOpenHelper(
					this, AstrologDBOpenHelper.DATABASE_NAME, null,
					AstrologDBOpenHelper.DATABASE_VERSION);
			SQLiteDatabase db = astrologDBOpenHelper.getWritableDatabase();

			switch (session_id) {
			case CREATE_SESSION:
				Log.w("editSession", "Inserting new record on SESSIONS table");
				ContentValues newValues = new ContentValues();

				// TODO
				// get values from fields
				// Assign values for each row.
				newValues.put(AstrologDBOpenHelper.SESSION_TITLE, 
						((EditText) findViewById(R.id.tf_title)).getText());
				newValues.put(AstrologDBOpenHelper.SESSION_DATE, 
						((DatePicker) findViewById(R.id.dp_date)).get);
				newValues.put(AstrologDBOpenHelper.SESSION_LOCATION,
						((EditText) findViewById(R.id.tf_location)).getText());
				newValues.put(AstrologDBOpenHelper.SESSION_NOTES,
						((EditText) findViewById(R.id.tf_notes)).getText());

				// Insert the row into your table
				long insertedIndex = db.insert(
						AstrologDBOpenHelper.DATABASE_SESSIONS_TABLE, null,
						newValues);

				Log.w("EditSession", "Inserted session. New index = "
						+ insertedIndex);

				endActivityOK();

				break;

			default:
				Log.w("editSession", "Updating record " + session_id
						+ " on SESSIONS table");
				//TODO update record
				Log.w("editSession", "Record " + session_id
						+ " updated on SESSIONS table");
				break;
			}

			db.close();
			astrologDBOpenHelper.close();
		}
	}
	
	private void endActivityOK(){
		Intent resultIntent = new Intent();
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}
}
