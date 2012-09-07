package net.zoogon.astrolog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class EditSessionActivity extends FragmentActivity implements
		OnDateSetListener, OnTimeSetListener {

	public static final int ADD_SESSION_REQUEST = 1;
	public static final int EDIT_SESSION_REQUEST = 0;

	private SessionsDAO dataSource;
	private int request_code;
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

		dataSource = new SessionsDAO(this);

		// check if main activity wants to create a new session
		// or edit an existing one
		request_code = getIntent().getExtras().getInt("request_code");

		if (request_code == EDIT_SESSION_REQUEST) {
			session_id = getIntent().getExtras().getLong("session_id");
			loadSession(session_id);
		} else
			setDefaultValues();
	}

	private void setDefaultValues() {
		date = new Date();
		updateDateLabel(date);

	}

	private void updateDateLabel(Date date) {

		String local_time_st = SimpleDateFormat.getDateInstance(
				SimpleDateFormat.SHORT).format(date)
				+ " - "
				+ SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT)
						.format(date);
		((TextView) findViewById(R.id.lb_date))
				.setText(local_time_st);
	}

	/**
	 * takes session_id and looks on DB for that row filling the layout fields
	 * 
	 * @param session_id
	 */
	private void loadSession(long session_id) {

		Log.w("EditSession", "Editing session, retrieving session row");

		// retrieve session_row from DB
		dataSource.open();
		session = dataSource.getSession(session_id);

		if (session != null) {
			// fill the text views
			((EditText) findViewById(R.id.tf_title))
					.setText(session.getTitle());
			((EditText) findViewById(R.id.tf_location)).setText(session
					.getLocation());
			((EditText) findViewById(R.id.tf_notes))
					.setText(session.getNotes());

			date = session.getDate();
			updateDateLabel(date);

		}

		dataSource.close();

	}

	public void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragmentSession();

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		Bundle args = new Bundle();
		args.putInt("year", cal.get(Calendar.YEAR));
		args.putInt("month", cal.get(Calendar.MONTH));
		args.putInt("day", cal.get(Calendar.DAY_OF_MONTH));
		newFragment.setArguments(args);
		newFragment.show(getSupportFragmentManager(), "datePicker");
	}

	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(year, month, day, cal.get(Calendar.HOUR_OF_DAY),
				cal.get(Calendar.MINUTE));
		date = cal.getTime();
		updateDateLabel(date);
	}

	public void showTimePickerDialog(View v) {
		DialogFragment newFragment = new TimePickerFragmentSession();

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		Bundle args = new Bundle();
		args.putInt("hourOfDay", cal.get(Calendar.HOUR_OF_DAY));
		args.putInt("minute", cal.get(Calendar.MINUTE));
		newFragment.setArguments(args);
		newFragment.show(getSupportFragmentManager(), "timePicker");
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);
		date = cal.getTime();

		updateDateLabel(date);

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

		// the rest of fields are optional

		return flag;
	}

	public void saveSession(View view) {

		if (validateInput()) {

			// get the field values
			title = ((EditText) findViewById(R.id.tf_title)).getText()
					.toString();
			location = ((EditText) findViewById(R.id.tf_location)).getText()
					.toString();
			notes = ((EditText) findViewById(R.id.tf_notes)).getText()
					.toString();

			// date is set on validation method

			dataSource.open();

			if (request_code == ADD_SESSION_REQUEST) {

				Log.w("EditSessionActivity",
						"Inserting new record on SESSIONS table");

				Session session = dataSource.createSession(title, date,
						location, notes);
				session_id = session.getId();

				Log.w("EditSessionActivity", "Inserted session. New index = "
						+ session_id);

			} else {

				Log.w("EditSessionActivity", "Updating record " + session_id
						+ " on SESSIONS table");

				dataSource.updateSession(session_id, title, date, location,
						notes);

				Log.w("EditSessionActivity", "Record " + session_id
						+ " updated on SESSIONS table");
			}
			dataSource.close();
			endActivityOK(session_id);

		}
	}

	private void endActivityOK(long session_id) {
		Intent resultIntent = new Intent();
		resultIntent.putExtra("session_id", session_id);
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}
}
