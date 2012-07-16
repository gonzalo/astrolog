package net.zoogon.astrolog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.text.method.DateTimeKeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

public class EditSessionActivity extends Activity {

	public static final int CREATE_SESSION = -1;
	public static final int ADD_SESSION_REQUEST = 1;
	public static final int EDIT_SESSION_REQUEST = 0;

	private int session_id;

	private String title;
	private String location;
	private String notes;
	
	private Session session;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_session);

		// check if main activity wants to create a new session
		// or edit an existing one
		session_id = getIntent().getExtras().getInt("session_id");

		if (session_id != CREATE_SESSION)
			loadSession(session_id);
	}

	/**
	 * takes session_id and looks on DB for that row filling the layout fields
	 * 
	 * @param session_id
	 */
	private void loadSession(long session_id) {

		Log.w("EditSession", "Editing session, retrieving session row");

		// retrieve session_row from DB
		SessionsDAO dataSource = new SessionsDAO(this);
		dataSource.open();
		session = dataSource.getSession(session_id);

		if (session!=null){
			// fill the text views
			((EditText) findViewById(R.id.tf_title)).setText(session.getTitle());
			((EditText) findViewById(R.id.tf_location)).setText(session.getLocation());
			((EditText) findViewById(R.id.tf_notes)).setText(session.getNotes());
			DatePicker dp_date = (DatePicker) findViewById(R.id.dp_date);			
		
			//TODO
			//fill the datepicker
		}
		
		
		

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

		// ¿validate date?
		// seems it's not necessary

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
	
			switch (session_id) {
			case CREATE_SESSION:
				
				
				Log.w("editSession", "Inserting new record on SESSIONS table");

				SessionsDAO dataSource = new SessionsDAO(this);
				dataSource.open();
				
				//get input values
				ContentValues newValues = new ContentValues();

				title = ((EditText) findViewById(R.id.tf_title)).getText()
						.toString();
				location = ((EditText) findViewById(R.id.tf_location))
						.getText().toString();
				notes = ((EditText) findViewById(R.id.tf_notes)).getText()
						.toString();

				DatePicker dp_date = (DatePicker) findViewById(R.id.dp_date);
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.YEAR, dp_date.getYear());
				calendar.set(Calendar.MONTH, dp_date.getMonth());
				calendar.set(Calendar.DAY_OF_MONTH, dp_date.getDayOfMonth());

				Date date = calendar.getTime();
				
				Session session = dataSource.createSession(title, date, location, notes);
				
				Log.w("EditSession", "Inserted session. New index = "
						+ session.getId());

				dataSource.close();
				endActivityOK();

				break;

			default:
				Log.w("editSession", "Updating record " + session_id
						+ " on SESSIONS table");
				// TODO update record
				Log.w("editSession", "Record " + session_id
						+ " updated on SESSIONS table");
				break;
			}


		}
	}

	private void endActivityOK() {
		Intent resultIntent = new Intent();
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}
}
