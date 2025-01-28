package io.github.b4tchkn.timez.feature.top

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.b4tchkn.timez.feature.top.component.ArticleCard
import io.github.b4tchkn.timez.model.Article
import io.github.b4tchkn.timez.ui.component.LoadingBox
import io.github.b4tchkn.timez.ui.component.MainSurface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopScreen() {
    val viewModel = hiltViewModel<TopViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Top") },
                actions = {
                    IconButton(onClick = { viewModel.take(TopUiEvent.Refresh) }) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                },
            )
        },
    ) { padding ->

        LoadingBox(
            modifier = Modifier.padding(padding),
            loading = state.loading,
        ) {
            when (val content = state.content) {
                is TopUiModel.Content.Default -> TopScreenDefaultContent(content.articles)
            }
        }
    }
}

@Composable
private fun TopScreenDefaultContent(articles: List<Article>) {
    LazyColumn(
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 24.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(articles) {
            ArticleCard(article = it)
        }
    }
}

@Preview
@Composable
private fun PreviewTopScreenDefaultContent() {
    MainSurface {
        TopScreenDefaultContent(
            articles = List(10) {
                Article.Default.copy(
                    title = "Title $it",
                    publishedAt = "2021-01-01",
                )
            },
        )
    }
}
