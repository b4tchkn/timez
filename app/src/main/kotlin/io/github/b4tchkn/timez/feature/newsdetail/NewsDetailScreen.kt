package io.github.b4tchkn.timez.feature.newsdetail

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import io.github.b4tchkn.timez.model.Article
import io.github.b4tchkn.timez.ui.component.LoadingBox

data class NewsDetailScreenNavArgs(
    val article: Article,
)

@OptIn(ExperimentalMaterial3Api::class)
@Destination(navArgsDelegate = NewsDetailScreenNavArgs::class)
@Composable
fun NewsDetailScreen(
    navigator: DestinationsNavigator,
) {
    val viewModel = hiltViewModel<NewsDetailViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { navigator.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
            )
        },
    ) {
        LoadingBox(
            modifier = Modifier.padding(it),
            loading = false,
        ) {
            when (val content = state.content) {
                is NewsDetailUiModel.Content.Default -> {
                    Text(content.article.title ?: "No title")
                }

                NewsDetailUiModel.Content.Empty -> Text("No article")
            }
        }
    }
}
