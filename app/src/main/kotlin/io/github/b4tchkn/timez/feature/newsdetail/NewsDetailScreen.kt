package io.github.b4tchkn.timez.feature.newsdetail

import MultiLocalePreviews
import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import io.github.b4tchkn.timez.R
import io.github.b4tchkn.timez.core.FakeNowLocalDateTime
import io.github.b4tchkn.timez.core.LocalNowLocalDateTime
import io.github.b4tchkn.timez.core.RelativeTime
import io.github.b4tchkn.timez.core.formatRelativeTimeFromNow
import io.github.b4tchkn.timez.feature.destinations.ArticleScreenDestination
import io.github.b4tchkn.timez.feature.newsdetail.NewsDetailScreenDefaultContentPreviewParameterProvider.Param
import io.github.b4tchkn.timez.feature.newsdetail.NewsDetailUiModel.Content
import io.github.b4tchkn.timez.feature.newsdetail.NewsDetailUiModel.MessageState
import io.github.b4tchkn.timez.model.Article
import io.github.b4tchkn.timez.model.Source
import io.github.b4tchkn.timez.ui.component.Gap
import io.github.b4tchkn.timez.ui.component.LoadingBox
import io.github.b4tchkn.timez.ui.component.MainSurface
import io.github.b4tchkn.timez.ui.component.rememberAppSnackbarState
import io.github.b4tchkn.timez.ui.foundation.LaunchStateEffect
import io.github.b4tchkn.timez.ui.theme.TimezTheme
import kotlinx.datetime.LocalDateTime

data class NewsDetailScreenNavArgs(
    val articleId: String,
)

@SuppressLint("ComposeModifierMissing")
@Destination(navArgsDelegate = NewsDetailScreenNavArgs::class)
@Composable
fun NewsDetailScreen(
    navigator: DestinationsNavigator,
    viewModel: NewsDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarState = rememberAppSnackbarState()

    val unknownErrorSnackbarMessage = stringResource(R.string.snackbar_unknown_error_message)

    viewModel.LaunchStateEffect(state.message, NewsDetailUiEvent.ClearMessage) {
        when (it) {
            MessageState.Error -> {
                snackbarState.showSnackbar(unknownErrorSnackbarMessage)
            }
            MessageState.NavigatePop -> navigator.popBackStack()
            is MessageState.NavigateArticle -> navigator.navigate(
                ArticleScreenDestination(it.url),
            )
        }
    }

    BackHandler {
        viewModel.take(NewsDetailUiEvent.Pop)
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarState.snackbarHostState)
        },
    ) { innerPadding ->
        LoadingBox(
            loading = false,
        ) {
            Box {
                NewsDetailScreenContent(
                    content = state.content,
                    onReadMoreClick = { viewModel.take(NewsDetailUiEvent.ClickReadMore(it)) },
                )
                IconButton(
                    modifier = Modifier.padding(
                        top = innerPadding.calculateTopPadding(),
                        start = 16.dp,
                    ),
                    colors = IconButtonDefaults.iconButtonColors().copy(
                        containerColor = TimezTheme.color.black.copy(alpha = 0.3F),
                    ),
                    onClick = { viewModel.take(NewsDetailUiEvent.Pop) },
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        tint = TimezTheme.color.white,
                        contentDescription = stringResource(R.string.description_back),
                    )
                }
            }
        }
    }
}

@Composable
private fun NewsDetailScreenContent(
    content: Content,
    onReadMoreClick: (url: String) -> Unit,
) {
    when (content) {
        is Content.Default -> {
            NewsDetailScreenDefaultContent(
                article = content.article,
                onReadMoreClick = onReadMoreClick,
            )
        }

        Content.Empty -> Text("No article")
    }
}

@Composable
private fun NewsDetailScreenDefaultContent(
    article: Article,
    onReadMoreClick: (url: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter,
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .drawWithContent {
                    drawContent()
                    drawRect(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Black.copy(alpha = 1f),
                            ),
                        ),
                    )
                },
            error = painterResource(R.drawable.baseline_hide_image_24),
            contentScale = ContentScale.FillHeight,
            contentDescription = article.title,
            model = article.urlToImage,
        )
        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp),
        ) {
            item { Gap(400.dp) }
            article.source?.name?.let {
                item {
                    Text(
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = TimezTheme.color.white,
                                shape = RoundedCornerShape(4.dp),
                            ).background(
                                TimezTheme.color.white.copy(alpha = 0.2f),
                            ).padding(
                                vertical = 4.dp,
                                horizontal = 8.dp,
                            ),
                        text = it,
                        style = TimezTheme.typography.h14,
                        color = TimezTheme.color.white,
                    )
                }
                item { Gap(8.dp) }
            }
            item {
                Text(
                    text = article.title ?: "-",
                    style = TimezTheme.typography.h32.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = TimezTheme.color.white,
                )
            }
            item { Gap(16.dp) }
            item {
                Text(
                    text = article.description ?: "-",
                    style = TimezTheme.typography.h16,
                    color = TimezTheme.color.white,
                )
            }
            item { Gap(16.dp) }
            article.publishedAt?.let {
                item {
                    val relativeText = when (val relativeTime = it.formatRelativeTimeFromNow(LocalNowLocalDateTime.current.value)) {
                        is RelativeTime.Days -> "${relativeTime.days}${stringResource(R.string.days_ago)}"
                        is RelativeTime.Hours -> "${relativeTime.hours}${stringResource(R.string.hours_ago)}"
                    }
                    Text(
                        text = relativeText,
                        style = TimezTheme.typography.h14,
                        color = TimezTheme.color.gray,
                    )
                }
            }
            item { Gap(32.dp) }
            article.url?.let {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {
                        OutlinedButton(
                            modifier = Modifier
                                .align(Alignment.Center),
                            colors = ButtonDefaults.outlinedButtonColors().copy(
                                containerColor = TimezTheme.color.white.copy(alpha = 0.08f),
                                contentColor = TimezTheme.color.primaryContainer,
                            ),
                            onClick = { onReadMoreClick(it) },
                        ) {
                            Text(stringResource(R.string.news_detail_read_more))
                            Gap(4.dp)
                            Icon(
                                Icons.AutoMirrored.Default.KeyboardArrowRight,
                                contentDescription = null,
                            )
                        }
                    }
                }
            }
            item { Gap(24.dp) }
        }
    }
}

@MultiLocalePreviews
@Composable
private fun PreviewNewsDetailScreenDefaultContent(
    @PreviewParameter(NewsDetailScreenDefaultContentPreviewParameterProvider::class) param: Param,
) {
    CompositionLocalProvider(LocalNowLocalDateTime provides FakeNowLocalDateTime) {
        MainSurface {
            NewsDetailScreenContent(
                content = param.content,
                onReadMoreClick = { },
            )
        }
    }
}

private class NewsDetailScreenDefaultContentPreviewParameterProvider : PreviewParameterProvider<Param> {
    override val values = sequenceOf(
        Param(
            Content.Default(
                article = Article.Default.copy(
                    title = "Title",
                    publishedAt = LocalDateTime(2000, 1, 1, 12, 0),
                    description = "This is content.".repeat(10),
                    source = Source.Default.copy(name = "SourceName"),
                ),
            ),
        ),
        Param(Content.Empty),
    )

    data class Param(
        val content: Content,
    )
}
