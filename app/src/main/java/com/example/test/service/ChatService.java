package com.example.test.service;

import com.example.test.Message;
import com.example.test.ServerResult;
import com.example.test.adapter.ContactsPageListAdapter;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ChatService {
    @GET("/apis/login")// apis/login时服务器中响应登录请求的路径
    //此方法返回一个Observable类型
    Observable<ServerResult<ContactsPageListAdapter.ContactInfo>> requestLogin(@Query("name") String name,@Query("password") String password);
    @GET("/apis/get_contacts") Observable<ServerResult<List<ContactsPageListAdapter.ContactInfo>>>getContacts();

    @POST("/apis/upload_message")Observable<ServerResult> uploadMessage(@Body Message msg);
    @GET("/apis/get_messages")Observable<ServerResult<List<Message>>> getMessagesFromIndex(@Query("after") int index);
}
