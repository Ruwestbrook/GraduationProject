package com.example.westbrook.graduationproject.Tool;

/**
 * Created by westbrook on 3/13/18.
 * MVP模式中activity注重视图表现
 *
 * 1.显示选择文件
 * 2.文件选择成功 显示图片 选择框消失
 *
 * 3.每次修改图片后图片的重新显示
 *
 */

public interface PictureView {
    public void showChooseFile(int type);
    public void showNewPicture();
    public void showSeekBar();
}
