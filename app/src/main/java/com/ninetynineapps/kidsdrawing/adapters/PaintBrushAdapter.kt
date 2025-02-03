package com.ninetynineapps.kidsdrawing.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatImageView
import com.ninetynineapps.kidsdrawing.R
import com.ninetynineapps.kidsdrawing.common.CommonConstants
import com.ninetynineapps.kidsdrawing.interfaces.AdapterItemTypeCallback
import com.ninetynineapps.kidsdrawing.pojo.PaintBrushClass




class PaintBrushAdapter(
    private val context: Context,
    private val paintBrushClassArrayList: ArrayList<PaintBrushClass>,
    private val adapterItemTypeCallback: AdapterItemTypeCallback
) : androidx.recyclerview.widget.RecyclerView.Adapter<PaintBrushAdapter.AdapterVH>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AdapterVH {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.row_brush, p0, false)
        return AdapterVH(view)
    }

    override fun getItemCount(): Int {
        return paintBrushClassArrayList.size
    }

    override fun onBindViewHolder(p0: AdapterVH, p1: Int) {
        val aClass = paintBrushClassArrayList[p1]


        try {
            val inputStream = context.assets.open(aClass.pbImagePath.replace(CommonConstants.AssetsFolderPath, ""))
            val drawable = Drawable.createFromStream(inputStream, null)

            val bitmap: Bitmap = (drawable as BitmapDrawable).bitmap
            p0.ivPaintBrush.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (aClass.isSelected) {
            p0.params.setMargins(p0.zeroDP, p0.zeroDP, p0.zeroDP, -20)
            p0.ivPaintBrush.layoutParams = p0.params
            p0.ivPaintBrush.layoutParams.width = context.resources.getDimension(R.dimen.paint_brush_width_selected).toInt()
            p0.ivPaintBrush.layoutParams.height = context.resources.getDimension(R.dimen.paint_brush_height_selected).toInt()
            p0.ivPaintBrush.requestLayout()
        } else {
            p0.params.setMargins(p0.zeroDP, p0.fifteenDp, p0.zeroDP, -20)
            p0.ivPaintBrush.layoutParams = p0.params
            p0.ivPaintBrush.layoutParams.width = context.resources.getDimension(R.dimen.paint_brush_width).toInt()
            p0.ivPaintBrush.layoutParams.height = context.resources.getDimension(R.dimen.paint_brush_height).toInt()
            p0.ivPaintBrush.requestLayout()
        }


        p0.ivPaintBrush.setTag(R.string.adapter_key, p1)
    }

    inner class AdapterVH(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view),
        View.OnClickListener {

        val params =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        val ivPaintBrush = view.findViewById(R.id.ivPaintBrush) as AppCompatImageView
        val zeroDP = context.resources.getDimension(R.dimen.zero_dp).toInt()
        val fifteenDp = context.resources.getDimension(R.dimen.fifteen_dp).toInt()

        init {
            ivPaintBrush.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val id = v!!.id
            var mPos = 0
            if (id == R.id.ivPaintBrush) {
                mPos = v.getTag(R.string.adapter_key) as Int
            }
            updateSelectedAndNotify(mPos)
            adapterItemTypeCallback.onAdapterItemTypeClick(CommonConstants.ClickTypePaint, mPos)
        }

        private fun updateSelectedAndNotify(mPos: Int) {
            for (i in 0 until paintBrushClassArrayList.size) {
                paintBrushClassArrayList[i].isSelected = false
            }
            paintBrushClassArrayList[mPos].isSelected = true
            notifyDataSetChanged()
        }
    }
}