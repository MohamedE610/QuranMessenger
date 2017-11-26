
package com.example.e610.quranmessenger.Models.PrayerTimes;

import java.io.Serializable;

public class PrayerTimes implements Serializable
{

    private Integer code;
    private String status;
    private Data data;
    private final static long serialVersionUID = 3605264945360830315L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public PrayerTimes() {
    }

    /**
     * 
     * @param status
     * @param data
     * @param code
     */
    public PrayerTimes(Integer code, String status, Data data) {
        super();
        this.code = code;
        this.status = status;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
