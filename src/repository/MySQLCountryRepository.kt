package com.ehtesham.repository

import com.ehtesham.database.DatabaseManager
import com.ehtesham.entity.Country

class MySQLCountryRepository : CountryRepository {

    private val database = DatabaseManager()

    override fun getAllCountries(): List<Country> {
        return database.getAllCountries().map { Country(it.countryCode, it.fullName) }
    }

    override fun getCountry(countryCode: String): Country? {
        return database.getCountry(countryCode)?.let { Country(it.countryCode, it.fullName) }
    }

    override fun saveCountries(countries: List<Country>) {
        for(country in countries) {
            database.addCountries(country)
        }

    }
}