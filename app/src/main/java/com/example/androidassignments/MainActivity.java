package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "MainActivity";
    Button btnCam;
    Button btnStartChat;
    Button StartWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(ACTIVITY_NAME, "In onCreate()");

        btnCam = (Button) findViewById(R.id.button);
        btnStartChat = findViewById(R.id.btnChat);
        StartWeather = findViewById(R.id.btnWeather);

        btnCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListItemsActivity.class);
                startActivity(intent);
            }
        });

        btnStartChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(ACTIVITY_NAME, "User clicked Start Chat");
                Intent intent = new Intent(MainActivity.this, ChatWindow.class);
                startActivity(intent);
            }
        });

        StartWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeatherForecast.class);
                startActivity(intent);
            }
        });

    }

    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        if (requestCode == 10) {
            Log.i(ACTIVITY_NAME, "Returned to MainActivity.onActivityResult");
        }
        if (responseCode == Activity.RESULT_OK) {
            String messagePassed = data.getStringExtra("Response");
            Toast toast = Toast.makeText(this, "ListItemsActivity passed: " + messagePassed, Toast.LENGTH_LONG);
            toast.show();

        }
    }

    public void startChat(View view) {
        Log.i(ACTIVITY_NAME, "User clicked Start Chat");
        Intent intent = new Intent(this, ChatWindow.class);
        startActivity(intent);
    }

    public void startToolbar(View view) {
        Log.i(ACTIVITY_NAME, "User clicked Test Toolbar");
        Intent intent = new Intent(this, TestToolbar.class);
        startActivity(intent);
    }

    public void startWeatherForecast(View View){
        Log.i(ACTIVITY_NAME, "User clicked Weather Forecast");
        Intent intent = new Intent(this, WeatherForecast.class);
        startActivity(intent);
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.i(ACTIVITY_NAME, "onResume: ");
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.i(ACTIVITY_NAME, "onStart: ");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }
}