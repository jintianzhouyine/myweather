package com.example.mycomputer.myweather;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mycomputer.myweather.gson.Forecast;
import com.example.mycomputer.myweather.gson.Weather;
import com.example.mycomputer.myweather.util.HttpUtil;
import com.example.mycomputer.myweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    public ImageView iconWeather;
    public DrawerLayout drawerLayout;
    private Button navButton;
    public SwipeRefreshLayout swipeRefresh;
    private ImageView bingPicImg;
    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private static String weatherid;
    public int[] image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_weather);
        //初始化各个控件
        iconWeather = (ImageView)findViewById(R.id.icon_weather);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navButton = (Button)findViewById(R.id.nav_button);
        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        weatherLayout = (ScrollView)findViewById(R.id.weather_layout);
        titleCity = (TextView)findViewById(R.id.title_city);
        titleUpdateTime = (TextView)findViewById(R.id.title_update_time);
        degreeText = (TextView)findViewById(R.id.degree_text);
        weatherInfoText = (TextView)findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout)findViewById(R.id.forecast_layout);
        aqiText = (TextView)findViewById(R.id.aqi_text);
        pm25Text = (TextView)findViewById(R.id.pm25_text);
        comfortText = (TextView)findViewById(R.id.comfort_text);
        carWashText = (TextView)findViewById(R.id.car_wash_text);
        sportText = (TextView)findViewById(R.id.sport_text);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        image=new int[1000];
        image[100]=R.drawable.a100;
        image[101]=R.drawable.a101;
        image[102]=R.drawable.a102;
        image[103]=R.drawable.a103;
        image[104]=R.drawable.a104;
        image[200]=R.drawable.a200;
        image[201]=R.drawable.a201;
        image[202]=R.drawable.a202;
        image[203]=R.drawable.a203;
        image[204]=R.drawable.a204;
        image[205]=R.drawable.a205;
        image[206]=R.drawable.a206;
        image[207]=R.drawable.a207;
        image[208]=R.drawable.a208;
        image[209]=R.drawable.a209;
        image[210]=R.drawable.a210;
        image[211]=R.drawable.a211;
        image[212]=R.drawable.a212;
        image[213]=R.drawable.a213;
        image[300]=R.drawable.a300;
        image[301]=R.drawable.a301;
        image[302]=R.drawable.a302;
        image[303]=R.drawable.a303;
        image[304]=R.drawable.a304;
        image[305]=R.drawable.a305;
        image[306]=R.drawable.a306;
        image[307]=R.drawable.a307;
        image[308]=R.drawable.a308;
        image[309]=R.drawable.a309;
        image[310]=R.drawable.a310;
        image[311]=R.drawable.a311;
        image[312]=R.drawable.a312;
        image[313]=R.drawable.a313;
        image[400]=R.drawable.a400;
        image[401]=R.drawable.a401;
        image[402]=R.drawable.a402;
        image[403]=R.drawable.a403;
        image[404]=R.drawable.a404;
        image[405]=R.drawable.a405;
        image[406]=R.drawable.a406;
        image[407]=R.drawable.a407;
        image[500]=R.drawable.a500;
        image[501]=R.drawable.a501;
        image[502]=R.drawable.a502;
        image[503]=R.drawable.a503;
        image[504]=R.drawable.a504;
        image[507]=R.drawable.a507;
        image[508]=R.drawable.a508;
        image[900]=R.drawable.a900;
        image[901]=R.drawable.a901;
        image[999]=R.drawable.a999;


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather",null);
        String bingPic = prefs.getString("bing_pic",null);
        if(bingPic != null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else{
            loadBingPic();
        }
        final String weatherId;

        if(weatherString!=null){

            Weather weather = Utility.handleWeatherResponse(weatherString);
            weatherId= weather.basic.WeatherId;
            showWeatherInfo(weather);
        }else{

            weatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                if(TextUtils.isEmpty(weatherid)){
                    weatherid=weatherId;
                }
                requestWeather(weatherid);
            }
        });
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


    }

    private void loadBingPic() {
        String requestBingPic ="http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.w("MainActivity", "onFailure: 获取照片失败 ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor =PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                        Log.w("MainActivity", "onFailure: 获取照片成功 "+bingPic);
                    }
                });

            }
        });
    }

    public void requestWeather(final String weatherId) {
        this.weatherid =weatherId;
        String weatherUrl = "https://free-api.heweather.com/v5/weather?city=" +
                weatherId +"&key=4cb918d757fa48b39c578eb611dd9c88";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"涛哥尽力了。。。",Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather != null&&"ok".equals(weather.status)){
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather",responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        }else{
                            Toast.makeText(WeatherActivity.this,"涛哥真尽力了。。。",Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
        loadBingPic();
    }

    private void showWeatherInfo(Weather weather) {
        int icon =weather.now.more.icon;
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature+"℃";
        String weatherInfo = weather.now.more.info;
        iconWeather.setImageResource(image[icon]);
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();

        for(Forecast forecast :weather.forecasts){
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
            TextView dateText = (TextView)view.findViewById(R.id.date_text);
            TextView infoText = (TextView)view.findViewById(R.id.info_text);
            TextView maxText = (TextView)view.findViewById(R.id.max_text);
            TextView minText = (TextView)view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            Log.w("MainActivity", "showWeatherInfo: "+forecast.date);
            Log.w("MainActivity", "showWeatherInfo: "+forecast.more.info);
            Log.w("MainActivity", "showWeatherInfo: "+forecast.temperature.max);
            Log.w("MainActivity", "showWeatherInfo: "+forecast.temperature.min);
            forecastLayout.addView(view);
        }
        if(weather.aqi!=null){
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }else{
            aqiText.setText("暂无"+"\n"+"数据");
            pm25Text.setText("暂无"+"\n"+"数据");
        }
        String comfort = "舒适度："+weather.suggestion.comfort.info;
        String carWash = "洗车建议："+weather.suggestion.carWash.info;
        String sport = "运动建议："+weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
    }

}
