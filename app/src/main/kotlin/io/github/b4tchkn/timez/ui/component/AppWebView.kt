package io.github.b4tchkn.timez.ui.component

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun AppWebView(
    modifier: Modifier = Modifier,
    url: String,
) {
    AndroidView(
        modifier = modifier,
        factory = ::WebView,
        update = { webView ->
            webView.webViewClient = WebViewClient()
            webView.loadUrl(url)
        },
    )
}
