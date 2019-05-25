package kz.mircella.blogapplication.forum

import androidx.lifecycle.MutableLiveData

open class ForumSectionHeaderViewModel {

    private val forumSectionHeader = MutableLiveData<String>()

    fun getForumSectionHeader(): MutableLiveData<String> {
        return forumSectionHeader
    }

    fun bind(sectionHeader: String) {
        forumSectionHeader.value = sectionHeader
    }
}