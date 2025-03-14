package io.github.b4tchkn.timez.ui.component

import android.annotation.SuppressLint
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun AppWebView(
    client: AppWebViewClient,
    url: String,
    modifier: Modifier = Modifier,
) {
    AndroidView(
        modifier = modifier,
        factory = ::WebView,
        update = { webView ->
            webView.webViewClient = client
            webView.loadUrl(url)
            webView.settings.javaScriptEnabled = true
        },
    )
}

class AppWebViewClient : WebViewClient() {
    var loading by mutableStateOf(false)

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        loading = true
        return super.shouldOverrideUrlLoading(view, request)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        loading = false
        super.onPageFinished(view, url)
    }
}
