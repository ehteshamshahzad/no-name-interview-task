package com.ehtesham.routes

import com.ehtesham.entity.Countries
import io.ktor.application.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Routing.consume() {
    val client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
    }

    get("/consume") {
        val anotherThing =
            client.get<Countries>("https://kayaposoft.com/enrico/json/v2.0/?action=getSupportedCountries")
//        call.respond(anotherThing)
        client.close()
    }
}