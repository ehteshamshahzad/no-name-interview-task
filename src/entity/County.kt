package com.ehtesham.entity

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object Countries : Table() {
    val countryCode: Column<String> = varchar("countryCode", 50)
    val fullName: Column<String> = varchar("fullName", 255)

    override val primaryKey = PrimaryKey(countryCode, name = "PK_Country_Code")

    fun toCountry(row: ResultRow): CountryResponse =
        CountryResponse(
            countryCode = row[Countries.countryCode],
            fullName = row[Countries.fullName]
        )
}

@Serializable
data class CountryResponse(
    val countryCode: String,
    val fullName: String
)

@Serializable
data class Country(
    val countryCode: String,
    val fullName: String,
    val fromDate: Date,
    val toDate: Date
)