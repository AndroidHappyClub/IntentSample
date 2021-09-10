package com.water.intentsample;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.BuildConfig;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.water.utilities.PermissionHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IntentActivity extends AppCompatActivity
        implements View.OnClickListener {
    String[] _permissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private GridLayout glContainer;
    private Button btnHome;
    private Button btnTel;
    private Button btnSms;
    private Button btnUrl;
    private Button btnWireless;
    private Button btnContent;
    private Button btnExplicitStart;
    private Button btnReturnData;
    private Button btnComplexData;
    private Button btnImageSms;
    private Button btnPlay;
    private Button btnInstallApk;
    private Button btnOpenApp;
    private Button btnOpenRecord;
    private Button btnPlayMediaStore;
    private Button btnTakePhoto;

    private ImageView ivPhoto;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent);

        glContainer = (GridLayout) findViewById(R.id.glContainer);
        int columnCount = glContainer.getColumnCount();

        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;

        for (int i = 0; i < glContainer.getChildCount(); i++) {
            Button btn = (Button) glContainer.getChildAt(i);
            btn.setWidth(screenWidth / columnCount);
        }

        btnHome= (Button) findViewById(R.id.btnHome);
        btnTel=(Button) findViewById(R.id.btnTel);
        btnSms=(Button)findViewById(R.id.btnSms);
        btnUrl=(Button)findViewById(R.id.btnUrl);
        btnWireless=(Button)findViewById(R.id.btnWireless);
        btnContent=(Button)findViewById(R.id.btnContent);
        btnExplicitStart=(Button)findViewById(R.id.btnExplicitStart);
        btnReturnData=(Button)findViewById(R.id.btnReturnData);
        btnComplexData=(Button)findViewById(R.id.btnComplexData);
        btnImageSms=(Button)findViewById(R.id.btnImageSms);
        btnPlay=(Button)findViewById(R.id.btnPlay);
        btnPlayMediaStore=(Button)findViewById(R.id.btnPlayMediaStore);
        btnInstallApk=(Button)findViewById(R.id.btnInstallApk);
        btnOpenApp=(Button)findViewById(R.id.btnOpenApp);
        btnOpenRecord=(Button)findViewById(R.id.btnOpenRecord);
        btnTakePhoto=(Button)findViewById(R.id.btnTakePhoto);
        ivPhoto=findViewById(R.id.ivPhoto);

        btnHome.setOnClickListener(this);
        btnTel.setOnClickListener(this);
        btnSms.setOnClickListener(this);
        btnUrl.setOnClickListener(this);
        btnWireless.setOnClickListener(this);
        btnContent.setOnClickListener(this);
        btnExplicitStart.setOnClickListener(this);
        btnReturnData.setOnClickListener(this);
        btnComplexData.setOnClickListener(this);
        btnImageSms.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnPlayMediaStore.setOnClickListener(this);
        btnInstallApk.setOnClickListener(this);
        btnOpenApp.setOnClickListener(this);
        btnOpenRecord.setOnClickListener(this);
        btnTakePhoto.setOnClickListener(this);

        // 共享数据
        MyApplication ma = MyApplication.getInstance();
        ma.setName("APPLICATION 数据传递");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnHome:
                //创建Intent 对象
                Intent ihome = new Intent();
                //为Intent设置Action和Category属性
                ihome.setAction(Intent.ACTION_MAIN);
                ihome.addCategory(Intent.CATEGORY_HOME);
                //启动Activity
                startActivity(ihome);
                break;
            case R.id.btnTel:// 给移动客服10086拨打电话
                Uri utel = Uri.parse("tel:10086");
                Intent itel = new Intent(Intent.ACTION_DIAL, utel);
                startActivity(itel);
                break;
            case R.id.btnSms:// 给10086发送内容为“Hello”的短信
                Uri us = Uri.parse("smsto:10086");
                Intent intent = new Intent(Intent.ACTION_SENDTO, us);
                intent.putExtra("sms_body", "Hello");
                startActivity(intent);
                break;
            case R.id.btnUrl:// 打开浏览器
                Uri uu = Uri.parse("http://www.baidu.com");
                Intent iu  = new Intent(Intent.ACTION_VIEW, uu);
                startActivity(iu);
                break;
            case R.id.btnWireless:
                Intent iw = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                startActivityForResult(iw, 0);
                break;
            case R.id.btnContent:
                Intent ic = new Intent();
                ic.setAction(Intent.ACTION_VIEW);
                ic.setData(ContactsContract.Contacts.CONTENT_URI);
                startActivity(ic);
                break;
            case R.id.btnExplicitStart:
                startActivity(new Intent(IntentActivity.this,
                        FirstActivity.class));
                break;
            case R.id.btnReturnData:
                Intent imars = new Intent(IntentActivity.this,
                        MarsActivity.class);
                String content = "地球来的消息:我是来自地球上的Tom，火星的朋友你好！";
                imars.putExtra("FromEarth", content);
                startActivityForResult(imars, RequestCode.Mars);
                break;
            case R.id.btnComplexData:
                Intent icd = new Intent(IntentActivity.this,
                        ComplexDataActivity.class);

                List<RequestCode> os = new ArrayList<RequestCode>(){{
                    add(new RequestCode());
                    add(new RequestCode());
                }};

                // 传递基本数据类型或String类型数组
                Bundle bd = new Bundle();
                bd.putStringArray("StringArray", new String[]{"请求","全球"});

                // 传递基本数据类型或String类型集合
                icd.putStringArrayListExtra("StringList", new ArrayList<String>(){{
                    add("请求");
                    add("全球");
                }});

                // 传递List<Object>集合，Object必须继承Serializable
                bd.putSerializable("ObjectList", (Serializable)os);
