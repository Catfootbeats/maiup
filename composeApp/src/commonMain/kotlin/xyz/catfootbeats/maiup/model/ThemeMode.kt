package xyz.catfootbeats.maiup.model

enum class ThemeMode {
    SYSTEM,LIGHT,DARK;
    companion object{
        fun fromInt(code: Int): ThemeMode{
            return when(code){
                0 -> SYSTEM
                1 -> LIGHT
                2 -> DARK
                else -> SYSTEM
            }
        }
        fun getList(): List<ThemeMode>{
            return listOf(
                SYSTEM,
                LIGHT,
                DARK
            )
        }
    }
}

fun ThemeMode.getName(): String{
    return when (this) {
        ThemeMode.SYSTEM -> "跟随系统"
        ThemeMode.LIGHT -> "浅色模式"
        ThemeMode.DARK -> "深色模式"
    }
}
