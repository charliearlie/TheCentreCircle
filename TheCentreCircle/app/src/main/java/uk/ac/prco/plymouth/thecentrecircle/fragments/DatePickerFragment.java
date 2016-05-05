package uk.ac.prco.plymouth.thecentrecircle.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import uk.ac.prco.plymouth.thecentrecircle.MainActivity;

/**
 * Created by charliewaite on 11/04/2016.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear = monthOfYear + 1;
        String dayString;
        String monthString;
        String titleDate = "";
        if (dayOfMonth < 10) {
            dayString = "0" + dayOfMonth;
        } else {
            dayString = String.valueOf(dayOfMonth);
        }

        if (monthOfYear < 10) {
            monthString = "0" + monthOfYear;
        } else {
            monthString = String.valueOf(monthOfYear);
        }

        String date = dayString + monthString + year;
        String dateTest = dayString + "/" + monthString + "/" + year;
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date date1 = format.parse(dateTest);
            DateFormat format2 = new SimpleDateFormat("EEEE dd MMMM yyyy");
            titleDate = format2.format(date1);
            Toast.makeText(getContext(), titleDate, Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }




        FixturesByDateFragment fbdf = new FixturesByDateFragment();
        Bundle bundle = new Bundle();
        bundle.putString("date", date);
        fbdf.setArguments(bundle);
        ((MainActivity) getActivity()).setActionBarTitle(titleDate);
        ((MainActivity) getActivity()).openFixtureFragment(fbdf);
        //Toast.makeText(getContext(), dayOfMonth + "/" + monthOfYear + "/" + year, Toast.LENGTH_LONG)
                //.show();
    }
}
