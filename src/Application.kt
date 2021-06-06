package com.ehtesham

import com.ehtesham.routes.root
//import com.ehtesham.service.DatabaseFactory
import com.viartemev.ktor.flyway.FlywayFeature
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.Database

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

//    val db = DatabaseFactory.create()
//    Database.connect(db)
//    install(FlywayFeature) {
//        dataSource = db
//    }

    routing {

        root()
    }

}