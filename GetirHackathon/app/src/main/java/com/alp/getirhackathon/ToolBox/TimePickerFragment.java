package com.alp.getirhackathon.ToolBox;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by AlparslanSelçuk on 25.03.2017.
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    Button btnTimePicker;

    public TimePickerFragment() { }

    public TimePickerFragment(Button btnTimePicker) {
        this.btnTimePicker = btnTimePicker;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        btnTimePicker.setText(String.valueOf(hour) + " : " + String.valueOf(minute));
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        btnTimePicker.setText(String.valueOf(hourOfDay) + " : " + String.valueOf(minute));
    }
}
