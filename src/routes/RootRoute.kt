package com.ehtesham.routes

import com.ehtesham.entity.Country
import com.ehtesham.repository.CountryRepository
import com.ehtesham.repository.InMemoryCountryRepository
import com.ehtesham.repository.MySQLCountryRepository
import com.ehtesham.repository.countryStorage
import io.ktor.application.*
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Routing.root() {

    val repository: CountryRepository = InMemoryCountryRepository()

    val client = HttpClient(Apache) {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
    }

//    get("/") {
//        if (countryStorage.isEmpty()) {
//            countryStorage =
//                client.request<List<Country>>("https://kayaposoft.com/enrico/json/v2.0/?action=getSupportedCountries")
//                    .toMutableList()
//        }
//        call.respond(countryStorage)
////        call.respondText("Hi!")
//    }

    get("/") {
        if (repository.getAllCountries().isEmpty()) {
            repository.saveCountries(client.request<List<Country>>("https://kayaposoft.com/enrico/json/v2.0/?action=getSupportedCountries"))
        }
//        if(countryStorage.isEmpty()){
//            countryStorage = client.request<MutableList<Country>>("https://kayaposoft.com/enrico/json/v2.0/?action=getSupportedCountries")
//        }
        call.respond(repository.getAllCountries())
    }

    get("/countries") {
        call.respond(repository.getAllCountries())
    }

    get("/country/{code}") {
        val countryCode = call.parameters["code"]

        // TODO: encapsulate it with if else, to check for null. And remove !!, !!.
        call.respond(repository.getCountry(countryCode!!)!!)
    }
}