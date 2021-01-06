package com.bearddr.newsapp.presentation.settings

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bearddr.newsapp.R
import com.bearddr.newsapp.databinding.FragmentSettingsBinding
import com.bearddr.newsapp.presentation.settings.epoxy.CategoriesEpoxyController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import timber.log.Timber

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

  private lateinit var binding: FragmentSettingsBinding
  private val viewModel: SettingsViewModel by viewModels()

  private val controller = CategoriesEpoxyController { category, isChecked ->
    viewModel.categoryCheckToggle(category, isChecked)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setHasOptionsMenu(true)
    binding = FragmentSettingsBinding.bind(view)

    setView()
    setObservers()

    viewModel.getCategoriesEvent()
  }

  private fun setView() {
    val categoriesLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    binding.epoxyList.apply {
      layoutManager = categoriesLayoutManager
      setController(controller)
      setHasFixedSize(true)
    }
  }

  private fun setObservers() {
    viewModel.state.observe(viewLifecycleOwner, {
      Timber.tag("AppDebug").d(it.toString())
      when (it.uiAction) {
        UiAction.Idle -> { }

        UiAction.Loading -> {
          //show progressbar
          Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
        }

        UiAction.ShowMessage -> Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()

        UiAction.ShowCategories -> {
          controller.setUiModels(it.categories)
        }
      }
    })
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.settings_options_menu, menu)
    super.onCreateOptionsMenu(menu, inflater)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when(item.itemId) {
      R.id.save_options_action -> {
        viewModel.saveEvent()
        true
      }

      else -> super.onOptionsItemSelected(item)
    }
  }
}