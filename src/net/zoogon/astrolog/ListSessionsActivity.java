package net.zoogon.astrolog;

import java.util.List;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListSessionsActivity extends Activity {

	private SessionsDAO dataSource;
	private List<Session> values;

	//request codes for onActivityResult
	public static final int ADD_SESSION_REQUEST = 1; 
	public static final int EDIT_SESSION_REQUEST = 2;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		Log.w(ACTIVITY_SERVICE, "ListSessionsActivity starting");
				
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// updateSessionList();
		dataSource = new SessionsDAO(this);

		updateSessionList();
				
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}	
	
	private void updateSessionList() {
		dataSource.open();

	
		// filling the viewList
		ListView listView = (ListView) findViewById(R.id.vl_sessions);
	
		values = dataSource.getAllSessions();
	
		//TODO show message if there is no sessions (invite to create some)
		
		ArrayAdapter<Session> adapter = new ArrayAdapter<Session>(this,
				android.R.layout.simple_list_item_1, values);
		
		listView.setAdapter(adapter);
		
		// add a event to each row
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
				//editSession(values.get(position).getId());
				listObservations(values.get(position).getId());
			}
		}); 
		// TODO add long click listener to delete sessions
		
		// TODO fill stats counter
		dataSource.close();

		
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

	
	/**
	 * Launch ListObservations activity 
	 * @param session_id
	 */
	public void listObservations(long session_id){
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
		
		dataSource.open();

		switch (requestCode) {
		//TODO update list and request codes
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
				// TODO combine with resources %d
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
