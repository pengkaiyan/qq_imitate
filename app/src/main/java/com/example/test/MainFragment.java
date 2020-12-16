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
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MainFragment extends Fragment {

    private TabLayout tabLayout;
//   ArrayList<RecyclerView> listViews;
    View[] listViews={null,null,null};
    ViewPager viewPager;

    public MainFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listViews[0]=new RecyclerView(getContext());
        listViews[1]=new RecyclerView(getContext());
        listViews[2]=new RecyclerView(getContext());
        listViews[0].setBackgroundColor(Color.RED);
        listViews[1].setBackgroundColor(Color.GREEN);
        listViews[2].setBackgroundColor(Color.BLUE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_main, container, false);

        viewPager = v.findViewById(R.id.viewPage);
        viewPager.setAdapter(new ViewPageAdapter());
        tabLayout=v.findViewById(R.id.tabLayout);
         tabLayout.setupWithViewPager(viewPager);
         tabLayout.getTabAt(0).setIcon(R.drawable.message_normal);
         tabLayout.getTabAt(1).setIcon(R.drawable.contacts_normal);
         tabLayout.getTabAt(2).setIcon(R.drawable.space_normal);
        return v;
    }

    //为ViewPager派生一个适配器类
    class ViewPageAdapter extends PagerAdapter {
        public ViewPageAdapter() {
        }

        @Nullable
        @Override//这个方法是为了在切换导航栏时，显示下面的TabItem
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
//                return makeTabItemTitle("消息",R.drawable.message_normal);
                return "消息";
            }else if (position==1){
//                return makeTabItemTitle("联系人",R.drawable.contacts_normal);
                return "联系人";
            }else if (position==2){
//                return makeTabItemTitle("动态",R.drawable.space_normal);
                return "动态";
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
        /*  //此方法为Tab Item创建文本与图像混合的标题字符串
        public CharSequence makeTabItemTitle(String title,int iconResId){
            Drawable image=getResources().getDrawable(iconResId);
            image.setBounds(0,0,80,80);
            SpannableString sb=new SpannableString("\n"+title);
            ImageSpan imageSpan=new ImageSpan(image,ImageSpan.ALIGN_BASELINE);
            sb.setSpan(imageSpan,0,1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;
        }*/
    }
}