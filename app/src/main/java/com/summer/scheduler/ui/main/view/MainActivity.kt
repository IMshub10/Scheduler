package com.summer.scheduler.ui.main.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.goodiebag.horizontalpicker.HorizontalPicker
import com.summer.scheduler.R
import kotlinx.android.synthetic.main.schedule_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val hpText: HorizontalPicker = findViewById(R.id.hpicker)

        val textItems: MutableList<com.goodiebag.horizontalpicker.HorizontalPicker.PickerItem> =
            ArrayList()
        for (i in 1..4) {
            textItems.add(com.goodiebag.horizontalpicker.HorizontalPicker.TextItem("S$i"))
        }
        hpText.setItems(
            textItems,
            3
        ) //3 here signifies the default selected item. Use : hpText.setItems(textItems) if none of the items are selected by default.

    }
}