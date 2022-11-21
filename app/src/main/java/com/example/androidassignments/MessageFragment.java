package com.example.androidassignments;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import android.os.Bundle;

public class MessageFragment extends Fragment {
    long itemID;
    String newMessage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null){
            if(getArguments() != null){
                itemID = getArguments().getLong("itemID", 0);
                newMessage = getArguments().getString("message", "");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup newParent, @Nullable Bundle savedInstance){
        View newView = inflater.inflate(R.layout.fragment_message, newParent, false);
        Button newButton = (Button) newView.findViewById(R.id.btnDelete);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ChatWindow.frameLayoutExists) {
                    Intent newIntent = new Intent();
                    newIntent.putExtra("itemID", itemID);
                    getActivity().setResult(Activity.RESULT_OK, newIntent);
                }
                else {
                    Intent newIntent = new Intent();
                    newIntent.putExtra("itemID", itemID);
                    getActivity().setResult(Activity.RESULT_OK, newIntent);
                    getActivity().finish();
                }
            }
        });
        return newView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        TextView textMsg = view.findViewById(R.id.txtMessage);
        TextView textID = view.findViewById(R.id.txtID);
        String newTempr = "Message = " + newMessage;
        textMsg.setText(newTempr);
        newTempr = "Item ID = " + String.valueOf(itemID);
        textID.setText(newTempr);
    }

}
