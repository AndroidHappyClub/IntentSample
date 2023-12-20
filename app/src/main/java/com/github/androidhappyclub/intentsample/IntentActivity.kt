/*
 * MIT License
 *
 * Copyright (c) 2023 AndroidHappyClub
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.androidhappyclub.intentsample

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.ave.vastgui.tools.activity.result.contract.TakePhotoContract
import com.ave.vastgui.tools.manager.filemgr.FileMgr
import com.ave.vastgui.tools.utils.AppUtils
import com.ave.vastgui.tools.utils.IntentUtils
import com.ave.vastgui.tools.utils.download.DLManager
import com.ave.vastgui.tools.utils.permission.Permission
import com.ave.vastgui.tools.utils.permission.requestPermission
import com.ave.vastgui.tools.view.toast.SimpleToast
import com.ave.vastgui.tools.viewbinding.viewBinding
import com.github.androidhappyclub.intentsample.adapter.IntentItemAdapter
import com.github.androidhappyclub.intentsample.databinding.ActivityIntentBinding
import com.github.androidhappyclub.intentsample.logger.mLogFactory
import java.io.File

class IntentActivity : AppCompatActivity(R.layout.activity_intent) {

    companion object {
        const val FROM_EARTH_KEY = "FROM_EARTH_KEY"
    }

    private val binding: ActivityIntentBinding by viewBinding(ActivityIntentBinding::bind)
    private val logger = mLogFactory.getLog(IntentActivity::class.java)

    /**
     * 注册返回数据回调。
     */
    private val getReturnString =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val msg = result.data?.extras?.getString(MarsActivity.MarsResultName)
                SimpleToast.showShortMsg(msg.toString())
            }
        }

    /**
     * 注册 [TakePhotoContract] 回调。
     */
    private val takePhoto =
        registerForActivityResult(TakePhotoContract()) { uri ->
            if (null == uri) return@registerForActivityResult
            contentResolver.openInputStream(uri).use {
                val bmp = BitmapFactory.decodeStream(it)
                // 根据旋转角度，生成旋转矩阵
                val matrix = Matrix()
                matrix.postRotate(90f)
                // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
                val bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
                binding.ivPhoto.setImageBitmap(bitmap)
                bmp.recycle()
            }
        }

    private val recorder =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            logger.d("从录音机中返回")
        }

    private val installRequest =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                installApk()
            }
        }

    private val iiAdapter = IntentItemAdapter(this)

    /**
     * 安装 apk 的根路径。
     */
    private val apkRoot: File by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            File(filesDir, "Download")
        } else {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        }
    }

    /**
     * 测试的 wav 文件存放路径。
     */
    private val wavRoot: File by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            File(filesDir, "Music")
        } else {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        downloadApk()
        readWav()

        iiAdapter.addItem("返回首页") {
            val home = Intent().apply {
                //为Intent设置Action和Category属性
                action = Intent.ACTION_MAIN
                addCategory(Intent.CATEGORY_HOME)
            }

            // 使用resolveActivity进行检查，这是十分必要的
            if (home.resolveActivity(packageManager) != null) {
                startActivity(home)
            } else {
                SimpleToast.showShortMsg("未找到对应应用")
            }
        }

        // 给移动客服10086拨打电话
        // 会申请 Manifest.permission.CALL_PHONE 权限
        iiAdapter.addItem("拨打电话") {
            requestPermission(Manifest.permission.CALL_PHONE) {
                granted = {
                    SimpleToast.showShortMsg("拨打电话")
                    IntentUtils.dialPhoneNumber(this@IntentActivity, "10086")
                }
            }
        }

        // 给10086发送内容为“Hello”的短信
        iiAdapter.addItem("发送短信") {
            // 关于smsto的内容，请参考下列链接
            // https://developer.android.com/guide/components/intents-common?hl=zh-cn
            IntentUtils.sendMmsMessage(this, "Hello", "10086")
        }

        // 打开浏览器
        iiAdapter.addItem("打开浏览器") {
            IntentUtils.openWebPage(this, "http://www.baidu.com")
        }

        iiAdapter.addItem("无线设置") {
            val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                SimpleToast.showShortMsg("未找到对应应用")
            }
        }

        iiAdapter.addItem("联系人") {
            val intent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = ContactsContract.Contacts.CONTENT_URI
            }
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                SimpleToast.showShortMsg("未找到对应应用")
            }
        }

        iiAdapter.addItem("显示启动") {
            val intent = Intent(this@IntentActivity, FirstActivity::class.java)
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                SimpleToast.showShortMsg("未找到对应应用")
            }
        }

        iiAdapter.addItem("返回数据") {
            val mars = Intent(this@IntentActivity, MarsActivity::class.java).apply {
                putExtra(FROM_EARTH_KEY, "地球来的消息:我是来自地球上的Tom，火星的朋友你好!")
            }
            getReturnString.launch(mars)
        }

        iiAdapter.addItem("复杂数据传递") {
            val intent = Intent(this@IntentActivity, ComplexDataActivity::class.java)

            // 传递基本数据类型或String类型数组
            val bd = Bundle()
            bd.putStringArray("StringArray", arrayOf("请求", "全球"))

            // 传递基本数据类型或String类型集合
            intent.putStringArrayListExtra("StringList", object : ArrayList<String>() {
                init {
                    add("请求")
                    add("全球")
                }
            })

            // 传递Bitmap
            val inputStream = resources.assets.open("car.png")
            val bmp = BitmapFactory.decodeStream(inputStream)
            bd.putParcelable("bitmap", bmp)
            intent.putExtras(bd)

            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                SimpleToast.showShortMsg("未找到对应应用")
            }
        }

        // 相当于发送带附件的短信
        iiAdapter.addItem("发送彩信") {
            val picture = File(FileMgr.appInternalFilesDir().path, "/pictures/android.png")
            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileMgr.getFileUriAboveApi24(picture, "${AppUtils.getPackageName()}.fileProvider")
            } else {
                FileMgr.getFileUriOnApi23(picture)
            }
            val intent = Intent(Intent.ACTION_SEND).apply {
                // 短信地址 设置类型
                setDataAndType(Uri.parse("10086"), "image/png")
                // 短信内容
                putExtra("sms_body", "Hello")
                putExtra(Intent.EXTRA_STREAM, uri)
                // 授予 Uri 读取权限
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                SimpleToast.showShortMsg("未找到对应应用")
            }
        }

        iiAdapter.addItem("播放文件") {
            requestPermission(Permission.READ_MEDIA_AUDIO) {
                granted = {
                    playWav()
                }
            }
        }

        iiAdapter.addItem("安装APK") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !packageManager.canRequestPackageInstalls()) {
                val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
                    data = Uri.parse("package:${AppUtils.getPackageName()}")
                }
                if (intent.resolveActivity(packageManager) != null) {
                    installRequest.launch(intent)
                } else {
                    SimpleToast.showShortMsg("未找到对应应用")
                }
            } else {
                installApk()
            }
        }

        // 打开另一程序
        iiAdapter.addItem("打开程序") {
            val intent = Intent().apply {
                val pkg = "com.github.androidhappyclub.uisample"
                val cn = ComponentName(pkg, "$pkg.ExpandableListViewActivity")
                component = cn
                action = Intent.ACTION_VIEW
            }

            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                SimpleToast.showShortMsg("未找到对应应用")
            }
        }

        iiAdapter.addItem("打开录音机") {
            val intent = Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION)
            if (intent.resolveActivity(packageManager) != null) {
                recorder.launch(intent)
            } else {
                SimpleToast.showShortMsg("未找到对应应用")
            }
        }

        iiAdapter.addItem("拍照") {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePhoto.launch(intent)
        }

        binding.intents.adapter = iiAdapter
        binding.intents.layoutManager = GridLayoutManager(this, 3)
    }

    /**
     * 下载测试的 apk 文件。
     */
    private fun downloadApk() {
        // 创建根目录
        FileMgr.makeDir(apkRoot).result.onSuccess {
            logger.d("${apkRoot.path} 创建成功")
        }.onFailure {
            logger.e(it.message.toString())
        }

        // 创建下载任务下载测试 apk
        val task = DLManager.createTaskConfig()
            .setDownloadUrl("https://alixiaobai.cn/files/task_stack.apk")
            .setSaveDir(apkRoot.path)
            .setListener {
                onFailure = {
                    Log.e("IntentActivity", "下载遭遇异常 ${it.exception.message}")
                }
                onDownloading = {
                    Log.d("IntentActivity", "当前的下载进度是 ${it.rate}")
                }
            }
            .build()
        task.start()
    }

    /**
     * 安装测试的 apk 文件。
     */
    private fun installApk() {
        val file = File(apkRoot, "task_stack.apk")
        if (!file.exists()) return
        val intent = Intent(Intent.ACTION_DEFAULT)
        val uri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            FileMgr.getFileUriAboveApi24(file, "${AppUtils.getPackageName()}.fileProvider")
        } else {
            FileMgr.getFileUriOnApi23(file)
        }
        intent.apply {
            setDataAndType(uri, FileMgr.getMimeType(file, ""))
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            SimpleToast.showShortMsg("未找到对应应用")
        }
    }

    /**
     * 从 assets 中读取音频文件
     */
    private fun readWav() {
        val file = FileMgr.getAssetsFile("test.wav", wavRoot)
        if (file.exists()) {
            logger.d("音频文件保存成功")
        } else {
            logger.e("音频文件保存失败")
        }
    }

    private fun playWav() {
        val file = File(wavRoot, "test.wav")
        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileMgr.getFileUriAboveApi24(file, "${AppUtils.getPackageName()}.fileProvider")
        } else {
            FileMgr.getFileUriOnApi23(file)
        }
        val intent = Intent(Intent.ACTION_DEFAULT).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            setDataAndType(uri, FileMgr.getMimeType(file, ""))
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            SimpleToast.showShortMsg("未找到对应应用")
        }
    }

}