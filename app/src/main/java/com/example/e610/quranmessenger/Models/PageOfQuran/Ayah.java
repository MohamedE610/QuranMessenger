
package com.example.e610.quranmessenger.Models.PageOfQuran;

import java.io.Serializable;

public class Ayah implements Serializable
{

    private Integer number;
    private String text;
    private Surah surah;
    private Integer numberInSurah;
    private Integer juz;
    private Integer manzil;
    private Integer page;
    private Integer ruku;
    private Integer hizbQuarter;
    private Boolean sajda;
    private final static long serialVersionUID = -2182749166026412489L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Ayah() {
    }

    /**
     * 
     * @param text
     * @param page
     * @param sajda
     * @param juz
     * @param ruku
     * @param manzil
     * @param number
     * @param numberInSurah
     * @param hizbQuarter
     * @param surah
     */
    public Ayah(Integer number, String text, Surah surah, Integer numberInSurah, Integer juz, Integer manzil, Integer page, Integer ruku, Integer hizbQuarter, Boolean sajda) {
        super();
        this.number = number;
        this.text = text;
        this.surah = surah;
        this.numberInSurah = numberInSurah;
        this.juz = juz;
        this.manzil = manzil;
        this.page = page;
        this.ruku = ruku;
        this.hizbQuarter = hizbQuarter;
        this.sajda = sajda;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Surah getSurah() {
        return surah;
    }

    public void setSurah(Surah surah) {
        this.surah = surah;
    }

    public Integer getNumberInSurah() {
        return numberInSurah;
    }

    public void setNumberInSurah(Integer numberInSurah) {
        this.numberInSurah = numberInSurah;
    }

    public Integer getJuz() {
        return juz;
    }

    public void setJuz(Integer juz) {
        this.juz = juz;
    }

    public Integer getManzil() {
        return manzil;
    }

    public void setManzil(Integer manzil) {
        this.manzil = manzil;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRuku() {
        return ruku;
    }

    public void setRuku(Integer ruku) {
        this.ruku = ruku;
    }

    public Integer getHizbQuarter() {
        return hizbQuarter;
    }

    public void setHizbQuarter(Integer hizbQuarter) {
        this.hizbQuarter = hizbQuarter;
    }

    public Boolean getSajda() {
        return sajda;
    }

    public void setSajda(Boolean sajda) {
        this.sajda = sajda;
    }

}
