
package com.example.e610.quranmessenger.Models.PrayerTimes;

import java.io.Serializable;

public class Data implements Serializable
{

    private Timings timings;
    private Date date;
    private Meta meta;
    private final static long serialVersionUID = 8566145230100487366L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Data() {
    }

    /**
     * 
     * @param timings
     * @param date
     * @param meta
     */
    public Data(Timings timings, Date date, Meta meta) {
        super();
        this.timings = timings;
        this.date = date;
        this.meta = meta;
    }

    public Timings getTimings() {
        return timings;
    }

    public void setTimings(Timings timings) {
        this.timings = timings;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

}
