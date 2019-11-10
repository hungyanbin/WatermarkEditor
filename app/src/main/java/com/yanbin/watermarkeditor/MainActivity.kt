package com.yanbin.watermarkeditor

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.enginebai.gallery.ui.GalleryEngine
import com.enginebai.gallery.ui.REQUEST_SELECT_MEDIA
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val imagePaths = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //set waterMark
        val waterMarkImage = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
        waterMark.setWaterMark(waterMarkImage)

        buttonMultipleSelection.setOnClickListener {
            GalleryEngine.Builder()
                .multiple(true)
                .maxSelect(50)
                .forResult(this)
        }

        buttonProcess.setOnClickListener {
            val processor = WaterMarkProcessor()
            processor.run(inputImages = imagePaths,
                waterMark = waterMarkImage,
                renderMatrix = waterMark.getWaterMarkMatrix())
                .subscribe()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SELECT_MEDIA && resultCode == Activity.RESULT_OK) {
            imagePaths.clear()
            imagePaths.addAll(GalleryEngine.getSelectMediaPaths(data))
            val b = BitmapFactory.decodeFile(imagePaths[0])
            waterMark.setImageBitmap(b)
        }
    }
}

