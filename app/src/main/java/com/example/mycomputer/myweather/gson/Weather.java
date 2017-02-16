package com.example.mycomputer.myweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mycomputer on 2017/2/13.
 */

public class Weather {
    public String status;
    public Basic basic;
    public AQI aqi;
    public Now now;
    public Suggestion suggestion;
    @SerializedName("daily_forecast")
    public List<Forecast> forecasts;
}
