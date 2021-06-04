package com.example.coolingyenews.bean;

import java.io.Serializable;

public class Weather implements Serializable {
    private String status;
    private String city;
    private String aqi;
    private String pm25;
    private String temp;
    private String weather;
    private String wind;
    private String weatherimg;
    private Tomorrow tomorrow;

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
    }

    public String getAqi() {
        return aqi;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public String getPm25() {
        return pm25;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getTemp() {
        return temp;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getWeather() {
        return weather;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getWind() {
        return wind;
    }

    public void setWeatherimg(String weatherimg) {
        this.weatherimg = weatherimg;
    }

    public String getWeatherimg() {
        return weatherimg;
    }

    public void setTomorrow(Tomorrow tomorrow) {
        this.tomorrow = tomorrow;
    }

    public Tomorrow getTomorrow() {
        return tomorrow;
    }

    public static class Tomorrow {
        private String temp;
        private String weather;
        private String wind;
        private String weatherimg;

        public void setTemp(String temp) {
            this.temp = temp;
        }

        public String getTemp() {
            return temp;
        }

        public void setWeather(String weather) {
            this.weather = weather;
        }

        public String getWeather() {
            return weather;
        }

        public void setWind(String wind) {
            this.wind = wind;
        }

        public String getWind() {
            return wind;
        }

        public void setWeatherimg(String weatherimg) {
            this.weatherimg = weatherimg;
        }

        public String getWeatherimg() {
            return weatherimg;
        }

    }
}
