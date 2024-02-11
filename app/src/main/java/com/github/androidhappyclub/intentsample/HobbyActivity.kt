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

import android.content.Intent
import android.os.Bundle
import com.ave.vastgui.tools.viewbinding.viewBinding
import com.github.androidhappyclub.intentsample.databinding.ActivityHobbyBinding
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.snackbar.Snackbar

class HobbyActivity : LifecycleActivity(R.layout.activity_hobby) {

    companion object {
        const val COUNT_KEY = "COUNT_KEY"
    }

    private val mBinding by viewBinding(ActivityHobbyBinding::bind)
    private val mCheckBoxes: MutableList<MaterialCheckBox> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mCheckBoxes.add(mBinding.cbxSing)
        mCheckBoxes.add(mBinding.cbxDance)
        mCheckBoxes.add(mBinding.cbxSport)
        mCheckBoxes.add(mBinding.cbxReadBook)

        mBinding.btnSubmit.setOnClickListener {
            val bundle = Bundle()
            var count = 0

            //将选中的喜好放到bundle中
            for ((index, cbx) in mCheckBoxes.withIndex()) {
                if (cbx.isChecked) {
                    bundle.putString("$index", "${cbx.text}")
                    count++
                }
            }

            if (0 == count) {
                Snackbar
                    .make(this, mBinding.root, "请至少选择一个爱好", Snackbar.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            // 喜好的个数也放到 bundle 中
            bundle.putInt(COUNT_KEY, count)
            val intent = Intent(this, SelectedHobbyActivity::class.java).apply {
                putExtras(bundle)
            }
            startActivity(intent)
        }
    }
}