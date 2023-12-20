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
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import com.ave.vastgui.tools.viewbinding.viewBinding
import com.github.androidhappyclub.intentsample.databinding.ActivityFirstBinding

class FirstActivity : AppCompatActivity(R.layout.activity_first) {

    companion object{
        const val COUNT_KEY = "COUNT_KEY"
    }

    private val binding by viewBinding(ActivityFirstBinding::bind)
    private val checkBoxes: MutableList<CheckBox> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 添加到集合中
        checkBoxes.add(binding.cbxSing)
        checkBoxes.add(binding.cbxDance)
        checkBoxes.add(binding.cbxSport)
        checkBoxes.add(binding.cbxReadBook)

        binding.btnSubmit.setOnClickListener{
            val bundle = Bundle()
            var i = 0

            //将选中的喜好放到bundle中
            for (cbx in checkBoxes) {
                if (cbx.isChecked) {
                    bundle.putString("$i", "${cbx.text}")
                    i++
                }
            }

            //喜好的个数也放到bundle中
            bundle.putInt(COUNT_KEY, i)
            val intent = Intent(this, SecondActivity::class.java).apply {
                putExtras(bundle)
            }
            startActivity(intent)
        }
    }

}
