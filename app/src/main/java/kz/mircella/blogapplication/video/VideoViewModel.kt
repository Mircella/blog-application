package kz.mircella.blogapplication.video

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kz.mircella.blogapplication.R
import kz.mircella.blogapplication.base.BaseViewModel
import kz.mircella.blogapplication.network.VideoApiService
import kz.mircella.blogapplication.ui.video.VideoListAdapter
import kz.mircella.blogapplication.utils.SingleLiveEvent
import kz.mircella.blogapplication.video.dto.VideoDetails
import kz.mircella.blogapplication.video.dto.VideoItemDto
import kz.mircella.blogapplication.video.model.VideoItem
import okhttp3.ResponseBody
import org.apache.commons.io.IOUtils
import timber.log.Timber
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import javax.inject.Inject

class VideoViewModel : BaseViewModel() {
    @Inject
    lateinit var videoApiService: VideoApiService

    private lateinit var allVideosDisposable: Disposable
    private val title: MutableLiveData<String> = MutableLiveData("Watch videos")
    private val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val loadingVideoList: MutableLiveData<String> = MutableLiveData()
    val errorMessage: SingleLiveEvent<Int> = SingleLiveEvent()
    val selectedVideo: SingleLiveEvent<String> = SingleLiveEvent()
    val errorClickListener = View.OnClickListener { loadVideoList() }
    private val videoListAdapter = VideoListAdapter(videoItemClick())

    init {
        loadVideoList()
    }

    override fun onCleared() {
        super.onCleared()
        allVideosDisposable.dispose()
    }

    private fun loadVideoList() {
        val allVideosDisposableName = "AllVideosDisposable"
        allVideosDisposable = videoApiService.getAllVideos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onRetrieveVideoListStart() }
                .doOnTerminate { onRetrieveVideoListFinish() }
                .subscribe(
                        { onRetrieveVideoListSuccess(it) },
                        { onRetrieveVideoListError(it) },
                        { onComplete(allVideosDisposableName) },
                        { onDispose(allVideosDisposableName, it) }
                )
    }

    private fun convertToVideoItems(videoItemDtos: List<VideoItemDto>): List<VideoItem> {
        return videoItemDtos.map { videoItemDto ->
            VideoItem(videoItemDto.videoTitle, videoItemDto.createdAt)
        }
    }

    private fun onRetrieveVideoListError(error: Throwable) {
        Timber.d("RetrieveBlogPostListError: ${error.localizedMessage}")
        errorMessage.value = R.string.data_loading_error
    }

    private fun onRetrieveVideoListSuccess(body: ResponseBody) {
        Timber.d("RetrieveBlogPostListSuccess")
        loadingVideoList.value = "Retrieved BlogPostList"
        val result = convertToBlogPostDtoList(body.byteStream())
        videoListAdapter.updateVideoList(convertToVideoItems(result))
    }

    private fun onComplete(subscriptionName: String) {
        Timber.d("Completed $subscriptionName")
    }

    private fun onDispose(subscriptionName: String, disposable: Disposable) {
        Timber.d("OnDispose $subscriptionName")
    }

    private fun onRetrieveVideoListFinish() {
        loadingVisibility.value = View.GONE
    }

    private fun onRetrieveVideoListStart() {
        loadingVisibility.value = View.VISIBLE
        errorMessage.value = null
    }

    @Throws(IOException::class)
    fun convertToBlogPostDtoList(inputStream: InputStream, charset: Charset = StandardCharsets.UTF_8): List<VideoItemDto> {
        val videoResponseString = IOUtils.toString(inputStream, charset).trim()
        val videoItemDtoStrings = videoResponseString.split("\n")
        val gsonConverter = Gson()
        return videoItemDtoStrings.map { gsonConverter.fromJson(it, VideoItemDto::class.java) }
    }


    public fun getLoadingVisibility(): MutableLiveData<Int> {
        return loadingVisibility
    }

    public fun getTitle(): MutableLiveData<String> {
        return title
    }

    public fun getVideoListAdapter(): VideoListAdapter {
        return videoListAdapter
    }

    private fun videoItemClick(): (videoTitle: String) -> Unit = {
        selectedVideo.value = it
    }

}