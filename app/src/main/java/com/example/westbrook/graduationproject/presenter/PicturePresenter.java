package com.example.westbrook.graduationproject.presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.example.westbrook.graduationproject.Tool.PictureView;
import com.example.westbrook.graduationproject.model.IPictureImpl;
import com.example.westbrook.graduationproject.model.PictureModel;

/**
 * Created by westbrook on 3/13/18.
 *MVP模式中的P
 */

public class PicturePresenter {
    PictureModel model;
    PictureView view;

    public PicturePresenter(PictureView view) {
        this.view = view;
        model=new PictureModel();
    }

    public Bitmap getFile(Context context, Uri uri){
       return model.getFile(context,uri);
    }
    public Bitmap getFile(Context context,Intent intent){
        return model.getFile(context,intent);
    }
}
