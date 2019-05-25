package kz.mircella.blogapplication.ui.forum

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.ramotion.garlandview.TailAdapter
import kotlinx.android.synthetic.main.answer_input_dialog.view.et_answer
import kz.mircella.blogapplication.R
import kz.mircella.blogapplication.forum.model.ForumAnswer
import kz.mircella.blogapplication.forum.model.ForumQuestion

class ForumTopicAdapter : TailAdapter<ForumTopicItem>() {
    private val POOL_SIZE = 16

    private lateinit var question: ForumQuestion
    private lateinit var answers: List<ForumAnswer>
    private lateinit var view: View
    private val mPool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()

    init {
        mPool.setMaxRecycledViews(0, POOL_SIZE)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForumTopicItem {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        this.view = view
        return ForumTopicItem(view, mPool) {
            displayAnswerInputDialog(R.string.answer_input_dialog_title, R.layout.answer_input_dialog) { view ->
                val answerText = view.et_answer.text.toString()
                val newAnswer = ForumAnswer("yevgeniya.li@gmail.com", "2019-05-17", answerText)
                val newAnswers = ArrayList<ForumAnswer>(answers)
                newAnswers.add(newAnswer)
                this.answers = newAnswers.toList()
                updateData(question, answers)
            }
        }
    }

    override fun onBindViewHolder(holder: ForumTopicItem, position: Int) {
        holder.setContent(question, answers)
    }

    override fun onViewRecycled(holder: ForumTopicItem) {
        holder.clearContent()
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.forum_topic_outer_item
    }

    fun updateData(question: ForumQuestion, answers: List<ForumAnswer>) {
        this.question = question
        this.answers = answers
        notifyDataSetChanged()
    }

    private fun displayAnswerInputDialog(
            @StringRes titleResId: Int,
            @LayoutRes layoutResId: Int,
            onConfirmationClick: (View) -> Unit
    ) {
        @SuppressLint("InflateParams")
        val view = LayoutInflater.from(view.context).inflate(
                layoutResId,
                null
        )
        AlertDialog.Builder(view.context)
                .setTitle(titleResId)
                .setView(view)
                .setPositiveButton(android.R.string.ok) { dialog, _ ->
                    onConfirmationClick(view)
                    dialog.dismiss()
                }
                .setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.dismiss() }
                .show()
    }

}

