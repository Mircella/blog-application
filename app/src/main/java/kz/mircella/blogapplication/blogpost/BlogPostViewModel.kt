package kz.mircella.blogapplication.blogpost

import android.view.View
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kz.mircella.blogapplication.R
import kz.mircella.blogapplication.base.BaseViewModel
import kz.mircella.blogapplication.blogpost.dto.BlogPostDetails
import kz.mircella.blogapplication.network.BlogPostApiService
import kz.mircella.blogapplication.ui.blogpost.BlogPostViewPagerAdapter
import timber.log.Timber
import javax.inject.Inject

class BlogPostViewModel(blogPostTitle: String, private val navigationButtonClick: (next: Boolean) -> Unit): BaseViewModel() {
    @Inject
    lateinit var blogPostApiService: BlogPostApiService

    private val disposables = CompositeDisposable()
    private var blogPostDisposable: Disposable? = null

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val loadingBlogPostItem: MutableLiveData<String> = MutableLiveData()
    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadBlogPost(title.value) }

    private val title = MutableLiveData<String>(blogPostTitle)
    private val content = MutableLiveData<String>()
    private val author = MutableLiveData<String>()
    private val createdAt = MutableLiveData<String>()
    private val viewPagerAdapter = BlogPostViewPagerAdapter()

    init {
        loadBlogPost(title.value)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    private fun loadBlogPost(blogPostTitle: String?) {
        blogPostDisposable = blogPostTitle?.let {
            blogPostApiService.getBlogPost(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { onRetrieveBlogPostItemStart() }
                    .subscribe (
                            { onRetrieveBlogPostListSuccess(it) },
                            { onRetrieveBlogPostListError(it) }
                    ).addTo(disposables)
        }
    }

    private fun onRetrieveBlogPostItemFinish() {
        loadingVisibility.value = View.GONE
    }

    private fun onRetrieveBlogPostItemStart() {
        loadingVisibility.value = View.VISIBLE
        errorMessage.value = null
    }

    private fun onRetrieveBlogPostListError(error: Throwable) {
        Timber.d("RetrieveBlogPostItemError: ${error.localizedMessage}")
        errorMessage.value = R.string.data_loading_error
    }

    private fun onRetrieveBlogPostListSuccess(blogPostDetails: BlogPostDetails) {
        Timber.d("RetrieveBlogPostItemSuccess $blogPostDetails")
        val images = blogPostDetails.imageIds
        viewPagerAdapter.updateBlogPostImages(images)
        content.value = blogPostDetails.content
        loadingBlogPostItem.value = "Retrieved BlogPostItem"
    }

    private fun onComplete(subscriptionName: String) {
        Timber.d("Completed $subscriptionName")
    }

    private fun onDispose(subscriptionName: String, disposable: Disposable) {
        Timber.d("OnDispose $subscriptionName")
    }

    fun getTitle(): MutableLiveData<String> {
        return title
    }

    fun getContent(): MutableLiveData<String> {
        return content
    }

    fun getAuthor(): MutableLiveData<String> {
        return author
    }

    fun getDate(): MutableLiveData<String> {
        return createdAt
    }

    fun getViewPagerAdapter() = viewPagerAdapter


    fun onNextClick() {
        navigationButtonClick(true)
    }


    fun onPreviousClick() {
        navigationButtonClick(false)
    }
}