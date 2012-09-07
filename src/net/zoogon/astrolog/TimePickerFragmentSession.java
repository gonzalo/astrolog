package net.zoogon.astrolog;

import java.util.Calendar;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class TimePickerFragmentSession extends DialogFragment{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        
        int hourOfDay = getArguments().getInt("hourOfDay");
        int minute = getArguments().getInt("minute");
        
 
        // Create a new instance of DatePickerDialog and return it
        return new TimePickerDialog(getActivity(), (EditSessionActivity)getActivity(), hourOfDay, minute, true);
    }

}
