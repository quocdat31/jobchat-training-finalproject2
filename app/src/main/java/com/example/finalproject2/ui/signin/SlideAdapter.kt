package com.example.finalproject2.ui.signin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import com.example.finalproject2.R


class SlideAdapter(var context: Context) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return 3
    }

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val modelObject = Model.values()[position]
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(modelObject.layoutResId, collection, false) as ViewGroup
        collection.addView(layout)
        return layout
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }

}

enum class Model(val titleResId: Int, val layoutResId: Int) {
    FIRST(1, R.layout.slide_item),
    SECOND(2, R.layout.slide_item_2),
    THIRD(3, R.layout.slide_item_3)
}
