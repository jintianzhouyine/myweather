package com.example.mycomputer.myweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mycomputer on 2017/2/10.
 */

public class Basic {
    @SerializedName("city")
    public String cityName;
    @SerializedName("id")
    public String WeatherId;
    public Update update;

    public class Update{
        @SerializedName("loc")
        public String updateTime;
    }

}

