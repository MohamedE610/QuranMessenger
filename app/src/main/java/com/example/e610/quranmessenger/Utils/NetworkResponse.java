package com.example.e610.quranmessenger.Utils;


public interface NetworkResponse {


    void OnSuccess(String JsonData);
    void OnFailure(boolean Failure);
}
