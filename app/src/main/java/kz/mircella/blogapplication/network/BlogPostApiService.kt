package kz.mircella.blogapplication.network

import io.reactivex.Observable
import io.reactivex.Single
import kz.mircella.blogapplication.blogpost.dto.BlogPostDetails
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming

interface BlogPostApiService {

    @GET("blog-posts")
    @Streaming
    fun getAllBlogPosts(): Observable<ResponseBody>

    @GET("blog-posts/{title}")
    fun getBlogPost(@Path("title") title: String): Single<BlogPostDetails>
}