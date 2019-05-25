package kz.mircella.blogapplication.ui.forum

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import kz.mircella.blogapplication.R
import kz.mircella.blogapplication.forum.model.ForumSection

class ForumSectionsListAdapter(private val forumItemClick: (forumItemTitle: String) -> Unit): SectionedRecyclerViewAdapter() {
    private lateinit var forumRecyclerViewSections: List<ForumRecyclerViewSection>

    fun updateForumSections(forumRecyclerViewSections: List<ForumSection>) {
        this.forumRecyclerViewSections = createForumSections(forumRecyclerViewSections)
        refresh()
    }

    private fun refresh() {
        this.forumRecyclerViewSections.forEach { addSection(it) }
        notifyDataSetChanged()
    }

    private fun createForumSections(forumSections: List<ForumSection>): List<ForumRecyclerViewSection> {
        return forumSections.map { ForumRecyclerViewSection(getSectionParameters(), it, forumItemClick)  }
    }

    fun getSectionParameters(): SectionParameters {
        return  SectionParameters.builder()
                .itemResourceId(R.layout.forum_list_item)
                .headerResourceId(R.layout.forum_section_header)
                .build()
    }

}