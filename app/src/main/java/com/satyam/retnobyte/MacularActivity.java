package com.satyam.retnobyte;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ibm.watson.developer_cloud.android.library.camera.CameraHelper;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;

public class MacularActivity extends AppCompatActivity {


    private ImageView mImageView;
    private TextView mTextView1;
    private VisualRecognition mVisualRecognition;
    private CameraHelper mCameraHelper;
    private File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_macular);

        mImageView = (ImageView) findViewById(R.id.image_view_main);
        mTextView1 = (TextView) findViewById(R.id.text_view_main);


        mCameraHelper = new CameraHelper(this);

        auth();

        captureImage();
    }
    private void auth(){
        IamOptions options = new IamOptions.Builder()
                .apiKey("F5lfyw3ho7Ch_o4ZGbHM-vdOS4ehNu0sSnIoX54UV2gf")
                .build();
        mVisualRecognition = new VisualRecognition("2018-03-19", options);
    }

    public void captureImage(){

        ImageView button =  findViewById(R.id.imageView3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraHelper.dispatchTakePictureIntent();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CameraHelper.REQUEST_IMAGE_CAPTURE) {
            final Bitmap photo = mCameraHelper.getBitmap(resultCode);
            photoFile = mCameraHelper.getFile(resultCode);
            mImageView.setImageBitmap(photo);

            backgroundThread();

        }
    }

    private void backgroundThread(){

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                InputStream imagesStream = null;
                try {
                    imagesStream = new FileInputStream(photoFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ClassifyOptions classifyOptions = new ClassifyOptions.Builder()
                        .imagesFile(imagesStream)
                        .imagesFilename(photoFile.getName())
                        .threshold((float) 0.6)
                        .classifierIds(Arrays.asList("MacularDegeneration_1181202483"))
                        .build();
                ClassifiedImages result = mVisualRecognition.classify(classifyOptions).execute();
                Gson gson = new Gson();
                String json = gson.toJson(result);
                Log.d("json", json);
                String name = null;
                double score = 0;
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray jsonArray = jsonObject.getJSONArray("images");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    JSONArray jsonArray1 = jsonObject1.getJSONArray("classifiers");
                    JSONObject jsonObject2 = jsonArray1.getJSONObject(0);
                    JSONArray jsonArray2 = jsonObject2.getJSONArray("classes");
                    JSONObject jsonObject3 = jsonArray2.getJSONObject(0);
                    name = jsonObject3.getString("class");
                    score = jsonObject3.getDouble("score");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final String finalName = name;
                final double finalScore = score;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTextView1.setText("Detection: " + finalName);



                    }
                });

            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
