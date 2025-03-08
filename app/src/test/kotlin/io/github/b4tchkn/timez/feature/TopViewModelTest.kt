package io.github.b4tchkn.timez.feature

import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import app.cash.turbine.test
import io.github.b4tchkn.timez.fake.FakeNewsRepository
import io.github.b4tchkn.timez.fake.fakeNewsRepositoryException
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

class TopViewModelTest {
    @Test
    fun `on launch, top headlines are loaded`() = runTest {
        val newsRepository = FakeNewsRepository()
        val viewModel = TopViewModel(
            newsRepository = newsRepository,
        )

        moleculeFlow(mode = RecompositionMode.Immediate) {
            viewModel.presenter(emptyFlow())
        }.distinctUntilChanged().test {
            assertEquals(
                TopUiModel(loading = false, error = null, content = TopUiModel.Content.Empty),
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
        )
        val events = Channel<TopUiEvent>()

        moleculeFlow(mode = RecompositionMode.Immediate) {
            viewModel.presenter(events.receiveAsFlow())
        }.distinctUntilChanged().test {
            assertEquals(
                TopUiModel(loading = false, error = null, content = TopUiModel.Content.Empty),
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
                TopUiModel(loading = false, error = null, content = TopUiModel.Content.Default(articles = articles)),
                awaitItem(),
            )
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `on error, top headlines are not loaded`() = runTest {
        val newsRepository = FakeNewsRepository(shouldFail = true)
        val viewModel = TopViewModel(
            newsRepository = newsRepository,
        )
        val events = Channel<TopUiEvent>()

        moleculeFlow(mode = RecompositionMode.Immediate) {
            viewModel.presenter(events.receiveAsFlow())
        }.distinctUntilChanged().test {
            assertEquals(
                TopUiModel(loading = false, error = null, content = TopUiModel.Content.Empty),
                awaitItem(),
            )
            events.send(TopUiEvent.Refresh)
            assertEquals(
                TopUiModel(loading = false, error = fakeNewsRepositoryException, content = TopUiModel.Content.Empty),
                awaitItem(),
            )
        }
    }
}
