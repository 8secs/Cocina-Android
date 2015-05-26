package com.homecooking.ykecomo.ui.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.widget.ImageView;
import android.widget.Toast;

import com.homecooking.ykecomo.app.App;
import com.homecooking.ykecomo.ui.activity.image.NewPhoto;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoHandler implements Camera.PictureCallback {

    private final Context mContext;
    private ImageView mPreview;
    private NewPhoto mParent;

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    private int rotation;


    public PhotoHandler(Context context, ImageView preview, NewPhoto activity){
        this.mContext = context;
        this.mPreview = preview;
        this.mParent = activity;
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Camera.Parameters parameters = camera.getParameters();

        File pictureFileDir = mParent.getDir();
        if(!pictureFileDir.exists() && !pictureFileDir.mkdirs()){
            Toast.makeText(mContext, "No se puede crear el directorio para guardar la imagen", Toast.LENGTH_SHORT).show();
            return;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String date = dateFormat.format(new Date());
        String photoFile = "pic_" + date + ".jpg";
        mParent.fotoString = photoFile;

        String fileName = pictureFileDir.getPath() + File.separator + photoFile;
        mParent.mFileName = fileName;
        File pictureFile = new File(fileName);

        Bitmap thePicture = BitmapFactory.decodeByteArray(data, 0, data.length);
        Matrix m = new Matrix();
        m.postRotate(rotation);
        thePicture = Bitmap.createBitmap(thePicture, 0, 0, thePicture.getWidth(), thePicture.getHeight(), m, false);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        thePicture.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        data = bos.toByteArray();

        try{
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
            Toast.makeText(mContext, "Nueva imagen guardada " + photoFile, Toast.LENGTH_SHORT).show();
        }catch (Exception e) {
            Toast.makeText(mContext, "No se ha podido guardar la imagen", Toast.LENGTH_SHORT).show();
        }

        Bitmap result = App.decodeBitmapFromByteArray(data, parameters.getPictureSize().width, parameters.getPictureSize().height);
        //App.rotateBitmap(result);
        mPreview.setImageBitmap(result);
        //result = App.rotateBitmap(result);

        mParent.startCamera();
        mParent.setFileName();
    }

}
