package com.example.perfectface.Module;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.amnix.skinsmoothness.AmniXSkinSmooth;
import com.darwindeveloper.horizontalscrollmenulibrary.custom_views.HorizontalScrollMenuView;
import com.darwindeveloper.horizontalscrollmenulibrary.extras.MenuItem;
import com.example.perfectface.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionPoint;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceContour;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class editactivity extends AppCompatActivity {
    private HorizontalScrollMenuView menu;
    private ImageView imageView;
    private Uri myUri;
    private Button ButtonBack;
    private Button ButtonApply;
    private Button resetButton;
    private Bitmap bitmap,bitmap1,Processed;
    private static int smoothR=500;
    private static int  white=0;
    private static int smooth=300;
    private static int  whiteR=6;
    private static int smoothK=1800;
    private static int whiteK=0;
    private ToggleButton toggleButton;
    private static int count;




    private final AmniXSkinSmooth amniXSkinSmooth = AmniXSkinSmooth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editactivity);
        menu=findViewById(R.id.controller);
        imageView=findViewById(R.id.imgPreview);
        ButtonBack=findViewById(R.id.btnBack);
        ButtonApply=findViewById(R.id.btnApply);
        toggleButton=findViewById(R.id.toggleButton);
        resetButton=findViewById(R.id.resetButton);

        // get the image from intent
        myUri= Uri.parse(getIntent().getStringExtra("uri"));
        imageView.setImageURI(myUri);




        // convert uri to bitmap
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),myUri);
            bitmap1=MediaStore.Images.Media.getBitmap(this.getContentResolver(),myUri);
        } catch (IOException e) {
            e.printStackTrace();
        }


        initMenu();


//return to the last activity
        ButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(editactivity.this, dashbord.class);
                startActivity(intent);
                finish();
            }
        });



        //reset button
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Processed=null;
                bitmap=bitmap1;
                imageView.setImageBitmap(bitmap);
            }
        });




ButtonApply.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Toast.makeText(editactivity.this,"coming soon",Toast.LENGTH_SHORT);
    }
});

// compar the original photo to the procceced one
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (bitmap == null || Processed == null) {
                    return;
                }else if (isChecked)
                    imageView.setImageBitmap(Processed);
                else
                    imageView.setImageBitmap(bitmap1);
            }
        });



    }


//insert the elements of the horizentall scroll menu
    private void initMenu() {
        menu.addItem("Beauty skin",R.drawable.beautyskin);
        menu.addItem("skin brightening",R.drawable.brightness);
        menu.addItem("wrinkles&acne",R.drawable.wrinklesss);

        menu.setOnHSMenuClickListener(new HorizontalScrollMenuView.OnHSMenuClickListener() {


            @Override
            public void onHSMClick(final MenuItem menuItem, int position) {
                Toast.makeText(editactivity.this,""+menuItem.getText(),Toast.LENGTH_SHORT).show();




//check wich item is Selected
                if(menuItem.getText()=="Beauty skin") {

                    final ProgressDialog progressDialog = ProgressDialog.show(editactivity.this, "Please Wait!", "Doing SOme Work", true, false);


                            new AsyncTask<Void, Void, Void>() {
                                @Override
                                protected Void doInBackground(Void... voids) {
                                    amniXSkinSmooth.storeBitmap(bitmap, false);
                                    amniXSkinSmooth.initSdk();
                                    amniXSkinSmooth.startFullBeauty(smoothR, white);
                                    amniXSkinSmooth.startSkinSmoothness(smoothR);
                                    amniXSkinSmooth.startSkinWhiteness(white);
                                    Processed = amniXSkinSmooth.getBitmapAndFree();
                                    amniXSkinSmooth.unInitSdk();
                                    return null;
                                }


                                @Override
                                protected void onPostExecute(Void aVoid) {
                                    super.onPostExecute(aVoid);
                                    if (progressDialog.isShowing())
                                        progressDialog.dismiss();
                                    if (Processed != null) {
                                        imageView.setImageBitmap(Processed);
                                        bitmap = Processed;

                                    } else {
                                        Toast.makeText(editactivity.this, "Unable to Process!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }.execute();


                }else
if(menuItem.getText()=="skin brightening"){
    final ProgressDialog progressDialog = ProgressDialog.show(editactivity.this, "Please Wait!", "Doing SOme Work", true, false);


    new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground(Void... voids) {
            amniXSkinSmooth.storeBitmap(bitmap, false);
            amniXSkinSmooth.initSdk();
            amniXSkinSmooth.startFullBeauty(smooth, whiteR);
            amniXSkinSmooth.startSkinSmoothness(smooth);
            amniXSkinSmooth.startSkinWhiteness(whiteR);
            Processed = amniXSkinSmooth.getBitmapAndFree();
            amniXSkinSmooth.unInitSdk();
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            if (Processed != null) {
                imageView.setImageBitmap(Processed);
                bitmap=Processed;

            } else {
                Toast.makeText(editactivity.this, "Unable to Process!", Toast.LENGTH_LONG).show();
            }
        }
    }.execute();
                   }else



    new AsyncTask<Void, Void, Void>() {
        final ProgressDialog progressDialog = ProgressDialog.show(editactivity.this, "Please Wait!", "Doing SOme Work", true, false);

        @Override
        protected Void doInBackground(Void... voids) {
            amniXSkinSmooth.storeBitmap(bitmap, false);
            amniXSkinSmooth.initSdk();
            amniXSkinSmooth.startFullBeauty(smoothK, whiteK);
            amniXSkinSmooth.startSkinSmoothness(smoothK);
            amniXSkinSmooth.startSkinWhiteness(whiteK);
            Processed = amniXSkinSmooth.getBitmapAndFree();
            amniXSkinSmooth.unInitSdk();
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            if (Processed != null) {
                imageView.setImageBitmap(Processed);
                bitmap=Processed;
            } else {
                Toast.makeText(editactivity.this, "Unable to Process!", Toast.LENGTH_LONG).show();
            }
        }
    }.execute();
}



         });

    }

    private  void saveImage2(Bitmap bm){

        String path = Environment.getExternalStorageDirectory().toString();
        OutputStream fOutputStream = null;
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File(path + "/Captures/", "screen.jpg");
        if (!file.exists()) {
            file.mkdirs();
        }

        try {
            fOutputStream = new FileOutputStream(file);

            bm.compress(Bitmap.CompressFormat.JPEG, 100, fOutputStream);

            fOutputStream.flush();
            fOutputStream.close();

            MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Save Failed", Toast.LENGTH_SHORT).show();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Save Failed", Toast.LENGTH_SHORT).show();
            return;
        }
    }

}