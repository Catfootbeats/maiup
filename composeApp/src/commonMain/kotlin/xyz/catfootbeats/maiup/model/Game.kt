package xyz.catfootbeats.maiup.model

enum class Game {
    CHU,MAI
}

fun Game.getName(): String {
    return when(this){
        Game.CHU -> "chunithm"
        Game.MAI -> "maimai"
    }
}