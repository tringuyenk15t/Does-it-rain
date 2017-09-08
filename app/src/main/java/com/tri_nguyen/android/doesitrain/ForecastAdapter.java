package com.tri_nguyen.android.doesitrain;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tri_nguyen.android.doesitrain.data.WeatherInfo;
import com.tri_nguyen.android.doesitrain.utils.DateTimeUtils;
import com.tri_nguyen.android.doesitrain.utils.NetworkUtils;
import com.tri_nguyen.android.doesitrain.utils.WeatherUtils;

import java.util.List;

/**
 * Created by Tri Nguyen on 8/7/2017.
 */

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastListViewHolder> {
    private List<WeatherInfo> mForecastList;

    private Context mContext;
    private static final int VIEW_TYPE_FUTURE_DAY = 2;
    private static final int VIEW_TYPE_TODAY = 1;
    private boolean mUseTodayLayout = true;

    public ForecastAdapter(Context context, List<WeatherInfo> mWeatherListItem) {
        this.mForecastList = mWeatherListItem;
        this.mContext = context;
    }

    @Override
    public ForecastListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId;

        switch (viewType){
            case VIEW_TYPE_TODAY:
                layoutId = R.layout.today_forecast_item;
                break;
            case VIEW_TYPE_FUTURE_DAY:
                layoutId = R.layout.list_forecast_item;
                break;
            default:
                throw new IllegalArgumentException(
                        parent.getResources().getString(R.string.invalid_view_type) + viewType);
        }

        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutId,parent,false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailIntent = new Intent(mContext,DetailActivity.class);
                mContext.startActivity(detailIntent);
            }
        });

        ForecastListViewHolder holder = new ForecastListViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ForecastListViewHolder holder, int position) {
        WeatherInfo singleItem = mForecastList.get(position);
//        //TODO need to fix date format
        holder.tvDate.setText(DateTimeUtils.convertDateTimeToString(mContext, singleItem.getDate()));
        holder.tvSummary.setText(singleItem.getWeatherDescription());

        //load weather icon from open weather server.
        String imgUrl = NetworkUtils.BASE_URL_FOR_IMG + singleItem.getWeatherIcon() + ".png";
        Glide.with(mContext)
                .load(imgUrl)
                .into(holder.imgWeatherIcon);

        double maxTempInDouble = singleItem.getMaxTemperature();
        String highString = WeatherUtils.formatTemperature(mContext, maxTempInDouble);
        String highA11y = mContext.getString(R.string.a11y_high_temp, highString);
        holder.tvHigh.setText(highA11y);

        double minTempInDouble = singleItem.getMinTemperature();
        String lowString = WeatherUtils.formatTemperature(mContext, minTempInDouble);
        holder.tvLow.setText(lowString);

    }

    public class ForecastListViewHolder extends RecyclerView.ViewHolder{
        public TextView tvDate, tvSummary, tvHigh, tvLow;
        public ImageView imgWeatherIcon;

        public ForecastListViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.date);
            tvSummary = itemView.findViewById(R.id.weather_description);
            tvHigh = itemView.findViewById(R.id.high_temperature);
            tvLow = itemView.findViewById(R.id.low_temperature);
            imgWeatherIcon = itemView.findViewById(R.id.weather_icon);
        }
    }

    @Override
    public int getItemCount() {
        return mForecastList.size();
    }

    @Override
    public int getItemViewType(int position) {
        //TODO update this methods by assign correct today value
        if(mUseTodayLayout && position == 0){
            return VIEW_TYPE_TODAY;
        }else{
            return VIEW_TYPE_FUTURE_DAY;
        }
    }

    public void setWeatherListItem(List<WeatherInfo> forecastList){
        this.mForecastList = forecastList;
    }

}
