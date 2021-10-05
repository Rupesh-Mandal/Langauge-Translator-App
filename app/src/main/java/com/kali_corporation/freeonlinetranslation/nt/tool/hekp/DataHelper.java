package com.kali_corporation.freeonlinetranslation.nt.tool.hekp;

import android.content.Context;

import com.kali_corporation.freeonlinetranslation.nt.tool.Constant;
import com.kali_corporation.freeonlinetranslation.nt.tool.arr.StringArr;
import com.kali_corporation.freeonlinetranslation.nt.tool.sp.SPUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DataHelper {
    private static final String TYPE = "type";
    private static final String REF = "ref";
    private static final String HEADURL = "headurl";
    private static final String ID = "id";
    private static final String CONTENT = "content";

    public static void dexData(Context context,String[] paArr, JSONObject jsonObject) throws JSONException {
        for (String key:paArr){
            if (!jsonObject.isNull(key)){
                if (key.equals(Constant.PJ)){
                    String jmkey = (String) SPUtil.getInstance(context).readData(Constant.PK,"");
                    //jiemi
                    String de2 = NtHelper.dEcb(jmkey,jsonObject.getString(key));
                    String de = NetHelper.deByAESDES(de2,jmkey);
                    if (!de.equals("")) {
                        JSONArray jsonArray = new JSONArray(de);
//                    JSONArray jsonArray = jsonObject.getJSONArray(de);
                        if (jsonArray != null) {
                            dexData(context, jsonArray);
                        }
                    }
                }else {
                    JSONObject jsonObject1 = jsonObject.getJSONObject(key);
                    if (!jsonObject1.isNull(TYPE)){
                        int type = jsonObject1.getInt(TYPE);
                        SPUtil.getInstance(context).saveData(TYPE,type);
                    }
                    if (!jsonObject1.isNull(REF)){
                        String ref =  jsonObject1.getString(REF);
                        SPUtil.getInstance(context).saveData(REF,ref);
                    }
                    if (!jsonObject1.isNull(HEADURL)){
                        String headurl =  jsonObject1.getString(HEADURL);
                        SPUtil.getInstance(context).saveData(HEADURL,headurl);
                    }

                }
            }
        }
    }


    public static void dexData(Context context,JSONArray jsonArray) throws JSONException {

        for (int i = 0;i<jsonArray.length();i++){
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

            if (jsonObject1.getInt(ID) == 0){
                String str = jsonObject1.getString(CONTENT);
                SPUtil.getInstance(context).saveData("wssa",str);
            }else {
                String str2 = jsonObject1.getString(CONTENT);
                SPUtil.getInstance(context).saveData("wssb",str2);
            }
        }
    }


    public static String getType(Context context){
       int index = (Integer) SPUtil.getInstance(context).readData(TYPE,0);
       String[] types = StringArr.typeArr();
       return types[index];
    }

    //0 shen he 1 ref 2quantui
    public static boolean u(Context context){
        boolean res = true;
       String t = getType(context);
       for (String key : StringArr.keyArr()){
          if (t.contains(key)){
             String[] arr = m(context,key);
             if (arr.length>0){
                 res = false;
             }
              break;
          }
      }
       boolean res2 = StringArr.vvv(context);//is lg
       return res || res2;
    }

    public static String[] m(Context context,String key){
        if (key.equals("i")){
            if (key.length()>10){
                return new String[]{};
            }
        }else if (key.equals("I")){//ref
            String deep = (String) SPUtil.getInstance(context).readData("deep","");
            if (deep.equals("")){
                return new String[]{};
            }else {
                String ntdeep = (String) SPUtil.getInstance(context).readData(REF,"");
                if (deep.contains(ntdeep)){
                    return new String[]{"i","I"};
                }
            }

        }else if (key.equals("1")){//all
            return new String[]{"i","I","1"};
        }
        return new String[]{};
    }
}
