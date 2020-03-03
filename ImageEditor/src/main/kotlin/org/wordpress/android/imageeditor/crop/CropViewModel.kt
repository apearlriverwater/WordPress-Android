package org.wordpress.android.imageeditor.crop

import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCrop.Options
import java.io.File

class CropViewModel : ViewModel() {
    private val _shouldCropAndSaveImage = MutableLiveData<Boolean>(false)
    val shouldCropAndSaveImage: LiveData<Boolean> = _shouldCropAndSaveImage

    private val _showCropScreenWithBundle = MutableLiveData<Bundle>()
    val showCropScreenWithBundle: LiveData<Bundle> = _showCropScreenWithBundle

    private lateinit var cacheDir: File
    private lateinit var inputFilePath: String
    private var isStarted = false

    private val cropOptions by lazy {
        Options().also {
            with(it) {
                setShowCropGrid(true)
                setFreeStyleCropEnabled(true)
                setShowCropFrame(true)
                setHideBottomControls(false)
            }
        }
    }

    private val cropOptionsBundleWithFilesInfo by lazy {
        Bundle().also {
            with(it) {
                putParcelable(UCrop.EXTRA_INPUT_URI, Uri.fromFile(File(inputFilePath)))
                putParcelable(UCrop.EXTRA_OUTPUT_URI, Uri.fromFile(File(cacheDir, IMAGE_EDITOR_OUTPUT_IMAGE_FILE_NAME)))
                putAll(cropOptions.optionBundle)
            }
        }
    }

    fun start(inputFilePath: String, cacheDir: File) {
        if (isStarted) {
            return
        }
        this.cacheDir = cacheDir
        this.inputFilePath = inputFilePath
        _showCropScreenWithBundle.value = cropOptionsBundleWithFilesInfo
        isStarted = true
    }

    fun onDoneMenuClicked() {
        _shouldCropAndSaveImage.value = true
    }

    companion object {
        const val IMAGE_EDITOR_OUTPUT_IMAGE_FILE_NAME = "image_editor_output_image.jpg"
    }
}