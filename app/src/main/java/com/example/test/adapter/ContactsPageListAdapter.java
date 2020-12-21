package com.example.test.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test.ChatActivity;
import com.example.test.R;
import com.niuedu.ListTree;
import com.niuedu.ListTreeAdapter;

import java.io.Serializable;


public class ContactsPageListAdapter extends ListTreeAdapter<ListTreeAdapter.ListTreeViewHolder> {


    public ContactsPageListAdapter(ListTree tree) {
        super(tree);
    }

    public ContactsPageListAdapter(ListTree tree, Bitmap expandIcon, Bitmap collapseIcon) {
        super(tree, expandIcon, collapseIcon);
    }

    @Override
    protected ListTreeViewHolder onCreateNodeView(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        if (viewType == R.layout.contacts_group_item) {
            View view=inflater.inflate(viewType,parent,true);
            return new GroupViewHolder(view);
        }else if (viewType==R.layout.contacts_contact_item){
            View view=inflater.inflate(viewType,parent,true);
            return new ContactViewHolder(view);
        }
        return null;
    }

    protected void onBindNodeViewHolder(ListTreeAdapter.ListTreeViewHolder viewHoler, int position) {
                //获取行控件
        View view=viewHoler.itemView;
        //获取这一行树对象中的对应节点
        ListTree.TreeNode node=tree.getNodeByPlaneIndex(position);
        if (node.getLayoutResId()==R.layout.contacts_group_item){
            GroupInfo info=(GroupInfo) node.getData();
            GroupViewHolder groupViewHolder = (GroupViewHolder)viewHoler;
            groupViewHolder.textViewTitle.setText(info.getTitle());
            groupViewHolder.textViewCount.setText(info.getOnlineCount()+"/"+node.getChildrenCount());
        }else if (node.getLayoutResId()==R.layout.contacts_contact_item){
            ContactInfo info=(ContactInfo) node.getData();
            ContactViewHolder contactViewHolder=(ContactViewHolder)viewHoler;
            contactViewHolder.imageViewHead.setImageBitmap(info.getAvatar());
            contactViewHolder.textViewTitle.setText(info.getName());
            contactViewHolder.textViewDetail.setText(info.getStatus());
        }
    }

    //存放组数据
    static public class GroupInfo{
        private String title;//组标题
        private int onlineCount;//该组一共有多少人在线

        public GroupInfo(String title, int onlineCount) {
            this.title = title;
            this.onlineCount = onlineCount;
        }

        public String getTitle() {
            return title;
        }

        public int getOnlineCount() {
            return onlineCount;
        }
    }
    //存放组中的联系人数据
    static public class ContactInfo implements Serializable {
        private Bitmap avatarURL;//联系人的头像URL
        private String name;//联系人的名字
        private String  status;//联系人的在线状态

        public Bitmap getAvatar() {
            return avatarURL;
        }

        public String getName() {
            return name;
        }

        public String getStatus() {
            return status;
        }

        public ContactInfo(Bitmap avatar, String name, String status) {
            this.avatarURL = avatar;
            this.name = name;
            this.status = status;
        }
    }
    //组ViewHolder
    class GroupViewHolder extends ListTreeViewHolder{
        TextView textViewTitle;//显示标题的控件
        TextView textViewCount;//显示好友在线人数的控件
        public GroupViewHolder(View itemView) {
            super(itemView);
           textViewTitle= itemView. findViewById(R.id.textViewTitle);
            textViewCount=itemView. findViewById(R.id.textViewCount);
        }
    }
    //联系人ViewHolder
    class ContactViewHolder extends ListTreeViewHolder{
        ImageView imageViewHead;//联系人的头像
        TextView textViewTitle;//显示联系人的名字的控件
        TextView textViewDetail;//显示好友状态的控件
        public ContactViewHolder(View itemView) {
            super(itemView);
            imageViewHead=(ImageView)itemView. findViewById(R.id.imageViewHead);
            textViewTitle=(TextView)itemView. findViewById(R.id.textViewTitle);
            textViewDetail=(TextView)itemView. findViewById(R.id.textViewDetail);
            //当点击这一行时，开始聊天
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //进入聊天界面
                    Intent intent=new Intent(itemView.getContext(), ChatActivity.class);
                    //将对方的名字作为参数传过去
                    intent.putExtra("contact_name",(String)v.getTag());
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}