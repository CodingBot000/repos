package com.exam.timetable.di

import com.exam.timetable.api.ApiService
import com.exam.timetable.utils.Const
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URLDecoder


val networkModule = module {
    single { provideApiService(get()) }
    single { provideRetrofit(get(), Const.BASE_URL) }
    single { provideOkHttpClient() }
    single { provideOkHttpClientHeaderSupport() }
}



private fun provideOkHttpClient() = if (Const.HTTP_LOG) {
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()
} else OkHttpClient
    .Builder()
    .build()


private fun provideOkHttpClientHeaderSupport()  = OkHttpClient.Builder().addInterceptor { chain ->
    val original = chain.request()

    // 헤더를 자유 자재로 변경
    val builder = original.newBuilder()
    builder.addHeader("Content-Type", "application/json; charset=utf-8")
    builder.addHeader("Accept", "application/json; charset=utf-8")
    builder.method(original.method(), original.body())
    val request = builder.build()
    val response = chain.proceed(request)


    // 아래 소스는 response로 오는 데이터가 URLEncode 되어 있을 때

    // URLDecode 하는 소스 입니다.
    response.newBuilder()
        .body(
            ResponseBody.create(
                response.body()!!.contentType(),
                URLDecoder.decode(response.body()!!.string(), "utf-8")
            )
        )
        .build()
}

private fun provideRetrofit(
    okHttpClient: OkHttpClient,
    BASE_URL: String
): Retrofit =
    Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

private fun provideApiService(retrofit: Retrofit): ApiService =
    retrofit.create(ApiService::class.java)

