package com.water.intentsample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.water.intentsample.databinding.ActivityComplexDataBinding;

import java.util.ArrayList;

public class ComplexDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.water.intentsample.databinding.ActivityComplexDataBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_complex_data);

        MyApplication ma = MyApplication.getInstance();
        binding.tvContent.setText(ma.getName());

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        ArrayList<String> alStr = intent.getStringArrayListExtra("StringList");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alStr);

        binding.list.setAdapter(arrayAdapter);

        Bitmap bmp = bd.getParcelable("bitmap");
        binding.ivBmp.setImageBitmap(bmp);
    }
}