package com.tri_nguyen.android.doesitrain;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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

    private ForecastAdapterOnClickHandler mClickHandler;

    public interface ForecastAdapterOnClickHandler {
        void onClick(long id);
    }

    public ForecastAdapter(Context context, List<WeatherInfo> mWeatherListItem,
                           ForecastAdapterOnClickHandler onclickHandler) {
        this.mForecastList = mWeatherListItem;
        this.mContext = context;
        this.mClickHandler = onclickHandler;
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

        ForecastListViewHolder holder = new ForecastListViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ForecastListViewHolder holder, int position) {
        WeatherInfo singleItem = mForecastList.get(position);
        holder.tvDate.setText(DateTimeUtils.convertDateTimeToString(mContext, singleItem.getDate()));
        holder.tvSummary.setText(singleItem.getWeatherDescription());

        //load weather icon from open weather server.
        String imgUrl = NetworkUtils.BASE_URL_FOR_IMG + singleItem.getWeatherIcon() + ".png";
        Glide.with(mContext)
                .load(imgUrl)
                .into(holder.imgWeatherIcon);


        double maxTempInDouble = singleItem.getMaxTemperature();
        double minTempInDouble = singleItem.getMinTemperature();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String units = sharedPreferences.getString(
                mContext.getString(R.string.pref_unit_key),
                mContext.getString(R.string.pref_units_metric));

        if(units.equals(mContext.getString(R.string.pref_units_imperial))){
            maxTempInDouble+= 32;
            minTempInDouble += 32;
        }

        String highString = WeatherUtils.formatTemperature(mContext, maxTempInDouble);
        String highA11y = mContext.getString(R.string.a11y_high_temp, highString);
        holder.tvHigh.setText(highA11y);

        String lowString = WeatherUtils.formatTemperature(mContext, minTempInDouble);
        holder.tvLow.setText(lowString);
    }

    public class ForecastListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tvDate, tvSummary, tvHigh, tvLow;
        public ImageView imgWeatherIcon;

        public ForecastListViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.date);
            tvSummary = itemView.findViewById(R.id.weather_description);
            tvHigh = itemView.findViewById(R.id.high_temperature);
            tvLow = itemView.findViewById(R.id.low_temperature);
            imgWeatherIcon = itemView.findViewById(R.id.weather_icon);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            long id = mForecastList.get(adapterPosition).getId();
            mClickHandler.onClick(id);
        }
    }

    @Override
    public int getItemCount() {
        return mForecastList.size();
    }

    @Override
    public int getItemViewType(int position) {
        long itemDate = mForecastList.get(position).getDate();

        if(DateTimeUtils.getDayName(mContext,itemDate).equals(
                mContext.getResources().getString(R.string.today))){
            return VIEW_TYPE_TODAY;
        }else{
            return VIEW_TYPE_FUTURE_DAY;
        }
    }

    public void setWeatherListItem(List<WeatherInfo> forecastList){
        this.mForecastList = forecastList;
    }

}
