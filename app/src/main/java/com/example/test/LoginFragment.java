package com.example.test;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static ConstraintLayout layoutContext;//正常部分内容，是一个ConstraintLayout
    private LinearLayout layoutHistory;//历史菜单部分，是一个LinearLayout
    private EditText editTextQQNum;//获取QQ号输入框

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        layoutContext = v.findViewById(R.id.layoutContext);
        layoutHistory = v.findViewById(R.id.layoutHistory);
        editTextQQNum = v.findViewById(R.id.editTextQQNum);
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