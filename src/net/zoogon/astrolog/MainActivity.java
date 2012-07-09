package net.zoogon.astrolog;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

@SuppressLint({ "ParserError", "ParserError", "ParserError" })
public class MainActivity extends Activity {

	public static final int ADD_SESSION_REQUEST = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/**
	 * Launch editSession activity in creation mode
	 * 
	 * @param view
	 */
	public void addSession(View view) {
		Intent intent = new Intent(this, editSession.class);
		startActivityForResult(intent, ADD_SESSION_REQUEST);
	}

	/**
	 * Launched after activity called with startActivityForResult finishes
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		String message = "";
		Resources myResources = getResources();

		switch (requestCode) {
		case ADD_SESSION_REQUEST:
			switch (resultCode) {
			case Activity.RESULT_OK:
				popUp(R.string.message_done);
				// TODO update sessions list
				break;

			case Activity.RESULT_CANCELED:
				popUp(R.string.message_canceled);
				break;

			default:
				//TODO combine with resources %d
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
	 * Just a code snippet for toast To use in conjuntion with resources class
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
