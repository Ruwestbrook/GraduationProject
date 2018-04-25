package com.example.westbrook.graduationproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
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
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.westbrook.graduationproject.Tool.ImageProcessing;
import com.example.westbrook.graduationproject.Tool.Tool;
import com.example.westbrook.graduationproject.Tool.itemAdapter;
import com.example.westbrook.graduationproject.custom.CustomView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity  implements SeekBar.OnSeekBarChangeListener{
    private Button leftButton;
    private Button  rightButton;
    private RecyclerView mRecyclerView;
    private static final String TAG = "MainActivity";
    private CustomView imageView;
    private RelativeLayout backgroundLinearLayout;
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
    private boolean[] isDeal=new boolean[14];
    private Tool mTool;
    private Intent data;
    private int type;

    private int tmp=128;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTool=new Tool(this);
        mRecyclerView=findViewById(R.id.recycler_view);
        imageView=findViewById(R.id.pic);
        rightButton=findViewById(R.id.button_two);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(initiallyBitmap!=null){
                    Log.d(TAG, "onClick: 重置图片");
                    Log.d(TAG, "onClick: "+type);
                    Bitmap bitmap=null;
                    if(type==1){
                         bitmap=mTool.getFile(MainActivity.this,imageUri);
                    }else {
                        int width=backgroundLinearLayout.getWidth();
                        int height=backgroundLinearLayout.getHeight();
                        bitmap=mTool.getFile(MainActivity.this,data);
                        if(bitmap.getHeight()>height){
                            bitmap=mTool.zoomBitmap(bitmap,width,height);
                        }else if(bitmap.getWidth()>width){
                            bitmap=mTool.zoomBitmap(bitmap,width,height);
                        }
                        imageView.setOnCLick(false,1);
                        imageView.setImageBitmap(initiallyBitmap);
                        imageView.setOnCLick(true,1);
                    }

                    initiallyBitmap=bitmap;

                    currentBitmap=initiallyBitmap;
                }

                else
                    Toast.makeText(MainActivity.this, "请先选择图片", Toast.LENGTH_SHORT).show();
            }
        });
        leftButton=findViewById(R.id.button_one);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentBitmap!=null){
                    if (Environment.getExternalStorageState().equals( Environment.MEDIA_MOUNTED)) // 判断是否可以对SDcard进行操作  
                    {    // 获取SDCard指定目录下  
                        String  sdCardDir = Environment.getExternalStorageDirectory()+ "/CoolImage/";
                        File dirFile  = new File(sdCardDir);  //目录转化成文件夹  
                        if (!dirFile .exists()) {              //如果不存在，那就建立这个文件夹  
                            dirFile .mkdirs();
                        }                          //文件夹有啦，就可以保存图片啦              
                        File file = new File(sdCardDir, System.currentTimeMillis()+".jpg");// 在SDcard的目录下创建图片文,以当前时间为其命名  

                        FileOutputStream out = null;
                        try {
                            out = new FileOutputStream(file);
                            currentBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                            Log.d(TAG, "_________保存到____sd______指定目录文件夹下____________________");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            Log.d(TAG, "onClick: "+e.toString());
                        }
                        try {
                            out.flush();
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(MainActivity.this,"保存已经至"+Environment.getExternalStorageDirectory()+"/CoolImage/"+"目录文件夹下", Toast.LENGTH_SHORT).show(); 
                }else {
                    Toast.makeText(MainActivity.this, "请先选择图片", Toast.LENGTH_SHORT).show();
                }
                }
            }

        });
        backgroundLinearLayout=findViewById(R.id.background);
        actionLinearLayout=findViewById(R.id.action);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        String[] a={"添加马赛克","涂鸦","剪裁","旋转","色相","灰度效果","怀旧","反转","去色","高饱和度","底片","浮雕","老照片","二值化"};//"色调","饱和度","亮度",
        for (int i=0;i<isDeal.length;i++){
            isDeal[i]=false;
        }
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
                 case 0:

                     break;
                 case 1:
                     backgroundLinearLayout.setOnClickListener(null);
                     imageView.setOnClickListener(null);
                     imageView.setOnCLick(true,1);
                 case 2:
                     break;
                 case 3:
                     currentBitmap= ImageProcessing.rotate(currentBitmap);
                     imageView.setImageBitmap(currentBitmap);

                     break;
                 case 4:
                     resetImageView();
                     showSeekBar(1);
                     flag=2;
                     holdBitmap=currentBitmap;
                     break;
                 case 5:
                     if(!isDeal[5]){
                         holdBitmap= ImageProcessing.grayProcessing(currentBitmap);
                         imageView.setImageBitmap(holdBitmap);

                     }else {
                         imageView.setImageBitmap(currentBitmap);
                     }
                     isDeal[5]=!isDeal[5];

                     break;
                 case 6:
                     resetImageView();
                     if(!isDeal[6]){
                         holdBitmap= ImageProcessing.nostalgiaProcessing(currentBitmap);
                         imageView.setImageBitmap(holdBitmap);
                     }else {
                         imageView.setImageBitmap(currentBitmap);
                     }
                     isDeal[6]=!isDeal[6];

                     break;
                 case 7:
                     resetImageView();
                     if(!isDeal[7]){
                         holdBitmap= ImageProcessing.reverseProcessing(currentBitmap);
                         imageView.setImageBitmap(holdBitmap);
                     }else {
                         imageView.setImageBitmap(currentBitmap);
                     }
                     isDeal[7]=!isDeal[7];
                     break;
                 case 8:
                     resetImageView();
                     if(!isDeal[8]){
                         holdBitmap= ImageProcessing.toColorEffect(currentBitmap);
                         imageView.setImageBitmap(holdBitmap);
                     }else {
                         imageView.setImageBitmap(currentBitmap);
                     }

                     isDeal[8]=!isDeal[8];

                     break;
                 case 9:
                     resetImageView();
                     if(!isDeal[9]){
                     holdBitmap= ImageProcessing.highSaturation(currentBitmap);
                     imageView.setImageBitmap(holdBitmap);
                 }else {
                     imageView.setImageBitmap(currentBitmap);
                 }

                     isDeal[9]=!isDeal[9];

                     break;
                 case 10:
                     resetImageView();
                     if(!isDeal[10]){
                         holdBitmap= ImageProcessing.handleImageNative(currentBitmap);
                         imageView.setImageBitmap(holdBitmap);
                     }else {
                         imageView.setImageBitmap(currentBitmap);
                     }

                     isDeal[10]=!isDeal[10];
                     break;
                 case 11:
                     resetImageView();
                     if(!isDeal[11]){
                         holdBitmap= ImageProcessing.Carving(currentBitmap);
                         imageView.setImageBitmap(holdBitmap);
                     }else {
                         imageView.setImageBitmap(currentBitmap);
                     }
                     isDeal[11]=!isDeal[11];

                     break;
                 case 12:
                     resetImageView();
                     if(!isDeal[12]){
                     holdBitmap= ImageProcessing.oldPicture(currentBitmap);
                     imageView.setImageBitmap(holdBitmap);
                 }else {
                     imageView.setImageBitmap(currentBitmap);
                 }
                     isDeal[12]=!isDeal[12];
                     break;
                 case 13:
                     resetImageView();
                     showSeekBar(2);
                     flag=2;
                     holdBitmap= ImageProcessing.convertToBMW(currentBitmap,tmp);
                     imageView.setImageBitmap(holdBitmap);
                     break;

             }

            }
        });



        backgroundLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=1;
                showChooseFile();
            }
        });




    }


    void resetImageView(){
        backgroundLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=1;
                showChooseFile();
            }
        });
        imageView.setOnCLick(false,0);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    type=1;
                    initiallyBitmap= mTool.getFile(this, imageUri);
                }
                break;
            case 2:
                //相册
                if(resultCode==RESULT_OK){
                    type=2;
                    this.data=data;
                    initiallyBitmap= mTool.getFile(this,data);
                }

                break;
                default:
                    break;
        }
       if(popupWindow!=null){
           popupWindow.dismiss();
           popupWindow=null;
       }

        int width=backgroundLinearLayout.getWidth();
        int height=backgroundLinearLayout.getHeight();
        if(initiallyBitmap.getHeight()>height){
            initiallyBitmap=mTool.zoomBitmap(initiallyBitmap,width,height);
        }else if(initiallyBitmap.getWidth()>width){
            initiallyBitmap=mTool.zoomBitmap(initiallyBitmap,width,height);
        }
        currentBitmap=initiallyBitmap;
        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(initiallyBitmap.getWidth(),initiallyBitmap.getHeight());
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        imageView.setLayoutParams(params);
        imageView.setImageBitmap(initiallyBitmap);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
        if(popupWindow==null){
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

    }




    public void showSeekBar(int type) {
        View view;
        LinearLayout.LayoutParams layoutParams;
      switch (type){
          case 1:
               view= LayoutInflater.from(MainActivity.this).inflate(R.layout.screen_action,null);
              layoutParams= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
              actionLinearLayout.removeAllViews();
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
              actionLinearLayout.removeAllViews();
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

