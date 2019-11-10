package com.yanbin.watermarkeditor

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.enginebai.gallery.model.MimeType
import com.enginebai.gallery.ui.GalleryEngine
import com.enginebai.gallery.ui.REQUEST_SELECT_MEDIA
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val disposableBag = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonSingleSelection.setOnClickListener {
            GalleryEngine.Builder()
                .choose(MimeType.IMAGE)
                .forResult(this)
        }
        buttonMultipleSelection.setOnClickListener {
            GalleryEngine.Builder()
                .multiple(true)
                .maxSelect(5)
                .forResult(this)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SELECT_MEDIA && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, GalleryEngine.getSelectMediaPaths(data).toString(), Toast.LENGTH_SHORT).show()
        }
    }
}

