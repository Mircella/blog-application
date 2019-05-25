package kz.mircella.blogapplication.ui.video

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
import com.google.android.material.snackbar.Snackbar
import kz.mircella.blogapplication.R
import kz.mircella.blogapplication.databinding.VideoItemFragmentBinding
import kz.mircella.blogapplication.injection.VideoViewModelProviderFactory
import kz.mircella.blogapplication.utils.extensions.setTitle
import kz.mircella.blogapplication.video.VideoItemViewModel
import kz.mircella.blogapplication.video.dto.VideoDetails

class VideoItemFragment: Fragment() {
    companion object {
        private const val VIDEO_ITEM_TITLE = "kz.mircella.blogapplication.video.video_item_title"

        fun createBundle(videoItemTitle: String) =
                Bundle().apply {
                    putString(VIDEO_ITEM_TITLE, videoItemTitle)
                }
    }

    private lateinit var videoBinding: VideoItemFragmentBinding
    private lateinit var videoItemViewModel: VideoItemViewModel
    private var errorSnackBar: Snackbar? = null

    private val videoItemTitle: String by lazy {
        arguments?.getString(VIDEO_ITEM_TITLE) ?: throw IllegalArgumentException("There is no VideoItemTitle")
    }
    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        videoBinding = DataBindingUtil.inflate(inflater, R.layout.video_item_fragment, container, false)
        videoItemViewModel = ViewModelProviders.of(this, VideoViewModelProviderFactory(videoItemTitle)).get(VideoItemViewModel::class.java)
        videoItemViewModel.errorMessage.observe(this, Observer { errorMessage ->
            errorMessage?.let { showError(it) } ?: hideError()
        })
        videoBinding.viewModel = videoItemViewModel
        return videoBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(videoItemTitle)
    }

    private fun showError(@StringRes errorMessage: Int) {
        errorSnackBar = Snackbar.make(videoBinding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackBar?.setAction(R.string.retry, videoItemViewModel.errorClickListener)
        errorSnackBar?.show()
    }

    private fun hideError() {
        errorSnackBar?.dismiss()
    }
}