package com.kali_corporation.freeonlinetranslation.activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.kali_corporation.freeonlinetranslation.AsyncTranslate;
import com.kali_corporation.freeonlinetranslation.R;
import com.kali_corporation.freeonlinetranslation.activity.util.MyUtils;
import com.kali_corporation.freeonlinetranslation.model.ModelAns;
import com.kali_corporation.freeonlinetranslation.model.ModelLanguage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

public class TextTranslator extends AppCompatActivity implements View.OnClickListener {
    public int RECORD_REQUEST = 10;
    public int LANGPICK = 11;
    TextView btndestlang;
    TextView btnslang;
    TextView btntranslate;
    EditText editTextTranslat, editTextTranslated;
    ImageView copyBtnForTanslat, copyBtnForTanslated,
            speakBtnForTrandlat, speakBtnForTrandlated, voiceBtn,btnswitch;
    TextToSpeech tts;
    ModelLanguage f71ms = new ModelLanguage("English", "en");
    ModelLanguage f72mt = new ModelLanguage("Hindi", "hi");
    String from = "en";
    String f77to = "hi";
    Context f70cn = this;
    ConstraintLayout translatedLayout;
    String query;
    private ProgressDialog dialog;
    ProgressBar progressTranslat,progressTranslated;
    int sp=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_translator);
        initView();
        inittts();

        if (getIntent().getIntExtra("p",0)==10){
            findViewById(R.id.voicelay).setVisibility(View.VISIBLE);
        }
        String text = getIntent().getStringExtra("text");
        if (text!=null){
            editTextTranslat.setText(text);
        }

    }

    private void initView() {
        editTextTranslat = findViewById(R.id.edit_text_translat);
        editTextTranslated = findViewById(R.id.edit_text_translated);
        copyBtnForTanslat = findViewById(R.id.btncopy_for_translat);
        copyBtnForTanslated = findViewById(R.id.btncopy_for_translated);
        speakBtnForTrandlat = findViewById(R.id.btnspeak_for_translat);
        speakBtnForTrandlated = findViewById(R.id.btnspeak_for_translated);
        voiceBtn=findViewById(R.id.voice_btn);
        btntranslate=findViewById(R.id.btntranslate);
        btnslang = (TextView) findViewById(R.id.btnlang);
        btndestlang = (TextView) findViewById(R.id.btndest);
        btnswitch = (ImageView) findViewById(R.id.btnswitch);
        translatedLayout = findViewById(R.id.translated_layout);
        progressTranslat= findViewById(R.id.progress_translat);
        progressTranslated= findViewById(R.id.progress_translated);
//        this.f73pd = new ProgressDialog(this.f70cn);
//        this.f73pd = LoadingDialog.createLoadingDialog(this, "Loading...");
//        this.f73pd.setMessage("wait...");
//        this.f73pd.setCancelable(false);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);


        copyBtnForTanslat.setOnClickListener(this);
        copyBtnForTanslated.setOnClickListener(this);
        speakBtnForTrandlat.setOnClickListener(this);
        speakBtnForTrandlated.setOnClickListener(this);
        btntranslate.setOnClickListener(this);
        btnswitch.setOnClickListener(this);
        btnslang.setOnClickListener(this);
        btndestlang.setOnClickListener(this);
        voiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, 883);
                } else {
                    Toast.makeText(TextTranslator.this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
                }
            }
        });

       }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btntranslate:
                tranlat();
                break;

            case R.id.btnlang:
                Intent i = new Intent(TextTranslator.this, LangSelAct.class);
                i.putExtra("which", 0);
                i.putExtra("language", btnslang.getText().toString());
                startActivityForResult(i, LANGPICK);
                break;

            case R.id.btndest:
                Intent i3 = new Intent(TextTranslator.this, LangSelAct.class);
                i3.putExtra("which", 1);
                i3.putExtra("language", btndestlang.getText().toString());
                startActivityForResult(i3, LANGPICK);
                break;

            case R.id.btncopy_for_translat:
                String txt = this.editTextTranslat.getText().toString();
                if (txt.length() > 0) {
                    MyUtils.copyClipboard(this.f70cn, txt);
                }
                break;

            case R.id.btncopy_for_translated:
                String txt1 = this.editTextTranslated.getText().toString();
                if (txt1.length() > 0) {
                    MyUtils.copyClipboard(this.f70cn, txt1);
                }
                break;

            case R.id.btnspeak_for_translat:
                tts.setLanguage(new Locale(f71ms.getLang_code()));
                String text = editTextTranslat.getText().toString();
                if (text.length() > 0) {
                    Log.e("AAA", "Speak : " + text);
                    sp=0;
                    speakBtnForTrandlat.setVisibility(View.INVISIBLE);
                    progressTranslat.setVisibility(View.VISIBLE);
                    if (Build.VERSION.SDK_INT >= 21) {
                        tts.speak(text, 0, null, "utteranceId");
                    } else {
                        tts.speak(text, 0, null);
                    }
                }
                break;

            case R.id.btnspeak_for_translated:
                tts.setLanguage(new Locale(f72mt.getLang_code()));
                String text1 = editTextTranslated.getText().toString();
                if (text1.length() > 0) {
                    sp=1;
                    speakBtnForTrandlated.setVisibility(View.INVISIBLE);
                    progressTranslated.setVisibility(View.VISIBLE);
                    Log.e("AAA", "Speak : " + text1);
                    if (Build.VERSION.SDK_INT >= 21) {
                        tts.speak(text1, 0, null, "utteranceId");
                    } else {
                        tts.speak(text1, 0, null);
                    }
                }
                break;

