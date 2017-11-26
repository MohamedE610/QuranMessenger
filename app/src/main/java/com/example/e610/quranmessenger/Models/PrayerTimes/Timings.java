
package com.example.e610.quranmessenger.Models.PrayerTimes;

import java.io.Serializable;

public class Timings implements Serializable
{

    public String Fajr;
    public String Sunrise;
    public String Dhuhr;
    public String Asr;
    public String Sunset;
    public String Maghrib;
    public String Isha;
    public String Imsak;
    public String Midnight;
    private final static long serialVersionUID = -4518066916184705395L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Timings() {
    }

    /**
     * 
     * @param imsak
     * @param dhuhr
     * @param midnight
     * @param isha
     * @param sunset
     * @param asr
     * @param sunrise
     * @param fajr
     * @param maghrib
     */
    public Timings(String fajr, String sunrise, String dhuhr, String asr, String sunset, String maghrib, String isha, String imsak, String midnight) {
        super();
        this.Fajr = fajr;
        this.Sunrise = sunrise;
        this.Dhuhr = dhuhr;
        this.Asr = asr;
        this.Sunset = sunset;
        this.Maghrib = maghrib;
        this.Isha = isha;
        this.Imsak = imsak;
        this.Midnight = midnight;
    }


}
