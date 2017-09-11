package com.tri_nguyen.android.doesitrain;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.tri_nguyen.android.doesitrain.data.DaoMaster;
import com.tri_nguyen.android.doesitrain.data.WeatherDpHelper;
import com.tri_nguyen.android.doesitrain.data.WeatherInfo;
import com.tri_nguyen.android.doesitrain.data.weather_pojo.WeatherResponse;
import com.tri_nguyen.android.doesitrain.utils.NetworkUtils;
import com.tri_nguyen.android.doesitrain.utils.OpenWeatherService;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Manifest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements
        ForecastAdapter.ForecastAdapterOnClickHandler {
    private ProgressBar mProgressBar;
    private RecyclerView rclForecast;
    private RecyclerView.LayoutManager mManager;
    private ForecastAdapter mAdapter;
    private List<WeatherInfo> mForecastList = new ArrayList<>();

    private DaoMaster mDaoMaster;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //remove action bar's border
        getSupportActionBar().setElevation(0);

        //initialize greenDao
        mDaoMaster = new DaoMaster(new WeatherDpHelper(this).getWritableDatabase());
        initializeViews();
    }

    private void initializeViews() {
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        rclForecast = (RecyclerView) findViewById(R.id.recycler_list_forecast);
        mAdapter = new ForecastAdapter(this, mForecastList, this);
        mManager = new LinearLayoutManager(this);
        rclForecast.setLayoutManager(mManager);
        rclForecast.setAdapter(mAdapter);
    }

    /**
     * fetch weather data from server
     */
    private void getWeatherData() {

        //Show progressBar while loading data
        onLoadingWeatherData();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    //get settings variables for fetching weather data
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication());
                    String cnt = sharedPreferences.getString(
                            getString(R.string.pref_cnt_key),getString(R.string.pref_cnt_default_value));
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();

                    OpenWeatherService openWeatherService =
                            NetworkUtils.createService(OpenWeatherService.class);
                    //TODO use current location instead of hard coded location
                    Call<WeatherResponse> call = openWeatherService.getForecast(lat, lon, cnt);
                    call.enqueue(new Callback<WeatherResponse>() {
                        @Override
                        public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {

                            WeatherDpHelper.clearWeatherData(mDaoMaster);

                            //convert response body into WeatherResponse POJO
                            WeatherResponse weatherResponse = response.body();
                            //insert weather data into db
                            WeatherDpHelper.weatherBulkInsert(weatherResponse.getWeatherItem(),mDaoMaster);
                            //update new data into recycler view
                            updateWeatherList();
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

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void onLoadingWeatherData(){
        mProgressBar.setVisibility(View.VISIBLE);
        rclForecast.setVisibility(View.INVISIBLE);
    }

    private void onShowingWeatherData(){
        mProgressBar.setVisibility(View.INVISIBLE);
        rclForecast.setVisibility(View.VISIBLE);
    }

    private void updateWeatherList(){
        List<WeatherInfo> forecastList = WeatherDpHelper.getAllWeatherData(mDaoMaster);
        mAdapter.setWeatherListItem(forecastList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(long id) {
        Intent detailActivityIntent = new Intent(this,DetailActivity.class);
        detailActivityIntent.putExtra(WeatherDpHelper.FORECAST_ID,id);
        startActivity(detailActivityIntent);
    }
}
