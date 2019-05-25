package kz.mircella.blogapplication.ui

import android.annotation.SuppressLint
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
import kz.mircella.blogapplication.blogpost.BlogPostListViewModel
import kz.mircella.blogapplication.databinding.HomeBinding
import kz.mircella.blogapplication.navigation.BlogPostNavigation
import kz.mircella.blogapplication.utils.extensions.setTitle
import me.vponomarenko.injectionmanager.x.XInjectionManager
import timber.log.Timber

private const val HOME_TITLE = "Home Page"

@SuppressLint("ValidFragment")
open class HomeFragment : Fragment() {

    private lateinit var homeBinding: HomeBinding
    private val blogPostListViewModel: BlogPostListViewModel by lazy {
        ViewModelProviders.of(this).get(BlogPostListViewModel::class.java)
    }
    private val navigation: BlogPostNavigation by lazy {
        XInjectionManager.findComponent<BlogPostNavigation>()
    }
    private var errorSnackBar: Snackbar? = null

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homeBinding = DataBindingUtil.inflate(inflater, R.layout.home, container, false)
        homeBinding.homeFragmentRv.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        blogPostListViewModel.errorMessage.observe(this, Observer { errorMessage ->
            errorMessage?.let { showError(it) } ?: hideError()
        })

        blogPostListViewModel.selectedBlogPostTitle.observe(this, Observer { selectedBlogPostTitle ->
            moveToSelectedBlogPost(selectedBlogPostTitle)
        })
        homeBinding.blogPostListViewModel = blogPostListViewModel
        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(HOME_TITLE)
    }

    fun moveToSelectedBlogPost(blogPostTitle: String) {
        Timber.d("$blogPostTitle was clicked")
        navigation.openBlogPostFragment(blogPostTitle)

    }

    private fun showError(@StringRes errorMessage: Int) {
        errorSnackBar = Snackbar.make(homeBinding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackBar?.setAction(R.string.retry, blogPostListViewModel.errorClickListener)
        errorSnackBar?.show()
    }

    private fun hideError() {
        errorSnackBar?.dismiss()
    }
}