//
//                // 传递对象
//                // icd.putExtra("RequestCode",new Gson().toJson(new RequestCode()));

                // 传递Bitmap
                try {
                    InputStream in = getResources().getAssets().open("car.png");
                    Bitmap bmp = BitmapFactory.decodeStream(in);
                    bd.putParcelable("bitmap", bmp);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                icd.putExtras(bd);
                startActivity(icd);
                break;
            case R.id.btnImageSms:// 相当于发送带附件的短信
                Intent iis=new Intent();
                iis.setAction(Intent.ACTION_SEND);
                iis.putExtra("address","10086"); // 邮件地址
                iis.putExtra("sms_body","Hello"); // 邮件内容
                File fsms = new File(Environment.getExternalStorageDirectory(),
                        "/Pictures/kb.png");
                Uri uri = null;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    uri= FileProvider.getUriForFile(this,
                            BuildConfig.APPLICATION_ID +
                                    ".fileProvider", fsms);
                    iis.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                else
                    uri = Uri.fromFile(fsms);

                iis.putExtra(Intent.EXTRA_STREAM, uri);
                iis.setType("image/png"); // 设置类型
                startActivity(iis);
                break;
            case R.id.btnPlay:
                try {
                    PermissionHelper.checkPermission(this, _permissions);
                    openSDcardFile("/Intent/ring.wav");
                }catch (Exception ex){
                    Log.i("",ex.getMessage());
                }
                break;
            case R.id.btnPlayMediaStore:// 获取SD卡下所有音频文件,然后播放第一首
                Uri upms = Uri.withAppendedPath(MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
                        "1");
                Intent ipms = new Intent(Intent.ACTION_VIEW, upms);
                startActivity(ipms);
                break;
            case R.id.btnInstallApk:
                try {
                    PermissionHelper.checkPermission(this, _permissions);
                    openSDcardFile("/Download/ui.apk");
                }catch (Exception ex){
                    Log.i("",ex.getMessage());
                }
                break;
            case R.id.btnOpenApp:// 打开另一程序
                Intent ioa = new Intent();
                ComponentName cn = new ComponentName("com.water.uisample",
                        "com.water.uisample.ExpandableListViewActivity");
                ioa.setComponent(cn);
                ioa.setAction(Intent.ACTION_PACKAGE_FIRST_LAUNCH);
                ioa.setAction(Intent.ACTION_MAIN);
                startActivityForResult(ioa, RESULT_OK);
                break;
            case R.id.btnOpenRecord:
                Intent ior = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                startActivity(ior);
                break;
            case R.id.btnTakePhoto:
                Intent itp = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(itp, RequestCode.TakePhoto);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data){
        switch (requestCode) {
            case RequestCode.Mars:
                Bundle MarsBuddle = data.getExtras();
                String msg = MarsBuddle.getString("FromMars");
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                break;
            case RequestCode.TakePhoto:
                // 取出照片数据
                Bundle extras = data.getExtras();
                Bitmap bmp = (Bitmap) extras.get("data");
                // 根据旋转角度，生成旋转矩阵
                Matrix matrix = new Matrix();
                matrix.postRotate(90);

                try {
                    // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
                    Bitmap rbmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
                            bmp.getHeight(), matrix, true);
                    ivPhoto.setImageBitmap(rbmp);
                    bmp.recycle();
                } catch (OutOfMemoryError e) {
                }

                break;
        }
    }

    private void openSDcardFile(String filename) throws IOException {
        File file = new File(Environment.getExternalStorageDirectory(),
                filename);

        if(!file.exists())
            return;

        Intent in = new Intent(Intent.ACTION_DEFAULT);
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri=null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri= FileProvider.getUriForFile(this,
                    BuildConfig.APPLICATION_ID +
                            ".fileProvider", file);
            in.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        else
            uri = Uri.fromFile(file);

        if (filename.contains(".doc") || filename.contains(".docx")) {
            in.setDataAndType(uri, "application/msword");
        } else if(filename.contains(".pdf")) {
            in.setDataAndType(uri, "application/pdf");
        } else if(filename.contains(".ppt") || filename.contains(".pptx")) {
            in.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if(filename.contains(".xls") || filename.contains(".xlsx")) {
            in.setDataAndType(uri, "application/vnd.ms-excel");
        } else if(filename.contains(".zip") || filename.contains(".rar")) {
            in.setDataAndType(uri, "application/x-wav");
        } else if(filename.contains(".rtf")) {
            in.setDataAndType(uri, "application/rtf");
        } else if(filename.contains(".wav") || filename.contains(".mp3")) {
            in.setDataAndType(uri, "audio/x-wav");
        } else if(filename.contains(".gif")) {
            in.setDataAndType(uri, "image/gif");
        } else if(filename.contains(".jpg") || filename.contains(".jpeg") ||
                filename.contains(".png")) {
            in.setDataAndType(uri, "image/jpeg");
        } else if(filename.contains(".txt")) {
            in.setDataAndType(uri, "text/plain");
        } else if(filename.contains(".3gp") || filename.contains(".mpg") ||
                filename.contains(".mpeg") || filename.contains(".mpe") ||
                filename.contains(".mp4") || filename.contains(".avi")) {
            in.setDataAndType(uri, "video/*");
        }else if (filename.contains(".apk")){
            in.setDataAndType(uri, "application/vnd.android.package-archive");
        } else {
            in.setDataAndType(uri, "*/*");
        }

        startActivity(in);
    }
}