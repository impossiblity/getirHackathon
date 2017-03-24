package com.alp.getirhackathon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.alp.getirhackathon.Service.BundleKeys;
import com.alp.getirhackathon.Service.WebServiceRequest;
import com.alp.getirhackathon.Service.WebServiceResponseListener;
import com.google.gson.Gson;

public class GetirMainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getir_main);
        if(isConnectingInternet()) {

        }
    }

    private void getGetirElementsWebService() {
        WebServiceRequest request = new WebServiceRequest(this, getirResponseListener);
        Bundle bundle = new Bundle();
        bundle.putString(BundleKeys.PARTICIPANT_EMAIL, "alparslan3806@gmail.com");
        bundle.putString(BundleKeys.PARTICIPANT_NAME, "Alpaslan Selçuk DEVELİOĞLU");
        bundle.putString(BundleKeys.PARTICIPANT_GSM, "+90 537 347 3201");
        request.setParams(bundle);
        request.showDialog(true);
        request.execute(WebServiceRequest.GET_ELEMENTS);
    }

    private WebServiceResponseListener getirResponseListener = new WebServiceResponseListener() {
        @Override
        public void onResponse(String jsonString) {
            if (jsonString != null) {
                Gson gson = new Gson();
                // REsult burada alınır
            }
        }
    };

}
