package com.water.intentsample;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MarsActivity extends AppCompatActivity{
	private Button btnReturn  = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mars);
		Intent EarthIntent = getIntent();
		String EarthMessage = EarthIntent.getStringExtra("FromEarth");
		btnReturn = (Button) findViewById(R.id.btnReturn);

		btnReturn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MarsActivity.this,
						IntentActivity.class);
				String msg = "火星来的消息:我是火星Jack，非常高兴你能来火星";
				intent.putExtra("FromMars", msg);
				setResult(RESULT_OK, intent);
				finish();
			}
		});

		TextView tv = (TextView) findViewById(R.id.tvContent);
		tv.setText(EarthMessage);
	}
}
