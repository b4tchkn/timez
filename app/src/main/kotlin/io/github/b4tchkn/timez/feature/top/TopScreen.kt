package io.github.b4tchkn.timez.feature.top

import MultiLocalePreviews
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.NewsDetailScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import io.github.b4tchkn.timez.R
import io.github.b4tchkn.timez.core.FakeNowLocalDateTime
import io.github.b4tchkn.timez.core.LocalNowLocalDateTime
import io.github.b4tchkn.timez.core.RelativeTime
import io.github.b4tchkn.timez.core.formatRelativeTimeFromNow
import io.github.b4tchkn.timez.feature.top.TopScreenPreviewParameterProvider.Param
import io.github.b4tchkn.timez.feature.top.TopUiModel.Content
import io.github.b4tchkn.timez.feature.top.component.ArticleCard
import io.github.b4tchkn.timez.model.Article
import io.github.b4tchkn.timez.ui.component.Gap
import io.github.b4tchkn.timez.ui.component.LoadingBox
import io.github.b4tchkn.timez.ui.component.MainSurface
import io.github.b4tchkn.timez.ui.component.rememberAppSnackbarState
import io.github.b4tchkn.timez.ui.foundation.LaunchStateEffect
import io.github.b4tchkn.timez.ui.theme.TimezTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "ComposeModifierMissing")
@Destination<RootGraph>(start = true)
@Composable
fun TopScreen(
    navigator: DestinationsNavigator,
    viewModel: TopViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarState = rememberAppSnackbarState()

    val unknownErrorSnackbarMessage = stringResource(R.string.snackbar_unknown_error_message)

    viewModel.LaunchStateEffect(state.message, TopUiEvent.ClearMessage) {
        when (it) {
            TopUiModel.MessageState.Error -> {
                snackbarState.showSnackbar(unknownErrorSnackbarMessage)
            }

            is TopUiModel.MessageState.NavigateNewsDetail -> navigator.navigate(NewsDetailScreenDestination(it.articleId))
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarState.snackbarHostState)
        },
    ) {
        LoadingBox(
            loading = state.loading,
        ) {
            val content = state.content ?: return@LoadingBox

            PullToRefreshBox(
                isRefreshing = state.loading,
                onRefresh = { viewModel.take(TopUiEvent.Refresh) },
            ) {
                TopScreenContent(
                    content = content,
                    onArticleClick = { viewModel.take(TopUiEvent.ClickArticle(it)) },
                )
            }
        }
    }
}

@Composable
private fun TopScreenContent(
    content: Content,
    onArticleClick: (Article) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (content) {
        is Content.Default -> TopScreenDefaultContent(
            modifier = modifier,
            articles = content.articles,
            onArticleClick = onArticleClick,
        )

        Content.Empty -> Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                modifier = Modifier.size(80.dp),
                painter = painterResource(R.drawable.baseline_newspaper_24),
                contentDescription = null,
            )
            Gap(16.dp)
            Text(
                stringResource(R.string.top_empty_news),
                style = TimezTheme.typography.h20.copy(
                    textAlign = TextAlign.Center,
                ),
                color = TimezTheme.color.textColor,
            )
        }
    }
}

@Composable
private fun TopScreenDefaultContent(
    articles: ImmutableList<Article>,
    onArticleClick: (Article) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            horizontal = 16.dp,
        ),
    ) {
        val breakingNews = articles.firstOrNull()

        if (breakingNews != null) {
            item { Spacer(Modifier.windowInsetsTopHeight(WindowInsets.systemBars)) }
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
                    stringResource(R.string.top_recent_news),
                    style = TimezTheme.typography.h20.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = TimezTheme.color.textColor,
                )
            }
            item { Gap(8.dp) }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.nestedScroll(rememberNestedScrollInteropConnection()),
                ) {
                    for (article in articles.drop(1)) {
                        ArticleCard(
                            article = article,
                            onClick = { onArticleClick(article) },
                        )
                    }
                }
            }

            item { Gap(24.dp) }

            item {
                Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.systemBars))
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
            stringResource(R.string.top_breaking_news),
            style = TimezTheme.typography.h24.copy(
                fontWeight = FontWeight.Bold,
            ),
            color = TimezTheme.color.textColor,
        )
        Gap(8.dp)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .clip(CardDefaults.shape)
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
                            ),
                            color = TimezTheme.color.white,
                        )
                    }
                    article.publishedAt?.let {
                        val relativeText = when (val relativeTime = it.formatRelativeTimeFromNow(LocalNowLocalDateTime.current.value)) {
                            is RelativeTime.Days -> "${relativeTime.days}${stringResource(R.string.days_ago)}"
                            is RelativeTime.Hours -> "${relativeTime.hours}${stringResource(R.string.hours_ago)}"
                        }
                        Gap(4.dp)
                        Text(
                            text = relativeText,
                            style = TimezTheme.typography.h14,
                            color = TimezTheme.color.white,
                        )
                    }
                    Gap(16.dp)
                }
            }
        }
    }
}

@MultiLocalePreviews
@Composable
private fun PreviewTopScreenDefaultContent(
    @PreviewParameter(TopScreenPreviewParameterProvider::class) param: Param,
) {
    CompositionLocalProvider(
        LocalNowLocalDateTime provides FakeNowLocalDateTime,
    ) {
        MainSurface {
            TopScreenContent(
                content = param.content,
                onArticleClick = {},
            )
        }
    }
}

private class TopScreenPreviewParameterProvider : PreviewParameterProvider<Param> {
    override val values = sequenceOf(
        Param(
            Content.Default(
                articles = List(10) {
                    Article.Default.copy(
                        title = "Titleee $it",
                        publishedAt = LocalDateTime(2000, 1, 1, 12, 0),
                    )
                }.toImmutableList(),
            ),
        ),
        Param(Content.Empty),
    )

    data class Param(
        val content: Content,
    )
}
