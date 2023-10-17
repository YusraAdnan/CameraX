package com.CameraX.camerax.API

import io.reactivex.Observable
import android.graphics.Bitmap
import com.CameraX.camerax.Model.ApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("prediction")
    fun sendImage(@Body image: Bitmap):
          Observable<ApiResponse>
}





