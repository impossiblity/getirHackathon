package com.alp.getirhackathon.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alp.getirhackathon.R;
import com.alp.getirhackathon.Service.ResponseModels.Message;
import com.alp.getirhackathon.Service.ResponseModels.SearchGroupResponseModel;

import java.util.ArrayList;

/**
 * Created by AlparslanSelçuk on 25.03.2017.
 */

public class MessagesAdapter extends BaseAdapter {
    ArrayList<Message> list;
    private LayoutInflater layoutInflater;
    Activity activity;

    public MessagesAdapter(ArrayList<Message> list, Activity activity) {
        layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Message model = list.get(i);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.item_message, null);
            viewHolder.txtMessage = (TextView) convertView.findViewById(R.id.txt_item_user);
            viewHolder.txtUser = (TextView) convertView.findViewById(R.id.txt_item_message);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtMessage.setText(model.getMessage());
        viewHolder.txtUser.setText(model.getUser());

        return convertView;
    }

    private class ViewHolder {
        TextView txtUser;
        TextView txtMessage;
    }
}
