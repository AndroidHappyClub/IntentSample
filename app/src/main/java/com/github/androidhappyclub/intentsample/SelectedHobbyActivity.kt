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
import androidx.activity.OnBackPressedCallback
import com.ave.vastgui.tools.viewbinding.viewBinding
import com.github.androidhappyclub.intentsample.databinding.ActivitySelectedHobbyBinding

class SelectedHobbyActivity : LifecycleActivity(R.layout.activity_selected_hobby) {

    private inner class IOnBackPressedCallback : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }

    private val binding by viewBinding(ActivitySelectedHobbyBinding::bind)
    private val mIOnBackPressedCallback by lazy {
        IOnBackPressedCallback()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(mIOnBackPressedCallback)

        //先获取用户的喜好个数
        val count = intent.getIntExtra(HobbyActivity.COUNT_KEY, 0)
        val sb = StringBuilder()

        //遍历喜好的内容
        for (i in 0 until count) {
            sb.append("${intent.getStringExtra("$i")} ")
        }

        //显示喜好
        binding.hobbyContent.text = sb.toString()
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onDestroy() {
        mIOnBackPressedCallback.remove()
        super.onDestroy()
    }
}