package com.ninetynineapps.kidsdrawing.activities

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.myandroid.views.ScaleGestureDetector
import com.myandroid.views.Vector2D
import com.ninetynineapps.kidsdrawing.R
import com.ninetynineapps.kidsdrawing.adapters.*
import com.ninetynineapps.kidsdrawing.common.CommonConstants
import com.ninetynineapps.kidsdrawing.common.CommonUtilities
import com.ninetynineapps.kidsdrawing.custom.CustomProgressDialog
import com.ninetynineapps.kidsdrawing.databinding.ActivityBgImageListBinding
import com.ninetynineapps.kidsdrawing.databinding.ActivityEditorBinding
import com.ninetynineapps.kidsdrawing.drwaing.WTDrawingView
import com.ninetynineapps.kidsdrawing.interfaces.AdapterItemTypeCallback
import com.ninetynineapps.kidsdrawing.interfaces.StickerCallback
import com.ninetynineapps.kidsdrawing.magicbrush.OverlayBrushView
import com.ninetynineapps.kidsdrawing.mywork.FullScreenActivity
import com.ninetynineapps.kidsdrawing.pojo.*
import com.ninetynineapps.kidsdrawing.sticker.StickerView
import com.ninetynineapps.kidsdrawing.stickerview.StickerConstant
import com.ninetynineapps.kidsdrawing.stickerview.StickerImageView
import com.ninetynineapps.kidsdrawing.utils.Utils
import com.warkiz.widget.IndicatorSeekBar
import com.warkiz.widget.OnSeekChangeListener
import com.warkiz.widget.SeekParams
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.lang.System.currentTimeMillis
import java.util.*

class EditorActivity : AppCompatActivity(), AdapterItemTypeCallback {

    private var fontClassArrayList = ArrayList<FontClass>()
    private var mbButtonClassArrayList = ArrayList<MagicBrushClass>()
    private var pbClassArrayList = ArrayList<PaintBrushClass>()
    private var pencilClassArrayList = ArrayList<PencilClass>()
    private var stickerClassArrayList = ArrayList<StickerClass>()

    private var fontStyleAdapter: FontStyleAdapter? = null
    private var magicBrushButtonAdapter: MagicBrushButtonAdapter? = null
    private var paintBrushAdapter: PaintBrushAdapter? = null
    private var pencilAdapter: PencilAdapter? = null
    private var stickerAdapter: StickerAdapter? = null

    private var isEasy = true
    private var itemPos = 0

    private var mPrevX: Float = 0.toFloat()
    private var mPrevY: Float = 0.toFloat()

    private var bgImagePath: String? = null

    lateinit var paintBrushView: WTDrawingView
    lateinit var magicBrushView: OverlayBrushView
    lateinit var stickerView: StickerView

    private var selectedPBColor: Int = 0
    private var lastSelectedPBColor: Int = 0
    private var selectedPencilColor: Int = 0
    private var lastSelectedPencilColor: Int = 0
    private var selectedMBContent: String = ""
    private var lastSelectedMBContent: String = ""

    private var textSize = 12f
    private var typeface: Typeface? = null
    private var textStyle = Typeface.NORMAL
    private var textAlpha = 1.0f
    private var textColor = 0

    val minBrushSize = 10F
    private var isBrushActive = true

    private lateinit var tvTextActive: AppCompatTextView
    private var isDataUpdated = false

    var IMAGE_WIDTH: Int = 0
    var IMAGE_HEIGHT: Int = 0

    private var saveBitmap: Bitmap? = null

    private var file: File? = null
    private var f: File? = null
    private var uri: Uri? = null
    private var fileName = ""
    private lateinit var binding: ActivityEditorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setContentView(R.layout.activity_editor)
//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        initViews(this)
        loadDefaults(this)
        loadDataFromIntent()
        Utils.loadBannerAd(binding.llAdView,binding.llAdViewFacebook,this)
//        Utils.loadBannerAd(adView)
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun initViews(context: Context) {
        fontClassArrayList = ArrayList()
        mbButtonClassArrayList = ArrayList()
        pbClassArrayList = ArrayList()
        pencilClassArrayList = ArrayList()
        stickerClassArrayList = ArrayList()

        paintBrushView = findViewById(R.id.drawing_view)
        stickerView = findViewById(R.id.sticker_view)
        magicBrushView = findViewById(R.id.drawingViewBitmap1)

        binding.rvFontStyle.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.rvPencil.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.rvPaintBrush.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.rvMGButton.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.rvSticker.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)


