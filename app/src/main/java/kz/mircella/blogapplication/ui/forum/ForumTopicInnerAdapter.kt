package kz.mircella.blogapplication.ui.forum

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.ramotion.garlandview.inner.InnerAdapter
import kz.mircella.blogapplication.R
import kz.mircella.blogapplication.databinding.ForumTopicInnerItemBinding
import kz.mircella.blogapplication.forum.model.ForumAnswer
import java.util.ArrayList

class ForumTopicInnerAdapter: InnerAdapter<ForumTopicInnerItem>(){
    private val forumAnswers = ArrayList<ForumAnswer>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForumTopicInnerItem {
        val binding = DataBindingUtil.inflate<ForumTopicInnerItemBinding>(LayoutInflater.from(parent.context), viewType, parent, false)
        return ForumTopicInnerItem(binding.root)
    }

    override fun onBindViewHolder(holder: ForumTopicInnerItem, position: Int) {
        holder.setContent(forumAnswers[position])
    }

    override fun onViewRecycled(holder: ForumTopicInnerItem) {
    }

    override fun getItemCount(): Int {
        return forumAnswers.size
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.forum_topic_inner_item
    }

    fun addData(innerDataList: List<ForumAnswer>) {
        val size = forumAnswers.size
        forumAnswers.addAll(innerDataList)
        notifyItemRangeInserted(size, innerDataList.size)
    }

    fun clearData() {
        forumAnswers.clear()
        notifyDataSetChanged()
    }
}