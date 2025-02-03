package com.ninetynineapps.kidsdrawing.adapters

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ninetynineapps.kidsdrawing.R
import com.ninetynineapps.kidsdrawing.common.CommonConstants
import com.ninetynineapps.kidsdrawing.interfaces.AdapterItemTypeCallback
import com.ninetynineapps.kidsdrawing.pojo.FontClass

class FontStyleAdapter(
    private val context: Context,
    private val fontClassArrayList: ArrayList<FontClass>,
    private val adapterItemTypeCallback: AdapterItemTypeCallback
) : androidx.recyclerview.widget.RecyclerView.Adapter<FontStyleAdapter.AdapterVH>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AdapterVH {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.cell_font_style_list, p0, false)
        return AdapterVH(view)
    }

    override fun getItemCount(): Int {
        return fontClassArrayList.size
    }

    override fun onBindViewHolder(p0: AdapterVH, p1: Int) {
        val aClass = fontClassArrayList[p1]

        p0.tvFontStyle.typeface = aClass.fontTypeFace

        if (aClass.isSelected) {
            p0.tvFontStyle.background = ContextCompat.getDrawable(context, R.drawable.rounded_corner_back_theme_5_dp)
            p0.tvFontStyle.setTextColor(ContextCompat.getColor(context, R.color.colorWhite))
        } else {
            p0.tvFontStyle.background = ContextCompat.getDrawable(context, R.drawable.rounded_corner_back_white_5_dp)
            p0.tvFontStyle.setTextColor(ContextCompat.getColor(context, R.color.colorTheme))
        }

        p0.cvChild.tag = p1
        p0.tvFontStyle.tag = p1
    }

    inner class AdapterVH(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view), View.OnClickListener {

        val cvChild = view.findViewById(R.id.cvChild) as androidx.cardview.widget.CardView
        val tvFontStyle = view.findViewById(R.id.tvFontStyle) as AppCompatTextView

        init {
            cvChild.setOnClickListener(this)
            tvFontStyle.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val id = v!!.id
            var mPos = 0
            if (id == R.id.cvChild) {
                mPos = v.tag as Int
            } else if (id == R.id.tvFontStyle) {
                mPos = v.tag as Int
            }
            adapterItemTypeCallback.onAdapterItemTypeClick(CommonConstants.ClickTypeFont, mPos)
            updateSelectedAndNotify(mPos)
        }

        private fun updateSelectedAndNotify(mPos: Int) {
            for (i in 0 until fontClassArrayList.size) {
                fontClassArrayList[i].isSelected = false
            }
            fontClassArrayList[mPos].isSelected = true
            notifyDataSetChanged()
        }
    }
}