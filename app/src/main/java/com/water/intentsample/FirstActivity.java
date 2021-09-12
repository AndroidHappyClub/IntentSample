package com.water.intentsample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class FirstActivity extends AppCompatActivity {
    private CheckBox cbxSing, cbxDance, cbxSport, cbxReadBook;
    private List<CheckBox> checkBoxs = new ArrayList<CheckBox>();
    private Button btnSubmit;
    private String content = "";
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        cbxSing = (CheckBox) findViewById(R.id.cbxSing);
        cbxDance = (CheckBox) findViewById(R.id.cbxDance);
        cbxSport = (CheckBox) findViewById(R.id.cbxSport);
        cbxReadBook = (CheckBox) findViewById(R.id.cbxReadBook);
        btnSubmit= (Button) findViewById(R.id.btnSubmit);

        // 添加到集合中
        checkBoxs.add(cbxSing);
        checkBoxs.add(cbxDance);
        checkBoxs.add(cbxSport);
        checkBoxs.add(cbxReadBook);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                int i=0;

                //将选中的喜好放到bundle中
                for (CheckBox cbx : checkBoxs) {
                    if (cbx.isChecked()) {
                        bundle.putString("" + i, cbx.getText().toString());
                        i++;
                    }
                }

                //喜好的个数也放到bundle中
                bundle.putInt("Count",i);
                Intent intent=new Intent(FirstActivity.this,SecondActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }
    public void getValues(View v) {
        if ("".equals(content)) {
            content = "请您选择您的爱好";
        }
    }
}
