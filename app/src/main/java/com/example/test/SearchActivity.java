package com.example.test;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.test.adapter.ContactsPageListAdapter;
import com.niuedu.ListTree;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private List<MyContactInfo> searchResultList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initSearching();
    }
    private void initSearching(){
        //搜索控件
        SearchView searchView=findViewById(R.id.searchView);
        //不以图标的形式显示
        searchView.setIconifiedByDefault(false);
        //取消按钮
        TextView cancelView=findViewById(R.id.tvCancel);
        final RecyclerView resultListView=findViewById(R.id.resultListView);
        resultListView.setLayoutManager(new LinearLayoutManager((this)));
        resultListView.setAdapter(new ResultListAdapter());
        //响应SearchView的文本输入事件，以实现搜索
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //根据newText中的字符串进行搜索，查找其中包含关键字的节点
                ListTree tree=MainFragment.getContactsTree();
                //必须每次都清空保存结果的集合对象
                searchResultList.clear();

                //只有当要搜索的字符串非空时才遍历列表
                if (! newText.equals("")){
                    //遍历整个树
                    ListTree.EnumPos pos=tree.startEnumNode();
                    while(pos!=null){
                        //如果这个节点中存储的是联系人信息
                        ListTree.TreeNode node=tree.getNodeByEnumPos(pos);
                        if(node.getData() instanceof  ContactsPageListAdapter.ContactInfo){
                            //获取联系人信息对象
                            ContactsPageListAdapter.ContactInfo contactInfo=(ContactsPageListAdapter.ContactInfo)node.getData();
                            //获取联系人的组名
                            ListTree.TreeNode groupNode=node.getParent();
                            ContactsPageListAdapter.GroupInfo groupInfo=(ContactsPageListAdapter.GroupInfo)groupNode.getData();
                            String groupName=groupInfo.getTitle();
                            //查看联系人的名字中或状态中是否包含了要搜索的字符串
                            if (contactInfo.getName().contains(newText) || contactInfo.getStatus().contains(newText)){
                                //查找到了，列出联系人的信息
                                searchResultList.add(new MyContactInfo(contactInfo, groupName));
                            }
                        }
                        pos=tree.enumNext(pos);
                    }
                }
                //通知RecyclerView刷新数据
                resultListView.getAdapter().notifyDataSetChanged();
                return true;
            }
        });
    }
    class ResultListAdapter extends RecyclerView.Adapter<ResultListAdapter.MyViewHolder>{

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v=getLayoutInflater().inflate(R.layout.search_result_item,viewGroup,false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
            //获取联系人信息，设置到对应的控件中
            MyContactInfo info=searchResultList.get(i);
            myViewHolder.imageViewHead.setImageBitmap(info.info.getAvatar());
            myViewHolder.textViewName.setText(info.info.getName());
            String groupName=info.groupName;
            myViewHolder.textViewDetail.setText("来自分组"+groupName);
        }

        @Override
        public int getItemCount() {
            return searchResultList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageViewHead;
            TextView textViewName;
            TextView textViewDetail;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                imageViewHead=itemView.findViewById(R.id.imageViewHead);
                textViewName=itemView.findViewById(R.id.textViewName);
                textViewDetail=itemView.findViewById(R.id.textViewDetail);
            }
        }
    }
    //为了能保存所在组的组名，创建此类
    class MyContactInfo{
            //增加一个信息：所在组的组名
        private String groupName;
        private ContactsPageListAdapter.ContactInfo info;

        public MyContactInfo(ContactsPageListAdapter.ContactInfo info, String groupName) {
            this.groupName = groupName;
            this.info = info;
        }

        public String getGroupName() {
            return groupName;
        }
    }
}