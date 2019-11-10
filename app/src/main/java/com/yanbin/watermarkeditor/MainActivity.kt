package com.yanbin.watermarkeditor

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.enginebai.gallery.ui.GalleryEngine
import com.enginebai.gallery.ui.REQUEST_SELECT_MEDIA
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonMultipleSelection.setOnClickListener {
            GalleryEngine.Builder()
                .multiple(true)
                .maxSelect(5)
                .forResult(this)
        }

        //set waterMark
        val waterMarkImage = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
        waterMark.setWaterMark(waterMarkImage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SELECT_MEDIA && resultCode == Activity.RESULT_OK) {
            val imagePaths = GalleryEngine.getSelectMediaPaths(data)
            Glide.with(this)
                .load(imagePaths[0])
                .into(waterMark)
        }
    }
}

