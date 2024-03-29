package com.example.imagefilters.repositories

import android.graphics.Bitmap
import android.net.Uri
import com.example.imagefilters.data.ImageFilter

interface EditImageRepository {
    suspend fun prepareImagePreview(imageUri: Uri): Bitmap?
    suspend fun getImageFilters(image: Bitmap): List<ImageFilter>
}