package com.ehtesham.routes

import com.ehtesham.entity.Countries
import com.ehtesham.entity.Country
import com.ehtesham.entity.Holiday
import com.ehtesham.entity.Holidays
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

fun Routing.root() {

    get("/") {
    }

    get("/holidays/{code}/{year}") {
        val countryCode = call.parameters["code"].toString()
        val year = call.parameters["year"].toString()

        val countryHolidays = transaction {
            Holidays.select { Holidays.country eq countryCode and (Holidays.year eq year.toLong()) }
                .map { Holidays.toHoliday(it) }
        }

        if (countryHolidays.isNotEmpty()) {
            call.respond(countryHolidays)
        }

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
                    it[Holidays.country] = "est"//update
                }
            }
        }

        val holidayResponse = transaction {
            Holidays.selectAll().map { Holidays.toHoliday(it) }
        }
        call.respond(holidayResponse)
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
}