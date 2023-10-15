package com.CameraX.camerax

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class PDFActivity : AppCompatActivity() {
    private var imageView: ImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdfactivity)
        imageView = findViewById(R.id.imageviewPDf)

        val filepath = intent.getStringExtra("FilePath")
        if(!filepath.isNullOrBlank())
        {
            val bitmap = BitmapFactory.decodeFile(filepath)//converts filepath back to bitmap
            imageView?.setImageBitmap(bitmap)
        }

    }
}