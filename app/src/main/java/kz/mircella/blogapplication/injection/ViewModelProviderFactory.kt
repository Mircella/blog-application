package kz.mircella.blogapplication.injection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kz.mircella.blogapplication.blogpost.BlogPostViewModel
import kz.mircella.blogapplication.forum.ForumTopicViewModel
import kz.mircella.blogapplication.video.VideoItemViewModel

@Suppress("UNCHECKED_CAST")
class BlogPostViewModelProviderFactory(private val blogPostTitle: String, private val navigationButtonClick: (next: Boolean) -> Unit) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BlogPostViewModel(blogPostTitle, navigationButtonClick) as T
    }
}

@Suppress("UNCHECKED_CAST")
class ForumViewModelProviderFactory(private val forumTopicTitle: String) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ForumTopicViewModel(forumTopicTitle) as T
    }
}


@Suppress("UNCHECKED_CAST")
class VideoViewModelProviderFactory(private val videoItemTitle: String) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return VideoItemViewModel(videoItemTitle) as T
    }
}