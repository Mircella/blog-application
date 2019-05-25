package kz.mircella.blogapplication.navigation

interface ForumNavigation {
    fun openForumFragment()
    fun openForumTopicFragment(selectedForumItemTitle: String)
}