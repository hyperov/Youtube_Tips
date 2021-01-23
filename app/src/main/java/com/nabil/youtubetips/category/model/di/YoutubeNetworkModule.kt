package com.nabil.youtubetips.category.model.di

import android.content.Context
import com.nabil.youtubetips.BuildConfig
import com.nabil.youtubetips.category.model.repo.YoutubeApis
import com.nabil.youtubetips.home.IS_CONNECTED
import com.nabil.youtubetips.home.Prefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit


@Module
@InstallIn(ApplicationComponent::class)
object YoutubeNetworkModule {

    @Provides
    fun getRetrofitInstance(@ApplicationContext context: Context): YoutubeApis {

        val cacheSize = (5 * 1024 * 1024).toLong()
        val myCache = Cache(context.cacheDir, cacheSize)

        var client = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            client.addInterceptor(interceptor)
        }

        client =
            client
                .cache(myCache)
                .addNetworkInterceptor { chain ->

                    val originalRequest: Request = chain.request()
                    val cacheHeaderValue =
                        if (Prefs.getBoolean(
                                IS_CONNECTED,
                                true
                            )
                        ) "public, max-age=2419200" else "public, only-if-cached, max-stale=2419200"
                    val request: Request = originalRequest.newBuilder().build()
                    val response: Response = chain.proceed(request)
                    response.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", cacheHeaderValue)
                        .build()
                }
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)

        val retrofit = Retrofit.Builder()
            .baseUrl(YoutubeApis.BASE_URL)
            .client(client.build())
            .addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
        return retrofit.create()
    }

}