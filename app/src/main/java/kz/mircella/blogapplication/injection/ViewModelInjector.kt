package kz.mircella.blogapplication.injection

import dagger.Component
import kz.mircella.blogapplication.blogpost.BlogPostListViewModel
import kz.mircella.blogapplication.blogpost.BlogPostViewModel
import kz.mircella.blogapplication.forum.ForumViewModel
import kz.mircella.blogapplication.userprofile.UserProfileViewModel
import kz.mircella.blogapplication.video.VideoItemViewModel
import kz.mircella.blogapplication.video.VideoViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkClient::class])
interface ViewModelInjector {
    fun injectBlogPostViewModel(blogPostListViewModel: BlogPostListViewModel)
    fun injectBlogPostViewModel(blogPostViewModel: BlogPostViewModel)
    fun injectUserProfileViewModel(userProfileViewModel: UserProfileViewModel)
    fun injectForumViewModel(forumViewModel: ForumViewModel)
    fun injectVideoViewModel(videoViewModel: VideoViewModel)
    fun injectVideoItemViewModel(videoItemViewModel: VideoItemViewModel)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector

        fun networkClient(networkClient: NetworkClient): Builder
    }
}
