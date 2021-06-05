package com.ehtesham.entity

data class Countries (
    val countryCode: String,
    val regions: List<String>,
    val holidayTypes: List<HolidayType>,
    val fullName: String,
//    val fromDate: Date,
//    val toDate: Date
)

//data class Date (
//    val day: Long,
//    val month: Long,
//    val year: Long
//)

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
            "observance"        -> Observance
            "other_day"         -> OtherDay
            "postal_holiday"    -> PostalHoliday
            "public_holiday"    -> PublicHoliday
            "school_holiday"    -> SchoolHoliday
            else                -> throw IllegalArgumentException()
        }
    }
}
