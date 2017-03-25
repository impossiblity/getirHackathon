package com.alp.getirhackathon;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.alp.getirhackathon.ToolBox.SharedPreference;
import com.alp.getirhackathon.ToolBox.Toast.CustomErrorToast;
import com.alp.getirhackathon.ToolBox.Toast.CustomSuccessToast;

/**
 * Created by AlparslanSelçuk on 24.03.2017.
 */

public class BaseActivity extends AppCompatActivity {

    private InputMethodManager imm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreference = SharedPreference.getInstance(this);
        imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);

    }

    public SharedPreference sharedPreference;
    public boolean isConnectingInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;

    }
    public void closeSoftKeyboard(View view) {
        try {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {

        }
    }

    public void showErrorToast(String text) {
        CustomErrorToast value = new CustomErrorToast(this);
        value.showToast(text);
    }

    public void showSuccessToast(String text) {
        CustomSuccessToast value = new CustomSuccessToast(this);
        value.showToast(text);
    }
}
