package com.codelabs.unikomradio.data.source.remote

import com.codelabs.unikomradio.BuildConfig
import com.codelabs.unikomradio.MyApplication
import com.codelabs.unikomradio.data.source.remote.plants.PlantsApiService
import com.codelabs.unikomradio.data.source.remote.plants.ProgramsApiService
import com.codelabs.unikomradio.data.source.remote.plants.TopchartsApiService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

interface ApiService {
    companion object Factory {
        private val getApiService : Retrofit by lazy {

            val mLoggingInterceptor = HttpLoggingInterceptor()
            mLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val cacheSize = (5 * 1024 * 1024).toLong()
            val appCache = Cache(MyApplication.getContext().cacheDir, cacheSize)
            val mClient = if (BuildConfig.DEBUG){
                OkHttpClient.Builder()
                    .cache(appCache)
                    .addInterceptor(mLoggingInterceptor)
                    .readTimeout(60,TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build()
            } else {
                OkHttpClient.Builder()
                    .cache(appCache)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build()
            }


            return@lazy Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(mClient)
                .build()
        }

        val plantApiService : PlantsApiService = getApiService.create(PlantsApiService::class.java)
        val programApiService : ProgramsApiService = getApiService.create(ProgramsApiService::class.java)
        val topchartApiService: TopchartsApiService = getApiService.create(TopchartsApiService::class.java)
    }
}