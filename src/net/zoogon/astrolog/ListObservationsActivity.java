package net.zoogon.astrolog;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class ListObservationsActivity extends Activity {

	private SessionsDAO sessionsDataSource;
	private Session session;
	private long session_id;
	//private ObservationsDAO observationsDataSource;
	//private List<Observation> values;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_observations);
        
        //TODO 
        //log
        Log.w(ACTIVITY_SERVICE, "ListObservationsActivity starting");
        
        //open data sources
        
        //get session data and load values
        session_id = getIntent().getExtras().getLong("session_id");
		sessionsDataSource = new SessionsDAO(this);
		sessionsDataSource.open();
		updateSession(session_id);
		
        //get session observations and load on viewlist
		
        
        //close data sources
    }

    private void updateSession(long session_id) {
		session = sessionsDataSource.getSession(session_id);
		TextView session_title = (TextView) findViewById(R.id.tv_title);
		session_title.setText(session.getTitle());
		TextView session_date = (TextView) findViewById(R.id.tv_date);
		session_date.setText(session.getDate().toString());
		TextView session_location = (TextView) findViewById(R.id.tv_location);
		session_location.setText(session.getLocation());
		TextView session_notes = (TextView) findViewById(R.id.tv_notes);
		session_notes.setText(session.getNotes());
		

		
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_list_observations, menu);
        return true;
    }

    
}
