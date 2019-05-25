package kz.mircella.blogapplication.forum

import android.view.View
import android.view.View.GONE
import androidx.lifecycle.MutableLiveData
import com.ramotion.garlandview.TailAdapter
import io.reactivex.disposables.CompositeDisposable
import kz.mircella.blogapplication.base.BaseViewModel
import kz.mircella.blogapplication.forum.model.ForumAnswer
import kz.mircella.blogapplication.forum.model.ForumQuestion
import kz.mircella.blogapplication.ui.forum.ForumTopicAdapter
import kz.mircella.blogapplication.ui.forum.ForumTopicItem
import kz.mircella.blogapplication.utils.BASE_URL

class ForumTopicViewModel(forumTopicTitle: String) : BaseViewModel() {
    private val title = MutableLiveData<String>(forumTopicTitle)
    private val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    private val adapter: TailAdapter<ForumTopicItem> = ForumTopicAdapter()
    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadForumTopic(title.value) }
    private val disposables = CompositeDisposable()

    fun getTitle(): MutableLiveData<String> {
        return title
    }

    init {
        loadForumTopic(title.value)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    private fun loadForumTopic(forumTopicTitle: String?) {
        var authorId = "13663503-b6e1-4cc6-9d57-e6da443c6520"
        var question = ForumQuestion(authorId, "swetlana.li@gmail.com", "${BASE_URL}users/avatar/$authorId", "2018-07-01", "How do you find Harry Potter?")
        var answers: List<ForumAnswer> = listOf(
                ForumAnswer("yevgeniya.li@gmail.com", "2018-07-01", "Fine!"),
                ForumAnswer("aydan.yenbekbay@gmail.com", "2018-07-02", "Bad!"),
                ForumAnswer("swetlana.li@gmail.com", "2018-07-02", "Disagree with you!"),
                ForumAnswer("aljona.korneva@gmail.com", "2018-07-10", "I really like it")
        )
        if (forumTopicTitle == "Group of fans of Game of Thrones") {
            authorId = "39a5e5f4-c28b-4238-b8be-b5e04ef29c8b"
            question = ForumQuestion(authorId, "marina.li@gmail.com", "${BASE_URL}users/avatar/$authorId", "2018-07-11", "Are you going to watch new episode?")
            answers = listOf(
                    ForumAnswer("aydan.yenbekbay@gmail.com", "2018-07-11", "Yes!"),
                    ForumAnswer("aljona.korneva@gmail.com", "2018-07-12", "Don't have time :(")
            )
        } else if (forumTopicTitle == "Premier show of The Social Network in MultiKino") {
            authorId = "a7efb314-3961-489d-b16f-11b0bbf1da32"
            question = ForumQuestion(authorId, "aydan.yenbekbay@gmail.com", "${BASE_URL}users/avatar/$authorId", "2018-07-05", "Premier will be next Monday!")
            answers = listOf(
                    ForumAnswer("yevgeniya.li@gmail.com", "2018-07-07", "Awesome!"),
                    ForumAnswer("swetlana.li@gmail.com", "2018-07-07", "I will go!")
            )
        }
        (adapter as ForumTopicAdapter).updateData(question, answers)
    }

    fun getLoadingVisibility(): MutableLiveData<Int> {
        return loadingVisibility
    }

    fun setLoadingUnvisible() {
        loadingVisibility.value = GONE
    }

    fun getAdapter(): TailAdapter<ForumTopicItem> {
        return adapter
    }
}