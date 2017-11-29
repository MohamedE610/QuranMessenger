
package com.example.e610.quranmessenger.Models.SurahOfQuran;

import java.io.Serializable;

public class Surah implements Serializable
{

    public Integer number;
    public String name;
    public String englishName;
    public String englishNameTranslation;
    public Integer numberOfAyahs;
    public String revelationType;
    private final static long serialVersionUID = -914062136243013660L;

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
    public Surah(Integer number, String name, String englishName, String englishNameTranslation, Integer numberOfAyahs, String revelationType) {
        super();
        this.number = number;
        this.name = name;
        this.englishName = englishName;
        this.englishNameTranslation = englishNameTranslation;
        this.numberOfAyahs = numberOfAyahs;
        this.revelationType = revelationType;
    }

}
