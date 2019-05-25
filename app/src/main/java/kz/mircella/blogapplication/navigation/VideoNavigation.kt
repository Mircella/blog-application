package kz.mircella.blogapplication.navigation

import kz.mircella.blogapplication.video.dto.VideoDetails

interface VideoNavigation {
    fun openVideoFragment()
    fun openVideoItemFragment(videoItemTitle: String)
}