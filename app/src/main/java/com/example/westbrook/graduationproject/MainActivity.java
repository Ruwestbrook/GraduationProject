package com.example.westbrook.graduationproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.westbrook.graduationproject.Tool.ImageProcessing;
import com.example.westbrook.graduationproject.Tool.Tool;
import com.example.westbrook.graduationproject.Tool.itemAdapter;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity  implements SeekBar.OnSeekBarChangeListener{
    private Button leftButton;
    private Button  rightButton;
    private RecyclerView mRecyclerView;
    private static final String TAG = "MainActivity";
    private ImageView imageView;
    private LinearLayout backgroundLinearLayout;
    private LinearLayout actionLinearLayout;
    private  PopupWindow popupWindow;

    //回退的按钮操作类型
    private int flag=0;
    //拍照的路径
    private Uri imageUri;
    //初始的bitmap
    private Bitmap initiallyBitmap;

    //当前显示的Bitamp
    private Bitmap currentBitmap;

    //需要操作的bitmap
    private Bitmap holdBitmap;
    //是否灰度处理
    private boolean isGray=false;
    //是否怀旧处理
    private boolean isNostalgia=false;
    private boolean isReverse=false;
    private Tool mTool;

    private int tmp=128;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTool=new Tool(this);
        mRecyclerView=findViewById(R.id.recycler_view);
        imageView=findViewById(R.id.pic);
        backgroundLinearLayout=findViewById(R.id.background);
        actionLinearLayout=findViewById(R.id.action);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        String[] a={"旋转","色相","灰度效果","怀旧","反转","去色","高饱和度","底片","浮雕","老照片","二值化","添加马赛克","涂鸦","剪裁"};//"色调","饱和度","亮度",
        itemAdapter adapter=new itemAdapter(a);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(manager);
        adapter.setItemClick(new itemAdapter.onItemClick() {
            @Override
            public void itemClick(int position) {
             if(currentBitmap==null){
                 Toast.makeText(MainActivity.this, "请先选择图片", Toast.LENGTH_SHORT).show();
                 return;
             }

             switch (position){
                 case 0: //旋转
                     currentBitmap= ImageProcessing.rotate(currentBitmap);
                     imageView.setImageBitmap(currentBitmap);
                     break;
                 case 1://改变颜色系数
                        showSeekBar(1);
                     flag=2;
                     holdBitmap=currentBitmap;
                     break;
                 case 2:
                     if(!isGray){
                         holdBitmap= ImageProcessing.grayProcessing(currentBitmap);
                         imageView.setImageBitmap(holdBitmap);

                     }else {
                         imageView.setImageBitmap(currentBitmap);
                     }
                     isGray=!isGray;
                     break;
                     case 3:
                     if(!isNostalgia){
                         holdBitmap= ImageProcessing.nostalgiaProcessing(currentBitmap);
                         imageView.setImageBitmap(holdBitmap);
                     }else {
                         imageView.setImageBitmap(currentBitmap);
                     }
                         isNostalgia=!isNostalgia;
                     break;
                 case 4:
                     if(!isReverse){
                         holdBitmap= ImageProcessing.reverseProcessing(currentBitmap);
                         imageView.setImageBitmap(holdBitmap);
                     }else {
                         imageView.setImageBitmap(currentBitmap);
                     }
                     isReverse=!isReverse;
                     break;
                 case 5:
                     holdBitmap= ImageProcessing.toColorEffect(currentBitmap);
                         imageView.setImageBitmap(holdBitmap);

                     break;
                 case 6:
                     holdBitmap= ImageProcessing.highSaturation(currentBitmap);
                     imageView.setImageBitmap(holdBitmap);
                     break;
                 case 7:
                     holdBitmap= ImageProcessing.handleImageNative(currentBitmap);
                     imageView.setImageBitmap(holdBitmap);
                     break;
                 case 8:
                     holdBitmap= ImageProcessing.Carving(currentBitmap);
                     imageView.setImageBitmap(holdBitmap);
                     break;
                 case 9:
                     holdBitmap= ImageProcessing.oldPicture(currentBitmap);
                     imageView.setImageBitmap(holdBitmap);
                     break;
                 case 10:
                     showSeekBar(2);
                     holdBitmap= ImageProcessing.convertToBMW(currentBitmap,tmp);
                     imageView.setImageBitmap(holdBitmap);
                     break;
                 case 11:
                     //马赛克

                     break;

                 case 12:
                     //涂鸦
                     break;
                 case 13:
                     //剪裁
                     break;

             }

            }
        });

//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                flag=1;
//                showChooseFile();
//            }
//        });

        backgroundLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=1;
                showChooseFile();
            }
        });




    }


    @Override
    public void onBackPressed() {
        switch (flag){
            case 0:
                super.onBackPressed();
                break;
            case 1:
                if(popupWindow!=null){
                    popupWindow.dismiss();
                    popupWindow=null;
                    flag=0;
                }
            case 2:
                if(actionLinearLayout!=null){
                    actionLinearLayout.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    flag=0;
                }
                break;
                default:
                    super.onBackPressed();
                    break;
        } 
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                //相机
                if(resultCode==RESULT_OK) {
                    initiallyBitmap= mTool.getFile(this, imageUri);
                }
                break;
            case 2:
                //相册
                if(resultCode==RESULT_OK){
                    initiallyBitmap= mTool.getFile(this,data);
                }

                break;
                default:
                    break;
        }
        popupWindow.dismiss();
        Log.d(TAG, "onActivityResult: "+backgroundLinearLayout.getWidth());
        Log.d(TAG, "onActivityResult: "+backgroundLinearLayout.getHeight());
        Log.d(TAG, "onActivityResult: 图片宽度"+initiallyBitmap.getWidth());
        Log.d(TAG, "onActivityResult: 图片高度"+initiallyBitmap.getHeight());
        if(initiallyBitmap.getHeight()>1326){
            initiallyBitmap=mTool.zoomBitmap(initiallyBitmap);
        }else if(initiallyBitmap.getWidth()>1080){
            initiallyBitmap=mTool.zoomBitmap(initiallyBitmap);
        }

        currentBitmap=initiallyBitmap;
        imageView.setImageBitmap(initiallyBitmap);
    }



    //权限的申请
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                //权限通过
                if(grantResults.length>0 && PackageManager.PERMISSION_GRANTED==grantResults[0])
                    showChooseFile();
                else
                    Toast.makeText(this, "您未授予权限, 软件无法使用", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                //权限通过
                if(grantResults.length>0 && PackageManager.PERMISSION_GRANTED==grantResults[0])
                {
                    Intent intent=null;
                    intent=new Intent("android.intent.action.GET_CONTENT");
                    intent.setType("image/*");
                    startActivityForResult(intent,2);
                }
                else
                    Toast.makeText(this, "您未授予权限, 软件无法使用", Toast.LENGTH_SHORT).show();


        }
    }
    float mHue = 1,mS = 1,mL=1;
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()){
            case R.id.seekOne:
                mHue=(progress-50)*1.0F/50*180;
                currentBitmap= ImageProcessing.screenAction(holdBitmap,mHue,mS,mL);
                imageView.setImageBitmap(currentBitmap);
                break;
            case R.id.seekTwo:
                mS=progress*1.0F/50;
                currentBitmap= ImageProcessing.screenAction(holdBitmap,mHue,mS,mL);
                imageView.setImageBitmap(currentBitmap);
                break;
            case R.id.seekThree:
                mL=progress*1.0F/50;
                currentBitmap= ImageProcessing.screenAction(holdBitmap,mHue,mS,mL);
                imageView.setImageBitmap(currentBitmap);
                break;
            case R.id.seekFour:
                tmp= (int) (progress*2.56);
                holdBitmap= ImageProcessing.convertToBMW(currentBitmap,tmp);
                imageView.setImageBitmap(holdBitmap);
                break;
        }


    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void showChooseFile() {

        popupWindow=new PopupWindow(mTool.dp2px(250),mTool.dp2px(100));
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
                popupWindow.dismiss();
            }
        });
        View view= LayoutInflater.from(this).inflate(R.layout.choose_file,null);
        popupWindow.setContentView(view);
        backgroundAlpha(0.3f);
        popupWindow.showAtLocation(this.getWindow().getDecorView(),   Gravity.CENTER,0,0);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        TextView chooseFile=view.findViewById(R.id.choose_file);

        chooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
                }else {
                  Intent intent=null;
                intent=new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                startActivityForResult(intent,2);

                }
            }
        });
        TextView takePhoto=view.findViewById(R.id.take_photo);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file=new File(getExternalCacheDir(),"bitmap.jpg");
                try {
                    if(file.exists()){
                        file.delete();
                    }
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent=null;
                if(Build.VERSION.SDK_INT>=24){
                    imageUri= FileProvider.getUriForFile(MainActivity.this,"com.westbrook.project.fileProvider",file);
                }else {
                    imageUri= Uri.fromFile(file);
                }
                //打开相机
                intent=new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent,1);

            }
        });

    }




    public void showSeekBar(int type) {
        View view;
        LinearLayout.LayoutParams layoutParams;
      switch (type){
          case 1:
               view= LayoutInflater.from(MainActivity.this).inflate(R.layout.screen_action,null);
              layoutParams= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
              actionLinearLayout.addView(view,layoutParams);
              mRecyclerView.setVisibility(View.GONE);
              actionLinearLayout.setVisibility(View.VISIBLE);
              SeekBar bar=view.findViewById(R.id.seekOne);
              bar.setOnSeekBarChangeListener(MainActivity.this);
              SeekBar bar1=view.findViewById(R.id.seekTwo);
              bar1.setOnSeekBarChangeListener(MainActivity.this);
              SeekBar bar2=view.findViewById(R.id.seekThree);
              bar2.setOnSeekBarChangeListener(MainActivity.this);
              break;
          case 2:
               view= LayoutInflater.from(MainActivity.this).inflate(R.layout.seek_bar,null);
              layoutParams= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
              actionLinearLayout.addView(view,layoutParams);
              mRecyclerView.setVisibility(View.GONE);
              actionLinearLayout.setVisibility(View.VISIBLE);
              SeekBar bar3=view.findViewById(R.id.seekFour);
              bar3.setOnSeekBarChangeListener(MainActivity.this);
              break;
      }

    }

    public void backgroundAlpha(float bgAlpha)

    {
        WindowManager.LayoutParams lp =this.getWindow().getAttributes();

        lp.alpha = bgAlpha; //0.0-1.0
        this.getWindow().setAttributes(lp);

    }


}

