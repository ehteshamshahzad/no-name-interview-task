package com.ehtesham

import com.ehtesham.entity.Countries
import com.ehtesham.entity.Country
import com.ehtesham.entity.Holiday
import com.ehtesham.entity.Holidays
import com.ehtesham.routes.root
import io.ktor.application.*
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

import kotlinx.coroutines.async

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    install(CallLogging)
    install(ContentNegotiation) {
        gson()
    }

    Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
    transaction {
        SchemaUtils.create(Countries)
        SchemaUtils.create(Holidays)

//        Countries.insert {
//            it[Countries.countryCode] = "pk"
//            it[Countries.fullName] = "Pakistan"
//        }
    }

    async {

        val data = transaction {
            Countries.selectAll().map { Countries.toCountry(it) }
        }

        val client = HttpClient(Apache) {
            install(JsonFeature) {
                serializer = GsonSerializer()
            }
        }

        if (data.isEmpty()) {

            val countries =
                client.request<List<Country>>("https://kayaposoft.com/enrico/json/v2.0/?action=getSupportedCountries")

            for (country in countries) {
                transaction {
                    Countries.insert {
                        it[Countries.countryCode] = country.countryCode
                        it[Countries.fullName] = country.fullName
                        it[Countries.fromYear] = country.fromDate.year
                        it[Countries.toYear] = country.toDate.year
                    }
                }
            }
        }


//        val holidays =
//            client.request<List<Holiday>>("https://kayaposoft.com/enrico/json/v2.0/?action=getHolidaysForYear&year=2022&country=est&holidayType=public_holiday")
//
//        for (holiday in holidays) {
//            transaction {
//                Holidays.insert {
//                    it[Holidays.date] = holiday.date.day
//                    it[Holidays.month] = holiday.date.month
//                    it[Holidays.year] = holiday.date.year
//                    it[Holidays.dayOfWeek] = holiday.date.dayOfWeek
//                    it[Holidays.name] = holiday.name[holiday.name.size-1].text
//                    it[Holidays.country]="est"//update
//                }
//            }
//        }

    }
//    val db = DatabaseFactory.create()
//    Database.connect(db)
//    install(FlywayFeature) {
//        dataSource = db
//    }

    routing {
        root()
    }
}