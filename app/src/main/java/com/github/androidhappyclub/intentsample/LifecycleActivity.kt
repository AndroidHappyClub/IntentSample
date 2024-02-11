/*
 * MIT License
 *
 * Copyright (c) 2024 AndroidHappyClub
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

import android.os.Bundle
import android.text.BoringLayout
import android.util.Log
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.github.androidhappyclub.intentsample.logger.mLogFactory

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/2/3

/**
 * [LifecycleActivity] .
 *
 * 用来打印日志周期。
 */
abstract class LifecycleActivity(@LayoutRes layoutId: Int) : AppCompatActivity(layoutId) {

    private val tag = this::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(tag, "调用 onCreate")
    }

    override fun onStart() {
        super.onStart()
        Log.d(tag, "调用 onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(tag, "调用 onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(tag, "调用 onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(tag, "调用 onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(tag, "调用 onDestroy")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(tag, "调用 onRestart")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(tag, "调用 onSaveInstanceState")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d(tag, "调用 onRestoreInstanceState")
    }

}