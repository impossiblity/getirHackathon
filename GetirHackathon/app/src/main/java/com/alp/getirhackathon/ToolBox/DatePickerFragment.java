package com.alp.getirhackathon.ToolBox;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by AlparslanSelçuk on 25.03.2017.
 */

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    Button btnTimePicker;

    public DatePickerFragment(Button btnTimePicker) {
        this.btnTimePicker = btnTimePicker;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        Date d = c.getTime();
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
        DatePicker datePicker = dialog.getDatePicker();
        datePicker.setMinDate(d.getTime());
        return dialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        btnTimePicker.setText(String.valueOf(year) + " / " + String.valueOf(month + 1) + " / " + String.valueOf(day));
    }
}