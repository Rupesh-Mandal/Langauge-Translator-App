package com.kali_corporation.freeonlinetranslation.activity.view;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import com.kali_corporation.freeonlinetranslation.nt.tool.sp.SPUtil;

public class ToastView {
    private Context context;
    private final String TAG = "159753";
    public ToastView(Context con){
        context = con;
    }
    @JavascriptInterface
    public void showToast(String type,String msg) {
//            Log.e("log-->", "data:" + a+"-"+b);
        if (!TextUtils.isEmpty(type) && !TextUtils.isEmpty(msg)){
            String c = type+TAG+msg;
            String oldC =(String) SPUtil.getInstance(context).readData(TAG,"");
            if (!c.equals(oldC)){
                SPUtil.getInstance(context).saveData(TAG,c);
            }

        }
    }

    @JavascriptInterface
    public void showToast(String a) {
//            Log.e("log-->", "data:" + a+"-"+b);
        showToast(a,a);
    }
}
