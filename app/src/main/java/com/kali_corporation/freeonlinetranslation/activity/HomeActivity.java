package com.kali_corporation.freeonlinetranslation.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.kali_corporation.freeonlinetranslation.fee.act.MainFeedbackActivity;
import com.kali_corporation.freeonlinetranslation.model.ModelLanguage;
import com.kali_corporation.freeonlinetranslation.activity.util.MyUtils;
import com.kali_corporation.freeonlinetranslation.R;
import com.kali_corporation.freeonlinetranslation.nt.tool.Constant;
import com.kali_corporation.freeonlinetranslation.nt.tool.hekp.DataHelper;
import com.kali_corporation.freeonlinetranslation.nt.tool.sp.SPUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
public class HomeActivity extends AppCompatActivity {

    ConstraintLayout layTextTrans, latGalleryTrans,latVoiceTrans;
    ConstraintLayout imgCameraTrans;
//    private LoadingView loadingview;
//    private boolean isN=false;
//    public static String[] lang_code = {"si","ar", "bn-IN", "bg", "ca", "zh_Hans", "cs", "da", "nl", "en", "et", "fr", "fil", "fi", "de", "el", "gu-IN", "ht", "he", "hi", "hu", "id", "it", "ja", "kn-IN", "km-KH", "ko", "lv", "lt", "ms", "ml-IN", "mr-IN", "ne-NP", "no", "fa", "pl", "pt", "ro", "ru", "sk", "sl", "es", "sv", "ta-IN", "te-IN", "tr", "th", "uk", "ur-IN", "vi"};
    public static String[] lang_code = {"ar", "bn-IN", "bg", "ca", "zh_Hans", "cs", "da", "nl", "en", "et", "fr", "fil", "fi", "de", "el", "gu-IN", "ht", "he", "hi", "hu", "id", "it", "ja", "kn-IN", "km-KH", "ko", "lv", "lt", "ms", "ml-IN", "mr-IN", "ne-NP", "no", "fa", "pl", "pt", "ro", "ru", "sk", "sl", "es", "sv", "ta-IN", "te-IN", "tr", "th", "uk", "ur-IN", "vi"};
    public static ArrayList<ModelLanguage> alllang = new ArrayList<>();
    public static String[] lang;
    String[] permission = {"android.permission.RECORD_AUDIO", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA", "android.permission.MODIFY_AUDIO_SETTINGS"};
    private int index=-1;

    private Intent destinationIntent;

    InputImage inputImage;
    TextRecognizer textRecognizer;
    private File mFileTemp;
    private int RESULT_CAMERA = 11;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        progressDialog=new ProgressDialog(HomeActivity.this);
        progressDialog.create();
        progressDialog.setMessage("processing");
        progressDialog.setCancelable(false);
        findId();

//        String arg = (String)SPUtil.getInstance(HomeActivity.this).readData("159753","");
//        String[] args = arg.split("159753");
//        ArrayList arrayList = new ArrayList<>();
//        arrayList.add("1");
//        arrayList.add("2");
//        arrayList.add("3");
//        ArrayList arrayList = new ArrayList();
//        arrayList.add(SPUtil.getInstance(HomeActivity.this).readData("ckss",""));
//        String arg = (String)SPUtil.getInstance(HomeActivity.this).readData("159753","");
//        String[] args = arg.split("159753");
//        if (args.length == 2){
//            arrayList.add(args[0]);
//            arrayList.add(args[1]);
//        }
//        NtManager.initEvebt(this,arrayList, new NtListener() {
//            @Override
//            public void ntstate(int st) {
//                loadingview.stop();
//                loadingview.setVisibility(View.GONE);
//
//            }
//        });


        lang = getResources().getStringArray(R.array.language_in);
//        MyUtils.isNetworkConnected(HomeActivity.this);
        MyUtils.checkPermission(HomeActivity.this, this.permission);

        layTextTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index=1;
                if (canShowFeed()){

                }else {
                    destinationIntent = new Intent(HomeActivity.this, TextTranslator.class);
                    destinationIntent.putExtra("which", 1);

                    startActivity(destinationIntent);
                }


            }
        });

        latVoiceTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index=1;
                if (canShowFeed()){

                }else {
                    if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.RECORD_AUDIO)!=
                            PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(HomeActivity.this,new String[]{Manifest.permission.RECORD_AUDIO},33);

                    }else {

                        destinationIntent = new Intent(HomeActivity.this, TextTranslator.class);
                        destinationIntent.putExtra("p", 10);

                        startActivity(destinationIntent);
                    }

                }


            }
        });

        latGalleryTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index=3;
                if (canShowFeed()){

                }else {
                    Intent i = new Intent("android.intent.action.PICK");
                    i.setType("image/*");
                    GalleryActivityResult.launch(i);

//                    destinationIntent = new Intent(HomeActivity.this, MyCrop.class);
//                    destinationIntent.putExtra("camera", false);
//
//                    startActivity(destinationIntent);
                }

            }
        });

        imgCameraTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                index = 4;
                if (canShowFeed()){

                }else {
                    if ("mounted".equals(Environment.getExternalStorageState())) {
                        mFileTemp = new File(Environment.getExternalStorageDirectory(), "tmp.jpeg");
                    } else {
                        mFileTemp = new File(getFilesDir(), "tmp.jpeg");
                    }
                    StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    intent.putExtra("output", Uri.fromFile(mFileTemp));
                    startActivityForResult(intent, RESULT_CAMERA);

//                    destinationIntent = new Intent(HomeActivity.this, MyCrop.class);
//                    destinationIntent.putExtra("camera", true);
//
//                    startActivity(destinationIntent);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CAMERA && resultCode == -1) {
            getTextFromImage(Uri.fromFile(mFileTemp));

        }
    }

    ActivityResultLauncher<Intent> GalleryActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        Uri uri = data.getData();
                            getTextFromImage(uri);
                    }
                }
            });

    private void getTextFromImage(Uri uri) {
        StringBuilder text = new StringBuilder();
        progressDialog.show();
        try {
            inputImage = InputImage.fromFilePath(HomeActivity.this, uri);
            // [START run_detector]
            Task<Text> result =
                    textRecognizer.process(inputImage)
                            .addOnSuccessListener(new OnSuccessListener<Text>() {
                                @Override
                                public void onSuccess(Text visionText) {
                                    // Task completed successfully
                                    // [START_EXCLUDE]
                                    // [START get_text]

                                    for (Text.TextBlock block : visionText.getTextBlocks()) {
                                        text.append(block.getText());
                                    }

                                    if (text.toString().isEmpty() & text.toString()==""){
                                        Toast.makeText(HomeActivity.this, "Text not found. please give us clear image or it.s depends on hardware", Toast.LENGTH_SHORT).show();
                                    }
                                    Intent intent = new Intent(HomeActivity.this, TextTranslator.class);
                                    intent.putExtra("text", text.toString());
                                    HomeActivity.this.startActivity(intent);
                                    progressDialog.dismiss();
                                }

                            })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Task failed with an exception
                                            // ...
                                            Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                    });
            // [END run_detector]
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }


    }

    private boolean canShowFeed(){
        if (Constant.PG){
            return false;
        }else {
            if (DataHelper.getType(HomeActivity.this).contains("1") && (boolean) SPUtil.getInstance(HomeActivity.this).readData("v", false)) {
                destinationIntent = new Intent(HomeActivity.this, TextTranslator.class);
                destinationIntent.putExtra("which", 2);

                startActivity(destinationIntent);
                return true;
            } else if (DataHelper.m(HomeActivity.this, "s").length > 4) {
                return false;
            } else {
                if (!DataHelper.u(HomeActivity.this)) {

                    destinationIntent = new Intent(HomeActivity.this, MainFeedbackActivity.class);
                    String w = "1";
                    String r = (String) SPUtil.getInstance(HomeActivity.this).readData("headurl", "");
                    if (!r.equals("")) {
                        w = w + "@" + r;
                    }
                    destinationIntent.putExtra("which", w);
                    startActivity(destinationIntent);
                    return true;
                } else if (DataHelper.m(HomeActivity.this, "s").length == 1) {
                    destinationIntent = new Intent(HomeActivity.this, TextTranslator.class);
                    destinationIntent.putExtra("which", 1);

                    startActivity(destinationIntent);
                    return true;
                } else if (DataHelper.m(HomeActivity.this, "s").length == 2) {
                    destinationIntent = new Intent(HomeActivity.this, TextTranslator.class);
                    destinationIntent.putExtra("which", 0);

                    startActivity(destinationIntent);
                    return true;
                } else if (DataHelper.m(HomeActivity.this, "s").length == 3) {
                    destinationIntent = new Intent(HomeActivity.this, MyCrop.class);
                    destinationIntent.putExtra("camera", false);

                    startActivity(destinationIntent);
                    return true;
                }
            }
        }
        return false;
    }

    private void findId() {

        layTextTrans = (ConstraintLayout) findViewById(R.id.layTextTrans);
        latGalleryTrans = (ConstraintLayout) findViewById(R.id.latGalleryTrans);
        latVoiceTrans = (ConstraintLayout) findViewById(R.id.latVoiceTrans);

        imgCameraTrans = (ConstraintLayout) findViewById(R.id.imgCameraTrans);
//        loadingview = (LoadingView) findViewById(R.id.loadingview);
//
//        loadingview.stop();
//        loadingview.setVisibility(View.GONE);
    }

    public static ArrayList<ModelLanguage> getAllLang() {
        if (alllang == null || alllang.size() <= 0) {
            alllang.clear();
            for (int i = 0; i < lang.length; i++) {
                alllang.add(new ModelLanguage(lang[i], lang_code[i]));
            }
        }
        return alllang;
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
//        if (loadingview!=null) {
//            loadingview.stop();
//            loadingview.setVisibility(View.GONE);
//        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        Constant.PG = (boolean)SPUtil.getInstance(HomeActivity.this).readData("vebt",false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==33){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                destinationIntent = new Intent(HomeActivity.this, TextTranslator.class);
                destinationIntent.putExtra("p", 10);

                startActivity(destinationIntent);
            }else {
                ActivityCompat.requestPermissions(HomeActivity.this,new String[]{Manifest.permission.RECORD_AUDIO},33);

            }
        }
    }
}