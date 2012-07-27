package net.zoogon.astrolog;

import java.text.SimpleDateFormat;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ListObservationsActivity extends Activity {

	private SessionsDAO sessionsDataSource;
	private Session session;
	private long session_id;

	// private ObservationsDAO observationsDataSource;
	// private List<Observation> values;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_observations);

		// TODO
		// log
		Log.w(ACTIVITY_SERVICE, "ListObservationsActivity starting");

		// open data sources

		// get session data and load values
		session_id = getIntent().getExtras().getLong("session_id");
		sessionsDataSource = new SessionsDAO(this);
		sessionsDataSource.open();
		updateSession(session_id);

		// get session observations and load on viewlist

		// close data sources
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_list_observations, menu);
		return true;
	}

	private void updateSession(long session_id) {
		session = sessionsDataSource.getSession(session_id);
		TextView session_title = (TextView) findViewById(R.id.tv_title);
		session_title.setText(session.getTitle());
		TextView session_date = (TextView) findViewById(R.id.tv_date);
		String date_st = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT).format(session.getDate());
		session_date.setText(date_st);
		TextView session_location = (TextView) findViewById(R.id.tv_location);
		session_location.setText(session.getLocation());
		TextView session_notes = (TextView) findViewById(R.id.tv_notes);
		session_notes.setText(session.getNotes());

	}

	public void editSession(View view) {
		Intent intent = new Intent(this, EditSessionActivity.class);
		intent.putExtra("request_code",
				EditSessionActivity.EDIT_SESSION_REQUEST);
		intent.putExtra("session_id", session_id);
		startActivityForResult(intent, EditSessionActivity.EDIT_SESSION_REQUEST);
	}

	/**
	 * Launched after activity called with startActivityForResult finishes
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		sessionsDataSource.open();

		switch (requestCode) {
		case EditSessionActivity.EDIT_SESSION_REQUEST:
			switch (resultCode) {
			case Activity.RESULT_OK:
				long session_id = data.getExtras().getLong("session_id");
				updateSession(session_id);
				// TODO: listObservations(session_id);
				break;

			case Activity.RESULT_CANCELED:
				popUp(R.string.message_canceled);
				break;
			}
			break;
		default:
			popUp(R.string.message_unknow_requestCode);
			break;
		}
	}

	/**
	 * Just a code snippet for toast To use in with resources class
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

}
