package kz.mircella.blogapplication.ui.video

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kz.mircella.blogapplication.R
import kz.mircella.blogapplication.databinding.VideoListItemBinding
import kz.mircella.blogapplication.video.VideoListItemViewModel
import kz.mircella.blogapplication.video.model.VideoItem

class VideoListAdapter(private val videoItemClick: (videoTitle: String) -> Unit) : RecyclerView.Adapter<VideoListAdapter.VideoItemViewHolder>() {
    private lateinit var videoItems: List<VideoItem>

    override fun onCreateViewHolder(parent: ViewGroup, index: Int): VideoListAdapter.VideoItemViewHolder {
        val binding = DataBindingUtil.inflate<VideoListItemBinding>(LayoutInflater.from(parent.context), R.layout.video_list_item, parent, false)
        return VideoItemViewHolder(binding, videoItemClick)
    }

    override fun getItemCount(): Int {
        return when {
            this::videoItems.isInitialized -> videoItems.size
            else -> 0
        }
    }

    override fun onBindViewHolder(videoItemViewHolder: VideoListAdapter.VideoItemViewHolder, index: Int) {
        videoItemViewHolder.bind(videoItems[index])
    }

    fun updateVideoList(videoItems: List<VideoItem>) {
        this.videoItems = videoItems
        notifyDataSetChanged()
    }

    class VideoItemViewHolder(private val binding: VideoListItemBinding, videoItemClick: (videoTitle: String) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        val videoListItemViewModel = VideoListItemViewModel(videoItemClick)

        fun bind(videoItem: VideoItem) {
            videoListItemViewModel.bind(videoItem)
            binding.viewModel = videoListItemViewModel
        }
    }
}