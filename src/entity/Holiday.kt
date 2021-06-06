package com.ehtesham.entity

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object Holidays : Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val date: Column<Long> = long("date")
    val month: Column<Long> = long("month")
    val year: Column<Long> = long("year")
    val dayOfWeek: Column<Long> = long("dayOfWeek")
    val name: Column<String> = varchar("name", 255)
    val country: Column<String> = varchar("country", 55)
    override val primaryKey = PrimaryKey(id, name = "PK_ID")

    fun toHoliday(row: ResultRow): HolidayResponse =
        HolidayResponse(
            id = row[Holidays.id],
            date = row[Holidays.date],
            month = row[Holidays.month],
            year = row[Holidays.year],
            dayOfWeek = row[Holidays.dayOfWeek],
            name = row[Holidays.name],
            country = row[Holidays.country]
        )
}

@Serializable
class HolidayResponse(
    val id: Int,
    val date: Long,
    val month: Long,
    val year: Long,
    val dayOfWeek: Long,
    val name: String,
    val country: String
)


@Serializable
class Holiday(
    val date: Date,
    val name: List<Name>,
    val holidayType: String,
    val note: List<Note>,
    val flags: List<String>,
)

@Serializable
class Date(
    val day: Long,
    val month: Long,
    val year: Long,
    val dayOfWeek: Long
)

@Serializable
class Name(
    val lang: String,
    val text: String
)

@Serializable
class Note(
    val lang: String,
    val text: String
)