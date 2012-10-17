package net.zoogon.astrolog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SessionsArrayAdapter extends ArrayAdapter<Session> {
	int resource;

	public SessionsArrayAdapter(Context context, int _resource,
			List<Session> items) {
		super(context, _resource, items);
		resource = _resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Create and inflate the View to display
		LinearLayout newView;

		Session session = getItem(position);
		long id = session.getId();
		Date date = session.getDate();
		String location = session.getLocation();
		String title = session.getTitle();
		String notes = session.getNotes();

		if (convertView == null) {
			// Inflate a new view if this is not an update.
			newView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater li;
			li = (LayoutInflater) getContext().getSystemService(inflater);
			li.inflate(resource, newView, true);
		} else {
			// Otherwise we’ll update the existing View
			newView = (LinearLayout) convertView;
		}

		TextView dateView = (TextView) newView.findViewById(R.id.session_date);
		TextView locationView = (TextView) newView
				.findViewById(R.id.session_location);
		TextView titleView = (TextView) newView
				.findViewById(R.id.session_title);
		TextView notesView = (TextView) newView
				.findViewById(R.id.session_notes);

		String date_st = SimpleDateFormat.getDateInstance(
				SimpleDateFormat.SHORT).format(session.getDate());
		dateView.setText(date_st);
		locationView.setText(location);
		if (title.length() != 0) {
			titleView.setText(title);
			titleView.setVisibility(View.VISIBLE);
		} else
			titleView.setVisibility(View.GONE);
		if (notes.length() != 0) {
			notesView.setText(notes);
			notesView.setVisibility(View.VISIBLE);
		} else
			notesView.setVisibility(View.GONE);

		return newView;
	}
}
