package io.github.b4tchkn.timez.data

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.b4tchkn.timez.data.repository.ArticleNavArgsRepository
import io.github.b4tchkn.timez.data.repository.DefaultNewsRepository
import io.github.b4tchkn.timez.data.repository.NavArgsRepository
import io.github.b4tchkn.timez.data.repository.NewsRepository
import io.github.b4tchkn.timez.model.Article

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    abstract fun bindNewsRepository(defaultNewsRepository: DefaultNewsRepository): NewsRepository

    @Binds
    abstract fun bindArticleNavArgsRepository(articleNavArgsRepository: ArticleNavArgsRepository): NavArgsRepository<Article>
}
