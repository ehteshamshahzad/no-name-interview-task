package com.ehtesham.entity

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

//object Countries : Table() {
//    val countryCode = varchar("countryCode", 50)
//    val fullName = varchar("fullName", 255)
//    override val primaryKey = PrimaryKey(countryCode)
//}


@Serializable
data class Country(
    val countryCode: String,
//    val regions: List<String>,
//    val holidayTypes: List<String>,
    val fullName: String,
//    val fromDate: Date,
//    val toDate: Date
)

@Serializable
data class Date(
    val day: Long,
    val month: Long,
    val year: Long
)

@Serializable
enum class HolidayType(val value: String) {
    ExtraWorkingDay("extra_working_day"),
    Observance("observance"),
    OtherDay("other_day"),
    PostalHoliday("postal_holiday"),
    PublicHoliday("public_holiday"),
    SchoolHoliday("school_holiday");

    companion object {
        public fun fromValue(value: String): HolidayType = when (value) {
            "extra_working_day" -> ExtraWorkingDay
            "observance" -> Observance
            "other_day" -> OtherDay
            "postal_holiday" -> PostalHoliday
            "public_holiday" -> PublicHoliday
            "school_holiday" -> SchoolHoliday
            else -> throw IllegalArgumentException()
        }
    }
}
