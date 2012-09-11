package net.zoogon.astrolog;

import java.util.List;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListSessionsActivity extends Activity {

	private SessionsDAO sessionDataSource;
	private ObservationsDAO observationDataSource;

	private List<Session> values;
	private ArrayAdapter<Session> adapter;

	// request codes for onActivityResult
	public static final int ADD_SESSION_REQUEST = 1;
	public static final int EDIT_SESSION_REQUEST = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		Log.w(ACTIVITY_SERVICE, "ListSessionsActivity starting");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_sessions);

		// updateSessionList();
		sessionDataSource = new SessionsDAO(this);
		observationDataSource = new ObservationsDAO(this);

		updateSessionList();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_list_sessions, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {

		case R.id.add_session:
			addSession();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	// TODO create a cleaner list more information by node
	private void updateSessionList() {

		sessionDataSource.open();

		// filling the viewList
		ListView listView = (ListView) findViewById(R.id.vl_sessions);

		values = sessionDataSource.getAllSessions();
		// TODO show message if there is no sessions (invite to create some)

		// Create the array adapter
		//ArrayAdapter<Session> adapter = new ArrayAdapter<Session>(this,
		//		android.R.layout.simple_list_item_1, values);
		adapter = new ArrayAdapter<Session>(this,
				R.layout.observation_item, values);

		
		listView.setAdapter(adapter);

		// add a event to each row
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//TODO extract id from adapter or listView not external list
				listObservations(values.get(position).getId());
			}
		});

		sessionDataSource.close();

		updateSummary();
	}

	private void updateSummary() {
		String summary_st = "";
		int nSessions, nObservations;
		Resources res = getResources();

		observationDataSource.open();
		nObservations = observationDataSource.getAllObservationsNumber();
		observationDataSource.close();

		sessionDataSource.open();
		nSessions = sessionDataSource.getAllSessionNumber();
		sessionDataSource.close();

		summary_st = res.getQuantityString(R.plurals.numberOfObservations,
				nObservations, nObservations);
		summary_st += " ";
		summary_st += res.getQuantityString(R.plurals.numberOfSessions,
				nSessions, nSessions);

		((TextView) findViewById(R.id.lb_summary)).setText(summary_st);

	}

	/**
	 * Launch editSession activity in creation mode
	 * 
	 * @param view
	 */
	public void addSession(View view) {
		Intent intent = new Intent(this, EditSessionActivity.class);
		intent.putExtra("request_code", EditSessionActivity.ADD_SESSION_REQUEST);
		startActivityForResult(intent, ADD_SESSION_REQUEST);
	}

	public void addSession() {
		Intent intent = new Intent(this, EditSessionActivity.class);
		intent.putExtra("request_code", EditSessionActivity.ADD_SESSION_REQUEST);
		startActivityForResult(intent, ADD_SESSION_REQUEST);
	}

	/**
	 * Launch ListObservations activity
	 * 
	 * @param session_id
	 */
	public void listObservations(long session_id) {
		Intent intent = new Intent(this, ListObservationsActivity.class);
		intent.putExtra("session_id", session_id);
		startActivityForResult(intent, EDIT_SESSION_REQUEST);
	}

	/**
	 * Launched after activity called with startActivityForResult finishes
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		String message = "";

		sessionDataSource.open();

		switch (requestCode) {
		case ADD_SESSION_REQUEST:
			switch (resultCode) {
			case Activity.RESULT_OK:
				long session_id = data.getExtras().getLong("session_id");
				updateSessionList();
				listObservations(session_id);
				break;

			case Activity.RESULT_CANCELED:
				updateSessionList();
				popUp(R.string.message_canceled);
				break;

			default:
				message = new String("Unknown resultCode " + resultCode
						+ ". Try it again");
				popUp(message);

				break;
			}
		case EDIT_SESSION_REQUEST:
			updateSessionList();
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
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	private void popUp(CharSequence message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

}
