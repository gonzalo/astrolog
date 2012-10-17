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

public class ObservationsArrayAdapter extends ArrayAdapter<Observation> {
	int resource;

	public ObservationsArrayAdapter(Context context, int _resource,
			List<Observation> items) {
		super(context, _resource, items);
		resource = _resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Create and inflate the View to display
		LinearLayout newView;

		Observation observation = getItem(position);
		Date date = observation.getDatetime();
		float seeing = observation.getSeeing();
		float rate = observation.getRate();
		String object_id = observation.getObjectId();
		String notes = observation.getNotes();
		String telescope = observation.getTelescope();
		String eyepiece = observation.getEyepiece();
		String barlow = observation.getBarlow();

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


		TextView objectIdView = (TextView) newView
				.findViewById(R.id.observation_object_id);
		TextView timeView = (TextView) newView
				.findViewById(R.id.observation_time);
		TextView seeingView = (TextView) newView
				.findViewById(R.id.observation_seeing);
		TextView rateView = (TextView) newView
				.findViewById(R.id.observation_rate);
		TextView notesView = (TextView) newView
				.findViewById(R.id.observation_notes);
		TextView equipmentView = (TextView) newView
				.findViewById(R.id.observation_equipment_lb);
		TextView telescopeView = (TextView) newView
				.findViewById(R.id.observation_telescope);
		TextView eyepieceView = (TextView) newView
				.findViewById(R.id.observation_eyepiece);
		TextView barlowView = (TextView) newView
				.findViewById(R.id.observation_barlow);

		objectIdView.setText(object_id);
		
		String time_st = SimpleDateFormat.getTimeInstance(
				SimpleDateFormat.SHORT).format(date);
		timeView.setText(time_st);

		seeingView.setText(seeing + "/5.0");
		rateView.setText(rate + "/5.0");


		if (notes.length() != 0) {
			notesView.setText(notes);
			notesView.setVisibility(View.VISIBLE);
		} else
			notesView.setVisibility(View.GONE);

		if ((telescope.length() != 0) || (eyepiece.length() != 0)
				|| (barlow.length() != 0)) {
			equipmentView.setVisibility(View.VISIBLE);
		} else
			equipmentView.setVisibility(View.GONE);

		if (telescope.length() != 0) {
			telescopeView.setText(telescope);
			telescopeView.setVisibility(View.VISIBLE);
		} else
			telescopeView.setVisibility(View.GONE);

		if (eyepiece.length() != 0) {
			eyepieceView.setText(eyepiece);
			eyepieceView.setVisibility(View.VISIBLE);
		} else
			eyepieceView.setVisibility(View.GONE);

		if (barlow.length() != 0) {
			barlowView.setText(barlow);
			barlowView.setVisibility(View.VISIBLE);
		} else
			barlowView.setVisibility(View.GONE);

		return newView;
	}
}
