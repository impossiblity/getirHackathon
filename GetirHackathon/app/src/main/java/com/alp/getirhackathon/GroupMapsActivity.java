package com.alp.getirhackathon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.alp.getirhackathon.Service.BundleKeys;
import com.alp.getirhackathon.Service.ResponseModels.SearchGroupResponseModel;
import com.alp.getirhackathon.Service.WebServiceRequestAsync;
import com.alp.getirhackathon.Service.WebServiceResponseListener;
import com.alp.getirhackathon.ToolBox.CustomDateManager;
import com.alp.getirhackathon.ToolBox.SharedPreference;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

public class GroupMapsActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    SearchGroupResponseModel group;
    TextView txtJoinGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        if(getSupportActionBar() != null)
            getSupportActionBar().hide();
        setContentView(R.layout.activity_group_maps);
        TextView txtTimeInfo = (TextView) findViewById(R.id.txt_time_info);
        txtJoinGroup = (TextView) findViewById(R.id.txt_join);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (getIntent().getExtras().getSerializable("chosenGroup") != null) {
            group = (SearchGroupResponseModel) getIntent().getExtras().getSerializable("chosenGroup");
            txtTimeInfo.setText(getResources().getString(R.string.starting) + " " + CustomDateManager.getDateFromString(group.getStartTime())
                    + " " + getResources().getString(R.string.ending) + " " +  CustomDateManager.getDateFromString(group.getEndTime()) );
        }

        getGroupDetailsResponse();

        setConversationTextView();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, EventGroupsActivity.class ));
    }

    public void setConversationTextView() {
        if (group.getPeople().contains(sharedPreference.getStringValue(SharedPreference.USERID))
                || group.getOwner().equals(sharedPreference.getStringValue(SharedPreference.USERID))) {
            txtJoinGroup.setText(R.string.conversation);
            txtJoinGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(createIntentToMessages());
                }
            });
        } else {
            txtJoinGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isConnectingInternet()) {
                        getChatListResponse();
                        startActivity(createIntentToMessages());
                    } else
                        showErrorToast(getString(R.string.no_connection));
                }
            });
        }
    }

    private void getGroupDetailsResponse() {
        WebServiceRequestAsync request = new WebServiceRequestAsync(this, groupDetailsResponse);
        Bundle bundle = new Bundle();
        bundle.putString(BundleKeys.GROUP_ID, group.getId());
        request.setParams(bundle);
        request.showDialog(true);
        request.execute(WebServiceRequestAsync.DETAIL_GROUPS);
    }
    private WebServiceResponseListener groupDetailsResponse = new WebServiceResponseListener() {
        @Override
        public void onResponse(String jsonString) {
            if (jsonString != null) {
                Gson gson = new Gson();
                SearchGroupResponseModel model = gson.fromJson(jsonString, SearchGroupResponseModel.class);
                group = model;
                setConversationTextView();
            } else {
                showErrorToast(getString(R.string.custom_error));
            }
        }
    };

    private Intent createIntentToMessages() {
        Intent intent = new Intent(GroupMapsActivity.this, MessagesActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("chosenGroup", group);
        intent.putExtras(bundle);
        return intent;
    }

    private void getChatListResponse() {
        WebServiceRequestAsync request = new WebServiceRequestAsync(this, getirResponseListener, sharedPreference);
        Bundle bundle = new Bundle();
        bundle.putString(BundleKeys.GROUP_ID, group.getId());
        bundle.putString(BundleKeys.PERSON, sharedPreference.getStringValue(SharedPreference.USERID));
        request.setParams(bundle);
        request.showDialog(true);
        request.execute(WebServiceRequestAsync.JOIN_GROUP);
    }
    private WebServiceResponseListener getirResponseListener = new WebServiceResponseListener() {
        @Override
        public void onResponse(String jsonString) {
            if (jsonString != null) {
                showSuccessToast(getString(R.string.joined_succesfully));
            } else {
                showErrorToast(getString(R.string.custom_error));
            }
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng ownerMarker = new LatLng(group.getLocation().get(0), group.getLocation().get(1));
        LatLng yourLocation = new LatLng(Double.parseDouble(sharedPreference.getStringValue(SharedPreference.LATITUDE)),
                Double.parseDouble(sharedPreference.getStringValue(SharedPreference.LONGITUDE)));
        if (!ownerMarker.equals(yourLocation) ) {
            try {
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(ownerMarker);
                builder.include(yourLocation);
                mMap.addMarker(new MarkerOptions().position(yourLocation).title(getString(R.string.your_location)));
                mMap.addMarker(new MarkerOptions().position(ownerMarker).title(getString(R.string.group_location)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 14));
            } catch (Exception e) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(ownerMarker).title(getString(R.string.group_location)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ownerMarker, 13));
            }
        } else {
            mMap.addMarker(new MarkerOptions().position(ownerMarker).title(getString(R.string.group_location)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ownerMarker, 13));
        }
    }
}
