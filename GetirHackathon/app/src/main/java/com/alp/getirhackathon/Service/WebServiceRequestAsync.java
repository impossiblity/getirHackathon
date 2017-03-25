package com.alp.getirhackathon.Service;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.alp.getirhackathon.ToolBox.SharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by AlparslanSelçuk on 25.03.2017.
 */

public class WebServiceRequestAsync extends AsyncTask<Integer, Void, String> {
    private final String USER_AGENT = "Mozilla/5.0";
    private WebServiceResponseListener responseListener;
    private String msg = "Lütfen Bekleyiniz...";
    private ProgressDialog serviceProgress;
    private boolean showDialog = false;
    private Activity activity;
    Bundle bundle;
    private SharedPreference sharedPreference;

    public static final int CREATEGROUP = 0;
    public static final int SEARCHGROUP = 1;
    public static final int JOIN_GROUP = 2;
    public static final int LIST_GROUP = 3;

    public WebServiceRequestAsync(Activity activity, WebServiceResponseListener responseListener) {
        this.activity = activity;
        this.responseListener = responseListener;
    }

    public WebServiceRequestAsync(Activity activity, WebServiceResponseListener responseListener, SharedPreference sharedPreference) {
        this.activity = activity;
        this.sharedPreference = sharedPreference;
        this.responseListener = responseListener;
    }

    private String getQuery(List<KeyValuePair> params, boolean isGet) {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        JSONArray array = new JSONArray();
        JSONObject cred = new JSONObject();

        for (KeyValuePair pair : params) {

            try {
                if (!isGet) {
                    if (!pair.getKey().equals(BundleKeys.LOCATION))
                        cred.put(pair.getKey(), pair.getValue());
                    else {
                        array.put(sharedPreference.getStringValue(SharedPreference.LATITUDE));
                        array.put(sharedPreference.getStringValue(SharedPreference.LONGITUDE));
                        cred.put(pair.getKey(), array);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                if(isGet) {
                    if (first)
                        first = false;
                    else
                        result.append("&");

                    result.append(URLEncoder.encode(pair.getKey(), "UTF-8"));
                    result.append("=");
                    result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return cred.toString();
    }

    private String getGetResponse(String url) {
        URL obj = null;
        try {
            obj = new URL(url);

            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept", "*/*");
            con.addRequestProperty("Content-Type", "application/json");
            con.setInstanceFollowRedirects(false);

            int responseCode = con.getResponseCode();
            Log.i("Response Code : ", String.valueOf(responseCode));
            Log.i("Response Message : ", con.getResponseMessage());

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getPostResponse(List<KeyValuePair> paramsList, String url) {
        URL obj = null;
        try {
            obj = new URL(url);

            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            //add reuqest header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept", "*/*");
            con.addRequestProperty("Content-Type", "application/json");
            con.setInstanceFollowRedirects(false);
            con.setDoInput(true);
            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            String params = getQuery(paramsList, false);
            wr.writeBytes(params);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            Log.i("Response Code : ", String.valueOf(responseCode));
            Log.i("Response Message : ", con.getResponseMessage());

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected String doInBackground(Integer... integers) {
        List<KeyValuePair> paramsList = new ArrayList<>();
        switch (integers[0]) {
            case CREATEGROUP:
                paramsList.add(new KeyValuePair(BundleKeys.OWNER, bundle.getString(BundleKeys.OWNER)));
                paramsList.add(new KeyValuePair(BundleKeys.ENDTIME, bundle.getString(BundleKeys.ENDTIME)));
                paramsList.add(new KeyValuePair(BundleKeys.STARTTIME, bundle.getString(BundleKeys.STARTTIME)));
                paramsList.add(new KeyValuePair(BundleKeys.LOCATION, bundle.getString(BundleKeys.LOCATION)));
                paramsList.add(new KeyValuePair("people", ""));
                return getPostResponse(paramsList, ServiceModel.CREATE_GROUP);
            case SEARCHGROUP:
                paramsList.clear();
                paramsList.add(new KeyValuePair(BundleKeys.OWNER, bundle.getString(BundleKeys.OWNER)));
                paramsList.add(new KeyValuePair(BundleKeys.ENDTIME, bundle.getString(BundleKeys.ENDTIME)));
                paramsList.add(new KeyValuePair(BundleKeys.STARTTIME, bundle.getString(BundleKeys.STARTTIME)));
                paramsList.add(new KeyValuePair(BundleKeys.LOCATION, bundle.getString(BundleKeys.LOCATION)));
                paramsList.add(new KeyValuePair("people", ""));
                return getPostResponse(paramsList, ServiceModel.SEARCH_GROUP);
            case JOIN_GROUP:
                paramsList.clear();
                paramsList.add(new KeyValuePair(BundleKeys.GROUP_ID, bundle.getString(BundleKeys.GROUP_ID)));
                paramsList.add(new KeyValuePair(BundleKeys.PERSON, bundle.getString(BundleKeys.PERSON)));
                return getPostResponse(paramsList, ServiceModel.JOIN_GROUP);
            case LIST_GROUP:
                paramsList.clear();
                return getGetResponse(ServiceModel.GROUP_LIST + "/" + sharedPreference.getStringValue(SharedPreference.USERID));
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        if (showDialog) {
            try {
                serviceProgress = new ProgressDialog(activity);
                serviceProgress = ProgressDialog.show(activity, null, msg, true);
                serviceProgress.setCancelable(true);
                serviceProgress.setCanceledOnTouchOutside(false);
                serviceProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
            } catch (Exception e) {
                Log.e("Error Web Service call", e.getMessage());
            }
        }
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            if (serviceProgress != null && serviceProgress.isShowing()) {
                serviceProgress.dismiss();
            }
        } catch (final IllegalArgumentException e) {
            // Handle or log or ignore
        } catch (final Exception e) {
            // Handle or log or ignore
        } finally {
            this.serviceProgress = null;
        }

        if (responseListener != null)
            responseListener.onResponse(result);
    }

    public void setParams(Bundle bundle) {
        this.bundle = bundle;
    }

    public void showDialog(boolean showDialog) {
        this.showDialog = showDialog;
    }
}
