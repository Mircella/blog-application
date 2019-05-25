package kz.mircella.blogapplication.base

import androidx.lifecycle.ViewModel
import kz.mircella.blogapplication.blogpost.BlogPostListViewModel
import kz.mircella.blogapplication.blogpost.BlogPostViewModel
import kz.mircella.blogapplication.forum.ForumViewModel
import kz.mircella.blogapplication.injection.DaggerViewModelInjector
import kz.mircella.blogapplication.injection.NetworkClient
import kz.mircella.blogapplication.injection.ViewModelInjector
import kz.mircella.blogapplication.userprofile.UserProfileViewModel
import kz.mircella.blogapplication.video.VideoItemViewModel
import kz.mircella.blogapplication.video.VideoViewModel

abstract class BaseViewModel: ViewModel(){
    private val injector: ViewModelInjector = DaggerViewModelInjector
            .builder()
            .networkClient(NetworkClient)
            .build()

    init {
        inject()
    }

    private fun inject() {
        when(this) {
            is BlogPostListViewModel -> injector.injectBlogPostViewModel(this)
            is UserProfileViewModel -> injector.injectUserProfileViewModel(this)
            is ForumViewModel -> injector.injectForumViewModel(this)
            is BlogPostViewModel -> injector.injectBlogPostViewModel(this)
            is VideoViewModel -> injector.injectVideoViewModel(this)
            is VideoItemViewModel -> injector.injectVideoItemViewModel(this)
        }
    }
}