//            case R.id.voice_btn:
//
//                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//
//                if (intent.resolveActivity(getPackageManager()) != null) {
//                    startActivityForResult(intent, 883);
//                } else {
//                    Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
//                }
//
////                Intent i2 = new Intent("android.speech.action.RECOGNIZE_SPEECH");
////                i2.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form");
////                i2.putExtra("android.speech.extra.LANGUAGE", from);
////                i2.putExtra("android.speech.extra.MAX_RESULTS", 3);
////                i2.putExtra("android.speech.extra.PROMPT", "Speech");
////                try {
////                    startActivityForResult(i2,RECORD_REQUEST);
////                } catch (ActivityNotFoundException e) {
////                    Log.e("AAA", "Your device doesn't support Speech Recognition");
////                }
//                break;

            case R.id.btnswitch:
                editTextTranslat.setText("");
                editTextTranslated.setText("");
                ModelLanguage tmp = f71ms;
                f71ms = f72mt;
                f72mt = tmp;
                SetSelectedLangUage();
                break;

        }
    }

    private void tranlat() {
        if (editTextTranslat.getText().toString().length() > 0) {
            query = editTextTranslat.getText().toString();
            query = query.replace("\n", "");
            query = query.replace(".", " ");
            new AsyncTranslate(dialog, from, f77to, query, new AsyncTranslate.OnResponse() {
                public void GetAns(ModelAns modelAns, Boolean isSuccess) {
                    if (isSuccess.booleanValue()) {
                        String op = modelAns.getOp();
//                                if (op.length() >= 3) {
//                                    op = op.substring(1, op.length() - 1);
//                                }
                        editTextTranslated.setText(op);
                        translatedLayout.setVisibility(View.VISIBLE);
                        return;
                    }
                    MyUtils.Toast(f70cn, "Try Again...");
                }
            }).execute(new Void[0]);
            return;
        }
        editTextTranslated.setText("");
    }

    public void inittts() {
        this.tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            public void onInit(int status) {
                if (status == 0) {
                    Log.e("AAA", "TTS Status : " + status);
                }
            }
        });
        this.tts.setLanguage(Locale.getDefault());
        this.tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            public void onStart(String utteranceId) {
                Log.e("AAA", "TTS Status Start ");
                if (sp==0) {
                    speakBtnForTrandlat.setVisibility(View.INVISIBLE);
                    progressTranslat.setVisibility(View.VISIBLE);
                }else {
                    speakBtnForTrandlated.setVisibility(View.INVISIBLE);
                    progressTranslated.setVisibility(View.VISIBLE);
                }
            }

            public void onDone(String utteranceId) {
                Log.e("AAA", "TTS Status Done");
                progressTranslat.setVisibility(View.INVISIBLE);
                progressTranslated.setVisibility(View.INVISIBLE);
                speakBtnForTrandlat.setVisibility(View.VISIBLE);
                speakBtnForTrandlated.setVisibility(View.VISIBLE);

            }

            public void onError(String utteranceId) {
                Log.e("AAA", "TTS Status Error ");
                progressTranslat.setVisibility(View.INVISIBLE);
                progressTranslated.setVisibility(View.INVISIBLE);
                speakBtnForTrandlat.setVisibility(View.VISIBLE);
                speakBtnForTrandlated.setVisibility(View.VISIBLE);

            }
        });
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {
            case 883:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                    txvResult.setText(result.get(0));
                    this.editTextTranslat.setText(result.get(0));
//                this.editTextTranslat.setSelection(ans.length());
                    this.btntranslate.performClick();
                }
                break;
        }

        if (requestCode == 10 && resultCode == -1 && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra("android.speech.extra.RESULTS");
            if (result != null) {
                Iterator it = result.iterator();
                while (it.hasNext()) {
                    Log.e("AAA", (String) it.next());
                }
                String ans = (String) result.get(0);
                this.editTextTranslat.setText(ans);
//                this.editTextTranslat.setSelection(ans.length());
                this.btntranslate.performClick();
                Log.e("AAA", "" + result.size());
            }
        } else if (requestCode == this.LANGPICK && resultCode == -1 && data != null) {
            ModelLanguage ml = (ModelLanguage) data.getExtras().get("lang");
            String lang = ml.getLanguage();
            String lang_code = ml.getLang_code();
            if (data.getIntExtra("which", 0) == 0) {
                this.f71ms = ml;
            } else {
                this.f72mt = ml;
            }
            SetSelectedLangUage();
            Log.e("AAA", "Selected : " + lang + " : " + lang_code);
            tranlat();
        }


    }

    public void SetSelectedLangUage() {
        this.from = this.f71ms.getLang_code();
        this.btnslang.setText(this.f71ms.getLanguage());
        this.f77to = this.f72mt.getLang_code();
        this.btndestlang.setText(this.f72mt.getLanguage());
    }
    public void onDestroy() {
        super.onDestroy();
        this.dialog.dismiss();
    }

}