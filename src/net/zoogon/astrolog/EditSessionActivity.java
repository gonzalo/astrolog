package net.zoogon.astrolog;

import java.util.Date;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class EditSessionActivity extends FragmentActivity {

	public static final int CREATE_SESSION = -1;
	public static final int ADD_SESSION_REQUEST = 1;
	public static final int EDIT_SESSION_REQUEST = 0;

	private long session_id;

	private String title;
	private String location;
	private String notes;
	private Date date;
	
	private Session session;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_session);

		// check if main activity wants to create a new session
		// or edit an existing one
		session_id = getIntent().getExtras().getLong("session_id", CREATE_SESSION);

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
			((EditText) findViewById(R.id.tf_date)).setText(session.getDate().toString());
			//DatePicker dp_date = (DatePicker) findViewById(R.id.dp_date);
			
			//Calendar calendar = Calendar.getInstance();
			
			//calendar.setTime(session.getDate());
			
			//dp_date.init(calendar.YEAR, calendar.MONTH, calendar.DAY_OF_MONTH, null);

		}
		
		
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_edit_session, menu);
		return true;
	}

	
	public void showDatePickerDialog(View v) {
	    DialogFragment newFragment = new DatePickerFragment();
	    newFragment.show(getSupportFragmentManager(), "datePicker");
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
			
			//get the field values
			title = ((EditText) findViewById(R.id.tf_title)).getText()
					.toString();
			location = ((EditText) findViewById(R.id.tf_location))
					.getText().toString();
			notes = ((EditText) findViewById(R.id.tf_notes)).getText()
					.toString();
			
			//date is updated every time we call datepicker

			//DatePicker dp_date = (DatePicker) findViewById(R.id.dp_date);
			
			//Calendar calendar = Calendar.getInstance();
			//calendar.set(Calendar.YEAR, dp_date.getYear());
			//calendar.set(Calendar.MONTH, dp_date.getMonth());
			//calendar.set(Calendar.DAY_OF_MONTH, dp_date.getDayOfMonth());

			//Date date = calendar.getTime();

			SessionsDAO dataSource = new SessionsDAO(this);
			dataSource.open();
			
			if (session_id==CREATE_SESSION) {
				
				Log.w("editSession", "Inserting new record on SESSIONS table");
							
				Session session = dataSource.createSession(title, date, location, notes);
				
				Log.w("EditSession", "Inserted session. New index = "
						+ session.getId());

			} else {
				
				Log.w("editSession", "Updating record " + session_id
						+ " on SESSIONS table");
				
				dataSource.updateSession(session_id, title, date, location, notes);
				
				Log.w("editSession", "Record " + session_id
						+ " updated on SESSIONS table");
			}
			dataSource.close();
			endActivityOK();


		}
	}

	private void endActivityOK() {
		Intent resultIntent = new Intent();
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}
}
