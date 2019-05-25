package kz.mircella.blogapplication.ui.forum

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.ramotion.garlandview.TailLayoutManager
import com.ramotion.garlandview.TailRecyclerView
import com.ramotion.garlandview.TailSnapHelper
import com.ramotion.garlandview.header.HeaderTransformer
import kz.mircella.blogapplication.R
import kz.mircella.blogapplication.databinding.ForumTopicFragmentBinding
import kz.mircella.blogapplication.forum.ForumTopicViewModel
import kz.mircella.blogapplication.injection.ForumViewModelProviderFactory
import kz.mircella.blogapplication.utils.extensions.setTitle

@SuppressLint("ValidFragment")
class ForumTopicFragment: Fragment() {
    companion object {
        private const val FORUM_TOPIC_TITLE = "kz.mircella.blogapplication.forum.forum_topic_title"

        fun createBundle(forumTopicTitle: String) =
                Bundle().apply {
                    putString(FORUM_TOPIC_TITLE, forumTopicTitle)
                }
    }
    private lateinit var forumTopicViewModel: ForumTopicViewModel
    private lateinit var forumListItemBinding: ForumTopicFragmentBinding

    private var errorSnackBar: Snackbar? = null

    private val forumTopicTitle: String by lazy {
        arguments?.getString(FORUM_TOPIC_TITLE) ?: throw IllegalStateException("No ForumTopicTitle")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        forumListItemBinding = DataBindingUtil.inflate(inflater, R.layout.forum_topic_fragment, container, false)
        forumTopicViewModel = ViewModelProviders.of(this, ForumViewModelProviderFactory(forumTopicTitle)).get(ForumTopicViewModel::class.java)
        forumTopicViewModel.errorMessage.observe(this, Observer { errorMessage ->
            errorMessage?.let { showError(it) } ?: hideError()
        })
        forumListItemBinding.viewModel = forumTopicViewModel
        val a = forumListItemBinding.root
        forumTopicViewModel.setLoadingUnvisible()

        return a
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(forumTopicTitle)
    }

    private fun showError(@StringRes errorMessage: Int) {
        errorSnackBar = Snackbar.make(forumListItemBinding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackBar?.setAction(R.string.retry, forumTopicViewModel.errorClickListener)
        errorSnackBar?.show()
    }

    private fun hideError() {
        errorSnackBar?.dismiss()
    }

}