        initAction()
        initActionBrush()
        initActionPencil()
        initActionMagicBrush()
        initActionSticker()
        initActionTextView()


    }

    /*
    * TODO View Action
    * Here All Icon Click Event Set Start
    */

    private fun initAction() {

        binding.llBack.setOnClickListener {
            CommonUtilities.showAlertFinishOnClick(this, isDataUpdated)
        }

        binding.llSave.setOnClickListener {
            if (Utils.checkPermission(this) ) {
                if (!CommonConstants.IS_CLEAR) {
                    saveBitmap()
                }else{
                    Toast.makeText(applicationContext,"Please select at least one item",Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.llBrush.setOnClickListener {
            activeBrush()
        }

        binding.llMagicBrush.setOnClickListener {
            activeMagicBrush()
        }

        binding.llPencil.setOnClickListener {
            activePencilView()
        }

        binding.llSticker.setOnClickListener {
            activeStickerView()
        }

        binding.llAddText.setOnClickListener {
            addTextView()
        }


    }

    private fun initActionBrush() {

        binding.llRedo.setOnClickListener {
            paintBrushView.redo()
        }

        binding.llUndo.setOnClickListener {
            paintBrushView.undo()
        }

        binding.ivEraser.setOnClickListener {
            paintBrushView.setEraserMode(true)

        }

        binding.ivBackBrush.setOnClickListener {
            if (binding.llBrushControl.visibility == View.VISIBLE) {
                binding.llBrushControl.visibility = View.GONE
                binding.rvPaintBrush.visibility = View.VISIBLE
            } else {
                binding.llBrushView.visibility = View.GONE
                binding.llButton.visibility = View.VISIBLE
                adjustDefault()
                selectedPBColor = 0
            }
        }

        binding.ivSettingBrush.setOnClickListener {
            binding.rvPaintBrush.visibility = View.GONE
            binding.llBrushControl.visibility = View.VISIBLE
        }

        binding.sbSmoothness.onSeekChangeListener = object : OnSeekChangeListener {
            override fun onSeeking(seekParams: SeekParams?) {
                if (seekParams!!.progressFloat == minBrushSize) {
                    paintBrushView.clearMaskFilter()
                } else {
                    paintBrushView.setBlurSize(seekParams.progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar?) {

            }
        }

        binding.sbBrushSize.onSeekChangeListener = object : OnSeekChangeListener {
            override fun onSeeking(seekParams: SeekParams?) {
                if (seekParams!!.progressFloat == minBrushSize) {
                    paintBrushView.strokeWidth = minBrushSize
                } else {
                    paintBrushView.strokeWidth = seekParams.progressFloat
                }
            }

            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar?) {
            }
        }

        binding.sbSmoothness.setProgress(binding.sbSmoothness.progress.toFloat())
        binding.sbBrushSize.setProgress(binding.sbBrushSize.progress.toFloat())

    }

    private fun adjustDefault() {
        magicBrushView.bringToFront()
        paintBrushView.bringToFront()
        binding.ivBackground.bringToFront()
    }

    private fun initActionPencil() {

        binding.ivBackPencil.setOnClickListener {
            binding.llPencilView.visibility = View.GONE
            binding.llButton.visibility = View.VISIBLE
            paintBrushView.setStrokeColor(0)
            adjustDefault()
        }
    }

    private fun initActionMagicBrush() {
        binding.ivBackMagicBrush.setOnClickListener {
            binding.llMagicBrushView.visibility = View.GONE
            binding.llButton.visibility = View.VISIBLE
            magicBrushView.closeTouch()
            binding.llClear.visibility = View.GONE
            binding.llEraser.visibility = View.VISIBLE
            adjustDefault()
        }

        binding.llClear.setOnClickListener {
            magicBrushView.clearMagicBrush()
        }
    }

    private fun initActionSticker() {
        binding.ivBackSticker.setOnClickListener {
            binding.llStickerView.visibility = View.GONE
            binding.llButton.visibility = View.VISIBLE
            adjustDefault()
        }

        stickerView.setOnTouchListener { _, _ ->
            if (StickerConstant.ivSticker != null) {
                StickerConstant.ivSticker.setControlItemsHidden(true)
            }
            if (StickerConstant.tvSticker != null) {
                StickerConstant.tvSticker.setBackgroundResource(0)
            }
            false
        }

        binding.rlMain.setOnTouchListener { v, _ ->
            if (StickerConstant.ivSticker != null) {
                StickerConstant.ivSticker.setControlItemsHidden(true)
            }
            false
        }
    }

    private fun initActionTextView() {
        binding.ivBackTextView.setOnClickListener {
            binding.llTextView.visibility = View.GONE
            binding.llButton.visibility = View.VISIBLE
            adjustDefault()
            if (StickerConstant.tvSticker != null) {
                StickerConstant.tvSticker.setBackgroundResource(0)
            }
        }

        binding.ivFontColor.setOnClickListener {
            ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose color")
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener { selectedColor ->

                }
                .setPositiveButton("ok") { d, lastSelectedColor, allColors ->
                    StickerConstant.tvSticker.setTextColor(lastSelectedColor)
                    d!!.dismiss()
                }
                .setNegativeButton("cancel") { dialog, which ->
                    dialog!!.dismiss()
                }
                .build()
                .show();
        }
    }


    /*
    * TODO Active Deactive View
    * Here Hide/Show View
    */

    private fun activeBrush() {
        binding.llBrushView.visibility = View.VISIBLE
        binding.llButton.visibility = View.GONE
        binding.llMagicBrushView.visibility = View.GONE
        binding.llPencilView.visibility = View.GONE
        magicBrushView.bringToFront()
        paintBrushView.bringToFront()
        binding.ivBackground.bringToFront()
        paintBrushView.setPencilView(false)
        paintBrushView.setEraserMode(false)
        isBrushActive = true
        selectedPBColor = lastSelectedPBColor
        paintBrushView.setStrokeColor(selectedPBColor)
        paintBrushView.setBlurSize(binding.sbSmoothness.progress)
        magicBrushView.closeTouch()

        if (binding.sbSmoothness.progress.toFloat() > 10) {
            paintBrushView.setBlurSize(binding.sbSmoothness.progress)
        }

        if (binding.sbBrushSize.progress > 10) {
            binding.sbBrushSize.setProgress(binding.sbBrushSize.progress.toFloat())
        }

        binding.llClear.visibility = View.GONE
        binding.llEraser.visibility = View.VISIBLE
    }



    private fun activeMagicBrush() {
        binding.llMagicBrushView.visibility = View.VISIBLE
        binding.llButton.visibility = View.GONE
        binding.llBrushView.visibility = View.GONE
        binding.llPencilView.visibility = View.GONE
        binding.llStickerView.visibility = View.GONE
        paintBrushView.bringToFront()
        magicBrushView.bringToFront()
        binding.ivBackground.bringToFront()
        paintBrushView.setPencilView(false)
        selectedMBContent = lastSelectedMBContent
        magicBrushView.setBrushStyle(selectedMBContent, this)
        isBrushActive = false
        paintBrushView.setStrokeColor(0)

        binding.llClear.visibility = View.VISIBLE
        binding.llEraser.visibility = View.GONE


    }

    private fun activePencilView() {
        binding.llButton.visibility = View.GONE
        binding.llBrushView.visibility = View.GONE
        binding.llMagicBrushView.visibility = View.GONE
        binding.llPencilView.visibility = View.VISIBLE
        magicBrushView.bringToFront()
        paintBrushView.bringToFront()
        binding.ivBackground.bringToFront()
        isBrushActive = true
        selectedPencilColor = lastSelectedPencilColor
        paintBrushView.setStrokeColor(selectedPencilColor)
        paintBrushView.setPencilView(true)
        paintBrushView.clearMaskFilter()
        magicBrushView.closeTouch()

        binding.llClear.visibility = View.GONE
        binding.llEraser.visibility = View.VISIBLE
    }

    private fun activeStickerView() {
        binding.llButton.visibility = View.GONE
        binding.llBrushView.visibility = View.GONE
        binding.llMagicBrushView.visibility = View.GONE
        binding.llPencilView.visibility = View.GONE
        binding.llStickerView.visibility = View.VISIBLE
        magicBrushView.bringToFront()
        paintBrushView.bringToFront()
        binding.ivBackground.bringToFront()
        stickerView.bringToFront()
        paintBrushView.setStrokeColor(0)
        magicBrushView.closeTouch()
        binding.llClear.visibility = View.GONE
        binding.llEraser.visibility = View.VISIBLE
    }

    private fun addTextView() {
        CommonConstants.IS_CLEAR = false
        CommonConstants.IS_CLEAR_TEXT = false
        magicBrushView.bringToFront()
        paintBrushView.bringToFront()
        binding.rvFontStyle.visibility = View.VISIBLE
        binding.ivBackground.bringToFront()
        paintBrushView.setStrokeColor(0)
        magicBrushView.closeTouch()
        binding.llClear.visibility = View.GONE
        binding.llEraser.visibility = View.VISIBLE

        val tvNewItem = AppCompatTextView(this)
        tvNewItem.id = View.generateViewId()
        tvNewItem.text = "Text Here"
        tvNewItem.tag = ""
        tvNewItem.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
        tvNewItem.setTypeface(typeface, textStyle)
        tvNewItem.alpha = 1.0f
        tvNewItem.setTextColor(textColor)
        tvNewItem.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_white_border))
        tvNewItem.gravity = Gravity.CENTER
        tvNewItem.setOnTouchListener(MultiTouch())
        binding.flMainLayout.addView(tvNewItem)
        tvNewItem.bringToFront()
        tvTextActive = tvNewItem
        StickerConstant.tvSticker = tvNewItem

        binding.llTextView.visibility = View.VISIBLE
        binding.llButton.visibility = View.GONE
    }

    private fun showTextDialog(context: Context, text: String) {
        binding.llTextView.visibility = View.VISIBLE
        binding.rvFontStyle.visibility = View.VISIBLE
        binding.llButton.visibility = View.GONE
        var addTextDialog: AlertDialog? = null
        try {
            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_text, null)

            val builder = AlertDialog.Builder(context)
            builder.setTitle(null)
            builder.setMessage(null)
            builder.setCancelable(true)
            builder.setView(dialogView)

            val etText = dialogView.findViewById(R.id.etText) as AppCompatEditText
            val tvCancel = dialogView.findViewById(R.id.tvCancel) as AppCompatTextView
            val tvOK = dialogView.findViewById(R.id.tvOK) as AppCompatTextView

            etText.setText(text)

            tvCancel.setOnClickListener {
                addTextDialog!!.dismiss()
            }
            tvOK.setOnClickListener {
                val enteredText = etText.text.toString()
                if (enteredText.isEmpty()) {
                    CommonUtilities.showToast(context, CommonConstants.MsgPleaseEnterText)
                } else {
                    val tvSticker = tvTextActive
                    val layoutParam = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                    )
                    tvSticker.layoutParams = layoutParam
                    tvSticker.text = etText.getText().toString()

                    val rlLayoutParam = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                    )
                    binding.flMainLayout.updateViewLayout(tvSticker, rlLayoutParam)
                    tvTextActive = tvSticker
                    StickerConstant.tvSticker = tvSticker

                    addTextDialog!!.dismiss()
                }
            }
            addTextDialog = builder.create()
            if (!addTextDialog!!.isShowing) {
                addTextDialog.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /*
    * TODO Load Content
    * Here Loading Default Content
    */

    private fun loadDefaults(context: Context) {

        selectedPBColor = 0
        lastSelectedPBColor = 0
        selectedPencilColor = 0
        lastSelectedPencilColor = 0
        selectedMBContent = ""
        lastSelectedMBContent = ""

        textSize = 12f
        try {
            typeface = Typeface.createFromAsset(
                this.assets,
                CommonConstants.DirFont + "/" + CommonConstants.DefaultAssetsFont
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        textStyle = Typeface.NORMAL
        textAlpha = 1.0f
        textColor = ContextCompat.getColor(this, R.color.colorBlack)

        tvTextActive = AppCompatTextView(context)

        fontStyleAdapter = FontStyleAdapter(context, fontClassArrayList, this)
        pencilAdapter = PencilAdapter(context, pencilClassArrayList, this)
        paintBrushAdapter = PaintBrushAdapter(context, pbClassArrayList, this)
        magicBrushButtonAdapter = MagicBrushButtonAdapter(context, mbButtonClassArrayList, this)
        stickerAdapter = StickerAdapter(context, stickerClassArrayList, this)
    }

    private fun loadDataFromIntent() {
        val intent = intent
        if (intent != null && intent.hasExtra(CommonConstants.KeyBGImageType)) {
            isEasy = intent.getBooleanExtra(CommonConstants.KeyBGImageType, true)
            itemPos = intent.getIntExtra(CommonConstants.KeyItemPos, 0)
            LoadResourcesAsyncForEditor(this).execute()
        }
    }

    private fun loadBitmap(resource: Bitmap?) {
        binding.ivBackground.setImageBitmap(resource)

        if (isEasy) {
            val vto = binding.ivBackground.viewTreeObserver
            vto.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    binding.ivBackground.viewTreeObserver.removeOnPreDrawListener(this)
                    IMAGE_WIDTH = binding.ivBackground.measuredWidth;
                    IMAGE_HEIGHT = binding.ivBackground.measuredHeight;

                    binding.ivBackground.layoutParams.width = IMAGE_WIDTH
                    binding.ivBackground.layoutParams.height = IMAGE_HEIGHT
                    val layoutParams1 = binding.ivBackground.layoutParams as RelativeLayout.LayoutParams
                    layoutParams1.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)

                    paintBrushView.layoutParams.width = IMAGE_WIDTH
                    paintBrushView.layoutParams.height = IMAGE_HEIGHT
                    val layoutParams2 = paintBrushView.layoutParams as RelativeLayout.LayoutParams
                    layoutParams2.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)

                    magicBrushView.layoutParams.width = IMAGE_WIDTH
                    magicBrushView.layoutParams.height = IMAGE_HEIGHT
                    val layoutParams3 = magicBrushView.layoutParams as RelativeLayout.LayoutParams
                    layoutParams3.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)

                    binding.rlMain.layoutParams.width = IMAGE_WIDTH
                    binding.rlMain.layoutParams.height = IMAGE_HEIGHT
                    val layoutParams4 = binding.rlMain.layoutParams as RelativeLayout.LayoutParams
                    layoutParams4.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)

                    binding.ivBackground.invalidate()
                    binding.ivBackground.requestLayout()

                    paintBrushView.invalidate()
                    paintBrushView.requestLayout()

                    magicBrushView.invalidate()
                    magicBrushView.requestLayout()


                    binding.rlMain.invalidate()
                    binding.rlMain.requestLayout()
                    binding.ivBackground.visibility = View.VISIBLE
                    binding.flMainLayout.invalidate()
                    binding.flMainLayout.requestLayout()
                    return true
                }

            })
        }

    }

    /*
    * TODO Save Image
    * Here Loading Default Content
    */
    private fun saveBitmap() {
        if (StickerConstant.ivSticker != null) {
            StickerConstant.ivSticker.setControlItemsHidden(true)
            StickerConstant.ivSticker = null
        }
        binding.flMainLayout.isDrawingCacheEnabled = true
        binding.flMainLayout.buildDrawingCache(true)
        if (saveBitmap != null) {
            saveBitmap!!.recycle()
            saveBitmap = null
        }

        saveBitmap = null
        binding.flMainLayout.invalidate()
        saveBitmap = Bitmap.createBitmap(binding.flMainLayout.drawingCache)
        saveImageToGallery()
    }

    private fun saveImageToGallery() {
//        Utils.onClickSave(this, this)

        fileName = currentTimeMillis().toString()
        if (android.os.Environment.getExternalStorageState() == android.os.Environment.MEDIA_MOUNTED) {
            file = File(filesDir.toString() + "" + File.separator +""+Environment.DIRECTORY_PICTURES+"/"+ resources.getString(R.string.folder_name))
            if (!file!!.exists()) {
                file!!.mkdirs()
            }
            f = File(file!!.absolutePath + "/" + fileName + ".jpg")
            uri = FileProvider.getUriForFile(this, "$packageName.provider", f!!)
        }
        var ostream: FileOutputStream? = null
        try {
            ostream = FileOutputStream(f)
        } catch (e1: FileNotFoundException) {
            e1.printStackTrace()
        }
        Log.e("TAG", "saveImageToGallery:::::Imaghe :::  $file  $f  $uri" )
        if (saveBitmap != null) {
            if (ostream != null) {
                saveBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, ostream)
            }
            Toast.makeText(this,"Save successfully to my creation",Toast.LENGTH_SHORT).show()
            val intent = Intent(applicationContext,FullScreenActivity::class.java)
            intent.putExtra(CommonConstants.LOCAL_IMAGE, "file://" + f!!.absolutePath)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Please Try Again", Toast.LENGTH_LONG).show()
        }

        try {
            ostream!!.close()
        } catch (e1: IOException) {
            e1.printStackTrace()
            Toast.makeText(this, "Save Failed", Toast.LENGTH_LONG).show()
        }
    }



    /*
    * TODO RecycleView Item Callback
    * Here Loading Default Content
    */
    override fun onAdapterItemTypeClick(mType: String, mPos: Int) {
        when (mType) {
            CommonConstants.ClickTypeFont -> {
                CommonConstants.IS_CLEAR = false
                CommonConstants.IS_CLEAR_TEXT = false
                val aClass = fontClassArrayList[mPos]
                typeface = aClass.fontTypeFace
                tvTextActive.setTypeface(typeface, textStyle)
            }
            CommonConstants.ClickTypePaint -> {
                val aClass = pbClassArrayList[mPos]
                try {
                    paintBrushView.clearMaskFilter()
                    if (paintBrushView.isDrawingMode()) {
                        paintBrushView.setEraserMode(false)
                        binding.sbSmoothness.setProgress(10F)
                    }

                    selectedPBColor = aClass.pbColor
                    lastSelectedPBColor = aClass.pbColor
                    paintBrushView.setStrokeColor(aClass.pbColor)

                    if (binding.sbSmoothness.progress.toFloat() > 10) {
                        paintBrushView.setBlurSize(binding.sbSmoothness.progress)
                    }

                    if (binding.sbBrushSize.progress > 10) {
                        binding.sbBrushSize.setProgress(binding.sbBrushSize.progress.toFloat())
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            CommonConstants.ClickTypePencil -> {

                val aClass = pencilClassArrayList[mPos]
                try {
                    paintBrushView.clearMaskFilter()

                    if (paintBrushView.isDrawingMode()) {
                        paintBrushView.setEraserMode(false)
                        binding.sbSmoothness.setProgress(10F)
                    }

                    selectedPencilColor = aClass.pencilColor
                    lastSelectedPencilColor = aClass.pencilColor
                    paintBrushView.setStrokeColor(aClass.pencilColor)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            CommonConstants.ClickTypeMagic -> {
                val aClass = mbButtonClassArrayList[mPos]

                selectedMBContent = aClass.mbImageName
                lastSelectedMBContent = aClass.mbImageName
                magicBrushView.setBrushStyle(selectedMBContent, this)
            }
            CommonConstants.ClickTypeSticker -> {

                CommonConstants.IS_CLEAR = false
                CommonConstants.IS_CLEAR_STICKER = false
                CommonConstants.STICKER_COUNT += 1

                val aClass = stickerClassArrayList[mPos]
                val inputStream = assets.open(
                    aClass.stickerImagePath.replace(
                        CommonConstants.AssetsFolderPath,
                        ""
                    )
                )
                val drawable = Drawable.createFromStream(inputStream, null)
                val ivSticker = StickerImageView(this@EditorActivity, object : StickerCallback {
                    override fun onDeleteSticker() {
                        CommonConstants.STICKER_COUNT -= 1
                        if (CommonConstants.STICKER_COUNT == 0){
                            if (CommonConstants.IS_CLEAR_TEXT && CommonConstants.IS_CLEAR_MAGIC && CommonConstants.IS_CLEAR_BRUSH && CommonConstants.IS_CLEAR_PENCIL){
                                CommonConstants.IS_CLEAR = true
                            }
                            CommonConstants.IS_CLEAR_STICKER = true
                        }
                        Log.e("TAG", "onDeleteSticker:::::Delete Sticker::::  "+CommonConstants.STICKER_COUNT+"  "+CommonConstants.IS_CLEAR_STICKER+"  "+ CommonConstants.IS_CLEAR  )
                    }

                })
                ivSticker.setImageDrawable(drawable)
                ivSticker.setScaleType()


                paintBrushView.setStrokeColor(0)
                magicBrushView.closeTouch()


                stickerView.addView(ivSticker)
                if (StickerConstant.ivSticker != null) {
                    StickerConstant.ivSticker!!.setControlItemsHidden(true)
                    StickerConstant.ivSticker = null
                }
                StickerConstant.ivSticker = ivSticker
            }
        }
    }


    @SuppressLint("StaticFieldLeak")
    inner class LoadResourcesAsyncForEditor(private val context: Context) : AsyncTask<Void, Void, Void>() {

        lateinit var pDialog: ProgressDialog

        override fun onPreExecute() {
            super.onPreExecute()
            pDialog = CustomProgressDialog.showProgress(context)
        }

        override fun doInBackground(vararg params: Void?): Void? {
            if (isEasy) {
                bgImagePath = CommonUtilities.getLargeEasyBGImageList(context)[itemPos].bgImagePath
            } else {
                bgImagePath = CommonUtilities.getLargeHardBGImageList(context)[itemPos].bgImagePath
            }

            fontClassArrayList = CommonUtilities.getFontStyleList(context)
            fontStyleAdapter = FontStyleAdapter(context, fontClassArrayList, this@EditorActivity)

            pencilClassArrayList = CommonUtilities.getPencilImageList(context)
            pencilAdapter = PencilAdapter(context, pencilClassArrayList, this@EditorActivity)

            pbClassArrayList = CommonUtilities.getPaintBrushImageList(context)
            paintBrushAdapter = PaintBrushAdapter(context, pbClassArrayList, this@EditorActivity)

            mbButtonClassArrayList = CommonUtilities.getMagicBrushButtonImageList(context)
            magicBrushButtonAdapter = MagicBrushButtonAdapter(context, mbButtonClassArrayList, this@EditorActivity)

            stickerClassArrayList = CommonUtilities.getStickerImageList(context)
            stickerAdapter = StickerAdapter(context, stickerClassArrayList, this@EditorActivity)

            return null
        }

        override fun onPostExecute(result: Void?) {
            CustomProgressDialog.hideProgress(pDialog)

            Glide.with(context)
                .asBitmap()
                .load(bgImagePath)
                .listener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        runOnUiThread() {
                            loadBitmap(resource)
                        }
                        return true
                    }

                }).submit()

            binding.rvFontStyle.adapter = fontStyleAdapter
            binding.rvPencil.adapter = pencilAdapter
            binding.rvPaintBrush.adapter = paintBrushAdapter
            binding.rvMGButton.adapter = magicBrushButtonAdapter
            binding.rvSticker.adapter = stickerAdapter
        }
    }

    inner class MultiTouch : View.OnTouchListener {
        var isRotateEnabled = false
        var isTranslateEnabled = true
        var isScaleEnabled = true
        var minimumScale = 0.5f
        var maximumScale = 3.0f
        private var mActivePointerId = -1

        private var mScaleGestureDetector = ScaleGestureDetector(ScaleGestureListener())

        lateinit var info1: TransformInfo

        private fun adjustAngle(degrees: Float): Float {
            var degrees = degrees
            if (degrees > 180.0f) {
                degrees -= 360.0f
            } else if (degrees < -180.0f) {
                degrees += 360.0f
            }
            return degrees
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        fun move(view: View?, info: TransformInfo) {
            computeRenderOffset(view!!, info.pivotX, info.pivotY)
            adjustTranslation(view, info.deltaX, info.deltaY)
            var scale = view.scaleX * info.deltaScale
            scale = Math.max(info.minimumScale, Math.min(info.maximumScale, scale))
            view.scaleX = scale
            view.scaleY = scale
            val rotation = adjustAngle(view.rotation + info.deltaAngle)
            view.rotation = rotation
        }

        private fun adjustTranslation(view: View, deltaX: Float, deltaY: Float) {
            val deltaVector = floatArrayOf(deltaX, deltaY)
            view.matrix.mapVectors(deltaVector)
            view.translationX = view.translationX + deltaVector[0]
            view.translationY = view.translationY + deltaVector[1]
        }

        private fun computeRenderOffset(view: View, pivotX: Float, pivotY: Float) {
            if (view.pivotX != pivotX || view.pivotY != pivotY) {
                val prevPoint = floatArrayOf(0.0f, 0.0f)
                view.matrix.mapPoints(prevPoint)
                view.pivotX = pivotX
                view.pivotY = pivotY
                val currPoint = floatArrayOf(0.0f, 0.0f)
                view.matrix.mapPoints(currPoint)
                val offsetX = currPoint[0] - prevPoint[0]
                val offsetY = currPoint[1] - prevPoint[1]
                view.translationX = view.translationX - offsetX
                view.translationY = view.translationY - offsetY
            }
        }


        var bClick = false
        lateinit var tmrClick: Timer

        override fun onTouch(view: View, event: MotionEvent): Boolean {
            view.bringToFront()
            this.mScaleGestureDetector.onTouchEvent(view, event)
            if (!this.isTranslateEnabled) {
                return true
            } else {
                val action = event.action
                val pointerIndex: Int
                when (action and event.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {

                        bClick = true
                        tmrClick = Timer()
                        tmrClick.schedule(object : TimerTask() {
                            override fun run() {
                                if (bClick) {
                                    bClick = false
                                }
                            }
                        }, 200)

                        mPrevX = event.x
                        mPrevY = event.y
                        this.mActivePointerId = event.getPointerId(0)
                    }
                    MotionEvent.ACTION_UP -> {
                        this.mActivePointerId = -1

                        if (bClick) {
                            if (StickerConstant.tvSticker != null) {
                                StickerConstant.tvSticker.setBackgroundResource(0)
                            }
                            tvTextActive = (view as AppCompatTextView)
                            StickerConstant.tvSticker = tvTextActive
                            StickerConstant.tvSticker.setBackgroundResource(R.drawable.ic_white_border)
                            showTextDialog(this@EditorActivity, tvTextActive.text.toString())
                            bClick = false
                        }
                    }
                    MotionEvent.ACTION_MOVE -> {
                        pointerIndex = event.findPointerIndex(this.mActivePointerId)
                        if (pointerIndex != -1) {
                            val currX = event.getX(pointerIndex)
                            val currY = event.getY(pointerIndex)
                            if (!this.mScaleGestureDetector.isInProgress) {
                                adjustTranslation(view, currX - mPrevX, currY - mPrevY)
                            }
                        }
                    }
                    MotionEvent.ACTION_CANCEL -> {
                        mActivePointerId = -1
                        pointerIndex = action and '\uff00'.toInt() shr 8
                        val pointerId = event.getPointerId(pointerIndex)
                        if (pointerId == mActivePointerId) {
                            val newPointerIndex = if (pointerIndex == 0) 1 else 0
                            mPrevX = event.getX(newPointerIndex)
                            mPrevY = event.getY(newPointerIndex)
                            mActivePointerId = event.getPointerId(newPointerIndex)
                        }
                    }
                    MotionEvent.ACTION_OUTSIDE,
                    MotionEvent.ACTION_POINTER_DOWN,
                    MotionEvent.ACTION_POINTER_UP -> {
                        pointerIndex = action and '\uff00'.toInt() shr 8
                        val pointerId = event.getPointerId(pointerIndex)
                        if (pointerId == mActivePointerId) {
                            val newPointerIndex = if (pointerIndex == 0) 1 else 0
                            mPrevX = event.getX(newPointerIndex)
                            mPrevY = event.getY(newPointerIndex)
                            mActivePointerId = event.getPointerId(newPointerIndex)
                        }
                    }
                }
                return true
            }
        }

        private inner class ScaleGestureListener :
            ScaleGestureDetector.SimpleOnScaleGestureListener() {
            private var mPivotX: Float = 0.toFloat()
            private var mPivotY: Float = 0.toFloat()
            private val mPrevSpanVector: Vector2D = Vector2D()

            override fun onScaleBegin(view: View?, detector: ScaleGestureDetector?): Boolean {
                mPivotX = detector!!.focusX
                mPivotY = detector.focusY
                mPrevSpanVector.set(detector.currentSpanVector)
                return true
            }

            override fun onScale(view: View?, detector: ScaleGestureDetector?): Boolean {
                val info = TransformInfo()
                info.deltaScale = if (isScaleEnabled) detector!!.scaleFactor else 1.0f
                info.deltaAngle =
                    if (isRotateEnabled) Vector2D.getAngle(mPrevSpanVector, detector!!.currentSpanVector) else 0.0f
                info.deltaX = if (isTranslateEnabled) detector!!.focusX - mPivotX else 0.0f
                info.deltaY = if (isTranslateEnabled) detector!!.focusY - mPivotY else 0.0f
                info.pivotX = mPivotX
                info.pivotY = mPivotY
                info.minimumScale = minimumScale
                info.maximumScale = maximumScale
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    move(view, info)
                }
                info1 = info
                return false
            }
        }

        inner class TransformInfo {
            var deltaX: Float = 0.toFloat()
            var deltaY: Float = 0.toFloat()
            var deltaScale: Float = 0.toFloat()
            var deltaAngle: Float = 0.toFloat()
            var pivotX: Float = 0.toFloat()
            var pivotY: Float = 0.toFloat()
            var minimumScale: Float = 0.toFloat()
            var maximumScale: Float = 0.toFloat()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        CommonUtilities.showAlertFinishOnClick(this, isDataUpdated)
    }

}