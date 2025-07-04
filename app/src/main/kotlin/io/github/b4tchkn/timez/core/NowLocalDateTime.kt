package io.github.b4tchkn.timez.core

import androidx.compose.runtime.staticCompositionLocalOf
import kotlin.time.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

interface NowLocalDateTime {
    val value: LocalDateTime
}

object DefaultNowLocalDateTime : NowLocalDateTime {
    override val value: LocalDateTime
        @OptIn(kotlin.time.ExperimentalTime::class)
        get() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
}

object FakeNowLocalDateTime : NowLocalDateTime {
    override val value: LocalDateTime
        get() = LocalDateTime(2021, 5, 1, 0, 0)
}

val LocalNowLocalDateTime = staticCompositionLocalOf<NowLocalDateTime> {
    error("No NowLocalDateTime provided")
}
