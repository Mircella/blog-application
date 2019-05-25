package kz.mircella.blogapplication.blogpost

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kz.mircella.blogapplication.R
import kz.mircella.blogapplication.base.BaseViewModel
import kz.mircella.blogapplication.blogpost.dto.BlogPostDto
import kz.mircella.blogapplication.blogpost.model.BlogPostItem
import kz.mircella.blogapplication.network.BlogPostApiService
import kz.mircella.blogapplication.ui.blogpost.BlogPostListAdapter
import kz.mircella.blogapplication.utils.BASE_URL
import kz.mircella.blogapplication.utils.SingleLiveEvent
import okhttp3.ResponseBody
import org.apache.commons.io.IOUtils
import timber.log.Timber
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import javax.inject.Inject

open class BlogPostListViewModel() : BaseViewModel() {

    @Inject
    lateinit var blogPostApiService: BlogPostApiService

    private lateinit var allBlogsDisposable: Disposable

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val loadingBlogPostList: MutableLiveData<String> = MutableLiveData()
    val errorMessage: SingleLiveEvent<Int> = SingleLiveEvent()
    val selectedBlogPostTitle: SingleLiveEvent<String> = SingleLiveEvent()
    val errorClickListener = View.OnClickListener { loadPosts() }
    private val blogPostListAdapter = BlogPostListAdapter(blogPostListItemClick())

    init {
        loadPosts()
    }

    override fun onCleared() {
        super.onCleared()
        allBlogsDisposable.dispose()
    }

    private fun loadPosts() {
        val allBlogsDisposableName = "AllBlogsDisposable"
        allBlogsDisposable = blogPostApiService.getAllBlogPosts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onRetrieveBlogPostListStart() }
                .doOnTerminate { onRetrieveBlogPostListFinish() }
                .subscribe(
                        { onRetrieveBlogPostListSuccess(it) },
                        { onRetrieveBlogPostListError(it) },
                        { onComplete(allBlogsDisposableName) },
                        { onDispose(allBlogsDisposableName, it) }
                )
    }

    private fun convertToBlogPostItems(blogPostList: List<BlogPostDto>): List<BlogPostItem> {
        return blogPostList.map { blogPostDto ->
            val imageUrl = blogPostDto.imageIds.firstOrNull()
            val blogPostImage = imageUrl?.let { "${BASE_URL}blog-posts/$it/image" }
            BlogPostItem(blogPostDto.title, blogPostDto.createdAt, blogPostDto.authorId, blogPostImage)
        }
    }

    private fun onRetrieveBlogPostListError(error: Throwable) {
        Timber.d("RetrieveBlogPostListError: ${error.localizedMessage}")
        errorMessage.value = R.string.data_loading_error
    }

    private fun onRetrieveBlogPostListSuccess(body: ResponseBody) {
        Timber.d("RetrieveBlogPostListSuccess")
        loadingBlogPostList.value = "Retrieved BlogPostList"
        val result = convertToBlogPostDtoList(body.byteStream())
        blogPostListAdapter.updateBlogPostList(convertToBlogPostItems(result))
    }

    private fun onRetrieveBlogPostListFinish() {
        loadingVisibility.value = View.GONE
    }

    private fun onRetrieveBlogPostListStart() {
        loadingVisibility.value = View.VISIBLE
        errorMessage.value = null
    }

    @Throws(IOException::class)
    fun convertToBlogPostDtoList(inputStream: InputStream, charset: Charset = StandardCharsets.UTF_8): List<BlogPostDto> {
        val blogPostResponseString = IOUtils.toString(inputStream, charset).trim()
        val blogPostDtoStrings = blogPostResponseString.split("\n")
        val gsonConverter = Gson()
        return blogPostDtoStrings.map { gsonConverter.fromJson(it, BlogPostDto::class.java) }
    }

    fun getBlogPostListAdapter(): BlogPostListAdapter {
        return blogPostListAdapter
    }

    private fun onComplete(subscriptionName: String) {
        Timber.d("Completed $subscriptionName")
    }

    private fun onDispose(subscriptionName: String, disposable: Disposable) {
        Timber.d("OnDispose $subscriptionName")
    }

    private fun onError(subscriptionName: String, error: Throwable) {
        Timber.d("OnError $subscriptionName, ${error.localizedMessage}")
    }

    private fun blogPostListItemClick(): (blogPostTitle: String) -> Unit = { selectedBlogPostTitle.value = it }

}