package com.example.oscar.hillfort.helpers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import com.example.oscar.hillfort.R
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

val REQUEST_CAMERA_PERMISSIONS_REQUEST_CODE = 35

fun showImagePicker(parent: Activity, id: Int) {
    val intent = Intent()
    intent.type = "image/*"
    intent.action = Intent.ACTION_OPEN_DOCUMENT
    intent.addCategory(Intent.CATEGORY_OPENABLE)
    val chooser = Intent.createChooser(intent, R.string.select_sight_image.toString())
    parent.startActivityForResult(chooser, id)
}

fun readImage(activity: Activity, resultCode: Int, data: Intent?): Bitmap? {
    var bitmap: Bitmap? = null
    if (resultCode == Activity.RESULT_OK && data != null && data.data != null) {
        try {
            bitmap = MediaStore.Images.Media.getBitmap(activity.contentResolver, data.data)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return bitmap
}

fun readImageFromPath(context: Context, path: String): Bitmap? {
    var bitmap: Bitmap? = null
    val uri = Uri.parse(path)
    if (uri != null) {
        try {
            val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r")
            val fileDescriptor = parcelFileDescriptor.fileDescriptor
            bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor.close()
        } catch (e: Exception) {
            return BitmapFactory.decodeFile(path)
        }
    }
    return bitmap
}

fun checkCameraPermissions(activity: Activity): Boolean {
    return if (ActivityCompat.checkSelfPermission(
            activity,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        true
    } else {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(android.Manifest.permission.CAMERA),
            REQUEST_CAMERA_PERMISSIONS_REQUEST_CODE
        )
        false
    }
}

fun dispatchTakePictureIntent(parent: Activity, imageNumber: Int) {
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    parent.startActivityForResult(intent, imageNumber)
}

@Throws(IOException::class)
fun createImageFile(parent: Activity): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val storageDir = parent.cacheDir
    val image = File.createTempFile(
        imageFileName,
        ".jpg",
        storageDir
    )

    return image
}
