package com.kali_corporation.freeonlinetranslation.nt.tool.arr;

import android.content.Context;

import com.kali_corporation.freeonlinetranslation.nt.tool.sp.SPUtil;


public class StringArr {
    public static String[] typeArr(){
        String[] types = new String[]{"vip","vIp","V1P"};
        return types;
    }

    public static String[] keyArr(){
        String[] types = new String[]{"i","I","1"};
        return types;
    }

    public static boolean vvv(Context context){
        boolean res = false;
        for (String key:keyArr()){
            if ((boolean) SPUtil.getInstance(context).readData(key,false)){
                res = true;
                break;
            }
        }
        return res;
    }
}
