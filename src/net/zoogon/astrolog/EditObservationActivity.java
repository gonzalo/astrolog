package net.zoogon.astrolog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;

public class EditObservationActivity extends FragmentActivity implements
		OnDateSetListener {

	public static final int ADD_OBSERVATION_REQUEST = 1;
	public static final int EDIT_OBSERVATION_REQUEST = 0;

	private ObservationsDAO dataSource;
	private int request_code;
	private long observation_id;

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
		} else
			setDefaultValues();
	}

	private void setDefaultValues() {
		// TODO Auto-generated method stub
		date = new Date();
		String date_st = SimpleDateFormat.getDateInstance(
				SimpleDateFormat.SHORT).format(date);
		String time_st = SimpleDateFormat.getTimeInstance(
				SimpleDateFormat.SHORT).format(date);
		((EditText) findViewById(R.id.tf_date)).setText(date_st);
		((EditText) findViewById(R.id.tf_time)).setText(time_st);

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

			// TODO fill the text views
			/*
			 * ((EditText) findViewById(R.id.tf_title))
			 * .setText(observation.getObjectId()); ((EditText)
			 * findViewById(R.id.tf_location)).setText(observation
			 * .getLocation()); ((EditText) findViewById(R.id.tf_notes))
			 * .setText(observation.getNotes());
			 * 
			 * date = observation.getDate(); String date_st =
			 * SimpleDateFormat.getDateInstance
			 * (SimpleDateFormat.SHORT).format(date);
			 * 
			 * 
			 * ((EditText) findViewById(R.id.tf_date)).setText(date_st);
			 */

		}

		dataSource.close();

	}

	// TODO what about menu button?
	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) {
	 * getMenuInflater().inflate(R.menu.activity_edit_observation, menu); return
	 * true; }
	 */
	public void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragment();

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
		// TODO update date and tf_date
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day);
		date = cal.getTime();
		String date_st = SimpleDateFormat.getDateInstance(
				SimpleDateFormat.SHORT).format(date);
		((EditText) findViewById(R.id.tf_date)).setText(date_st);
	}

	/**
	 * Checks if input files are valid to be inserted in DB
	 * 
	 * @return true if them are valid
	 */
	public boolean validateInput() {

		boolean flag = true;

		EditText tf_to_validate;

		// Date is valid date
		tf_to_validate = (EditText) findViewById(R.id.tf_date);

		try {
			date = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT)
					.parse(tf_to_validate.getText().toString());
		} catch (Exception e) {
			flag = false;
		}

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

				Observation observation = dataSource.createObservation(date,
						object_id, telescope, eyepiece, barlow, seeing, rate,
						notes);
				observation_id = observation.getId();

				Log.w("EditObservationActivity",
						"Inserted observation. New index = " + observation_id);

			} else {

				Log.w("EditObservationActivity", "Updating record "
						+ observation_id + " on OBSERVATIONS table");

				dataSource.updateObservation(observation_id, date, object_id,
						telescope, eyepiece, barlow, seeing, rate, notes);

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
