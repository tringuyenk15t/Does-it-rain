package com.tri_nguyen.android.doesitrain;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.tri_nguyen.android.doesitrain.data.SampleWeatherObject;
import com.tri_nguyen.android.doesitrain.data.weather.WeatherItem;
import com.tri_nguyen.android.doesitrain.data.weather.WeatherResponse;
import com.tri_nguyen.android.doesitrain.utils.NetworkUtils;
import com.tri_nguyen.android.doesitrain.utils.OpenWeatherService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private List<SampleWeatherObject> list = new ArrayList<>();
    private RecyclerView rclForecast;
    private RecyclerView.LayoutManager manager;
    private ForecastAdapter adapter;
    private List<WeatherItem> mWeatherListItem = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //remove action bar's border
        getSupportActionBar().setElevation(0);

        initializedLayout();
        getWeatherData();
    }

    private void initializedLayout(){
        rclForecast = (RecyclerView) findViewById(R.id.recycler_list_forecast);
        manager = new LinearLayoutManager(this);
        adapter = new ForecastAdapter(this,mWeatherListItem);
        rclForecast.setLayoutManager(manager);
        rclForecast.setAdapter(adapter);
    }

    /**
     * fetch weather data from server
     */
    private void getWeatherData (){
        //TODO this method should query from sqlite database instead of loading data directly
        // to improve performance
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        OpenWeatherService openWeatherService =
                NetworkUtils.createService(OpenWeatherService.class);

        String cnt = sharedPreferences.getString(
                getString(R.string.pref_cnt_key),getString(R.string.pref_cnt_default_value));
        Call<WeatherResponse> call = openWeatherService.getForecast(cnt);

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                WeatherResponse weatherResponse = response.body();
                adapter.setWeatherListItem(weatherResponse.getWeatherItem());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.e("Network Error: " , "Failed of fetching forecast data!");
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWeatherData();
    }
}
