package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;
import android.content.DialogInterface;

import android.widget.CheckBox;


public class ListItemsActivity extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "ListItemsActivity";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Switch SwitchCheck;
    ImageButton ButtonPicture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);
        Log.i(ACTIVITY_NAME, "In onCreate()");
        ButtonPicture = findViewById(R.id.buttonPicture);
        ButtonPicture.setOnClickListener(v -> switchActivities());
    }

    private void switchActivities() {
        Intent takePictureIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.camera_error, Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageButton myImageBtn = findViewById(R.id.buttonPicture);
            myImageBtn.setImageBitmap(imageBitmap);
        }
    }

    public void setOnCheckedChanged(View view){
        SwitchCheck = findViewById(R.id.switchCheck);
        CharSequence text="";
        int duration = 0;
        if (SwitchCheck.isChecked()){
            text = "Switch is On";
            duration = Toast.LENGTH_SHORT;

        }else if(!SwitchCheck.isChecked()){
            text = "Switch is Off";
            duration = Toast.LENGTH_LONG;
        }

        Toast toast = Toast.makeText(this , text, duration);
        toast.show();

    }

    public void OnCheckChanged(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ListItemsActivity.this);
        builder.setMessage(R.string.dialog_message)
                .setTitle(R.string.dialog_title)
                .setPositiveButton(R.string.ok, (dialog, id) -> {
                    Intent resultIntent = new Intent(  );
                    resultIntent.putExtra("Response", "Here is my response");
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CheckBox myCheckBox = findViewById(R.id.newCheckBox);
                        myCheckBox.setChecked(false);
                    }
                })
                .show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }
}