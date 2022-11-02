package com.example.androidassignments;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.sql.SQLException;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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
    private SQLiteDatabase Database;
    private ChatDatabaseHelper databaseHelper;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        msg = new ArrayList<>();
        buttonSend = findViewById(R.id.btnSend);
        newMessage = findViewById(R.id.editMessage);
        chats = findViewById(R.id.chatView);

        databaseHelper = new ChatDatabaseHelper(this);
        Database = databaseHelper.getWritableDatabase();
        String[] getColumns = {ChatDatabaseHelper.KEY_MESSAGE};
        Cursor newCursor = Database.query(ChatDatabaseHelper.TABLE_NAME, getColumns, null, null, null, null, null);
        newCursor.moveToFirst();

        while(!newCursor.isAfterLast()){
            msg.add(newCursor.getString(newCursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            Log.i("ChatWindow.java","SQL MESSAGE: " + newCursor.getString(newCursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            newCursor.moveToNext();
        }
        Log.i("ChatWindow.java", "Cursor's column count =" + newCursor.getColumnCount());

        for(int i = 0; i < newCursor.getColumnCount(); i++){
            Log.i("ChatWindow.java", newCursor.getColumnName(i));
        }
        newCursor.close();

        messageAdapter = new ChatAdapter(this);
        chats.setAdapter(messageAdapter);

        buttonSend.setOnClickListener(v -> {
            msg.add(newMessage.getText().toString());
            ContentValues newValue = new ContentValues();
            newValue.put(ChatDatabaseHelper.KEY_MESSAGE, newMessage.getText().toString());
            Database.insert(ChatDatabaseHelper.TABLE_NAME, null, newValue);
            messageAdapter.notifyDataSetChanged();
            newMessage.setText("");
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Database.close();
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