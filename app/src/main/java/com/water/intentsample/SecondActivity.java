package com.water.intentsample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {
    private TextView tvContent;
    private Button btnBack;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        tvContent= (TextView) findViewById(R.id.tvContent);

        Intent intent=getIntent();
        //先获取用户的喜好个数
        int count=intent.getIntExtra("Count",0);
        String str="";

        //遍历喜好的内容
        for (int i=0;i<count;i++){
            str+=intent.getStringExtra(""+i)+" ";
        }

        //显示喜好
        tvContent.setText(	str);

        btnBack=(Button)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}