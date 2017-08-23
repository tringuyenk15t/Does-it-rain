package com.tri_nguyen.android.doesitrain;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.tri_nguyen.android.doesitrain.data.SampleWeatherObject;
import com.tri_nguyen.android.doesitrain.databinding.ActivityDetailBinding;

/**
 * Created by Tri Nguyen on 8/11/2017.
 */

public class DetailActivity extends AppCompatActivity {
    private ActivityDetailBinding mDataBinding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //dummy data object
        SampleWeatherObject object = new SampleWeatherObject(
                "Monday, August 11","Cloud", "20\u00B0", "10\u00B0",R.drawable.art_clouds);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        mDataBinding.primaryInfo.date.setText(object.getDate());
        mDataBinding.primaryInfo.weatherDescription.setText(object.getWeatherSummary());
        mDataBinding.primaryInfo.highTempeturate.setText(object.getHighTemp());
        mDataBinding.primaryInfo.lowTempeturate.setText(object.getLowTemp());

        mDataBinding.primaryInfo.weatherIcon.setImageResource(object.getScrID());

        mDataBinding.extraInfo.humidity.setText("38%");
        mDataBinding.extraInfo.pressure.setText("699 hPa");
        mDataBinding.extraInfo.wind.setText("4km/h NW");
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
