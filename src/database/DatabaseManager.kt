package com.ehtesham.database

import com.ehtesham.entity.Country
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.insert
import org.ktorm.dsl.update
import org.ktorm.entity.firstOrNull
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.toList
import org.ktorm.support.mysql.insertOrUpdate

class DatabaseManager {

    private val hostname = "127.0.0.1:3306/ktor_task"
    private val databaseName = "sys"
    private val username = "root"
    private val password = "password"

    private val ktorMDatabase: Database

    init {
        ktorMDatabase =
            Database.connect("jdbc:mysql://$hostname:3306/$databaseName?user=$username&password=$password&useSSL=false")
    }

//    private val ktorMDatabase: Database = Database.connect(
//        url = "jdbc:mysql://localhost:3306/ktorm",
//        driver = "com.mysql.cj.jdbc.Driver",
//        user = "root",
//        password = "password")

    fun getAllCountries(): List<DBCountryEntity> {
        return ktorMDatabase.sequenceOf(DBCountryTable).toList()
    }

    fun getCountry(countryCode: String): DBCountryEntity? {
        return ktorMDatabase.sequenceOf(DBCountryTable).firstOrNull { it.countryCode eq countryCode }
    }

    fun addCountries(country: Country): Country {
        val insertedData = ktorMDatabase.insertOrUpdate(DBCountryTable) {
            set(DBCountryTable.countryCode, country.countryCode)
            set(DBCountryTable.fullName, country.fullName)
        }
        return Country(country.countryCode, country.fullName)
    }
}