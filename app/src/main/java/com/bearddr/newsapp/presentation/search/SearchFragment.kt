package com.bearddr.newsapp.presentation.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bearddr.newsapp.R
import com.bearddr.newsapp.databinding.FragmentSearchBinding
import com.bearddr.newsapp.presentation.search.epoxy.SearchNewsEpoxyController
import com.mancj.materialsearchbar.MaterialSearchBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import timber.log.Timber

@ExperimentalCoroutinesApi
@FlowPreview
@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

  private lateinit var binding: FragmentSearchBinding
  private val viewModel: SearchViewModel by viewModels()

  private val controller = SearchNewsEpoxyController { articleUrl ->
    Timber.tag("AppDebug").d(articleUrl)
    val bundle = Bundle().apply {
      putString("articleUrl", articleUrl)
    }
    findNavController().navigate(
      R.id.action_searchFragment_to_newsDetailsFragment,
      bundle
    )
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragmentSearchBinding.bind(view)

    setupView()
    setObservers()
    setListeners()
  }

  private fun setupView() {
    val newsLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    binding.epoxyList.apply {
      layoutManager = newsLayoutManager
      setController(controller)
      setHasFixedSize(true)
    }
  }

  private fun setListeners() {
    binding.searchBar.setOnSearchActionListener(object : MaterialSearchBar.OnSearchActionListener {
      override fun onSearchStateChanged(enabled: Boolean) {}

      override fun onSearchConfirmed(text: CharSequence?) {
        text?.let {
          if (it.isNotEmpty())
            viewModel.searchNews(text.toString())
        }
      }

      override fun onButtonClicked(buttonCode: Int) {}
    })
  }

  private fun setObservers() {
    viewModel.state.observe(viewLifecycleOwner, {
      when(it.uiAction) {
        UiAction.NewsDate.Idle -> {  }
        UiAction.NewsDate.Loading -> {  }
        UiAction.NewsDate.ShowMessage -> {  }
        UiAction.NewsDate.DisplayNews -> {
          controller.setUiModels(it.news)
        }


        UiAction.NewsRelevancy.Idle -> {  }
        UiAction.NewsRelevancy.Loading -> {  }
        UiAction.NewsRelevancy.ShowMessage -> {  }
        UiAction.NewsRelevancy.DisplayNews -> {
          controller.setUiModels(it.news)
        }


        UiAction.NewsPopularity.Idle -> {  }
        UiAction.NewsPopularity.Loading -> {  }
        UiAction.NewsPopularity.ShowMessage -> {  }
        UiAction.NewsPopularity.DisplayNews -> {
          controller.setUiModels(it.news)
        }
      }
    })
  }

  companion object {
    @JvmStatic
    fun newInstance() = SearchFragment()
  }
}