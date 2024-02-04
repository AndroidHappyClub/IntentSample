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

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.ave.vastgui.tools.graphics.BmpUtils
import com.ave.vastgui.tools.os.getParcelable
import com.ave.vastgui.tools.utils.AppUtils
import com.ave.vastgui.tools.viewbinding.viewBinding
import com.github.androidhappyclub.intentsample.databinding.ActivityComplexDataBinding

class ComplexDataActivity : AppCompatActivity(R.layout.activity_complex_data) {

    private val mBinding by viewBinding(ActivityComplexDataBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.getStringArrayListExtra(LIST_DATA)?.apply {
            val arrayAdapter = ArrayAdapter(
                this@ComplexDataActivity,
                android.R.layout.simple_list_item_1,
                this.toTypedArray()
            )
            mBinding.list.adapter = arrayAdapter
        }
        val callback = BmpUtils.getBitmapFromDrawable(R.drawable.android)
        val bmp = intent.extras?.getParcelable<Bitmap>(IMAGE, callback)
        mBinding.ivBmp.setImageBitmap(bmp)
    }

    companion object{
        const val LIST_DATA = "LIST_DATA"
        const val IMAGE = "IMAGE"
    }
}