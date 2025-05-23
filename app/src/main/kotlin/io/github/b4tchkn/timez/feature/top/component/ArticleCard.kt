package io.github.b4tchkn.timez.feature.top.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import io.github.b4tchkn.timez.R
import io.github.b4tchkn.timez.core.FakeNowLocalDateTime
import io.github.b4tchkn.timez.core.LocalNowLocalDateTime
import io.github.b4tchkn.timez.core.RelativeTime
import io.github.b4tchkn.timez.core.formatRelativeTimeFromNow
import io.github.b4tchkn.timez.model.Article
import io.github.b4tchkn.timez.ui.component.Gap
import io.github.b4tchkn.timez.ui.theme.TimezTheme
import kotlinx.datetime.LocalDateTime

@Composable
fun ArticleCard(
    article: Article,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardSize = 120.dp

    Column(modifier = modifier) {
        Card(
            modifier = Modifier
                .height(cardSize)
                .clip(CardDefaults.shape)
                .clickable { onClick() },
            colors = CardDefaults.cardColors().copy(
                containerColor = TimezTheme.color.white.copy(alpha = 0.0f),
            ),
        ) {
            Row {
                AsyncImage(
                    modifier = Modifier
                        .size(cardSize)
                        .clip(CardDefaults.shape),
                    model = article.urlToImage,
                    error = painterResource(R.drawable.baseline_hide_image_24),
                    contentScale = ContentScale.Crop,
                    contentDescription = article.title,
                )
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(16.dp),
                ) {
                    article.title?.let {
                        Text(
                            it,
                            style = TimezTheme.typography.h14.copy(
                                fontWeight = FontWeight.Bold,
                            ),
                            color = TimezTheme.color.textColor,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    article.publishedAt?.let {
                        val relativeText = when (val relativeTime = it.formatRelativeTimeFromNow(LocalNowLocalDateTime.current.value)) {
                            is RelativeTime.Days -> "${relativeTime.days}${stringResource(R.string.days_ago)}"
                            is RelativeTime.Hours -> "${relativeTime.hours}${stringResource(R.string.hours_ago)}"
                        }
                        Text(
                            text = relativeText,
                            style = TimezTheme.typography.h12,
                            color = TimezTheme.color.textColor,
                        )
                    }
                }
            }
        }

        Gap(16.dp)

        HorizontalDivider(
            modifier = Modifier.padding(start = cardSize),
        )
    }
}

@Preview
@Composable
private fun PreviewArticleCard() {
    CompositionLocalProvider(
        LocalNowLocalDateTime provides FakeNowLocalDateTime,
    ) {
        TimezTheme {
            ArticleCard(
                article = Article.Default.copy(
                    title = "Elon Musk's X reprimanded after disinformation safety feature scrapped",
                    publishedAt = LocalDateTime(2000, 1, 1, 12, 0),
                ),
                onClick = {},
            )
        }
    }
}
