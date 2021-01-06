package com.bearddr.newsapp.presentation.search.epoxy

import android.view.View
import com.airbnb.epoxy.*
import com.bearddr.newsapp.R
import com.bearddr.newsapp.databinding.SearchFilterTitleBinding

@EpoxyModelClass(layout = R.layout.search_filter_title)
abstract class SearchCategoryEpoxyModel: EpoxyModelWithHolder<SearchCategoryEpoxyModel.SearchCategoryViewHolder>() {

  @EpoxyAttribute
  lateinit var categoryTitle: String

  override fun bind(holder: SearchCategoryViewHolder) {
    super.bind(holder)

    holder.binding.filterTitle.text = categoryTitle
  }

  inner class SearchCategoryViewHolder: EpoxyHolder() {

    lateinit var binding: SearchFilterTitleBinding

    override fun bindView(itemView: View) {
      binding = SearchFilterTitleBinding.bind(itemView)
    }
  }
}