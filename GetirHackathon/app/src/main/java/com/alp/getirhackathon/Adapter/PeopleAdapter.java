package com.alp.getirhackathon.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alp.getirhackathon.R;
import com.alp.getirhackathon.Service.ResponseModels.SearchGroupResponseModel;

import java.util.ArrayList;

/**
 * Created by AlparslanSelçuk on 25.03.2017.
 */

public class PeopleAdapter extends BaseAdapter {
    ArrayList<SearchGroupResponseModel> list;
    private LayoutInflater layoutInflater;
    Activity activity;

    public PeopleAdapter(ArrayList<SearchGroupResponseModel> list, Activity activity) {
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
        SearchGroupResponseModel model = list.get(i);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.item_people, null);
            viewHolder.pnlMain = (RelativeLayout) convertView.findViewById(R.id.pnl_main);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.txt_item_title);
            viewHolder.txtTime = (TextView) convertView.findViewById(R.id.txt_item_time);
            viewHolder.txtDistance = (TextView) convertView.findViewById(R.id.txt_item_distance);
            viewHolder.txtFirstLetter = (TextView) convertView.findViewById(R.id.txt_item_first_letter);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtFirstLetter.setText(String.valueOf(model.getOwner().charAt(0) != 'A' ? model.getOwner().charAt(0) : 'A'));
        viewHolder.txtDistance.setText(String.valueOf(model.getDistance()).equals("0") ? String.valueOf(model.getDistance()) : activity.getResources().getString(R.string.very_close));
        viewHolder.txtName.setText(model.getOwner());
        viewHolder.txtTime.setText(model.getTimeDifference());


        return convertView;
    }

    private class ViewHolder {
        RelativeLayout pnlMain;
        TextView txtName;
        TextView txtTime;
        TextView txtDistance;
        TextView txtFirstLetter;
    }
}
