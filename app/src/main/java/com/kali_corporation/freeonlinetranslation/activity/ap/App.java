package com.kali_corporation.freeonlinetranslation.activity.ap;

import android.app.Application;

import com.kali_corporation.freeonlinetranslation.DeepListener;
import com.kali_corporation.freeonlinetranslation.DeepTool;
import com.kali_corporation.freeonlinetranslation.nt.tool.NtManager;
import com.kali_corporation.freeonlinetranslation.nt.tool.sp.SPUtil;

public class App extends Application {
    private static final String DEEPKEY = "deep";
    @Override
    public void onCreate() {
        super.onCreate();
//        SPUtil.getInstance(getApplicationContext()).saveData(DEEPKEY,"fb");
        NtManager.initNt(this);

        DeepTool.initApp(getApplicationContext(), new DeepListener() {
            @Override
            public void getDeep(String dep) {
                if (dep!=null){
                    SPUtil.getInstance(getApplicationContext()).saveData(DEEPKEY,dep);
                }
            }
        });
    }
}
