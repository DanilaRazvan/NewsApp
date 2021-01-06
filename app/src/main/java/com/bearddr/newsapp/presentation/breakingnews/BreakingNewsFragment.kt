
package com.bearddr.newsapp.presentation.breakingnews

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bearddr.newsapp.R
import com.bearddr.newsapp.databinding.FragmentBreakingNewsBinding
import com.bearddr.newsapp.presentation.breakingnews.epoxy.NewsEpoxyController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import timber.log.Timber

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

  private lateinit var binding: FragmentBreakingNewsBinding
  private val viewModel: BreakingNewsViewModel by viewModels()

  private val controller = NewsEpoxyController(
    { position ->
      viewModel.expandItem(position)
    },
    { articleUrl ->
      Timber.tag("AppDebug").d(articleUrl)
      val bundle = Bundle().apply {
        putString("articleUrl", articleUrl)
      }
      findNavController().navigate(
        R.id.action_breakingNewsFragment_to_newsDetailsFragment,
        bundle
      )
    })

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragmentBreakingNewsBinding.bind(view)

    setupView()
    setObservers()
    viewModel.fetchBreakingNewsEvent()
  }

  private fun setupView() {
    val newsLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    binding.epoxyList.apply {
      layoutManager = newsLayoutManager
      setController(controller)
      setHasFixedSize(true)
    }
  }

  private fun setObservers() {
    viewModel.state.observe(viewLifecycleOwner, {
      when (it.uiAction) {
        UiAction.Idle -> {
        }
        UiAction.Loading -> {
          Timber.tag("AppDebug").d("Loading")
        }
        UiAction.ShowMessage -> {
          Timber.tag("AppDebug").d("ShowMessage")
        }
        UiAction.DisplayNews -> {
          controller.setUiModels(it.feeds)
        }
      }
    })
  }

  companion object {
    @JvmStatic
    fun newInstance() = BreakingNewsFragment()
  }
}