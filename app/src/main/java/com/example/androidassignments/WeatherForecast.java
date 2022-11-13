package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;
import org.xmlpull.v1.XmlPullParser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import javax.net.ssl.HttpsURLConnection;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;



public class WeatherForecast extends AppCompatActivity{
    public static String ACTIVITY_NAME = "WeatherForecast.java";
    List<String> makeNewCityList;
    ProgressBar loadProgressBar;
    TextView newCityName, newMaxTemperature, newMinTemperature, newCurrentTemperature;
    ImageView newWeatherImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Info about Weather");
        setContentView(R.layout.activity_weather_forecast);
        newWeatherImage = findViewById(R.id.imageWeather);
        newCityName = findViewById(R.id.newCityName);
        newCurrentTemperature = findViewById(R.id.newCurrentTemperature);
        newMaxTemperature = findViewById(R.id.newMaxTemperature);
        newMinTemperature = findViewById(R.id.newMinTemperature);
        loadProgressBar = findViewById(R.id.proBar);
        loadProgressBar.setVisibility(View.VISIBLE);
        newCityFunc();
    }
    private void newCityFunc(){
        makeNewCityList = Arrays.asList(getResources().getStringArray(R.array.defineCity));
        final Spinner spin = findViewById(R.id.Spin);
        ArrayAdapter<CharSequence> newAdapt = ArrayAdapter.createFromResource(this, R.array.defineCity, android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(newAdapt);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadProgressBar.setVisibility(View.VISIBLE);
                new ForecastQuery(makeNewCityList.get(position)).execute();
                newCityName.setText(String.format("%s Weather", makeNewCityList.get(position)));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public class ForecastQuery extends AsyncTask<String, Integer, String>{
        private String minTemperature;
        private String CurrentTemperature;
        private String maxTemperature;
        private Bitmap imageCurrentWeather;
        protected String newCities;

        public ForecastQuery(String str) {
            this.newCities = str;
        }
        @Override
        protected String doInBackground(String... strings){
            try {
                URL newURL = new URL("https://api.openweathermap.org/" +
                        "data/2.5/weather?q=" + this.newCities  +"," +"ffad799aba9ee2d01c4c7954c17f94a0" + "mode=xml&units=metric");
                HttpsURLConnection openConnection = (HttpsURLConnection) newURL.openConnection();
                openConnection.setReadTimeout(10000);
                openConnection.setConnectTimeout(15000);
                openConnection.setRequestMethod("GET");
                openConnection.setDoInput(true);
                openConnection.connect();

                InputStream newInput = openConnection.getInputStream();

                try {
                    XmlPullParser newParser = Xml.newPullParser();
                    newParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    newParser.setInput(newInput, null);

                    while (newParser.getEventType() != XmlPullParser.END_DOCUMENT) {
                        if(newParser.getEventType() == XmlPullParser.START_TAG) {
                            if (newParser.getName().equals("temperature")) {
                                publishProgress(25);
                                CurrentTemperature = newParser.getAttributeValue(null, "value");
                                publishProgress(50);
                                minTemperature = newParser.getAttributeValue(null, "min");
                                publishProgress(75);
                                maxTemperature = newParser.getAttributeValue(null, "max");

                            } if (newParser.getName().equals("weather")) {
                                String nameOfIcon = newParser.getAttributeValue(null, "icon");
                                String nameOfFile = nameOfIcon + ".png";
                                Log.i(ACTIVITY_NAME, "Searching..." + nameOfFile);

                                if (checkForFile(nameOfFile)) {
                                    FileInputStream fileInput = null;

                                    try {
                                        fileInput = openFileInput(nameOfFile);

                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    Log.i(ACTIVITY_NAME, "File was found");
                                    imageCurrentWeather = BitmapFactory.decodeStream(fileInput);
                                }

                                    String newIconUrl = "https://openweathermap.org/img/w/" + nameOfFile;
                                    imageCurrentWeather = getImage(new URL(newIconUrl));

                                    FileOutputStream outStream = openFileOutput(nameOfFile, Context.MODE_PRIVATE);
                                    imageCurrentWeather.compress(Bitmap.CompressFormat.PNG, 80, outStream);
                                    Log.i(ACTIVITY_NAME, "Downloaded the File");
                                    outStream.flush();
                                    outStream.close();
                                }
                                publishProgress(100);
                            }
                            newParser.next();
                        }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    newInput.close();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Ended";
        }

        public Bitmap getImage(URL url) {
            HttpsURLConnection connection = null;
            try {
                connection = (HttpsURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    return BitmapFactory.decodeStream(connection.getInputStream());
                } else
                    return null;
            } catch (Exception e) {
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }



        public boolean checkForFile(String nameOfFile){
            File newFiles = getBaseContext().getFileStreamPath(nameOfFile);
            return newFiles.exists();
        }

        @Override
        public void onProgressUpdate(Integer... newValues){
            super.onProgressUpdate(newValues);
            loadProgressBar.setProgress(newValues[0]);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onPostExecute(String newString){
            super.onPostExecute(newString);
            loadProgressBar.setVisibility(View.INVISIBLE);
            newWeatherImage.setImageBitmap(imageCurrentWeather);
            newCurrentTemperature.setText(String.format("Current: %s°C", CurrentTemperature));
            newMinTemperature.setText(String.format("Min: %s°C", minTemperature));
            newMaxTemperature.setText(String.format("Max: %s°C", maxTemperature));
        }

    }
}

