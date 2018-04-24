package com.example.westbrook.graduationproject.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;


public class CustomView extends android.support.v7.widget.AppCompatImageView {

    //是否开启触摸事件
    private boolean isOnCLick=false;

    //涂鸦时候画笔的宽度
    private int paintWidth;

    //涂鸦是画笔的颜色

    private int paintColor;

    //记录画笔的集合

    private List<MyPath> mPaths;

    //画笔
    private Paint mPaint;

    //记录路线
    private Path mPath;

    private Canvas mCanvas;


    public void setOnCLick(boolean onCLick,int type) {
        isOnCLick = onCLick;
        if(isOnCLick){
            mPaint=new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaths=new ArrayList<>();
            mPath=new Path();
        }
        if(type==1){
            //涂鸦
            mPaint.setStrokeWidth(10);
            mPaint.setColor(Color.parseColor("#fff"));
            mCanvas=new Canvas(changeDrawable());
        }
        if(type==2){

        }
    }

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
       if(isOnCLick){

       }else {
           super.onDraw(canvas);
       }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(isOnCLick){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                case MotionEvent.ACTION_MOVE:

                    break;
            }
            return  true;

        }else {
            return super.onTouchEvent(event);
        }

    }

    static class MyPath{
        Path mPath;
        Color mColor;
        int width;

        public MyPath(Path path, Color color, int width) {
            mPath = path;
            mColor = color;
            this.width = width;
        }
    }

    private Bitmap changeDrawable(){
        Bitmap bitmap=null;
        Drawable drawable=getDrawable();
        if(drawable instanceof BitmapDrawable){
            return ((BitmapDrawable) drawable).getBitmap();
        }
        int w=drawable.getIntrinsicWidth();
        int h=drawable.getIntrinsicHeight();
        bitmap=Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        drawable.setBounds(0,0,w,h);
        drawable.draw(canvas);
        return bitmap;
    }
}
