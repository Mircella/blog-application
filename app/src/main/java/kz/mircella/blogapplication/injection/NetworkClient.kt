package kz.mircella.blogapplication.injection

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.Reusable
import kz.mircella.blogapplication.network.BlogPostApiService
import kz.mircella.blogapplication.network.ForumApiService
import kz.mircella.blogapplication.network.UserProfileApiService
import kz.mircella.blogapplication.network.VideoApiService
import kz.mircella.blogapplication.utils.BASE_URL
import kz.mircella.blogapplication.utils.LogLevel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@Suppress("unused")
object NetworkClient {
    var retrofit: Retrofit? = null

    fun getClient(logLevel: LogLevel): Retrofit {

        val interceptor = HttpLoggingInterceptor()
        when (logLevel) {
            LogLevel.LOG_NOT_NEEDED ->
                interceptor.level = HttpLoggingInterceptor.Level.NONE
            LogLevel.LOG_REQ_RES ->
                interceptor.level = HttpLoggingInterceptor.Level.BASIC
            LogLevel.LOG_REQ_RES_BODY_HEADERS ->
                interceptor.level = HttpLoggingInterceptor.Level.BODY
            LogLevel.LOG_REQ_RES_HEADERS_ONLY ->
                interceptor.level =
                        HttpLoggingInterceptor.Level.HEADERS
        }

        val client = OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                .addInterceptor(interceptor)
                .build()
        if (null == retrofit) {
            retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                            .setLenient()
                            .create()))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build()
        }

        return retrofit!!
    }

    @Provides
    fun logLevel(): LogLevel {
        return LogLevel.LOG_REQ_RES_BODY_HEADERS
    }

    @Provides
    @Reusable
    @JvmStatic
    fun getUserAPIService(logLevel: LogLevel) =
            getClient(logLevel).create(UserProfileApiService::class.java)

    @Provides
    @Reusable
    @JvmStatic
    fun getBlogPostApiService(logLevel: LogLevel) =
            getClient(logLevel).create(BlogPostApiService::class.java)

    @Provides
    @Reusable
    @JvmStatic
    fun getForumApiService(logLevel: LogLevel) =
            getClient(logLevel).create(ForumApiService::class.java)

    @Provides
    @Reusable
    @JvmStatic
    fun getVideoApiService(logLevel: LogLevel) =
            getClient(logLevel).create(VideoApiService::class.java)
}