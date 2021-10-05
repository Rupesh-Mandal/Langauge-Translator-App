package com.kali_corporation.freeonlinetranslation.nt.tool.sp;
import android.content.Context;

public class SPUtil {

    private static String name = "app_db";
    private static SPUtil instance;
    private static MySharedPreferences sharedPreferences;
    private SPUtil (){}

    public static SPUtil getInstance(Context context) {
        if (instance == null) {
            instance = new SPUtil();
            sharedPreferences = new MySharedPreferences(context, name);
        }
        return instance;
    }

    public void saveData(String key ,Object content){
        sharedPreferences.putValue(key,content);
    }

    public Object readData(String key ,Object content){
       return sharedPreferences.getValue(key,content);
    }
}
