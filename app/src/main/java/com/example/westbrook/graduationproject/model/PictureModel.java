package com.example.westbrook.graduationproject.model;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.RequiresApi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by westbrook on 3/13/18.
 * 实现方法
 */

public class PictureModel implements IPictureImpl {

    @Override
    public Bitmap getFile(Context context,Uri imageUri) {
        try {
            return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(imageUri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return  null;
    }

    @Override
    public Bitmap getFile(Context context,Intent data) {

        if(Build.VERSION.SDK_INT>=19)
            return  handleOnKitkat(context,data);
        else
            return handleBeforeKitkat(context,data);
    }

    @Override
    public void savePicture(Bitmap bitmap) {
        //是否可以对SDcard操作
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            String sdCardDir=Environment.getExternalStorageDirectory()+File.separator+"androidPicture"+File.separator;
            File file=new File(sdCardDir);
            if(!file.exists())
                file.mkdir();

            File file1=new File(sdCardDir, System.currentTimeMillis()+".jpg");
            try {
                FileOutputStream stream=new FileOutputStream(file1);
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private Bitmap handleOnKitkat(Context context,Intent data) {
        String imagePath=null;
        Uri uri=data.getData();
        if(DocumentsContract.isDocumentUri(context,uri)){
            String docId=DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id=docId.split(":")[1];
                String selection= MediaStore.Images.Media._ID+"="+id;
                imagePath=getImagePath(context,MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);

            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://dowmloads/public_downloads"),Long.valueOf(docId));
                imagePath=getImagePath( context,contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            imagePath=getImagePath( context,uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            imagePath=uri.getPath();
        }
        return BitmapFactory.decodeFile(imagePath);
    }


    private Bitmap handleBeforeKitkat(Context context,Intent data) {
        Uri uri=data.getData();
        String imagePath=getImagePath(context,uri,null);
        return BitmapFactory.decodeFile(imagePath);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private String getImagePath(Context context,Uri uri, String selection){
        String path=null;
        @SuppressLint("Recycle") Cursor cursor=context.getContentResolver().query(uri,null,selection,null,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst())
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        }
        return  path;
    }
}
