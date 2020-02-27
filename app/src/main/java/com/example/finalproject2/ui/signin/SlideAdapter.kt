package com.example.finalproject2.ui.signin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import com.example.finalproject2.R


class SlideAdapter(var context: Context) : PagerAdapter() {

    val slideDescription =
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum iaculis eleifend purus vel cursus."
    val slideTitle = "Lorem ipsum dolor sit amet"

    val listSlideDes = arrayListOf(slideDescription, slideDescription, slideDescription)
    val listSlideImg =
        arrayListOf(R.drawable.slide_image, R.drawable.slide_image2, R.drawable.slide_image3)
    val listSlideTitle = arrayListOf(slideTitle, slideTitle, slideTitle)


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

enum class Model private constructor(val titleResId: Int, val layoutResId: Int) {
    RED(1, R.layout.slide_item),
    BLUE(2, R.layout.slide_item_2),
    GREEN(3, R.layout.slide_item_3)
}
