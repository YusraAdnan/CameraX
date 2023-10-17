package com.CameraX.camerax.API

import io.reactivex.Observable
import android.graphics.Bitmap
import com.CameraX.camerax.Model.ApiResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
   @Multipart
    @POST("prediction")
    fun SendImage(@Part image: MultipartBody.Part): Observable<ApiResponse>
}





