package com.example.test.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.test.R;

public class MessagePageListAdapter extends RecyclerView.Adapter<MessagePageListAdapter.MyViewHolder> {
    private Activity activity;//用于获取Activity

    //创建一个带参数的构造方法，通过参数可以把Activity传过来
    public MessagePageListAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public MessagePageListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //从Layout资源加载行View
        LayoutInflater inflater=activity.getLayoutInflater();
        View view=null;
        if (i== R.layout.message_list_item_search){
            view=inflater.inflate(R.layout.message_list_item_search,viewGroup,false);
        }else {
            view    =inflater.inflate(R.layout.message_list_item_normal,viewGroup,false);
        }
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        //用于绑定数据
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return R.layout.message_list_item_search;
        }
        //只有最顶端是搜索栏，其他的都是消息栏
        return R.layout.message_list_item_normal;
    }

    //创建ViewHolder内部类
   protected class MyViewHolder extends RecyclerView.ViewHolder{
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
