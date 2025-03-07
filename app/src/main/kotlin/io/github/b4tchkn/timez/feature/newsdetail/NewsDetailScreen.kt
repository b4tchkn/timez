package io.github.b4tchkn.timez.feature.newsdetail

import MultiLocalePreviews
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import io.github.b4tchkn.timez.feature.newsdetail.NewsDetailScreenDefaultContentPreviewParameterProvider.Param
import io.github.b4tchkn.timez.feature.newsdetail.NewsDetailUiModel.Content
import io.github.b4tchkn.timez.model.Article
import io.github.b4tchkn.timez.model.Source
import io.github.b4tchkn.timez.ui.component.Gap
import io.github.b4tchkn.timez.ui.component.LoadingBox
import io.github.b4tchkn.timez.ui.component.MainSurface
import io.github.b4tchkn.timez.ui.theme.TimezTheme

data class NewsDetailScreenNavArgs(
    val article: Article,
)

@Destination(navArgsDelegate = NewsDetailScreenNavArgs::class)
@Composable
fun NewsDetailScreen(
    navigator: DestinationsNavigator,
) {
    val viewModel = hiltViewModel<NewsDetailViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold {
        LoadingBox(
            modifier = Modifier.padding(it),
            loading = false,
        ) {
            Box {
                NewsDetailScreenContent(
                    content = state.content,
                    onReadMoreClick = { /* TODO */ },
                )
                IconButton(
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp),
                    onClick = { navigator.navigateUp() },
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
    onReadMoreClick: () -> Unit,
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
    modifier: Modifier = Modifier,
    article: Article,
    onReadMoreClick: () -> Unit,
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
                    Text(
                        text = it,
                        style = TimezTheme.typography.h14,
                        color = TimezTheme.color.gray,
                    )
                }
            }
            item { Gap(32.dp) }
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
                        ),
                        onClick = onReadMoreClick,
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
    }
}

@MultiLocalePreviews
@Composable
private fun PreviewNewsDetailScreenDefaultContent(
    @PreviewParameter(NewsDetailScreenDefaultContentPreviewParameterProvider::class) param: Param,
) {
    MainSurface {
        NewsDetailScreenContent(
            content = param.content,
            onReadMoreClick = { },
        )
    }
}

private class NewsDetailScreenDefaultContentPreviewParameterProvider : PreviewParameterProvider<Param> {
    override val values = sequenceOf(
        Param(
            Content.Default(
                article = Article.Default.copy(
                    title = "Title",
                    publishedAt = "2021-01-01",
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
