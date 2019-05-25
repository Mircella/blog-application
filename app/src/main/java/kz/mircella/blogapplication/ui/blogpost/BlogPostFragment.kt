package kz.mircella.blogapplication.ui.blogpost

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
import kotlinx.android.synthetic.main.blog_post.blog_post_pager_indicator
import kotlinx.android.synthetic.main.blog_post.blog_post_rtl_view_pager
import kz.mircella.blogapplication.R
import kz.mircella.blogapplication.blogpost.BlogPostViewModel
import kz.mircella.blogapplication.databinding.BlogPostBinding
import kz.mircella.blogapplication.injection.BlogPostViewModelProviderFactory
import kz.mircella.blogapplication.utils.extensions.setTitle

@SuppressLint("ValidFragment")
class BlogPostFragment: Fragment() {

    companion object {
        private const val BLOG_POST_TITLE = "kz.mircella.blogapplication.blogpost.blog_post_title"

        fun createBundle(blogPostTitle: String) =
                Bundle().apply {
                    putString(BLOG_POST_TITLE, blogPostTitle)
                }
    }
    private lateinit var blogPostViewModel: BlogPostViewModel
    private lateinit var blogPostBinding: BlogPostBinding

    private var errorSnackBar: Snackbar? = null

    private val blogPostTitle: String by lazy {
        arguments?.getString(BLOG_POST_TITLE) ?: throw IllegalStateException("No BlogPostTitle")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        blogPostBinding = DataBindingUtil.inflate(inflater, R.layout.blog_post, container, false)
        blogPostViewModel = ViewModelProviders.of(this, BlogPostViewModelProviderFactory(blogPostTitle, setViewPagerCurrentItem())).get(BlogPostViewModel::class.java)
        blogPostViewModel.errorMessage.observe(this, Observer { errorMessage ->
            errorMessage?.let { showError(it) } ?: hideError()
        })
        blogPostBinding.viewModel = blogPostViewModel
        return blogPostBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(blogPostTitle)
        blog_post_pager_indicator.attachToViewPager(blog_post_rtl_view_pager)
    }

    private fun showError(@StringRes errorMessage: Int) {
        errorSnackBar = Snackbar.make(blogPostBinding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackBar?.setAction(R.string.retry, blogPostViewModel.errorClickListener)
        errorSnackBar?.show()
    }

    private fun hideError() {
        errorSnackBar?.dismiss()
    }

    private fun setViewPagerCurrentItem(): (next: Boolean) -> Unit = {
        if (it) {
            if (blog_post_rtl_view_pager.currentItem == blog_post_rtl_view_pager.adapter?.count?.let { it - 1 }) {
                blog_post_rtl_view_pager.currentItem = 0
            } else {
                blog_post_rtl_view_pager.currentItem = blog_post_rtl_view_pager.currentItem + 1
            }
        } else {
            if (blog_post_rtl_view_pager.currentItem == 0) {
                blog_post_rtl_view_pager.currentItem = blog_post_rtl_view_pager.adapter?.count?.let { it - 1 }?: 0
            } else {
                blog_post_rtl_view_pager.currentItem = blog_post_rtl_view_pager.currentItem - 1
            }
        }
    }
}