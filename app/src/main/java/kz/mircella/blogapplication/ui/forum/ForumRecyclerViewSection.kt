package kz.mircella.blogapplication.ui.forum

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection
import kz.mircella.blogapplication.R
import kz.mircella.blogapplication.databinding.ForumListItemBinding
import kz.mircella.blogapplication.databinding.ForumSectionHeaderBinding
import kz.mircella.blogapplication.forum.model.ForumSection

open class ForumRecyclerViewSection(sectionParameters: SectionParameters?,
                                    private val forumSection: ForumSection,
                                    private val forumItemClick: (forumItemTitle: String) -> Unit) : StatelessSection(sectionParameters) {

    override fun getContentItemsTotal(): Int {
        val forumItemsCount = forumSection.forumItems.size
        return when {
            forumItemsCount < 3 -> forumItemsCount
            else -> 3
        }
    }

    override fun getHeaderViewHolder(view: View?): RecyclerView.ViewHolder {
        val forumSectionHeaderBinding = DataBindingUtil.inflate<ForumSectionHeaderBinding>(
                LayoutInflater.from(view?.context),
                R.layout.forum_section_header,
                view as ViewGroup,
                false)
        return ForumSectionHeaderViewHolder(forumSectionHeaderBinding)
    }

    override fun getItemViewHolder(view: View?): RecyclerView.ViewHolder {
        val forumListItemBinding = DataBindingUtil.inflate<ForumListItemBinding>(LayoutInflater.from(view?.context), R.layout.forum_list_item, view as ViewGroup, false)
        return ForumListItemViewHolder(forumListItemBinding, forumItemClick)
    }

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val forumListItemViewHolder = holder as ForumListItemViewHolder
        forumListItemViewHolder.bind(forumSection.forumItems[position])

    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder?) {
        val forumSectionHeaderViewHolder = holder as ForumSectionHeaderViewHolder
        forumSectionHeaderViewHolder.bind(forumSection.sectionTitle)
    }
}