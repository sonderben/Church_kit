package com.churchkit.churchkit.ui.more

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Toast
import com.churchkit.churchkit.R


class WebViewFragment : Fragment() {
    lateinit var webView: WebView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_web_view, container, false)
         webView = view.findViewById(R.id.webView)

        val documentationLink =
            requireArguments().getString("WEB_VIEW_LINK")

        if (!documentationLink.isNullOrBlank()) {
            webView.loadUrl(documentationLink)
        }



        return view
    }



    override fun onDestroy() {
        super.onDestroy()
        webView.clearCache(true)
        webView.destroy()
    }

}