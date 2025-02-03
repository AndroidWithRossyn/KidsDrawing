package com.ninetynineapps.kidsdrawing.adapters

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.ninetynineapps.kidsdrawing.R
import com.ninetynineapps.kidsdrawing.interfaces.EditorItemCallback
import com.ninetynineapps.kidsdrawing.pojo.EditorItemClass

class EditorItemAdapter(
    private val context: Context,
    private val editorItemClassArrayList: ArrayList<EditorItemClass>,
    private val editorItemCallback: EditorItemCallback
) : androidx.recyclerview.widget.RecyclerView.Adapter<EditorItemAdapter.AdapterVH>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AdapterVH {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.cell_editor_item_list, p0, false)
        view.layoutParams.width = p0.width / 5
        return AdapterVH(view)
    }

    override fun getItemCount(): Int {
        return editorItemClassArrayList.size
    }

    override fun onBindViewHolder(p0: AdapterVH, p1: Int) {
        val aClass = editorItemClassArrayList[p1]
        p0.ivEditorItem.setImageDrawable(aClass.editorIcon)
        p0.tvEditorItem.text = aClass.editorTitle

        if (aClass.isSelected) {
            p0.ivEditorItem.setColorFilter(p0.colorThemeBlue)
            p0.tvEditorItem.setTextColor(p0.colorThemeBlue)
        } else {
            p0.ivEditorItem.setColorFilter(p0.colorGrey)
            p0.tvEditorItem.setTextColor(p0.colorGrey)
        }

        p0.llEditorItem.tag = p1
        p0.ivEditorItem.tag = p1
        p0.tvEditorItem.tag = p1

    }

    inner class AdapterVH(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view), View.OnClickListener {

        val colorGrey = ContextCompat.getColor(context, R.color.colorGrey)
        val colorThemeBlue = ContextCompat.getColor(context, R.color.colorDarkBlue)

        val llEditorItem = view.findViewById(R.id.llEditorItem) as LinearLayout
        val ivEditorItem = view.findViewById(R.id.ivEditorItem) as AppCompatImageView
        val tvEditorItem = view.findViewById(R.id.tvEditorItem) as AppCompatTextView

        init {
            view.setOnClickListener(this)
//            llEditorItem.setOnClickListener(this)
//            ivEditorItem.setOnClickListener(this)
//            tvEditorItem.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            var mPos = adapterPosition

//            val id = v!!.id
//            if (id == R.id.llEditorItem) {
//                mPos = v.tag as Int
//            } else if (id == R.id.llEditorItem) {
//                mPos = v.tag as Int
//            } else if (id == R.id.tvEditorItem) {
//                mPos = v.tag as Int
//            }

            if (mPos != -1) {
                editorItemCallback.onEditorItemClick(v!!.tag as Int)
                updateSelectedAndNotify(mPos)
            }
        }

        private fun updateSelectedAndNotify(mPos: Int) {
            for (i in 0 until editorItemClassArrayList.size) {
                editorItemClassArrayList[i].isSelected = false
            }
            editorItemClassArrayList[mPos].isSelected = true
            notifyDataSetChanged()
        }
    }
}