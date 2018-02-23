package com.example.westbrook.graduationproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.v4.graphics.BitmapCompat;

/**
 * Created by westbrook on 2018/1/21.
 * 图像处理类
 */

class ImageProcessing {
    static Bitmap rotate(Bitmap initiallyBitmap){
        Bitmap bitmap=null;
        Matrix matrix=new Matrix();
        matrix.setRotate(90);
        bitmap= Bitmap.createBitmap(initiallyBitmap,0,0,
                initiallyBitmap.getWidth(),initiallyBitmap.getHeight(),matrix,true);
        return  bitmap;
    }

    static Bitmap screenAction(Bitmap currentBitmap, float mHue, float mS, float mL) {
        Bitmap bitmap=null;
        bitmap= Bitmap.createBitmap(currentBitmap.getWidth(),currentBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        Paint paint=new Paint();

        ColorMatrix matrix=new ColorMatrix();
        matrix.setRotate(0,mHue);
        matrix.setRotate(1,mHue);
        matrix.setRotate(2,mHue);

        ColorMatrix saturationMatrix=new ColorMatrix();
        saturationMatrix.setSaturation(mS);


        ColorMatrix lumMatrix=new ColorMatrix();
        lumMatrix.setScale(mL,mL,mL,1);


        ColorMatrix imageMatrix =new ColorMatrix();
        imageMatrix.postConcat(matrix);
        imageMatrix.postConcat(saturationMatrix);
        imageMatrix.postConcat(lumMatrix);
        paint.setColorFilter(new ColorMatrixColorFilter(imageMatrix));
        canvas.drawBitmap(currentBitmap,0,0,paint);


        return  bitmap;
    }

    /*
      灰度处理
       */
    static Bitmap grayProcessing(Bitmap currentBitmap){
        Bitmap bitmap=null;
        bitmap= Bitmap.createBitmap(currentBitmap.getWidth(),currentBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        Paint paint=new Paint();
        ColorMatrix imageMatrix =new ColorMatrix();
        imageMatrix.set(new float[]{0.33F,0.59F,0.11F,0,0,0.33F,0.59F,0.11F,0,0,0.33F,0.59F,0.11F,0,0,0,0,0,1,0});
        paint.setColorFilter(new ColorMatrixColorFilter(imageMatrix));
        canvas.drawBitmap(currentBitmap,0,0,paint);


        return  bitmap;
    }/*
      反转处理
       */
    static Bitmap reverseProcessing(Bitmap currentBitmap){
        Bitmap bitmap=null;
        bitmap= Bitmap.createBitmap(currentBitmap.getWidth(),currentBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        Paint paint=new Paint();
        ColorMatrix imageMatrix =new ColorMatrix();
        imageMatrix.set(new float[]{-1,0,0,1,1,0,-1,0,1,1,0,0,-1,1,1,0,0,0,1,0});
        paint.setColorFilter(new ColorMatrixColorFilter(imageMatrix));
        canvas.drawBitmap(currentBitmap,0,0,paint);


        return  bitmap;
    }
    /*
    怀旧效果
     */
    static Bitmap nostalgiaProcessing(Bitmap currentBitmap){
        Bitmap bitmap=null;
        bitmap= Bitmap.createBitmap(currentBitmap.getWidth(),currentBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        Paint paint=new Paint();
        ColorMatrix imageMatrix =new ColorMatrix();
        imageMatrix.set(new float[]{0.393F,0.769F,0.189F,0,0,0.349F,0.686F,0.168F,0,0,0.272F,0.534F,0.131F,0,0,0,0,0,1,0});
        paint.setColorFilter(new ColorMatrixColorFilter(imageMatrix));
        canvas.drawBitmap(currentBitmap,0,0,paint);


        return  bitmap;
    }

    /*
    去色效果
     */
    static Bitmap toColorEffect(Bitmap currentBitmap){
        Bitmap bitmap=null;
        bitmap= Bitmap.createBitmap(currentBitmap.getWidth(),currentBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        Paint paint=new Paint();
        ColorMatrix imageMatrix =new ColorMatrix();
        imageMatrix.set(new float[]{1.5F,1.5F,1.5F,0,-1,1.5F,1.5F,1.5F,0,-1,1.5F,1.5F,1.5F,0,-1,0,0,0,1,0});
        paint.setColorFilter(new ColorMatrixColorFilter(imageMatrix));
        canvas.drawBitmap(currentBitmap,0,0,paint);
        return  bitmap;
    }
    /*
    高饱和度
     */
    static Bitmap highSaturation(Bitmap currentBitmap){
        Bitmap bitmap=null;
        bitmap= Bitmap.createBitmap(currentBitmap.getWidth(),currentBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        Paint paint=new Paint();
        ColorMatrix imageMatrix =new ColorMatrix();
        imageMatrix.set(new float[]{1.438F,-0.122F,-0.016F,0,-0.03F,-0.062F,1.378F,-0.016F,0,0.05F,-0.062F,-0.122F,1.438F,0,-0.02F,0,0,0,1,0});
        paint.setColorFilter(new ColorMatrixColorFilter(imageMatrix));
        canvas.drawBitmap(currentBitmap,0,0,paint);
        return  bitmap;
    }

}
