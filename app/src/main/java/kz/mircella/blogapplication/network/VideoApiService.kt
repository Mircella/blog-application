package kz.mircella.blogapplication.network

import io.reactivex.Observable
import io.reactivex.Single
import kz.mircella.blogapplication.video.dto.VideoDetails
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming

interface VideoApiService {
    @GET("videos")
    @Streaming
    fun getAllVideos(): Observable<ResponseBody>

    @GET("videos/{title}")
    @Streaming
    fun getVideoByTitle(@Path("title") title: String): Single<VideoDetails>
}