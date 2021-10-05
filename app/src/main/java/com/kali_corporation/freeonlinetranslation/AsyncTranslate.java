package com.kali_corporation.freeonlinetranslation;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.kali_corporation.freeonlinetranslation.model.ModelAns;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

public class AsyncTranslate extends AsyncTask<Void, Void, String> {
    String ans = "";

    /* renamed from: cn */
    Context f49cn;
    String from;
    OnResponse onResponse;

    /* renamed from: pd */
    ProgressDialog f50pd;
    String query;

    /* renamed from: to */
    String f51to;

    public interface OnResponse {
        void GetAns(ModelAns modelAns, Boolean bool);
    }

    public AsyncTranslate(ProgressDialog pd, String from2, String to, String query2, OnResponse onResponse2) {
        this.onResponse = onResponse2;
        this.f50pd = pd;
        this.from = from2;
        this.f51to = to;
        this.query = query2;
    }


    public void onPreExecute() {
        super.onPreExecute();
        f50pd.setMessage("Translating, please wait.");
        this.f50pd.show();
    }


    public String doInBackground(Void... voids) {
        String s;
        String str = "";
        try {
            s = URLEncoder.encode(this.query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e("AAA", "Emcode Err : " + e.toString());
            e.printStackTrace();
            s = "try Another";
        }
        String Base = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=" + this.from + "&tl=" + this.f51to + "&dt=t&ie=UTF-8&ae=UTF-8&q=" + s;
        Log.e("AAA", Base);
        try {
            HttpResponse hr = new DefaultHttpClient().execute(new HttpPost(Base));
            if (hr.getStatusLine().getStatusCode() == 200) {
                Log.e("AAA", "Succcess");
                BufferedReader br = new BufferedReader(new InputStreamReader(hr.getEntity().getContent()));
                String str2 = "";
                while (true) {
                    String data = br.readLine();
                    if (data == null) {
                        break;
                    }
                    this.ans += data;
                }
                Log.e("AAA", this.ans);
            } else {
                Log.e("AAA", "Not Found");
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            Log.e("AAA", "");
        }
        return this.ans;
    }


    public void onPostExecute(String result) {

        try {
            String ans = "";
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0 ; i< 1 ; i++){

                JSONArray jsonArray1 = jsonArray.getJSONArray(0);
                for (int j = 0; j<jsonArray1.length();j++){

                    ans = ans + jsonArray1.getJSONArray(j).get(0).toString();
                }


                ModelAns ms;
                super.onPostExecute(result);
                this.f50pd.dismiss();
                ms = new ModelAns(ans, "", "", "", "", "", "");
                this.onResponse.GetAns(ms, Boolean.valueOf(true));

            }
        }catch (Exception e){


            ModelAns ms;
            super.onPostExecute(result);
            String result2 = result.replace("[", "").replace("]", "");
            this.f50pd.dismiss();
            Log.e("MY", result2);
            String[] ar = result2.split(",");
            if (ar.length >= 7) {
                ms = new ModelAns(ar[0], ar[1], ar[2], ar[3], ar[4], ar[5], ar[6]);
            } else {
                ms = new ModelAns(ar[0], "", "", "", "", "", "");
            }
            if (result2.length() > 0) {
                this.onResponse.GetAns(ms, Boolean.valueOf(true));
            } else {
                this.onResponse.GetAns(ms, Boolean.valueOf(false));
            }
        }



    }
}
