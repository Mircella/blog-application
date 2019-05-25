package kz.mircella.blogapplication.userprofile.dto

import kz.mircella.blogapplication.userprofile.UserStatus

data class UserProfileResponse(val login:String,
                               val age: Int,
                               val country: String,
                               val occupancy: String,
                               val userStatus: UserStatus,
                               val registeredAt: String)