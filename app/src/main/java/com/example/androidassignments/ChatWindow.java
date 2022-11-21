package com.example.androidassignments;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.sql.SQLException;
import static com.example.androidassignments.ListItemsActivity.ACTIVITY_NAME;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.AdapterView;
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
    public static boolean frameLayoutExists;

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

        if(findViewById(R.id.contentView) != null) {
            frameLayoutExists = true;
        } else {
            frameLayoutExists = false;
        }
        //Event listener for the elements in the list view
        ListView lv = findViewById(R.id.chatView);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3)
            {
                String message = messageAdapter.getItem(position);
                long itemID = messageAdapter.getItemId(position);

                if(frameLayoutExists) {
                    MessageFragment mFragment = new MessageFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    Bundle args = new Bundle();
                    args.putLong("itemID", itemID);
                    args.putString("message", message);
                    mFragment.setArguments(args);
                    ft.replace(R.id.contentView, mFragment);
                    ft.commit();
                } else {
                    //Phone mode
                    Intent intent = new Intent(getApplication(), MessageDetails.class);
                    intent.putExtra("itemID", itemID);
                    intent.putExtra("message", message);
                    startActivityForResult(intent, 10);
                }
            }
        });
    }

    @SuppressLint("Range")
    public void updateMessageList() {
        msg = new ArrayList<String>();
        //In this case, “this” is the ChatWindow, which is-A Context object
        messageAdapter =new ChatAdapter( this );
        chats.setAdapter(messageAdapter);
        //Open SQLiteDatabase using ChatDatabaseHelper
        databaseHelper = new ChatDatabaseHelper(this);
        Database = databaseHelper.getWritableDatabase();
        //Add all database messages to the messages ArrayList
        String sSQL = "SELECT KEY_MESSAGE FROM messages;";
        Cursor cursor = Database.rawQuery(sSQL, null);
        Log.i(ACTIVITY_NAME, "Cursor’s column count = " + cursor.getColumnCount());
        Log.i(ACTIVITY_NAME, "Column name = " + cursor.getColumnName(cursor.getColumnIndex(databaseHelper.KEY_ID)));
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + cursor.getString(cursor.getColumnIndex(databaseHelper.KEY_ID)));
            msg.add(cursor.getString(cursor.getColumnIndex(databaseHelper.KEY_ID)));
            cursor.moveToNext();
        }
        cursor.close();
    }

    //Gets the text in the EditText field, and adds it to your array list
    public void sendMessage(View view) {
        //Add text from EditText to database
        ContentValues values = new ContentValues();
        values.put(databaseHelper.KEY_ID, newMessage.getText().toString());
        Database.insert(databaseHelper.TABLE_NAME, null, values);
        //Add text from EditText to messages
        msg.add(newMessage.getText().toString());
        messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount()/getView()
        newMessage.setText("");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            long returnValue = data.getLongExtra("itemID",0);
            deleteMessage(returnValue);
        }
    }

    public void deleteMessage(long itemID) {
        //Delete returned message from database
        Database.delete("messages", "KEY_ID = '" + itemID + "'", null);
        //Update the list message list
        updateMessageList();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Database.close();
    }

    public long getItemId(int position) {
        String sSQL = "SELECT * FROM messages;";
        Cursor cursor = Database.rawQuery(sSQL, null);
        cursor.moveToPosition(position);
        @SuppressLint("Range") long itemID = cursor.getLong(cursor.getColumnIndex(databaseHelper.KEY_ID));
        cursor.close();
        return itemID;
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