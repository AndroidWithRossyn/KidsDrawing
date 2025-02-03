package com.ninetynineapps.kidsdrawing.magicbrush

import android.graphics.Color

import java.util.Random

object GetColorList {
    private val colorlist = arrayOf(
        "#F44336",
        "#F44336",
        "#9C27B0",
        "#2196F3",
        "#3F51B5",
        "#673AB7",
        "#03A9F4",
        "#00BCD4",
        "#009688",
        "#4CAF50",
        "#CDDC39",
        "#8BC34A",
        "#FFEB3B",
        "#FFC107",
        "#FF9800",
        "#FF5722"
    )

    fun getRandomColorList(): Int {
        return Color.parseColor(colorlist[Random().nextInt(colorlist.size)])
    }
}
