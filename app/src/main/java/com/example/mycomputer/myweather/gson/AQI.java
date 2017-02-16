package com.example.mycomputer.myweather.gson;

/**
 * Created by mycomputer on 2017/2/10.
 */

public class AQI {
    public AQICity city;
    public class AQICity {
        public String aqi;
        public String pm25;
    }
}
