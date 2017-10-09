package com.oxy.easilyhttpenginetest;

/*
 * Created by Administrator on 2017/7/25.
 */

import java.util.List;

public class AddressInfo {
    private String provinceName;
    private int provinceID;

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(int provinceID) {
        this.provinceID = provinceID;
    }

    public List<City> getCitys() {
        return citys;
    }

    public void setCitys(List<City> citys) {
        this.citys = citys;
    }

    private List<City> citys;

    class City{
        private String cityName;
        private int cityID;

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public int getCityID() {
            return cityID;
        }

        public void setCityID(int cityID) {
            this.cityID = cityID;
        }

        public List<Area> getAreas() {
            return areas;
        }

        public void setAreas(List<Area> areas) {
            this.areas = areas;
        }

        private List<Area> areas;
    }
    static class Area{
        private String areaName;
        private int areaID;

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public int getAreaID() {
            return areaID;
        }

        public void setAreaID(int areaID) {
            this.areaID = areaID;
        }
    }
}
