package io.github.b4tchkn.timez.domain.newsapi

interface UseCase<in T, out R> {
    suspend fun invoke(param: T): Result<R>
}
