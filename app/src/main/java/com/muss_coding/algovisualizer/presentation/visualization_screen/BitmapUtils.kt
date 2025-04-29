package com.muss_coding.algovisualizer.presentation.visualization_screen

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object BitmapUtils {

    private const val TAG = "BitmapUtils"

    /**
     * Saves a single Bitmap to the MediaStore (Gallery) or app-specific external storage.
     *
     * @param context The application context.
     * @param bitmap The Bitmap to save.
     * @param filename The desired filename (without extension).
     * @param format The Bitmap.CompressFormat (e.g., Bitmap.CompressFormat.JPEG, Bitmap.CompressFormat.PNG).
     * @param quality The compression quality (0-100, where 100 is lossless for PNG and highest quality for JPEG).
     * @return The Uri of the saved image if successful, null otherwise.
     */
    private fun saveBitmap(
        context: Context,
        bitmap: Bitmap,
        filename: String,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
        quality: Int = 90
    ): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "$filename.${format.toString().lowercase()}")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/${format.toString().lowercase()}")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "YourAppName") // Customize directory
                put(MediaStore.MediaColumns.IS_PENDING, 1) // For transactional writing
            } else {
                val directory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "YourAppName") // Ensure directory exists
                directory.mkdirs()
                put(MediaStore.MediaColumns.DATA, File(directory, "$filename.${format.toString().lowercase()}").absolutePath)
            }
        }

        val resolver = context.contentResolver
        var uri: Uri? = null

        try {
            uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            uri?.let { imageUri ->
                resolver.openOutputStream(imageUri)?.use { outputStream ->
                    if (!bitmap.compress(format, quality, outputStream)) {
                        throw IOException("Failed to save bitmap.")
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    contentValues.clear()
                    contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0) // Finalize writing
                    resolver.update(imageUri, contentValues, null, null)
                }
                Log.d(TAG, "Bitmap saved to: $imageUri")
                return imageUri
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error saving bitmap: ${e.message}")
            uri?.let { resolver.delete(it, null, null) } // Clean up if insertion failed
        }

        return null
    }

    /**
     * Compiles a list of Bitmaps to individual image files.
     *
     * @param context The application context.
     * @param bitmaps A list of Bitmap objects to save.
     * @param baseFilename A base filename for the images (will be appended with an index).
     * @param format The Bitmap.CompressFormat to use.
     * @param quality The compression quality.
     * @return A list of Uris for the saved images.
     */
    fun compileBitmapsToImages(
        context: Context,
        bitmaps: List<Bitmap>,
        baseFilename: String = "image",
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
        quality: Int = 90
    ): List<Uri?> {
        val savedUris = mutableListOf<Uri?>()
        for (i in bitmaps.indices) {
            val fileIndex = if ((i + 1) in (1..9)) {
                "0${i + 1}"
            } else "${i + 1}"
            val filename = "${baseFilename}_$fileIndex"
            val uri = saveBitmap(context, bitmaps[i], filename, format, quality)
            savedUris.add(uri)
        }
        return savedUris
    }
}