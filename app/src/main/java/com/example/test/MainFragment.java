package com.example.test;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.test.adapter.MessagePageListAdapter;

import java.util.ArrayList;

public class MainFragment extends Fragment {

    private ViewGroup rootView;
    private TabLayout tabLayout;
    ViewPager viewPager;
private RecyclerView listViews[]=new RecyclerView[3];
    public MainFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       // View v=inflater.inflate(R.layout.fragment_main, container, false);
        this.rootView= (ViewGroup) inflater.inflate(R.layout.fragment_main,container,false);
        //获取ViewPAge实例，将Adapter设置给它
        viewPager =this.rootView.findViewById(R.id.viewPage);
        viewPager.setAdapter(new ViewPageAdapter());
        //获取TabLayout并配置给它
        tabLayout=this.rootView.findViewById(R.id.tabLayout);
         tabLayout.setupWithViewPager(viewPager);
        listViews[0]=new RecyclerView(getContext());
        listViews[1]=new RecyclerView(getContext());
        listViews[2]=new RecyclerView(getContext());
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        listViews[0].setLayoutManager(layoutManager);
        //设置不同的颜色，用于测试
        // listViews[0].setBackgroundColor(Color.RED);
        listViews[1].setBackgroundColor(Color.GREEN);
        listViews[2].setBackgroundColor(Color.BLUE);
        //为RecyclerView设置Adapter
        listViews[0].setAdapter(new MessagePageListAdapter(getActivity()));
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
        return rootView;
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