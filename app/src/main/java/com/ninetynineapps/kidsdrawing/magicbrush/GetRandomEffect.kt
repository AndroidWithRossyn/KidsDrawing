package com.ninetynineapps.kidsdrawing.magicbrush

import java.util.Random

class GetRandomEffect(var f11x: Float, var f12y: Float, brushEffectLoad: BrushEffectLoad) {
    var color = GetColorList.getRandomColorList()
    var f1427a: Int = 0
    var f1429c: Int = 0
    var f1430d: Int = 0

    init {
        val random = Random()
        this.f1429c = random.nextInt(brushEffectLoad.GetColumn() * brushEffectLoad.GetRow())
        this.f1430d = random.nextInt(45)
        this.f1427a = random.nextInt(2)
    }
}
