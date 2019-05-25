package kz.mircella.blogapplication.ui

import android.annotation.SuppressLint
import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import kz.mircella.blogapplication.R
import kz.mircella.blogapplication.databinding.ForumBinding
import kz.mircella.blogapplication.forum.ForumViewModel
import kz.mircella.blogapplication.navigation.ForumNavigation
import kz.mircella.blogapplication.utils.extensions.setTitle
import me.vponomarenko.injectionmanager.x.XInjectionManager
import timber.log.Timber

private const val FORUM_TITLE = "Forum"

@SuppressLint("ValidFragment")
open class ForumFragment : Fragment() {

    private lateinit var forumBinding: ForumBinding
    private lateinit var forumViewModel: ForumViewModel
    private lateinit var forumFragmentRv: RecyclerView
    private var errorSnackBar: Snackbar? = null
    private val navigation: ForumNavigation by lazy {
        XInjectionManager.findComponent<ForumNavigation>()
    }
    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        forumBinding = DataBindingUtil.inflate(inflater, R.layout.forum, container, false)
        forumFragmentRv = forumBinding.forumFragmentRv
        forumFragmentRv.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        forumFragmentRv.setHasFixedSize(true)
        forumViewModel = ViewModelProviders.of(this).get(ForumViewModel::class.java)
        forumViewModel.errorMessage.observe(this, Observer { errorMessage ->
            errorMessage?.let { showError(errorMessage) } ?: hideError()
        })
        forumViewModel.selectedForumItemTitle.observe(this, Observer { selectedForumItemTitle ->
            moveToSelectedForumItem(selectedForumItemTitle)
        })
        forumBinding.forumViewModel = forumViewModel
        return forumBinding.root
    }

    private fun moveToSelectedForumItem(selectedForumItemTitle: String) {
        Timber.d("$selectedForumItemTitle was clicked")
        navigation.openForumTopicFragment(selectedForumItemTitle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(FORUM_TITLE)
    }


    private fun showError(@StringRes errorMessage: Int) {
        errorSnackBar = Snackbar.make(forumBinding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackBar?.setAction(R.string.retry, forumViewModel.errorClickListener)
        errorSnackBar?.show()
    }

    private fun hideError() {
        errorSnackBar?.dismiss()
    }
}