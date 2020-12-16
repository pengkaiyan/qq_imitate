package com.example.test;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;

public class LoginFragment extends Fragment {


    private static ConstraintLayout layoutContext;//正常部分内容，是一个ConstraintLayout
    private LinearLayout layoutHistory;//历史菜单部分，是一个LinearLayout
    private EditText editTextQQNum;//获取QQ号输入框

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        layoutContext = v.findViewById(R.id.layoutContext);
        layoutHistory = v.findViewById(R.id.layoutHistory);
        editTextQQNum = v.findViewById(R.id.editTextQQNum);
        View buttonLogin = v.findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                MainFragment mainFragment = new MainFragment();
                //替换掉FrameLayout中现有的Fragment
                fragmentTransaction.replace(R.id.fragment_container, mainFragment);
                //将这次切换放入后退栈中，这样可以在点击后退键时自动返回上一个页面
                fragmentTransaction.addToBackStack("login");
                fragmentTransaction.commit();
            }
        });
        //响应下拉箭头的点击事件,弹出登录历史记录菜单
        v.findViewById(R.id.textViewHistory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutContext.setVisibility(View.INVISIBLE);
                layoutHistory.setVisibility(View.VISIBLE);
                //创建3条历史记录菜单项，添加到layoutHistory中
                for (int i = 0; i < 3; i++) {
                    View layoutItem = getActivity().getLayoutInflater().inflate(R.layout.login_history_item, null);
                    //当点击历史记录中的某一条时，把历史记录中的QQ号输入到QQ登录框中
                    layoutItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            editTextQQNum.setText("0123456789");
                            layoutHistory.setVisibility(View.INVISIBLE);
                            layoutContext.setVisibility(View.VISIBLE);
                        }
                    });
                    layoutHistory.addView(layoutItem);
                }
                //使用动画显示历史记录
                AnimationSet set = (AnimationSet) AnimationUtils.loadAnimation(getContext(), R.anim.login_history_anim);
                layoutHistory.startAnimation(set);
            }
        });
        //当点击历史登录记录菜单之外的区域时应把历史菜单收起来
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (layoutHistory.getVisibility() == View.VISIBLE) {
                    layoutContext.setVisibility(View.VISIBLE);
                    layoutHistory.setVisibility(View.INVISIBLE);
                }
            }
        });
        return v;
    }
}