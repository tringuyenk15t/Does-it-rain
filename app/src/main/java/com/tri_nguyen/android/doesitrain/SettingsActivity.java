package com.tri_nguyen.android.doesitrain;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by Tri Nguyen on 8/19/2017.
 */

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //TODO consider to use
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//         /*
//         * Normally, calling setDisplayHomeAsUpEnabled(true) as well as
//         * declaring the parent activity in the AndroidManifest is all that is required to get the
//         * up button working properly. However, in this case, we want to navigate to the previous
//         * screen the user came from when the up button was clicked, rather than a single
//         * designated Activity in the Manifest.
//         *
//         * We use the up button's ID (android.R.id.home) to listen for when the up button is
//         * clicked and then call onBackPressed to navigate to the previous Activity when this
//         * happens.
//         */
//        int id = item.getItemId();
//        if(id == R.id.home){
//            onBackPressed();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

}
