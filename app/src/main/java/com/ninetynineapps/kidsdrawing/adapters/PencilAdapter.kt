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
import com.ninetynineapps.kidsdrawing.pojo.PencilClass

class PencilAdapter(
    private val context: Context,
    private val pencilClassArrayList: ArrayList<PencilClass>,
    private val adapterItemTypeCallback: AdapterItemTypeCallback
) : androidx.recyclerview.widget.RecyclerView.Adapter<PencilAdapter.AdapterVH>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AdapterVH {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.row_pencil, p0, false)
        return AdapterVH(view)
    }

    override fun getItemCount(): Int {
        return pencilClassArrayList.size
    }

    var selectedPosition=-1
    override fun onBindViewHolder(p0: AdapterVH, p1: Int) {
        val aClass = pencilClassArrayList[p1]

        try {
            Glide.with(context)
                .load(aClass.pencilImagePath)
                .apply(
                    RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .placeholder(R.color.colorDefaultImg)
                        .fallback(R.color.colorDefaultImg)
                        .error(R.color.colorDefaultImg)
                )
                .into(p0.ivPencil)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (p1==selectedPosition) {
            val lp = p0.ivPencil.layoutParams as LinearLayout.LayoutParams
            lp.width = context.resources.getDimension(R.dimen.pencil_width).toInt();
            lp.height = context.resources.getDimension(R.dimen.pencil_height_selected).toInt();
            p0.ivPencil.layoutParams = lp

        } else {
            val lp = p0.ivPencil.layoutParams as LinearLayout.LayoutParams
            lp.width = context.resources.getDimension(R.dimen.pencil_width).toInt();
            lp.height = context.resources.getDimension(R.dimen.pencil_height).toInt();
            p0.ivPencil.layoutParams = lp
        }

        p0.ivPencil.setTag(R.string.adapter_key, p1)
    }

    inner class AdapterVH(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        val ivPencil = view.findViewById(R.id.ivPencil) as AppCompatImageView

        init {
            ivPencil.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val id = v!!.id
            var mPos = 0

            if (id == R.id.ivPencil) {
                mPos = v.getTag(R.string.adapter_key) as Int
                selectedPosition=mPos
                notifyDataSetChanged()
            }
            adapterItemTypeCallback.onAdapterItemTypeClick(CommonConstants.ClickTypePencil, mPos)
//            updateSelectedAndNotify(mPos)
        }

        private fun updateSelectedAndNotify(mPos: Int) {
            for (i in 0 until pencilClassArrayList.size) {
                pencilClassArrayList[i].isSelected = false
            }
            pencilClassArrayList[mPos].isSelected = true
            notifyDataSetChanged()
        }
    }
}