package com.example.westbrook.graduationproject.custom;

/**
 * Created by westbrook on 2018/5/14.
 * GraffitiView 涂鸦
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;


import java.util.ArrayList;
public class GraffitiView extends android.support.v7.widget.AppCompatImageView {
    private Bitmap mMosaicBmp;
    private Paint mPaint;
    private ArrayList<DrawPath> mPaths;
    private DrawPath mLastPath;
    private RectF mBitmapRectF;
    private PorterDuffXfermode mDuffXfermode;
    private float tempX,tempY;
    private int paintWidth=10;
    private  int paintColor=Color.BLACK;
    public GraffitiView(Context context) {
        this(context,null);

    }

    public GraffitiView(Context context, AttributeSet attrs) {
        this(context, attrs,0);

    }

    public GraffitiView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);//描边
        mPaint.setColor(paintColor);
        mPaint.setStrokeWidth(paintWidth);
        mPaths = new ArrayList<>();
        mBitmapRectF = new RectF();
        //叠加效果
        mDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    }
  public void setWidth(int width){
        if(mPaint!=null){
            mPaint.setStrokeWidth(width);
            paintWidth=width;
        }
  }
  public void setColor(int color){
      if(mPaint!=null){
          mPaint.setColor(color);
          paintColor=color;
      }
  }
    /**
     * 清楚操作
     * */
    public void clear() {
        mPaths.clear();
        invalidate();
    }

    /**
     * 撤销
     * */
    public void undo() {
        int size  = mPaths.size();
        if (size > 0) {
            mPaths.remove(size-1);
            invalidate();
        }
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        if (drawable != null && drawable instanceof BitmapDrawable) {
            scaleBitmap(((BitmapDrawable) drawable).getBitmap());
        }
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        scaleBitmap(bm);
    }

    // 生成小图
    private void scaleBitmap(Bitmap bm) {
        int width = bm.getWidth();
        float mTargetWidth = 20.0f;
        float scale = mTargetWidth / width;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        mMosaicBmp = Bitmap.createBitmap(bm, 0, 0, width, bm.getHeight(),
                matrix, true);
    }

    /**
     * 得到图片展示区域
     */
    private RectF getBitmapRect() {
        final Drawable drawable = getDrawable();
        if (drawable == null) {
            return new RectF();
        }
        // Get image matrix values and place them in an array.
        final float[] matrixValues = new float[9];
        getImageMatrix().getValues(matrixValues);

        // Extract the scale and translation values from the matrix.
        final float scaleX = matrixValues[Matrix.MSCALE_X];
        final float scaleY = matrixValues[Matrix.MSCALE_Y];
        final float transX = matrixValues[Matrix.MTRANS_X] + getPaddingLeft();
        final float transY = matrixValues[Matrix.MTRANS_Y] + getPaddingTop();

        // Get the width and height of the original bitmap.
        final int drawableIntrinsicWidth = drawable.getIntrinsicWidth();
        final int drawableIntrinsicHeight = drawable.getIntrinsicHeight();

        // Calculate the dimensions as seen on screen.
        final int drawableDisplayWidth = Math.round(drawableIntrinsicWidth * scaleX);
        final int drawableDisplayHeight = Math.round(drawableIntrinsicHeight * scaleY);

        // Get the Rect of the displayed image within the ImageView.
        final float left = Math.max(transX, 0);
        final float top = Math.max(transY, 0);
        final float right = Math.min(left + drawableDisplayWidth, getWidth());
        final float bottom = Math.min(top + drawableDisplayHeight, getHeight());

        return new RectF(left, top, right, bottom);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mMosaicBmp != null) {
            mBitmapRectF = getBitmapRect();
            Matrix mosaicMatrix = new Matrix();
            mosaicMatrix.setTranslate(mBitmapRectF.left, mBitmapRectF.top);
            float scaleX = (mBitmapRectF.right - mBitmapRectF.left) / mMosaicBmp.getWidth();
            float scaleY = (mBitmapRectF.bottom - mBitmapRectF.top) / mMosaicBmp.getHeight();
            mosaicMatrix.postScale(scaleX, scaleY);
            // 生成整张模糊图片
            mMosaicBmp = Bitmap.createBitmap(mMosaicBmp, 0, 0, mMosaicBmp.getWidth(), mMosaicBmp.getHeight());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!mPaths.isEmpty()){
            int canvasWidth = canvas.getWidth();
            int canvasHeight = canvas.getHeight();
            //新图层
            int layerId = canvas.saveLayer(0, 0, canvasWidth, canvasHeight, null, Canvas.ALL_SAVE_FLAG);
            canvas.clipRect(mBitmapRectF); //限定区域
            for(DrawPath drawPath:mPaths){
                //滑过的区域
                drawPath.draw(canvas);
            }
            mPaint.setXfermode(mDuffXfermode);//设置叠加模式
            canvas.drawBitmap(mMosaicBmp, mBitmapRectF.left, mBitmapRectF.top, mPaint);//画出重叠区域
            mPaint.setXfermode(null);
            canvas.restoreToCount(layerId);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                float downX = event.getX();
                float downY = event.getY();
                mLastPath = new DrawPath();//每次手指下去都是一条新的路径
                mLastPath.moveTo(downX,downY);
                mLastPath.color=paintColor;
                mLastPath.width=paintWidth;
                mPaths.add(mLastPath);
                invalidate();
                tempX = downX;
                tempY = downY;
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();
                if (Math.abs(moveX - tempX) > 5 || Math.abs(moveY - tempY) > 5) {
                    mLastPath.quadTo(tempX,tempY,moveX,moveY);
                    invalidate();
                }
                tempX = moveX;
                tempY = moveY;
                break;
            case MotionEvent.ACTION_UP:
                mLastPath.up();
                break;
        }
        return true;
    }

    /**
     * 封装一条路径
     */
    class  DrawPath {
        Path path;
        float downX;
        float downY;
        boolean quaded;
        int color;
        int width;

        DrawPath() {
            path = new Path();
        }

        void moveTo(float x, float y) {
            downX = x;
            downY = y;
            path.moveTo(x, y);
        }

        void quadTo(float x1, float y1, float x2, float y2) {
            quaded = true;
            path.quadTo(x1, y1, x2, y2);
        }

        void up() {
            if (!quaded) {
                //画点
                path.lineTo(downX, downY + 0.1f);
                invalidate();
            }
        }

        void draw(Canvas canvas) {
            if (path != null) {
                mPaint.setStrokeWidth(width);
                mPaint.setColor(color);
                canvas.drawPath(path, mPaint);
            }
        }
    }
}

