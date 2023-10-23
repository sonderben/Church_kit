package com.churchkit.churchkit.ui.more

import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.churchkit.churchkit.R

class DocumentationWebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_documentation_webview)

        val webView: WebView = findViewById(R.id.webView)

        val documentationLink = intent.getStringExtra("documentation_link")

        if (!documentationLink.isNullOrBlank()) {
            webView.loadUrl(documentationLink)
        } else {
            // Handle the case where the documentation link is not valid
            // For example, show an error message or redirect to a default page
        }
    }
}

