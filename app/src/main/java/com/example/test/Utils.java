package com.example.test;

import android.content.Context;

public final class Utils{
    //根据手机的分辨率从dp的单位转成px像素
    public static int dip2px(Context context, float dpValue){
        final float scale=context.getResources().getDisplayMetrics().density;
        return (int) (dpValue*scale+0.5f);
    }
    //根据手机的分辨率从px像素的单位转换成dp
    public static int px2dip(Context context,float pxValue){
        final float scale =context.getResources().getDisplayMetrics().density;
        return  (int)(pxValue / scale+0.5f);
    }
}