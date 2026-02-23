package xyz.catfootbeats.maiup.model

enum class Game {
    CHU,MAI
}

fun Game.getApiName(): String {
    return when(this){
        Game.CHU -> "chunithm"
        Game.MAI -> "maimai"
    }
}

fun Game.getName(): String {
    return when(this){
        Game.CHU -> "中二节奏"
        Game.MAI -> "舞萌DX"
    }
}
