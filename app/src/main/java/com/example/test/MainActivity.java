package com.example.test;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.test.adapter.ContactsPageListAdapter;
import com.example.test.service.FragmentListener;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements FragmentListener {
    //为Activity类添加一个Retrofit对象
    private Retrofit retrofit;
    //保存我自己的信息
    public static ContactsPageListAdapter.ContactInfo myInfo;
    public static String serverHostURL = "http://10.0.2.2:8080/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //创建Retrofit对象
        retrofit=new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/")
                //本来接口方法返回的是Call，由于现在的返回类型变成了observable,所以必须设置Call适配器将Observable与Call结合起来
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                //Json数据自动转换
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //将LoginFragment加入，作为首页
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        LoginFragment loginFragment=new LoginFragment();
        fragmentTransaction.add(R.id.fragment_container,loginFragment);
        fragmentTransaction.commit();
    }

    @Override
    public Retrofit getRetrofit() {
        return retrofit;
    }
}