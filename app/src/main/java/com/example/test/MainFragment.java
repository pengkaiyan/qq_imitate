package com.example.test;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.test.adapter.ContactsPageListAdapter;
import com.example.test.adapter.MessagePageListAdapter;
import com.niuedu.ListTree;

public class MainFragment extends Fragment {

    private ViewGroup rootView;
    private TabLayout tabLayout;
    ViewPager viewPager;
    //用一个数组保存三个View的实例
    private View listViews[]={null,null,null};
  //  private RecyclerView listViews[]=new RecyclerView[3];
  //创建一棵树
    private static ListTree tree=new ListTree();
    public static ListTree getContactsTree(){
        return tree;
    }
    public MainFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.rootView= (ViewGroup) inflater.inflate(R.layout.fragment_main,container,false);
        //获取ViewPAge实例，将Adapter设置给它
        viewPager =this.rootView.findViewById(R.id.viewPage);
        viewPager.setAdapter(new ViewPageAdapter());
        //获取TabLayout并配置给它
        tabLayout=this.rootView.findViewById(R.id.tabLayout);
         tabLayout.setupWithViewPager(viewPager);
         RecyclerView v1=new RecyclerView(getContext());
      //   View v2=getLayoutInflater().inflate(R.layout.contacts_page_layout,null);
        View v2=createContactsPage();
         RecyclerView v3=new RecyclerView(getContext());
         listViews[0]=v1;
         listViews[1]=v2;
         listViews[2]=v3;
       /* listViews[0]=new RecyclerView(getContext());
        listViews[1]=new RecyclerView(getContext());
        listViews[2]=new RecyclerView(getContext());*/
        // LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        v1.setLayoutManager(new LinearLayoutManager(getContext()));
       // RecyclerView recyclerViewInV2 =v2.findViewById(R.id.contactListView);
       // recyclerViewInV2.setLayoutManager(new LinearLayoutManager(getContext()));
        //为RecyclerView设置Adapter
       // listViews[0].setAdapter(new MessagePageListAdapter(getActivity()));
        v1.setAdapter(new MessagePageListAdapter(getActivity()));
      //  recyclerViewInV2.setAdapter(new ContactsPageListAdapter(null));
        //响应+号图标事件，显示蒙板和气泡菜单
        TextView popMenu=this.rootView.findViewById(R.id.textViewPopMenu);
        popMenu.setOnClickListener(new View.OnClickListener() {
            //蒙板
            View mask;
            //把弹出窗口作为成员变量
            PopupWindow pop;
            @Override
            public void onClick(View view) {
                //向Fragment容器FrameLayout中加入一个View作为上层容器和遮罩
                if (mask == null) {
                    mask=new View(getContext());
                    mask.setBackgroundColor(Color.DKGRAY);
                    mask.setAlpha(0.5f);
                    //响应蒙板View的点击事件
                    mask.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            MainFragment.this.rootView.removeView(mask);
                            //隐藏弹出窗口
                            pop .dismiss();
                        }
                    });
                }
                 //将蒙板View添加到Fragment的根View   FrameLayout中
                MainFragment.this.rootView.addView(mask, FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT);
                //如果弹出窗口还未创建，则创建弹出窗口
                if (pop == null) {
                    //创建PopupWindow，用于承载气泡菜单
                    pop=new PopupWindow(getActivity());
                    //加载菜单项资源layout\pop_menu_layout.xml
                    LinearLayout menu=(LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.pop_menu_layout,null);
                    //计算一下菜单layout的实际大小然后获取
                    menu.measure(0,0);
                    int w=menu.getMeasuredWidth();
                    int h=menu.getMeasuredHeight();
                    //设置Windows的宽和高 //设置窗口的大小
                    pop .setWidth(60+w);
                    pop.setHeight(h+60);
                    pop.setContentView(menu);//设置Windows中要显示的View
                    Drawable drawable=getResources().getDrawable(R.drawable.pop_bk);
                    pop.setBackgroundDrawable(drawable);//设置气泡图像作为Windows的背景
                    pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            MainFragment.this.rootView.removeView(mask);
                        }
                    });
                    //设置窗口出现时获取焦点，这样在按下返回键时，窗口消失
                    pop.setFocusable(true);
                }
                //显示窗口
                pop.showAsDropDown(view,pop.getWidth()+30,-10);
            }
        });
        //响应左上角的用户头像图标点击事件，显示抽屉页面
        ImageView headImage = rootView.findViewById(R.id.headImage);
        headImage.setOnClickListener(v -> {
            //创建抽屉页面
            final View drawerLayout = getActivity().getLayoutInflater().inflate(
                    R.layout.drawer_layout, rootView, false);

            //获取原内容的根控件
            final View contentLayout = rootView.findViewById(R.id.contentLayout);

            //动画持续的时间
            final int duration = 400;

            //先计算一下消息页面中，左边一排图像的大小，在界面构建器中设置的是dp
            //在代码中只能用像素，所以这里要换算一下，因为不同的屏幕分辩率，dp对应
            //的像素数是不同的
            int messageImageWidth = Utils.dip2px(getActivity(), 60);
            //计算抽屉页面的宽度，rootView是FrameLayout，
            //利用getWidth()即可获得它当前的宽度
            final int drawerWidth = rootView.getWidth() - messageImageWidth;
            //设置抽屉页面的宽度
            drawerLayout.getLayoutParams().width = drawerWidth;
            //将抽屉页面加入FrameLayout中
            rootView.addView(drawerLayout);

            //创建蒙板View
            final View maskView = new View(getContext());
            maskView.setBackgroundColor(Color.GRAY);
            //必须将其初始透明度设为完全透明
            maskView.setAlpha(0);
            //当点击蒙板View时，隐藏抽屉页面
            maskView.setOnClickListener(v4 -> {
                //动画反着来，让抽屉消失

                //创建动画，移动原内容，从0位置移动抽屉页面宽度的距离（注意其宽度不变）
                ObjectAnimator animatorContent = ObjectAnimator.ofFloat(
                        contentLayout,
                        "translationX",
                        drawerWidth, 0);

                //移动蒙板的动画
                ObjectAnimator animatorMask = ObjectAnimator.ofFloat(
                        maskView,
                        "translationX",
                        drawerWidth, 0);
                //响应此动画的刷新事件，在其中改变原页面的背景色，使其逐渐变暗
                animatorMask.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    //响应动画更新的方法
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        //计算当前进度比例,最后除以2的原因是因为透明度最终只降到一半,约127
                        float progress = (animation.getCurrentPlayTime() / (float) duration);
                        maskView.setAlpha(1 - progress);
                    }
                });

                //创建动画，让抽屉页面向右移，注意它是从左移出来的，
                //所以其初始位值设置为-drawerWidth/2，即有一半位于屏幕之外。
                ObjectAnimator animatorDrawer = ObjectAnimator.ofFloat(
                        drawerLayout,
                        "translationX",
                        0, -drawerWidth / 2);

                //创建动画集合，同时播放三个动画
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(animatorContent, animatorMask, animatorDrawer);
                animatorSet.setDuration(duration);
                //设置侦听器，主要侦听动画关闭事件
                animatorSet.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) { }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        //动画结束，将蒙板和抽屉页面删除
                        rootView.removeView(maskView);
                        rootView.removeView(drawerLayout);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) { }

                    @Override
                    public void onAnimationRepeat(Animator animation) { }
                });
                animatorSet.start();
            });
            rootView.addView(maskView);

            //把它搞到最上层，这样在移动时能一直看到它（QQ就是这个效果）
            contentLayout.bringToFront();
            //再将蒙板View搞到最上层
            maskView.bringToFront();
            //创建动画，移动原内容，从0位置移动抽屉页面宽度的距离（注意其宽度不变）
            ObjectAnimator animatorContent = ObjectAnimator.ofFloat(contentLayout,
                    "translationX", 0, drawerWidth);

            //移动蒙板的动画
            ObjectAnimator animatorMask = ObjectAnimator.ofFloat(maskView,
                    "translationX", 0, drawerWidth);

            //响应此动画的刷新事件，在其中改变原页面的背景色，使其逐渐变暗
            //响应动画更新的方法
            animatorMask.addUpdateListener(animation -> {
                //计算当前进度比例,最后除以2的原因是因为透明度最终只降到一半,约127
                float progress = (animation.getCurrentPlayTime() / (float) duration) / 2;
                maskView.setAlpha(1-progress);
            });

            //创建动画，让抽屉页面向右移，注意它是从左移出来的，
            //所以其初始位值设置为-drawerWidth/2，即有一半位于屏幕之外。
            ObjectAnimator animatorDrawer = ObjectAnimator.ofFloat(drawerLayout,
                    "translationX", -drawerWidth / 2, 0);

            //创建动画集合，同时播放三个动画
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(animatorContent, animatorMask, animatorDrawer);
            animatorSet.setDuration(duration);
            animatorSet.start();
        });
        //设置下拉刷新控件的响应事件
        final SwipeRefreshLayout refreshLayout=rootView.findViewById(R.id.refreshLayout);
        //响应它发出的事件
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //网络刷操作

                //刷新完成，隐藏刷新控件
                refreshLayout.setRefreshing(false);
            }
        });
        return rootView;
    }
    //创建并实现联系人界面
    private View createContactsPage(){
        View v=getLayoutInflater().inflate(R.layout.contacts_page_layout,null);

        ContactsPageListAdapter.GroupInfo group1=new ContactsPageListAdapter.GroupInfo("特别关心",0);
        ContactsPageListAdapter.GroupInfo group2=new ContactsPageListAdapter.GroupInfo("QQ好友",1);
        ContactsPageListAdapter.GroupInfo group3=new ContactsPageListAdapter.GroupInfo("朋友",0);
        ContactsPageListAdapter.GroupInfo group4=new ContactsPageListAdapter.GroupInfo("家人",0);
        ContactsPageListAdapter.GroupInfo group5=new ContactsPageListAdapter.GroupInfo("同学",0);
        ListTree.TreeNode groupNode1=tree.addNode(null,group1,R.layout.contacts_group_item);
        ListTree.TreeNode groupNode2=tree.addNode(null,group2,R.layout.contacts_group_item);
        ListTree.TreeNode groupNode3=tree.addNode(null,group3,R.layout.contacts_group_item);
        ListTree.TreeNode groupNode4=tree.addNode(null,group4,R.layout.contacts_group_item);
        ListTree.TreeNode groupNode5=tree.addNode(null,group5,R.layout.contacts_group_item);
        //第二层，联系人信息
        //头像
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.contacts_normal);
        //联系人1
        ContactsPageListAdapter.ContactInfo contactInfo1=new ContactsPageListAdapter.ContactInfo(bitmap,"张三","[在线]我是张三");
        //头像
     bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.contacts_normal);
        //联系人1
        ContactsPageListAdapter.ContactInfo contactInfo2=new ContactsPageListAdapter.ContactInfo(bitmap,"王三","[离线]我是王三");
        //添加两个联系人
        tree.addNode(groupNode2,contactInfo1,R.layout.contacts_contact_item);
        tree.addNode(groupNode2,contactInfo2,R.layout.contacts_contact_item);
        //获取页面里的RecycleView，为它创建Adapter
        RecyclerView recyclerView=v.findViewById(R.id.contactListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ContactsPageListAdapter(tree));
        return  v;
    }
    //为ViewPager派生一个适配器类
    class ViewPageAdapter extends PagerAdapter {
        public ViewPageAdapter() {
        }

        @Nullable
        @Override//这个方法是为了在切换导航栏时，显示下面的TabItem
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
               return makeTabItemTitle("消息",R.drawable.message_normal);
                //               return "消息";
            }else if (position==1){
             return makeTabItemTitle("联系人",R.drawable.contacts_normal);
                //      return "联系人";
            }else if (position==2){
           return makeTabItemTitle("动态",R.drawable.space_normal);
                //       return "动态";
            }
            return null;
        }

        @Override
        public int getCount() {
            return listViews.length;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
           View v=listViews[position];
            //必须加入容器中
            container.addView(v);
            return v;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View)object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }
          //此方法为Tab Item创建文本与图像混合的标题字符串
        public CharSequence makeTabItemTitle(String title,int iconResId){
            Drawable image=getResources().getDrawable(iconResId);
            image.setBounds(0,0,40,40);
            SpannableString sb=new SpannableString(" \n"+title);
            ImageSpan imageSpan=new ImageSpan(image,ImageSpan.ALIGN_BASELINE);
            sb.setSpan(imageSpan,0,1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;
        }
    }
}