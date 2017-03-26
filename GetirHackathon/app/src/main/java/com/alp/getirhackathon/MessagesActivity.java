package com.alp.getirhackathon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import com.alp.getirhackathon.Adapter.MessagesAdapter;
import com.alp.getirhackathon.Service.BundleKeys;
import com.alp.getirhackathon.Service.ResponseModels.Message;
import com.alp.getirhackathon.Service.ResponseModels.MessagePostResponse;
import com.alp.getirhackathon.Service.ResponseModels.SearchGroupResponseModel;
import com.alp.getirhackathon.Service.WebServiceRequestAsync;
import com.alp.getirhackathon.Service.WebServiceResponseListener;
import com.alp.getirhackathon.ToolBox.SharedPreference;
import com.google.gson.Gson;
import java.util.ArrayList;

public class MessagesActivity extends BaseActivity {

    private SearchGroupResponseModel group;
    private EditText edtNewMessage;
    MessagesAdapter adapter;
    ArrayList<Message> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        final ImageButton pnlAnswer = (ImageButton) findViewById(R.id.img_answer);

        if (getIntent().getExtras().getSerializable("chosenGroup") != null) {
            group = (SearchGroupResponseModel) getIntent().getExtras().getSerializable("chosenGroup");
            messageList = new ArrayList<>(group.getMessages());
        }

        ListView messagesList = (ListView) findViewById(R.id.listview_messages_list);
        adapter = new MessagesAdapter(messageList, MessagesActivity.this);
        messagesList.setAdapter(adapter);
        edtNewMessage = (EditText) findViewById(R.id.edt_new_message);

        pnlAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeSoftKeyboard(pnlAnswer);
                getChatListResponse();
            }
        });
    }

    @Override
    public void onBackPressed() {
        getGroupDetailsResponse();
        Intent intent = new Intent(this, GroupMapsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("chosenGroup", group);
        intent.putExtras(bundle);
        startActivity(intent);
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

            } else {
                showErrorToast(getString(R.string.custom_error));
            }
        }
    };

    private void getChatListResponse() {
        WebServiceRequestAsync request = new WebServiceRequestAsync(this, getirResponseListener, sharedPreference);
        Bundle bundle = new Bundle();
        bundle.putString(BundleKeys.GROUP_ID, group.getId());
        bundle.putString(BundleKeys.MESSAGE, edtNewMessage.getText().toString());
        bundle.putString(BundleKeys.USER, sharedPreference.getStringValue(SharedPreference.USERID));
        request.setParams(bundle);
        request.showDialog(true);
        request.execute(WebServiceRequestAsync.MESSAGE_GROUPS);
    }
    private WebServiceResponseListener getirResponseListener = new WebServiceResponseListener() {
        @Override
        public void onResponse(String jsonString) {
            if (jsonString != null) {
                Gson gson = new Gson();
                MessagePostResponse response = gson.fromJson(jsonString, MessagePostResponse.class);
                if (response != null) {
                    messageList.clear();
                    messageList.addAll(response.getMessages());
                    adapter.notifyDataSetChanged();
                }
            } else
                showErrorToast(getString(R.string.no_connection));
        }
    };
}
