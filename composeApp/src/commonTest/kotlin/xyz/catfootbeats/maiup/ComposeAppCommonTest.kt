package xyz.catfootbeats.maiup

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import xyz.catfootbeats.maiup.api.HttpClientFactory
import kotlin.test.Test
import xyz.catfootbeats.maiup.api.LxnsApi
import xyz.catfootbeats.maiup.model.LevelIndex
import xyz.catfootbeats.maiup.model.Score
import xyz.catfootbeats.maiup.model.SongType

class ComposeAppCommonTest {

    @Test
    fun upload() {
        val factory = HttpClientFactory()
        val api = LxnsApi(factory.create())
        CoroutineScope(Dispatchers.IO).launch {
            api.uploadPlayerScores(
                "3YyTP5phXWKLzdEfCgX7VrkQn7i1tE9jVr_vycoSjEA=",
                listOf(Score(
                    id = 834,
                    level = "15",
                    level_index = LevelIndex.ReMASTER,
                    achievements = 101F,
                    dx_score = 101,
                    dx_star = 5,
                    type = SongType.STANDARD,
                )))
        }
    }
}