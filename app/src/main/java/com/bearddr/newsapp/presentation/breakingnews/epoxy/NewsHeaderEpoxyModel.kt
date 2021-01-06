package com.bearddr.newsapp.presentation.breakingnews.epoxy

import android.view.View
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bearddr.newsapp.R
import com.bearddr.newsapp.databinding.NewsListItemHeaderBinding
import com.bearddr.newsapp.presentation.models.NewsUiModel
import com.bearddr.newsapp.util.extensions.loadFromUrl

@EpoxyModelClass(layout = R.layout.news_list_item_header)
abstract class NewsHeaderEpoxyModel: EpoxyModelWithHolder<NewsHeaderEpoxyModel.NewsHeaderViewHolder>() {

  @EpoxyAttribute
  lateinit var newsHeaderModel: NewsUiModel.Header

  @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash, EpoxyAttribute.Option.DoNotUseInToString)
  var myCallback: View.OnClickListener? = null

  override fun bind(holder: NewsHeaderViewHolder) {
    super.bind(holder)

    holder.binding.root.setOnClickListener(myCallback)
    holder.binding.newsDetails.visibility = if (newsHeaderModel.expanded) View.VISIBLE else View.GONE

    holder.binding.ivNewsImage.loadFromUrl(newsHeaderModel.imageUrl)
    holder.binding.tvTitle.text = newsHeaderModel.title
  }

  inner class NewsHeaderViewHolder: EpoxyHolder() {

    lateinit var binding: NewsListItemHeaderBinding

    override fun bindView(itemView: View) {
      binding = NewsListItemHeaderBinding.bind(itemView)
    }
  }
}