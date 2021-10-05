package com.kali_corporation.freeonlinetranslation.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.os.StrictMode.VmPolicy.Builder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.kali_corporation.freeonlinetranslation.R;
import com.kali_corporation.freeonlinetranslation.activity.util.MyUtils;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.theartofdev.edmodo.cropper.CropImageView.CropResult;
import com.theartofdev.edmodo.cropper.CropImageView.OnCropImageCompleteListener;
import com.theartofdev.edmodo.cropper.CropImageView.OnSetImageUriCompleteListener;

import java.io.File;
import java.io.IOException;

public class MyCrop extends AppCompatActivity {

    private int RESULT_CAMERA = 11;
    ImageView btnback, btncrop, btnrotate;
    Context f61cn = this;
    CropImageView cropImageView;
    LayoutParams layoutParams;
    LinearLayout laytop;
    private File mFileTemp;
    InputImage inputImage;
    TextRecognizer textRecognizer;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_crop);
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        init();
        getWindow().setFlags(1024, 1024);
        if (getIntent().getBooleanExtra("camera", false)) {
            if ("mounted".equals(Environment.getExternalStorageState())) {
                this.mFileTemp = new File(Environment.getExternalStorageDirectory(), "tmp.jpeg");
            } else {
                this.mFileTemp = new File(getFilesDir(), "tmp.jpeg");
            }
            StrictMode.setVmPolicy(new Builder().build());
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra("output", Uri.fromFile(this.mFileTemp));
            startActivityForResult(intent, this.RESULT_CAMERA);
        } else {
            Intent i = new Intent("android.intent.action.PICK");
            i.setType("image/*");
            startActivityForResult(i, 10);
        }
        this.cropImageView.setOnCropImageCompleteListener(new OnCropImageCompleteListener() {
            public void onCropImageComplete(CropImageView view, CropResult result) {
//                detectText(result.getBitmap());
                detectText2(result.getBitmap());
//                TextRecognizer textRecognizer = new TextRecognizer.Builder(MyCrop.this).build();
//                StringBuilder imageText = new StringBuilder();
//                Frame imageFrame = new Frame.Builder()
//                        .setBitmap(result.getBitmap())
//                        .build();
//                SparseArray<TextBlock> textBlocks = textRecognizer.detect(imageFrame);
//                for (int i = 0; i < textBlocks.size(); i++) {
//                    TextBlock textBlock = textBlocks.get(textBlocks.keyAt(i));
//                    imageText.append(textBlock.getValue());
//                }
//                Log.e("AAA", "Text : " + imageText);
//
////                String text = OCRCapture.Builder(MyCrop.this).getTextFromBitmap(result.getBitmap());
////                Log.e("AAA", "Text : " + text);
//                if (imageText.length() > 0) {
//                    Intent i1 = new Intent(MyCrop.this.f61cn, TextTranslator.class);
//                    i1.putExtra("text", imageText.toString());
//                    MyCrop.this.startActivity(i1);
//                    MyCrop.this.finish();
//                    return;
//                }
//                MyUtils.Toast(MyCrop.this.f61cn, "No Text Detect...");
//                MyCrop.this.finish();
            }
        });
        this.cropImageView.setOnSetImageUriCompleteListener(new OnSetImageUriCompleteListener() {
            public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
                if (error != null) {
                    Log.e("AAA", error.getLocalizedMessage());
                }
            }
        });
        this.btnrotate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MyCrop.this.cropImageView.rotateImage(90);
            }
        });
        this.btncrop.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MyCrop.this.btnrotate.setEnabled(false);
                MyCrop.this.cropImageView.getCroppedImageAsync();
            }
        });
    }

    private void detectText2(Bitmap bitmap) {


    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == -1 && data != null) {
            Uri uri = data.getData();
            Log.e("AAA", "Gal Uri : " + uri.toString());
            StringBuilder text = new StringBuilder();
            try {
                inputImage = InputImage.fromFilePath(MyCrop.this, uri);
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

                                        Intent intent = new Intent(MyCrop.this, TextTranslator.class);
                                        intent.putExtra("text", text.toString());
                                        MyCrop.this.startActivity(intent);
                                        MyCrop.this.finish();

                                    }

                                })
                                .addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Task failed with an exception
                                                // ...
                                                Toast.makeText(f61cn, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                // [END run_detector]
            } catch (IOException e) {
                e.printStackTrace();
            }

//            this.cropImageView.setImageUriAsync(uri);
        } else if (requestCode == this.RESULT_CAMERA && resultCode == -1) {
            this.cropImageView.setImageUriAsync(Uri.fromFile(this.mFileTemp));
        } else {
            MyUtils.Toast(this.f61cn, "Selection Cancel");
            finish();
        }
    }


    public void init() {
        this.btncrop = (ImageView) findViewById(R.id.btncrop);
        this.btnrotate = (ImageView) findViewById(R.id.btnrotate);
        this.cropImageView = (CropImageView) findViewById(R.id.setImage);
        this.btnback = (ImageView) findViewById(R.id.btnback);
        this.laytop = (LinearLayout) findViewById(R.id.laytop);
        resize();
        this.btnback.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MyCrop.this.onBackPressed();
            }
        });
    }

    private void processTextBlock(Text result) {
        // [START mlkit_process_text_block]
        String resultText = result.getText();
        for (Text.TextBlock block : result.getTextBlocks()) {
            String blockText = block.getText();
            Point[] blockCornerPoints = block.getCornerPoints();
            Rect blockFrame = block.getBoundingBox();
            for (Text.Line line : block.getLines()) {
                String lineText = line.getText();
                Point[] lineCornerPoints = line.getCornerPoints();
                Rect lineFrame = line.getBoundingBox();
                for (Text.Element element : line.getElements()) {
                    String elementText = element.getText();
                    Point[] elementCornerPoints = element.getCornerPoints();
                    Rect elementFrame = element.getBoundingBox();
                }
            }
        }
        // [END mlkit_process_text_block]
    }


    public void resize() {
//        this.layoutParams = MyUtils.getParamsL(this.f61cn, 450, 170);
//        this.btnrotate.setLayoutParams(this.layoutParams);
//        this.btncrop.setLayoutParams(this.layoutParams);
//        RelativeLayout.LayoutParams layoutParams2 = MyUtils.getParamsR(this.f61cn, 110, 110);
//        layoutParams2.addRule(15);
//        this.btnback.setLayoutParams(layoutParams2);
//        this.laytop.setLayoutParams(MyUtils.getParamsR(this.f61cn, 1080, 197));
    }

//    private void detectText(Bitmap imageBitmap) {
//        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(imageBitmap);
//        FirebaseVisionTextDetector detector = FirebaseVision.getInstance().getVisionTextDetector();
//
//        detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
//            @Override
//            public void onSuccess(FirebaseVisionText firebaseVisionText) {
//                processText(firebaseVisionText);
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(f61cn, e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void processText(FirebaseVisionText firebaseVisionText) {
//        List<FirebaseVisionText.Block> blocks = firebaseVisionText.getBlocks();
//        StringBuilder text=new StringBuilder();
//        if(blocks == null || blocks.size() == 0) {
//            Toast.makeText(this, "No Text Found", Toast.LENGTH_LONG).show();
//            return;
//        }
//
//
//        for(FirebaseVisionText.Block block : blocks) {
//            text.append(block.getText());
//        }
//        Intent i1 = new Intent(MyCrop.this.f61cn, TextTranslator.class);
//        i1.putExtra("text", text.toString());
//        MyCrop.this.startActivity(i1);
//        MyCrop.this.finish();
//
//    }
//

}
