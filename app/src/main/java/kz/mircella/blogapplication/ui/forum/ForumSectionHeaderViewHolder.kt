package kz.mircella.blogapplication.ui.forum

import kz.mircella.blogapplication.databinding.ForumSectionHeaderBinding
import kz.mircella.blogapplication.forum.ForumSectionHeaderViewModel

class ForumSectionHeaderViewHolder(private val forumSectionHeaderLayoutBinding: ForumSectionHeaderBinding): ForumItemViewHolder(forumSectionHeaderLayoutBinding) {
    val forumSectionHeaderViewModel = ForumSectionHeaderViewModel()

    fun bind(sectionTitle: String) {
        forumSectionHeaderViewModel.bind(sectionTitle)
        forumSectionHeaderLayoutBinding.viewModel = forumSectionHeaderViewModel
    }
}