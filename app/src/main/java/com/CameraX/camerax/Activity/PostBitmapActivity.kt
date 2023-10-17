package com.CameraX.camerax.Activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import java.io.ByteArrayOutputStream
import android.widget.TextView
import com.CameraX.camerax.Client.ApiClient
import com.CameraX.camerax.Model.ApiResponse
import com.CameraX.camerax.Model.ImageRequest
import com.CameraX.camerax.R
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers

class PostBitmapActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var buttonSendPic: Button
    private lateinit var textView: TextView
    var BitmapPictureSend: Bitmap? = null
    private var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_bitmap)

        imageView = findViewById(R.id.imageView)
        buttonSendPic = findViewById(R.id.buttonsend)
        textView = findViewById(R.id.textViewExtractText)
        val imageFileName = "horizontaldummypic"
        val resourceId = resources.getIdentifier(imageFileName, "drawable", packageName)
        if (resourceId != 0) {
            BitmapPictureSend = BitmapFactory.decodeResource(resources, resourceId)
            imageView.setImageBitmap(BitmapPictureSend)
        }
        buttonSendPic.setOnClickListener { BitmapPictureSend?.let { it1 -> sendImage(it1) } }
    }

    fun sendImage(bitmap: Bitmap) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)

        val apiService = ApiClient.buildService()
        compositeDisposable.add(
            apiService.sendImage(bitmap)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { response: ApiResponse ->

                    val resp = bitmap.let { ImageRequest(it) }?.let { apiService.sendImage(bitmap) }
                    if (resp != null) {

                        val extractedText = response.extractedText
                        Log.e("I Entered SendImage", "${extractedText.toString()}")
                        runOnUiThread {
                            textView.text = extractedText
                        }
                    }
                    else { error: Throwable ->
                        Log.e("NewsActivity", "Error fetching news articles: ${error.message}")
                    }
                })
    }
}