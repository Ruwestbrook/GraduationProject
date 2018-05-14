package com.example.westbrook.graduationproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.westbrook.graduationproject.custom.ClipImageLayout;



/**
 * Created by westbrook on 2018/5/14.
 */

public class TailoringActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tailoring);

        ClipImageLayout mClipImageLayout = (ClipImageLayout) findViewById(R.id.id_clipImageLayout);

    }

}
