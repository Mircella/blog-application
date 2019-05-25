package kz.mircella.blogapplication.network

import io.reactivex.Single
import kz.mircella.blogapplication.userprofile.dto.UserProfileResponse
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path

interface UserProfileApiService {

    @GET("users/{userId}")
    fun getUserProfile(@Path("userId") userId: String): Single<UserProfileResponse>

    @GET("users/avatar/{userId}")
    fun getUserAvatar(@Path("userId") userId: String): Single<ResponseBody>

}