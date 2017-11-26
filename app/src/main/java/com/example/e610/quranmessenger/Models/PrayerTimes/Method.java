
package com.example.e610.quranmessenger.Models.PrayerTimes;

import java.io.Serializable;

public class Method implements Serializable
{

    private Integer id;
    private String name;
    private Params params;
    private final static long serialVersionUID = -6987780112170299303L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Method() {
    }

    /**
     * 
     * @param id
     * @param name
     * @param params
     */
    public Method(Integer id, String name, Params params) {
        super();
        this.id = id;
        this.name = name;
        this.params = params;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Params getParams() {
        return params;
    }

    public void setParams(Params params) {
        this.params = params;
    }

}
