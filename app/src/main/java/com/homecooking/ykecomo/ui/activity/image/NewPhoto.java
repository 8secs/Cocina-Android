package com.homecooking.ykecomo.ui.activity.image;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.homecooking.ykecomo.R;
import com.homecooking.ykecomo.app.Constants;
import com.homecooking.ykecomo.ui.utils.PhotoHandler;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("deprecation")
public class NewPhoto extends ActionBarActivity implements SurfaceHolder.Callback{

    ImageView iview;
    SurfaceHolder sHolder;
    SurfaceView sView;
    Camera mcam;
    private ImageButton btn_capture;
    private ImageButton publica;

    public String fotoString;
    public String mFileName;
    boolean isFirst = true;
    boolean isFrontCamera;


    private static final String CAMERA_PARAM_ORIENTATION = "orientation";
    private static final String CAMERA_PARAM_LANDSCAPE = "landscape";
    private static final String CAMERA_PARAM_PORTRAIT = "portrait";

    int cameraID;
    public int resultDeg;

    private PhotoHandler mpic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        isFrontCamera = extras.getBoolean(Constants.IS_FRONT_CAMERA);

        setContentView(R.layout.activity_new_photo);

        iview = (ImageView) findViewById(R.id.imageView1);
        sView = (SurfaceView) findViewById(R.id.surfaceView1);
        btn_capture = (ImageButton) findViewById(R.id.button1);
        sHolder = sView.getHolder();
        sHolder.addCallback(this);
        sHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        if(isFrontCamera) cameraID = getFrontCameraId();
        else cameraID = getBackCameraId();

        mpic = new PhotoHandler(NewPhoto.this, iview, this);

        sHolder.addCallback(this);
        btn_capture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mcam.takePicture(null, null, mpic);
            }
        });


        publica = (ImageButton) findViewById(R.id.publicar_foto_btn);
        publica.setVisibility(View.GONE);
        publica.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("filename",mFileName);
                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });
    }

    public File getDir() {
        File sdDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(sdDir, "HomeCooking");
    }

    public void startCamera(){
        mcam.release();
        mcam = null;
        try {
            mcam= Camera.open(cameraID);
            setCameraDisplayOrientation(this, cameraID, mcam);
            mcam.setPreviewDisplay(sHolder);
            mcam.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setFileName(){

        btn_capture.setVisibility(View.GONE);
        publica.setVisibility(View.VISIBLE);
    }

    private int getFrontCameraId(){
        int camId = -1;
        int numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo ci = new Camera.CameraInfo();

        for(int i = 0;i < numberOfCameras;i++){
            Camera.getCameraInfo(i, ci);
            if(ci.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
                camId = i;
            }
        }

        return camId;
    }

    private int getBackCameraId(){
        int camId = -1;
        int numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo ci = new Camera.CameraInfo();

        for(int i = 0;i < numberOfCameras;i++){
            Camera.getCameraInfo(i, ci);
            if(ci.facing == Camera.CameraInfo.CAMERA_FACING_BACK){
                camId = i;
            }
        }

        return camId;
    }

    public void setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
            resultDeg = result;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        mpic.setRotation(-result);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            Camera.Parameters parameters = getCameraParameters();
            if(isPortrait()){
                parameters.set(CAMERA_PARAM_ORIENTATION, CAMERA_PARAM_PORTRAIT);
            }else{
                parameters.set(CAMERA_PARAM_ORIENTATION, CAMERA_PARAM_LANDSCAPE);
            }
            mcam.setParameters(parameters);
        }else{
            camera.setDisplayOrientation(result);
        }

    }

    private Camera.Parameters getCameraParameters(){

        return mcam.getParameters();
    }

    private boolean isPortrait() {
        return (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        if (mcam != null) {
            try {
                mcam.setPreviewDisplay(sHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mcam.startPreview();
        }else{
            try {
                mcam= Camera.open(cameraID);
                if(mcam != null){
                    mcam.setPreviewDisplay(sHolder);
                    mcam.startPreview();
                }else{
                    Toast.makeText(getApplicationContext(), "ERROR: No hay cámara disponible", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if(mcam == null){
            try {
                mcam= Camera.open(cameraID);
                if(mcam != null){
                    mcam.setPreviewDisplay(sHolder);
                    mcam.startPreview();
                }else{
                    Toast.makeText(getApplicationContext(), "ERROR: No hay cámara disponible", Toast.LENGTH_SHORT).show();
                }
                if(isFirst == true){
                    setCameraDisplayOrientation(this, cameraID, mcam);
                    isFirst = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(mcam != null){
            mcam.stopPreview();
            mcam.release();
            mcam = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //getMenuInflater().inflate(R.menu.menu_new_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }else if(id == android.R.id.home ){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
