package com.tri_nguyen.android.doesitrain;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.tri_nguyen.android.doesitrain.data.CallbackEvent;
import com.tri_nguyen.android.doesitrain.data.DaoMaster;
import com.tri_nguyen.android.doesitrain.data.WeatherDpHelper;
import com.tri_nguyen.android.doesitrain.data.WeatherInfo;
import com.tri_nguyen.android.doesitrain.data.weather_pojo.WeatherResponse;
import com.tri_nguyen.android.doesitrain.sync.SynchronizeUtils;
import com.tri_nguyen.android.doesitrain.utils.LocationUtils;
import com.tri_nguyen.android.doesitrain.utils.NetworkUtils;
import com.tri_nguyen.android.doesitrain.utils.OpenWeatherService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements
        ForecastAdapter.ForecastAdapterOnClickHandler{
    private ProgressBar mProgressBar;
    private RecyclerView rclForecast;
    private RecyclerView.LayoutManager mManager;
    private ForecastAdapter mAdapter;
    private List<WeatherInfo> mForecastList = new ArrayList<>();

    private DaoMaster mDaoMaster;

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int PERMISSION_REQUEST_ID = 17;
    private LocationUtils locationUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationUtils = new LocationUtils(this);

        //remove action bar's border
        getSupportActionBar().setElevation(0);

        //initialize greenDao
        mDaoMaster = new DaoMaster(new WeatherDpHelper(this).getWritableDatabase());
        initializeViews();

    }

    /**
     * Initialize ui components
     */
    private void initializeViews() {
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        rclForecast = (RecyclerView) findViewById(R.id.recycler_list_forecast);
        mAdapter = new ForecastAdapter(this, mForecastList, this);
        mManager = new LinearLayoutManager(this);
        rclForecast.setLayoutManager(mManager);
        rclForecast.setAdapter(mAdapter);
    }

    /**
     *
     */
    private void showForecastList() {
        updateWeatherList();
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

    private void onLoadingWeatherData(){
        mProgressBar.setVisibility(View.VISIBLE);
        rclForecast.setVisibility(View.INVISIBLE);
    }

    private void onShowingWeatherData(){
        mProgressBar.setVisibility(View.INVISIBLE);
        rclForecast.setVisibility(View.VISIBLE);
    }

    /**
     * load forecast list from db
     */
    private void updateWeatherList(){
        List<WeatherInfo> forecastList = WeatherDpHelper.getAllWeatherData(mDaoMaster);
        if(forecastList.size() >0 && forecastList != null){
            mAdapter.setWeatherListItem(forecastList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(long id) {
        Intent detailActivityIntent = new Intent(this,DetailActivity.class);
        detailActivityIntent.putExtra(WeatherDpHelper.FORECAST_ID,id);
        startActivity(detailActivityIntent);
    }

    /**
     *  Check does activity has permission to access to coarse_location
     * @return whether permission has been granted or not
     */
    private boolean checkPermission(){
        int permissionStatus = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionStatus == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * request permission needed
     */
    private void requestPermission(){
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_REQUEST_ID
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case PERMISSION_REQUEST_ID:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    SynchronizeUtils.initializeWeatherData(this);
                    // permission granted
                    showForecastList();
                }else
                {
                    Toast.makeText(this, getResources().getString(R.string.reject_permission_warning)
                            , Toast.LENGTH_LONG).show();
                    // Permission denied
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    @Subscribe
    public void onHandleCallbackEvent(CallbackEvent event){
        updateWeatherList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(checkPermission()){
            SynchronizeUtils.initializeWeatherData(this);
            showForecastList();
        }else{
            requestPermission();
        }
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
