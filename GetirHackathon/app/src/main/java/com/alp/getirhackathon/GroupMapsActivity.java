package com.alp.getirhackathon;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.alp.getirhackathon.Service.SearchGroupResponseModel;
import com.alp.getirhackathon.ToolBox.CustomDateManager;
import com.alp.getirhackathon.ToolBox.SharedPreference;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class GroupMapsActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    SearchGroupResponseModel group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        if(getSupportActionBar() != null)
            getSupportActionBar().hide();
        setContentView(R.layout.activity_group_maps);
        TextView txtTimeInfo = (TextView) findViewById(R.id.txt_time_info);
        TextView txtJoinGroup = (TextView) findViewById(R.id.txt_join);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (getIntent().getExtras().getSerializable("chosenGroup") != null) {
            group = (SearchGroupResponseModel) getIntent().getExtras().getSerializable("chosenGroup");
            txtTimeInfo.setText("Başlangıç : " + CustomDateManager.getDateFromString(group.getStartTime())
                    + " Bitiş : " +  CustomDateManager.getDateFromString(group.getEndTime()) );
        }

        txtJoinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng ownerMarker = new LatLng(group.getLocation().get(0), group.getLocation().get(1));
        LatLng yourLoocation = new LatLng(Double.parseDouble(sharedPreference.getStringValue(SharedPreference.LATITUDE)),
                Double.parseDouble(sharedPreference.getStringValue(SharedPreference.LONGITUDE)));
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(ownerMarker);
        if (!ownerMarker.equals(yourLoocation)) {
            builder.include(yourLoocation);
            mMap.addMarker(new MarkerOptions().position(yourLoocation).title("Your Location"));
            mMap.addMarker(new MarkerOptions().position(ownerMarker).title("Group Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 14));
        } else {
            mMap.addMarker(new MarkerOptions().position(ownerMarker).title("Group Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ownerMarker, 13));
        }



    }
}
