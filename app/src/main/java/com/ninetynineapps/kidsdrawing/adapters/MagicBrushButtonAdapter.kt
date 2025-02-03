package com.ninetynineapps.kidsdrawing.adapters

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.ninetynineapps.kidsdrawing.R
import com.ninetynineapps.kidsdrawing.common.CommonConstants
import com.ninetynineapps.kidsdrawing.interfaces.AdapterItemTypeCallback
import com.ninetynineapps.kidsdrawing.pojo.MagicBrushClass

class MagicBrushButtonAdapter(
    private val context: Context,
    private val mbButtonClassArrayList: ArrayList<MagicBrushClass>,
    private val adapterItemTypeCallback: AdapterItemTypeCallback
) : androidx.recyclerview.widget.RecyclerView.Adapter<MagicBrushButtonAdapter.AdapterVH>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AdapterVH {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.row_magic_brush, p0, false)
        return AdapterVH(view)
    }

    override fun getItemCount(): Int {
        return mbButtonClassArrayList.size
    }

    override fun onBindViewHolder(p0: AdapterVH, p1: Int) {
        val aClass = mbButtonClassArrayList[p1]

        try {
            Glide.with(context)
                .load(aClass.mbImagePath)
                .apply(
                    RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .placeholder(R.color.colorDefaultImg)
                        .fallback(R.color.colorDefaultImg)
                        .error(R.color.colorDefaultImg)
                )
                .into(p0.ivMBButton)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (aClass.isSelected) {
            p0.params.setMargins(p0.zeroDP, p0.zeroDP, p0.zeroDP, p0.zeroDP)
            p0.ivMBButton.layoutParams = p0.params
        } else {
            p0.params.setMargins(p0.zeroDP, p0.tenDp, p0.zeroDP, p0.zeroDP)
            p0.ivMBButton.layoutParams = p0.params
        }

        p0.ivMBButton.setTag(R.string.adapter_key, p1)
    }

    inner class AdapterVH(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val zeroDP = context.resources.getDimension(R.dimen.zero_dp).toInt()
        val tenDp = context.resources.getDimension(R.dimen.ten_dp).toInt()
        val params =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        val ivMBButton = view.findViewById(R.id.ivMBButton) as AppCompatImageView

        init {
            ivMBButton.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val id = v!!.id
            var mPos = 0
            if (id == R.id.ivMBButton) {
                mPos = v.getTag(R.string.adapter_key) as Int
            }
            updateSelectedAndNotify(mPos)
            adapterItemTypeCallback.onAdapterItemTypeClick(CommonConstants.ClickTypeMagic, mPos)
        }

        private fun updateSelectedAndNotify(mPos: Int) {
            for (i in 0 until mbButtonClassArrayList.size) {
                mbButtonClassArrayList[i].isSelected = false
            }
            mbButtonClassArrayList[mPos].isSelected = true
            notifyDataSetChanged()
        }
    }
}