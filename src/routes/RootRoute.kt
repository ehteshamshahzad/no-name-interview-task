package com.ehtesham.routes

import com.ehtesham.entity.*
import io.ktor.application.*
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * This is our root function. Responsible for handling all calls
 */
fun Routing.root() {

    get("/") {
        call.respond("Welcome")
    }

    get("/holidays/{code}/{year}") {
        val countryCode = call.parameters["code"].toString()
        val year = call.parameters["year"]

        val countryHolidays = transaction {
            Holidays.select { (Holidays.country eq countryCode) and (Holidays.year eq year!!.toLong()) }
                .map { Holidays.toHoliday(it) }
        }

        if (countryHolidays.isNotEmpty()) {
            call.respond(groupByMonth(countryHolidays))

        } else {
            val client = HttpClient(Apache) {
                install(JsonFeature) {
                    serializer = GsonSerializer()
                }
            }

            val holidays =
                client.request<List<Holiday>>(
                    "https://kayaposoft.com/enrico/json/v2.0/?action=getHolidaysForYear&" +
                            "year=$year&country=$countryCode&holidayType=public_holiday"
                )

            for (holiday in holidays) {
                transaction {
                    Holidays.insert {
                        it[Holidays.date] = holiday.date.day
                        it[Holidays.month] = holiday.date.month
                        it[Holidays.year] = holiday.date.year
                        it[Holidays.dayOfWeek] = holiday.date.dayOfWeek
                        it[Holidays.name] = holiday.name[holiday.name.size - 1].text
                        it[Holidays.country] = countryCode//update
                    }
                }
            }

            val holidayResponse = transaction {
                Holidays.select { (Holidays.country eq countryCode) and (Holidays.year eq year!!.toLong()) }
                    .map { Holidays.toHoliday(it) }
            }
            call.respond(groupByMonth(holidayResponse))
        }
    }

    get("/countries") {
        val countries = transaction {
            Countries.selectAll().map { Countries.toCountry(it) }
        }
        call.respond(countries)
    }

    get("/country/{code}") {
        val countryCode = call.parameters["code"].toString()
        val country = transaction {
            Countries.select { Countries.countryCode eq countryCode }.map { Countries.toCountry(it) }
        }
        call.respond(country)
    }

    get("/streak/{code}/{year}") {
        val countryCode = call.parameters["code"].toString()
        val year = call.parameters["year"]

        val countryHolidays = transaction {
            Holidays.select { (Holidays.country eq countryCode) and (Holidays.year eq year!!.toLong()) }
                .map { Holidays.toHoliday(it) }
        }
        if (countryHolidays.isNotEmpty()) {
//            calculateMaxDays(countryHolidays)
            call.respond(Streak(calculateMaxDays(countryHolidays).maxOrNull()!!.toInt()))

        } else {
            val client = HttpClient(Apache) {
                install(JsonFeature) {
                    serializer = GsonSerializer()
                }
            }

            val holidays =
                client.request<List<Holiday>>(
                    "https://kayaposoft.com/enrico/json/v2.0/?action=getHolidaysForYear&" +
                            "year=$year&country=$countryCode&holidayType=public_holiday"
                )

            for (holiday in holidays) {
                transaction {
                    Holidays.insert {
                        it[Holidays.date] = holiday.date.day
                        it[Holidays.month] = holiday.date.month
                        it[Holidays.year] = holiday.date.year
                        it[Holidays.dayOfWeek] = holiday.date.dayOfWeek
                        it[Holidays.name] = holiday.name[holiday.name.size - 1].text
                        it[Holidays.country] = countryCode//update
                    }
                }
            }

            val holidayResponse = transaction {
                Holidays.select { (Holidays.country eq countryCode) and (Holidays.year eq year!!.toLong()) }
                    .map { Holidays.toHoliday(it) }
            }
            call.respond(Streak(calculateMaxDays(holidayResponse).maxOrNull()!!.toInt()))
        }
        // Missing case: If one holiday is on Friday, and other one is on the following Monday. So Friday, Saturday, Sunday, Monday.
    }
}

/**
 * We are grouping holidays by months.
 * @param [List<HolidayResponse>)]
 * @return [List<Month>]
 */
fun groupByMonth(countryHolidays: List<HolidayResponse>): List<Month> {
    var i: Long
    val list: MutableList<Month> = mutableListOf()
    var monthName: String
    var data: MutableList<Data> = mutableListOf()

    i = countryHolidays[0].month
    monthName = getMonthByCode(i.toInt()).toString()

    for (holiday in countryHolidays) {
        if (i == holiday.month) {
            data.add(Data(holiday.date, holiday.dayOfWeek, holiday.name))

        } else {
            list.add(Month(monthName, data))
            data = mutableListOf()
            i = holiday.month
            monthName = getMonthByCode(i.toInt()).toString()
            data.add(Data(holiday.date, holiday.dayOfWeek, holiday.name))
        }
    }
    list.add(Month(monthName, data))
    return list
}

/**
 * We are calculating the maximum number of free days
 * @param [List<HolidayResponse>)]
 * @return [MutableList<Int>]
 */
fun calculateMaxDays(holidays: List<HolidayResponse>): MutableList<Int> {
    var maxHolidays = 2
    val maxNum = mutableListOf<Int>()
    maxNum.add(maxHolidays)

    var index = 0
    var placeHolder = 2
    var oppositePlaceHolder = 4

    while (index != holidays.size) {
        if (holidays[index].dayOfWeek.toInt() == 1) {
            maxHolidays = 3
            var temp = index + 1
            while (temp < holidays.size) {
                if (holidays[temp].dayOfWeek.toInt() == placeHolder &&
                    holidays[temp - 1].month == holidays[temp].month
                ) {
                    placeHolder++
                    temp++
                    maxHolidays++
                } else break
            }
            maxNum.add(maxHolidays)
        }
        index++
    }


    index = 0
    while (index != holidays.size) {
        if (holidays[index].dayOfWeek.toInt() == 5) {
            maxHolidays = 3
            var temp = index - 1
            while (temp > 0) {
                if (holidays[temp].dayOfWeek.toInt() == oppositePlaceHolder
                    && holidays[temp + 1].month == holidays[temp].month
                ) {
                    oppositePlaceHolder--
                    temp--
                    maxHolidays++
                } else break
            }
            maxNum.add(maxHolidays)
        }
        index++
    }

    var newMax = 0
    index = 0
    while (index < holidays.size) {
        if (index + 1 != holidays.size) {
            if (holidays[index].date + 1 == holidays[index + 1].date &&
                holidays[index].month == holidays[index + 1].month
            ) {
                newMax++
            } else if (newMax > 0) {
                maxNum.add(newMax)
                newMax = 0
            }
        }
        index++
    }

    return maxNum
}