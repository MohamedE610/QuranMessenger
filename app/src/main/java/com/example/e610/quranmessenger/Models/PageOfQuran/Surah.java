
package com.example.e610.quranmessenger.Models.PageOfQuran;

import java.io.Serializable;

public class Surah implements Serializable
{

    private Integer number;
    private String name;
    private String englishName;
    private String englishNameTranslation;
    private String revelationType;
    private Integer numberOfAyahs;
    private final static long serialVersionUID = -8210086826161876544L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Surah() {
    }

    /**
     * 
     * @param englishName
     * @param numberOfAyahs
     * @param name
     * @param number
     * @param revelationType
     * @param englishNameTranslation
     */
    public Surah(Integer number, String name, String englishName, String englishNameTranslation, String revelationType, Integer numberOfAyahs) {
        super();
        this.number = number;
        this.name = name;
        this.englishName = englishName;
        this.englishNameTranslation = englishNameTranslation;
        this.revelationType = revelationType;
        this.numberOfAyahs = numberOfAyahs;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getEnglishNameTranslation() {
        return englishNameTranslation;
    }

    public void setEnglishNameTranslation(String englishNameTranslation) {
        this.englishNameTranslation = englishNameTranslation;
    }

    public String getRevelationType() {
        return revelationType;
    }

    public void setRevelationType(String revelationType) {
        this.revelationType = revelationType;
    }

    public Integer getNumberOfAyahs() {
        return numberOfAyahs;
    }

    public void setNumberOfAyahs(Integer numberOfAyahs) {
        this.numberOfAyahs = numberOfAyahs;
    }

}
