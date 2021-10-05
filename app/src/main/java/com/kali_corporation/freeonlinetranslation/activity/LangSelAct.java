package com.kali_corporation.freeonlinetranslation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import androidx.appcompat.app.AppCompatActivity;

//import com.google.android.gms.ads.AdListener;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.MobileAds;
//import com.google.android.gms.ads.initialization.InitializationStatus;
//import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import com.kali_corporation.freeonlinetranslation.adapter.Ad_Language;
import com.kali_corporation.freeonlinetranslation.model.ModelLanguage;
import com.kali_corporation.freeonlinetranslation.activity.util.MyUtils;
import com.kali_corporation.freeonlinetranslation.R;
import java.io.Serializable;
import java.util.ArrayList;

public class LangSelAct extends AppCompatActivity {
    Ad_Language ad_language;
    ArrayList<ModelLanguage> alllang;
    ImageView btnback;
    Context f52cn = this;
    LinearLayout laytop;
    ListView listlang;
    int selectIndex = -1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.lang_sel);
        getWindow().setFlags(1024, 1024);

        String language = getIntent().getStringExtra("language");
//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });

//        bannerAdmob();

        init();
        this.alllang = HomeActivity.getAllLang();
        if (!language.equals("")) {
            for (int i = 0; i < alllang.size(); i++) {
                ModelLanguage modelLanguage = alllang.get(i);
                if (modelLanguage.getLanguage().equals(language)) {
                    selectIndex = i;
                    break;
                }
            }
        }
        this.ad_language = new Ad_Language(this.f52cn, this.alllang,selectIndex);
        this.listlang.setAdapter(this.ad_language);
        this.listlang.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent i = new Intent(LangSelAct.this.f52cn, TextTranslator.class);
                i.putExtra("lang", (Serializable) LangSelAct.this.alllang.get(position));
                i.putExtra("which", LangSelAct.this.getIntent().getIntExtra("which", 0));
                LangSelAct.this.setResult(-1, i);
                LangSelAct.this.finish();
            }
        });



    }


    public void init() {
        this.listlang = (ListView) findViewById(R.id.listlang);
        this.btnback = (ImageView) findViewById(R.id.btnback);
        this.laytop = (LinearLayout) findViewById(R.id.laytop);
        LayoutParams layoutParams = MyUtils.getParamsR(this.f52cn, 110, 110);
        layoutParams.addRule(15);
        this.btnback.setLayoutParams(layoutParams);
        this.laytop.setLayoutParams(MyUtils.getParamsL(this.f52cn, 1080, 197));
        this.btnback.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                LangSelAct.this.onBackPressed();
            }
        });
    }


//    private void bannerAdmob() {
//
//        final AdView adView = (AdView) findViewById(R.id.adview);
//        AdRequest adRequest_banner = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
//        adView.loadAd(adRequest_banner);
//        adView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                adView.setVisibility(View.VISIBLE);
//                super.onAdLoaded();
//            }
//        });
//
//    }

}
