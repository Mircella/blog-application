package kz.mircella.blogapplication.video

import android.view.View
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kz.mircella.blogapplication.R
import kz.mircella.blogapplication.base.BaseViewModel
import kz.mircella.blogapplication.network.VideoApiService
import kz.mircella.blogapplication.video.dto.VideoDetails
import timber.log.Timber
import javax.inject.Inject

class VideoItemViewModel(private val videoItemTitle: String): BaseViewModel() {

    @Inject
    lateinit var videoApiService: VideoApiService

    private lateinit var videoDetailsDisposable: Disposable
    private val title: MutableLiveData<String> = MutableLiveData(videoItemTitle)
    private val authorId: MutableLiveData<String> = MutableLiveData()
    private val videoDate: MutableLiveData<String> = MutableLiveData()

    private var videoUrl: MutableLiveData<String> = MutableLiveData()
    private var videoImageUrl: MutableLiveData<String> = MutableLiveData()
    private val description: MutableLiveData<String> = MutableLiveData()
    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadVideoDetails(title.value!!) }

    init {
        loadVideoDetails(title.value!!)
    }

    override fun onCleared() {
        super.onCleared()
        videoDetailsDisposable.dispose()
    }

    private fun loadVideoDetails(videoItemTitle: String) {
        val videoDetailsDisposableName = "Disposable of $videoItemTitle"
        videoDetailsDisposable = videoApiService.getVideoByTitle(videoItemTitle)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onRetrieveVideoDetailsStart() }
                .doOnError { onError(videoDetailsDisposableName, it) }
                .subscribe(
                        { onRetrieveVideoDetailsSuccess(it) },
                        { onRetrieveVideoDetailsError(it) }
                )
    }

    fun getTitle(): MutableLiveData<String> {
        return title
    }

    fun getAuthorId(): MutableLiveData<String> {
        return authorId
    }

    fun getDate(): MutableLiveData<String> {
        return videoDate
    }

    fun getDescription(): MutableLiveData<String> {
        return description
    }

    fun getVideoUrl(): MutableLiveData<String> {
        return videoUrl
    }

    fun getVideoImageUrl(): MutableLiveData<String> {
        return videoImageUrl
    }

    private fun onRetrieveVideoDetailsError(error: Throwable) {
        Timber.d("RetrieveVideoDetailsError: ${error.localizedMessage}")
        errorMessage.value = R.string.data_loading_error
    }

    private fun onRetrieveVideoDetailsSuccess(videoDetails: VideoDetails) {
        authorId.value = videoDetails.authorId
        videoDate.value = videoDetails.createdAt
        description.value = videoDetails.description
        videoUrl.value = videoDetails.videoUrl
        videoImageUrl.value = videoDetails.videoImageUrl
    }

    private fun onRetrieveVideoDetailsStart() {
        errorMessage.value = null
    }

    private fun onError(subscriptionName: String, error: Throwable) {
        Timber.d("OnError $subscriptionName, ${error.localizedMessage}")
    }

}