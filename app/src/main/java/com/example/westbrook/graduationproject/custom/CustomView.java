package com.example.westbrook.graduationproject.custom;

import android.annotation.SuppressLint;
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
import android.util.Log;
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



    //画笔
    private Paint mPaint;

    //记录路线
    private Path mPath;

    private Canvas mCanvas;
    private Bitmap drawBitmap;
    private float lastX;
    private float lastY;


    public void setOnCLick(boolean onCLick,int type) {
        isOnCLick = onCLick;
        if(isOnCLick){
            mPaint=new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.STROKE);
            mPath=new Path();
        }
        if(type==1){
            //涂鸦
            paintWidth=10;
            mPaint.setStrokeWidth(paintWidth);
            paintColor=Color.RED;
            mPaint.setColor(paintColor);
            drawBitmap=changeDrawable();
            mCanvas=new Canvas(drawBitmap);
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
            canvas.drawBitmap(drawBitmap,0,0,null);
        }else {
            super.onDraw(canvas);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(isOnCLick){
            Log.d("MainActivity", "onTouchEvent: "+event.getAction());
            float x=event.getX();
            float y=event.getY();
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    lastX=x;
                    lastY=y;
                    mPath.moveTo(x,y);
                    break;
                case MotionEvent.ACTION_UP:
                    //每次抬起之后记录当前的颜色和轨迹
                    mPath.reset();
                    break;
                case MotionEvent.ACTION_MOVE:
                    mPath.quadTo(lastX,lastY,x,y);
                    drawBitmap();
                    invalidate();
                    lastY=y;
                    lastX=x;
                    break;
            }
            return  true;

        }else {
            return super.onTouchEvent(event);
        }

    }
    void drawBitmap(){
        mCanvas.drawPath(mPath,mPaint);
    }


    private Bitmap changeDrawable(){
        Bitmap bitmap=null;
        Drawable drawable=getDrawable();

        int w=drawable.getIntrinsicWidth();
        int h=drawable.getIntrinsicHeight();
        bitmap=Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        drawable.setBounds(0,0,w,h);
        drawable.draw(canvas);
        return bitmap;
    }
}
