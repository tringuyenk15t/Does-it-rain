package com.tri_nguyen.android.doesitrain;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tri_nguyen.android.doesitrain.data.WeatherContract;
import com.tri_nguyen.android.doesitrain.data.WeatherDpHelper;
import com.tri_nguyen.android.doesitrain.data.weather_model.CustomWeatherModel;
import com.tri_nguyen.android.doesitrain.data.weather_model.WeatherItem;
import com.tri_nguyen.android.doesitrain.data.weather_model.WeatherResponse;
import com.tri_nguyen.android.doesitrain.utils.NetworkUtils;
import com.tri_nguyen.android.doesitrain.utils.OpenWeatherService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rclForecast;
    private RecyclerView.LayoutManager mManager;
    private ForecastAdapter mAdapter;

    private List<CustomWeatherModel> mWeatherListItem = new ArrayList<>();

    private ProgressBar mProgressBar;
    private WeatherDpHelper mDbHelper;
    private Cursor mCursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //remove action bar's border
        getSupportActionBar().setElevation(0);

        mDbHelper = new WeatherDpHelper(this);

        initializedLayout();
        getWeatherData();
    }

    private void initializedLayout(){
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        rclForecast = (RecyclerView) findViewById(R.id.recycler_list_forecast);
        mManager = new LinearLayoutManager(this);
        mAdapter = new ForecastAdapter(this,mWeatherListItem);
        rclForecast.setLayoutManager(mManager);
        rclForecast.setAdapter(mAdapter);
    }

    /**
     * fetch weather data from server
     */
    private void getWeatherData (){

        onLoadingWeatherData();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String cnt = sharedPreferences.getString(
                getString(R.string.pref_cnt_key),getString(R.string.pref_cnt_default_value));

        OpenWeatherService openWeatherService =
                NetworkUtils.createService(OpenWeatherService.class);
        Call<WeatherResponse> call = openWeatherService.getForecast(cnt);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                WeatherResponse weatherResponse = response.body();
//                mAdapter.setWeatherListItem(weatherResponse.getWeatherItem());
//                mAdapter.notifyDataSetChanged();

                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                mDbHelper.insertNewForecast(db,weatherResponse);
                onShowingWeatherData();
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                onShowingWeatherData();
                Log.e("Network Error: " , "Failed of fetching forecast data!");
                Toast.makeText(getApplication(), "Network Error: Failed of fetching forecast data!",
                        Toast.LENGTH_SHORT).show();
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

    private void onLoadingWeatherData(){
        mProgressBar.setVisibility(View.VISIBLE);
        rclForecast.setVisibility(View.INVISIBLE);
    }

    private void onShowingWeatherData(){
        mProgressBar.setVisibility(View.INVISIBLE);
        rclForecast.setVisibility(View.VISIBLE);
    }
}
