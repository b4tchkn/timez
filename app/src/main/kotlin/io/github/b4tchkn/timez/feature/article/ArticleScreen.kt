package io.github.b4tchkn.timez.feature.article

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import io.github.b4tchkn.timez.R
import io.github.b4tchkn.timez.feature.article.ArticleUiModel.MessageState.NavigatePop
import io.github.b4tchkn.timez.ui.component.AppWebView
import io.github.b4tchkn.timez.ui.foundation.LaunchStateEffect

data class ArticleScreenNavArgs(
    val url: String,
)

@SuppressLint("ComposeModifierMissing")
@OptIn(ExperimentalMaterial3Api::class)
@Destination(navArgsDelegate = ArticleScreenNavArgs::class)
@Composable
fun ArticleScreen(
    navigator: DestinationsNavigator,
    viewModel: ArticleViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    viewModel.LaunchStateEffect(state.message, ArticleUiEvent.ClearMessage) {
        when (it) {
            NavigatePop -> navigator.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { viewModel.take(ArticleUiEvent.Pop) }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = stringResource(R.string.description_close),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxWidth(),
        ) {
            if (state.loading)
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth(),
                )
            ArticleScreen(
                content = state.content,
            )
        }
    }
}

@Composable
private fun ArticleScreen(
    content: ArticleUiModel.Content,
    modifier: Modifier = Modifier,
) {
    when (content) {
        is ArticleUiModel.Content.Default ->
            AppWebView(
                modifier = modifier
                    .fillMaxSize(),
                client = content.client,
                url = content.url,
            )
    }
}
