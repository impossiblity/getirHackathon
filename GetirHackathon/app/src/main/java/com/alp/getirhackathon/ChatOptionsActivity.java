package com.alp.getirhackathon;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.alp.getirhackathon.Service.BundleKeys;
import com.alp.getirhackathon.ToolBox.CustomDateManager;
import com.alp.getirhackathon.ToolBox.DatePickerFragment;
import com.alp.getirhackathon.ToolBox.TimePickerFragment;

public class ChatOptionsActivity extends AppCompatActivity {

    public Button btnStartTimePicker, btnFinishTimePicker;
    public Button btnStartDatePicker, btnFinishDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        if(getSupportActionBar() != null)
            getSupportActionBar().hide();
        setContentView(R.layout.activity_chat_options);

        btnStartTimePicker = (Button) findViewById(R.id.btn_start_time_picker);
        btnFinishTimePicker = (Button) findViewById(R.id.btn_finish_time_picker);
        btnStartTimePicker.setText(CustomDateManager.getTimeWellFormedString(0));
        btnFinishTimePicker.setText(CustomDateManager.getTimeWellFormedString(1));

        btnStartDatePicker = (Button) findViewById(R.id.btn_start_date_picker);
        btnFinishDatePicker = (Button) findViewById(R.id.btn_finish_date_picker);
        btnStartDatePicker.setText(CustomDateManager.getDateWellFormedString(0));
        btnFinishDatePicker.setText(CustomDateManager.getDateWellFormedString(1));
    }

    public void chatOptionsClickListener(View view) {
        switch (view.getId()) {
            case R.id.btn_finish_time_picker:
            case R.id.btn_start_time_picker:
                DialogFragment newTimeFragment = new TimePickerFragment((Button) view);
                newTimeFragment.show(getSupportFragmentManager(), "timePicker");
                break;
            case R.id.btn_finish_date_picker:
            case R.id.btn_start_date_picker:
                DialogFragment newDateFragment = new DatePickerFragment((Button) view);
                newDateFragment.show(getSupportFragmentManager(), "datePicker");
                break;

            case R.id.pnl_chat_options:
                Intent intent = new Intent(view.getContext(), ChatListActivity.class);
                intent.putExtra(BundleKeys.STARTTIME, btnStartDatePicker.getText().toString() + " " + btnStartTimePicker.getText().toString());
                intent.putExtra(BundleKeys.ENDTIME, btnFinishDatePicker.getText().toString() + " " + btnFinishTimePicker.getText().toString());
                intent.putExtra(BundleKeys.ISMOVEONCLICKED, false);
                startActivity(intent);
                break;

            case R.id.pnl_chat_options_move:
                intent = new Intent(view.getContext(), ChatListActivity.class);
                intent.putExtra(BundleKeys.STARTTIME, btnStartDatePicker.getText().toString() + " " + btnStartTimePicker.getText().toString());
                intent.putExtra(BundleKeys.ENDTIME, btnFinishDatePicker.getText().toString() + " " + btnFinishTimePicker.getText().toString());
                intent.putExtra(BundleKeys.ISMOVEONCLICKED, true);
                startActivity(intent);


        }
    }

}
