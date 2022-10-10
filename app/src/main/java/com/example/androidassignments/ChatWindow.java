package com.example.androidassignments;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import java.util.ArrayList;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ChatWindow extends AppCompatActivity {
    Button buttonSend;
    EditText newMessage;
    ListView chats;
    ArrayList<String> msg;
    ChatAdapter messageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        msg = new ArrayList<>();
        buttonSend = findViewById(R.id.btnSend);
        newMessage = findViewById(R.id.editMessage);
        chats = findViewById(R.id.chatView);

        messageAdapter = new ChatAdapter(this);
        chats.setAdapter(messageAdapter);

        buttonSend.setOnClickListener(v -> {
            msg.add(newMessage.getText().toString());
            messageAdapter.notifyDataSetChanged();
            newMessage.setText("");
        });
    }

    private class ChatAdapter extends ArrayAdapter<String>{

        public ChatAdapter(@NonNull Context context) {
            super(context, 0);
        }
        public int getCount(){
            return msg.size();
        }
        public String getItem(int position){
            return msg.get(position);
        }
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result;
            if(position % 2 == 0){
                result = inflater.inflate(R.layout.chat_row_incoming,null);
            }
            else{
                result = inflater.inflate(R.layout.chat_row_outgoing,null);
            }

            TextView message = result.findViewById(R.id.message_text);

            message.setText(getItem(position));
            return result;

        }

    }
}