package kz.mircella.blogapplication.navigation

import androidx.navigation.NavController
import kz.mircella.blogapplication.R
import kz.mircella.blogapplication.ui.blogpost.BlogPostFragment
import kz.mircella.blogapplication.ui.forum.ForumTopicFragment
import kz.mircella.blogapplication.ui.video.VideoItemFragment
import kz.mircella.blogapplication.video.dto.VideoDetails

class Navigator: BlogPostNavigation, ForumNavigation, VideoNavigation {

    override fun openForumTopicFragment(forumTopicTitle: String) {
        navController?.navigate(R.id.action_forumFragment_to_forumTopicFragment, ForumTopicFragment.createBundle(forumTopicTitle))
    }

    override fun openVideoFragment() {
        navController?.navigate(R.id.action_to_videoFragment)
    }

    override fun openVideoItemFragment(videoItemTitle: String) {
        navController?.navigate(R.id.action_videoFgragment_to_videoItemFragment, VideoItemFragment.createBundle(videoItemTitle))
    }

    override fun openForumFragment() {
        navController?.navigate(R.id.action_to_forumFragment)
    }

    private var navController: NavController? = null

    override fun openBlogPostFragment(blogPostTitle: String) {
        navController?.navigate(R.id.action_homeFragment_to_blogPostFragment, BlogPostFragment.createBundle(blogPostTitle))
    }

    fun bind(navController: NavController) {
        this.navController = navController
    }

    fun unbind() {
        navController = null
    }

    fun goBack() {
        navController?.popBackStack()
    }

    fun openHomeFragment() {
        navController?.navigate(R.id.action_to_homeFragment)
    }
}