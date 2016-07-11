package com.example.android.sunshine.app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by AmmY on 11/07/16.
 */
public class ForecastFragment extends Fragment {
    ArrayAdapter<String> mForecastAdapter;

    public ForecastFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.forecastfragment,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_refresh){
            FetchWeatherTask weatherTask = new FetchWeatherTask();
            weatherTask.execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        String[] forecastArray = {"Today-Sunny-88/63","Tomorrow-Windy-98/65", "Wednesday-Cloudy-76/54", "Thursday-Cloudy-76/54",
                "Friday-Cloudy-76/54","Saturday-Cloudy-76/54","Sunday-Cloudy-76/54" };

        List<String> weekForecast = new ArrayList<String>(Arrays.asList(forecastArray));



        mForecastAdapter= new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_forecast,
                R.id.list_item_forecast_textview,
                weekForecast);

        ListView weekForecastList = (ListView) rootView.findViewById(R.id.listview_forecast);
        weekForecastList.setAdapter(mForecastAdapter);
        return rootView;
    }

    public class FetchWeatherTask extends AsyncTask<Void,Void,Void> {

        private final String LOG_CAT = FetchWeatherTask.class.getSimpleName();

        @Override
        protected Void doInBackground(Void... params){
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String foreCastJsonStr = null;

            try{

                URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=Buffalo,usa&mode=json&units=metric&cnt=7&APPID=c7d59852761ed8bde914802a6c9c93fc");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if(inputStream==null){
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line=reader.readLine())!=null){
                    buffer.append(line+"\n");

                }
                if (buffer.length()==0){
                    return null;
                }

                foreCastJsonStr = buffer.toString();

            }
            catch(IOException e)
            {
                Log.e("PlaceholderFragment","ERROR", e);
                return null;

            }
            finally {
                if (urlConnection != null)
                {
                    urlConnection.disconnect();
                }
                if(reader != null){
                    try{
                        reader.close();
                    }
                    catch(IOException e){
                        Log.e("PlaceHolderFragment", "Error Closing stream", e);
                    }
                }
            }
            return null;
        }

    }
}