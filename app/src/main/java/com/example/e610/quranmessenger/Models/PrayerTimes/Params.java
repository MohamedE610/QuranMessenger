
package com.example.e610.quranmessenger.Models.PrayerTimes;

import java.io.Serializable;

public class Params implements Serializable
{

    private Integer fajr;
    private Integer isha;
    private final static long serialVersionUID = -506560059308590481L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Params() {
    }

    /**
     * 
     * @param isha
     * @param fajr
     */
    public Params(Integer fajr, Integer isha) {
        super();
        this.fajr = fajr;
        this.isha = isha;
    }

    public Integer getFajr() {
        return fajr;
    }

    public void setFajr(Integer fajr) {
        this.fajr = fajr;
    }

    public Integer getIsha() {
        return isha;
    }

    public void setIsha(Integer isha) {
        this.isha = isha;
    }

}
