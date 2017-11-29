
package com.example.e610.quranmessenger.Models.PageOfQuran;

import java.io.Serializable;
import java.util.List;

public class Data implements Serializable
{

    private Integer number;
    private List<Ayah> ayahs = null;
    private Surahs surahs;
    private Edition edition;
    private final static long serialVersionUID = -5172809647061338686L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Data() {
    }

    /**
     * 
     * @param edition
     * @param surahs
     * @param number
     * @param ayahs
     */
    public Data(Integer number, List<Ayah> ayahs, Surahs surahs, Edition edition) {
        super();
        this.number = number;
        this.ayahs = ayahs;
        this.surahs = surahs;
        this.edition = edition;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public List<Ayah> getAyahs() {
        return ayahs;
    }

    public void setAyahs(List<Ayah> ayahs) {
        this.ayahs = ayahs;
    }

    public Surahs getSurahs() {
        return surahs;
    }

    public void setSurahs(Surahs surahs) {
        this.surahs = surahs;
    }

    public Edition getEdition() {
        return edition;
    }

    public void setEdition(Edition edition) {
        this.edition = edition;
    }

}
