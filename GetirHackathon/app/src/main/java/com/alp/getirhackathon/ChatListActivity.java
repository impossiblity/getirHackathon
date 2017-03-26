package com.alp.getirhackathon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alp.getirhackathon.Adapter.PeopleAdapter;
import com.alp.getirhackathon.Service.BundleKeys;
import com.alp.getirhackathon.Service.ResponseModels.MyGroupListResult;
import com.alp.getirhackathon.Service.ResponseModels.SearchGroupResponseModel;
import com.alp.getirhackathon.Service.WebServiceRequestAsync;
import com.alp.getirhackathon.Service.WebServiceResponseListener;
import com.alp.getirhackathon.ToolBox.SharedPreference;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class ChatListActivity extends BaseActivity {

    private String startTime, finishTime;
    private int isMoveOnClicked;
    private RelativeLayout pnlErrorScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        if(getSupportActionBar() != null)
            getSupportActionBar().hide();
        setContentView(R.layout.activity_chat_list);
        TextView txtTitle = (TextView) findViewById(R.id.chat_list_title);
        pnlErrorScreen = (RelativeLayout) findViewById(R.id.pnl_error_screen);

        isMoveOnClicked = getIntent().getExtras().getInt(BundleKeys.ISMOVEONCLICKED);

        if (getIntent().getExtras().getString(BundleKeys.STARTTIME) != null) {
            startTime = getIntent().getExtras().getString(BundleKeys.STARTTIME);
            finishTime = getIntent().getExtras().getString(BundleKeys.ENDTIME);
            startTime = startTime.replace(" / ","/");
            finishTime = finishTime.replace(" / ", "/");
        }
        if (isConnectingInternet()) {
            if (isMoveOnClicked == 0) {
                getChatListResponse();
            } else if (isMoveOnClicked == 1) {
                getSearchResponse();
            } else if (isMoveOnClicked == 2) {
                getMyGroupsList();
                txtTitle.setText(R.string.your_groups);
            } else if (isMoveOnClicked == 3){
                getMyGroupsList();
                txtTitle.setText(R.string.partipiated_groups);
            }
        } else {
            showErrorToast(getString(R.string.no_connection));
        }
    }

    private void getMyGroupsList() {
        WebServiceRequestAsync request = new WebServiceRequestAsync(this, getMyGroupsListener, sharedPreference);
        request.showDialog(true);
        request.execute(WebServiceRequestAsync.LIST_GROUP);
    }
    private WebServiceResponseListener getMyGroupsListener = new WebServiceResponseListener() {
        @Override
        public void onResponse(String jsonString) {
            if (jsonString != null) {
                try {
                    Gson gson = new Gson();
                    MyGroupListResult groupListResult = gson.fromJson(jsonString, MyGroupListResult.class);
                    if(groupListResult != null) {
                        SearchGroupResponseModel[] model = new SearchGroupResponseModel[0];
                        if (isMoveOnClicked == 2) {
                            model = new SearchGroupResponseModel[groupListResult.getOwns().size()];
                            for (int i = 0; i < groupListResult.getOwns().size(); i++) {
                                SearchGroupResponseModel item = groupListResult.getOwns().get(i);
                                model[i] = item;
                            }
                        } else if(isMoveOnClicked == 3) {
                            model = new SearchGroupResponseModel[groupListResult.getParticipates().size()];
                            for (int i = 0; i < groupListResult.getParticipates().size(); i++) {
                                SearchGroupResponseModel item = groupListResult.getParticipates().get(i);
                                model[i] = item;
                            }
                        }

                        setGroupsView(model);
                    } else
                        setErrorScreen();
                } catch (Exception e) {
                    setErrorScreen();
                }
            } else {
                setErrorScreen();
                showErrorToast(getString(R.string.custom_error));
            }
        }
    };

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
            } else
                setErrorScreen();
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
                try {
                    Gson gson = new Gson();
                    SearchGroupResponseModel[] responseModel = gson.fromJson(jsonString, SearchGroupResponseModel[].class);
                    if (responseModel != null) {
                        setGroupsView(responseModel);
                    } else
                        setErrorScreen();
                } catch (Exception e) {
                    setErrorScreen();
                }
            } else {
                setErrorScreen();
            }
        }
    };

    private void setGroupsView(SearchGroupResponseModel[] responseModel) {
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

    private void setErrorScreen() {
        showErrorToast(getString(R.string.custom_error));
        pnlErrorScreen.setVisibility(View.VISIBLE);
    }
}