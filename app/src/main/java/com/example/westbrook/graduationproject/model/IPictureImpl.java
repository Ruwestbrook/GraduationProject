package com.example.westbrook.graduationproject.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by westbrook on 3/13/18.
 * model接口
 * 用来存贮数据并且提供数据
 */

public interface IPictureImpl {
    public Bitmap getFile(Context context, Uri imageUri);
    public Bitmap getFile(Context context,Intent data);
    public void savePicture(Bitmap bitmap);
}
