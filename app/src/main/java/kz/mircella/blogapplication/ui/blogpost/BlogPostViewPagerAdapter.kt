package kz.mircella.blogapplication.ui.blogpost

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import kz.mircella.blogapplication.R
import kz.mircella.blogapplication.blogpost.BlogPostImageViewModel
import kz.mircella.blogapplication.databinding.BlogPostImageCardBinding
import kz.mircella.blogapplication.utils.BASE_URL

class BlogPostViewPagerAdapter: PagerAdapter() {
    private lateinit var blogPostImageUrls: List<String>
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return when {
            this::blogPostImageUrls.isInitialized -> blogPostImageUrls.size
            else -> 0
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val blogPostImageCardBinding = DataBindingUtil.inflate<BlogPostImageCardBinding>(LayoutInflater.from(container.context), R.layout.blog_post_image_card, container, false)
        val view = blogPostImageCardBinding.root
        blogPostImageCardBinding.viewModel = BlogPostImageViewModel(createImageUrl(position))
        container.addView(view)
        return view
    }

    fun updateBlogPostImages(blogPostImageUrls: List<String>) {
        this.blogPostImageUrls = blogPostImageUrls
        notifyDataSetChanged()
    }

    private fun createImageUrl(position: Int): String {
        return "${BASE_URL}blog-posts/${blogPostImageUrls[position]}/image"
    }
}