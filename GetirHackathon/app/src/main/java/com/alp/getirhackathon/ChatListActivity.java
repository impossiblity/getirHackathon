package com.alp.getirhackathon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.alp.getirhackathon.Adapter.PeopleAdapter;
import com.alp.getirhackathon.Service.BundleKeys;
import com.alp.getirhackathon.Service.SearchGroupResponseModel;
import com.alp.getirhackathon.Service.WebServiceRequestAsync;
import com.alp.getirhackathon.Service.WebServiceResponseListener;
import com.alp.getirhackathon.ToolBox.SharedPreference;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class ChatListActivity extends BaseActivity {

    private String startTime, finishTime;
    private boolean isMoveOnClicked;
    private RelativeLayout pnlErrorScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        if(getSupportActionBar() != null)
            getSupportActionBar().hide();
        setContentView(R.layout.activity_chat_list);

        pnlErrorScreen = (RelativeLayout) findViewById(R.id.pnl_error_screen);

        if (getIntent().getExtras().getString(BundleKeys.STARTTIME) != null) {
            startTime = getIntent().getExtras().getString(BundleKeys.STARTTIME);
            finishTime = getIntent().getExtras().getString(BundleKeys.ENDTIME);
            isMoveOnClicked = getIntent().getExtras().getBoolean(BundleKeys.ISMOVEONCLICKED);
            startTime = startTime.replace(" / ","/");
            finishTime = finishTime.replace(" / ", "/");
        }
        if (isConnectingInternet()) {
            if (!isMoveOnClicked)
                getChatListResponse();
            else
                getSearchResponse();
        } else {
            showErrorToast(getString(R.string.no_connection));
        }
    }

    private void getChatListResponse() {
        WebServiceRequestAsync request = new WebServiceRequestAsync(this, getirResponseListener, sharedPreference);
        Bundle bundle = new Bundle();
        bundle.putString(BundleKeys.LOCATION, "[\"" + sharedPreference.getStringValue(SharedPreference.LATITUDE) + "\", \"" +
        sharedPreference.getStringValue(SharedPreference.LONGITUDE) + "\"]");
        bundle.putString(BundleKeys.OWNER, sharedPreference.getStringValue(SharedPreference.USERID));
        bundle.putString(BundleKeys.STARTTIME, startTime);
        bundle.putString(BundleKeys.ENDTIME, finishTime);
        request.setParams(bundle);
        request.showDialog(true);
        request.execute(WebServiceRequestAsync.CREATEGROUP);
    }
    private WebServiceResponseListener getirResponseListener = new WebServiceResponseListener() {
        @Override
        public void onResponse(String jsonString) {
            if (jsonString != null) {
                if (isConnectingInternet())
                    getSearchResponse();
                else
                    showErrorToast(getString(R.string.no_connection));
            } else {
                showErrorToast(getString(R.string.custom_error));
                pnlErrorScreen.setVisibility(View.VISIBLE);
            }
        }
    };

    private void getSearchResponse() {
        WebServiceRequestAsync request = new WebServiceRequestAsync(this, searchGroupResponseListener, sharedPreference);
        Bundle bundle = new Bundle();
        bundle.putString(BundleKeys.LOCATION, "[\"" + sharedPreference.getStringValue(SharedPreference.LATITUDE) + "\", \"" +
                sharedPreference.getStringValue(SharedPreference.LONGITUDE) + "\"]");
        bundle.putString(BundleKeys.OWNER, sharedPreference.getStringValue(SharedPreference.USERID));
        bundle.putString(BundleKeys.STARTTIME, startTime);
        bundle.putString(BundleKeys.ENDTIME, finishTime);
        request.setParams(bundle);
        request.showDialog(true);
        request.execute(WebServiceRequestAsync.SEARCHGROUP);
    }
    private WebServiceResponseListener searchGroupResponseListener = new WebServiceResponseListener() {
        @Override
        public void onResponse(String jsonString) {
            if (jsonString != null) {
                Gson gson = new Gson();
                    SearchGroupResponseModel[] responseModel = gson.fromJson(jsonString, SearchGroupResponseModel[].class);
                    if (responseModel != null) {

                        final ArrayList<SearchGroupResponseModel> list = new ArrayList<>(Arrays.asList(responseModel));

                        PeopleAdapter adapter = new PeopleAdapter(list, ChatListActivity.this);
                        ListView listPeople = (ListView) findViewById(R.id.list_people);
                        listPeople.setAdapter(adapter);

                        listPeople.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent = new Intent(view.getContext(), GroupMapsActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("chosenGroup", (Serializable) list.get(i));
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                    }
            } else {
                showErrorToast(getString(R.string.custom_error));
                pnlErrorScreen.setVisibility(View.VISIBLE);
            }
        }
    };
}