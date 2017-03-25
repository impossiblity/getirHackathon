package com.alp.getirhackathon;

import android.os.Bundle;

import com.alp.getirhackathon.Service.BundleKeys;
import com.alp.getirhackathon.Service.SearchGroupResponseModel;
import com.alp.getirhackathon.Service.WebServiceRequestAsync;
import com.alp.getirhackathon.Service.WebServiceResponseListener;
import com.alp.getirhackathon.ToolBox.SharedPreference;
import com.google.gson.Gson;

public class ChatListActivity extends BaseActivity {

    private String startTime, finishTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        if (getIntent().getExtras().getString(BundleKeys.STARTTIME) != null) {
            startTime = getIntent().getExtras().getString(BundleKeys.STARTTIME);
            finishTime = getIntent().getExtras().getString(BundleKeys.ENDTIME);
            startTime = startTime.replace(" / ","/");
            finishTime = finishTime.replace(" / ", "/");
        }

        getChatListResponse();
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
                getSearchResponse();
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
                SearchGroupResponseModel responseModel = gson.fromJson(jsonString, SearchGroupResponseModel.class);
                if(responseModel != null) {
                    responseModel.getPeople();
                }
            }
        }
    };
}