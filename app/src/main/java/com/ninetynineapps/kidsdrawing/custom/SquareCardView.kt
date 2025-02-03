package com.ninetynineapps.kidsdrawing.custom

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import android.util.AttributeSet

class SquareCardView : androidx.cardview.widget.CardView {
    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}