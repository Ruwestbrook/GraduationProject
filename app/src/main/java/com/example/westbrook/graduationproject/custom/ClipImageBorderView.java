package com.example.westbrook.graduationproject.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

public class ClipImageBorderView extends View
{
	private static final String TAG = "ClipImageBorderView";
	/**
	 * 水平方向与View的边距
	 */
	private int mHorizontalPadding;
	/**
	 * 垂直方向与View的边距
	 */
	private int mVerticalPadding;
	/**
	 * 绘制的矩形的宽度
	 */
	private int mWidth;
	/**
	 * 边框的颜色，默认为白色
	 */
	private int mBorderColor = Color.parseColor("#FFFFFF");
	/**
	 * 边框的宽度 单位dp
	 */
	private int mBorderWidth = 1;
	/*
	 总的宽度
	 */
	private  int width;
	/**
	 * 总的高度
	 */
	private int height;

	private Paint mPaint;

	//分别是上左,上右,下左,下右是否可以移动
	private boolean[] isMove=new boolean[4];

	//分别是上下左右的距离
	private float[] distance={0,0,40,40};

	public ClipImageBorderView(Context context)
	{
		this(context, null);
	}

	public ClipImageBorderView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public ClipImageBorderView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	
		mBorderWidth = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, mBorderWidth, getResources()
						.getDisplayMetrics());
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width=getWidth();
		height=getHeight();
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		// 计算矩形区域的宽度
		mWidth = getWidth() - 2 * mHorizontalPadding;
		distance[1]=mVerticalPadding;
		// 计算距离屏幕垂直边界 的边距
		mVerticalPadding = (getHeight() - mWidth) / 2;
		distance[0]=mVerticalPadding;
		distance[1]=mVerticalPadding;
		mPaint.setColor(Color.parseColor("#aa000000"));
		mPaint.setStyle(Style.FILL);
		// 绘制左边1
		canvas.drawRect(0, 0, mHorizontalPadding, getHeight(), mPaint);
		// 绘制右边2
		canvas.drawRect(getWidth() - mHorizontalPadding, 0, getWidth(),
				getHeight(), mPaint);
		// 绘制上边3
		canvas.drawRect(mHorizontalPadding, 0, getWidth() - mHorizontalPadding,
				mVerticalPadding, mPaint);
		// 绘制下边4
		canvas.drawRect(mHorizontalPadding, getHeight() - mVerticalPadding,
				getWidth() - mHorizontalPadding, getHeight(), mPaint);
		// 绘制外边框
		mPaint.setColor(mBorderColor);
		mPaint.setStrokeWidth(mBorderWidth);
		mPaint.setStyle(Style.STROKE);
		canvas.drawRect(mHorizontalPadding, mVerticalPadding, getWidth()
				- mHorizontalPadding, getHeight() - mVerticalPadding, mPaint);

	}

	public void setHorizontalPadding(int mHorizontalPadding)
	{
		this.mHorizontalPadding = mHorizontalPadding;
	}

}
