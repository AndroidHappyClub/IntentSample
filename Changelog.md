# 2021/9/12 黄师塔前江水东，春光懒困倚微风

## 🔧 修改内容

- IntentActivity.java
  - 增加startActivity方法检查
    ```java
    //使用resolveActivity进行检查，这是十分必要的
    if(home.resolveActivity(getPackageManager())!=null){
        startActivity(home);
    }else{
        Toast.makeText(_context,"未找到对应应用",Toast.LENGTH_SHORT).show();
    }
    ```

  - 放弃startActivityForResult，采用registerForActivityResult，对于更多内容，查看[获取 activity 的结果](https://developer.android.com/training/basics/intents/result?hl=zh-cn#java)
    ```java
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
    ```

  - **Android 11 解决 Intent.resolveActivity(context.getPackageManager()) == null的问题**
  在使用resolveActivity检查时，如果不在清单文件中声明queries会导致返回结果为null，以拨打电话为例：
    ```xml
    <queries>
        <intent>
            <action android:name="android.intent.action.DIAL"/>
        </intent>
    </queries>
    ```
- RequestCode.java
  删除该java文件,因为不再使用**startActivityForResult**

- BroadCastReceiverActivity.java
  删除处理

## ⚠ 提示

- IntentActivity.java
  如果你无法唤起短信应用，可能是手机本身不允许，例如MI 8可以正常唤起但是K40就不可以
  
