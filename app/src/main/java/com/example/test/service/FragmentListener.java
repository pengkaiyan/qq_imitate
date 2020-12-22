package com.example.test.service;

import retrofit2.Retrofit;

//Activity实现此接口，为fragment提供服务
public interface FragmentListener {
    Retrofit getRetrofit();
    void showServerAddressSetDlg();
}
