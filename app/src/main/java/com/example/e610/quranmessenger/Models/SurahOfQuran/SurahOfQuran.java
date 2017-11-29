
package com.example.e610.quranmessenger.Models.SurahOfQuran;

import java.io.Serializable;
import java.util.List;

public class SurahOfQuran implements Serializable
{

    public Integer code;
    public String status;
    public List<Surah> data = null;
    private final static long serialVersionUID = -7646251752769157218L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public SurahOfQuran() {
    }

    /**
     * 
     * @param status
     * @param data
     * @param code
     */
    public SurahOfQuran(Integer code, String status, List<Surah> data) {
        super();
        this.code = code;
        this.status = status;
        this.data = data;
    }

}
