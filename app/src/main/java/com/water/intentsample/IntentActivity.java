package com.water.intentsample;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import com.water.intentsample.databinding.ActivityIntentBinding;
import com.water.utilities.PermissionHelper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class IntentActivity extends AppCompatActivity {

    private Context _context;

    String[] _permissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private final String tag = this.getClass().getSimpleName();

    private ActivityIntentBinding binding;

    /**
     * 注册返回数据回调
     */
    private final ActivityResultLauncher<Intent> GetReturnString = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    assert result.getData() != null;
                    Bundle MarsBundle = result.getData().getExtras();
                    String msg = MarsBundle.getString(MarsActivity.MarsResultName);
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                }
            });

    /**
     * 注册TakePhoto回调
     */
    private final ActivityResultLauncher<Intent> TakePhoto = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // 取出照片数据
                    assert result.getData() != null;
                    Bundle extras = result.getData().getExtras();
                    Bitmap bmp = (Bitmap) extras.get("data");
                    // 根据旋转角度，生成旋转矩阵
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);

                    try {
                        // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
                        Bitmap bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
                                bmp.getHeight(), matrix, true);
                        binding.ivPhoto.setImageBitmap(bitmap);
                        bmp.recycle();
                    } catch (OutOfMemoryError e) {
                        Log.e(tag, "OutOfMemoryError in IntentActivity", e);
                    }
                }
            });

    @SuppressLint("QueryPermissionsNeeded")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_intent);

        _context = this.getBaseContext();

        int columnCount = binding.glContainer.getColumnCount();

        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;

        for (int i = 0; i < binding.glContainer.getChildCount(); i++) {
            Button btn = (Button) binding.glContainer.getChildAt(i);
            btn.setWidth(screenWidth / columnCount);
        }

        // 返回首页
        binding.btnHome.setOnClickListener(v -> {
            Intent home = new Intent();
            //为Intent设置Action和Category属性
            home.setAction(Intent.ACTION_MAIN);
            home.addCategory(Intent.CATEGORY_HOME);

            //使用resolveActivity进行检查，这是十分必要的
            if(home.resolveActivity(getPackageManager())!=null){
                startActivity(home);
            }else{
                Toast.makeText(_context,"未找到对应应用",Toast.LENGTH_SHORT).show();
            }
        });

        // 给移动客服10086拨打电话
        binding.btnTel.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:10086"));

            if(intent.resolveActivity(getPackageManager())!=null){
                startActivity(intent);
            }else{
                Toast.makeText(_context,"未找到对应应用",Toast.LENGTH_SHORT).show();
            }
        });

        // 给10086发送内容为“Hello”的短信
        binding.btnSms.setOnClickListener(v -> {
            // 关于smsto的内容，请参考下列链接
            // https://developer.android.com/guide/components/intents-common?hl=zh-cn
            Uri us = Uri.parse("smsto:10086");
            Intent intent = new Intent(Intent.ACTION_SENDTO, us);
            intent.putExtra("sms_body", "Hello");

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(_context, "未找到对应应用", Toast.LENGTH_SHORT).show();
            }
        });

        // 打开浏览器
        binding.btnUrl.setOnClickListener(v -> {
            Uri uu = Uri.parse("http://www.baidu.com");
            Intent intent  = new Intent(Intent.ACTION_VIEW, uu);

            if(intent.resolveActivity(getPackageManager())!=null){
                startActivity(intent);
            }else{
                Toast.makeText(_context,"未找到对应应用",Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnWireless.setOnClickListener(v -> {
            Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);

            if(intent.resolveActivity(getPackageManager())!=null){
                startActivity(intent);
            }else{
                Toast.makeText(_context,"未找到对应应用",Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnContent.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(ContactsContract.Contacts.CONTENT_URI);

            if(intent.resolveActivity(getPackageManager())!=null){
                startActivity(intent);
            }else{
                Toast.makeText(_context,"未找到对应应用",Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnExplicitStart.setOnClickListener(v -> {
            Intent intent = new Intent(IntentActivity.this, FirstActivity.class);

            if(intent.resolveActivity(getPackageManager())!=null){
                startActivity(intent);
            }else{
                Toast.makeText(_context,"未找到对应应用",Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnReturnData.setOnClickListener(v -> {
            Intent mars = new Intent(IntentActivity.this,
                    MarsActivity.class);
            String content = "地球来的消息:我是来自地球上的Tom，火星的朋友你好！";
            mars.putExtra("FromEarth", content);

            GetReturnString.launch(mars);
        });

        binding.btnComplexData.setOnClickListener(v -> {
            Intent intent = new Intent(IntentActivity.this, ComplexDataActivity.class);

            // 传递基本数据类型或String类型数组
            Bundle bd = new Bundle();
            bd.putStringArray("StringArray", new String[]{"请求","全球"});

            // 传递基本数据类型或String类型集合
            intent.putStringArrayListExtra("StringList", new ArrayList<String>(){{
                add("请求");
                add("全球");
            }});

            // 传递Bitmap
            try {
                InputStream in = getResources().getAssets().open("car.png");
                Bitmap bmp = BitmapFactory.decodeStream(in);
                bd.putParcelable("bitmap", bmp);
            } catch (IOException e) {
                e.printStackTrace();
            }

            intent.putExtras(bd);

            if(intent.resolveActivity(getPackageManager()) != null){
                startActivity(intent);
            }else{
                Toast.makeText(_context,"未找到对应应用",Toast.LENGTH_SHORT).show();
            }

        });

        // 相当于发送带附件的短信
        binding.btnImageSms.setOnClickListener(v -> {
            Intent iis=new Intent();
            iis.setAction(Intent.ACTION_SEND);
            iis.putExtra("address","10086"); // 邮件地址
            iis.putExtra("sms_body","Hello"); // 邮件内容
            File fsms = new File(Environment.getExternalStorageDirectory(),
                    "/Pictures/kb.png");
            Uri uri;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri= FileProvider.getUriForFile(_context,
                        BuildConfig.APPLICATION_ID +
                                ".fileProvider", fsms);
                iis.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            else
                uri = Uri.fromFile(fsms);

            iis.putExtra(Intent.EXTRA_STREAM, uri);
            iis.setType("image/png"); // 设置类型
            startActivity(iis);
        });

        binding.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PermissionHelper.checkPermission(this, _permissions);
                    openSDCardFile("/Intent/ring.wav");
                }catch (Exception ex){
                    Log.i(tag,ex.getMessage());
                }
            }
        });

        // 获取SD卡下所有音频文件,然后播放第一首
        binding.btnPlayMediaStore.setOnClickListener(v -> {
            Uri upms = Uri.withAppendedPath(MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
                    "1");
            Intent ipms = new Intent(Intent.ACTION_VIEW, upms);
            startActivity(ipms);
        });

        binding.btnInstallApk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PermissionHelper.checkPermission(this, _permissions);
                    openSDCardFile("/Download/ui.apk");
                }catch (Exception ex){
                    Log.i(tag,ex.getMessage());
                }
            }
        });

        // 打开另一程序
        binding.btnOpenApp.setOnClickListener(v -> {
            Intent intent = new Intent();
            ComponentName cn = new ComponentName("com.water.uisample",
                    "com.water.uisample.ExpandableListViewActivity");
            intent.setComponent(cn);
            intent.setAction(Intent.ACTION_PACKAGE_FIRST_LAUNCH);
            intent.setAction(Intent.ACTION_MAIN);
            startActivity(intent);
        });

        binding.btnOpenRecord.setOnClickListener(v -> {
            Intent ior = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);

            if(ior.resolveActivity(getPackageManager())!=null){
                startActivity(ior);
            }else{
                Toast.makeText(_context,"未找到对应应用",Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnTakePhoto.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            TakePhoto.launch(intent);
        });

        // 共享数据
        MyApplication ma = MyApplication.getInstance();
        ma.setName("APPLICATION 数据传递");
    }

    private void openSDCardFile(String filename) {
        File file = new File(Environment.getExternalStorageDirectory(),
                filename);

        if(!file.exists())
            return;

        Intent in = new Intent(Intent.ACTION_DEFAULT);
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri;

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