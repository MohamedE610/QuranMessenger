
package com.example.e610.quranmessenger.Models.PrayerTimes;

import java.io.Serializable;

public class Meta implements Serializable
{

    private Float latitude;
    private Float longitude;
    private String timezone;
    private Method method;
    private String latitudeAdjustmentMethod;
    private String midnightMode;
    private String school;
    private Offset offset;
    private final static long serialVersionUID = -8549473042312671746L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Meta() {
    }

    /**
     * 
     * @param midnightMode
     * @param latitudeAdjustmentMethod
     * @param timezone
     * @param school
     * @param method
     * @param longitude
     * @param latitude
     * @param offset
     */
    public Meta(Float latitude, Float longitude, String timezone, Method method, String latitudeAdjustmentMethod, String midnightMode, String school, Offset offset) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
        this.timezone = timezone;
        this.method = method;
        this.latitudeAdjustmentMethod = latitudeAdjustmentMethod;
        this.midnightMode = midnightMode;
        this.school = school;
        this.offset = offset;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getLatitudeAdjustmentMethod() {
        return latitudeAdjustmentMethod;
    }

    public void setLatitudeAdjustmentMethod(String latitudeAdjustmentMethod) {
        this.latitudeAdjustmentMethod = latitudeAdjustmentMethod;
    }

    public String getMidnightMode() {
        return midnightMode;
    }

    public void setMidnightMode(String midnightMode) {
        this.midnightMode = midnightMode;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public Offset getOffset() {
        return offset;
    }

    public void setOffset(Offset offset) {
        this.offset = offset;
    }

}
