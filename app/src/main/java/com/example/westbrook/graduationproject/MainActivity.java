package com.example.westbrook.graduationproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

public class MainActivity extends AppCompatActivity  implements SeekBar.OnSeekBarChangeListener{
    private Button leftButton;
    private Button  rightButton;
    private RecyclerView mRecyclerView;
    private static final String TAG = "MainActivity";
    private ImageView imageView;
    private LinearLayout linearLayout;
    private LinearLayout actionLinearLayout;
    private TextView chooseFile;
    //选择拍照
    private TextView takePhoto;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView=findViewById(R.id.recycler_view);
        imageView=findViewById(R.id.pic);
        chooseFile=findViewById(R.id.choose_file);
        takePhoto=findViewById(R.id.take_photo);
        linearLayout=findViewById(R.id.choose);
        actionLinearLayout=findViewById(R.id.action);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        String[] a={"旋转","色相","灰度效果","怀旧","反转","二值化","浮雕","底片","怀旧","重叠","添加马赛克"};//"色调","饱和度","亮度",
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
                     currentBitmap=ImageProcessing.rotate(currentBitmap);
                     imageView.setImageBitmap(currentBitmap);
                     break;
                 case 1://改变颜色系数
                     View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.screen_action,null);
                        LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                     actionLinearLayout.addView(view,layoutParams);
                     mRecyclerView.setVisibility(View.GONE);
                     actionLinearLayout.setVisibility(View.VISIBLE);
                     SeekBar bar=view.findViewById(R.id.seekOne);
                     bar.setOnSeekBarChangeListener(MainActivity.this);
                     SeekBar bar1=view.findViewById(R.id.seekTwo);
                     bar1.setOnSeekBarChangeListener(MainActivity.this);
                     SeekBar bar2=view.findViewById(R.id.seekThree);
                     bar2.setOnSeekBarChangeListener(MainActivity.this);
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

             }

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=1;
                linearLayout.setVisibility(View.VISIBLE);
            }
        });
        chooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
                }else {
                 choosePicture(2);
                }
            }
        });
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture(1);
            }
        });

    }

      @RequiresApi(api = Build.VERSION_CODES.FROYO)
      void choosePicture(int type){
        linearLayout.setVisibility(View.GONE);
        flag=0;
        Intent intent=null;
        switch (type){
            case 1:
                File file=new File(getExternalCacheDir(),"bitmap.jpg");
                        try {
                        if(file.exists()){
                            file.delete();
                        }
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                if(Build.VERSION.SDK_INT>=24){
                    imageUri= FileProvider.getUriForFile(MainActivity.this,"com.westbrook.project.fileProvider",file);
                }else {
                    imageUri= Uri.fromFile(file);
                }
                //打开相机
                intent=new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent,1);
                break;
            case 2:
                //打开相册
                intent=new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                startActivityForResult(intent,2);
                break;
                default:
                    break;
        }

      }
    @Override
    public void onBackPressed() {
        switch (flag){
            case 0:
                super.onBackPressed();
                break;
            case 1:
                if(linearLayout!=null){
                    linearLayout.setVisibility(View.GONE);
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
                if(resultCode==RESULT_OK){
                    try {
                       initiallyBitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                       currentBitmap=initiallyBitmap;
                        imageView.setImageBitmap(initiallyBitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 2:
                //相册
                if(resultCode==RESULT_OK){
                        if(Build.VERSION.SDK_INT>=19)
                            handleOnKitkat(data);else
                                handleBeforeKitkat(data);
                }
                break;
                default:
                    break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleOnKitkat(Intent data) {
            String imagePath=null;
            Uri uri=data.getData();
            if(DocumentsContract.isDocumentUri(this,uri)){
                  String docId=DocumentsContract.getDocumentId(uri);
                  if("com.android.providers.media.documents".equals(uri.getAuthority())){
                      String id=docId.split(":")[1];
                      String selection=MediaStore.Images.Media._ID+"="+id;
                      imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);

                  }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                      Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://dowmloads/public_downloads"),Long.valueOf(docId));
                      imagePath=getImagePath(contentUri,null);
                  }
            }else if("content".equalsIgnoreCase(uri.getScheme())){
                imagePath=getImagePath(uri,null);
            }else if("file".equalsIgnoreCase(uri.getScheme())){
                imagePath=uri.getPath();
            }
          initiallyBitmap=BitmapFactory.decodeFile(imagePath);
            imageView.setImageBitmap(initiallyBitmap);
        currentBitmap=initiallyBitmap;
    }


    private void handleBeforeKitkat(Intent data) {
                Uri uri=data.getData();
                String imagePath=getImagePath(uri,null);
                initiallyBitmap=BitmapFactory.decodeFile(imagePath);
                imageView.setImageBitmap(initiallyBitmap);
        currentBitmap=initiallyBitmap;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private String getImagePath(Uri uri, String selection){
    String path=null;
        @SuppressLint("Recycle") Cursor cursor=getContentResolver().query(uri,null,selection,null,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst())
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        }
    return  path;
    }
    //权限的申请
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
//            case 1:
//                //权限通过
//                if(grantResults.length>0 && PackageManager.PERMISSION_GRANTED==grantResults[0])
//                    choosePicture(1);
//                else
//                    Toast.makeText(this, "您未授予权限, 软件无法使用", Toast.LENGTH_SHORT).show();
//                break;
            case 2:
                //权限通过
                if(grantResults.length>0 && PackageManager.PERMISSION_GRANTED==grantResults[0])
                choosePicture(2);
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
                break;
            case R.id.seekTwo:
                mS=progress*1.0F/50;
                break;
            case R.id.seekThree:
                mL=progress*1.0F/50;
                break;

        }
      currentBitmap= ImageProcessing.screenAction(holdBitmap,mHue,mS,mL);
      imageView.setImageBitmap(currentBitmap);

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}

