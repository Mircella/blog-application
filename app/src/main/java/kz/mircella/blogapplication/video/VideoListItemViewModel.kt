package kz.mircella.blogapplication.video

import androidx.lifecycle.MutableLiveData
import kz.mircella.blogapplication.video.model.VideoItem

class VideoListItemViewModel(private val videoListItemClick: (videoTitle: String) -> Unit) {

    private val videoListItemTitle = MutableLiveData<String>()
    private val videoListItemCreationDate = MutableLiveData<String>()

    fun bind(videoItem: VideoItem) {
        videoListItemTitle.value = videoItem.title
        videoListItemCreationDate.value = videoItem.createdAt
    }

    fun getVideoListItemTitle(): MutableLiveData<String> {
        return videoListItemTitle
    }

    fun getVideoListItemCreationDate(): MutableLiveData<String> {
        return videoListItemCreationDate
    }

    fun onVideoListItemClick() {
        videoListItemClick(videoListItemTitle.value!!)
    }
}