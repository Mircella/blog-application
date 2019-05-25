package kz.mircella.blogapplication.network

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming

interface ForumApiService {
    @GET("forum/sections")
    @Streaming
    fun getAllSections(): Observable<ResponseBody>
}