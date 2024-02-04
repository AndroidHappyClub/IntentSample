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

import android.content.Intent
import android.os.Bundle
import com.ave.vastgui.tools.viewbinding.viewBinding
import com.github.androidhappyclub.intentsample.databinding.ActivityMarsBinding

class MarsActivity : LifecycleActivity(R.layout.activity_mars) {

    private val binding by viewBinding(ActivityMarsBinding::bind)

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnReturn.setOnClickListener {
            val intent = Intent(this, IntentActivity::class.java).apply {
                putExtra(MarsResultName, "火星来的消息:我是火星Jack，非常高兴你能来火星")
            }
            setResult(RESULT_OK, intent)
            finish()
        }

        binding.tvContent.text = intent.getStringExtra(IntentActivity.FROM_EARTH_KEY)
    }

    companion object {
        const val MarsResultName = "FromMars"
    }
}
