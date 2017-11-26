
package com.example.e610.quranmessenger.Models.PrayerTimes;

import java.io.Serializable;

public class Offset implements Serializable
{

    private Integer imsak;
    private Integer fajr;
    private Integer sunrise;
    private Integer dhuhr;
    private Integer asr;
    private Integer maghrib;
    private Integer sunset;
    private Integer isha;
    private Integer midnight;
    private final static long serialVersionUID = -5017020401412622895L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Offset() {
    }

    /**
     * 
     * @param dhuhr
     * @param imsak
     * @param midnight
     * @param isha
     * @param sunset
     * @param asr
     * @param sunrise
     * @param fajr
     * @param maghrib
     */
    public Offset(Integer imsak, Integer fajr, Integer sunrise, Integer dhuhr, Integer asr, Integer maghrib, Integer sunset, Integer isha, Integer midnight) {
        super();
        this.imsak = imsak;
        this.fajr = fajr;
        this.sunrise = sunrise;
        this.dhuhr = dhuhr;
        this.asr = asr;
        this.maghrib = maghrib;
        this.sunset = sunset;
        this.isha = isha;
        this.midnight = midnight;
    }

    public Integer getImsak() {
        return imsak;
    }

    public void setImsak(Integer imsak) {
        this.imsak = imsak;
    }

    public Integer getFajr() {
        return fajr;
    }

    public void setFajr(Integer fajr) {
        this.fajr = fajr;
    }

    public Integer getSunrise() {
        return sunrise;
    }

    public void setSunrise(Integer sunrise) {
        this.sunrise = sunrise;
    }

    public Integer getDhuhr() {
        return dhuhr;
    }

    public void setDhuhr(Integer dhuhr) {
        this.dhuhr = dhuhr;
    }

    public Integer getAsr() {
        return asr;
    }

    public void setAsr(Integer asr) {
        this.asr = asr;
    }

    public Integer getMaghrib() {
        return maghrib;
    }

    public void setMaghrib(Integer maghrib) {
        this.maghrib = maghrib;
    }

    public Integer getSunset() {
        return sunset;
    }

    public void setSunset(Integer sunset) {
        this.sunset = sunset;
    }

    public Integer getIsha() {
        return isha;
    }

    public void setIsha(Integer isha) {
        this.isha = isha;
    }

    public Integer getMidnight() {
        return midnight;
    }

    public void setMidnight(Integer midnight) {
        this.midnight = midnight;
    }

}
