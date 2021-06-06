package com.ehtesham.entity

import kotlinx.serialization.Serializable

@Serializable
data class Month(
    val name: String,
    val data: List<Data>
)

@Serializable
data class Data(
    val date: Long,
    val dayOfWeek: Long,
    val name: String
)


fun getMonthByCode(month: Int): String? {
    if (month == 1) {
        return "Jan"
    }
    if (month == 2) {
        return "Feb"
    }
    if (month == 3) {
        return "March"
    }
    if (month == 4) {
        return "April"
    }
    if (month == 5) {
        return "May"
    }
    if (month == 6) {
        return "June"
    }
    if (month == 7) {
        return "July"
    }
    if (month == 8) {
        return "August"
    }
    if (month == 9) {
        return "Sept"
    }
    if (month == 10) {
        return "Oct"
    }
    if (month == 11) {
        return "Nov"
    }
    return if (month == 12) {
        "Dec"
    } else {
        null
    }
}
