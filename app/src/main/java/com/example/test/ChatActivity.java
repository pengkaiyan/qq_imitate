package com.example.test;


import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
        //存放消息数据的类

    private List<ChatMessage> chatMessages=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        //获取启动此Activity时传过来的数据
        //在启动聊天界面时，通过此方法把对方的名字传过来
        String contactName=getIntent().getStringExtra("contact_name");
        if (contactName!=null){
            toolbar.setTitle(contactName);
        }
        setSupportActionBar(toolbar);
        //设置显示动作栏上的返回图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //获取RecyclerView控件并设置适配器
        RecyclerView recyclerView=findViewById(R.id.chatMessageListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ChatMessagesAdapter());
        //响应按钮发出消息
        findViewById(R.id.buttonSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //现在还不能真正地发出消息，把消息放在chatMessages中，显示出来即可
                //从EditText控件中取出消息
                EditText editText=findViewById(R.id.editMessage);
                String msg=editText.getText().toString().trim();
                //添加到集合中，从而能在RecyclerView中显示
                ChatMessage chatMessage=new ChatMessage("我",new Date(),msg,true);
                chatMessages.add(chatMessage);
                //同时把对方的话也加上，对方只回复一句话
                chatMessage=new ChatMessage("对方",new Date(),"你是谁？？？",false);
                chatMessages.add(chatMessage);

                //通知Recyclerview更新一行数据
                recyclerView.getAdapter().notifyItemRangeInserted(chatMessages.size()-2,2);
                //让RecyclerView向下滚动，显示最新消息
                recyclerView.scrollToPosition(chatMessages.size()-1);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();
        if (id==android.R.id.home){
            //当点击动作栏上的返回键图标时关闭自己，返回来时的界面
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public static class ChatMessage{
        String contactName;//联系人名字
        Date time;//消息的时间
        String content;//消息的内容
        boolean isMe;//判断消息是否是我发出的

        public ChatMessage(String contactName, Date time, String content, boolean isMe) {
            this.contactName = contactName;
            this.time = time;
            this.content = content;
            this.isMe = isMe;
        }
    }
    //为RecyclerView提供数据的适配器
    public class ChatMessagesAdapter extends
            RecyclerView.Adapter<ChatMessagesAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //参数viewType即行的Layout资源Id，由getItemViewType()的返回值决定的
            View itemView = getLayoutInflater().inflate(viewType, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            ChatMessage message = chatMessages.get(position);
            holder.textView.setText(message.content);
        }

        @Override
        public int getItemCount() {
            return chatMessages.size();
        }

        //有两种行layout，所以Override此方法
        @Override
        public int getItemViewType(int position) {
            ChatMessage message = chatMessages.get(position);
            if (message.isMe) {
                //如果是我的，靠右显示
                return R.layout.chat_message_right_item;
            } else {
                //对方的，靠左显示
                return R.layout.chat_message_left_item;
            }
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView textView;
            private ImageView imageView;

            public MyViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.textView);
                imageView = itemView.findViewById(R.id.imageView);
            }
        }
    }
}