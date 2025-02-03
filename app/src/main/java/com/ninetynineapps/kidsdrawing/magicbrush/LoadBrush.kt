package com.ninetynineapps.kidsdrawing.magicbrush

import android.content.Context

import java.util.ArrayList

internal class LoadBrush(private val context: Context) {

    private var loadBrush: LoadBrush? = null
    private val loadbrushlist = ArrayList<BrushEffectLoad>()

    init {
        this.loadbrushlist.add(BrushLoad("brush/brush1.png", 2, 3, true, 0.15f, 120))
        this.loadbrushlist.add(BrushLoad("brush/brush2.png", 2, 2, true, 0.15f, 120))
        this.loadbrushlist.add(BrushLoad("brush/brush3.png", 1, 2, true, 0.15f, 120))
        this.loadbrushlist.add(BrushLoad("brush/brush4.png", 1, 3, true, 0.15f, 120))
        this.loadbrushlist.add(BrushLoad("brush/brush5.png", 2, 2, true, 0.15f, 120))
        this.loadbrushlist.add(BrushLoad("brush/brush6.png", 2, 2, true, 0.15f, 120))
        this.loadbrushlist.add(BrushLoad("brush/brush7.png", 2, 2, true, 0.15f, 120))
        this.loadbrushlist.add(BrushLoad("brush/brush8.png", 2, 2, true, 0.15f, 120))
        this.loadbrushlist.add(BrushLoad("brush/brush9.png", 1, 3, true, 0.15f, 120))
        this.loadbrushlist.add(BrushLoad("brush/brush10.png", 2, 2, true, 0.15f, 120))
    }

    fun loadBrushInstance(context: Context): LoadBrush {
        if (loadBrush == null) {
            loadBrush = LoadBrush(context)
        }
        return loadBrush!!
    }

    private fun BrushLoad(
        path: String,
        row: Int,
        column: Int,
        rotate: Boolean,
        unknown: Float,
        alpha: Int
    ): BrushEffectLoad {
        val brushEffectLoad = BrushEffectLoad()
        brushEffectLoad.setContext(this.context)
        brushEffectLoad.setFromResource(BrushEffectLoad.BrushEffectFromResource.ASSERT)
        brushEffectLoad.setPath(path)
        brushEffectLoad.setRow(row)
        brushEffectLoad.setColumn(column)
        brushEffectLoad.setRotate(rotate)
        brushEffectLoad.setUnknown(unknown)
        brushEffectLoad.setAlpha(alpha)
        return brushEffectLoad
    }

    fun loadBrushInstance(i: Int): BrushEffectLoad {
        return this.loadbrushlist[i]
    }
}
