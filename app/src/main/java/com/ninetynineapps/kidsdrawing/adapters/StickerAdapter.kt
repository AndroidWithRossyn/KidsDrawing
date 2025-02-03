package com.ninetynineapps.kidsdrawing.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.ninetynineapps.kidsdrawing.R
import com.ninetynineapps.kidsdrawing.common.CommonConstants
import com.ninetynineapps.kidsdrawing.interfaces.AdapterItemTypeCallback
import com.ninetynineapps.kidsdrawing.pojo.StickerClass

class StickerAdapter(
    private val context: Context,
    private val stickerClassArrayList: ArrayList<StickerClass>,
    private val adapterItemTypeCallback: AdapterItemTypeCallback
) : androidx.recyclerview.widget.RecyclerView.Adapter<StickerAdapter.AdapterVH>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AdapterVH {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.row_sticker, p0, false)
        return AdapterVH(view)
    }

    override fun getItemCount(): Int {
        return stickerClassArrayList.size
    }

    override fun onBindViewHolder(p0: AdapterVH, p1: Int) {
        val aClass = stickerClassArrayList[p1]

        Glide.with(context)
            .load(aClass.stickerImagePath)
            .into(p0.ivSticker)

        p0.ivSticker.setTag(R.string.adapter_key, p1)
    }

    inner class AdapterVH(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view), View.OnClickListener {

        val ivSticker = view.findViewById(R.id.ivSticker) as AppCompatImageView

        init {
            ivSticker.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val id = v!!.id
            if (id == R.id.ivSticker) {
                adapterItemTypeCallback.onAdapterItemTypeClick(CommonConstants.ClickTypeSticker, v.getTag(R.string.adapter_key) as Int)
            }
        }
    }
}