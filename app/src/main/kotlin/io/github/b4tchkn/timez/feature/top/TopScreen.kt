package io.github.b4tchkn.timez.feature.top

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import io.github.b4tchkn.timez.R
import io.github.b4tchkn.timez.feature.destinations.NewsDetailScreenDestination
import io.github.b4tchkn.timez.feature.top.component.ArticleCard
import io.github.b4tchkn.timez.model.Article
import io.github.b4tchkn.timez.ui.component.Gap
import io.github.b4tchkn.timez.ui.component.LoadingBox
import io.github.b4tchkn.timez.ui.component.MainSurface
import io.github.b4tchkn.timez.ui.theme.TimezTheme

@OptIn(ExperimentalMaterial3Api::class)
@RootNavGraph(start = true)
@Destination
@Composable
fun TopScreen(
    navigator: DestinationsNavigator,
) {
    val viewModel = hiltViewModel<TopViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TIMEZ") },
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
                is TopUiModel.Content.Default -> TopScreenDefaultContent(
                    articles = content.articles,
                    onArticleClick = { article ->
                        navigator.navigate(
                            NewsDetailScreenDestination(article),
                        )
                    },
                )

                TopUiModel.Content.Empty -> TODO()
            }
        }
    }
}

@Composable
private fun TopScreenDefaultContent(
    articles: List<Article>,
    onArticleClick: (Article) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 24.dp,
        ),
    ) {
        val breakingNews = articles.firstOrNull()

        if (breakingNews != null) {
            item {
                TopArticle(
                    article = breakingNews,
                    onClick = { onArticleClick(breakingNews) },
                )
            }
        }

        val recentNews = articles.drop(1)

        if (recentNews.isNotEmpty()) {
            item { Gap(16.dp) }
            item {
                Text(
                    "Recent News",
                    style = TimezTheme.typography.h20.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                )
            }
            item { Gap(8.dp) }

            item {
                LazyColumn(
                    modifier = Modifier.fillParentMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    userScrollEnabled = false,
                ) {
                    items(articles.drop(1)) {
                        ArticleCard(
                            article = it,
                            onClick = { onArticleClick(it) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TopArticle(
    article: Article,
    onClick: () -> Unit,
) {
    Column {
        Text(
            "Breaking News",
            style = TimezTheme.typography.h24.copy(
                fontWeight = FontWeight.Bold,
            ),
        )
        Gap(8.dp)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .clickable { onClick() },
        ) {
            Box(
                contentAlignment = Alignment.BottomStart,
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .drawWithContent {
                            drawContent()
                            drawRect(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Black.copy(alpha = 0.7f),
                                    ),
                                ),
                            )
                        },
                    model = article.urlToImage,
                    error = painterResource(R.drawable.baseline_hide_image_24),
                    contentScale = ContentScale.Crop,
                    contentDescription = article.title,
                )
                Column(
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                    ),
                ) {
                    article.title?.let {
                        Text(
                            it,
                            style = TimezTheme.typography.h20.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                            ),
                        )
                    }
                    article.publishedAt?.let {
                        Gap(4.dp)
                        Text(
                            it,
                            style = TimezTheme.typography.h14.copy(
                                color = Color.White,
                            ),
                        )
                    }
                    Gap(16.dp)
                }
            }
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
            onArticleClick = {},
        )
    }
}
