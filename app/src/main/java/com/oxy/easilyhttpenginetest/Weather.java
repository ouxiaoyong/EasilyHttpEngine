package com.oxy.easilyhttpenginetest;

/**
 * Created by Administrator on 2017/8/3.
 */

public class Weather {
    public WeatherInfo weatherinfo;
    public class WeatherInfo{
        public String city;
        public int cityid;
        public int temp;
        //风向
        public String WD;
        //风速
        public String WS;
        public String SD;
        public int WSE;
        public String time;
        public int isRadar;
        public String Radar;
        public String njd;
        public int qy;
        public int rain;

    }

    @Override
    public String toString() {
        return "{cityName:"+weatherinfo.city+",cityid:"+weatherinfo.cityid+"}";
    }
}
