package io.github.b4tchkn.timez.feature

import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import app.cash.turbine.test
import io.github.b4tchkn.timez.fake.FakeArticleNavArgsRepository
import io.github.b4tchkn.timez.feature.newsdetail.NewsDetailUiModel
import io.github.b4tchkn.timez.feature.newsdetail.NewsDetailViewModel
import io.github.b4tchkn.timez.model.Article
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewsDetailViewModelTest {
    @Test
    fun `on launch, selected news are loaded`() = runTest {
        val viewModel = NewsDetailViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf("articleId" to "id1"),
            ),
            articleNavArgsRepository = FakeArticleNavArgsRepository().apply {
                data["id1"] = Article.Default
            },
        )

        moleculeFlow(mode = RecompositionMode.Immediate) {
            viewModel.presenter(emptyFlow())
        }.distinctUntilChanged().test {
            assertEquals(
                NewsDetailUiModel(
                    loading = false,
                    content = NewsDetailUiModel.Content.Empty,
                    message = null,
                ),
                awaitItem(),
            )

            assertEquals(
                NewsDetailUiModel(
                    loading = false,
                    content = NewsDetailUiModel.Content.Default(Article.Default),
                    message = null,
                ),
                awaitItem(),
            )
        }
    }

    @Test
    fun `on launch, selected news are not loaded`() = runTest {
        val viewModel = NewsDetailViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf("articleId" to "id1"),
            ),
            articleNavArgsRepository = FakeArticleNavArgsRepository(),
        )

        moleculeFlow(mode = RecompositionMode.Immediate) {
            viewModel.presenter(emptyFlow())
        }.distinctUntilChanged().test {
            assertEquals(
                NewsDetailUiModel(
                    loading = false,
                    content = NewsDetailUiModel.Content.Empty,
                    message = null,
                ),
                awaitItem(),
            )
        }
    }

    @Test
    fun `on launch, selected news cannot be loaded error`() = runTest {
        val viewModel = NewsDetailViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf("articleId" to "id1"),
            ),
            articleNavArgsRepository = FakeArticleNavArgsRepository(shouldFail = true),
        )

        moleculeFlow(mode = RecompositionMode.Immediate) {
            viewModel.presenter(emptyFlow())
        }.distinctUntilChanged().test {
            assertEquals(
                NewsDetailUiModel(
                    loading = false,
                    content = NewsDetailUiModel.Content.Empty,
                    message = null,
                ),
                awaitItem(),
            )

            assertEquals(
                NewsDetailUiModel(
                    loading = false,
                    content = NewsDetailUiModel.Content.Empty,
                    message = NewsDetailUiModel.MessageState.Error,
                ),
                awaitItem(),
            )
        }
    }
}
