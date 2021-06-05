package com.ehtesham

import com.ehtesham.entity.LoginRequest
import com.ehtesham.entity.LoginResponse
import com.ehtesham.routes.consume
import com.ehtesham.routes.root
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {


    routing {
//        get("/test"){
//            call.respondText { "Testing" }
//        }
//
//        post("/test"){
//            call.respondText { "post request" }
//        }
//
//        post("/testt"){
//            val parameters = call.receiveParameters()
//
//            val paramValue1 = parameters["param1"]
//            val paramValue2 = parameters["param2"] ?: "DEFAULT"
//            call.respondText("This is a test POST request with parameter values $paramValue1 and $paramValue2")
//        }


        install(ContentNegotiation) {
            gson { }
        }

        this.root()
        this.consume()


        get("/test") {
            call.respondText("This is a test")
        }

        post("/login") {
            val loginRequest = call.receive<LoginRequest>()

            if (loginRequest.username == "admin" && loginRequest.password == "adminpw") {
                call.respond(LoginResponse(true, "Login successful!"))
            } else {
                call.respond(LoginResponse(false, "Credentials are invalid!"))
            }
        }

        get("/todo/{id}") {
            val id = call.parameters["id"]
            call.respondText("$id")
        }


    }

}