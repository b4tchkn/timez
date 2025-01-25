package io.github.b4tchkn.timez.ui.top

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TopScreen(viewModel: TopViewModel = viewModel()) {
    val articles = viewModel.articles.collectAsStateWithLifecycle()

    Scaffold { padding ->
        Box(
            modifier = Modifier.padding(padding),
        ) {
            Text(articles.value.toString())
        }
    }
}
