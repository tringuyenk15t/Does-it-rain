package com.tri_nguyen.android.doesitrain;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.tri_nguyen.android.doesitrain.data.DaoMaster;
import com.tri_nguyen.android.doesitrain.data.WeatherDpHelper;
import com.tri_nguyen.android.doesitrain.data.WeatherInfo;
import com.tri_nguyen.android.doesitrain.databinding.ActivityDetailBinding;
import com.tri_nguyen.android.doesitrain.utils.DateTimeUtils;
import com.tri_nguyen.android.doesitrain.utils.NetworkUtils;
import com.tri_nguyen.android.doesitrain.utils.WeatherUtils;

/**
 * Created by Tri Nguyen on 8/11/2017.
 */

public class DetailActivity extends AppCompatActivity {
    private ActivityDetailBinding mDataBinding;
    private DaoMaster mDaoMaster;
    private WeatherInfo mWeatherInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDaoMaster = new DaoMaster(new WeatherDpHelper(this).getReadableDatabase());
        mDataBinding = DataBindingUtil.setContentView(this,R.layout.activity_detail);

        long id  = getIntent().getLongExtra(WeatherDpHelper.FORECAST_ID,0);
        mWeatherInfo = WeatherDpHelper.getWeatherById(mDaoMaster,id);

        mDataBinding.primaryInfo.date.setText(DateTimeUtils.convertDateTimeToString(this,mWeatherInfo.getDate()));
        String imgUrl = NetworkUtils.BASE_URL_FOR_IMG + mWeatherInfo.getWeatherIcon() + ".png";
        Glide.with(this)
                .load(imgUrl)
                .into(mDataBinding.primaryInfo.weatherIcon);
        mDataBinding.primaryInfo.weatherDescription.setText(mWeatherInfo.getWeatherDescription());

        double maxTempInDouble = mWeatherInfo.getMaxTemperature();
        String highString = WeatherUtils.formatTemperature(this, maxTempInDouble);
        mDataBinding.primaryInfo.highTempeturate.setText(highString);

        double minTempInDouble = mWeatherInfo.getMinTemperature();
        String lowString = WeatherUtils.formatTemperature(this, minTempInDouble);
        mDataBinding.primaryInfo.lowTempeturate.setText(lowString);

        String humilityFormat = getResources().getString(R.string.format_humidity);
        mDataBinding.extraInfo.humidity.setText(
                String.format(humilityFormat, mWeatherInfo.getHumidity()));

        String pressureFormat = getResources().getString(R.string.format_pressure);
        mDataBinding.extraInfo.pressure.setText(
                String.format(pressureFormat, mWeatherInfo.getPressure()));

        String wind = WeatherUtils.getFormattedWind(this,mWeatherInfo.getWindSpeed()
                , mWeatherInfo.getWindDirection());
        mDataBinding.extraInfo.wind.setText(wind);

        String rainFormat = getResources().getString(R.string.format_rain);
        mDataBinding.extraInfo.rain.setText(String.format(rainFormat, mWeatherInfo.getRain()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
