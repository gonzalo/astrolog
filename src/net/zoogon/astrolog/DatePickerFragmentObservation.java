package net.zoogon.astrolog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DatePickerFragmentObservation extends DialogFragment{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        
        int year = getArguments().getInt("year");
        int month = getArguments().getInt("month");
        int day = getArguments().getInt("day");
 
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), (EditObservationActivity)getActivity(), year, month, day);
    }

}
