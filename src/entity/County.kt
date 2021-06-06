package com.ehtesham.entity

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object Countries : Table() {
    val countryCode: Column<String> = varchar("countryCode", 50)
    val fullName: Column<String> = varchar("fullName", 255)
    val fromYear: Column<Long> = long("fromYear")
    val toYear: Column<Long> = long("toYear")

    override val primaryKey = PrimaryKey(countryCode, name = "PK_Country_Code")

    fun toCountry(row: ResultRow): CountryResponse =
        CountryResponse(
            countryCode = row[Countries.countryCode],
            fullName = row[Countries.fullName],
            fromYear = row[Countries.fromYear],
            toYear = row[Countries.toYear]
        )
}

@Serializable
data class CountryResponse(
    val countryCode: String,
    val fullName: String,
    val fromYear: Long,
    val toYear: Long
)

@Serializable
data class Country(
    val countryCode: String,
//    val regions: List<String>,
//    val holidayTypes: List<String>,
    val fullName: String,
    val fromDate: Date,
    val toDate: Date
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
