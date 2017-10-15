package com.hirantha.aselachat;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ChatDisplayFragment extends Fragment {

    public static ChatDisplayFragment context;
    ListView messageList;
    EditText txtMessage;
    Button btnSend;
    String userName;
    ListViewAdapter adapter;
    ObjectOutputStream sOutput;
    SimpleDateFormat sdf;
    ArrayList<ChatMessage> messages = new ArrayList<>();

    public ChatDisplayFragment() {
        // Required empty public constructor
    }

    public void setRequrements(String userName,ObjectOutputStream sOutput) {
        this.userName = userName;
        this.sOutput = sOutput;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat_display, container, false);
        context = this;
        sdf = new SimpleDateFormat("HH:mm:ss");

        // Inflate the layout for this fragment
        adapter = new ListViewAdapter();
        messageList = (ListView) rootView.findViewById(R.id.messageList);
        txtMessage = (EditText) rootView.findViewById(R.id.txtMessage);
        btnSend = (Button) rootView.findViewById(R.id.btnSend);
        messageList.setAdapter(adapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sending message
                //message eka server ekata gihin apahu awoth display wenna dala thiyenne
                //server eken set wela inna hamotama broadcast karanawa
                try {
                    sOutput.writeObject(new ChatMessage(ChatMessage.MESSAGE,userName,txtMessage.getText().toString(),sdf.format(new Date())));
                    txtMessage.setText("");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        return rootView;
    }

    public void addMessage(ChatMessage message) {
        messages.add(message);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.chat_menu,menu);
        //defult menu ekata mage menu eka inflate kara
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case R.id.menuWhoIsIn:
                    sOutput.writeObject(new ChatMessage(ChatMessage.WHOISIN, "", "", ""));
                    return true;
                case R.id.menuLogout:
                    sOutput.writeObject(new ChatMessage(ChatMessage.LOGOUT, "", "", ""));
                    LoginFragment loginFragment = new LoginFragment();
                    MainActivity.context.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, loginFragment).commit();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }catch (IOException e){

        }
        return super.onOptionsItemSelected(item);
    }

    public class ListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return messages.size();
        }

        @Override
        public ChatMessage getItem(int i) {
            return messages.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View rowView, ViewGroup viewGroup) {
            ChatMessage message = messages.get(i);
                if (message.getUserName().equals(userName)) {
                    //design later
                    rowView = LayoutInflater.from(context.getContext()).inflate(R.layout.send_chat_row_layout, viewGroup, false);
                    TextView lblMessage = (TextView) rowView.findViewById(R.id.lblMessage);
                    TextView lblTime = (TextView) rowView.findViewById(R.id.lblTime);
                    lblMessage.setText("Me:\n" + message.getMessage());
                    lblTime.setText(message.getTime());
                } else {
                    rowView = LayoutInflater.from(context.getContext()).inflate(R.layout.recive_chat_row_layout, viewGroup, false);
                    TextView lblMessage = (TextView) rowView.findViewById(R.id.lblMessage);
                    TextView lblTime = (TextView) rowView.findViewById(R.id.lblTime);
                    lblMessage.setText(message.getUserName()+ ":\n" + message.getMessage());
                    lblTime.setText(message.getTime());
                }
            return rowView;
        }
    }
}
