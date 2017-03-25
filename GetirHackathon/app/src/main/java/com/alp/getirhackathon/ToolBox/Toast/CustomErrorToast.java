package com.alp.getirhackathon.ToolBox.Toast;

import android.app.Activity;
import android.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.alp.getirhackathon.R;

/**
 * Created by AlparslanSelçuk on 19.11.2016.
 */

public class CustomErrorToast extends Fragment
{
    LayoutInflater inflater;
    Activity activity;
    View view;
    TextView title;
    Toast toast;

    public CustomErrorToast(){ }

    public CustomErrorToast(Activity activity){
        this.activity = activity;
        inflater = activity.getLayoutInflater();
        view = inflater.inflate(R.layout.warning_toast, null);
        title = (TextView) view.findViewById(R.id.txt_custom_warning_toast_message);
        toast = new Toast(activity);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 250);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
    }
    public void showToast(String text){
        title.setText(text);
        toast.show();
    }
}