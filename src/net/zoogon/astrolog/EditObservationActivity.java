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
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.EditText;
import android.widget.RatingBar;

public class EditObservationActivity extends FragmentActivity implements
		OnDateSetListener, OnTimeSetListener {

	public static final int ADD_OBSERVATION_REQUEST = 1;
	public static final int EDIT_OBSERVATION_REQUEST = 0;

	private ObservationsDAO dataSource;
	private int request_code;
	private long observation_id;
	private long session_id;

	private Date date;
	private String object_id;
	private String telescope;
	private String eyepiece;
	private String barlow;
	private float seeing;
	private float rate;
	private String notes;

	private Observation observation;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_observation);

		dataSource = new ObservationsDAO(this);

		// check if main activity wants to create a new observation
		// or edit an existing one
		request_code = getIntent().getExtras().getInt("request_code");

		if (request_code == EDIT_OBSERVATION_REQUEST) {
			observation_id = getIntent().getExtras().getLong("observation_id");
			loadObservation(observation_id);
		} else {
			session_id = getIntent().getExtras().getLong("session_id");
			setDefaultValues();
		}

	}

	private void setDefaultValues() {
		date = new Date();
	
		updateDateLabel(date);
	}
	
	private void updateDateLabel(Date date){

		String local_time_st = SimpleDateFormat.getDateInstance(
				SimpleDateFormat.SHORT).format(date)
				+ " - "
				+ SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT)
						.format(date);
		((TextView) findViewById(R.id.lb_local_date_time))
		.setText(local_time_st);
	}

	/**
	 * takes observation_id and looks on DB for that row filling the layout
	 * fields
	 * 
	 * @param observation_id
	 */
	private void loadObservation(long observation_id) {

		Log.w("EditObservation",
				"Editing observation, retrieving observation row");

		// retrieve observation_row from DB
		dataSource.open();
		observation = dataSource.getObservation(observation_id);

		if (observation != null) {

			session_id = observation.getSessionId();
			date = observation.getDatetime();
			object_id = observation.getObjectId();
			telescope = observation.getTelescope();
			eyepiece = observation.getEyepiece();
			barlow = observation.getBarlow();
			seeing = observation.getSeeing();
			rate = observation.getRate();
			notes = observation.getNotes();

			updateDateLabel(date);

			((EditText) findViewById(R.id.tf_object_id)).setText(object_id);

			((EditText) findViewById(R.id.tf_telescope)).setText(telescope);
			((EditText) findViewById(R.id.tf_eyepiece)).setText(eyepiece);
			((EditText) findViewById(R.id.tf_barlow)).setText(barlow);
			((RatingBar) findViewById(R.id.rb_rate)).setRating(rate);
			((RatingBar) findViewById(R.id.rb_seeing)).setRating(seeing);
			((EditText) findViewById(R.id.tf_notes)).setText(notes);
		}

		dataSource.close();

	}

	public void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragmentObservation();

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
		DialogFragment newFragment = new TimePickerFragmentObservation();

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

		// object id not empty
		tf_to_validate = (EditText) findViewById(R.id.tf_object_id);
		if (tf_to_validate.getText().toString().length() == 0) {
			tf_to_validate.setError(getText(R.string.error_title_length_0));
			flag = false;
		} else {
			tf_to_validate.setError(null);
		}

		// the rest of field are optional

		return flag;
	}

	public void saveObservation(View view) {

		if (validateInput()) {

			// get the field values
			object_id = ((EditText) findViewById(R.id.tf_object_id)).getText()
					.toString();
			telescope = ((EditText) findViewById(R.id.tf_telescope)).getText()
					.toString();
			eyepiece = ((EditText) findViewById(R.id.tf_eyepiece)).getText()
					.toString();
			barlow = ((EditText) findViewById(R.id.tf_barlow)).getText()
					.toString();
			seeing = ((RatingBar) findViewById(R.id.rb_seeing)).getRating();
			rate = ((RatingBar) findViewById(R.id.rb_seeing)).getRating();

			notes = ((EditText) findViewById(R.id.tf_notes)).getText()
					.toString();

			// date is set on validation method

			dataSource.open();

			if (request_code == ADD_OBSERVATION_REQUEST) {

				Log.w("EditObservationActivity",
						"Inserting new record on OBSERVATIONS table");

				Observation observation = dataSource.createObservation(
						session_id, date, object_id, telescope, eyepiece,
						barlow, seeing, rate, notes);
				observation_id = observation.getId();

				Log.w("EditObservationActivity",
						"Inserted observation. New index = " + observation_id);

			} else {

				Log.w("EditObservationActivity", "Updating record "
						+ observation_id + " on OBSERVATIONS table");

				dataSource.updateObservation(observation_id, session_id, date,
						object_id, telescope, eyepiece, barlow, seeing, rate,
						notes);

				Log.w("EditObservationActivity", "Record " + observation_id
						+ " updated on OBSERVATIONS table");
			}
			dataSource.close();
			endActivityOK(observation_id);

		}
	}

	private void endActivityOK(long observation_id) {
		Intent resultIntent = new Intent();
		resultIntent.putExtra("observation_id", observation_id);
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}

}
