package com.ninetynineapps.kidsdrawing.common

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.ninetynineapps.kidsdrawing.R
import com.ninetynineapps.kidsdrawing.pojo.*
import java.util.*
import kotlin.collections.ArrayList


object CommonUtilities {


//    private var editorItemClassArrayList = ArrayList<EditorItemClass>()
//    private var smallEasyBGImgClassArrayList = ArrayList<BGImageClass>()
//    private var smallHardBGImgClassArrayList = ArrayList<BGImageClass>()

//    private var largeEasyBGImgClassArrayList = ArrayList<BGImageClass>()
//    private var largeHardBGImgClassArrayList = ArrayList<BGImageClass>()

//    private var fontClassArrayList = ArrayList<FontClass>()
//    private var mbButtonClassArrayList = ArrayList<MagicBrushClass>()
//    private var mbContentClassArrayList = ArrayList<MagicBrushClass>()
//    private var pbClassArrayList = ArrayList<PaintBrushClass>()
//    private var pencilClassArrayList = ArrayList<PencilClass>()
//    private var stickerClassArrayList = ArrayList<StickerClass>()

    //Todo-------------------- Connection & Toast --------------------
    fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }


    //Todo-------------------- Permissions --------------------
    fun checkRequiredPermission(context: Context): Boolean {
        val result1 = ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        val result2 = ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED
    }

    fun requestRequiredPermission(appCompatActivity: AppCompatActivity) {
        ActivityCompat.requestPermissions(
            appCompatActivity,
            arrayOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), CommonConstants.RequestCodePermission
        )
    }

    fun showPermissionConfirmDialog(appCompatActivity: AppCompatActivity) {
        val builder = AlertDialog.Builder(appCompatActivity)
        builder.setTitle(CommonConstants.CapPermission)
        builder.setMessage(CommonConstants.MsgAllowPermission)
        builder.setPositiveButton(CommonConstants.CapContinue) { dialog, which ->
            dialog.dismiss()
            requestRequiredPermission(appCompatActivity)
        }
        builder.setNegativeButton(CommonConstants.CapCancel) { dialog, which -> dialog.dismiss() }
        builder.show()
    }


    //Todo--------------------Finish Alert --------------------
    fun showAlertFinishOnClick(appCompatActivity: AppCompatActivity, isDataUpdated: Boolean) {
        val alertDialog = AlertDialog.Builder(appCompatActivity)
        alertDialog.setTitle(CommonConstants.CapConfirm)
        alertDialog.setMessage(CommonConstants.MsgDoYouWantToExit)
        alertDialog.setPositiveButton(CommonConstants.CapExit) { dialog, _ ->
            dialog.dismiss()
            val intent = Intent()
            intent.putExtra(CommonConstants.KeyIsDataUpdated, isDataUpdated)
            appCompatActivity.setResult(Activity.RESULT_OK, intent)
            appCompatActivity.finish()
        }
        alertDialog.setNegativeButton(CommonConstants.CapCancel) { dialog, _ -> dialog.dismiss() }
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    //Todo--------------------Contents From Assets & Drawables --------------------

    //Todo--------------------Small Easy & Hard BG Images --------------------
    fun getSmallEasyBGImageList(context: Context): ArrayList<BGImageClass> {
        val smallEasyBGImgClassArrayList = ArrayList<BGImageClass>()
//        if (smallEasyBGImgClassArrayList.isEmpty()) {
            try {
                val assetManager = context.assets
                val images = assetManager.list(CommonConstants.DirBGImageEasySmall)
                images!!.sort()

                for (i in 0 until images.size) {
                    val imageName = images[i]
//                    val inputStream = context.assets.open(CommonConstants.DirBGImageEasySmall + "/" + imageName)
                    val aClass = BGImageClass()
//                    aClass.bgImage = Drawable.createFromStream(inputStream, null)
//                    aClass.bgImage = BitmapFactory.decodeStream(inputStream)
                    aClass.bgImageName = imageName
                    aClass.bgImagePath =
                        CommonConstants.AssetsFolderPath.plus(CommonConstants.DirBGImageEasySmall).plus("/")
                            .plus(imageName)
                    smallEasyBGImgClassArrayList.add(aClass)
//                    inputStream.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
//        }
        return smallEasyBGImgClassArrayList
    }

    fun getSmallHardBGImageList(context: Context): ArrayList<BGImageClass> {
        val smallHardBGImgClassArrayList = ArrayList<BGImageClass>()
//        if (smallHardBGImgClassArrayList.isEmpty()) {
            try {
                val assetManager = context.assets
                val images = assetManager.list(CommonConstants.DirBGImageHardSmall)
                images!!.sort()

                for (i in 0 until images.size) {
                    val imageName = images[i]
//                    val inputStream = context.assets.open(CommonConstants.DirBGImageHardSmall + "/" + imageName)
                    val aClass = BGImageClass()
//                    aClass.bgImage = Drawable.createFromStream(inputStream, null)
//                    aClass.bgImage = BitmapFactory.decodeStream(inputStream)
                    aClass.bgImageName = imageName
                    aClass.bgImagePath =
                        CommonConstants.AssetsFolderPath.plus(CommonConstants.DirBGImageHardSmall).plus("/")
                            .plus(imageName)
                    smallHardBGImgClassArrayList.add(aClass)
//                    inputStream.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
//        }
        return smallHardBGImgClassArrayList
    }

    //Todo--------------------Large Easy & Hard BG Images --------------------
    fun getLargeEasyBGImageList(context: Context): ArrayList<BGImageClass> {
        val largeEasyBGImgClassArrayList = ArrayList<BGImageClass>()
//        if (largeEasyBGImgClassArrayList.isEmpty()) {
            try {
                val assetManager = context.assets
                val images = assetManager.list(CommonConstants.DirBGImageEasyLarge)
                images!!.sort()

                for (i in 0 until images.size) {
                    val imageName = images[i]
//                    val inputStream = context.assets.open(CommonConstants.DirBGImageHardSmall + "/" + imageName)
                    val aClass = BGImageClass()
//                    aClass.bgImage = Drawable.createFromStream(inputStream, null)
//                    aClass.bgImage = BitmapFactory.decodeStream(inputStream)
                    aClass.bgImageName = imageName
                    aClass.bgImagePath =
                        CommonConstants.AssetsFolderPath.plus(CommonConstants.DirBGImageEasyLarge).plus("/")
                            .plus(imageName)
                    largeEasyBGImgClassArrayList.add(aClass)
//                    inputStream.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
//        }
        return largeEasyBGImgClassArrayList
    }

    fun getLargeHardBGImageList(context: Context): ArrayList<BGImageClass> {
        val largeHardBGImgClassArrayList = ArrayList<BGImageClass>()
//        if (largeHardBGImgClassArrayList.isEmpty()) {
            try {
                val assetManager = context.assets
                val images = assetManager.list(CommonConstants.DirBGImageHardLarge)
                images!!.sort()

                for (i in 0 until images.size) {
                    val imageName = images[i]
//                    val inputStream = context.assets.open(CommonConstants.DirBGImageHardSmall + "/" + imageName)
                    val aClass = BGImageClass()
//                    aClass.bgImage = Drawable.createFromStream(inputStream, null)
//                    aClass.bgImage = BitmapFactory.decodeStream(inputStream)
                    aClass.bgImageName = imageName
                    aClass.bgImagePath =
                        CommonConstants.AssetsFolderPath.plus(CommonConstants.DirBGImageHardLarge).plus("/")
                            .plus(imageName)
                    largeHardBGImgClassArrayList.add(aClass)
//                    inputStream.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
//        }
        return largeHardBGImgClassArrayList
    }

    //Todo--------------------Font --------------------
    fun getFontStyleList(context: Context): ArrayList<FontClass> {
        val fontClassArrayList = ArrayList<FontClass>()
        if (fontClassArrayList.isEmpty()) {
            try {
                val assetManager = context.assets
                val images = assetManager.list(CommonConstants.DirFont)
                images!!.sort()

                for (i in images.indices) {
                    val aClass = FontClass()
                    aClass.name = images[i]
                    aClass.pos = i
                    aClass.fontTypeFace =
                        Typeface.createFromAsset(assetManager, CommonConstants.DirFont + "/" + images[i])
                    aClass.isSelected = false
                    fontClassArrayList.add(aClass)
                }

                Collections.sort(fontClassArrayList, object : Comparator<FontClass> {
                    override fun compare(o1: FontClass, o2: FontClass): Int {
                        val s1 = o1.name
                        val s2 = o2.name
                        return s1.compareTo(s2, true)
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return fontClassArrayList
    }

    //Todo--------------------Pencil --------------------
    fun getPencilImageList(context: Context): ArrayList<PencilClass> {
        val pencilClassArrayList = ArrayList<PencilClass>()
        if (pencilClassArrayList.isEmpty()) {
            try {
                val assetManager = context.assets
                val images = assetManager.list(CommonConstants.DirPencil)
                images!!.sort()

                val pencilColorArray = context.resources.getIntArray(R.array.PencilColorArray)

                for (i in 0 until images.size) {
                    val imageName = images[i]
//                    val inputStream = context.assets.open(CommonConstants.DirPencil + "/" + imageName)
                    val aClass = PencilClass()
//                    aClass.pencilImage = Drawable.createFromStream(inputStream, null)
//                    aClass.pencilImage = BitmapFactory.decodeStream(inputStream)
                    aClass.pencilColor=pencilColorArray[i]
                    aClass.pencilImagePath = CommonConstants.AssetsFolderPath.plus(CommonConstants.DirPencil).plus("/").plus(imageName)
                    aClass.isSelected = false
                    pencilClassArrayList.add(aClass)
//                    inputStream.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return pencilClassArrayList
    }

    //Todo--------------------Paint Brush --------------------
    fun getPaintBrushImageList(context: Context): ArrayList<PaintBrushClass> {
        val pbClassArrayList = ArrayList<PaintBrushClass>()
        if (pbClassArrayList.isEmpty()) {
            try {
                val assetManager = context.assets
                val images = assetManager.list(CommonConstants.DirPaintBrush)
                images!!.sort()

                val paintBrushColorArray = context.resources.getIntArray(R.array.PaintBrushColorArray)

                for (i in 0 until images.size) {
                    val imageName = images[i]
//                    val inputStream = context.assets.open(CommonConstants.DirPaintBrush + "/" + imageName)
                    val aClass = PaintBrushClass()
//                    aClass.pbImage = Drawable.createFromStream(inputStream, null)
//                    aClass.pbImage = BitmapFactory.decodeStream(inputStream)
                    aClass.pbColor = paintBrushColorArray[i]
                    aClass.pbImagePath = CommonConstants.AssetsFolderPath.plus(CommonConstants.DirPaintBrush).plus("/").plus(imageName)
                    pbClassArrayList.add(aClass)
//                    inputStream.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return pbClassArrayList
    }

    //Todo--------------------Magic Brush Button --------------------
    fun getMagicBrushButtonImageList(context: Context): ArrayList<MagicBrushClass> {
        val mbButtonClassArrayList = ArrayList<MagicBrushClass>()
        if (mbButtonClassArrayList.isEmpty()) {
            try {
                val assetManager = context.assets
                val mgButtonImages = assetManager.list(CommonConstants.DirMagicBrushButton)
                mgButtonImages!!.sort()

                val mgContentImages = assetManager.list(CommonConstants.DirMagicBrushContent)
                mgContentImages!!.sort()

                for (i in 0 until mgButtonImages.size) {
//                    val inputStream = context.assets.open(CommonConstants.DirMagicBrushButton + "/" + imageName)
                    val aClass = MagicBrushClass()
//                    aClass.mbImage = Drawable.createFromStream(inputStream, null)
//                    aClass.mbImage = BitmapFactory.decodeStream(inputStream)
                    aClass.mbImageName = mgContentImages[i]
                    aClass.mbImagePath = CommonConstants.AssetsFolderPath.plus(CommonConstants.DirMagicBrushButton).plus("/").plus(mgButtonImages[i])
                    mbButtonClassArrayList.add(aClass)
//                    inputStream.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return mbButtonClassArrayList
    }

    //Todo--------------------Sticker --------------------
    fun getStickerImageList(context: Context): ArrayList<StickerClass> {
        val stickerClassArrayList = ArrayList<StickerClass>()
        if (stickerClassArrayList.isEmpty()) {
            try {
                val assetManager = context.assets
                val images = assetManager.list(CommonConstants.DirSticker)
                images!!.sort()

                for (i in 0 until images.size) {
                    val imageName = images[i]
//                    val inputStream = context.assets.open(CommonConstants.DirSticker + "/" + imageName)
                    val aClass = StickerClass()
//                    aClass.stickerImage = Drawable.createFromStream(inputStream, null)
//                    aClass.stickerImage = BitmapFactory.decodeStream(inputStream)
                    aClass.stickerImagePath =
                        CommonConstants.AssetsFolderPath.plus(CommonConstants.DirSticker).plus("/").plus(imageName)
                    stickerClassArrayList.add(aClass)
//                    inputStream.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return stickerClassArrayList
    }





}