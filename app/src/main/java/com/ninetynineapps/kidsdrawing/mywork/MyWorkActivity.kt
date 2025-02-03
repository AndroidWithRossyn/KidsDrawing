package com.ninetynineapps.kidsdrawing.mywork

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.ninetynineapps.kidsdrawing.R
import com.ninetynineapps.kidsdrawing.common.CommonConstants
import com.ninetynineapps.kidsdrawing.custom.SquareImageView
import com.ninetynineapps.kidsdrawing.utils.Utils
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class MyWorkActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    lateinit var context: Context

    lateinit var txtNoItem: TextView

    lateinit var arrOfAllImages: ArrayList<HashMap<String, String>>

    lateinit var gridOfMyWork: GridView
    lateinit var gridViewAdapter: GridViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_work)
//        supportActionBar!!.hide()
//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        context = this
        initDefine()
        getImagesFromSdCard()
        Utils.loadBannerAd(findViewById(R.id.llAdView),findViewById(R.id.llAdViewFacebook),this)
    }

    private fun initDefine() {
        gridOfMyWork = findViewById(R.id.gridOfMyWork)
        txtNoItem = findViewById(R.id.txtNoItem)
        gridOfMyWork.onItemClickListener = this

        findViewById<ImageView>(R.id.ivBack).setOnClickListener {
            finish()
        }
    }

    private fun getImagesFromSdCard() {
        arrOfAllImages = ArrayList()
        val listFile: Array<File>
//        val file = File(context.filesDir.toString() + File.separator, context.resources.getString(R.string.folder_name))
        val file = File(filesDir.toString() + "" + File.separator +""+ Environment.DIRECTORY_PICTURES+"/"+ resources.getString(R.string.folder_name))
        if (file.isDirectory) {
            listFile = file.listFiles()
            for (i in listFile.indices) {
                val hashMap = HashMap<String, String>()
                Log.e("TAG", "getImagesFromSdCard:::::: ${listFile[i].absolutePath}" )
                hashMap[CommonConstants.LOCAL_IMAGE] = "file://" + listFile[i].absolutePath
                hashMap[CommonConstants.IMAGE_DATE] = getDate(listFile[i].absolutePath)
                arrOfAllImages.add(hashMap)
            }
        }
        setGridViewAdapter()
    }


    fun getDate(filePath: String): String {
        val file = File(filePath)
        val lastModDate = Date(file.lastModified())
        val sdf = SimpleDateFormat("dd MMM kk:mm EE")
        return sdf.format(lastModDate)
    }

    private fun setGridViewAdapter() {
        if (arrOfAllImages.size > 0) {
            arrOfAllImages.reverse()
            gridViewAdapter = GridViewAdapter()
            gridOfMyWork.adapter = gridViewAdapter
            txtNoItem.visibility = View.GONE
        } else {
            txtNoItem.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        getImagesFromSdCard()
        super.onResume()
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val intent = Intent(this, FullScreenActivity::class.java)
        intent.putExtra(CommonConstants.LOCAL_IMAGE, arrOfAllImages[position][CommonConstants.LOCAL_IMAGE])
        startActivity(intent)
    }


    inner class GridViewAdapter : BaseAdapter() {

        override fun getCount(): Int {
            return arrOfAllImages.size
        }

        override fun getItem(position: Int): Any {
            return position
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {

            var viewMyWork = LayoutInflater.from(context).inflate(R.layout.row_of_my_work, null)
            val cellImgMyWork: SquareImageView = viewMyWork.findViewById(R.id.cellImgMyWork)


            Glide.with(context)
                .load(arrOfAllImages[position][CommonConstants.LOCAL_IMAGE])
                .into(cellImgMyWork)

            return viewMyWork!!

        }
    }


}
