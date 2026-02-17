package xyz.catfootbeats.maiup.utils

fun convertUtcToPlus8(utcTimeStr: String): String {
    // UTC时间格式: 2026-02-13T15:39:13Z
    val pattern = Regex("""^(\d{4})-(\d{2})-(\d{2})T(\d{2}):(\d{2}):(\d{2})Z$""")
    val match = pattern.find(utcTimeStr) ?: return utcTimeStr
    
    val year = match.groupValues[1].toInt()
    val month = match.groupValues[2].toInt()
    val day = match.groupValues[3].toInt()
    val hour = match.groupValues[4].toInt()
    val minute = match.groupValues[5].toInt()
    val second = match.groupValues[6].toInt()
    
    // 加上8小时
    var newHour = hour + 8
    var newDay = day
    var newMonth = month
    var newYear = year
    
    // 处理跨天
    if (newHour >= 24) {
        newHour -= 24
        newDay++
        
        // 处理跨月
        val daysInMonth = when (newMonth) {
            2 -> if ((newYear % 4 == 0 && newYear % 100 != 0) || (newYear % 400 == 0)) 29 else 28
            4, 6, 9, 11 -> 30
            else -> 31
        }
        if (newDay > daysInMonth) {
            newDay = 1
            newMonth++
            
            // 处理跨年
            if (newMonth > 12) {
                newMonth = 1
                newYear++
            }
        }
    }
    
    // 格式化输出
    return "${newYear.toString().padStart(4, '0')}-${newMonth.toString().padStart(2, '0')}-${newDay.toString().padStart(2, '0')} ${newHour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}:${second.toString().padStart(2, '0')}"
}