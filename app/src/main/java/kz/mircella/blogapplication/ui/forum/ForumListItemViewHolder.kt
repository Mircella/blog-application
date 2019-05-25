package kz.mircella.blogapplication.ui.forum

import kz.mircella.blogapplication.databinding.ForumListItemBinding
import kz.mircella.blogapplication.forum.ForumItemViewModel
import kz.mircella.blogapplication.forum.model.ForumItem

class ForumListItemViewHolder(private val forumListItemBinding: ForumListItemBinding,
                              forumItemClick: (forumItemTitle: String) -> Unit
) :  ForumItemViewHolder(forumListItemBinding) {
    val forumItemViewModel = ForumItemViewModel(forumItemClick)

    fun bind(forumItem: ForumItem) {
        forumItemViewModel.bind(forumItem)
        forumListItemBinding.viewModel = forumItemViewModel
    }
}