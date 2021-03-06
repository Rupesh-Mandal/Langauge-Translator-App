package com.kali_corporation.freeonlinetranslation.nt.model;

import android.app.Application;

import androidx.lifecycle.LifecycleOwner;

import com.google.gson.JsonSyntaxException;
import com.hjq.gson.factory.GsonFactory;
import com.hjq.http.EasyLog;
import com.hjq.http.config.IRequestApi;
import com.hjq.http.config.IRequestHandler;
import com.hjq.http.exception.CancelException;
import com.hjq.http.exception.DataException;
import com.hjq.http.exception.HttpException;
import com.hjq.http.exception.ResponseException;
import com.hjq.http.exception.TimeoutException;
import com.hjq.http.exception.TokenException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;

import okhttp3.Headers;
import okhttp3.Response;
import okhttp3.ResponseBody;

public final class RequestHandler implements IRequestHandler {

    private static final String ERROR="error";

    private final Application mApplication;

    public RequestHandler(Application application) {
        mApplication = application;
    }

    @Override
    public Object requestSucceed(LifecycleOwner lifecycle, IRequestApi api, Response response, Type type) throws Exception {
        if (Response.class.equals(type)) {
            return response;
        }

        if (!response.isSuccessful()) {
            // 返回响应异常
            throw new ResponseException(ERROR + "，responseCode：" + response.code() + "，message：" + response.message(), response);
        }

        if (Headers.class.equals(type)) {
            return response.headers();
        }

        ResponseBody body = response.body();
        if (body == null) {
            return null;
        }

        if (InputStream.class.equals(type)) {
            return body.byteStream();
        }

        String text;
        try {
            text = body.string();
        } catch (IOException e) {
            // 返回结果读取异常
            throw new DataException(ERROR, e);
        }

        // 打印这个 Json 或者文本
        EasyLog.json(text);

        if (String.class.equals(type)) {
            return text;
        }

        if (JSONObject.class.equals(type)) {
            try {
                // 如果这是一个 JSONObject 对象
                return new JSONObject(text);
            } catch (JSONException e) {
                throw new DataException(ERROR, e);
            }
        }

        if (JSONArray.class.equals(type)) {
            try {
                // 如果这是一个 JSONArray 对象
                return new JSONArray(text);
            } catch (JSONException e) {
                throw new DataException(ERROR, e);
            }
        }

        final Object result;

        try {
            result = GsonFactory.getSingletonGson().fromJson(text, type);
        } catch (JsonSyntaxException e) {
            // 返回结果读取异常
//            throw new DataException(mApplication.getString(R.string.http_data_explain_error), e);
            return text;
        }

//        if (result instanceof HttpData) {
//            HttpData<?> model = (HttpData<?>) result;
//            if (model.getCode() == 0) {
//                // 代表执行成功
//                return result;
//            } else if (model.getCode() == 1001) {
//                // 代表登录失效，需要重新登录
//                throw new TokenException(mApplication.getString(R.string.http_token_error));
//            }
//            // 代表执行失败
//            throw new ResultException(model.getMessage(), model);
//        }
        return text;
//        return result;
    }

    @Override
    public Exception requestFail(LifecycleOwner lifecycle, IRequestApi api, Exception e) {
        // 判断这个异常是不是自己抛的
        if (e instanceof HttpException) {
            if (e instanceof TokenException) {
                // 登录信息失效，跳转到登录页
            }

            return e;
        }

        if (e instanceof SocketTimeoutException) {
            return new TimeoutException(ERROR, e);
        }

//        if (e instanceof UnknownHostException) {
//            NetworkInfo info = ((ConnectivityManager) mApplication.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
//            // 判断网络是否连接
//            if (info != null && info.isConnected()) {
//                // 有连接就是服务器的问题
//                return new ServerException(mApplication.getString(R.string.http_server_error), e);
//            }
//            // 没有连接就是网络异常
//            return new NetworkException(mApplication.getString(R.string.http_network_error), e);
//        }

        if (e instanceof IOException) {
            //e = new CancelException(context.getString(R.string.http_request_cancel), e);
            return new CancelException("", e);
        }

        return new HttpException(e.getMessage(), e);
    }
}
