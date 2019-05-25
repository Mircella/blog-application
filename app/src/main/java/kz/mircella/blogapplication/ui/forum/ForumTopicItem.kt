package kz.mircella.blogapplication.ui.forum

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ramotion.garlandview.header.HeaderDecorator
import com.ramotion.garlandview.header.HeaderItem
import com.ramotion.garlandview.inner.InnerLayoutManager
import com.ramotion.garlandview.inner.InnerRecyclerView
import kotlinx.android.synthetic.main.forum_topic_outer_header.view.answer
import kotlinx.android.synthetic.main.forum_topic_outer_header.view.avatar
import kotlinx.android.synthetic.main.forum_topic_outer_header.view.header_footer
import kotlinx.android.synthetic.main.forum_topic_outer_header.view.header_main_title
import kotlinx.android.synthetic.main.forum_topic_outer_header.view.header_middle
import kotlinx.android.synthetic.main.forum_topic_outer_header.view.header_middle_answer
import kotlinx.android.synthetic.main.forum_topic_outer_header.view.header_spannable_title
import kotlinx.android.synthetic.main.forum_topic_outer_header.view.tv_author
import kotlinx.android.synthetic.main.forum_topic_outer_header.view.tv_date
import kotlinx.android.synthetic.main.forum_topic_outer_item.view.header
import kotlinx.android.synthetic.main.forum_topic_outer_item.view.header_alpha
import kotlinx.android.synthetic.main.forum_topic_outer_item.view.recycler_view
import kz.mircella.blogapplication.R
import kz.mircella.blogapplication.forum.model.ForumAnswer
import kz.mircella.blogapplication.forum.model.ForumQuestion
import java.util.ArrayList

class ForumTopicItem(itemView: View, pool: RecyclerView.RecycledViewPool, val onAnswerClick: () -> Unit) : HeaderItem(itemView) {
    private val AVATAR_RATIO_START = 1f
    private val AVATAR_RATIO_MAX = 0.25f
    private val AVATAR_RATIO_DIFF = AVATAR_RATIO_START - AVATAR_RATIO_MAX

    private val ANSWER_RATIO_START = 0.75f
    private val ANSWER_RATIO_MAX = 0.35f
    private val ANSWER_RATIO_DIFF = ANSWER_RATIO_START - ANSWER_RATIO_MAX

    private val MIDDLE_RATIO_START = 0.7f
    private val MIDDLE_RATIO_MAX = 0.1f
    private val MIDDLE_RATIO_DIFF = MIDDLE_RATIO_START - MIDDLE_RATIO_MAX

    private val FOOTER_RATIO_START = 1.1f
    private val FOOTER_RATIO_MAX = 0.35f
    private val FOOTER_RATIO_DIFF = FOOTER_RATIO_START - FOOTER_RATIO_MAX

    private var m10dp: Int = itemView.context.resources.getDimensionPixelSize(R.dimen.dp10)
    private var m120dp: Int = itemView.context.resources.getDimensionPixelSize(R.dimen.dp120)
    private var mTitleSize1 = itemView.context.resources.getDimensionPixelSize(R.dimen.header_title2_text_size)
    private var mTitleSize2 = itemView.context.resources.getDimensionPixelSize(R.dimen.header_title2_name_text_size)

    private val mMiddleCollapsible = ArrayList<View>(2)

    private var mIsScrolling: Boolean = false

