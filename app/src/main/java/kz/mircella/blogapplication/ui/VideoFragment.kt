package kz.mircella.blogapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kz.mircella.blogapplication.R
import kz.mircella.blogapplication.databinding.VideoBinding
import kz.mircella.blogapplication.navigation.VideoNavigation
import kz.mircella.blogapplication.utils.extensions.setTitle
import kz.mircella.blogapplication.video.VideoViewModel
import kz.mircella.blogapplication.video.dto.VideoDetails
import me.vponomarenko.injectionmanager.x.XInjectionManager
import timber.log.Timber

private const val VIDEO_FRAGMENT_TITLE = "Videos"
open class VideoFragment: Fragment() {
    private lateinit var videoBinding: VideoBinding
    private val videoViewModel: VideoViewModel by lazy {
        ViewModelProviders.of(this).get(VideoViewModel::class.java)
    }
    private val navigation: VideoNavigation by lazy {
        XInjectionManager.findComponent<VideoNavigation>()
    }
    private var errorSnackBar: Snackbar? = null

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        videoBinding = DataBindingUtil.inflate(inflater, R.layout.video, container, false)
        videoBinding.videoFragmentRv.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        videoViewModel.errorMessage.observe(this, Observer { errorMessage ->
            errorMessage?.let { showError(it) } ?: hideError()
        })

        videoViewModel.selectedVideo.observe(this, Observer { videoItemTitle ->
            openSelectedVideoItem(videoItemTitle)
        })
        videoBinding.viewModel = videoViewModel
        return videoBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(VIDEO_FRAGMENT_TITLE)
    }

    private fun openSelectedVideoItem(videoItemTitle: String) {
        Timber.d("${videoItemTitle} was clicked")
        navigation.openVideoItemFragment(videoItemTitle)

    }

    private fun showError(@StringRes errorMessage: Int) {
        errorSnackBar = Snackbar.make(videoBinding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackBar?.setAction(R.string.retry, videoViewModel.errorClickListener)
        errorSnackBar?.show()
    }

    private fun hideError() {
        errorSnackBar?.dismiss()
    }

}