package kz.mircella.blogapplication.ui.forum

import android.view.View
import android.view.ViewGroup
import com.ramotion.garlandview.inner.InnerItem
import kotlinx.android.synthetic.main.forum_topic_inner_item.view.tv_author
import kotlinx.android.synthetic.main.forum_topic_inner_item.view.tv_comment
import kotlinx.android.synthetic.main.forum_topic_inner_item.view.tv_date
import kz.mircella.blogapplication.forum.model.ForumAnswer
import org.greenrobot.eventbus.EventBus

class ForumTopicInnerItem(itemView: View) : InnerItem(itemView) {
    private val mInnerLayout: View = (itemView as ViewGroup).getChildAt(0)

    init {
        mInnerLayout.setOnClickListener { EventBus.getDefault().post(this) }
    }

    override fun getInnerLayout(): View {
        return mInnerLayout
    }

    internal fun setContent(forumAnswer: ForumAnswer) {

        itemView.tv_author.text = forumAnswer.authorId
        itemView.tv_date.text = "Posted at: ${forumAnswer.postedAt}"
        itemView.tv_comment.text = forumAnswer.comment
    }
}