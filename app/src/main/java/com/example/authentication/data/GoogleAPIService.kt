package com.example.authentication.data

import com.example.authentication.data.response.DirectionPrediction
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


const val API_KEY = "AIzaSyBJYkizjyo1iqL6Flk5kSv8zypy3A0GG5s"

//url = https://maps.googleapis.com/maps/api/distancematrix/json?origins=46.79542968182268,9.8245288366505&destinations=45.47811143966341,9.23460752183241&sensor=false&units=metric&mode=driving&key=AIzaSyBJYkizjyo1iqL6Flk5kSv8zypy3A0GG5s

interface GoogleAPIService {

    @GET("distancematrix/json")
    fun getGoogleDirection(
        @Query("origins") origins: String,
        @Query("destinations") destinations: String,
        @Query("sensor") sensor: String = "false",
        @Query("units") units: String = "metrics",
        @Query("mode") mode: String
    ): Deferred<DirectionPrediction>

    companion object{
        operator fun invoke(): GoogleAPIService? {
            val requestInterceptor = Interceptor { chain ->

                val url = chain.request()
                    .url
                    .newBuilder()
                    .addQueryParameter("key", API_KEY)
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)
            }
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor).build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://maps.googleapis.com/maps/api/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GoogleAPIService::class.java)
        }
    }
}