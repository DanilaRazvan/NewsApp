package com.bearddr.newsapp.presentation.breakingnews.epoxy

import android.view.View
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bearddr.newsapp.R
import com.bearddr.newsapp.databinding.NewsListItemDetailsBinding
import com.bearddr.newsapp.presentation.models.NewsUiModel
import kotlinx.android.synthetic.main.news_list_item_details.view.*

@EpoxyModelClass(layout = R.layout.news_list_item_details)
abstract class NewsDetailsEpoxyModel :
  EpoxyModelWithHolder<NewsDetailsEpoxyModel.NewsDetailsViewHolder>() {

  @EpoxyAttribute
  lateinit var newsDetailsModel: NewsUiModel.Details

  @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash, EpoxyAttribute.Option.DoNotUseInToString)
  var myCallback: View.OnClickListener? = null

  override fun bind(holder: NewsDetailsViewHolder) {
    super.bind(holder)

    holder.binding.root.tvMore.setOnClickListener(myCallback)
    holder.binding.tvPublishedDate.text = newsDetailsModel.publishedAt
    holder.binding.tvSourceName.text = newsDetailsModel.source
    holder.binding.tvContent.text = newsDetailsModel.content
  }

  inner class NewsDetailsViewHolder : EpoxyHolder() {

    lateinit var binding: NewsListItemDetailsBinding

    override fun bindView(itemView: View) {
      binding = NewsListItemDetailsBinding.bind(itemView)
    }
  }
}