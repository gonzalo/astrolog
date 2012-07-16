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

	//AstrologDBOpenHelper astrologDBOpenHelper;
	//SQLiteDatabase db;
	private SessionsDAO dataSource;
	private List<Session> values;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		Log.w(ACTIVITY_SERVICE, "MainActivity starting");
				
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// updateSessionList();
		dataSource = new SessionsDAO(this);
		dataSource.open();

		updateSessionList();
		
	}

	private void updateSessionList() {


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
				editSession(values.get(position).getId());
			}
		}); 
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		dataSource.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		dataSource.close();
		super.onPause();
	}	
	
	/**
	 * Launch editSession activity in creation mode
	 * 
	 * @param view
	 */
	public void addSession(View view) {
		dataSource.close();
		Intent intent = new Intent(this, EditSessionActivity.class);
		intent.putExtra("session_id", EditSessionActivity.CREATE_SESSION);
		startActivityForResult(intent, EditSessionActivity.ADD_SESSION_REQUEST);
	}
	
	/**
	 * Launch editSession activity to edit some session
	 * 
	 * @param view
	 */
	public void editSession(long session_id) {
		Intent intent = new Intent(this, EditSessionActivity.class);
		intent.putExtra("session_id", session_id);
		startActivityForResult(intent, EditSessionActivity.EDIT_SESSION_REQUEST);
	}
	
	/**
	 * Launched after activity called with startActivityForResult finishes
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		String message = "";
		
		dataSource.open();

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
