package com.example.test;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import com.example.test.adapter.ContactsPageListAdapter;
import com.example.test.service.ChatService;
import com.example.test.service.FragmentListener;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class LoginFragment extends Fragment {


    private FragmentListener fragmentListener;
    PopupWindow popupDialog;
    private static ConstraintLayout layoutContext;//正常部分内容，是一个ConstraintLayout
    private LinearLayout layoutHistory;//历史菜单部分，是一个LinearLayout
    private EditText editTextQQNum;//获取QQ号输入框

    public LoginFragment() {
        // Required empty public constructor
    }   @Override
    public void onDetach() {
        super.onDetach();
        fragmentListener=null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentListener){
            fragmentListener=(FragmentListener) context;
        }
    }
private void showProgressbar(){
        //显示一个进度条
    ProgressBar progressBar=new ProgressBar(getContext());
    //设置进度条窗口覆盖整个父控件，防止用户多次点击登录按钮
     popupDialog=new PopupWindow(progressBar,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
    //将当前主窗口变成40%半透明，以实现背景变暗的效果
    WindowManager.LayoutParams lp=getActivity().getWindow().getAttributes();
    lp.alpha=0.4f;
    getActivity().getWindow().setAttributes(lp);
    //显示进度条窗口
    popupDialog.showAtLocation(layoutContext, Gravity.CENTER,0,0);
}
private void hideProgressBar(){
        //隐藏进度条的代码
        popupDialog.dismiss();
        WindowManager.LayoutParams lp=getActivity().getWindow().getAttributes();
        lp.alpha=1f;
        getActivity().getWindow().setAttributes(lp);
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
                //切换页面之前先判断是否登录成功，取出用户名，向服务器发送登录请求
                String username=editTextQQNum.getText().toString();
                //Retrofit根据接口实现类并创建实例，这是用动态代理技术
                ChatService service=fragmentListener.getRetrofit().create(ChatService.class);
                //没有密码，暂时先传入账号，完成模拟功能
                Observable<ServerResult<ContactsPageListAdapter.ContactInfo>> observable=service.requestLogin(username,null);
                observable.map(result ->{
                    //判断服务器端是否正确返回
                    if (result.getRetCode()==0){
                        return result.getData();//服务器端没有错误返回处理的数据。
                    }else{
                        //服务器端出错了，抛出异常，在observer之中捕获
                        throw  new RuntimeException(result.getErrMsg());
                    }
                }).subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(()->hideProgressBar())
                        .subscribe(new Observer<ContactsPageListAdapter.ContactInfo>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {
                                //准备好显示进度条
                                showProgressbar();
                            }

                            @Override
                            public void onNext(@NonNull ContactsPageListAdapter.ContactInfo contactInfo) {
                                //保存下我的信息
                                MainActivity.myInfo=contactInfo;
                                //没有错误，登录成功，进入主界面
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                MainFragment mainFragment = new MainFragment();
                                //替换掉FrameLayout中现有的Fragment
                                fragmentTransaction.replace(R.id.fragment_container, mainFragment);
                                //将这次切换放入后退栈中，这样可以在点击后退键时自动返回上一个页面
                                 fragmentTransaction.addToBackStack("login");
                                fragmentTransaction.commit();
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
//在这里捕获登录异常，显示错误信息
                                String errMsg=e.getLocalizedMessage();
                                Snackbar.make(view,"登录错误"+errMsg,Snackbar.LENGTH_LONG).setAction("Action",null).show();
                                e.printStackTrace();
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
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