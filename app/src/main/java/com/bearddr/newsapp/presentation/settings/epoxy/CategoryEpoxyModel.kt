package com.bearddr.newsapp.presentation.settings.epoxy

import android.view.View
import android.widget.CompoundButton
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bearddr.newsapp.R
import com.bearddr.newsapp.databinding.SearchCategoryLayoutBinding
import com.bearddr.newsapp.presentation.models.CategoryUiModel

@EpoxyModelClass(layout = R.layout.search_category_layout)
abstract class CategoryEpoxyModel : EpoxyModelWithHolder<CategoryEpoxyModel.CategoryViewHolder>() {

  @EpoxyAttribute
  lateinit var category: CategoryUiModel

  @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash, EpoxyAttribute.Option.DoNotUseInToString)
  var myCallback: CompoundButton.OnCheckedChangeListener? = null

  override fun bind(holder: CategoryViewHolder) {
    super.bind(holder)

    holder.binding.filterTitle.text = category.title
    holder.binding.categoryCheckbox.isChecked = category.checked
    holder.binding.categoryCheckbox.setOnCheckedChangeListener(myCallback)
  }

  inner class CategoryViewHolder: EpoxyHolder() {
    lateinit var binding: SearchCategoryLayoutBinding

    override fun bindView(itemView: View) {
      binding = SearchCategoryLayoutBinding.bind(itemView)
    }
  }
}