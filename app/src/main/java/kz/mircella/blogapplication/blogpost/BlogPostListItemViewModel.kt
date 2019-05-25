package kz.mircella.blogapplication.blogpost

import androidx.lifecycle.MutableLiveData
import kz.mircella.blogapplication.blogpost.model.BlogPostItem

class BlogPostListItemViewModel(private val blogPostListItemClick: (blogPostTitle: String) -> Unit) {

    private val blogPostTitle = MutableLiveData<String>()
    private val blogPostAuthor = MutableLiveData<String>()
    private val blogPostDate = MutableLiveData<String>()
    private var imageUrl: String? = null
    fun bind(blogPostItem: BlogPostItem) {
        blogPostTitle.value = blogPostItem.title
        blogPostAuthor.value = blogPostItem.authorId
        blogPostDate.value = blogPostItem.createdAt
        imageUrl = blogPostItem.imageUrl
    }

    fun getBlogPostTitle(): MutableLiveData<String> {
        return blogPostTitle
    }

    fun getBlogPostAuthor(): MutableLiveData<String> {
        return blogPostAuthor
    }

    fun getBlogPostDate(): MutableLiveData<String> {
        return blogPostDate
    }

    fun getImageUrl() = imageUrl

    fun onBlogPostListItemClick() {
        blogPostListItemClick(blogPostTitle.value!!)
    }
}