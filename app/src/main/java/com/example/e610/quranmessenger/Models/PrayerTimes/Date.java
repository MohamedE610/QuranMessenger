
package com.example.e610.quranmessenger.Models.PrayerTimes;

import java.io.Serializable;

public class Date implements Serializable
{

    private String readable;
    private String timestamp;
    private final static long serialVersionUID = 6058454261389981204L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Date() {
    }

    /**
     * 
     * @param timestamp
     * @param readable
     */
    public Date(String readable, String timestamp) {
        super();
        this.readable = readable;
        this.timestamp = timestamp;
    }

    public String getReadable() {
        return readable;
    }

    public void setReadable(String readable) {
        this.readable = readable;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
