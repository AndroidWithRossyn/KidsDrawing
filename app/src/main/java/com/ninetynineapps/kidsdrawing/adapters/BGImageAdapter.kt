package com.ninetynineapps.kidsdrawing.adapters

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.ninetynineapps.kidsdrawing.R
import com.ninetynineapps.kidsdrawing.custom.SquareCardView
import com.ninetynineapps.kidsdrawing.interfaces.AdapterItemCallback
import com.ninetynineapps.kidsdrawing.pojo.BGImageClass

class BGImageAdapter(
    private val context: Context,
    private val BGImageClassArrayList: ArrayList<BGImageClass>,
    private val adapterItemCallback: AdapterItemCallback
) : androidx.recyclerview.widget.RecyclerView.Adapter<BGImageAdapter.AdapterVH>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AdapterVH {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.cell_bg_image_list, p0, false)
        return AdapterVH(view)
    }

    override fun getItemCount(): Int {
        return BGImageClassArrayList.size
    }

    override fun onBindViewHolder(p0: AdapterVH, p1: Int) {

        val aClass = BGImageClassArrayList[p1]
        try {
            Glide.with(context)
                .load(aClass.bgImagePath)
                .apply(
                    RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .placeholder(R.color.colorWhite)
                        .fallback(R.color.colorWhite)
                        .error(R.color.colorWhite)
                )
                .into(p0.ivBGImage)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        p0.cvBGImage.tag = p1
//        p0.rlBGImage.tag = p1
        p0.ivBGImage.setTag(R.string.adapter_key, p1)
    }

    inner class AdapterVH(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view), View.OnClickListener {

        val cvBGImage = view.findViewById(R.id.cvBGImage) as SquareCardView
//        val rlBGImage = view.findViewById(R.id.rlBGImage) as RelativeLayout
        val ivBGImage = view.findViewById(R.id.ivBGImage) as AppCompatImageView

        init {
            cvBGImage.setOnClickListener(this)
//            rlBGImage.setOnClickListener(this)
            ivBGImage.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val id = v!!.id
            if (id == R.id.cvBGImage) {
                adapterItemCallback.onAdapterItemClick(v.tag as Int)
            }
//            else if (id == R.id.rlBGImage) {
//                adapterItemCallback.onAdapterItemClick(v.tag as Int)
//            }
            else if (id == R.id.ivBGImage) {
                adapterItemCallback.onAdapterItemClick(v.getTag(R.string.adapter_key) as Int)
            }
        }
    }
}