package com.example.test;

import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

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
      /*  //创建Retrofit对象
        retrofit=new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/")
                //本来接口方法返回的是Call，由于现在的返回类型变成了observable,所以必须设置Call适配器将Observable与Call结合起来
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                //Json数据自动转换
                .addConverterFactory(GsonConverterFactory.create())
                .build();*/
        //将LoginFragment加入，作为首页
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        LoginFragment loginFragment=new LoginFragment();
        fragmentTransaction.add(R.id.fragment_container,loginFragment);
        fragmentTransaction.commit();
        //选将网络连接搞出来
        getRetrofit();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    public Retrofit getRetrofit() {
        if(retrofit==null){
            //从本地读取server host name，
            SharedPreferences preferences = getApplicationContext().getSharedPreferences(
                    "qqapp", MODE_PRIVATE);
            serverHostURL = preferences.getString("server_addr", "");
            if (serverHostURL.isEmpty()){
                //弹出输入对话框，让用户设置server地址
                showServerAddressSetDlg();
            }else {
                try {
                    //创建Retrofit对象
                    retrofit = new Retrofit.Builder()
                            .baseUrl(serverHostURL)
                            //本来接口方法返回的是Call，由于现在返回类型变成了Observable，
                            //所以必须设置Call适配器将Observable与Call结合起来
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            //Json数据自动转换
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }catch (Exception e){
                    Snackbar.make(findViewById(R.id.fragment_container),
                            e.getLocalizedMessage(),Snackbar.LENGTH_LONG);
                    showServerAddressSetDlg();
                }
            }
        }
        return retrofit;
    }

    @Override
    public void showServerAddressSetDlg() {
        //弹出输入对话框，让用户设置server地址
        EditText editText = new EditText(this);
        editText.setText("http://10.0.2.2:8080");
        AlertDialog.Builder inputDialog = new AlertDialog.Builder(this);
        inputDialog.setTitle("请输入服务器地址").setView(editText);
        inputDialog.setPositiveButton("确定",
                (dialog, which) -> {
                    serverHostURL = editText.getText().toString();
                    //将服务端地址保存到本地
                    SharedPreferences preferences= getApplicationContext().getSharedPreferences("qqapp", MODE_PRIVATE);
                    SharedPreferences.Editor edit = preferences.edit();
                    edit.putString("server_addr",serverHostURL);
                    edit.commit();
                    //创建Retrofit对象
                    retrofit = new Retrofit.Builder()
                            .baseUrl(serverHostURL)
                            //本来接口方法返回的是Call，由于现在返回类型变成了Observable，
                            //所以必须设置Call适配器将Observable与Call结合起来
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            //Json数据自动转换
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }).show();
    }
}