    init {

        mMiddleCollapsible.add(itemView.avatar.parent as View)
        mMiddleCollapsible.add(itemView.tv_author.parent as View)

        itemView.recycler_view.setRecycledViewPool(pool)
        itemView.recycler_view.adapter = ForumTopicInnerAdapter()

        itemView.recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                mIsScrolling = newState != RecyclerView.SCROLL_STATE_IDLE
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                onItemScrolled(recyclerView, dx, dy)
            }
        })

        itemView.recycler_view.addItemDecoration(HeaderDecorator(
                itemView.context.resources.getDimensionPixelSize(R.dimen.inner_item_height),
                itemView.context.resources.getDimensionPixelSize(R.dimen.inner_item_offset)))
        itemView.answer.setOnClickListener { _ ->
            onAnswerClick()
        }
    }

    override fun isScrolling(): Boolean {
        return mIsScrolling
    }

    override fun getViewGroup(): InnerRecyclerView {
        return itemView.recycler_view
    }

    override fun getHeader(): View {
        return itemView.header
    }

    override fun getHeaderAlphaView(): View {
        return itemView.header_alpha
    }

    internal fun setContent(forumQuestion: ForumQuestion, forumAnswers: List<ForumAnswer>) {

        itemView.recycler_view.layoutManager = InnerLayoutManager()
        (itemView.recycler_view.adapter as ForumTopicInnerAdapter).addData(forumAnswers)

        val options = RequestOptions
                .placeholderOf(R.drawable.avatar_placeholder)
                .error(R.drawable.ic_launcher_background)
                .centerCrop()
                .fitCenter()
        Glide.with(itemView.context)
                .load(forumQuestion.authorAvatarUrl)
                .apply(options)
                .into(itemView.avatar)

        val mainTitle = forumQuestion.question
        val spannedTitle = SpannableString("${forumQuestion.question} - ${forumQuestion.authorLogin}")
        spannedTitle.setSpan(AbsoluteSizeSpan(mTitleSize1), 0, mainTitle.length, SPAN_INCLUSIVE_INCLUSIVE)
        spannedTitle.setSpan(AbsoluteSizeSpan(mTitleSize2), mainTitle.length, spannedTitle.length, SPAN_INCLUSIVE_INCLUSIVE)
        spannedTitle.setSpan(ForegroundColorSpan(Color.argb(204, 255, 255, 255)), mainTitle.length, spannedTitle.length, SPAN_INCLUSIVE_INCLUSIVE)

        itemView.header_main_title.text = mainTitle
        itemView.header_spannable_title.text = spannedTitle

        itemView.tv_author.text = String.format("Asked by: %s", forumQuestion.authorLogin)
        itemView.tv_date.text = String.format("Asked at: %s", forumQuestion.postedAt)

    }


    internal fun clearContent() {
        Glide.with(itemView.avatar.context).clear(itemView.avatar)
        (itemView.recycler_view.adapter as ForumTopicInnerAdapter).clearData()
    }

    private fun onItemScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val ratio = computeRatio(recyclerView)

        val footerRatio = Math.max(0f, Math.min(FOOTER_RATIO_START, ratio) - FOOTER_RATIO_DIFF) / FOOTER_RATIO_MAX
        val avatarRatio = Math.max(0f, Math.min(AVATAR_RATIO_START, ratio) - AVATAR_RATIO_DIFF) / AVATAR_RATIO_MAX
        val answerRatio = Math.max(0f, Math.min(ANSWER_RATIO_START, ratio) - ANSWER_RATIO_DIFF) / ANSWER_RATIO_MAX
        val middleRatio = Math.max(0f, Math.min(MIDDLE_RATIO_START, ratio) - MIDDLE_RATIO_DIFF) / MIDDLE_RATIO_MAX

        ViewCompat.setPivotY(itemView.header_footer, 0f)
        ViewCompat.setScaleY(itemView.header_footer, footerRatio)
        ViewCompat.setAlpha(itemView.header_footer, footerRatio)

        ViewCompat.setPivotY(itemView.header_middle_answer, itemView.header_middle_answer.height.toFloat())
        ViewCompat.setScaleY(itemView.header_middle_answer, 1f - answerRatio)
        ViewCompat.setAlpha(itemView.header_middle_answer, 0.5f - answerRatio)

        ViewCompat.setAlpha(itemView.header_main_title, answerRatio)
        ViewCompat.setAlpha(itemView.header_spannable_title, 1f - answerRatio)

        val mc2 = mMiddleCollapsible[1]
        ViewCompat.setPivotX(mc2, 0f)
        ViewCompat.setPivotY(mc2, (mc2.height / 2).toFloat())

        for (view in mMiddleCollapsible) {
            ViewCompat.setScaleX(view, avatarRatio)
            ViewCompat.setScaleY(view, avatarRatio)
            ViewCompat.setAlpha(view, avatarRatio)
        }

        val lp = itemView.header_middle.layoutParams
        lp.height = m120dp - (m10dp * (1f - middleRatio)).toInt()
        itemView.header_middle.layoutParams = lp
    }

    private fun computeRatio(recyclerView: RecyclerView): Float {
        val child0 = recyclerView.getChildAt(0)
        val pos = recyclerView.getChildAdapterPosition(child0)
        if (pos != 0) {
            return 0f
        }

        val height = child0.height
        val y = Math.max(0f, child0.y)
        return y / height
    }
}