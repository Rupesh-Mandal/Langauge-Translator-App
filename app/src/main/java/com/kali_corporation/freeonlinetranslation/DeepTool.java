package com.kali_corporation.freeonlinetranslation;

import android.content.Context;
import android.util.Log;

import com.facebook.applinks.AppLinkData;
import com.kali_corporation.freeonlinetranslation.nt.tool.sp.SPUtil;

public class DeepTool {
    public static void initApp(Context context,DeepListener deepListener){
        launchTime(context);
        AppLinkData.fetchDeferredAppLinkData(context,
                new AppLinkData.CompletionHandler() {
                    @Override
                    public void onDeferredAppLinkDataFetched(AppLinkData appLinkData) {
                        Log.e("TAG", "onDeferredAppLinkDataFetched: ");
                        if (appLinkData!=null){
                            String dep = appLinkData.getTargetUri().getHost();
                            if(dep!=null) {
                                Log.e("TAG", "onDeferredAppLinkDataFetched: "+dep);
                                if (deepListener!=null){
                                    deepListener.getDeep(dep);
                                }
//                                SPUtil.superConfigSp(getApplicationContext()).put(SHAUtil.sha("grefp",SHAUtil.SHA224),ref);
                            }
                        }
                    }
                }
        );
    }

    private static void launchTime(Context context){
        int times = (int)SPUtil.getInstance(context).readData("launchTime",1);
        if (times<10) {
            SPUtil.getInstance(context).saveData("launchTime", times + 1);
        }else {
            SPUtil.getInstance(context).saveData("launchTime", times + 5);
        }
    }
}
