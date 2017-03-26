package com.alp.getirhackathon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.alp.getirhackathon.Service.BundleKeys;
import com.alp.getirhackathon.ToolBox.CustomDateManager;
import com.alp.getirhackathon.ToolBox.DatePickerFragment;
import com.alp.getirhackathon.ToolBox.SharedPreference;
import com.alp.getirhackathon.ToolBox.TimePickerFragment;

public class EventGroupsActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public Button btnStartTimePicker, btnFinishTimePicker;
    public Button btnStartDatePicker, btnFinishDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_groups);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        btnStartTimePicker = (Button) findViewById(R.id.btn_start_time_picker);
        btnFinishTimePicker = (Button) findViewById(R.id.btn_finish_time_picker);
        btnStartTimePicker.setText(CustomDateManager.getTimeWellFormedString(0));
        btnFinishTimePicker.setText(CustomDateManager.getTimeWellFormedString(1));

        btnStartDatePicker = (Button) findViewById(R.id.btn_start_date_picker);
        btnFinishDatePicker = (Button) findViewById(R.id.btn_finish_date_picker);
        btnStartDatePicker.setText(CustomDateManager.getDateWellFormedString(0));
        btnFinishDatePicker.setText(CustomDateManager.getDateWellFormedString(1));

        TextView txtHeader = (TextView) header.findViewById(R.id.txt_header_side_menu);
        txtHeader.setText(sharedPreference.getStringValue(SharedPreference.EMAIL));
        TextView txtUserName = (TextView) header.findViewById(R.id.txt_user_name);
        txtUserName.setText(sharedPreference.getStringValue(SharedPreference.USERID));

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
                startActivity(createIntent(0));
                break;

            case R.id.pnl_chat_options_move:
                startActivity(createIntent(1));
                break;
        }
    }

    public Intent createIntent (int isMoveOnClicked) {
        Intent intent = new Intent(EventGroupsActivity.this, ChatListActivity.class);
        intent.putExtra(BundleKeys.STARTTIME, btnStartDatePicker.getText().toString() + " " + btnStartTimePicker.getText().toString());
        intent.putExtra(BundleKeys.ENDTIME, btnFinishDatePicker.getText().toString() + " " + btnFinishTimePicker.getText().toString());
        intent.putExtra(BundleKeys.ISMOVEONCLICKED, isMoveOnClicked);
        return intent;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_groups, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            startActivity(createIntent(1));
        } else if (id == R.id.nav_gallery) {
            startActivity(createIntent(2));
        } else if (id == R.id.nav_slideshow) {
            startActivity(createIntent(3));
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            sharedPreference.setStringValue(SharedPreference.USERID, "");
            sharedPreference.setStringValue(SharedPreference.EMAIL, "");
            sharedPreference.setStringValue(SharedPreference.PASSWORD, "");
            startActivity(new Intent(EventGroupsActivity.this, LoginActivity.class ));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
