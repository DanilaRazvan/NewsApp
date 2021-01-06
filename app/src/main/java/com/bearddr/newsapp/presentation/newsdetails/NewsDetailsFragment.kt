package com.bearddr.newsapp.presentation.newsdetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.webkit.WebViewClient
import com.bearddr.newsapp.R
import com.bearddr.newsapp.databinding.FragmentNewsDetailsBinding
import timber.log.Timber

class NewsDetailsFragment : Fragment(R.layout.fragment_news_details) {

  private lateinit var binding: FragmentNewsDetailsBinding

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding = FragmentNewsDetailsBinding.bind(view)

    Timber.tag("AppDebug").d(arguments?.getString("articleUrl") ?: "https://www.google.ro/")

    binding.webView.apply {
      settings.javaScriptEnabled = true
      webViewClient = WebViewClient()
      loadUrl(arguments?.getString("articleUrl") ?: "https://www.google.ro/")
    }
  }

  companion object {
    @JvmStatic
    fun newInstance() = NewsDetailsFragment()
  }
}