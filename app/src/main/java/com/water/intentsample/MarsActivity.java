package com.water.intentsample;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MarsActivity extends AppCompatActivity {

	public final static String MarsResultName = "FromMars";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mars);
		Intent EarthIntent = getIntent();
		String EarthMessage = EarthIntent.getStringExtra("FromEarth");
		Button btnReturn = findViewById(R.id.btnReturn);

		btnReturn.setOnClickListener(v -> {
			Intent intent = new Intent(MarsActivity.this, IntentActivity.class);
			String msg = "火星来的消息:我是火星Jack，非常高兴你能来火星";
			intent.putExtra(MarsResultName, msg);
			setResult(RESULT_OK, intent);
			finish();
		});

		TextView tv = findViewById(R.id.tvContent);
		tv.setText(EarthMessage);
	}
}
