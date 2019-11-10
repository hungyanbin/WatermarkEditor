package com.yanbin.watermarkeditor

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.util.Log
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class WaterMarkProcessor {

    fun run(inputImages: List<String>, waterMark: Bitmap, renderMatrix: Matrix): Completable {
        return Observable.fromIterable(inputImages)
            .subscribeOn(Schedulers.io())
            .map { BitmapFactory.decodeFile(it) }
            .map { inputImage ->
                val canvas = Canvas()
                val outputImage = inputImage.copy(Bitmap.Config.ARGB_8888, true)
                canvas.setBitmap(outputImage)
                canvas.drawBitmap(waterMark, renderMatrix, null)
                outputImage
            }
            .doOnNext { image ->
                Log.i("testt", "image ${image.width}")
            }
            .ignoreElements()
    }
}