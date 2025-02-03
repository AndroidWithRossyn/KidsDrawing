package com.ninetynineapps.kidsdrawing.common

import com.ninetynineapps.kidsdrawing.stickerview.StickerImageView

object CommonConstants {

    const val MsgAllowPermission = "Please allow all required permission to continue..."
    const val MsgSomethingWrong = "Something went wrong. Please try again!"
    const val MsgDoubleBackToExit = "Click back again to Exit..."
    const val MsgDoYouWantToExit = "Are you sure do you want to exit?"
    const val MsgDoYouWantToAdd = "Are you sure do you want to add this item?"
    const val MsgDoYouWantToRemove = "Are you sure do you want to remove this item?"
    const val MsgDoYouWantToDuplicate = "Are you sure do you want to duplicate this item?"
    const val MsgDoYouWantToClean = "Are you sure do you want to clean this screen?"
    const val MsgPleaseSelectItem = "Please select item first."
    const val MsgNoItemsInEditor = "There are no items in editor."
    const val MsgPleaseEnterText = "Please enter some text."

    //Todo------------------------Code For Request & Result Handling------------------------
    const val ReqCodeDataUpdated = 111
    const val KeyIsDataUpdated = "IsDataUpdated"
    const val RequestCodePermission = 222

    const val KeyIsFirstTime = "IsFirstTime"
    const val KeyIsFrom = "IsFrom"
    const val KeyIsFromEditing = "IsFromEditing"
    const val KeyItemPos = "ItemPos"
    const val KeyBGImageType = "BGImageType"
    const val KeySavedDirName = "SavedDirName"

    //Todo------------------------Caption Or Hints For Views-----------------------------------
    const val CapPleaseWait = "Please wait..."
    const val CapPermission = "Permission"
    const val CapContinue = "Continue"
    const val CapConfirm = "Confirm"
    const val CapCancel = "Cancel"
    const val CapExit = "Exit"
    const val CapDuplicate = "Duplicate"
    const val CapAdd = "Add"
    const val CapRemove = "Remove"
    const val CapClean = "Clean"
    const val CapDelete = "Delete"

    //Todo------------------------ Assets & Storage Folder Names ------------------------------
    const val AssetsFolderPath = "file:///android_asset/"
    const val DirBGImageEasySmall = "BGImageEasySmall"
    const val DirBGImageHardSmall = "BGImageHardSmall"
    const val DirBGImageEasyLarge = "BGImageEasyLarge"
    const val DirBGImageHardLarge = "BGImageHardLarge"
    const val DirFont = "Font"
    const val DirMagicBrushButton = "MagicBrushButton"
    const val DirMagicBrushContent = "MagicBrushContent"
    const val DirPaintBrush = "PaintBrush"
    const val DirPencil = "Pencil"
    const val DirSticker = "Sticker"
    const val DirSavedCard = "SavedCard"

    //Todo-------------------------------- Defaults --------------------------------------
    const val DefaultAssetsFont = "Font_01.ttf"

    //Todo-------------------------------- Action Type --------------------------------------

    const val EditorItemText = "Text"
    const val EditorItemFont = "Font"
    const val EditorItemMagic = "Magic"
    const val EditorItemPencil = "Pencil"
    const val EditorItemPaint = "Paint"
    const val EditorItemSticker = "Sticker"

    const val ClickTypeFont = "Font"
    const val ClickTypePencil = "Pencil"
    const val ClickTypePaint = "Paint"
    const val ClickTypeMagic = "Magic"
    const val ClickTypeSticker = "Sticker"


    const val LOCAL_IMAGE = "IMAGE"
    const val IMAGE_DATE = "DATE"
    const val ARRAY_OF_IMAGE = "ARRAY_OF_IMAGE"

    public var POSITION=-1;

    var IS_CLEAR = true;
    var IS_CLEAR_BRUSH = true;
    var IS_CLEAR_MAGIC = true;
    var IS_CLEAR_PENCIL = true;
    var IS_CLEAR_STICKER = true;
    var IS_CLEAR_TEXT = true;
    var STICKER_COUNT = 0;




    const val GOOGLE_ADMOB_APP_ID = "ca-app-pub-3940256099942544~3347511713"
    const val GOOGLE_BANNER_ID = "ca-app-pub-3940256099942544/6300978111"
    const val GOOGLE_INTERSTITIAL_ID = "ca-app-pub-3940256099942544/1033173712"

    const val FB_BANNER_ID = "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID"
    const val FB_INTERSTITIAL_ID = "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID"

    const val AD_FACEBOOK = "facebook"
    const val AD_GOOGLE = "google"
    const val AD_TYPE_FACEBOOK_GOOGLE = AD_GOOGLE

    const val ENABLE = "Enable"
    const val DISABLE = "Disable"
    const val ENABLE_DISABLE = ENABLE

    const val AD_TYPE_FB_GOOGLE = "AD_TYPE_FB_GOOGLE"
    const val GOOGLE_BANNER = "GOOGLE_BANNER"
    const val GOOGLE_INTERSTITIAL = "GOOGLE_INTERSTITIAL"
    const val FB_BANNER = "FB_BANNER"
    const val FB_INTERSTITIAL = "FB_INTERSTITIAL"
    const val SPLASH_SCREEN_COUNT = "splash_screen_count"
    const val CLICK_IMAGE_COUNT = "click_image_count"
    const val STATUS_ENABLE_DISABLE = "STATUS_ENABLE_DISABLE"
}