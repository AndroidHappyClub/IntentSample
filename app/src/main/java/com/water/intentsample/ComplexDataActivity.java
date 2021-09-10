package com.water.intentsample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ComplexDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complex_data);

        TextView tv = (TextView)findViewById(R.id.tvContent);
        ImageView iv = (ImageView)findViewById(R.id.ivBmp);

        MyApplication ma = MyApplication.getInstance();
        tv.setText(ma.getName());

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        String[] str = bd.getStringArray("StringArray");
        ArrayList<String> alStr = intent.getStringArrayListExtra("StringList");

        List<RequestCode> rcs = (List<RequestCode>) bd.getSerializable("ObjectList");

        Bitmap bmp = bd.getParcelable("bitmap");
        iv.setImageBitmap(bmp);
    }
}