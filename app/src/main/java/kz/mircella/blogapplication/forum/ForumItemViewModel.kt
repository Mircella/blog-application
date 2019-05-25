package kz.mircella.blogapplication.forum

import androidx.lifecycle.MutableLiveData
import kz.mircella.blogapplication.forum.model.ForumItem

open class ForumItemViewModel(private val onForumItemClick: (forumItemTitle: String) -> Unit)  {
    private val topicTitle = MutableLiveData<String>()
    private val lastPostAuthor = MutableLiveData<String>()
    private val lastPostDate = MutableLiveData<String>()

    fun getTopicTitle(): MutableLiveData<String> {
        return topicTitle
    }

    fun getLastPostAuthor(): MutableLiveData<String> {
        return lastPostAuthor
    }

    fun getLastPostDate(): MutableLiveData<String> {
        return lastPostDate
    }

    fun bind(forumItem: ForumItem) {
        topicTitle.value = forumItem.topicTitle
        lastPostAuthor.value = forumItem.lastPostAuthor
        lastPostDate.value = forumItem.lastPostDate
    }

    fun onForumItemClick() {
        onForumItemClick(topicTitle.value!!)
    }
}