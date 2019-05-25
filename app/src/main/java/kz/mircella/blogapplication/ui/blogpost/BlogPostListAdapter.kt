package kz.mircella.blogapplication.ui.blogpost

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kz.mircella.blogapplication.R
import kz.mircella.blogapplication.blogpost.BlogPostListItemViewModel
import kz.mircella.blogapplication.blogpost.model.BlogPostItem
import kz.mircella.blogapplication.databinding.BlogListItemBinding

class BlogPostListAdapter(private val blogPostListItemClick: (blogPostTitle: String) -> Unit) : RecyclerView.Adapter<BlogPostListAdapter.BlogPostItemViewHolder>() {
    private lateinit var blogPostList: List<BlogPostItem>

    override fun onCreateViewHolder(parent: ViewGroup, index: Int): BlogPostListAdapter.BlogPostItemViewHolder {
        val blogListItemBinding = DataBindingUtil.inflate<BlogListItemBinding>(LayoutInflater.from(parent.context), R.layout.blog_list_item, parent, false)
        return BlogPostItemViewHolder(blogListItemBinding, blogPostListItemClick)
    }

    override fun getItemCount(): Int {
        return when {
            this::blogPostList.isInitialized -> blogPostList.size
            else -> 0
        }
    }

    override fun onBindViewHolder(blogPostItemViewHolder: BlogPostListAdapter.BlogPostItemViewHolder, index: Int) {
        blogPostItemViewHolder.bind(blogPostList[index])
    }

    fun updateBlogPostList(blogPostItemList: List<BlogPostItem>) {
        this.blogPostList = blogPostItemList
        notifyDataSetChanged()
    }

    class BlogPostItemViewHolder(private val binding: BlogListItemBinding, blogPostListItemClick: (blogPostTitle: String) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        val blogPostItemViewModel = BlogPostListItemViewModel(blogPostListItemClick)

        fun bind(blogPostItem: BlogPostItem) {
            blogPostItemViewModel.bind(blogPostItem)
            binding.viewModel = blogPostItemViewModel
        }
    }
}