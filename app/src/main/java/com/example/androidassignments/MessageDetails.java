package com.example.androidassignments;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;

public class MessageDetails extends AppCompatActivity{
    long itemID;
    String newMsg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_message_details);

        if(savedInstanceState == null){
            itemID = getIntent().getLongExtra("itemID", 0);
            newMsg = getIntent().getStringExtra("message");
        }

        MessageFragment newFragment = new MessageFragment();
        FragmentTransaction fragTran = getSupportFragmentManager().beginTransaction();
        Bundle args = new Bundle();
        args.putLong("itemID", itemID);
        args.putString("message", newMsg);
        newFragment.setArguments(args);
        fragTran.add(R.id.frame1, newFragment);
        fragTran.commit();
    }
}
