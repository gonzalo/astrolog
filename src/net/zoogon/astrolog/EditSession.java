package net.zoogon.astrolog;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class EditSession extends Activity {

	public static final int CREATE_SESSION = -1;
	private int session_id;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_session);
        
        //add textWatchers
        
        //check if main activity wants to create a new session
        //or edit an existing one
        session_id = getIntent().getExtras().getInt("session_id");
        if (session_id != CREATE_SESSION)
        	loadSession(session_id);
        else
        	loadDefaultValues();
        
    }

    private void loadDefaultValues() {
		// TODO Auto-generated method stub
    	Log.w("EditSession", "New session, setting default values");
		
	}

	private void loadSession(int session_id) {
    	
    	Log.w("EditSession", "Editing session, retrieving session row");
		// TODO Auto-generated method stub
    	// retrieve session_row from DB
    	
    	// fill the textfields
		
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_edit_session, menu);
        return true;
    }

	public boolean validateInput(){
		
		boolean flag=true;
		
		//TODO validation methods
		//Title not empty
		
		
		return flag;
	}
    public void saveSession(View view){
    	
    	//TODO
    	//validate fields
    	
		AstrologDBOpenHelper astrologDBOpenHelper = new AstrologDBOpenHelper(this, AstrologDBOpenHelper.DATABASE_NAME, null, AstrologDBOpenHelper.DATABASE_VERSION);
		SQLiteDatabase db = astrologDBOpenHelper.getWritableDatabase();
    	
    	switch (session_id) {
		case CREATE_SESSION:
			ContentValues newValues = new ContentValues();
			
			//TODO
			//get values from fields
			// Assign values for each row.
			newValues.put(AstrologDBOpenHelper.SESSION_TITLE, "title_sample");
			newValues.put(AstrologDBOpenHelper.SESSION_DATE, "date_sample");
			newValues.put(AstrologDBOpenHelper.SESSION_LOCATION, "location_sample");
			newValues.put(AstrologDBOpenHelper.SESSION_NOTES, "notes_sample");

			// Insert the row into your table
			long insertedIndex = db.insert(AstrologDBOpenHelper.DATABASE_SESSIONS_TABLE, null, newValues);		
			
			Log.w("EditSession", "Inserted session. New index = " + insertedIndex);
			
			//TODO
			//close activity
			
			break;

		default:
			break;
		}
    	Log.w("editSession","trying to query DB");
    }
    
}
