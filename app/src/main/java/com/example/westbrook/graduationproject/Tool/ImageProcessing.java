package com.example.westbrook.graduationproject.Tool;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ThumbnailUtils;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.westbrook.graduationproject.R;

/**
 * Created by westbrook on 2018/1/21.
 * 图像处理类
 */

public class ImageProcessing {
    public static Bitmap rotate(Bitmap initiallyBitmap){
        Bitmap bitmap=null;
        Matrix matrix=new Matrix();
        matrix.setRotate(90);
        bitmap= Bitmap.createBitmap(initiallyBitmap,0,0,
                initiallyBitmap.getWidth(),initiallyBitmap.getHeight(),matrix,true);
        return  bitmap;
    }

    public static Bitmap screenAction(Bitmap currentBitmap, float mHue, float mS, float mL) {
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
    public static Bitmap grayProcessing(Bitmap currentBitmap){
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
    public static Bitmap reverseProcessing(Bitmap currentBitmap){
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
    public static Bitmap nostalgiaProcessing(Bitmap currentBitmap){
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
    public static Bitmap toColorEffect(Bitmap currentBitmap){
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
    public static Bitmap highSaturation(Bitmap currentBitmap){
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

    public static Bitmap handleImageNative(Bitmap bm){
        int Width = bm.getWidth();
        int Height = bm.getHeight();
        int color;
        int r,g,b,a;

        Bitmap bitmap = Bitmap.createBitmap(Width, Height, Bitmap.Config.ARGB_8888);

        int[] oldPx = new int[Width * Height];
        int[] newPx = new int[Width * Height];

        bm.getPixels(oldPx, 0, Width, 0, 0, Width, Height);

        for(int i = 0; i < Width * Height; i++){
            color = oldPx[i];

            r = Color.red(color);
            g = Color.green(color);
            b = Color.blue(color);
            a = Color.alpha(color);

            //各通道值颜色值反转
            r = 255 - r;
            g = 255 - g;
            b = 255 - b;

            //检测各通道值是否超出范围
            if(r > 255){
                r = 255;
            }else if(r < 0){
                r = 0;
            }

            if(g > 255){
                g = 255;
            }else if(g < 0){
                g = 0;
            }

            if(b > 255){
                b = 255;
            }else if(b < 0){
                b = 0;
            }
            newPx[i] = Color.argb(a, r, g, b);
        }
        bitmap.setPixels(newPx, 0, Width, 0, 0, Width, Height);
        return bitmap;
    }

    //浮雕
    public static Bitmap Carving(Bitmap bm){
        int Width = bm.getWidth();
        int Height = bm.getHeight();

        Bitmap bitmap = Bitmap.createBitmap(Width, Height, Bitmap.Config.ARGB_8888);

        int color = 0,colorBefore = 0;
        int a,r,g,b;
        int r1,g1,b1;

        int[] oldPx = new int[Width * Height];
        int[] newPx = new int[Width * Height];

        bm.getPixels(oldPx, 0, Width, 0, 0, Width, Height);
        for(int i = 1; i < Width * Height; i++){
            colorBefore = oldPx[i - 1];
            a = Color.alpha(colorBefore);
            r = Color.red(colorBefore);
            g = Color.green(colorBefore);
            b = Color.blue(colorBefore);

            color = oldPx[i];

            r1 = Color.red(color);
            g1 = Color.green(color);
            b1 = Color.blue(color);

            r = (r - r1 + 127);
            g = (g - g1 + 127);
            b = (b - b1 + 127);

            //检查各点像素值是否超出范围
            if(r > 255){
                r = 255;
            }

            if(g > 255){
                g = 255;
            }

            if(b > 255){
                b = 255;
            }
            newPx[i] = Color.argb(a, r, g, b);
        }

        bitmap.setPixels(newPx, 0, Width, 0, 0, Width, Height);
        return bitmap;
    }
    //老照片
    public static Bitmap oldPicture(Bitmap bm){
        int Width = bm.getWidth();
        int Height = bm.getHeight();
        int color;
        int r,g,b,a;

        Bitmap bitmap = Bitmap.createBitmap(Width, Height, Bitmap.Config.ARGB_8888);

        int[] oldPx = new int[Width * Height];
        int[] newPx = new int[Width * Height];

        bm.getPixels(oldPx, 0, Width, 0, 0, Width, Height);

        for(int i = 0; i < Width * Height; i++){
            color = oldPx[i];

            r = Color.red(color);
            g = Color.green(color);
            b = Color.blue(color);
            a = Color.alpha(color);

            //各通道值颜色值反转
            r = (int) (0.393*+0.769*g+0.189*b);
            g =(int) (0.349*+0.686*g+0.168*b);
            b = (int) (0.272*+0.534*g+0.131*b);

            //检测各通道值是否超出范围
            if(r > 255){
                r = 255;
            }else if(r < 0){
                r = 0;
            }

            if(g > 255){
                g = 255;
            }else if(g < 0){
                g = 0;
            }

            if(b > 255){
                b = 255;
            }else if(b < 0){
                b = 0;
            }
            newPx[i] = Color.argb(a, r, g, b);
        }
        bitmap.setPixels(newPx, 0, Width, 0, 0, Width, Height);
        return bitmap;
    }


    /**
     * 转为二值图像
     *
     * @param bmp
     *            原图bitmap
     * @return
     */
    public static Bitmap convertToBMW(Bitmap bmp,int tmp) {
        int width = bmp.getWidth(); // 获取位图的宽
        int height = bmp.getHeight(); // 获取位图的高
        int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组
        // 设定二值化的域值，默认值为100
        //tmp = 180;
        // 新建图片
        Bitmap newBmp = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888);
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];
                // 分离三原色
                alpha = ((grey & 0xFF000000) >> 24);
                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);//第一部分：”0x“类型表示的此数值是16进制。
                int blue = (grey & 0x000000FF);//第二部分：”000000ff“表示的是16进制下的数值是”ff“，转换为10进制就是”255“
                if (red > tmp) {
                    red = 255;
                } else {
                    red = 0;
                }
                if (blue > tmp) {
                    blue = 255;
                } else {
                    blue = 0;
                }
                if (green > tmp) {
                    green = 255;
                } else {
                    green = 0;
                }
                pixels[width * i + j] = alpha << 24 | red << 16 | green << 8
                        | blue;
                if (pixels[width * i + j] == -1) {
                    pixels[width * i + j] = -1;
                } else {
                    pixels[width * i + j] = -16777216;
                }
            }
        }

        // 设置图片数据
        newBmp.setPixels(pixels, 0, width, 0, 0, width, height);
        return newBmp;
    }



        //图片马赛克
    public static Bitmap doBlur(Bitmap sentBitmap, int radius, boolean canReuseInBitmap) {
        Bitmap bitmap;
        if (canReuseInBitmap) {
            bitmap = sentBitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }

}
