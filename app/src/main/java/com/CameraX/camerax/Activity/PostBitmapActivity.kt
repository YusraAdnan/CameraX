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
import android.widget.Toast
import com.CameraX.camerax.Client.ApiClient
import com.CameraX.camerax.Model.ApiResponse
import com.CameraX.camerax.Model.ImageRequest
import com.CameraX.camerax.R
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

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
        val byteArray = byteArrayOutputStream.toByteArray()

        //https://www.youtube.com/watch?v=aY9xsGMlC5c
        val requestFile = RequestBody.create(MediaType.parse("image/jpeg"),byteArray)//request body showing data in binary format
        val body = MultipartBody.Part.createFormData("image","image.jpg", requestFile)
        Log.e("Enter Message","Entered the sendImage function")

        val apiService = ApiClient.buildService()
        compositeDisposable.add(
            apiService.SendImage(body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { response: ApiResponse ->
                    val extractedText = response.extractedText
                    textView.text = extractedText
                    //  val resp = bitmap.let { ImageRequest(it) }?.let { apiService.sendImage(bitmap) }
                }, {error: Throwable ->
                        Log.e("SendingImageError","Error sending image: ${error.message}")

                    }
        ))
    }
}