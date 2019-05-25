package kz.mircella.blogapplication.forum

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kz.mircella.blogapplication.R
import kz.mircella.blogapplication.base.BaseViewModel
import kz.mircella.blogapplication.forum.dto.ForumSectionDto
import kz.mircella.blogapplication.forum.dto.ForumTopicDetails
import kz.mircella.blogapplication.forum.model.ForumItem
import kz.mircella.blogapplication.forum.model.ForumSection
import kz.mircella.blogapplication.network.ForumApiService
import kz.mircella.blogapplication.ui.forum.ForumSectionsListAdapter
import kz.mircella.blogapplication.utils.SingleLiveEvent
import okhttp3.ResponseBody
import org.apache.commons.io.IOUtils
import timber.log.Timber
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import javax.inject.Inject

open class ForumViewModel : BaseViewModel() {
    @Inject
    lateinit var forumApiService: ForumApiService

    val selectedForumItemTitle: SingleLiveEvent<String> = SingleLiveEvent()
    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadForumSections() }

    private val loadingForumSectionsList: MutableLiveData<String> = MutableLiveData()
    private val forumListAdapter = ForumSectionsListAdapter(forumItemClick())

    private lateinit var allForumSectionsDisposable: Disposable

    init {
       loadForumSections()
    }

    override fun onCleared() {
        super.onCleared()
        allForumSectionsDisposable.dispose()
    }


    fun getForumListAdapter(): SectionedRecyclerViewAdapter {
        return forumListAdapter
    }

    fun loadForumSections() {
        val allForumsDisposableName = "AllBlogsDisposable"
        allForumSectionsDisposable = forumApiService.getAllSections().
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onRetrieveForumSectionsStart() }
                .doOnTerminate { onRetrieveForumSectionsFinish() }
                .subscribe(
                        { onRetrieveForumSectionsSuccess(it) },
                        { onRetrieveForumSectionsError(it) },
                        { onCompleteFetchingForumSections(allForumsDisposableName) },
                        { onDisposeFetchingForumSections(allForumsDisposableName, it) }
                )
    }

    private fun onDisposeFetchingForumSections(subscriptionName: String, disposable: Disposable) {
        Timber.d("OnDispose $subscriptionName ${disposable.isDisposed}")
    }

    private fun onCompleteFetchingForumSections(subscriptionName: String) {
        Timber.d("Completed $subscriptionName")
    }

    private fun onRetrieveForumSectionsError(error: Throwable) {
        Timber.d("RetrieveForumSectionListError: ${error.localizedMessage}")
        errorMessage.value = R.string.data_loading_error
    }

    private fun onRetrieveForumSectionsSuccess(body: ResponseBody) {
        Timber.d("RetrieveForumSectionListSuccess")
        loadingForumSectionsList.value = "Retrieved ForumSectionList"
        val result = convertToForumSectionDtoList(body.byteStream())
        forumListAdapter.updateForumSections(convertToForumItemViewModel(result))
    }

    @Throws(IOException::class)
    fun convertToForumSectionDtoList(inputStream: InputStream, charset: Charset = StandardCharsets.UTF_8): List<ForumSectionDto> {
        val blogPostResponseString = IOUtils.toString(inputStream, charset).trim()
        val blogPostDtoStrings = blogPostResponseString.split("\n")
        val gsonConverter = Gson()
        return blogPostDtoStrings.map { gsonConverter.fromJson(it, ForumSectionDto::class.java) }
    }

    private fun convertToForumItemViewModel(forumSectionDtos: List<ForumSectionDto>): List<ForumSection> {
        return forumSectionDtos.map { ForumSection(it.title,  convertToForumItem(it.forumTopics)) }
    }

    private fun convertToForumItem(forumTopics: List<ForumTopicDetails>): List<ForumItem> {
        return forumTopics.map { ForumItem(it.title, it.lastTopicPostAuthorId, it.lastTopicPostDateTime) }
    }

    private fun onRetrieveForumSectionsFinish() {
        loadingVisibility.value = View.GONE
    }

    private fun onRetrieveForumSectionsStart() {
        loadingVisibility.value = View.VISIBLE
        errorMessage.value = null
    }

    private fun forumItemClick(): (forumItemTitle: String) -> Unit = { selectedForumItemTitle.value = it }
}