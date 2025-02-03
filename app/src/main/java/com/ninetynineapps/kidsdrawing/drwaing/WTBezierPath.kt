package com.ninetynineapps.kidsdrawing.drwaing

import android.graphics.Path

class WTBezierPath : Path() {
    var strokeColor: Int = 0
    var strokeWidth: Float = 0.toFloat()
    var isEraser: Boolean = false
    var isCircle: Boolean = false
}