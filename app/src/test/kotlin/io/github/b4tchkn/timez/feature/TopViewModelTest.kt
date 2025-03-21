package io.github.b4tchkn.timez.feature

import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import app.cash.turbine.test
import io.github.b4tchkn.timez.fake.FakeArticleNavArgsRepository
import io.github.b4tchkn.timez.fake.FakeNewsRepository
import io.github.b4tchkn.timez.feature.top.TopUiEvent
import io.github.b4tchkn.timez.feature.top.TopUiModel
import io.github.b4tchkn.timez.feature.top.TopViewModel
import io.github.b4tchkn.timez.model.Article
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TopViewModelTest {
    @Test
    fun `on launch, top headlines are loaded`() = runTest {
        val viewModel = TopViewModel(
            newsRepository = FakeNewsRepository(),
            articleNavArgsRepository = FakeArticleNavArgsRepository(),
        )

        moleculeFlow(mode = RecompositionMode.Immediate) {
            viewModel.presenter(emptyFlow())
        }.distinctUntilChanged().test {
            assertEquals(
                TopUiModel(loading = false, content = null, message = null),
                awaitItem(),
            )
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `on receive refresh event, top headlines are reloaded`() = runTest {
        val newsRepository = FakeNewsRepository()
        val viewModel = TopViewModel(
            newsRepository = newsRepository,
            articleNavArgsRepository = FakeArticleNavArgsRepository(),
        )
        val events = Channel<TopUiEvent>()

        moleculeFlow(mode = RecompositionMode.Immediate) {
            viewModel.presenter(events.receiveAsFlow())
        }.distinctUntilChanged().test {
            assertEquals(
                TopUiModel(loading = false, content = null, message = null),
                awaitItem(),
            )
            skipItems(1)
            val articles = listOf(
                Article.Default.copy(title = "title1"),
                Article.Default.copy(title = "title2"),
                Article.Default.copy(title = "title3"),
            )
            newsRepository.articles.add(articles)
            events.send(TopUiEvent.Refresh)
            assertEquals(
                TopUiModel(loading = false, content = TopUiModel.Content.Default(articles = articles), message = null),
                awaitItem(),
            )
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `on error, top headlines are not loaded`() = runTest {
        val viewModel = TopViewModel(
            newsRepository = FakeNewsRepository(shouldFail = true),
            articleNavArgsRepository = FakeArticleNavArgsRepository(),
        )
        val events = Channel<TopUiEvent>()

        moleculeFlow(mode = RecompositionMode.Immediate) {
            viewModel.presenter(events.receiveAsFlow())
        }.distinctUntilChanged().test {
            assertEquals(
                TopUiModel(loading = false, content = null, message = null),
                awaitItem(),
            )
            events.send(TopUiEvent.Refresh)
            assertEquals(
                TopUiModel(loading = false, content = TopUiModel.Content.Empty, message = TopUiModel.MessageState.Error),
                awaitItem(),
            )
        }
    }
}
